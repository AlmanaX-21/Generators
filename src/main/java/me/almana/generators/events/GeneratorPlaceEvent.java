package me.almana.generators.events;

import me.almana.generators.models.Gen;
import me.almana.generators.models.User;
import me.almana.generators.utilities.SimpleUtilities;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import redempt.redlib.blockdata.BlockDataManager;
import redempt.redlib.blockdata.DataBlock;

import java.io.IOException;

public class GeneratorPlaceEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private User user;
    private Gen generator;
    private final Block block;
    private final BlockDataManager manager;
    private final String serverPrefix;

    public GeneratorPlaceEvent(User user, Gen generator, Block block, BlockDataManager manager, String serverPrefix) throws IOException {
        this.user = user;
        this.generator = generator;
        this.block = block;
        this.manager = manager;
        this.serverPrefix = serverPrefix;
        run();
    }

    private void run() throws IOException {

        Player player = user.asPlayer();
        user.getGenLocations().add(SimpleUtilities.serialiseLocation(block.getLocation()));
        DataBlock db = manager.getDataBlock(block);
        db.set("owner", player.getUniqueId().toString());
        db.set("id", generator.getId());
        player.sendRichMessage(serverPrefix + "Placed 1 of " + generator.getName() + "<green>. Generators placed " + user.getGenLocations().size() + "/" + user.getMaxGeneratorSlots());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Gen getGenerator() {
        return generator;
    }

    public void setGenerator(Gen generator) {
        this.generator = generator;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
