package net.Conorsmine.com.Utils;

import net.Conorsmine.com.GameManagers.ResourceNode.NodeChance;
import net.Conorsmine.com.WorldSetup.WorldManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.*;

public class FileDeserializer {

    private JSONObject mainObj;

    private int gameTime, teamLifes;
    private Location mapSpawn;
    private Location redSpawn, blueSpawn;
    private Location mazeOrigin;
    private int mazeWidth, mazeHeight, wallThiccness, wallHeight;
    private int nodeRespawnTime;
    private HashMap<Location, LinkedList<NodeChance>> nodeBlocks;

    public FileDeserializer(String mapFile) {
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(mapFile);
            this.mainObj = (JSONObject) parser.parse(reader);

            reader.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

        deserialize();
    }

    private void deserialize() {
        getSetupConfig();
        getTeamsConfig();
        getMazeConfig();
        getResourceNodes();
    }

    private void getSetupConfig() {
        JSONObject setup = (JSONObject) mainObj.get("setup");
        gameTime =  Math.toIntExact((Long) setup.get("game_time"));
        teamLifes =  Math.toIntExact((Long) setup.get("team_lifes"));
        double[] mS = getSpawn((JSONObject) setup.get("map_spawn"));
        mapSpawn = new Location(WorldManager.getCurrentWorld(), mS[0], mS[1], mS[2], (float)mS[3], 0);
    }

    private void getTeamsConfig() {
        JSONObject teams = (JSONObject) mainObj.get("teams");

        double[] bS = getSpawn((JSONObject) teams.get("blue_spawn"));
        blueSpawn = new Location(WorldManager.getCurrentWorld(), bS[0], bS[1], bS[2], (float)bS[3], 0);

        double[] rS = getSpawn((JSONObject) teams.get("red_spawn"));
        redSpawn = new Location(WorldManager.getCurrentWorld(), rS[0], rS[1], rS[2], (float)rS[3], 0);
    }

    private void getMazeConfig() {
        JSONObject mazeGen = (JSONObject) mainObj.get("maze_gen");

        double[] mO = getLoc((JSONObject) mazeGen.get("maze_spawn"));
        mazeOrigin = new Location(WorldManager.getCurrentWorld(), mO[0], mO[1], mO[2]);
        mazeWidth = Math.toIntExact((Long) mazeGen.get("maze_width"));
        mazeHeight = Math.toIntExact((Long) mazeGen.get("maze_height"));
        wallThiccness = Math.toIntExact((Long) mazeGen.get("maze_wall_thiccness"));
        wallHeight = Math.toIntExact((Long) mazeGen.get("maze_wall_height"));
    }

    private void getResourceNodes() {
        JSONObject nodes = (JSONObject) mainObj.get("resource_nodes");

        nodeRespawnTime = Math.toIntExact((long) nodes.get("respawn_time"));
        HashMap<Location, LinkedList<NodeChance>> nodeBlock = new HashMap<>();

        for (Object o : nodes.keySet()) {
            String key = (String) o;
            if (key.equals("respawn_time")) continue;

            JSONObject node = (JSONObject) nodes.get(key);
            double[] nL = getLoc((JSONObject) node.get("resource_node_spawn"));
            Location loc = new Location(WorldManager.getCurrentWorld(), nL[0], nL[1], nL[2]);
            int total = 0;
            LinkedList<NodeChance> chanceList = new LinkedList<>();

            JSONObject chances = (JSONObject) node.get("chances");
            for (Object value : chances.keySet()) {
                String mat = (String) value;
                Material resource = Material.valueOf(mat);
                chanceList.add(new NodeChance(Math.toIntExact((long) chances.get(mat)), total, resource));

                total += Math.toIntExact((long) chances.get(mat));
            }

            nodeBlock.put(loc, chanceList);
        }

        this.nodeBlocks = nodeBlock;
    }



    private double[] getSpawn(JSONObject spawn) {
        double[] info = new double[4];

        info[0] = ((double) spawn.get("pos_x"));
        info[1] = ((double) spawn.get("pos_y"));
        info[2] = ((double) spawn.get("pos_z"));
        info[3] = ((double) spawn.get("facing"));

        return info;
    }

    private double[] getLoc(JSONObject loc) {
        double[] info = new double[4];

        info[0] = ((Long) loc.get("pos_x")).doubleValue();
        info[1] = ((Long) loc.get("pos_y")).doubleValue();
        info[2] = ((Long) loc.get("pos_z")).doubleValue();

        return info;
    }



    public int getGameTime() {
        return gameTime;
    }

    public int getTeamLifes() {
        return teamLifes;
    }

    public Location getMapSpawn() {
        return mapSpawn;
    }

    public Location getRedSpawn() {
        return redSpawn;
    }

    public Location getBlueSpawn() {
        return blueSpawn;
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

    public int getNodeRespawnTime() {
        return nodeRespawnTime;
    }

    public HashMap<Location, LinkedList<NodeChance>> getNodeBlocks() {
        return nodeBlocks;
    }
}
