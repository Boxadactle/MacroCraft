package dev.boxadactle.macrocraft.command;

import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BClientCommand;
import dev.boxadactle.boxlib.scheduling.Scheduling;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.config.MacroCraftConfigScreen;
import dev.boxadactle.macrocraft.gui.MacroListScreen;

public class MacroCommand {

    public static BClientCommand create() {
        return BClientCommand.create("macro", MacroCommand::openList)
                .registerSubcommand("config", MacroCommand::openConfig)
                .registerSubcommand(new PlaySubcommand())
                .registerSubcommand(new RecordSubcommand())
                .registerSubcommand(new SaveSubcommand());
    }

    public static int openList(CommandContext<BCommandSourceStack> ignored) {
        // we have to delay because when the command is run it sets the screen to null
        Scheduling.schedule(1, () -> ClientUtils.setScreen(new MacroListScreen(null)));

        return 0;

    }

    public static int openConfig(CommandContext<BCommandSourceStack> ignored) {
        Scheduling.schedule(1, () -> ClientUtils.setScreen(new MacroCraftConfigScreen(null)));

        return 0;
    }

}
