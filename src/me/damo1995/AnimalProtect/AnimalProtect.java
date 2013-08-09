package me.damo1995.AnimalProtect;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AnimalProtect extends JavaPlugin
{
   Logger log = Bukkit.getServer().getLogger();

   String success = ChatColor.GREEN + "[AnimalProtect]: ";

   String fail = ChatColor.RED + "[AnimalProtect]: ";

   public static String noDamagePermMsg = "CONFIG ERROR!";
   public static String cmdFail = "CONFIG ERROR!";
   public static String adminNotifyMsg = "CONFIG ERROR!";
   public static String NoRideBecauseNoBuildPermMsg = "CONFIG ERROR!";
   public static String NoRideBecauseNotOwnerMsg = "CONFIG ERROR!";
   public static String MountOwnerMsg = "CONFIG ERROR!";
   
   private final NewDamageListeners dl = new NewDamageListeners(this);
   private final InteractListener shear = new InteractListener(this); 
   private final RideListener rl = new RideListener(this);

   public void onEnable()
   {
      PluginManager pm = getServer().getPluginManager();
      pm.registerEvents(this.dl, this);
      pm.registerEvents(this.shear, this);
      pm.registerEvents(this.rl, this);

      logMessage("Enabled!");

      getWorldGuardPlugin();
      setupConfig();
      readConfigValues();     

      getCommand("animalprotect").setExecutor(new CommandHandler(this));      
   }

   public void onDisable()
   {
      logMessage("Disabled!");
      getServer().getPluginManager().disablePlugin(this);
   }

   public WorldGuardPlugin getWorldGuardPlugin()
   {
      Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
      PluginManager pm = getServer().getPluginManager();
      if ((plugin == null) || (!(plugin instanceof WorldGuardPlugin)))
      {
         logWarning("WorldGuard Plugin Not found!");
         logWarning("AnimalProtect Disabled!");

         pm.disablePlugin(this);
         return null;
      }
      return (WorldGuardPlugin)plugin;
   }

   protected void logMessage(String msg)
   {
      PluginDescriptionFile pdFile = getDescription();
      this.log.info("[" + pdFile.getName() + " " + pdFile.getVersion() + "]: " + msg);
   }

   protected void logWarning(String msg)
   {
      PluginDescriptionFile pdFile = getDescription();
      this.log.warning("[" + pdFile.getName() + " " + pdFile.getVersion() + "]: " + msg);
   }

   private void setupConfig()
   {
      FileConfiguration cfg = getConfig();
      FileConfigurationOptions cfgOptions = cfg.options();
      File file = new File(getDataFolder() + File.separator + "config.yml");
      if (!file.exists())
      {
         logMessage("No Config found - Defaulting.");
         saveDefaultConfig();
         cfgOptions.copyDefaults(true);
         cfgOptions.header("Default Config for AnimalProtect");
         cfgOptions.copyHeader(true);
         saveConfig();
      }
   }

   public void readConfigValues()
   {
      AnimalProtect.noDamagePermMsg = (this.fail + getConfig().getString("NoDamagePermMsg"));
      AnimalProtect.NoRideBecauseNoBuildPermMsg = (this.fail + getConfig().getString("NoRideBecauseNoBuildPermMsg"));
      AnimalProtect.NoRideBecauseNotOwnerMsg = (this.fail + getConfig().getString("NoRideBecauseNotOwnerMsg"));
      AnimalProtect.MountOwnerMsg = (this.fail + getConfig().getString("MountOwnerMsg"));
      AnimalProtect.cmdFail = (this.fail + getConfig().getString("CommandFail"));
      AnimalProtect.adminNotifyMsg = (this.fail + getConfig().getString("AdminNotification"));
      
      // TODO check der config werte
   }
}