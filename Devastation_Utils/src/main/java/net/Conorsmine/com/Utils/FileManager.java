package net.Conorsmine.com.Utils;

import net.Conorsmine.com.Main;
import net.Conorsmine.com.Utils.MagicBlocks.MagicBlocks;
import net.Conorsmine.com.Utils.ResourceNode.NodeChance;
import net.Conorsmine.com.Utils.ResourceNode.ResourceNode;
import org.bukkit.Location;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class FileManager {

    public void createSettingsFile(String fileName) {
        JSONObject mainObj = new JSONObject();

        mainObj.put("setup", createSetup());
        mainObj.put("teams", createTeams());
        mainObj.put("maze_gen", createMazeConfig());
        mainObj.put("resource_nodes", createResourceNodes());



        File f = new File(Main.FOLDER_PATH + fileName + ".json");

        try {
            FileWriter writer = new FileWriter(f);
            writer.write(mainObj.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject createSetup() {
        JSONObject setup = new JSONObject();
        Location spawn = MagicBlocks.getMapSpawn();

        setup.put("game_time", 1800);
        setup.put("team_lifes", 20);
        setup.put("map_spawn", createPosition((spawn.getX() + 0.5), (spawn.getY() + 0.2), (spawn.getZ() + 0.5), 0.0));

        return setup;
    }

    private JSONObject createTeams() {
        JSONObject teams = new JSONObject();
        Location bSpawn = MagicBlocks.getBlueSpawn();
        Location rSpawn = MagicBlocks.getRedSpawn();

        teams.put("blue_spawn", createPosition((bSpawn.getX() + 0.5), (bSpawn.getY() + 0.2), (bSpawn.getZ() + 0.5), 0.0));
        teams.put("red_spawn", createPosition((rSpawn.getX() + 0.5), (rSpawn.getY() + 0.2), (rSpawn.getZ() + 0.5), 180.0));

        return teams;
    }

    private JSONObject createMazeConfig() {
        JSONObject mazeConfig = new JSONObject();
        Maze mazeSettings = Maze.getMaze();
        Location mazeSpawn = mazeSettings.getOrigin();

        mazeConfig.put("maze_spawn", createPosition(mazeSpawn.getBlockX(), mazeSpawn.getBlockY(), mazeSpawn.getBlockZ()));
        mazeConfig.put("maze_width", mazeSettings.getWidth());
        mazeConfig.put("maze_height", mazeSettings.getHeight());
        mazeConfig.put("maze_wall_thiccness", mazeSettings.getWallThiccness());
        mazeConfig.put("maze_wall_height", mazeSettings.getWallHeight());
        mazeConfig.put("maze_wall_breakable", false);

        return mazeConfig;
    }

    private JSONObject createResourceNodes() {
        JSONObject resourceNodes = new JSONObject();

        resourceNodes.put("respawn_time", 25);

        for (ResourceNode node : ResourceNode.nodes) {
            resourceNodes.put(UUID.randomUUID().toString(), createNode(node));
        }

        return resourceNodes;
    }

    private JSONObject createNode(ResourceNode currentNode) {
        JSONObject node = new JSONObject();

        final Location loc = currentNode.getLoc();
        node.put("resource_node_spawn", createPosition((int)loc.getX(), (int)loc.getY(), (int)loc.getZ()));
        node.put("bedrock", false);

        HashMap<String, Integer> spawnChances = new HashMap<>();
        for (NodeChance chance : currentNode.getNodeSpawnChanes()) {
            spawnChances.put(chance.getMat().name(), chance.getChance());
        }
        node.put("chances", spawnChances);

        return node;
    }



    protected HashMap<String, Double> createPosition(double x, double y, double z, double yaw) {
        return new HashMap<String, Double>() {{
            put("pos_x", x);
            put("pos_y", y);
            put("pos_z", z);
            put("facing", yaw);
        }};
    }

    protected HashMap<String, Integer> createPosition(int x, int y, int z) {
        return new HashMap<String, Integer>() {{
            put("pos_x", x);
            put("pos_y", y);
            put("pos_z", z);
        }};
    }

}
