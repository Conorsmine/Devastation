package net.Conorsmine.com.Utils;

import org.bukkit.Location;

import java.util.LinkedList;
import java.util.List;

public class Maze {

    private final Location origin;
    private final int width, height, wallThiccness, wallHeight;
    private static Maze maze;

    public Maze(Location origin, int width, int height, int wallThiccness, int wallHeight) {
        this.origin = origin;
        this.width = width;
        this.height = height;
        this.wallThiccness = wallThiccness;
        this.wallHeight = wallHeight;

        maze = this;
    }

    public Location getOrigin() {
        return origin;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getWallThiccness() {
        return wallThiccness;
    }

    public int getWallHeight() {
        return wallHeight;
    }

    public static Maze getMaze() {
        return maze;
    }
}
