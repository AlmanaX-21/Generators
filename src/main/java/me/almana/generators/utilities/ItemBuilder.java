package me.almana.generators.utilities;

import me.almana.generators.models.Drop;
import me.almana.generators.models.Gen;
import org.bukkit.Material;

import java.util.List;

public class ItemBuilder {

    private String id;
    private Material material;
    private String name;
    private List<String> lore;
    private boolean isEnchanted = false;
    private int customModelData = 0;
    private long upgradeCost = 10;
    private long sellCost = 1;
    private Drop drop;

    public ItemBuilder() {

    }

    public static ItemBuilder builder() {
        return new ItemBuilder();
    }

    public ItemBuilder id(String id) {
        this.id = id;
        return this;
    }

    public ItemBuilder material(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder isEnchanted(boolean isEnchanted) {
        this.isEnchanted = isEnchanted;
        return this;
    }

    public ItemBuilder customModelData(int customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public ItemBuilder upgradeCost(long upgradeCost) {
        this.upgradeCost = upgradeCost;
        return this;
    }

    public ItemBuilder sellCost(long sellCost) {
        this.sellCost = sellCost;
        return this;
    }

    public ItemBuilder drop(Drop drop) {
        this.drop = drop;
        return this;
    }

    public Gen buildGen() {
        if (id == null || material == null || name == null || lore == null || drop == null) {
            throw new NullPointerException("Invalid parametres for building a generator");
        }
        return new Gen(id, material, name, lore, isEnchanted, customModelData, drop, upgradeCost);
    }

    public Drop buildDrop() {
        if (material == null || name == null || lore == null || drop == null) {
            throw new NullPointerException("Invalid parametres for building a drop");
        }
        return new Drop(material, name, lore, isEnchanted, customModelData, sellCost);
    }

}
