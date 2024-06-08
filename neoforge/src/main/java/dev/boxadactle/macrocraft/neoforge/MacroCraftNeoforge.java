package dev.boxadactle.macrocraft.neoforge;

import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.config.MacroCraftConfigScreen;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(MacroCraft.MOD_ID)
public class MacroCraftNeoforge {
    public MacroCraftNeoforge() {
        MacroCraft.init();

        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () ->
                ((minecraft, screen) -> new MacroCraftConfigScreen(screen))
        );
    }
}