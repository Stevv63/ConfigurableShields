package stev6.ConfigurableShields;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class ConfigurableShields extends JavaPlugin {

  private AttackListener listener;

  @Override
  public void onEnable() {
    saveDefaultConfig();
    new Commands(this);
    listener = new AttackListener(this);
    getServer().getPluginManager().registerEvents(listener, this);
  }

  public void reload() {
    saveDefaultConfig();
    reloadConfig();
    EntityDamageByEntityEvent.getHandlerList().unregister(listener);
    listener = new AttackListener(this);
    getServer().getPluginManager().registerEvents(listener, this);
  }
}
