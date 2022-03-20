package net.Conorsmine.com;

import net.Conorsmine.com.Utils.MagicBlocks.MagicBlocks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class Events implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent ev) {
        MagicBlocks.onBlockPlace(ev);
    }
}
