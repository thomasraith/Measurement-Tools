/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rcraft.measurementtools;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;


/**
 *
 * @author Thomas Raith
 */
class MeasurementListener implements Listener {
    
    MeasurementTools plugin;
    int count;
    int help = 0;
    LocationUser lhelp;
    int i;
    
    public MeasurementListener(MeasurementTools p) {
        plugin = p;
    }
    
    	@EventHandler
        public void getPositions (PlayerInteractEvent event){
            
            if (event.getAction()== Action.RIGHT_CLICK_BLOCK || event.getAction()== Action.LEFT_CLICK_BLOCK){
                    Location l = event.getClickedBlock().getLocation();
            
            
            if(event.getPlayer().getItemInHand().getType() == Material.WOOD_AXE && event.getAction() == Action.RIGHT_CLICK_BLOCK){
                               
                for (i = 0; i < plugin.positions.size(); i++)
                {
                    lhelp = plugin.positions.get(i);
                    if (lhelp.user == event.getPlayer().getName()){
                        lhelp.setLocation2(l.getBlockX(), l.getBlockY(), l.getBlockZ(), l);
                        help = 1;
                        count = (Math.abs(lhelp.x1-lhelp.x2)+1)*(Math.abs(lhelp.y1-lhelp.y2)+1)*(Math.abs(lhelp.z1-lhelp.z2)+1);
                    }
                }
                
                if (help != 1)
                {
                    LocationUser l1 = new LocationUser(event.getPlayer().getName());  
                    plugin.positions.add(l1);
                    l1.setLocation2(l.getBlockX(), l.getBlockY(), l.getBlockZ(), l);
                    event.getPlayer().sendMessage(ChatColor.BLUE + plugin.getConfig().getString("config.messages.position2")+" (" + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ() + ")");
                }
                else 
                {
                    help = 0;
                    event.getPlayer().sendMessage(ChatColor.BLUE + plugin.getConfig().getString("config.messages.position2")+" (" + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ() + ") (" + count + " Blocks)");
                }

                
                
            }
            
            if(event.getPlayer().getItemInHand().getType() == Material.WOOD_AXE && event.getAction() == Action.LEFT_CLICK_BLOCK){ 
                event.setCancelled(true);
                
                for (i = 0; i < plugin.positions.size(); i++)
                {
                    lhelp = plugin.positions.get(i);
                    if (lhelp.user == event.getPlayer().getName()){
                        lhelp.setLocation1(l.getBlockX(), l.getBlockY(), l.getBlockZ(), l);
                        help = 1;
                        count = (Math.abs(lhelp.x1-lhelp.x2)+1)*(Math.abs(lhelp.y1-lhelp.y2)+1)*(Math.abs(lhelp.z1-lhelp.z2)+1);
                    }
                }
                
                if (help != 1)
                {
                    LocationUser l1 = new LocationUser(event.getPlayer().getName());  
                    plugin.positions.add(l1);
                    l1.setLocation1(l.getBlockX(), l.getBlockY(), l.getBlockZ(), l);
                    event.getPlayer().sendMessage(ChatColor.BLUE + plugin.getConfig().getString("config.messages.position1")+" (" + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ() + ")");
                }
                else 
                {
                    help = 0;
                    event.getPlayer().sendMessage(ChatColor.BLUE+ plugin.getConfig().getString("config.messages.position1")+" (" + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ() + ") (" + count + " Blocks)");
                }
                
            }
        }
       }
        
        @EventHandler
        public void checkUpdate(PlayerJoinEvent event){
            if(plugin.permCeck(event.getPlayer(), "measuretools.checkupdate")){   
                checkVersion(event.getPlayer());
                }
            
        }
        
        
        private void checkVersion(Player player){
        
        player.sendMessage(ChatColor.GREEN+"[Measurement Tools] " + plugin.getConfig().getString("config.update.message.check"));
        System.out.println("[Measurement Tools] Check for updates ...");   
        
        PluginDescriptionFile descFile = plugin.getDescription();
        URL url = null;
        BufferedInputStream bufferedInput = null;
        byte[] buffer = new byte[1024];
        try {
        url = new URL("http://rcraft.at/plugins/measurement_tools/VERSION");
        } catch (MalformedURLException ex) {
            System.out.println("[Measurement Tools] Check for updates failed.");
            player.sendMessage(ChatColor.RED+"[Measurement Tools] " + plugin.getConfig().getString("config.update.message.error"));
        }
        try 
        {
            bufferedInput = new BufferedInputStream(url.openStream());
            int bytesRead = 0;
             while ((bytesRead = bufferedInput.read(buffer)) != -1) {
                
                String version= new String(buffer, 0, bytesRead);
                if (Float.valueOf(version) > Float.valueOf(descFile.getVersion()))
                {
                    player.sendMessage(ChatColor.GOLD+"[Measurement Tools] " + plugin.getConfig().getString("config.update.message.newupdate"));
                    System.out.println("[Measurement Tools] A newer Version is available.");
                }
                else{
                    if (version.equals(descFile.getVersion())){
                        player.sendMessage(ChatColor.GREEN+"[Measurement Tools] " + plugin.getConfig().getString("config.update.message.noupdate"));
                        System.out.println("[Measurement Tools] Measurement Tools is up to date.");
                    }
                    else{
                        player.sendMessage(ChatColor.RED+"[Measurement Tools] " + plugin.getConfig().getString("config.update.message.developementbuild"));
                        System.out.println("[Measurement Tools] You are using a developementbuild."); 
                    }
                }
             }
                bufferedInput.close();
            
        } 
        catch (IOException ex) 
        {
            System.out.println("[Measurement Tools] Check for updates failed!");
            player.sendMessage(ChatColor.RED+"[Measurement Tools] " + plugin.getConfig().getString("config.update.message.error"));
        }
    }
}
