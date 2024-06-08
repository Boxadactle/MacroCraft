package dev.boxadactle.macrocraft.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.command.BCommandManager;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BClientSubcommand;
import dev.boxadactle.boxlib.scheduling.Scheduling;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.gui.MacroRecordScreen;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraft.network.chat.Component;

public class RecordSubcommand implements BClientSubcommand {
    @Override
    public ArgumentBuilder<BCommandSourceStack, ?> getSubcommand() {
        return BCommandManager.literal("record").executes(this::onRecordCommand);
    }

    @Override
    public void build(ArgumentBuilder<BCommandSourceStack, ?> builder) {
        builder.then(BCommandManager.literal("start").executes(this::onStartRecord));

        builder.then(BCommandManager.literal("stop").executes(this::onStopRecord));

        builder.then(BCommandManager.literal("pause").executes(this::onPauseRecord));
    }

    private int onRecordCommand(CommandContext<BCommandSourceStack> ignored) {
        Scheduling.schedule(1, () -> ClientUtils.setScreen(new MacroRecordScreen(null)));

        return 0;
    }

    private int onStartRecord(CommandContext<BCommandSourceStack> ignored) {
        if (MacroState.IS_RECORDING) {
            MacroCraft.LOGGER.player.chat(Component.translatable("command.macrocraft.recording.already"));

            return 1;
        }

        if (MacroState.IS_PAUSED) {
            MacroState.resumeRecording();

            return 0;
        }

        MacroState.startRecording();

        MacroCraft.LOGGER.player.chat(Component.translatable("command.macrocraft.recording.started"));

        return 0;
    }

    private int onStopRecord(CommandContext<BCommandSourceStack> ignored) {
        if (!MacroState.IS_RECORDING) {
            MacroCraft.LOGGER.player.chat(Component.translatable("command.macrocraft.recording.not"));

            return 1;
        }

        MacroState.stopRecording();

        return 0;
    }

    private int onPauseRecord(CommandContext<BCommandSourceStack> ignored) {
        if (!MacroState.IS_RECORDING) {
            MacroCraft.LOGGER.player.chat(Component.translatable("command.macrocraft.recording.not"));

            return 1;
        }

        MacroState.pauseRecording();

        return 0;
    }
}
