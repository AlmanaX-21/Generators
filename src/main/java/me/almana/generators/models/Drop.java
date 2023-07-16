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

public class Drop {

    private Material material;
    private String name;
    private List<String> lore;
    private boolean isEnchanted;
    private int customModelData;
    private long sellCost;

    public Drop(Material material, String name, List<String> lore, boolean isEnchanted, int customModelData, long sellCost) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.isEnchanted = isEnchanted;
        this.customModelData = customModelData;
        this.sellCost = sellCost;
    }

    public ItemStack asItemStack() {

        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        MiniMessage miniMessage = Generators.MINI_MESSAGE;

        container.set(Generators.sellCost, PersistentDataType.LONG, sellCost);
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

    public long getSellCost() {
        return sellCost;
    }

    public void setSellCost(long sellCost) {
        this.sellCost = sellCost;
    }
}
