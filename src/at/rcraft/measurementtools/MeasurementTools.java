/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rcraft.measurementtools;


import com.avaje.ebean.validation.Length;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;


/**
 *
 * @author Thomas Raith
 */
public class MeasurementTools extends JavaPlugin{
    
    
    public ArrayList<LocationUser> positions = new ArrayList<LocationUser>();
    
    @Override
    public void onEnable(){
        
        loadConfig();
        
        try
        {
            if(testPluginInstalled("WorldEdit")){
                if (this.getConfig().getBoolean("config.showselectmessage")){
                      System.out.println("[Measurement Tools] Set showselectmessage false!");
                }
            }
        }
        catch (Exception e){
            System.out.println("[Measurement Tools] WorldEdit is not installed.");
        }
        
        // Metrics Plugin
        if (getConfig().getBoolean("config.allowpluginmetrics")){
            try {
               Metrics metrics = new Metrics(this);
               metrics.start();
               System.out.println("[Measurement Tools] PluginMetrics enabled.");
            } catch (Exception e) {
                System.out.println("[Measurement Tools] Failed to activate PluginMetrics.");
            }
        }
        else {
            System.out.println("[Measurement Tools] PluginMetrics disabled.");
        }
        //Metrics Plugin
    
        PluginDescriptionFile descFile = this.getDescription();
        
        System.out.println("[Measurement Tools] Plugin by "+descFile.getAuthors());
        
        getServer().getPluginManager().registerEvents(new MeasurementListener(this), this);    
    }
        
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        
        if(cmd.getName().equalsIgnoreCase("center")){
            if(sender instanceof Player){
                if(!permCeck((Player)sender, "measuretools.center")){
                    sender.sendMessage(ChatColor.RED+ this.getConfig().getString("config.errormessages.nopermission"));
                    return true;
                }
            }
           /* if(args.length != 0){
                sender.sendMessage(ChatColor.RED+"ERROR: /center [username]");
                return true;
            }*/
            int x = 0,y = 0,z = 0;
            LocationUser lhelp = null;
            for (int i = 0; i < this.positions.size(); i++)
                {
                    lhelp = this.positions.get(i);
                    if (lhelp.user == ((Player)sender).getName()){
                        x = Math.round((Math.abs(lhelp.x1-lhelp.x2)+1)/2);
                        y = Math.round((Math.abs(lhelp.y1-lhelp.y2)+1)/2);
                        z = Math.round((Math.abs(lhelp.z1-lhelp.z2)+1)/2);
                        
                        lhelp.lc = lhelp.l1;
                        
                        if (lhelp.x1 < lhelp.x2){
                            x = lhelp.x1+x;
                            lhelp.lc.setX(x);
                        }
                        else{
                            x = lhelp.x2+x;
                            lhelp.lc.setX(x);
                        }
                        
                        if (lhelp.y1 < lhelp.y2){
                            y = lhelp.y1+y;
                            lhelp.lc.setY(y);
                        }
                        else{
                            y = lhelp.y2+y;
                            lhelp.lc.setY(y);
                        }
                        
                        if (lhelp.z1 < lhelp.z2){
                            z = lhelp.z1+z;
                            lhelp.lc.setZ(z);
                        }
                        else{
                            z = lhelp.z2+z;
                            lhelp.lc.setZ(z);
                        }
                        
                         lhelp.lc.getBlock().setTypeId(this.getConfig().getInt("config.datavalues.centerblockid"));  
                         sender.sendMessage(ChatColor.BLUE+this.getConfig().getString("config.messages.center")+ " x:" + x + " y: " + y +" z: " + z);   
                         return true;
                    }
                }
            sender.sendMessage(ChatColor.RED+this.getConfig().getString("config.errormessages.noareamarked"));
        }
        
        if(cmd.getName().equalsIgnoreCase("length")){
                if(!permCeck((Player)sender, "measuretools.length")){
                    sender.sendMessage(ChatColor.RED+ this.getConfig().getString("config.errormessages.nopermission"));
                    return true;
                }
            
                LocationUser lhelp;
                int x = 0,y = 0,z = 0;
                for (int i = 0; i < this.positions.size(); i++)
                {
                    lhelp = this.positions.get(i);
                    if (lhelp.user == ((Player)sender).getName()){
                        x = Math.round(Math.abs(lhelp.x1-lhelp.x2)+1);
                        y = Math.round(Math.abs(lhelp.y1-lhelp.y2)+1);
                        z = Math.round(Math.abs(lhelp.z1-lhelp.z2)+1);
                        
                        if (args[0].equalsIgnoreCase("x")){
                            sender.sendMessage(ChatColor.BLUE+this.getConfig().getString("config.messages.lengthx")+" "+x + " Blocks");
                        }
                        else {
                            if (args[0].equalsIgnoreCase("y")){
                                    sender.sendMessage(ChatColor.BLUE+this.getConfig().getString("config.messages.lengthy")+" "+y + " Blocks");
                            }
                            else{
                                if (args[0].equalsIgnoreCase("z")){
                                    sender.sendMessage(ChatColor.BLUE+this.getConfig().getString("config.messages.lengthz")+" "+z + " Blocks");
                                }
                                else{
                                     sender.sendMessage(ChatColor.RED+this.getConfig().getString("config.errormessages.syntaxerror")+" /length <axis>");
                                }
                            }
                        }
                        return true;
                    }
                }
                sender.sendMessage(ChatColor.RED+this.getConfig().getString("config.errormessages.noareamarked"));
        }
        
        if(cmd.getName().equalsIgnoreCase("mm")){
            if (args.length == 0){
                PluginDescriptionFile descFile = this.getDescription();
                sender.sendMessage(ChatColor.GREEN+"-----------------------------------------------------");
                sender.sendMessage(ChatColor.GREEN+descFile.getFullName() +" by "+descFile.getAuthors());
                sender.sendMessage(ChatColor.GREEN+ "Type /mm help for help");
                sender.sendMessage(ChatColor.GREEN+ "Type /mm perms for permissions");
                sender.sendMessage(ChatColor.GREEN+"-----------------------------------------------------");
            }
            else{
                if (args[0].equalsIgnoreCase("help")){
                   sender.sendMessage(ChatColor.GREEN+"Get center: /center");
                   sender.sendMessage(ChatColor.GREEN+"Get axis length /length <axis>  (Axis: x, y, z)");
                }
                else{
                   if (args[0].equalsIgnoreCase("perms")){
                        sender.sendMessage(ChatColor.GREEN+"Get center and place block: measuretools.center");
                        sender.sendMessage(ChatColor.GREEN+"Get Length: measuretools.length");
                        sender.sendMessage(ChatColor.GREEN+"Check Update by Join: measuretools.checkupdate");
                    }
                   else{
                       sender.sendMessage(ChatColor.RED+this.getConfig().getString("config.errormessages.syntaxerror")+" /mm <command>");
                   }
                }
            }
        }      
        return true;
    }
    
    
    private void sendMessage(String username, String message){
        
            Player player = getServer().getPlayerExact(username);
        
            if(player!=null){ 
                player.sendMessage(message);    
            }
    }
    
    
    private void loadConfig(){
      //String rundruf = this.getConfig().getString(rundruf);
       this.getConfig().options().header("MEASUREMENT TOOLS CONFIGURATION");
       this.getConfig().addDefault("config.messages.position1", "First position selected");
       this.getConfig().addDefault("config.messages.position2", "Second position selected");
       this.getConfig().addDefault("config.messages.center", "The center of the selected area is at:");
       this.getConfig().addDefault("config.messages.lengthx", "X - Length:");
       this.getConfig().addDefault("config.messages.lengthy", "Y - Length:");
       this.getConfig().addDefault("config.messages.lengthz", "Z - Length:");
       this.getConfig().addDefault("config.errormessages.nopermission", "You don't have the required permissons.");
       this.getConfig().addDefault("config.errormessages.noareamarked", "You have to mark the area first.");
       this.getConfig().addDefault("config.errormessages.syntaxerror", "Please check syntax! Usage:");
       this.getConfig().addDefault("config.update.message.check", "Check for updates ... ");
       this.getConfig().addDefault("config.update.message.newupdate", "A newer Version is available.");
       this.getConfig().addDefault("config.update.message.noupdate", "Measurement Tools is up to date.");
       this.getConfig().addDefault("config.update.message.developementbuild", "You are using a developementbuild.");
       this.getConfig().addDefault("config.update.message.error", "Check for updates failed.");
       this.getConfig().addDefault("config.datavalues.centerblockid", 42);
       this.getConfig().addDefault("config.showselectmessage", true);
       this.getConfig().addDefault("config.allowpluginmetrics", true);
       
       this.getConfig().options().copyDefaults(true);
       this.saveConfig();
    }
    
    
    public boolean permCeck(Player player, String permission){
        if(player.isOp() || player.hasPermission(permission)){
            return true;
        }
        return false;
    }
    
    
    private boolean testPluginInstalled(String pluginname){
        if (getServer().getPluginManager().getPlugin(pluginname).isEnabled()){
            System.out.println("[Measurement Tools] "+pluginname + " was detected.");
            return true;
        }
        else
            return false;
    }
}
