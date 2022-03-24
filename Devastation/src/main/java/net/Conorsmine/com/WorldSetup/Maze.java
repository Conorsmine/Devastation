package net.Conorsmine.com.WorldSetup;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class Maze {

    private final int width, height;
    private final int wallThiccness, wallHeight;
    private final Location origin;
    private final boolean[] [] mazeMap;

    public Maze(int width, int height, int wallThiccness, int wallHeight, Location origin) {
        this.width = width;
        this.height = height;
        this.wallThiccness = wallThiccness;
        this.wallHeight = wallHeight;
        this.origin = origin;

        this.mazeMap = new MazeGenerator(width, height).generate();
    }



    public void place(World world) {
        boolean[] [] map = new MazeGenerator(this.width, this.height).generate();

        for (int y = 0; y < map.length; y++) {
            // Wall along the Z axis
            placeWall(world, -1, y);

            for (int x = 0; x < map[y].length; x++) {
                if (x != map[y].length / 2) {
                    // Wall along the X axis
                    placeWall(world, x, -1);
                    placeWall(world, x, map[y].length);
                }
                if (map[x] [y]) continue;

                placeWall(world, x, y);
            }

            // Wall along the Z axis
            placeWall(world, map.length, y);
        }
    }

    private void placeWall(World world, int x, int y) {
        for (int width = 0; width < this.wallThiccness; width++) {
            for (int length = 0; length < this.wallThiccness; length++) {
                for (int h = 0; h < height; h++) {
                    new Location(world, (width + this.origin.getBlockX() + x * this.wallThiccness),
                            this.origin.getBlockY() + h, (length + this.origin.getBlockZ() + y * this.wallThiccness))
                            .getBlock().setType(Material.STONE_BRICKS);
                }
            }
        }
    }

}
