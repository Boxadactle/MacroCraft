package dev.boxadactle.macrocraft.config;

import dev.boxadactle.boxlib.config.BConfig;
import dev.boxadactle.boxlib.config.BConfigFile;

@BConfigFile("macrocraft")
public class MacroCraftConfig implements BConfig {

    public boolean blockInputsWhenPlaying = true;

    public boolean ignoreMenuNavigation = true;

    public boolean ignoreChatTyping = true;

    public boolean moveMouseWhenPlaying = false;

    public boolean shouldRenderHud = true;

}
