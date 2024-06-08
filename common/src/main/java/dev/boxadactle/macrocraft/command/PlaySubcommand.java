package dev.boxadactle.macrocraft.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.command.BCommandManager;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BClientSubcommand;
import dev.boxadactle.boxlib.scheduling.Scheduling;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.gui.MacroPlayScreen;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraft.network.chat.Component;

public class PlaySubcommand implements BClientSubcommand {

    @Override
    public ArgumentBuilder<BCommandSourceStack, ?> getSubcommand() {
        return BCommandManager.literal("play").executes(this::onPlayCommand);
    }

    @Override
    public void build(ArgumentBuilder<BCommandSourceStack, ?> builder) {
        builder.then(BCommandManager.literal("gui").executes(this::openPlayGui));
    }

    private int onPlayCommand(CommandContext<BCommandSourceStack> ignored) {
        if (!MacroState.hasLoadedMacro()) {
            MacroCraft.LOGGER.player.chat(Component.translatable("command.macrocraft.notLoaded"));
            return 0;
        }

        MacroState.LOADED_MACRO.playMacro();

        return 0;
    }

    private int openPlayGui(CommandContext<BCommandSourceStack> ignored) {
        Scheduling.schedule(1, () -> ClientUtils.setScreen(new MacroPlayScreen(null)));

        return 0;
    }
}
