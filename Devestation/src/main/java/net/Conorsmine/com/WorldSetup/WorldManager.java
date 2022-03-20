package net.Conorsmine.com.WorldSetup;

import net.Conorsmine.com.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class WorldManager {

    private static String currentWorld, nextWorld;
    private static MapWorld currentMap, nextMap;
    // Idea: Create two worlds for smooth transition between both ?
    // 2 days later: I should have just made a lobby, would have been easier XD


    public WorldManager() {
    }

    public static void createWorlds() {
        currentWorld = Main.MAP_1;
        nextWorld = Main.MAP_2;

        currentMap = new MapWorld(Main.MAP_1);
        nextMap = new MapWorld(Main.MAP_2);

        // Stupidly enough it has to be in this order,
        // has to do with the resource nodes
        nextMap.createWorld();
        currentMap.createWorld();
    }

    public static void switchMaps() {
        currentMap.resetWorld();

        MapWorld prevMap = currentMap;
        currentMap = nextMap;
        nextMap = prevMap;

        String prevWorld = currentWorld;
        currentWorld = nextWorld;
        nextWorld = prevWorld;
    }

    public static void cleanup() {
        currentMap.deleteWorld();
        nextMap.deleteWorld();
    }

    public static World getCurrentWorld() {
        return Main.INSTANCE.getServer().getWorld(currentWorld);
    }

    public static World getNextWorld() {
        return Main.INSTANCE.getServer().getWorld(nextWorld);
    }

    public static MapWorld getCurrentMap() {
        return currentMap;
    }

    public static MapWorld getNextMap() {
        return nextMap;
    }
}
