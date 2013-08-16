package me.damo1995.AnimalProtect;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class InteractListener
implements Listener
{
   private AnimalProtect plugin;

   public InteractListener(AnimalProtect instance)
   {
      this.plugin = instance;
   }

   @EventHandler(priority=EventPriority.HIGHEST)
   public void onSheepShear(PlayerShearEntityEvent event) {
      if ((event.getEntity() instanceof Sheep)) {
         Player player = event.getPlayer();
         Location loc = event.getEntity().getLocation();
         if (event.isCancelled()) return;

         if (this.plugin.getConfig().getBoolean("shear-protect"))
            if (this.plugin.getWorldGuardPlugin().canBuild(player, loc)) {
               event.setCancelled(false);
               if (this.plugin.getConfig().getBoolean("debug")) player.sendMessage("Sheared Sheep Sucessfully!"); 
            }
            else
            {
               event.setCancelled(true);
               event.getPlayer().sendMessage(ChatColor.RED + "You cannot shear sheep here!");
               if (this.plugin.getConfig().getBoolean("debug")) player.sendMessage("Sheared Sheep Failed!");
            }
      }
   }
}