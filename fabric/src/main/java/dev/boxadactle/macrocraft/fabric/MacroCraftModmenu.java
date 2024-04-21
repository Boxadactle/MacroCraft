package dev.boxadactle.macrocraft.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.boxadactle.macrocraft.config.MacroCraftConfigScreen;

public class MacroCraftModmenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return MacroCraftConfigScreen::new;
    }
}
