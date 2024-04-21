package dev.boxadactle.macrocraft.json;

import com.google.gson.JsonObject;
import dev.boxadactle.macrocraft.macro.MacroAction;
import dev.boxadactle.macrocraft.macro.action.KeyboardAction;
import dev.boxadactle.macrocraft.macro.action.MouseButtonAction;
import dev.boxadactle.macrocraft.macro.action.MousePositionAction;
import dev.boxadactle.macrocraft.macro.action.MouseScrollAction;

import java.util.function.Function;

public enum MacroActions {

    KEYBOARD(0, KeyboardAction.class, (object) -> new KeyboardAction(0, 0, 0, 0, 0).deserialize(object)),
    MOUSE(1, MouseButtonAction.class, (object) -> new MouseButtonAction(0, 0, 0, 0).deserialize(object)),
    MOUSE_POSITION(2, MousePositionAction.class, (object) -> new MousePositionAction(0, 0, 0).deserialize(object)),
    MOUSE_SCROLL(3, MouseScrollAction.class, (object) -> new MouseScrollAction(0, 0, 0).deserialize(object));

    int id;
    Class<? extends MacroAction> clazz;
    Function<JsonObject, MacroAction> constructor;

    public static int getId(MacroAction action) {
        for (MacroActions macroAction : values()) {
            if (macroAction.isInstance(action)) {
                return macroAction.getId();
            }
        }

        return -1;
    }

    public static MacroAction getAction(int id, JsonObject object) {
        for (MacroActions action : values()) {
            if (action.getId() == id) {
                return action.construct(object);
            }
        }

        return null;
    }

    MacroActions(int id, Class<? extends MacroAction> clazz, Function<JsonObject, MacroAction> constructor) {
        this.id = id;
        this.clazz = clazz;
        this.constructor = constructor;
    }

    public int getId() {
        return id;
    }

    public MacroAction construct(JsonObject object) {
        return constructor.apply(object);
    }

    public boolean isInstance(MacroAction action) {
        return clazz.isInstance(action);
    }

}
