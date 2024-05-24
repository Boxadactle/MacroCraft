package dev.boxadactle.macrocraft.macro;

import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.GuiUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.fs.MacroFile;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;

public class MacroState {

    public static Macro LOADED_MACRO = new Macro();
    public static String MACRO_NAME = GuiUtils.getTranslatable("macrocraft.unsaved");
    public static boolean HAS_UNSAVED_CHANGES = false;

    public static boolean IS_RECORDING = false;
    public static boolean IS_PAUSED = false;

    public static int ticksElapsed = 0;

    public static void loadMacro(Macro macro, String name) {
        LOADED_MACRO = macro;
        MACRO_NAME = MacroFile.resolveFilename(name);
        HAS_UNSAVED_CHANGES = false;

        ClientUtils.showToast(
                Component.translatable("toast.macrocraft.loadedMacro.title"),
                Component.translatable("toast.macrocraft.loadedMacro.description", macro.actions.size(), MACRO_NAME)
        );
    }

    public static void unloadMacro() {
        LOADED_MACRO = new Macro();
        MACRO_NAME = GuiUtils.getTranslatable("macrocraft.unsaved");
        HAS_UNSAVED_CHANGES = false;
    }

    public static boolean hasLoadedMacro() {
        return !LOADED_MACRO.actions.isEmpty();
    }

    public static boolean startRecording() {
        if (IS_RECORDING) {
            return false;
        }

        if (WorldUtils.getWorld() == null) {
            ClientUtils.showToast(
                    Component.translatable("toast.macrocraft.notInWorld.title"),
                    Component.translatable("toast.macrocraft.notInWorld.description")
            );

            return false;
        }

        unloadMacro();

        IS_RECORDING = true;
        IS_PAUSED = false;
        ticksElapsed = 0;

        MacroCraft.LOGGER.info("Macro recording started, now capturing actions...");

        return true;
    }

    public static boolean shouldRenderHud() {
        return (IS_RECORDING || IS_PAUSED) && MacroCraft.CONFIG.get().shouldRenderHud;
    }

    public static void resumeRecording() {
        if (!IS_RECORDING) {
            return;
        }

        ClientUtils.showToast(
            Component.translatable("toast.macrocraft.resumed.title"),
            Component.translatable("toast.macrocraft.resumed.description")
        );

        IS_PAUSED = false;
    }

    public static void pauseRecording() {
        if (!IS_RECORDING) {
            return;
        }

        ClientUtils.showToast(
            Component.translatable("toast.macrocraft.paused.title"),
            Component.translatable("toast.macrocraft.paused.description")
        );

        IS_PAUSED = true;
    }

    public static void stopRecording() {
        HAS_UNSAVED_CHANGES = true;

        IS_RECORDING = false;
        IS_PAUSED = false;
        LOADED_MACRO.duration = ticksElapsed;

        MacroCraft.LOGGER.info("Macro recording stopped, captured " + LOADED_MACRO.actions.size() + " actions in " + ticksElapsed + " ticks.");

        ClientUtils.showToast(
            Component.translatable("toast.macrocraft.recording.finished.title"),
            Component.translatable("toast.macrocraft.recording.finished.description", LOADED_MACRO.actions.size(), MacroCraft.formatTicks(ticksElapsed))
        );
    }

    public static void tick() {
        if (IS_RECORDING && !IS_PAUSED) {
            ticksElapsed++;
        }

        if (LOADED_MACRO != null) {
            LOADED_MACRO.tick();
        }
    }

    public static void addAction(MacroAction action) {
        if (!IS_RECORDING) {
            throw new IllegalStateException("Attempting to add action to macro when not recording!");
        }

        if (IS_PAUSED) {
            MacroCraft.LOGGER.debug("Ignoring " + action.getClass().getSimpleName() + " due to macro recording being paused.");
            return;
        }

        if (ClientUtils.getCurrentScreen() != null) {
            if (ClientUtils.getCurrentScreen().isPauseScreen() && MacroCraft.CONFIG.get().ignoreMenuNavigation) {
                MacroCraft.LOGGER.debug("Ignoring " + action.getClass().getSimpleName() + " due to menu navigation.");
                return;
            }

            if (ClientUtils.getCurrentScreen() instanceof ChatScreen && MacroCraft.CONFIG.get().ignoreChatTyping) {
                MacroCraft.LOGGER.debug("Ignoring " + action.getClass().getSimpleName() + " due to chat typing.");
                return;
            }
        }

        MacroCraft.LOGGER.debug("Captured " + action.getClass().getSimpleName() + " and added to macro.");

        LOADED_MACRO.actions.add(action);
    }

}
