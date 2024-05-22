package dev.boxadactle.macrocraft.neoforge;

import dev.boxadactle.boxlib.neoforge.command.BCommandManager;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.config.MacroCraftConfigScreen;
import dev.boxadactle.macrocraft.neoforge.command.ConfigCommand;
import dev.boxadactle.macrocraft.neoforge.command.PlayCommand;
import dev.boxadactle.macrocraft.neoforge.command.RecordCommand;
import dev.boxadactle.macrocraft.neoforge.command.SaveCommand;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(MacroCraft.MOD_ID)
public class MacroCraftNeoforge {
    public MacroCraftNeoforge() {
        MacroCraft.init();

        BCommandManager.registerCommand("macro", list -> {
            list.add(ConfigCommand::new);
            list.add(PlayCommand::new);
            list.add(RecordCommand::new);
            list.add(SaveCommand::new);
        });

        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () ->
                ((minecraft, screen) -> new MacroCraftConfigScreen(screen))
        );
    }
}