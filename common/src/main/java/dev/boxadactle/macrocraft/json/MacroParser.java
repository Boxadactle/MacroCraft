package dev.boxadactle.macrocraft.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.boxadactle.macrocraft.macro.Macro;

public class MacroParser {

    public static String serialize(Macro macro) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Macro.class, new MacroSerializer());
        Gson gson = builder.create();

        return gson.toJson(macro);
    }

    public static Macro deserialize(String json) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Macro.class, new MacroDeserializer());
        Gson gson = builder.create();

        return gson.fromJson(json, Macro.class);
    }

}
