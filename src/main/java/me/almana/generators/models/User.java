package me.almana.generators.models;

import me.almana.generators.Generators;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    private String uuid;
    private String name;
    private List<String> genLocations;
    private int maxGeneratorSlots;


    public User(String uuid, String name, int maxGeneratorSlots) {
        this.uuid = uuid;
        this.name = name;
        this.genLocations = new ArrayList<>();
        this.maxGeneratorSlots = maxGeneratorSlots;
    }

    public Player asPlayer() {

        return Bukkit.getPlayer(UUID.fromString(uuid));
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGenLocations() {
        return genLocations;
    }

    public void setGenLocations(List<String> genLocations) {
        this.genLocations = genLocations;
    }

    public int getMaxGeneratorSlots() {
        return maxGeneratorSlots;
    }

    public void setMaxGeneratorSlots(int maxGeneratorSlots) {
        this.maxGeneratorSlots = maxGeneratorSlots;
    }
}
