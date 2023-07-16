package me.almana.generators.utilities;

import com.google.gson.Gson;
import me.almana.generators.Generators;
import me.almana.generators.models.Gen;
import me.almana.generators.models.User;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.List;

public class JsonUtilities {

    private Gson gson = new Gson();
    private Plugin plugin;

    public JsonUtilities(Plugin plugin) {
        this.plugin = plugin;
    }

    public List<Gen> readGenerators(File file) throws FileNotFoundException {

        if (!file.exists()) throw new FileNotFoundException("Generator file not found");

        Reader reader = new FileReader(file);
        Gen[] genArray = gson.fromJson(reader, Gen[].class);
        List<Gen> gens = List.of(genArray);
        plugin.getLogger().info("Loaded " + gens.size() + " generators into memory...");
        return gens;
    }

    public User readUser(String uuid, String name, int maxGeneratorSlots) throws IOException {

        File file = new File(plugin.getDataFolder().getAbsolutePath() + "/players/" + uuid + "[" + name + "].json");
        plugin.getLogger().info(file.getAbsolutePath());
        if (!file.exists() ) {

            file.getParentFile().mkdir();

            User user = new User(uuid, name, maxGeneratorSlots);
            plugin.getLogger().info("Created database entry for " + name);
            file.createNewFile();
            saveUser(user);
            return user;
        }
        Reader reader = new FileReader(file);
        User user = gson.fromJson(reader, User.class);
        if (user == null) {

            user = new User(uuid, name, maxGeneratorSlots);
            plugin.getLogger().info("Created database entry for " + name);
            file.createNewFile();
            saveUser(user);
            return user;
        }
        plugin.getLogger().info("Loaded from database, entry for " + name);
        return user;
    }

    public void saveUser(User user) throws IOException {

        File file = new File(plugin.getDataFolder().getAbsolutePath() + "/players/" + user.getUuid() + "[" + user.getName() + "].json");
        if (!file.exists()) {

            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        Writer writer = new FileWriter(file);
        gson.toJson(user, writer);
        writer.flush();
        writer.close();
        plugin.getLogger().info("Saved " + user.getName() + " into database.");
    }
}
