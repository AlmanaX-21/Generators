package me.almana.generators.models;

import me.almana.generators.Generators;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Gen {

    private String id;
    private Material material;
    private String name;
    private List<String> lore;
    private boolean isEnchanted;
    private int customModelData;
    private long upgradeCost;
    private Drop drop;

    public Gen(String id, Material material, String name, List<String> lore, boolean isEnchanted, int customModelData, Drop drop, long upgradeCost) {
        this.id = id;
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.isEnchanted = isEnchanted;
        this.customModelData = customModelData;
        this.drop = drop;
        this.upgradeCost = upgradeCost;
    }

    public ItemStack asItemStack() {

        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        MiniMessage miniMessage = Generators.MINI_MESSAGE;

        Generators.getPlugin(Generators.class).getLogger().info("Gen Id: " + id);

        container.set(Generators.genId, PersistentDataType.STRING, id);
        container.set(Generators.upgradeCost, PersistentDataType.LONG, upgradeCost);
        meta.displayName(miniMessage.deserialize(name));
        List<Component> loreList = new ArrayList<>();
        for (String loreL: lore) {
            loreList.add(miniMessage.deserialize(loreL));
        }
        meta.lore(loreList);
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setCustomModelData(customModelData);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public boolean isEnchanted() {
        return isEnchanted;
    }

    public void setEnchanted(boolean enchanted) {
        isEnchanted = enchanted;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public void setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
    }

    public Drop getDrop() {
        return drop;
    }

    public void setDrop(Drop drop) {
        this.drop = drop;
    }

    public long getUpgradeCost() {
        return upgradeCost;
    }

    public void setUpgradeCost(long upgradeCost) {
        this.upgradeCost = upgradeCost;
    }

}
