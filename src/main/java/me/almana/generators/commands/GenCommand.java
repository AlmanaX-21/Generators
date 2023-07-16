package me.almana.generators.commands;

import me.almana.generators.models.Gen;
import me.almana.generators.utilities.SimpleUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GenCommand implements TabExecutor {

    private Plugin plugin;
    private String serverPrefix;
    private List<Gen> generatorList;

    public GenCommand(Plugin plugin, String serverPrefix, List<Gen> generatorList) {
        this.plugin = plugin;
        this.serverPrefix = serverPrefix;
        this.generatorList = generatorList;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender instanceof Player player) {
            if (strings.length < 2) {
                commandSender.sendRichMessage(serverPrefix + "<red>Usage: /gen get <id>");
                return true;
            }
            String id = strings[1];
            ItemStack stack = SimpleUtilities.findGen(generatorList, id).asItemStack();
            if (stack == null) {
                commandSender.sendRichMessage(serverPrefix + "<red>Generator item could not be generated.");
                return true;
            }
            player.getInventory().addItem(stack);
            player.sendRichMessage(serverPrefix + "<green>Given 1 " + SimpleUtilities.findGen(generatorList, id).getName() + " to player.");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings.length == 1) {
            String text = strings[0].toLowerCase(Locale.ROOT);
            return List.of("get").stream().filter(t -> t.toLowerCase().startsWith(text)).collect(Collectors.toList());
        } else if (strings.length == 2) {
            String text = strings[1].toLowerCase();
            List<String> complete = new ArrayList<>();
            for (Gen gen: generatorList) {
                complete.add(gen.getId());
            }
            return complete.stream().filter(t -> t.toLowerCase().startsWith(text)).collect(Collectors.toList());
        }
        return null;
    }
}
