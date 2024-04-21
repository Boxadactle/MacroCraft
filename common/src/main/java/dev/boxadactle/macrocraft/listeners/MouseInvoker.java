package dev.boxadactle.macrocraft.listeners;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MouseHandler.class)
public interface MouseInvoker {

    @Invoker("onPress")
    void invokeMousePress(long l, int i, int j, int k);

    @Invoker("onMove")
    void invokeMove(long l, double d, double e);

    @Invoker("onScroll")
    void invokeScroll(long l, double d, double e);

}
