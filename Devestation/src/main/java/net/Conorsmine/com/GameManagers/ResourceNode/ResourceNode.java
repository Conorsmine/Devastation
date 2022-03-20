package net.Conorsmine.com.GameManagers.ResourceNode;

import net.Conorsmine.com.Main;
import net.Conorsmine.com.WorldSetup.WorldManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ResourceNode {

    private int totalChance = 0;
    private final Location loc;
    private final LinkedList<NodeChance> nodeSpawnChanes;
    public static ArrayList<ResourceNode> nodes = new ArrayList<>();
    private static LinkedList<ResourceNode> broken = new LinkedList<>();
    private static final Random rand = new Random();

    public ResourceNode(final ResourceNodeItem nodeItem, final Location loc) {
        this.nodeSpawnChanes = calcSpawnChance(nodeItem.getNodeSpawnChanes());
        this.loc = loc;

        nodes.add(this);
    }

    public ResourceNode(Location loc, LinkedList<NodeChance> nodeSpawnChanes) {
        this.loc = loc;
        this.nodeSpawnChanes = nodeSpawnChanes;

        for (NodeChance chance : nodeSpawnChanes) {
            totalChance += chance.getChance();
        }

        nodes.add(this);
    }

    public Material getRandomResource() {
        final int i = rand.nextInt(totalChance);
        for (NodeChance chance : nodeSpawnChanes) {
            if (chance.getLowVal() <= i && i < chance.getHighVal()) return chance.getMat();
        }

        return Material.STONE;
    }

    public int getTotalChance() {
        return totalChance;
    }

    public Location getLoc() {
        return loc;
    }

    public LinkedList<NodeChance> getNodeSpawnChanes() {
        return nodeSpawnChanes;
    }

    public void respawn() {
        new Location(WorldManager.getCurrentWorld(), loc.getX(), loc.getY(), loc.getBlockZ()).getBlock().setType(getRandomResource());
    }

    public LinkedList<NodeChance> calcSpawnChance(HashMap<Material, Integer> nodeSpawnChances) {
        LinkedList<NodeChance> chanceList = new LinkedList<>();

        for (Map.Entry<Material, Integer> set : nodeSpawnChances.entrySet()) {
            chanceList.add(new NodeChance(set.getValue(), totalChance, set.getKey()));
            totalChance += set.getValue();
        }

        return chanceList;
    }



    public static void resetNodes() {
        nodes.clear();
    }

    public static void addBroken(ResourceNode node) {
        broken.add(node);
    }

    public static void startTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<ResourceNode> breakList = new ArrayList<>(broken);

                for (int i = 0; i < breakList.size(); i++) {
                    breakList.get(i).respawn();
                    breakList.remove(i);
                    broken.remove(0);
                }
            }

        }.runTaskTimer(Main.INSTANCE, 0, WorldManager.getCurrentMap().getResourceTime());
    }

    public static ResourceNode isResourceNode(final Block block) {
        for (ResourceNode node : nodes) {
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
