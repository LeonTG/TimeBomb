/**
 * Project: Timebomb
 * Class: com.leontg77.timebomb.listeners.DeathListener
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

package com.leontg77.timebomb.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.leontg77.timebomb.Main;
import com.leontg77.timebomb.Settings;

/**
 * Death listener class.
 * 
 * @author LeonTG77
 */
public class DeathListener implements Listener {
    private final Settings settings;
    private final Main plugin;
        
    public DeathListener(Main plugin, Settings settings) {
        this.settings = settings;
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final Location loc = player.getLocation().clone();

        if (settings.getConfig().getStringList("safe worlds").contains(loc.getWorld().getName())) {
            return;
        }
        
        Block block = loc.getBlock();

        block = block.getRelative(BlockFace.DOWN);
        block.setType(Material.CHEST);

        Chest chest = (Chest) block.getState();

        block = block.getRelative(BlockFace.NORTH);
        block.setType(Material.CHEST);

        for (ItemStack item : event.getDrops()) {
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            chest.getInventory().addItem(item);
        }

        event.getDrops().clear();
        
        final ArmorStand stand = player.getWorld().spawn(chest.getLocation().clone().add(0.5, 1, 0), ArmorStand.class);

        stand.setCustomNameVisible(true);
        stand.setSmall(true);

        stand.setGravity(false);
        stand.setVisible(false);
        
        stand.setMarker(true);
        
        new BukkitRunnable() {
            private int time = settings.getConfig().getInt("time", 30) + 1; // add one for countdown.
            
            public void run() {
                time--;
                
                if (time == 0) {
                    plugin.broadcast(Main.PREFIX + "§a" + player.getName() + "'s §fcorpse has exploded!");

                    loc.getBlock().setType(Material.AIR);

                    loc.getWorld().createExplosion(loc.getBlockX() + 0.5, loc.getBlockY() + 0.5, loc.getBlockZ() + 0.5, settings.getConfig().getInt("explosion force", 10), false, true);
                    loc.getWorld().strikeLightning(loc); // Using actual lightning to kill the items.

                    stand.remove();
                    cancel();
                    return;
                }
                else if (time == 1) {
                    stand.setCustomName("§4" + time + "s");
                }
                else if (time == 2) {
                    stand.setCustomName("§c" + time + "s");
                }
                else if (time == 3) {
                    stand.setCustomName("§6" + time + "s");
                }
                else if (time <= 15) {
                    stand.setCustomName("§e" + time + "s");
                }
                else {
                    stand.setCustomName("§a" + time + "s");
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}
