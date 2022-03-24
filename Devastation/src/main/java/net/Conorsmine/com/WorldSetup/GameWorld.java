package net.Conorsmine.com.WorldSetup;

import net.Conorsmine.com.Main;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class GameWorld {

    private final String worldName;
    private World world;

    public GameWorld(String worldName, Maps map) {
        this.worldName = worldName;
        this.world = createWorld(map);
    }

    private World createWorld(Maps mapName) {
        try {
            FileUtils.copyDirectory(new File(Main.MAP_FOLDER_PATH + mapName.getMapFolder() + File.separator + "Map"), new File(Main.WORLD_SAVES_PATH + worldName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        WorldCreator creator = new WorldCreator(worldName)
                .generateStructures(false)
                .type(WorldType.FLAT)
                .environment(World.Environment.NORMAL)
                .generatorSettings("2;0;1;");

        Main.INSTANCE.getServer().createWorld(creator);
        return creator.createWorld();
    }

    public void deleteWorld() {
        unloadWorld();
        try {
            FileUtils.deleteDirectory(new File(Main.WORLD_SAVES_PATH + worldName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void unloadWorld() {
        for (Player p : world.getPlayers()) {
            p.teleport(WorldManager.getNextMap().getMapSpawn());
        }

        Main.INSTANCE.getServer().unloadWorld(worldName, false);
    }

    public String getWorldName() {
        return worldName;
    }

    public World getWorld() {
        return world;
    }
}
