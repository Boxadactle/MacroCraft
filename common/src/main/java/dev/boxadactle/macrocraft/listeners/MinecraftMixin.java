package dev.boxadactle.macrocraft.listeners;

import dev.boxadactle.macrocraft.MacroCraft;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(
            method = "isWindowActive",
            at = @At("HEAD"),
            cancellable = true
    )
    private void aVoid(CallbackInfoReturnable<Boolean> cir) {
        if (MacroCraft.shouldIgnoreInput()) {
            cir.setReturnValue(true);
        }
    }

}
