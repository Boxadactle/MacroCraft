package dev.boxadactle.macrocraft.gui;

import dev.boxadactle.boxlib.gui.config.BOptionButton;
import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.BCustomButton;
import dev.boxadactle.boxlib.gui.config.widget.label.BCenteredLabel;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.fs.MacroFile;
import dev.boxadactle.macrocraft.macro.Macro;
import dev.boxadactle.macrocraft.macro.MacroState;
import dev.boxadactle.macrocraft.macro.action.KeyboardAction;
import dev.boxadactle.macrocraft.macro.action.MouseButtonAction;
import dev.boxadactle.macrocraft.macro.action.MousePositionAction;
import dev.boxadactle.macrocraft.macro.action.MouseScrollAction;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;

public class MacroScreen extends BOptionScreen {

    Macro macro;
    Path file;

    protected MacroScreen(Screen parent, Path macroFile) {
        super(parent);

        this.file = macroFile;
    }

    @Override
    protected Component getName() {
        if (macro == null) {
            return Component.translatable("screen.macrocraft.macro.title");
        } else {
            return Component.literal(file.toFile().getName());
        }
    }

    @Override
    protected void initFooter(int startX, int startY) {
        addRenderableWidget(createBackButton(startX, startY, parent));
    }

    @Override
    protected void initConfigButtons() {
        this.macro = MacroFile.loadMacro(file);

        String filename = file.toFile().getName();
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.filename", filename)));

        String filepath = file.toFile().getAbsolutePath();
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.filepath", filepath)));

        long lastModified = file.toFile().lastModified();
        String formattedDate = MacroCraft.formatDate(lastModified);
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.lastModified", formattedDate)));

        addConfigLine(new BCustomButton(Component.translatable("screen.macrocraft.macro.openfolder")) {
            @Override
            protected void buttonClicked(BOptionButton<?> button) {
                if (MacroCraft.openMacroFolder()) {
                    button.setMessage(Component.translatable("screen.macrocraft.macro.openfolder.success"));
                } else {
                    button.setMessage(Component.translatable("screen.macrocraft.macro.openfolder.error"));
                }

                button.active = false;
            }
        });

        addConfigLine(new BSpacingEntry());


        String duration = MacroCraft.formatTicks(macro.duration);
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.duration", duration)));

        int actions = macro.actions.size();
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.actions", actions)));

        int keyboardActions = (int) macro.actions.stream().filter(action -> action instanceof KeyboardAction).count();
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.keyboardActions", keyboardActions)));

        int mouseButtonActions = (int) macro.actions.stream().filter(action -> action instanceof MouseButtonAction).count();
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.mouseButtonActions", mouseButtonActions)));

        int mousePositionActions = (int) macro.actions.stream().filter(action -> action instanceof MousePositionAction).count();
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.mousePositionActions", mousePositionActions)));

        int mouseScrollActions = (int) macro.actions.stream().filter(action -> action instanceof MouseScrollAction).count();
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macro.mouseScrollActions", mouseScrollActions)));

        addConfigLine(new BSpacingEntry());

        BCustomButton loadButton = new BCustomButton(Component.translatable("screen.macrocraft.macro.loadMacro")) {
            @Override
            protected void buttonClicked(BOptionButton<?> button) {
                MacroCraft.LOGGER.info("Loading macro " + filename + "...");
                MacroState.loadMacro(macro, filename);
                MacroState.IS_RECORDING = false;
                MacroState.ticksElapsed = 0;
                MacroCraft.LOGGER.info("Macro " + filename + " loaded.");
                ClientUtils.setScreen(parent);
            }
        };

        loadButton.active = !MacroState.IS_RECORDING && !MacroState.LOADED_MACRO.isPlaying;

        addConfigLine(loadButton);
    }
}
