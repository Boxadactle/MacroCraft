package dev.boxadactle.macrocraft.forge;

import dev.boxadactle.boxlib.forge.command.BCommandManager;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.config.MacroCraftConfigScreen;
import dev.boxadactle.macrocraft.forge.command.ConfigCommand;
import dev.boxadactle.macrocraft.forge.command.PlayCommand;
import dev.boxadactle.macrocraft.forge.command.RecordCommand;
import dev.boxadactle.macrocraft.forge.command.SaveCommand;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(MacroCraft.MOD_ID)
public class MacroCraftForge {
    public MacroCraftForge() {
        MacroCraft.init();

        BCommandManager.registerCommand("macrocraft", list -> {
            list.add(ConfigCommand::new);
            list.add(PlayCommand::new);
            list.add(RecordCommand::new);
            list.add(SaveCommand::new);
        });

        // what a pain
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new MacroCraftConfigScreen(screen)));
    }
}