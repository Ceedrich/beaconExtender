package beaconextender.mixin;

import beaconextender.BeaconExtender;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.BeaconMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(BeaconScreen.class)
public class BeaconScreenMixin {
    @Unique
    private static final Component LAYERS_LABEL = Component.translatable("block.minecraft.beacon.bannerextender.layers");

    @Inject(at = @At("HEAD"), method = "renderLabels")
    private void renderLabels(GuiGraphics guiGraphics, int i, int j, CallbackInfo ci) {
        if (BeaconExtender.CONFIG.showBeaconLayers()) {
            BeaconScreen instance = (BeaconScreen) (Object) this;
            Font font = instance.getFont();

            BeaconMenu menu = instance.getMenu();
            int layers = menu.getLevels();

            guiGraphics.drawCenteredString(font, LAYERS_LABEL.getString() + layers, 169, 80, 14737632);
        }
    }
}
