package dev.boxadactle.macrocraft.listeners;

import com.mojang.blaze3d.platform.InputConstants;
import dev.boxadactle.macrocraft.MacroCraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InputConstants.class)
public class InputConstantsMixin {

    @Inject(
            method = "grabOrReleaseMouse",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void ignoreMouseGrabbing(long l, int i, double d, double e, CallbackInfo ci) {
        if (MacroCraft.shouldIgnoreInput()) {
            ci.cancel();
        }
    }

}
