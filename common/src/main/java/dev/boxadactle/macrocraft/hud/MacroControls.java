package dev.boxadactle.macrocraft.hud;

import dev.boxadactle.boxlib.layouts.LayoutComponent;
import dev.boxadactle.boxlib.layouts.RenderingLayout;
import dev.boxadactle.boxlib.layouts.component.CenteredParagraphComponent;
import dev.boxadactle.boxlib.layouts.component.LayoutContainerComponent;
import dev.boxadactle.boxlib.layouts.layout.CenteredLayout;
import dev.boxadactle.boxlib.layouts.layout.ColumnLayout;
import dev.boxadactle.boxlib.layouts.layout.PaddingLayout;
import dev.boxadactle.boxlib.layouts.layout.RowLayout;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.macrocraft.MacroCraftKeybinds;
import dev.boxadactle.macrocraft.listeners.KeyAccessor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class MacroControls {

    public static final ResourceLocation PAUSE_DISABLED = new ResourceLocation("macrocraft", "textures/gui/pause_disabled.png");
    public static final ResourceLocation PAUSE_ENABLED = new ResourceLocation("macrocraft", "textures/gui/pause_enabled.png");
    public static final ResourceLocation PLAY_DISABLED = new ResourceLocation("macrocraft", "textures/gui/play_disabled.png");
    public static final ResourceLocation PLAY_ENABLED = new ResourceLocation("macrocraft", "textures/gui/play_enabled.png");
    public static final ResourceLocation STOP = new ResourceLocation("macrocraft", "textures/gui/stop.png");

    static int padding = 5;

    public static RenderingLayout createButtons(GuiGraphics graphics, boolean disablePlayButton, boolean disablePauseButton) {
        RowLayout layout = new RowLayout(0, 0, padding);

        layout.addComponent(new LayoutContainerComponent(renderPlayButton(disablePlayButton)));
        layout.addComponent(new LayoutContainerComponent(renderPauseButton(disablePauseButton)));
        layout.addComponent(new LayoutContainerComponent(renderStopButton()));

        int w = graphics.guiWidth();
        int h = graphics.guiHeight();

        return new CenteredLayout(0, h - 100, w, layout.calculateRect().getHeight() + 20, layout);
    }

    static RenderingLayout renderPlayButton(boolean isDisabled) {
        ColumnLayout layout = new ColumnLayout(0, 0, 5);
        layout.addComponent(new ImageRendererComponent(isDisabled ? PLAY_DISABLED : PLAY_ENABLED));

        ColumnLayout text = new ColumnLayout(0, 0, 0);
        text.addComponent(new CenteredParagraphComponent(
                0,
                GuiUtils.brackets(((KeyAccessor) MacroCraftKeybinds.playMacro).getKey().getDisplayName())
        ));
        layout.addComponent(new LayoutContainerComponent(
                new CenteredLayout(
                        0, 0,
                        36, text.calculateRect().getHeight(),
                        text
                )
        ));

        return layout;

    }

    static RenderingLayout renderPauseButton(boolean isDisabled) {
        ColumnLayout layout = new ColumnLayout(0, 0, 5);
        layout.addComponent(new ImageRendererComponent(isDisabled ? PAUSE_DISABLED : PAUSE_ENABLED));

        ColumnLayout text = new ColumnLayout(0, 0, 0);
        text.addComponent(new CenteredParagraphComponent(
                0,
                GuiUtils.brackets(((KeyAccessor) MacroCraftKeybinds.pauseMacro).getKey().getDisplayName())
        ));
        layout.addComponent(new LayoutContainerComponent(
                new CenteredLayout(
                        0, 0,
                        36, text.calculateRect().getHeight(),
                        text
                )
        ));

        return layout;
    }

    static RenderingLayout renderStopButton() {
        ColumnLayout layout = new ColumnLayout(0, 0, 5);
        layout.addComponent(new ImageRendererComponent(STOP));

        ColumnLayout text = new ColumnLayout(0, 0, 0);
        text.addComponent(new CenteredParagraphComponent(
                0,
                GuiUtils.brackets(((KeyAccessor) MacroCraftKeybinds.stopMacro).getKey().getDisplayName())
        ));
        layout.addComponent(new LayoutContainerComponent(
                new CenteredLayout(
                        0, 0,
                        36, text.calculateRect().getHeight(),
                        text
                )
        ));

        return layout;
    }

    private static class ImageRendererComponent extends LayoutComponent<ResourceLocation> {

        public ImageRendererComponent(ResourceLocation component) {
            super(component);
        }

        @Override
        public int getWidth() {
            return 36;
        }

        @Override
        public int getHeight() {
            return 36;
        }

        @Override
        public void render(GuiGraphics graphics, int x, int y) {
            int size = 36;
            graphics.blit(
                    component,
                    x, y,
                    size, size,
                    size, size,
                    size, size
            );
        }
    }

}
