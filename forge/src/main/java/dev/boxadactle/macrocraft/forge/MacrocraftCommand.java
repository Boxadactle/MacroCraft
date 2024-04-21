package dev.boxadactle.macrocraft.forge;

import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.forge.command.BClientCommand;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.gui.MacroListScreen;
import net.minecraft.commands.CommandSourceStack;

public abstract class MacrocraftCommand extends BClientCommand {
    @Override
    public String getName() {
        return "macrocraft";
    }

    @Override
    protected int onRootCommand(CommandContext<CommandSourceStack> context) {
        ClientUtils.setScreen(new MacroListScreen(null));
        return 0;
    }
}
