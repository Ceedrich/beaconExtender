package beaconextender;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.impl.resource.v1.FabricResourceReloader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeaconExtender implements ModInitializer {
  public static final String MOD_ID = "beaconextender";

  // This logger is used to write text to the console and the log file.
  // It is considered best practice to use your mod id as the logger's name.
  // That way, it's clear which mod wrote info, warnings, and errors.
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  @Override
  public void onInitialize() {
    // This code runs as soon as Minecraft is in a mod-load-ready state.
    // However, some things (like resources) may still be uninitialized.
    // Proceed with mild caution.
    BeaconExtenderConfig.HANDLER.load();
    BeaconExtenderConfig.HANDLER.save();

    registerReloadListener();
  }

  private void registerReloadListener() {
    class DataReloader implements FabricResourceReloader {
		@Override
		public CompletableFuture<Void> reload(SharedState sharedState, Executor executor, PreparationBarrier preparationBarrier, Executor executor2) {
			CompletableFuture<Void> future = CompletableFuture.runAsync(
					() -> { BeaconExtenderConfig.HANDLER.load(); }, executor2);

			return preparationBarrier.wait(future).thenCompose(f -> f);
		}

		@Override
		public ResourceLocation fabric$getId() {
			return ResourceLocation.fromNamespaceAndPath(MOD_ID, "config_reloader");
		}
    };

    ResourceLoader.get(PackType.SERVER_DATA)
        .registerReloader(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "config_reloader"),
            new DataReloader());
  }
}
