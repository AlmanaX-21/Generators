package me.almana.generators.listeners;

import me.almana.generators.Generators;
import me.almana.generators.events.GeneratorPlaceEvent;
import me.almana.generators.models.Gen;
import me.almana.generators.models.User;
import me.almana.generators.utilities.SimpleUtilities;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import redempt.redlib.blockdata.BlockDataManager;

import java.io.IOException;
import java.util.List;

public class BlockPlaceListener implements Listener {

    private final List<User> users;
    private final List<Gen> generatorList;
    private final NamespacedKey genId;
    private final BlockDataManager manager;
    private final String serverPrefix;

    public BlockPlaceListener(List<User> users, List<Gen> generatorList, NamespacedKey genId, BlockDataManager manager, String serverPrefix) {
        this.users = users;
        this.generatorList = generatorList;
        this.genId = genId;
        this.manager = manager;
        this.serverPrefix = serverPrefix;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) throws IOException {

        User user = SimpleUtilities.findUser(users, event.getPlayer().getUniqueId().toString());
        ItemStack stack = event.getItemInHand();

        if (!isGen(stack) || user == null) return;
        if (!canRun(user)) {

            event.getPlayer().sendActionBar(Generators.MINI_MESSAGE.deserialize("<red>Cannot place anymore gens"));
            return;
        }

        Gen gen = SimpleUtilities.findGen(generatorList, stack.getItemMeta().getPersistentDataContainer().get(genId, PersistentDataType.STRING));
        GeneratorPlaceEvent e = new GeneratorPlaceEvent(user, gen, event.getBlockPlaced(), manager, serverPrefix);
        e.callEvent();
    }


    private boolean isGen(ItemStack stack) {

        PersistentDataContainer container = stack.getItemMeta().getPersistentDataContainer();
        return container.has(genId);
    }

    private boolean canRun(User user) {

        return user.getGenLocations().size() < user.getMaxGeneratorSlots();
    }
}
