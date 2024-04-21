package dev.boxadactle.macrocraft.forge.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.forge.MacrocraftCommand;
import dev.boxadactle.macrocraft.gui.MacroSaveScreen;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class SaveCommand extends MacrocraftCommand {
    @Override
    public String getCommandName() {
        return "save";
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.executes(this::save);
    }

    private int save(CommandContext<CommandSourceStack> context) {
        if (MacroState.LOADED_MACRO == null) {
            sendFeedback(Component.translatable("command.macrocraft.notLoaded"));

            return 1;
        }

        ClientUtils.setScreen(new MacroSaveScreen(null));

        return 0;
    }
}
