package beaconextender;

import com.sun.jna.platform.win32.Winnetwk;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class BeaconExtender implements ModInitializer {
	public static final String MOD_ID = "beaconextender";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static BeaconExtenderConfig CONFIG = BeaconExtenderConfig.load();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		registerReloadListener();
	}

	private void loadConfiguration() {
		LOGGER.info("Loading configuration...");
        CONFIG = BeaconExtenderConfig.load();
	}

	private void registerReloadListener() {
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
			@Override
			public @NotNull CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, Executor executor, Executor executor2) {
				CompletableFuture<Void> future =  CompletableFuture.runAsync(() -> {
					loadConfiguration();
				}, executor2);

				return preparationBarrier.wait(future).thenCompose(f -> f);
			}

			@Override
			public ResourceLocation getFabricId() {
				return ResourceLocation.fromNamespaceAndPath(MOD_ID, "config_reloader");
			}
		});
	}
}