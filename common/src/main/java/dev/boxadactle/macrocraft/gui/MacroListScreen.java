package dev.boxadactle.macrocraft.gui;

import com.google.common.collect.ImmutableList;
import dev.boxadactle.boxlib.gui.BOptionButton;
import dev.boxadactle.boxlib.gui.BOptionHelper;
import dev.boxadactle.boxlib.gui.BOptionScreen;
import dev.boxadactle.boxlib.gui.widget.button.BConfigScreenButton;
import dev.boxadactle.boxlib.gui.widget.button.BCustomButton;
import dev.boxadactle.boxlib.gui.widget.label.BCenteredLabel;
import dev.boxadactle.boxlib.gui.widget.label.BLabel;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.RenderUtils;
import dev.boxadactle.macrocraft.fs.MacroFile;
import dev.boxadactle.macrocraft.macro.Macro;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.io.File;
import java.util.List;

public class MacroListScreen extends BOptionScreen {
    public MacroListScreen(Screen parent) {
        super(parent);
    }

    @Override
    protected int getScrollingWidgetStart() {
        return super.getScrollingWidgetStart() + 40;
    }

    @Override
    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 100;
    }

    @Override
    protected int getRowWidth() {
        return 350;
    }

    @Override
    protected int getRowHeight() {
        return super.getRowHeight() + 4;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);

        RenderUtils.drawTextCentered(guiGraphics, Component.translatable("screen.macrocraft.record.loaded", MacroState.MACRO_NAME), this.width / 2, 25);
    }

    @Override
    protected Component getName() {
        return Component.translatable("screen.macrocraft.macroList.title");
    }

    @Override
    protected void initFooter(int startX, int startY) {
        Button doneButton = createCancelButton(startX, startY, parent);
        doneButton.setMessage(GuiUtils.DONE);

        addRenderableWidget(doneButton);

        initMacroButtons(startX);
    }

    private void initMacroButtons(int startX) {
        int startY = getScrollingWidgetStart() - 20;

        Button recordButton = createHalfCancelButton(startX, startY, (b) -> {
            ClientUtils.setScreen(new MacroRecordScreen(this));
        });
        recordButton.setMessage(Component.translatable("screen.macrocraft.macroList.record"));

        Button playButton = createHalfSaveButton(startX, startY, (b) -> {
            ClientUtils.setScreen(new MacroPlayScreen(this));
        });
        playButton.setMessage(Component.translatable("screen.macrocraft.macroList.play"));

        addRenderableWidget(recordButton);
        addRenderableWidget(playButton);
    }

    @Override
    protected void initConfigButtons() {
        List<File> macroFiles = MacroFile.getMacroFiles();

        if (!macroFiles.isEmpty()) {
            for (File file : macroFiles) {
                this.configList.addEntry(new FileEntry(file.getName()));
            }
        } else {
            addConfigLine(new BCenteredLabel(Component.translatable("screen.macrocraft.macroList.noMacros")));
        }
    }

    public class FileEntry extends ConfigList.ConfigEntry {
        BLabel label;
        BCustomButton loadButton;
        BConfigScreenButton editButton;
        BCustomButton deleteButton;

        public FileEntry(String filename) {
            this.label = new BLabel(Component.literal(filename));
            this.loadButton = new BCustomButton(Component.translatable("screen.macrocraft.macroList.load")) {
                @Override
                protected void buttonClicked(BOptionButton<?> button) {
                    Macro macro = MacroFile.loadMacro(filename);
                    MacroState.loadMacro(macro, MacroFile.resolveFilename(filename));
                    ClientUtils.setScreen(MacroListScreen.this.parent);
                }
            };

            this.editButton = new BConfigScreenButton(
                    Component.translatable("screen.macrocraft.macroList.edit"),
                    MacroListScreen.this,
                    (parent) -> new MacroScreen(parent, MacroFile.resolveMacroPath(filename))
            );

            this.deleteButton = new BCustomButton(Component.translatable("screen.macrocraft.macroList.delete")) {
                @Override
                protected void buttonClicked(BOptionButton<?> button) {
                    ClientUtils.confirm(
                            Component.translatable("screen.macrocraft.macroList.delete.title"),
                            Component.translatable("screen.macrocraft.macroList.delete.description", filename),
                            () -> {
                                MacroFile.deleteMacroFile(filename);
                                ClientUtils.setScreen(new MacroListScreen(MacroListScreen.this.parent));
                            },
                            () -> ClientUtils.setScreen(new MacroListScreen(MacroListScreen.this.parent))
                    );

                }
            };
        }

        @Override
        public List<? extends AbstractWidget> getWidgets() {
            return ImmutableList.of(label, loadButton, editButton, deleteButton);
        }

        @Override
        public boolean isInvalid() {
            return label.isInvalid() || loadButton.isInvalid() || editButton.isInvalid() || deleteButton.isInvalid();
        }

        @Override
        public void render(GuiGraphics p_93523_, int index, int y1, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int p1 = BOptionHelper.padding() / 2;
            int p2 = BOptionHelper.padding() / 2;

            int y = y1 + 2;

            int width1 = (int) (entryWidth * 0.66 - p1);
            int bwidth = (int) (entryWidth * 0.34 - p2);
            int width2 = bwidth / 3 - p2;

            label.setX(x);
            label.setY(y);
            label.setWidth(width1);

            loadButton.setX(x + width1);
            loadButton.setY(y);
            loadButton.setWidth(width2);

            editButton.setX(x + width1 + width2);
            editButton.setY(y);
            editButton.setWidth(width2);

            deleteButton.setX(x + width1 + width2 * 2);
            deleteButton.setY(y);
            deleteButton.setWidth(width2);

            label.render(p_93523_, mouseX, mouseY, tickDelta);
            loadButton.render(p_93523_, mouseX, mouseY, tickDelta);
            editButton.render(p_93523_, mouseX, mouseY, tickDelta);
            deleteButton.render(p_93523_, mouseX, mouseY, tickDelta);
        }
    }
}
