package dev.boxadactle.macrocraft.forge.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.forge.MacrocraftCommand;
import dev.boxadactle.macrocraft.gui.MacroRecordScreen;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class RecordCommand extends MacrocraftCommand {
    @Override
    public String getCommandName() {
        return "record";
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.executes(this::onRecordCommand);

        builder.then(Commands.literal("start").executes(this::onStartRecord));

        builder.then(Commands.literal("stop").executes(this::onStopRecord));

        builder.then(Commands.literal("pause").executes(this::onPauseRecord));
    }

    private int onRecordCommand(CommandContext<CommandSourceStack> context) {
        ClientUtils.setScreen(new MacroRecordScreen(null));

        return 0;
    }

    private int onStartRecord(CommandContext<CommandSourceStack> context) {
        if (MacroState.IS_RECORDING) {
            sendFeedback(Component.translatable("command.macrocraft.recording.already"));

            return 1;
        }

        if (MacroState.IS_PAUSED) {
            MacroState.resumeRecording();

            return 0;
        }

        MacroState.startRecording();

        sendFeedback(Component.translatable("command.macrocraft.recording.started"));

        return 0;
    }

    private int onStopRecord(CommandContext<CommandSourceStack> context) {
        if (!MacroState.IS_RECORDING) {
            sendFeedback(Component.translatable("command.macrocraft.recording.not"));

            return 1;
        }

        MacroState.stopRecording();

        return 0;
    }

    private int onPauseRecord(CommandContext<CommandSourceStack> context) {
        if (!MacroState.IS_RECORDING) {
            sendFeedback(Component.translatable("command.macrocraft.recording.not"));

            return 1;
        }

        MacroState.pauseRecording();

        return 0;
    }
}
