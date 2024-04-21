package dev.boxadactle.macrocraft.fabric;

import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.fabric.command.BClientCommand;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.gui.MacroListScreen;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public abstract class MacrocraftCommand extends BClientCommand {
    @Override
    public String getName() {
        return "macrocraft";
    }

    @Override
    protected int onRootCommand(CommandContext<FabricClientCommandSource> context) {
        ClientUtils.setScreen(new MacroListScreen(null));
        return 0;
    }
}
