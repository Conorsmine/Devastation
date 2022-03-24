package net.Conorsmine.com.WorldSetup;

import net.Conorsmine.com.GameManagers.RespawnManager.NodeChance;
import net.Conorsmine.com.GameManagers.RespawnManager.ResourceManager;
import net.Conorsmine.com.GameManagers.RespawnManager.ResourceNode;
import net.Conorsmine.com.GameManagers.Teams.Team;
import net.Conorsmine.com.GameManagers.Teams.Teams;
import net.Conorsmine.com.Utils.FileDeserializer;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Map {

    private final Maps map;
    private final Location mapSpawn;
    private final Maze maze;
    private final Team redTeam, blueTeam;
    private final List<ResourceManager> respawnableList = new ArrayList<>();
    private static final Random rand = new Random();

    public Map() {
        this.map = getRandomMap();
        FileDeserializer des = new FileDeserializer(map);

        this.mapSpawn = des.getMapSpawn();
        this.maze = new Maze(des.getMazeWidth(), des.getMazeHeight(), des.getWallThiccness(), des.getWallHeight(), des.getMazeOrigin());
        this.redTeam = new Team(des.getRedSpawn(), Teams.RED, des.getTeamLifes());
        this.blueTeam = new Team(des.getBlueSpawn(), Teams.BLUE, des.getTeamLifes());

        for (java.util.Map.Entry<Location, LinkedList<NodeChance>> set : des.getNodeBlocks().entrySet()) {
            respawnableList.add(new ResourceNode(set.getKey(), set.getValue()));
        }
    }

    public static Maps getRandomMap() {
        return Maps.values()[rand.nextInt(Maps.values().length)];
    }

    public Maps getMap() {
        return map;
    }

    public Location getMapSpawn() {
        return mapSpawn;
    }

    public Maze getMaze() {
        return maze;
    }

    public List<ResourceManager> getRespawnableList() {
        return respawnableList;
    }

    public Team getRedTeam() {
        return redTeam;
    }

    public Team getBlueTeam() {
        return blueTeam;
    }
}
