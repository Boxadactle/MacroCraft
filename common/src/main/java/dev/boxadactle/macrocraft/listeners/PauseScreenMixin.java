package dev.boxadactle.macrocraft.listeners;

import dev.boxadactle.macrocraft.gui.MacroListScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

// yes i delete the send feedback button in pause screen
// but who actually clicks it
@Mixin(PauseScreen.class)
@SuppressWarnings("unchecked")
public abstract class PauseScreenMixin extends Screen {

    @Shadow protected abstract Button openScreenButton(Component arg, Supplier<Screen> supplier);

    protected PauseScreenMixin(Component component) {
        super(component);
    }

    @ModifyArg(
            method = "createPauseMenu",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;",
                    ordinal = 2
            )
    )
    private <T extends LayoutElement> T removeSendFeedbackButton(T arg) {
        return (T) this.openScreenButton(Component.translatable("button.macrocraft.macros"), () -> new MacroListScreen(this));
    }

}
