package me.almana.generators.listeners;

import me.almana.generators.models.Gen;
import me.almana.generators.models.User;
import me.almana.generators.utilities.JsonUtilities;
import me.almana.generators.utilities.SimpleUtilities;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import redempt.redlib.blockdata.BlockDataManager;
import redempt.redlib.blockdata.DataBlock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PlayerJoinListener implements Listener {

    private final List<User> users;
    private final Map<Player, BukkitTask> tasks;
    private final List<Gen> generatorList;
    private final Plugin plugin;
    private final int maxGenSlots;
    private final int secondsPerDrop;
    private final BlockDataManager manager;

    public PlayerJoinListener(List<User> users, Map<Player, BukkitTask> tasks, List<Gen> generatorList, Plugin plugin, int maxGenSlots, int secondsPerDrop, BlockDataManager manager) {
        this.users = users;
        this.tasks = tasks;
        this.generatorList = generatorList;
        this.plugin = plugin;
        this.maxGenSlots = maxGenSlots;
        this.secondsPerDrop = secondsPerDrop;
        this.manager = manager;
    }

    @EventHandler
    public void preLogin(AsyncPlayerPreLoginEvent event) throws IOException {

        JsonUtilities utilities = new JsonUtilities(plugin);
        User user = utilities.readUser(event.getUniqueId().toString(), event.getName(), maxGenSlots);
        users.add(user);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        User user = SimpleUtilities.findUser(users, event.getPlayer().getUniqueId().toString());
        if (user == null) {

            throw new NullPointerException("User not found for " + event.getPlayer().getName());
        }
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            CompletableFuture<List<Location>> future = CompletableFuture.supplyAsync(() -> {
                List<Location> locations = new ArrayList<>();
                for (String stringLocs: user.getGenLocations()) {
                    try {
                        locations.add(SimpleUtilities.deserializeLoc(stringLocs));
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                return locations;
            });
            List<Location> locs;
            try {
                locs = future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            for (Location loc: locs) {

                plugin.getLogger().info("Manager: " + manager);
                plugin.getLogger().info("BlockData: " + manager.getDataBlock(loc.getBlock()));

                for (DataBlock dataBlock: manager.getAllLoaded()) {

                    plugin.getLogger().info("Location: " + dataBlock.getBlock().getLocation());
                    plugin.getLogger().info("Block type: " + dataBlock.getBlock().getType());
                    plugin.getLogger().info("ID: " + dataBlock.getString("id"));
                    plugin.getLogger().info("OWNER: " + dataBlock.getString("owner"));
                }

                loc.getWorld().dropItem(loc.add(0, 1, 0).toCenterLocation(), SimpleUtilities.findGen(generatorList, manager.getDataBlock(loc.getBlock()).getString("id")).getDrop().asItemStack());
            }

        }, 10L, secondsPerDrop * 20);
        tasks.put(event.getPlayer(), task);
        plugin.getLogger().info("Plugin task begun for " + event.getPlayer().getName());
    }
}
