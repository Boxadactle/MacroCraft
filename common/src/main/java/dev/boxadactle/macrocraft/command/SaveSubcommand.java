package dev.boxadactle.macrocraft.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.boxadactle.boxlib.command.BCommandManager;
import dev.boxadactle.boxlib.command.BCommandSourceStack;
import dev.boxadactle.boxlib.command.api.BClientSubcommand;
import dev.boxadactle.boxlib.scheduling.Scheduling;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.fs.MacroFile;
import dev.boxadactle.macrocraft.gui.MacroSaveScreen;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraft.network.chat.Component;

public class SaveSubcommand implements BClientSubcommand {
    @Override
    public ArgumentBuilder<BCommandSourceStack, ?> getSubcommand() {
        return BCommandManager.literal("save").executes(this::save);
    }

    @Override
    public void build(ArgumentBuilder<BCommandSourceStack, ?> builder) {
        builder.then(
                BCommandManager.argument("filename", StringArgumentType.string())
                        .executes(this::saveFile)
        );
    }

    private int save(CommandContext<BCommandSourceStack> ignored) {
        if (MacroState.LOADED_MACRO == null) {
            MacroCraft.LOGGER.player.chat(Component.translatable("command.macrocraft.notLoaded"));

            return 1;
        }

        Scheduling.schedule(1, () -> ClientUtils.setScreen(new MacroSaveScreen(null)));

        return 0;
    }

    private int saveFile(CommandContext<BCommandSourceStack> context) {
        if (!MacroState.hasLoadedMacro()) {
            MacroCraft.LOGGER.player.chat(Component.translatable("command.macrocraft.notLoaded"));
            return 1;
        }

        String filename = StringArgumentType.getString(context, "filename");

        if (filename.isBlank()) {
            return save(context);
        }

        if (!filename.endsWith(MacroFile.MACRO_EXTENSION)) {
            filename += MacroFile.MACRO_EXTENSION;
        }

        MacroCraft.LOGGER.player.chat(Component.translatable("command.macrocraft.saving"));

        MacroFile.saveMacro(filename, MacroState.LOADED_MACRO);

        MacroCraft.LOGGER.player.chat(Component.translatable("command.macrocraft.saved", filename));

        return 0;
    }
}
