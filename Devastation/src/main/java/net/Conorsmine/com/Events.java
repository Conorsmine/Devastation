package net.Conorsmine.com;

import net.Conorsmine.com.GameManagers.RespawnManager.ResourceEntity;
import net.Conorsmine.com.GameManagers.RespawnManager.ResourceNode;
import net.Conorsmine.com.GameManagers.RespawnManager.ResourceManager;
import net.Conorsmine.com.GameManagers.Teams.TeamManager;
import net.Conorsmine.com.WorldSetup.WorldManager;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class Events implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent ev) {
        TeamManager.onJoinEvent(ev);
        ev.getPlayer().setScoreboard(WorldManager.getCurrentMap().getGameBoard());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent ev) {
        TeamManager.onPlayerDeathEvent(ev);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent ev) {
        ResourceEntity.onDeath(ev);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent ev) {
        TeamManager.onPlayerRespawn(ev);
    }

    @EventHandler
    public void onChatMsg(AsyncPlayerChatEvent ev) {
        TeamManager.onChatMsg(ev);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent ev) {
        if (ev.getBlock().getLocation().getBlockY() > 36) { ev.setCancelled(true); return;}

        ev.getBlock().setMetadata("PLAYERPLACED", new FixedMetadataValue(Main.INSTANCE, true));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent ev) {
        Block block = ev.getBlock();
        ResourceNode node = ResourceNode.isResourceNode(block);
        if (block.hasMetadata("PLAYERPLACED") && block.getMetadata("PLAYERPLACED").get(0).asBoolean()) return;
        if (node == null) { ev.setCancelled(true); return; }

//        ev.getBlock().setType(Material.BEDROCK); This doesn't work for some reason
        ResourceManager.addBroken(node);
    }

    @EventHandler
    public void onItemInteract(PlayerInteractEvent ev) {
        TeamManager.onItemUse(ev);
    }
}
