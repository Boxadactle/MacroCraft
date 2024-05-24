package dev.boxadactle.macrocraft.neoforge;

import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.MacroCraftKeybinds;
import dev.boxadactle.macrocraft.gui.MacroListScreen;
import dev.boxadactle.macrocraft.hud.MacroPlayHud;
import dev.boxadactle.macrocraft.hud.MacroRecordHud;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = MacroCraft.MOD_ID, value = Dist.CLIENT)
public class MacroCraftEvents {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent ignored) {
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

    @EventBusSubscriber(modid = MacroCraft.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
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
