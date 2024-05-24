package dev.boxadactle.macrocraft.fabric.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.fabric.MacrocraftCommand;
import dev.boxadactle.macrocraft.gui.MacroSaveScreen;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class SaveCommand extends MacrocraftCommand {
    @Override
    public String getName() {
        return "save";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        builder.executes(this::save);
    }

    private int save(CommandContext<FabricClientCommandSource> context) {
        if (MacroState.LOADED_MACRO == null) {
            sendFeedback(Component.translatable("command.macrocraft.notLoaded"));

            return 1;
        }

        ClientUtils.setScreen(new MacroSaveScreen(null));

        return 0;
    }
}
