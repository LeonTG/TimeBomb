/**
 * Project: Timebomb
 * Class: com.leontg77.timebomb.Settings
 *
 * The MIT License (MIT)
 * 
 * Copyright (c) 2016 Leon Vaktskjold <leontg77@gmail.com>.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.leontg77.timebomb;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
 
/**
 * Settings class to manage the config file.
 * 
 * @author LeonTG77
 */
public class Settings {
    private final Main plugin;
    
    public Settings(Main plugin) {
        this.plugin = plugin;
    }

    private FileConfiguration config;
    private File cfile;

    /**
     * Sets the settings configs and create missing files.
     */
    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        
        cfile = new File(plugin.getDataFolder(), "config.yml");
        boolean created = false;

        if (!cfile.exists()) {
            try {
                cfile.createNewFile();
                created = true;
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, ChatColor.RED + "Could not create config.yml!", e);
            }
        }

        config = YamlConfiguration.loadConfiguration(cfile);
        
        if (created) {
            config.set("safe worlds", Arrays.asList("spawn"));
            config.set("explosion force", 10);
            config.set("time", 30);
            saveConfig();
        }
    }
    
    /**
     * Gets the config file.
     *
     * @return The file.
     */
    public FileConfiguration getConfig() {
        return config;
    }
    
    /**
     * Saves the data config.
     */
    public void saveConfig() {
        try {
            config.save(cfile);
        } catch (Exception e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save config.yml!");
        }
    }
    
    /**
     * Reloads the config file.
     */
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(cfile);
    }
}