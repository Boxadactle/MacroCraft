package dev.boxadactle.macrocraft;

import dev.boxadactle.boxlib.function.EmptyMethod;
import dev.boxadactle.macrocraft.listeners.KeyAccessor;
import dev.boxadactle.macrocraft.macro.MacroState;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class MacroCraftKeybinds {

    public static KeyMapping hideGui = new KeyMapping("key.macrocraft.hide_gui", GLFW.GLFW_KEY_X, "key.categories.macrocraft");

    public static KeyMapping playMacro = new KeyMapping("key.macrocraft.play_macro", GLFW.GLFW_KEY_K, "key.categories.macrocraft");

    public static KeyMapping pauseMacro = new KeyMapping("key.macrocraft.pause_macro", GLFW.GLFW_KEY_P, "key.categories.macrocraft");

    public static KeyMapping stopMacro = new KeyMapping("key.macrocraft.stop_macro", GLFW.GLFW_KEY_L, "key.categories.macrocraft");

    public static KeyMapping openMacroList = new KeyMapping("key.macrocraft.open_macro_list", GLFW.GLFW_KEY_C, "key.categories.macrocraft");

    public static void checkKeybind(int code, EmptyMethod resume, EmptyMethod pause, EmptyMethod stop) {
        if (((KeyAccessor)hideGui).getKey().getValue() == code) {
            MacroCraft.CONFIG.get().shouldRenderHud = !MacroCraft.CONFIG.get().shouldRenderHud;
            MacroCraft.CONFIG.save();
        } else if (((KeyAccessor)playMacro).getKey().getValue()  == code) {
            resume.accept();
        } else if (((KeyAccessor)pauseMacro).getKey().getValue()  == code) {
            pause.accept();
        } else if (((KeyAccessor)stopMacro).getKey().getValue()  == code) {
            stop.accept();
        }
    }

    public static boolean shouldIgnoreInput(int code) {
        return ((KeyAccessor)hideGui).getKey().getValue() == code
                || ((KeyAccessor)playMacro).getKey().getValue() == code
                || ((KeyAccessor)pauseMacro).getKey().getValue() == code
                || ((KeyAccessor)stopMacro).getKey().getValue() == code;
    }
}
