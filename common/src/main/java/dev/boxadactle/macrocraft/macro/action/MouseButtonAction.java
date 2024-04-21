package dev.boxadactle.macrocraft.macro.action;

import com.google.gson.JsonObject;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.macro.MacroAction;
import dev.boxadactle.macrocraft.listeners.MouseInvoker;
import net.minecraft.client.MouseHandler;

public class MouseButtonAction extends MacroAction {
    int button;
    int action;
    int mods;

    public MouseButtonAction(int startTicks, int button, int action, int mods) {
        super(startTicks);

        this.button = button;
        this.action = action;
        this.mods = mods;
    }

    @Override
    public void execute() {
        MouseHandler m = ClientUtils.getClient().mouseHandler;
        long window = ClientUtils.getWindow();

        ((MouseInvoker)m).invokeMousePress(window, button, action, mods);
    }

    @Override
    public JsonObject getDataObject() {
        JsonObject dataObject = new JsonObject();
        dataObject.addProperty("button", button);
        dataObject.addProperty("action", action);
        dataObject.addProperty("mods", mods);

        return dataObject;
    }

    @Override
    public void loadObject(JsonObject object) {
        button = object.get("button").getAsInt();
        action = object.get("action").getAsInt();
        mods = object.get("mods").getAsInt();
    }
}
