package net.Conorsmine.com.WorldSetup;

import net.Conorsmine.com.GameManagers.RespawnManager.NodeChance;
import net.Conorsmine.com.GameManagers.RespawnManager.ResourceNode;
import net.Conorsmine.com.GameManagers.Teams.TeamManager;
import net.Conorsmine.com.Main;
import net.Conorsmine.com.Utils.FileDeserializer;
import net.Conorsmine.com.GameManagers.Sidebar;
import org.bukkit.*;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class MapWorld {

    private String worldName;
    private String mapName;
    private World map;
    private TeamManager teamManager;
    private FileDeserializer des;

    private int gameTime, teamLifes, resourceTime;
    private Location mapSpawn;
    private Location redSpawn, blueSpawn;
    private Location mazeOrigin;
    private int mazeWidth, mazeHeight, wallThiccness, wallHeight;
    private Scoreboard gameBoard;


    public MapWorld(String worldName) {
        mapName = selectRandomMap();
        this.des = new FileDeserializer(Main.MAP_FOLDER_PATH + mapName   + File.separator + "config.json");

        this.worldName = worldName;

        setVals();
    }

    private void setVals() {
        this.gameTime = des.getGameTime();
        this.teamLifes = des.getTeamLifes();
        this.mapSpawn = des.getMapSpawn();
        this.redSpawn = des.getRedSpawn();
        this.blueSpawn = des.getBlueSpawn();
        this.mazeOrigin = des.getMazeOrigin();
        this.mazeWidth = des.getMazeWidth();
        this.mazeHeight = des.getMazeHeight();
        this.wallThiccness = des.getWallThiccness();
        this.wallHeight = des.getWallHeight();
        this.resourceTime = des.getNodeRespawnTime();
    }

    private String selectRandomMap() {
        return Maps.values()[new Random().nextInt(Maps.values().length)].getMapFolder();
    }

    public World createWorld() {
        try {
            FileUtils.copyDirectory(new File(Main.MAP_FOLDER_PATH + mapName   + File.separator + "Map"), new File(Main.WORLD_SAVES_PATH + worldName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        WorldCreator creator = new WorldCreator(worldName)
                .generateStructures(false)
                .type(WorldType.FLAT)
                .environment(World.Environment.NORMAL)
                .generatorSettings("2;0;1;");

        Main.INSTANCE.getServer().createWorld(creator);
        map = creator.createWorld();


        gameSetup();

//        System.out.println("[CREATED] Created world " + worldName + " with the map " + mapName);

        return map;
    }

    public void deleteWorld() {
        unloadWorld();
        try {
            FileUtils.deleteDirectory(new File(Main.WORLD_SAVES_PATH + worldName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetWorld() {
        deleteWorld();

        mapName = selectRandomMap();
        des = new FileDeserializer(Main.MAP_FOLDER_PATH + mapName   + File.separator + "config.json");
        setVals();
        createWorld();
    }

    private void unloadWorld() {
        for (Player p : map.getPlayers()) {
            p.teleport(WorldManager.getNextMap().getMapSpawn());
        }

        Main.INSTANCE.getServer().unloadWorld(worldName, false);
    }

    private void gameSetup() {
        TeamManager.resetPlayerMap();
        for (Player p : Main.INSTANCE.getServer().getOnlinePlayers()) {
            TeamManager.playerMap.put(p.getUniqueId(), TeamManager.specTeam);
            p.getInventory().clear();
            TeamManager.giveSpecItem(p);
        }

        placeMaze(map, mazeWidth, mazeHeight, mazeOrigin.getBlockX(), mazeOrigin.getBlockY(), mazeOrigin.getBlockZ(), wallThiccness, wallHeight);
        for (Map.Entry<Location, LinkedList<NodeChance>> set : this.des.getNodeBlocks().entrySet()) {
            new ResourceNode(set.getKey(), set.getValue());
        }

        this.teamManager = new TeamManager();

        Sidebar.setManager(teamManager);
        gameBoard = Sidebar.create();
        Sidebar.update();
    }



    public String getWorldName() {
        return worldName;
    }

    public void placeMaze(World world, int width, int height, int startX, int startY, int startZ, int thiccness, int wallHeight) {
        boolean[] [] map = new MazeGenerator(width, height).generate();

        for (int y = 0; y < map.length; y++) {
            // Wall along the Z axis
            placeWall(world, startX, startY, startZ, -1, y, thiccness, wallHeight);

            for (int x = 0; x < map[y].length; x++) {
                if (x != map[y].length / 2) {
                    // Wall along the X axis
                    placeWall(world, startX, startY, startZ, x, -1, thiccness, wallHeight);
                    placeWall(world, startX, startY, startZ, x, map[y].length, thiccness, wallHeight);
                }
                if (map[x] [y]) continue;

                placeWall(world, startX, startY, startZ, x, y, thiccness, wallHeight);
            }

            // Wall along the Z axis
            placeWall(world, startX, startY, startZ, map.length, y, thiccness, wallHeight);
        }
    }

    private void placeWall(World world, int startX, int startY, int startZ, int x, int y, int thiccness, int height) {
        for (int width = 0; width < thiccness; width++) {
            for (int length = 0; length < thiccness; length++) {
                for (int h = 0; h < height; h++) {
                    new Location(world, (width + startX + x * thiccness), startY + h, (length + startZ + y * thiccness))
                            .getBlock().setType(Material.STONE_BRICKS);
                }
            }
        }
    }

    public String getMapName() {
        return mapName;
    }

    public FileDeserializer getDes() {
        return des;
    }

    public int getGameTime() {
        return gameTime;
    }

    public int getTeamLifes() {
        return teamLifes;
    }

    public Location getMapSpawn() {
        return new Location(map, mapSpawn.getX(), mapSpawn.getY(), mapSpawn.getZ(), mapSpawn.getYaw(), mapSpawn.getPitch());
    }

    public Location getRedSpawn() {
        return new Location(WorldManager.getCurrentWorld(), redSpawn.getX(), redSpawn.getY(), redSpawn.getZ(), redSpawn.getYaw(), redSpawn.getPitch());
    }

    public Location getBlueSpawn() {
        return new Location(WorldManager.getCurrentWorld(), blueSpawn.getX(), blueSpawn.getY(), blueSpawn.getZ(), blueSpawn.getYaw(), blueSpawn.getPitch());
    }

    public Location getMazeOrigin() {
        return mazeOrigin;
    }

    public int getMazeWidth() {
        return mazeWidth;
    }

    public int getMazeHeight() {
        return mazeHeight;
    }

    public int getWallThiccness() {
        return wallThiccness;
    }

    public int getWallHeight() {
        return wallHeight;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public Scoreboard getGameBoard() {
        return gameBoard;
    }

    public int getResourceTime() {
        return resourceTime;
    }
}
