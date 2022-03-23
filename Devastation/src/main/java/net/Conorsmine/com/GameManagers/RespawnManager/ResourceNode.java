package net.Conorsmine.com.GameManagers.RespawnManager;

import net.Conorsmine.com.WorldSetup.WorldManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.*;

public class ResourceNode extends ResourceManager {

    private int totalChance;
    private final Location loc;
    private final LinkedList<NodeChance> nodeSpawnChanes;
    private static final Random rand = new Random();

    public ResourceNode(Location loc, LinkedList<NodeChance> nodeSpawnChanes) {
        this.loc = loc;
        this.nodeSpawnChanes = nodeSpawnChanes;

        for (NodeChance chance : nodeSpawnChanes) {
            totalChance += chance.getChance();
        }
    }

    @Override
    public void spawn() {
        loc.setWorld(WorldManager.getCurrentWorld());
        loc.getBlock().setType(getRandomResource());
    }

    public Material getRandomResource() {
        final int i = rand.nextInt(totalChance);
        for (NodeChance chance : nodeSpawnChanes) {
            if (chance.getLowVal() <= i && i < chance.getHighVal()) return chance.getMat();
        }

        return Material.STONE;
    }

    public Location getLoc() {
        return loc;
    }

    public static ResourceNode isResourceNode(final Block block) {
        for (ResourceManager respawnable : ResourceManager.getRespawnList()) {
            if (!(respawnable instanceof ResourceNode)) continue;
            ResourceNode node = (ResourceNode) respawnable;
            if (!isLocIgnoreWorld(block.getLocation(), node.getLoc())) continue;

            return node;
        }

        return null;
    }

    protected static boolean isLocIgnoreWorld(final Location loc1, final Location loc2) {
        return loc1.getX() == loc2.getX() &&
                loc1.getY() == loc2.getY() &&
                loc1.getZ() == loc2.getZ();
    }
}
