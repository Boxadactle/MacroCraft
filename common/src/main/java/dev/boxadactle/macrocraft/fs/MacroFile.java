package dev.boxadactle.macrocraft.fs;

import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.json.MacroParser;
import dev.boxadactle.macrocraft.macro.Macro;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MacroFile {

    public static final Path MACRO_PATH = Paths.get(ClientUtils.getConfigFolder().toString(), MacroCraft.MOD_ID);
    public static final String MACRO_EXTENSION = ".macrocraft";

    public static List<File> getMacroFiles() {
        try {
            return Files.walk(MACRO_PATH)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(MACRO_EXTENSION))
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            MacroCraft.LOGGER.printStackTrace(e);
            return Collections.emptyList();
        }
    }

    public static String resolveFilename(String name) {
        if (name.endsWith(MACRO_EXTENSION))
            return name;
        return name + MACRO_EXTENSION;
    }

    public static boolean doesMacroExist(String name) {
        return Files.exists(Paths.get(MACRO_PATH.toString(), resolveFilename(name)));
    }

    public static void resolveMacroFolder() {
        try {
            if (!Files.exists(MACRO_PATH)) {
                Files.createDirectories(MACRO_PATH);
            }
        } catch (IOException e) {
            MacroCraft.LOGGER.printStackTrace(e);
        }
    }

    public static Path resolveMacroPath(String filename) {
        return MACRO_PATH.resolve(resolveFilename(filename));
    }

    public static void deleteMacroFile(String name) {
        try {
            Files.deleteIfExists(Paths.get(MACRO_PATH.toString(), resolveFilename(name)));
        } catch (IOException e) {
            MacroCraft.LOGGER.printStackTrace(e);
        }
    }

    public static void saveMacro(String name, Macro macro) {
        MacroCraft.LOGGER.info("Attempting to save macro: " + name);
        Path file = Paths.get(MACRO_PATH.toString(), resolveFilename(name));

        try {
            String json = MacroParser.serialize(macro);
            byte[] encrypted = Crypto.encrypt(json);

            Files.write(file, encrypted);
        } catch (Exception e) {
            MacroCraft.LOGGER.error("Failed to save macro: " + name);
            MacroCraft.LOGGER.printStackTrace(e);
        }
    }

    public static Macro loadMacro(String name) {
        return loadMacro(MACRO_PATH.resolve(resolveFilename(name)));
    }

    public static Macro loadMacro(Path file) {
        MacroCraft.LOGGER.info("Attempting to load macro: " + file.getFileName());

        try {
            byte[] encrypted = Files.readAllBytes(file);
            String json = Crypto.decrypt(encrypted);

            return MacroParser.deserialize(json);
        } catch (Exception e) {
            MacroCraft.LOGGER.error("Failed to load macro: " + file.getFileName());
            MacroCraft.LOGGER.printStackTrace(e);
            return null;
        }
    }

}
