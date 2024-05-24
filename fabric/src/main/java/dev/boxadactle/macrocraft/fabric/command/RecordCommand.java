package dev.boxadactle.macrocraft.fabric.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.fabric.MacrocraftCommand;
import dev.boxadactle.macrocraft.gui.MacroRecordScreen;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

public class RecordCommand extends MacrocraftCommand {
    @Override
    public String getName() {
        return "record";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        builder.executes(this::onRecordCommand);

        builder.then(ClientCommandManager.literal("start").executes(this::onStartRecord));

        builder.then(ClientCommandManager.literal("stop").executes(this::onStopRecord));

        builder.then(ClientCommandManager.literal("pause").executes(this::onPauseRecord));
    }

    private int onRecordCommand(CommandContext<FabricClientCommandSource> context) {
        ClientUtils.setScreen(new MacroRecordScreen(null));

        return 0;
    }

    private int onStartRecord(CommandContext<FabricClientCommandSource> context) {
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

    private int onStopRecord(CommandContext<FabricClientCommandSource> context) {
        if (!MacroState.IS_RECORDING) {
            sendFeedback(Component.translatable("command.macrocraft.recording.not"));

            return 1;
        }

        MacroState.stopRecording();

        return 0;
    }

    private int onPauseRecord(CommandContext<FabricClientCommandSource> context) {
        if (!MacroState.IS_RECORDING) {
            sendFeedback(Component.translatable("command.macrocraft.recording.not"));

            return 1;
        }

        MacroState.pauseRecording();

        return 0;
    }
}
