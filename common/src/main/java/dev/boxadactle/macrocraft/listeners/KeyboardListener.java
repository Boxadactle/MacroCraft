package dev.boxadactle.macrocraft.listeners;

import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.MacroCraftKeybinds;
import dev.boxadactle.macrocraft.hud.MacroPlayHud;
import dev.boxadactle.macrocraft.hud.MacroRecordHud;
import dev.boxadactle.macrocraft.macro.MacroState;
import dev.boxadactle.macrocraft.macro.action.KeyboardAction;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.gui.screens.ChatScreen;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardListener {

    @Inject(
            method = "keyPress",
            at = @At("HEAD")
    )
    private void recordMacro(long l, int i, int j, int k, int m, CallbackInfo ci) {
        if (MacroState.IS_RECORDING) {
            if (
                    (ClientUtils.getCurrentScreen() instanceof ChatScreen && MacroCraft.CONFIG.get().ignoreChatTyping)
                    || i == ((KeyAccessor)ClientUtils.getOptions().keyChat).getKey().getValue()
            ) {
                MacroCraft.LOGGER.info("Ignoring KeyboardAction due to chat typing.");
                return;
            }

            if (MacroCraftKeybinds.shouldIgnoreInput(i)) {
                MacroCraft.LOGGER.info("Ignoring KeyboardAction due to keybind.");
                return;
            }

            if (i == 256) {
                MacroCraft.LOGGER.info("Ignoring KeyboardAction due to escape key.");
                return;
            }

            MacroState.addAction(new KeyboardAction(MacroState.ticksElapsed, i, j, k, m));
        }
    }

    // we block GLFW from handling the keyboard input instead of cancelling
    // the minecraft methods when a macro is playing
    // so our own code can still run
    @ModifyArg(
            method = "setup",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/InputConstants;setupKeyboardCallbacks(JLorg/lwjgl/glfw/GLFWKeyCallbackI;Lorg/lwjgl/glfw/GLFWCharModsCallbackI;)V"
            ),
            index = 1
    )
    private GLFWKeyCallbackI ignoreGLFWKeyboard(GLFWKeyCallbackI gLFWKeyCallbackI) {
        return (l, i, j, k, m) -> {
            if (MacroState.LOADED_MACRO.shouldRenderHud()) {
                MacroPlayHud.checkKeys(i);
            }

            if (MacroState.shouldRenderHud()) {
                MacroRecordHud.checkKeys(i);
            }

            if (!MacroCraft.shouldIgnoreInput()) {
                gLFWKeyCallbackI.invoke(l, i, j, k, m);
            }
        };
    }

}
