package net.Conorsmine.com.GameManagers.RespawnManager;

import net.Conorsmine.com.Main;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.List;

public abstract class ResourceManager {

    private static final List<ResourceManager> respawnList = new LinkedList<>(),
    brokenList = new LinkedList<>();
    private static BukkitTask timer;

    public ResourceManager() {
        respawnList.add(this);
    }

    abstract void spawn();

    public static void startRespawn() {
        for (ResourceManager respawnable: respawnList) {
            respawnable.spawn();
        }

        timer = new BukkitRunnable() {
            @Override
            public void run() {
                final List<ResourceManager> brokenCopy = new LinkedList<>(brokenList);

                for (ResourceManager respawnable : brokenCopy) {
                    respawnable.spawn();
                    brokenList.remove(respawnable);
                }
            }
        }.runTaskTimer(Main.INSTANCE, 0, 20);
    }

    public static void stopRespawn() {
        timer.cancel();
    }

    public static void addBroken(ResourceManager respawnable) {
        brokenList.add(respawnable);
    }

    public static List<ResourceManager> getRespawnList() {
        return respawnList;
    }
}
