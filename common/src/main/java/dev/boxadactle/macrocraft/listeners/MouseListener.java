package dev.boxadactle.macrocraft.listeners;

import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.macro.MacroState;
import dev.boxadactle.macrocraft.macro.action.MouseButtonAction;
import dev.boxadactle.macrocraft.macro.action.MousePositionAction;
import dev.boxadactle.macrocraft.macro.action.MouseScrollAction;
import net.minecraft.client.MouseHandler;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseListener {

    @Inject(
            method = "onMove",
            at = @At("HEAD")
    )
    private void captureMove(long l, double d, double e, CallbackInfo ci) {
        if (MacroState.IS_RECORDING) {
            MacroState.addAction(new MousePositionAction(MacroState.ticksElapsed, d, e));
        }
    }

    @Inject(
            method = "onPress",
            at = @At("HEAD")
    )
    private void capturePress(long l, int i, int j, int k, CallbackInfo ci) {
        if (MacroState.IS_RECORDING) {
            MacroState.addAction(new MouseButtonAction(MacroState.ticksElapsed, i, j, k));
        }
    }

    @Inject(
            method = "onScroll",
            at = @At("HEAD")
    )
    private void captureScroll(long l, double d, double e, CallbackInfo ci) {
        if (MacroState.IS_RECORDING) {
            MacroState.addAction(new MouseScrollAction(MacroState.ticksElapsed, d, e));
        }
    }

    // we block GLFW from handling the mouse input instead of cancelling
    // the minecraft methods when a macro is playing
    // so our own code can still run
    @ModifyArg(
            method = "setup",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/InputConstants;setupMouseCallbacks(JLorg/lwjgl/glfw/GLFWCursorPosCallbackI;Lorg/lwjgl/glfw/GLFWMouseButtonCallbackI;Lorg/lwjgl/glfw/GLFWScrollCallbackI;Lorg/lwjgl/glfw/GLFWDropCallbackI;)V"
            ),
            index = 1
    )
    private GLFWCursorPosCallbackI ignoreGLFWMouseMove(GLFWCursorPosCallbackI gLFWCursorPosCallbackI) {
        return (l, d, e) -> {
            if (!MacroCraft.shouldIgnoreInput()) {
                gLFWCursorPosCallbackI.invoke(l, d, e);
            }
        };
    }

    @ModifyArg(
            method = "setup",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/InputConstants;setupMouseCallbacks(JLorg/lwjgl/glfw/GLFWCursorPosCallbackI;Lorg/lwjgl/glfw/GLFWMouseButtonCallbackI;Lorg/lwjgl/glfw/GLFWScrollCallbackI;Lorg/lwjgl/glfw/GLFWDropCallbackI;)V"
            ),
            index = 2
    )
    private GLFWMouseButtonCallbackI ignoreGLFWMousePress(GLFWMouseButtonCallbackI gLFWMouseButtonCallbackI) {
        return (l, i, j, k) -> {
            if (!MacroCraft.shouldIgnoreInput()) {
                gLFWMouseButtonCallbackI.invoke(l, i, j, k);
            }
        };
    }

    @ModifyArg(
            method = "setup",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/InputConstants;setupMouseCallbacks(JLorg/lwjgl/glfw/GLFWCursorPosCallbackI;Lorg/lwjgl/glfw/GLFWMouseButtonCallbackI;Lorg/lwjgl/glfw/GLFWScrollCallbackI;Lorg/lwjgl/glfw/GLFWDropCallbackI;)V"
            ),
            index = 3
    )
    private GLFWScrollCallbackI ignoreGLFWMouseScroll(GLFWScrollCallbackI gLFWScrollCallbackI) {
        return (l, d, e) -> {
            if (!MacroCraft.shouldIgnoreInput()) {
                gLFWScrollCallbackI.invoke(l, d, e);
            }
        };
    }

}
