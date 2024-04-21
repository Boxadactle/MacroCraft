package dev.boxadactle.macrocraft.hud;

import dev.boxadactle.boxlib.layouts.RenderingLayout;
import dev.boxadactle.boxlib.layouts.component.CenteredParagraphComponent;
import dev.boxadactle.boxlib.layouts.layout.CenteredLayout;
import dev.boxadactle.boxlib.layouts.layout.RowLayout;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.MacroCraftKeybinds;
import dev.boxadactle.macrocraft.listeners.KeyAccessor;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class MacroRecordHud {

    public static void render(GuiGraphics graphics) {
        RowLayout information = new RowLayout(0, 0, 10);

        information.addComponent(new CenteredParagraphComponent(
                0,
                Component.translatable("hud.macrocraft.elapsed", MacroCraft.formatTicks(MacroState.ticksElapsed))
        ));

        information.addComponent(new CenteredParagraphComponent(
                0,
                Component.translatable("hud.macrocraft.recording")
        ));

        information.addComponent(new CenteredParagraphComponent(
                0,
                Component.translatable(
                        "hud.macrocraft.hide",
                        GuiUtils.brackets(((KeyAccessor)MacroCraftKeybinds.hideGui).getKey().getDisplayName())
                )
        ));

        CenteredLayout centeredInformation = new CenteredLayout(
                10, 15,
                graphics.guiWidth() - 10,
                information.calculateRect().getHeight() + 5,
                information
        );

        centeredInformation.render(graphics);

        RenderingLayout controls = MacroControls.render(
                MacroState.IS_RECORDING,
                MacroState.IS_PAUSED
        );

        controls.render(graphics);
    }

    public static void checkKeys(int code) {
        MacroCraftKeybinds.checkKeybind(
                code,
                () -> {
                    if (MacroState.IS_PAUSED) {
                        MacroState.resumeRecording();
                    }
                },
                () -> {
                    if (MacroState.IS_RECORDING) {
                        MacroState.pauseRecording();
                    }
                },
                () -> {
                    if (MacroState.IS_RECORDING || MacroState.IS_PAUSED) {
                        MacroState.stopRecording();
                    }
                }
        );
    }

}
