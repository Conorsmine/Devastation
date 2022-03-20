package net.Conorsmine.com;

import net.Conorsmine.com.GameManagers.ResourceNode.ResourceNode;
import net.Conorsmine.com.GameManagers.ResourceNode.ResourceNodeItem;
import net.Conorsmine.com.GameManagers.Teams.TeamManager;
import net.Conorsmine.com.WorldSetup.WorldManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;

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
    public void onPlayerRespawn(PlayerRespawnEvent ev) {
        TeamManager.onPlayerRespawn(ev);
    }

    @EventHandler
    public void onChatMsg(AsyncPlayerChatEvent ev) {
        TeamManager.onChatMsg(ev);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent ev) {
        ItemStack item = ev.getPlayer().getInventory().getItemInMainHand();

        if (ev.getBlock().getLocation().getBlockY() > 36) { ev.setCancelled(true); return;}

        if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.INSTANCE, "RESOURCEITEMID"), PersistentDataType.STRING)) {
            ev.getPlayer().sendMessage(Main.PREFIX + " §aPlaced a resource node block.");
            new ResourceNode(ResourceNodeItem.isResourceNode(item), ev.getBlock().getLocation());
        }

        ev.getBlock().setMetadata("PLAYERPLACED", new FixedMetadataValue(Main.INSTANCE, true));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent ev) {
        Block block = ev.getBlock();
        ResourceNode node = ResourceNode.isResourceNode(block);
        // Can break resource nodes with a resource node item in hand
        ItemStack item = ev.getPlayer().getInventory().getItemInMainHand();
        if (item.getType() != Material.AIR) {
            if (ResourceNodeItem.isResourceNode(item) != null && node != null) {
                ResourceNode.nodes.remove(node);
                ev.getPlayer().sendMessage(Main.PREFIX + " §cRemoved a resource node.");

                return;
            }
        }
        if (block.hasMetadata("PLAYERPLACED") && block.getMetadata("PLAYERPLACED").get(0).asBoolean()) return;
        if (node == null) { ev.setCancelled(true); return; }
        if (item.getType() != Material.AIR) { block.breakNaturally(item); }
        else { block.breakNaturally(); }

//        ev.getBlock().setType(Material.BEDROCK); This doesn't work for some reason
        ResourceNode.addBroken(node);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent ev) {
        if (!ev.getView().getTitle().equals("§5Resource Node")) return;

        ResourceNodeItem.isResourceNode(ev.getPlayer().getInventory().getItemInMainHand()).setNodeItems((Player) ev.getPlayer(), ev.getInventory());
    }

    @EventHandler
    public void onItemInteract(PlayerInteractEvent ev) {
        TeamManager.onItemUse(ev);
    }
}
