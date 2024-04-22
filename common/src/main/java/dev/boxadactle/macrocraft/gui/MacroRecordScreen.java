package dev.boxadactle.macrocraft.gui;

import dev.boxadactle.boxlib.gui.config.BOptionButton;
import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.BConfigScreenButton;
import dev.boxadactle.boxlib.gui.config.widget.button.BCustomButton;
import dev.boxadactle.boxlib.gui.config.widget.label.BCenteredLabel;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MacroRecordScreen extends BOptionScreen {

    BCustomButton recordButton;
    BCustomButton stopButton;

    public MacroRecordScreen(Screen parent) {
        super(parent);
    }

    @Override
    protected Component getName() {
        return Component.translatable("screen.macrocraft.record.title");
    }

    @Override
    protected void initFooter(int startX, int startY) {
        addRenderableWidget(createBackButton(startX, startY, parent));
    }

    @Override
    protected void initConfigButtons() {
        // loaded macro
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.record.loaded", MacroState.MACRO_NAME)));

        // macro size
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.record.size", MacroState.LOADED_MACRO.actions.size())));

        // unsaved changes
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.record.unsaved", (MacroState.HAS_UNSAVED_CHANGES ? GuiUtils.YES : GuiUtils.NO))));

        // load and unload button
        BCustomButton unloadButton = new BCustomButton(Component.translatable("screen.macrocraft.record.unload")) {
            @Override
            protected void buttonClicked(BOptionButton<?> button) {
                if (MacroState.HAS_UNSAVED_CHANGES) {
                    ClientUtils.confirm(
                            Component.translatable("screen.macrocraft.record.unload.confirm"),
                            Component.translatable("screen.macrocraft.record.unload.confirm.description"),
                            () -> {
                                MacroState.unloadMacro();
                                ClientUtils.setScreen(new MacroRecordScreen(parent));
                            },
                            () -> ClientUtils.setScreen(MacroRecordScreen.this)
                    );
                } else {
                    MacroState.unloadMacro();
                    ClientUtils.setScreen(new MacroRecordScreen(parent));
                }
            }
        };

        unloadButton.active = MacroState.hasLoadedMacro();

        addConfigLine(
                new BConfigScreenButton(Component.translatable("screen.macrocraft.record.loadDifferent"), new MacroRecordScreen(parent), MacroListScreen::new),
                unloadButton
        );

        addConfigLine(new BSpacingEntry());

        // macro label
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.record.macro")));

        recordButton = new BCustomButton(Component.translatable("screen.macrocraft.record.start")) {
            @Override
            protected void buttonClicked(BOptionButton<?> button) {
                if (MacroState.startRecording()) {
                    button.active = false;
                    stopButton.active = true;

                    minecraft.setScreen(null);
                } else {
                    button.setMessage(Component.translatable("screen.macrocraft.record.error"));
                    button.active = false;
                }
            }
        };

        stopButton = new BCustomButton(Component.translatable("screen.macrocraft.record.stop")) {
            @Override
            protected void buttonClicked(BOptionButton<?> button) {
                MacroState.stopRecording();
                button.active = false;
                recordButton.active = true;
            }
        };

        if (MacroState.hasLoadedMacro()) {
            recordButton.active = false;
            stopButton.active = false;

            Tooltip tooltip = Tooltip.create(Component.translatable("screen.macrocraft.record.errorLoaded"));
            recordButton.setTooltip(tooltip);
            stopButton.setTooltip(tooltip);
        } else {
            recordButton.active = !MacroState.IS_RECORDING;
            stopButton.active = MacroState.IS_RECORDING;
        }

        addConfigLine(recordButton, stopButton);

        // save button
        addConfigLine(new BConfigScreenButton(Component.translatable("screen.macrocraft.record.save"), new MacroRecordScreen(parent), MacroSaveScreen::new));
    }
}
