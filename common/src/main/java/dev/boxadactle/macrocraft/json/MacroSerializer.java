package dev.boxadactle.macrocraft.json;

import com.google.gson.*;
import dev.boxadactle.macrocraft.macro.Macro;
import dev.boxadactle.macrocraft.macro.MacroAction;

import java.lang.reflect.Type;

public class MacroSerializer implements JsonSerializer<Macro> {
    private JsonObject serializeMacroAction(MacroAction macroAction) {
        int id = MacroActions.getId(macroAction);

        JsonObject object = macroAction.serialize();
        object.addProperty("id", id);

        return object;
    }

    @Override
    public JsonElement serialize(Macro macro, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray array = new JsonArray();

        for (MacroAction macroAction : macro.actions) {
            array.add(serializeMacroAction(macroAction));
        }

        JsonObject object = new JsonObject();
        object.addProperty("duration", macro.duration);
        object.add("actions", array);

        return object;
    }
}
