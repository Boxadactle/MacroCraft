package dev.boxadactle.macrocraft.forge.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.forge.MacrocraftCommand;
import dev.boxadactle.macrocraft.gui.MacroPlayScreen;
import net.minecraft.commands.CommandSourceStack;

public class PlayCommand extends MacrocraftCommand {
    @Override
    public String getCommandName() {
        return "play";
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.executes(this::onPlayCommand);
    }

    private int onPlayCommand(CommandContext<CommandSourceStack> context) {
        ClientUtils.setScreen(new MacroPlayScreen(null));

        return 0;
    }
}
