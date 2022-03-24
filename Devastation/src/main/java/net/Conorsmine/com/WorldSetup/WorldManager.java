package net.Conorsmine.com.WorldSetup;

import net.Conorsmine.com.Main;
import org.bukkit.*;

public class WorldManager {

    private static WorldManager INSTANCE;
    private static GameWorld currentWorld, nextWorld;
    private static Map currentMap, nextMap;
    // Idea: Create two worlds for smooth transition between both ?
    // 2 days later: I should have just made a lobby, would have been easier XD


    public WorldManager() {
        INSTANCE = this;
        setupWorlds();
    }

    private static void setupWorlds() {
        currentMap = new Map();
        nextMap = new Map();

        // Stupidly enough it has to be in this order,
        // has to do with the resource nodes
        currentWorld = new GameWorld(Main.MAP_1, currentMap.getMap());
        nextWorld = new GameWorld(Main.MAP_2, nextMap.getMap());

        currentMap.getMaze().place(currentWorld.getWorld());
        nextMap.getMaze().place(nextWorld.getWorld());
    }

    public static void switchMaps() {
        String worldName = currentWorld.getWorldName();
        currentWorld.deleteWorld();

        currentWorld = nextWorld;
        currentMap = nextMap;

        nextMap = new Map();
        nextWorld = new GameWorld(worldName, nextMap.getMap());
    }

    public static void cleanup() {
        currentWorld.deleteWorld();
        nextWorld.deleteWorld();
    }

    public static World getCurrentWorld() {
        return currentWorld.getWorld();
    }

    public static Map getCurrentMap() {
        return currentMap;
    }

    public static Map getNextMap() {
        return nextMap;
    }
}
