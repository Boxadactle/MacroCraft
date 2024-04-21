package dev.boxadactle.macrocraft.macro.action;

import com.google.gson.JsonObject;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.macro.MacroAction;
import net.minecraft.client.KeyboardHandler;

public class KeyboardAction extends MacroAction {
    int key;
    int scancode;
    int action;
    int mods;

    public KeyboardAction(int startTicks, int key, int scancode, int action, int mods) {
        super(startTicks);

        this.key = key;
        this.scancode = scancode;
        this.action = action;
        this.mods = mods;
    }

    @Override
    public void execute() {
        KeyboardHandler k =  ClientUtils.getClient().keyboardHandler;
        long window = ClientUtils.getWindow();

        k.keyPress(window, key, scancode, action, mods);
    }

    @Override
    public JsonObject getDataObject() {
        JsonObject dataObject = new JsonObject();
        dataObject.addProperty("key", key);
        dataObject.addProperty("scancode", scancode);
        dataObject.addProperty("action", action);
        dataObject.addProperty("mods", mods);

        return dataObject;
    }

    @Override
    public void loadObject(JsonObject object) {
        key = object.get("key").getAsInt();
        scancode = object.get("scancode").getAsInt();
        action = object.get("action").getAsInt();
        mods = object.get("mods").getAsInt();
    }
}
