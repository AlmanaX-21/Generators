package me.almana.generators.listeners;

import me.almana.generators.events.GeneratorBreakEvent;
import me.almana.generators.models.Gen;
import me.almana.generators.models.User;
import me.almana.generators.utilities.SimpleUtilities;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import redempt.redlib.blockdata.BlockDataManager;
import redempt.redlib.blockdata.DataBlock;

import java.io.IOException;
import java.util.List;

public class GenBreakListener implements Listener {

    private final List<User> users;
    private final List<Gen> generatorList;
    private final BlockDataManager manager;
    private final String serverPrefix;

    public GenBreakListener(List<User> users, List<Gen> generatorList, BlockDataManager manager, String serverPrefix) {
        this.users = users;
        this.generatorList = generatorList;
        this.manager = manager;
        this.serverPrefix = serverPrefix;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) throws IOException {

        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND) {

            Block block = event.getClickedBlock();
            User user = SimpleUtilities.findUser(users, player.getUniqueId().toString());
            Gen gen = SimpleUtilities.findGen(generatorList, manager.getDataBlock(block).getString("id"));
            if (!isOwner(manager.getDataBlock(block), player)) {

                player.sendRichMessage(serverPrefix + "<red>Cannot break someone else's generator.");
                event.setCancelled(true);
                return;
            }
            GeneratorBreakEvent e = new GeneratorBreakEvent(user, gen, block, manager, serverPrefix);
            e.callEvent();
        }
    }

    private boolean isOwner(DataBlock dataBlock, Player player) {

        return dataBlock.getString("id").equals(player.getUniqueId().toString());
    }
}
