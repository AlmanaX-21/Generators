package me.almana.generators.events;

import me.almana.generators.models.Gen;
import me.almana.generators.models.User;
import me.almana.generators.utilities.SimpleUtilities;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import redempt.redlib.blockdata.BlockDataManager;

import java.io.IOException;

public class GeneratorBreakEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final User user;
    private final Gen generator;
    private final Block block;
    private final BlockDataManager manager;
    private final String serverPrefix;

    public GeneratorBreakEvent(User user, Gen generator, Block block, BlockDataManager manager, String serverPrefix) throws IOException {
        this.user = user;
        this.generator = generator;
        this.block = block;
        this.manager = manager;
        this.serverPrefix = serverPrefix;

        run();
    }

    private void run() throws IOException {

        block.setType(Material.AIR);
        user.asPlayer().getInventory().addItem(generator.asItemStack());
        manager.getDataBlock(block).clear();
        user.getGenLocations().remove(SimpleUtilities.serialiseLocation(block.getLocation()));
        user.asPlayer().sendRichMessage(serverPrefix + "<green>Broke 1 of " + generator.getName() + " <green>. Generators left " + user.getGenLocations().size() + "/" + user.getMaxGeneratorSlots());
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
