package dev.boxadactle.macrocraft.fabric.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.fabric.MacrocraftCommand;
import dev.boxadactle.macrocraft.gui.MacroPlayScreen;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class PlayCommand extends MacrocraftCommand {
    @Override
    public String getCommandName() {
        return "play";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        builder.executes(this::onPlayCommand);
    }

    private int onPlayCommand(CommandContext<FabricClientCommandSource> context) {
        ClientUtils.setScreen(new MacroPlayScreen(null));

        return 0;
    }
}
