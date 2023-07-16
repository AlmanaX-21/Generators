package me.almana.generators.listeners;

import me.almana.generators.models.User;
import me.almana.generators.utilities.JsonUtilities;
import me.almana.generators.utilities.SimpleUtilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PlayerQuitListener implements Listener {

    private List<User> users;
    private Map<Player, BukkitTask> tasks;
    private Plugin plugin;

    public PlayerQuitListener(List<User> users, Map<Player, BukkitTask> tasks, Plugin plugin) {
        this.users = users;
        this.tasks = tasks;
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) throws IOException {

        Player player = event.getPlayer();
        User user = SimpleUtilities.findUser(users, player.getUniqueId().toString());

        JsonUtilities jsonUtilities = new JsonUtilities(plugin);
        jsonUtilities.saveUser(user);
        users.remove(user);
        tasks.get(player).cancel();
        tasks.remove(player);
        plugin.getLogger().info("Database entry saved for " + player.getName());
        plugin.getLogger().info("Task stopped.");
    }
}
