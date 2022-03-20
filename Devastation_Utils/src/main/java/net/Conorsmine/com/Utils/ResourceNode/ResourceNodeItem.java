package net.Conorsmine.com.Utils.ResourceNode;

import net.Conorsmine.com.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ResourceNodeItem {

    private final UUID uuid;
    private HashMap<Material, Integer> nodeSpawnChanes = new HashMap();
    private static final HashMap<UUID, ResourceNodeItem> itemMap = new HashMap<>();

    public ResourceNodeItem() {
        this.uuid = UUID.randomUUID();

        itemMap.put(uuid, this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public static HashMap<UUID, ResourceNodeItem> getItemMap() {
        return itemMap;
    }

    public HashMap<Material, Integer> getNodeSpawnChanes() {
            return nodeSpawnChanes;
    }

    public static ResourceNodeItem createNode(final ItemStack item) {
        ResourceNodeItem nodeItem = new ResourceNodeItem();

        ItemMeta it = item.getItemMeta();
        it.setLore(Collections.singletonList("§5Resource Node"));
        it.addEnchant(Enchantment.CHANNELING, 1, false);
        it.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        PersistentDataContainer container = it.getPersistentDataContainer();
        container.set(new NamespacedKey(Main.INSTANCE, "RESOURCEITEMID"), PersistentDataType.STRING, nodeItem.getUuid().toString());

        item.setItemMeta(it);
        return nodeItem;
    }

    public void setNodeItems(final Player p, final Inventory inv) {
        nodeSpawnChanes = new HashMap<>();

        if (inv.getContents().length == 0) { nodeSpawnChanes.put(p.getInventory().getItemInMainHand().getType(), 1); return; }
        for (ItemStack item : inv.getContents()) {
            if (item == null) continue;
            nodeSpawnChanes.put(item.getType(), item.getAmount());
        }
    }

    public static ResourceNodeItem isResourceNode(@Nonnull final ItemStack item) {
        if (!Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().has(new NamespacedKey(Main.INSTANCE, "RESOURCEITEMID"), PersistentDataType.STRING)) return null;
        final UUID uuid = UUID.fromString(Objects.requireNonNull(item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.INSTANCE, "RESOURCEITEMID"), PersistentDataType.STRING)));
        if (!itemMap.containsKey(uuid)) return null;

        return itemMap.get(uuid);
    }
}
