/**
 * Project: Timebomb
 * Class: com.leontg77.timebomb.Main
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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.leontg77.timebomb.commands.TimebombCommand;
import com.leontg77.timebomb.listeners.DeathListener;

/**
 * Main class of the plugin.
 * 
 * @author LeonTG77
 */
public class Main extends JavaPlugin {
    public static final String PREFIX = "§4Timebomb §8» §f";

    @Override
    public void onDisable() {
        PluginDescriptionFile file = getDescription();
        getLogger().info(file.getName() + " is now disabled.");
    }
    
    @Override
    public void onEnable() {
        PluginDescriptionFile file = getDescription();
        getLogger().info(file.getName() + " v" + file.getVersion() + " is now enabled.");
        getLogger().info("Plugin is made by LeonTG77.");
        
        Settings settings = new Settings(this);
        settings.setup();
        
        DeathListener listener = new DeathListener(this, settings);
        TimebombCommand cmd = new TimebombCommand(this, settings, listener);
        
        // register command.
        getCommand("timebomb").setExecutor(cmd);
    }
    
    /**
     * Send the given message to all online players.
     * 
     * @param message The message to send.
     */
    public void broadcast(String message) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.sendMessage(message);
        }
        
        Bukkit.getLogger().info(message);
    }
}