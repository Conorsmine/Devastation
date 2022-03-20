package net.Conorsmine.com.Utils.MagicBlocks;

import net.Conorsmine.com.Main;
import net.Conorsmine.com.Utils.Maze;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MagicBlocks {

    public static final NamespacedKey NAMESPACEDKEY = new NamespacedKey(Main.INSTANCE, "MAGICBLOCKTYPE");
    protected static Location defaultLoc = new Location(Main.INSTANCE.getServer().getWorlds().get(0), 0, 0, 0);
    private static Location mapSpawn = defaultLoc, redSpawn = defaultLoc, blueSpawn = defaultLoc;
    private static final List<Location> holeList = new ArrayList<>();



    public static void createItem(ItemStack item, MagicBlockTypes type) {
        ItemMeta it = item.getItemMeta();
        it.setDisplayName("§5" + type.name());
        it.setLore(Collections.singletonList("§6Magic Block"));
        it.addEnchant(Enchantment.CHANNELING, 1, false);
        it.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        it.getPersistentDataContainer().set(NAMESPACEDKEY, PersistentDataType.STRING, type.name());
        item.setItemMeta(it);
    }

    public static void onBlockPlace(BlockPlaceEvent ev) {
        ItemStack item = ev.getItemInHand();
        if (item.getType() == Material.AIR) return;
        if (!item.getItemMeta().getPersistentDataContainer().has(NAMESPACEDKEY, PersistentDataType.STRING)) return;

        MagicBlockTypes type = MagicBlockTypes.valueOf(item.getItemMeta().getPersistentDataContainer().get(NAMESPACEDKEY, PersistentDataType.STRING));
        Location loc = ev.getBlock().getLocation();
        Player p = ev.getPlayer();

        switch (type) {
            case MAP_SPAWN:
                mapSpawn = loc;
                p.sendMessage(Main.PREFIX + " §aPlaced map spawn.");
                break;
            case MAZE_GEN_ORIGIN:
                new Maze(loc, 10, 10, 1, 1);
                p.sendMessage(Main.PREFIX + " §aPlaced maze spawn. Use /gen to check the generation.");
                break;
            case RED_SPAWN:
                redSpawn = loc;
                p.sendMessage(Main.PREFIX + " §aPlaced red spawn.");
                break;
            case BLUE_SPAWN:
                blueSpawn = loc;
                p.sendMessage(Main.PREFIX + " §aPlaced blue spawn.");
                break;
            case MAZE_HOLE:
                holeList.add(loc);
                p.sendMessage(Main.PREFIX + " §aPlaced maze hole.");
                break;
        }

    }

    public static Location getMapSpawn() {
        return mapSpawn;
    }

    public static Location getRedSpawn() {
        return redSpawn;
    }

    public static Location getBlueSpawn() {
        return blueSpawn;
    }

    public static List<Location> getHoleList() {
        return holeList;
    }
}
