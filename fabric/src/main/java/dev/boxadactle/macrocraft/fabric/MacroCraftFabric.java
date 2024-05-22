package dev.boxadactle.macrocraft.fabric;

import dev.boxadactle.boxlib.fabric.command.BCommandManager;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.MacroCraftKeybinds;
import dev.boxadactle.macrocraft.fabric.command.ConfigCommand;
import dev.boxadactle.macrocraft.fabric.command.PlayCommand;
import dev.boxadactle.macrocraft.fabric.command.RecordCommand;
import dev.boxadactle.macrocraft.fabric.command.SaveCommand;
import dev.boxadactle.macrocraft.gui.MacroListScreen;
import dev.boxadactle.macrocraft.hud.MacroPlayHud;
import dev.boxadactle.macrocraft.hud.MacroRecordHud;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class MacroCraftFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MacroCraft.init();

        ClientTickEvents.END_CLIENT_TICK.register(this::tick);

        HudRenderCallback.EVENT.register(this::renderMacroHud);

        initKeybinds();
        initCommands();
    }

    private void initKeybinds() {
        KeyBindingHelper.registerKeyBinding(MacroCraftKeybinds.hideGui);
        KeyBindingHelper.registerKeyBinding(MacroCraftKeybinds.pauseMacro);
        KeyBindingHelper.registerKeyBinding(MacroCraftKeybinds.playMacro);
        KeyBindingHelper.registerKeyBinding(MacroCraftKeybinds.stopMacro);
        KeyBindingHelper.registerKeyBinding(MacroCraftKeybinds.openMacroList);
    }

    private void initCommands() {
        BCommandManager.registerCommand("macro", (list) -> {
            list.add(ConfigCommand::new);
            list.add(PlayCommand::new);
            list.add(RecordCommand::new);
            list.add(SaveCommand::new);
        });
    }

    private void tick(Minecraft client) {
        MacroState.tick();

        if (MacroCraftKeybinds.openMacroList.consumeClick()) {
            client.setScreen(new MacroListScreen(null));
        }
    }

    private void renderMacroHud(GuiGraphics graphics, float tickDelta) {
        if (MacroState.LOADED_MACRO.shouldRenderHud()) {
            MacroPlayHud.render(graphics);
        }

        if (MacroState.shouldRenderHud()) {
            MacroRecordHud.render(graphics);
        }
    }
}