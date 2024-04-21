package dev.boxadactle.macrocraft;

import dev.boxadactle.boxlib.config.BConfigClass;
import dev.boxadactle.boxlib.config.BConfigHandler;
import dev.boxadactle.boxlib.util.ModLogger;
import dev.boxadactle.macrocraft.config.MacroCraftConfig;
import dev.boxadactle.macrocraft.fs.MacroFile;
import dev.boxadactle.macrocraft.macro.MacroState;
import org.apache.commons.lang3.SystemUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MacroCraft {
	public static final String MOD_NAME = "MacroCraft";
	public static final String MOD_ID = "macrocraft";
	public static final String VERSION = "1.0.0";
	public static final String VERSION_STRING = MOD_NAME + " v" + VERSION;

	public static final ModLogger LOGGER = new ModLogger(MOD_NAME);

	public static BConfigClass<MacroCraftConfig> CONFIG;

	public static void init() {
		LOGGER.info("Initializing " + VERSION_STRING + "...");

		LOGGER.info("Resolving macro folder...");
		MacroFile.resolveMacroFolder();

		LOGGER.info("Loading configuration...");
		CONFIG = BConfigHandler.registerConfig(MacroCraftConfig.class);
	}

	public static String formatDate(long timestamp) {
		Date date = new Date(timestamp);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}

	public static String formatTicks(int ticks) {
		int totalSeconds = ticks / 20;

		int minutes = totalSeconds / 60;
		int seconds = totalSeconds % 60;

		return String.format("%02d:%02d", minutes, seconds);
	}

	public static boolean openMacroFolder() {
		LOGGER.info("Attempting to open macro folder in native file explorer...");
		File f = MacroFile.MACRO_PATH.toFile();
		if (SystemUtils.OS_NAME.toLowerCase().contains("windows")) {
			try {
				Runtime.getRuntime().exec(new String[]{"explorer.exe", f.getAbsolutePath()});
				return true;
			} catch (IOException e) {
				LOGGER.error("Got an error: ");
				LOGGER.printStackTrace(e);
				return false;
			}
		} else {
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browseFileDirectory(f);
				LOGGER.info("Opened directory");
				return true;
			} else {
				LOGGER.warn("Incompatible with desktop class");
				return false;
			}
		}
	}

	public static boolean shouldIgnoreInput() {
		return
				(MacroState.LOADED_MACRO.isPlaying ||
				MacroState.LOADED_MACRO.isPaused) &&
				MacroCraft.CONFIG.get().blockInputsWhenPlaying;
	}
}
