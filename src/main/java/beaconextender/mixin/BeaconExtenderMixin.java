package beaconextender.mixin;

import beaconextender.BeaconExtender;
import beaconextender.BeaconExtenderConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(BeaconBlockEntity.class)
public class BeaconExtenderMixin {
	@Inject(at = @At("HEAD"), method = "applyEffects", cancellable = true)
	private static void applyEffects(Level level, BlockPos blockPos, int beaconLevel, @Nullable Holder<MobEffect> primaryEffect, @Nullable Holder<MobEffect> secondaryEffect, CallbackInfo ci) {
		// Cancel default action
		ci.cancel();
		if (!level.isClientSide && primaryEffect != null) {
			double distance = BeaconExtender.CONFIG.getDistance(beaconLevel);

			int effectAmplifier = 0;
			if (beaconLevel >= 4 && Objects.equals(primaryEffect, secondaryEffect)) {
				effectAmplifier = 1;
			}

			int effectDuration = (9 + beaconLevel * 2) * 20;
			AABB aABB = (new AABB(blockPos)).inflate(distance).expandTowards((double) 0.0F, (double) level.getHeight(), (double) 0.0F);
			List<Player> list = level.getEntitiesOfClass(Player.class, aABB);

			for (Player player : list) {
				player.addEffect(new MobEffectInstance(primaryEffect, effectDuration, effectAmplifier, true, true));
			}

			if (beaconLevel >= 4 && !Objects.equals(primaryEffect, secondaryEffect) && secondaryEffect != null) {
				for (Player player : list) {
					player.addEffect(new MobEffectInstance(secondaryEffect, effectDuration, 0, true, true));
				}
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "updateBase", cancellable = true)
	private static void updateBase(Level world, int baseX, int baseY, int baseZ, CallbackInfoReturnable<Integer> ci) {
		int MAX_LAYERS = BeaconExtender.CONFIG.getMaxLayers();

		int validLayers = 0;

		for (int layer = 1; layer <= MAX_LAYERS; validLayers = layer++) {
			int y = baseY - layer;
			if (y < world.getMinY()) {
				break;
			}

			boolean isLayerValid = true;

			for (int x = baseX - layer; x <= baseX + layer && isLayerValid; ++x) {
				for (int z = baseZ - layer; z <= baseZ + layer; ++z) {
					if (!world.getBlockState(new BlockPos(x, y, z)).is(BlockTags.BEACON_BASE_BLOCKS)) {
						isLayerValid = false;
						break;
					}
				}
			}

			if (!isLayerValid) {
				break;
			}
		}
		ci.setReturnValue(validLayers);
	}
}