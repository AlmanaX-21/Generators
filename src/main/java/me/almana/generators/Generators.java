package me.almana.generators;

import me.almana.generators.commands.GenCommand;
import me.almana.generators.listeners.BlockPlaceListener;
import me.almana.generators.listeners.GenBreakListener;
import me.almana.generators.listeners.PlayerJoinListener;
import me.almana.generators.listeners.PlayerQuitListener;
import me.almana.generators.models.Gen;
import me.almana.generators.models.User;
import me.almana.generators.utilities.JsonUtilities;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import redempt.redlib.blockdata.BlockDataManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Generators extends JavaPlugin {

    private Plugin plugin;
    private List<Gen> generatorList = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private Map<Player, BukkitTask> tasks = new HashMap<>();
    private int maxGeneratorSlots;
    private int secondsPerDrop;
    private String serverPrefix;
    private BlockDataManager manager;

    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    public static NamespacedKey genId;
    public static NamespacedKey upgradeCost;
    public static NamespacedKey sellCost;

    @Override
    public void onEnable() {

        plugin = this;
        manager = BlockDataManager.createPDC(this, true, true);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        JsonUtilities jsonUtilities = new JsonUtilities(plugin);

        maxGeneratorSlots = getConfig().getInt("MAX-GENERATOR-SLOTS");
        serverPrefix = getConfig().getString("SERVER-PREFIX");
        secondsPerDrop = getConfig().getInt("SECONDS-PER-DROP");

        genId = new NamespacedKey(this, "ID");
        upgradeCost = new NamespacedKey(this, "UPGRADECOST");
        sellCost = new NamespacedKey(this, "SELLCOST");

        File genFile = new File(getDataFolder().getAbsolutePath() + "/generators.json");
        if (!genFile.exists()) saveResource("generators.json", false);

        try {
            generatorList = jsonUtilities.readGenerators(genFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        registerEvents();
        registerCommands();

        getLogger().info("Plugin enabled successfully.");
        getLogger().info("Generators by AlmanaX21.");
    }

    private void registerEvents() {

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(users, tasks, generatorList, plugin, maxGeneratorSlots, secondsPerDrop, manager), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(users, tasks, plugin), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(users, generatorList, genId, manager, serverPrefix), this);
        getServer().getPluginManager().registerEvents(new GenBreakListener(users, generatorList, manager, serverPrefix), this);
        getLogger().info("Events registered.");
    }

    private void registerCommands() {

        getServer().getPluginCommand("gen").setExecutor(new GenCommand(plugin, serverPrefix, generatorList));
    }

    @Override
    public void onDisable() {

    }

    public String getServerPrefix() {
        return serverPrefix;
    }
}
