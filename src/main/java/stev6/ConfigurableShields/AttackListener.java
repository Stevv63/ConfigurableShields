package stev6.ConfigurableShields;

import io.papermc.paper.event.player.PlayerShieldDisableEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AttackListener implements Listener {
  private final Map<Material, Integer> whitelist;
  private final int cooldown;

  public AttackListener(ConfigurableShields plugin) {
    this.cooldown = plugin.getConfig().getInt("shields.cooldown", 20);
    ConfigurationSection s = plugin.getConfig().getConfigurationSection("items");
    whitelist =
        s != null
            ? s.getKeys(false).stream()
                .map(Material::matchMaterial)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(m -> m, m -> s.getInt(m.name(), cooldown)))
            : new HashMap<>();
  }

  @EventHandler(ignoreCancelled = true)
  public void onAttack(EntityDamageByEntityEvent e) {
    if (e.getEntity() instanceof Player p && e.getDamager() instanceof Player ap) {
      if (!p.isBlocking()) return;
      Material attackingItem = ap.getInventory().getItemInMainHand().getType();
      if (whitelist.containsKey(attackingItem)) {
        p.clearActiveItem();
        p.setCooldown(Material.SHIELD, whitelist.get(attackingItem));
      }
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onDisableShield(PlayerShieldDisableEvent e) {
    e.setCooldown(cooldown);
  }
}
