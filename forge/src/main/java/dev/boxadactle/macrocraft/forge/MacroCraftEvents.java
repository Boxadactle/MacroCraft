package dev.boxadactle.macrocraft.forge;

import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.MacroCraftKeybinds;
import dev.boxadactle.macrocraft.gui.MacroListScreen;
import dev.boxadactle.macrocraft.hud.MacroPlayHud;
import dev.boxadactle.macrocraft.hud.MacroRecordHud;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MacroCraft.MOD_ID, value = Dist.CLIENT)
public class MacroCraftEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent ignored) {
        MacroState.tick();
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void renderMacroHud(RenderGuiEvent.Pre event) {
        if (MacroState.LOADED_MACRO.shouldRenderHud()) {
            MacroPlayHud.render(event.getGuiGraphics());
        }

        if (MacroState.shouldRenderHud()) {
            MacroRecordHud.render(event.getGuiGraphics());
        }
    }

    @SubscribeEvent
    public static void keyInput(InputEvent.Key e) {
        if (MacroCraftKeybinds.openMacroList.consumeClick()) {
            ClientUtils.setScreen(new MacroListScreen(null));
        }
    }

    @Mod.EventBusSubscriber(modid = MacroCraft.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerKeyBindings(RegisterKeyMappingsEvent e) {
            e.register(MacroCraftKeybinds.playMacro);
            e.register(MacroCraftKeybinds.pauseMacro);
            e.register(MacroCraftKeybinds.stopMacro);
            e.register(MacroCraftKeybinds.hideGui);
            e.register(MacroCraftKeybinds.openMacroList);
        }

    }

}
