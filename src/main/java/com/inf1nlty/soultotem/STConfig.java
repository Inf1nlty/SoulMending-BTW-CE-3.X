package com.inf1nlty.soultotem;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class STConfig {

    public static boolean renderSoulParticles = true;
    public static boolean showSoulHUD = true;

    private static final Logger LOGGER = Logger.getLogger("SoulTotemConfig");
    private static final File configFile = new File("config/soultotem.cfg");

    public static void load() {

        if (!configFile.exists()) {
            LOGGER.info("SoulTotem config file does not exist, using defaults.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("renderSoulParticles=")) {
                    renderSoulParticles = Boolean.parseBoolean(line.split("=")[1]);
                } else if (line.startsWith("showSoulHUD=")) {
                    showSoulHUD = Boolean.parseBoolean(line.split("=")[1]);
                }
            }
            LOGGER.info("SoulTotem config loaded successfully.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load SoulTotem config.", e);
        }
    }

    public static void save() {
        File dir = configFile.getParentFile();
        if (!dir.exists() && !dir.mkdirs()) {
            LOGGER.severe("Failed to create config directory: " + dir.getAbsolutePath());
            return;
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(configFile))) {
            writer.println("renderSoulParticles=" + renderSoulParticles);
            writer.println("showSoulHUD=" + showSoulHUD);
            LOGGER.info("SoulTotem config saved.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save SoulTotem config.", e);
        }
    }
}