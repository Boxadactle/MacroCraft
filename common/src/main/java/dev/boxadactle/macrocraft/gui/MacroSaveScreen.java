package dev.boxadactle.macrocraft.gui;

import dev.boxadactle.boxlib.gui.config.BOptionButton;
import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.BCustomButton;
import dev.boxadactle.boxlib.gui.config.widget.field.BStringField;
import dev.boxadactle.boxlib.gui.config.widget.label.BCenteredLabel;
import dev.boxadactle.boxlib.gui.config.widget.label.BLabel;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.fs.MacroFile;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MacroSaveScreen extends BOptionScreen {
    String fileName;
    BCustomButton saveButton;

    public MacroSaveScreen(Screen parent) {
        super(parent);
    }

    @Override
    protected Component getName() {
        return Component.literal("screen.macrocraft.save.title");
    }

    @Override
    protected void initFooter(int startX, int startY) {
        addRenderableWidget(createCancelButton(startX, startY, parent));
    }

    @Override
    protected void initConfigButtons() {
        // loaded macro
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.record.loaded", MacroState.MACRO_NAME)));

        // macro size
        addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.record.size", MacroState.LOADED_MACRO.actions.size())));

        addConfigLine(new BSpacingEntry());

        if (MacroState.HAS_UNSAVED_CHANGES) {
            saveButton = new BCustomButton(Component.translatable("screen.macrocraft.save.save")) {
                @Override
                protected void buttonClicked(BOptionButton<?> button) {
                    String file = MacroFile.resolveFilename(fileName);
                    MacroCraft.LOGGER.info("Saving macro to file: " + file);

                    if (MacroFile.doesMacroExist(fileName)) {
                        MacroCraft.LOGGER.warn("File already exists! Prompting user...");

                        ClientUtils.confirm(
                                Component.translatable("screen.macrocraft.overwrite.title", file),
                                Component.translatable("screen.macrocraft.overwrite.description", file),
                                () -> {
                                    MacroFile.deleteMacroFile(fileName);
                                    MacroFile.saveMacro(fileName, MacroState.LOADED_MACRO);
                                    ClientUtils.setScreen(parent);
                                },
                                () -> ClientUtils.setScreen(MacroSaveScreen.this)
                        );
                    }

                    MacroFile.saveMacro(fileName, MacroState.LOADED_MACRO);
                    MacroState.loadMacro(MacroState.LOADED_MACRO, fileName);

                    ClientUtils.setScreen(parent);
                }
            };

            addConfigLine(
                    new BStringField("", (f) -> {
                        fileName = f;
                        saveButton.active = !f.isEmpty();
                    }),
                    new BLabel(Component.literal(MacroFile.MACRO_EXTENSION))
            );

            addConfigLine(saveButton);
        } else {
            addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.save.alreadySaved")));
        }



    }
}
