package dev.boxadactle.macrocraft.fabric.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.config.MacroCraftConfigScreen;
import dev.boxadactle.macrocraft.fabric.MacrocraftCommand;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class ConfigCommand extends MacrocraftCommand {
    @Override
    public String getName() {
        return "config";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        builder.executes(this::openConfig);
    }

    private int openConfig(CommandContext<FabricClientCommandSource> source) {
        ClientUtils.setScreen(new MacroCraftConfigScreen(null));

        return 0;
    }
}
