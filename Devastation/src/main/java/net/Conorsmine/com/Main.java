package net.Conorsmine.com;

import net.Conorsmine.com.GameManagers.RespawnManager.ResourceEntity;
import net.Conorsmine.com.GameManagers.RespawnManager.ResourceManager;
import net.Conorsmine.com.WorldSetup.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Main extends JavaPlugin {

    public static JavaPlugin INSTANCE;
    public static final String PREFIX = "§7[§6Devastation§7]§r";
    public static final String MAP_1 = "map1", MAP_2 = "map-1";
    public static String MAP_FOLDER_PATH, WORLD_SAVES_PATH;

    public static void main(String[] args) {
    }

    public void onLoad() {
        setupFiles();
    }


    public void onEnable() {
        INSTANCE = this;
        MAP_FOLDER_PATH = this.getDataFolder().getAbsolutePath()  + File.separator + "Maps"  + File.separator;
        WORLD_SAVES_PATH = getServer().getWorldContainer().getAbsolutePath();
        WORLD_SAVES_PATH = WORLD_SAVES_PATH.substring(0, WORLD_SAVES_PATH.length() - 1);

        new ResourceEntity(new Location(null, -8, 28, 7), EntityType.CHICKEN);

        new WorldManager();
        ResourceManager.startRespawn();

        this.getServer().getPluginManager().registerEvents(new Events(), this);
        CommandManager.registerCommands(this);

        Bukkit.getServer().getLogger().info(PREFIX + " §aStarted successfully.");
    }


    @Override
    public void onDisable() {
        WorldManager.cleanup();

        Bukkit.getServer().getLogger().info(PREFIX + " §aStopped successfully.");
    }




    private void setupFiles() {
        new File(this.getDataFolder().getAbsolutePath() + File.separator + "Devastation" + File.separator + "Maps").mkdir();
    }
}
