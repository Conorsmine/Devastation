package net.Conorsmine.com.Utils;

import net.Conorsmine.com.GameManagers.RespawnManager.NodeChance;
import net.Conorsmine.com.Main;
import net.Conorsmine.com.WorldSetup.Maps;
import net.Conorsmine.com.WorldSetup.WorldManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
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

    public FileDeserializer(Maps map) {
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(Main.MAP_FOLDER_PATH + map.getMapFolder() + File.separator + "config.json");
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
        mapSpawn = getSpawn((JSONObject) setup.get("map_spawn"));
    }

    private void getTeamsConfig() {
        JSONObject teams = (JSONObject) mainObj.get("teams");

        blueSpawn = getSpawn((JSONObject) teams.get("blue_spawn"));
        redSpawn = getSpawn((JSONObject) teams.get("red_spawn"));
    }

    private void getMazeConfig() {
        JSONObject mazeGen = (JSONObject) mainObj.get("maze_gen");

        mazeOrigin = getLoc((JSONObject) mazeGen.get("maze_spawn"));
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
            int total = 0;

            // Get spawn chances
            LinkedList<NodeChance> chanceList = new LinkedList<>();
            JSONObject chances = (JSONObject) node.get("chances");
            for (Object value : chances.keySet()) {
                String mat = (String) value;
                Material resource = Material.valueOf(mat);
                chanceList.add(new NodeChance(Math.toIntExact((long) chances.get(mat)), total, resource));

                total += Math.toIntExact((long) chances.get(mat));
            }


            // Add to list for each location
            for (Object pos : (JSONArray) node.get("positions_list")) {
                nodeBlock.put(getLoc((JSONObject) pos), chanceList);
            }
        }

        this.nodeBlocks = nodeBlock;
    }



    private Location getSpawn(JSONObject spawn) {
        return new Location(null, (Double) spawn.get("pos_x"), (Double) spawn.get("pos_y"), (Double) spawn.get("pos_z"), ((Double) spawn.get("facing")).floatValue(), 0);
    }

    private Location getLoc(JSONObject loc) {
        return new Location(null, ltd(loc.get("pos_x")), ltd(loc.get("pos_y")), ltd(loc.get("pos_z")));
    }

    private double ltd(Object val) {
        return ((Long) val).doubleValue();
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
