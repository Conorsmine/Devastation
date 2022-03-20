package net.Conorsmine.com;

import net.Conorsmine.com.Utils.FileManager;
import net.Conorsmine.com.Utils.MagicBlocks.MagicBlockTypes;
import net.Conorsmine.com.Utils.MagicBlocks.MagicBlocks;
import net.Conorsmine.com.Utils.Maze;
import net.Conorsmine.com.Utils.MazeGenerator;
import net.Conorsmine.com.Utils.ResourceNode.ResourceNode;
import net.Conorsmine.com.Utils.ResourceNode.ResourceNodeItem;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.EnumUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Commands {

    public static void registerCommands(JavaPlugin pl) {
        pl.getCommand("gen").setExecutor(new MazeGenerationCommand());
        pl.getCommand("gen").setTabCompleter(new MazeGenerationCommand());

        pl.getCommand("node").setExecutor(new ResourceNodeCommands());
        pl.getCommand("node").setTabCompleter(new ResourceNodeCommands());

        pl.getCommand("magic").setExecutor(new MagicBlocksCommand());
        pl.getCommand("magic").setTabCompleter(new MagicBlocksCommand());

        pl.getCommand("createFile").setExecutor(new CreateFileCommand());
    }
}

class MazeGenerationCommand implements CommandExecutor, TabCompleter {

    @Override
    // /gen <startX> <startY> <startZ> <width> <height> <thiccness> <wallHeight>
    public boolean onCommand(CommandSender commandSender, Command command, String com, String[] args) {
        if (!(commandSender instanceof Player)) return false;
        if (!(args.length >= 7)) return false;
        for (int i = 0; i < 3; i++) { if (!isInt(args[i])) return false; }
        for (int i = 3; i < args.length; i++) { if (!isPosInt(args[i])) return false; }

        final int startX = Integer.parseInt(args[0]), startY = Integer.parseInt(args[1]), startZ = Integer.parseInt(args[2]),
                width = Integer.parseInt(args[3]), height = Integer.parseInt(args[4]), thicccness = Integer.parseInt(args[5]),
                wallHeight = Integer.parseInt(args[6]);

        placeFakeMaze((Player) commandSender, width, height, startX, startY, startZ, thicccness, wallHeight);

        new Maze(new Location(null, startX, startY, startZ), width, height, thicccness, wallHeight);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        Location targetBlock = getBlockLoc((Player) commandSender);
        int len = strings.length;

        switch (len) {
            case 1:
                return Collections.singletonList(String.valueOf(targetBlock.getBlockX()));
            case 2:
                return Collections.singletonList(String.valueOf(targetBlock.getBlockY()));
            case 3:
                return Collections.singletonList(String.valueOf(targetBlock.getBlockZ()));
            case 4:
                return Collections.singletonList("<width>");
            case 5:
                return Collections.singletonList("<height>");
            case 6:
                return Collections.singletonList("<thiccness>");
        }

        return null;
    }



    private boolean isPosInt(final String check) {
        // yes, yes I used regex
        return check.matches("^(\\+?\\d+)");
    }

    private boolean isInt(final String check) {
        return check.matches("^([+-]?\\d+)");
    }

    private Location getBlockLoc(Player p) {
        Block target = p.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        if (target == null) return p.getLocation();
        return target.getLocation();
    }

    public void placeFakeMaze(Player viewer, int width, int height, int startX, int startY, int startZ, int thiccness, int wallHeight) {
        boolean[] [] map = new MazeGenerator(width, height).generate();

        for (int y = 0; y < map.length; y++) {
            placeFakeWall(viewer, startX, startY, startZ, -1, y, thiccness, wallHeight);

            for (int x = 0; x < map[y].length; x++) {
                placeFakeWall(viewer, startX, startY, startZ, x, -1, thiccness, wallHeight);
                if (map[x] [y]) continue;

                placeFakeWall(viewer, startX, startY, startZ, x, y, thiccness, wallHeight);
                placeFakeWall(viewer, startX, startY, startZ, x, map.length, thiccness, wallHeight);
            }

            placeFakeWall(viewer, startX, startY, startZ, map[y].length, y, thiccness, wallHeight);
        }
    }

    private void placeFakeWall(Player p, int startX, int startY, int startZ, int x, int y, int thiccness, int height) {
        for (int width = 0; width < thiccness; width++) {
            for (int length = 0; length < thiccness; length++) {
                for (int h = 0; h < height; h++) {
                    p.sendBlockChange(new Location(p.getWorld(), (width + startX + x * thiccness), startY + h, (length + startZ + y * thiccness)), Material.DIAMOND_BLOCK.createBlockData());
                }
            }
        }
    }
}

class ResourceNodeCommands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        Player p = ((Player) commandSender);

        if (strings.length == 0) {
            addItemInfo(p.getInventory().getItemInMainHand());
        }
        else if (strings.length >= 1 && strings[0].equals("open")) {
            if (p.getInventory().getItemInMainHand().getType() == Material.AIR) { p.sendMessage("Please hold a resource node."); return false; }
            openResourceNodeOptions(p.getInventory().getItemInMainHand(), p);
        }


        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            return Collections.singletonList("open");
        }

        return null;
    }


    // /node
    private void addItemInfo(final ItemStack item) {
        ResourceNodeItem.createNode(item);
    }

    // /node open
    private void openResourceNodeOptions(final ItemStack item, final Player p) {
        ResourceNodeItem itemNode = ResourceNodeItem.isResourceNode(item);
        if (itemNode == null) return;

        Inventory inv = Main.INSTANCE.getServer().createInventory(p, 27, "§5Resource Node");

        for (Map.Entry<Material, Integer> set : itemNode.getNodeSpawnChanes().entrySet()) {
            for (int i = 0; i < set.getValue(); i++) {
                inv.addItem(new ItemStack(set.getKey()));
            }
        }

        p.openInventory(inv);
    }
}

class MagicBlocksCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(strings.length >= 1)) return false;
        if (!(commandSender instanceof Player)) return false;
        Player p = (Player) commandSender;
        ItemStack item = p.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) return false;
        if (!(EnumUtils.isValidEnum(MagicBlockTypes.class, strings[0]))) return false;

        MagicBlocks.createItem(item, MagicBlockTypes.valueOf(strings[0]));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            List<String> types = new LinkedList<>();
            for (MagicBlockTypes type : MagicBlockTypes.values()) {
                types.add(type.name());
            }
            return types;
        }

        return null;
    }
}

class CreateFileCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        Player p = (Player) commandSender;
        if (!(strings.length >= 1)) { p.sendMessage(Main.PREFIX + " §cUsage: /createFile <mapName>"); return false; }
        if (Maze.getMaze() == null) { p.sendMessage(Main.PREFIX + " §cNo maze origin has been selected, preferably use a magic block to define one or use /gen."); return false; }
        p.sendMessage(Main.PREFIX + " §cAre you sure about your settings ?\nFollowing settings are given:");
        p.sendMessage("§aMaze spawn origin: §7" + Maze.getMaze().getOrigin().toString());
        p.sendMessage("§aBlue spawn: §7" + MagicBlocks.getBlueSpawn());
        p.sendMessage("§aRed spawn: §7" + MagicBlocks.getRedSpawn());
        p.sendMessage("§6" + ResourceNode.nodes.size() + " §ahave been registered.");

        if (!(strings.length >= 2)) { p.sendMessage(Main.PREFIX + " §aUse \"/createFile <mapName> true\" to confirm your selection"); return false; }
        if (strings[1].equals("true")) {
            new FileManager().createSettingsFile(strings[0]);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 2) {
            return Collections.singletonList("true");
        }

        return null;
    }
}
