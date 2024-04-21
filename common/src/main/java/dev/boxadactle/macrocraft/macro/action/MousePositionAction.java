package dev.boxadactle.macrocraft.macro.action;

import com.google.gson.JsonObject;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.macro.MacroAction;
import dev.boxadactle.macrocraft.listeners.MouseInvoker;
import net.minecraft.client.MouseHandler;
import org.lwjgl.glfw.GLFW;

public class MousePositionAction extends MacroAction {

    double xpos;
    double ypos;

    public MousePositionAction(int startTicks, double xpos, double ypos) {
        super(startTicks);

        this.xpos = xpos;
        this.ypos = ypos;
    }

    @Override
    public void execute() {
        MouseHandler m = ClientUtils.getClient().mouseHandler;
        long window = ClientUtils.getWindow();

        if (MacroCraft.CONFIG.get().moveMouseWhenPlaying)
            GLFW.glfwSetCursorPos(window, xpos, ypos);

        ((MouseInvoker) m).invokeMove(window, xpos, ypos);
    }

    @Override
    public JsonObject getDataObject() {
        JsonObject dataObject = new JsonObject();
        dataObject.addProperty("xpos", xpos);
        dataObject.addProperty("ypos", ypos);

        return dataObject;
    }

    @Override
    public void loadObject(JsonObject object) {
        xpos = object.get("xpos").getAsDouble();
        ypos = object.get("ypos").getAsDouble();
    }
}
