package dev.boxadactle.macrocraft.json;

import com.google.gson.*;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.macro.Macro;
import dev.boxadactle.macrocraft.macro.MacroAction;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class MacroDeserializer implements JsonDeserializer<Macro> {

    @Override
    public Macro deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (jsonElement.isJsonObject()) {
            JsonObject macro = jsonElement.getAsJsonObject();

            JsonElement arrayElement = macro.get("actions");

            if (!arrayElement.isJsonArray()) {
                throw new JsonParseException("Expected a JSON array for the actions field");
            }

            JsonArray array = arrayElement.getAsJsonArray();

            List<JsonElement> actions = array.asList();

            List<MacroAction> parsed = new LinkedList<>();

            for (JsonElement action : actions) {
                if (action.isJsonObject()) {
                    JsonObject object = action.getAsJsonObject();
                    int id = object.get("id").getAsInt();

                    MacroAction macroAction = MacroActions.getAction(id, object);

                    if (macroAction != null) {
                        parsed.add(macroAction);
                    } else {
                        MacroCraft.LOGGER.error("Failed to parse macro action with id: " + id);
                    }
                }
            }

            int duration = macro.get("duration").getAsInt();

            return new Macro(duration, parsed);
        }

        throw new JsonParseException("Expected a JSON object");
    }

}
