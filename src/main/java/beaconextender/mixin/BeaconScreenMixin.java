package beaconextender.mixin;

import beaconextender.BeaconExtender;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.BeaconMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeaconScreen.class)
public class BeaconScreenMixin {
    @Unique
    private static final String ACTIVE_LAYERS_LABEL = "block.minecraft.beacon.beaconextender.active_layers";

    @Unique
    private static final String INACTIVE_LABEL = "block.minecraft.beacon.beaconextender.inactive";

    @Inject(at = @At("HEAD"), method = "renderLabels")
    private void renderLabels(GuiGraphics guiGraphics, int i, int j, CallbackInfo ci) {
        if (BeaconExtender.CONFIG.showBeaconLayers()) {
            BeaconScreen instance = (BeaconScreen) (Object) this;
            Font font = instance.getFont();

            BeaconMenu menu = instance.getMenu();
            int layers = menu.getLevels();

            if (layers == 0) {
                guiGraphics.drawCenteredString(font, Component.translatable(INACTIVE_LABEL), 169, 80, 14737632);
            } else {
                guiGraphics.drawCenteredString(font, Component.translatable(ACTIVE_LAYERS_LABEL, layers), 169, 80, 14737632);
            }
        }
    }
}
