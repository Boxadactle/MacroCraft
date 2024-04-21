package dev.boxadactle.macrocraft.macro.action;

import com.google.gson.JsonObject;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.macro.MacroAction;
import dev.boxadactle.macrocraft.listeners.MouseInvoker;
import net.minecraft.client.MouseHandler;

public class MouseScrollAction extends MacroAction {
    double xoffset;
    double yoffset;

    public MouseScrollAction(int startTicks, double xoffset, double yoffset) {
        super(startTicks);

        this.xoffset = xoffset;
        this.yoffset = yoffset;
    }

    @Override
    public void execute() {
        MouseHandler m = ClientUtils.getClient().mouseHandler;
        long window = ClientUtils.getWindow();

        ((MouseInvoker) m).invokeScroll(window, xoffset, yoffset);
    }

    @Override
    public JsonObject getDataObject() {
        JsonObject dataObject = new JsonObject();
        dataObject.addProperty("xoffset", xoffset);
        dataObject.addProperty("yoffset", yoffset);

        return dataObject;
    }

    @Override
    public void loadObject(JsonObject object) {
        xoffset = object.get("xoffset").getAsDouble();
        yoffset = object.get("yoffset").getAsDouble();
    }
}
