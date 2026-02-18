package org.shanguanling.serverhub.config;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public abstract class AbstractYamlManager {

    private final Plugin plugin;
    private final String fileName;
    private Configuration configuration;

    public AbstractYamlManager(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        load();
    }

    public void load() {
        plugin.getLogger().info("Loading file: " + fileName);
        if (!plugin.getDataFolder().exists()) {
            plugin.getLogger().info("Creating plugin folder...");
            plugin.getDataFolder().mkdirs();
        }
        File file = new File(plugin.getDataFolder(), fileName);
        try {
            if (!file.exists()) {
                plugin.getLogger().info("File not exists, copying from jar...");
                try (InputStream in = plugin.getResourceAsStream(fileName)) {
                    if (in == null) {
                        plugin.getLogger().severe("Resource not found in jar: " + fileName);
                        return;
                    }
                    Files.copy(in, file.toPath());
                    plugin.getLogger().info("File copied successfully.");
                }
            }
            configuration = ConfigurationProvider
                    .getProvider(YamlConfiguration.class)
                    .load(file);
            plugin.getLogger().info(fileName + " loaded.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void reload() {
        load();
    }

    public Configuration getYaml() {
        return configuration;
    }

    public String getYamlString(String path) {
        if (!getYaml().contains(path)) return null;
        return getYaml().getString(path);
    }

    public List<String> getYamlStringList(String path) {
        if (!getYaml().contains(path)) return Collections.emptyList();
        return getYaml().getStringList(path);
    }

    public String getRandomYamlStringFromList(String path) {
        if (!getYaml().contains(path)) return null;
        List<String> list = getYaml().getStringList(path);
        if (list.isEmpty()) return null;
        return list.get((int) (Math.random() * list.size()));
    }
}