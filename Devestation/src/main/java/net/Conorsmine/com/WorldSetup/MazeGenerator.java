package net.Conorsmine.com.WorldSetup;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MazeGenerator {

    private final int WIDTH, HEIGHT;
    private final boolean[] [] map;
    private final List<Cell> cellList = new ArrayList<>();
    private Cell firstCell;

    protected final Random rand = new Random();

    public MazeGenerator(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        map = new boolean[WIDTH * 2 + 1][HEIGHT * 2 + 1];
    }



    // Growing Tree Alg.
    public boolean[] [] generate() {
        // Pick random cell
        int cellX = rand.nextInt(WIDTH), cellY = rand.nextInt(HEIGHT);
        firstCell = new Cell(cellX * 2, cellY * 2);
        cellList.add(firstCell);
        map[cellX * 2] [cellY * 2] = true;

        while (cellList.size() != 0) {
            // Move to neighbour cell
            Cell lastCell = cellList.get(lastIndex());
            final int x = lastCell.getCellX(), y = lastCell.getCellY();
            int[] dir = getFreeNeighbour(lastCell);
            if (dir == null) { cellList.remove(lastIndex()); continue; }



            map[x + dir[0]][y + dir[1]] = true;
            map[x + (dir[0] * 2)][y + (dir[1] * 2)] = true;
            cellList.add(new Cell(x + (dir[0] * 2), y + (dir[1] * 2)));
        }

//        createImage("C:\\Users\\Conor\\Desktop\\Devestation\\src\\main\\resources\\");
        return map;
    }

    private int[] getFreeNeighbour(Cell search) {
        ArrayList<Direction> freeNeighbours = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            int x = search.getCellX() + (dir.getDir()[0] * 2), y = search.getCellY() + (dir.getDir()[1] * 2);
            if (0 > x || x > WIDTH * 2) continue;
            if (0 > y || y > HEIGHT * 2) continue;
            if (map[x] [y]) continue;
            freeNeighbours.add(dir);
        }

        if (freeNeighbours.isEmpty()) return null;

        return freeNeighbours.get(rand.nextInt(freeNeighbours.size())).getDir();
    }

    private int lastIndex() {
        return cellList.size() - 1;
    }

    private void createImage(final String path) {
        BufferedImage bImg = new BufferedImage(WIDTH * 2 + 3, HEIGHT * 2 + 3, BufferedImage.TYPE_INT_RGB);
        Graphics2D plane = bImg.createGraphics();

        // Background
        plane.setColor(Color.BLACK);
        plane.fillRect(0, 0, WIDTH * 2, HEIGHT * 2);


        // Actual map
        plane.setColor(Color.WHITE);
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                if (map[x] [y]) plane.drawRect(x + 1, y + 1, 0, 0);
            }
        }


        plane.setColor(Color.pink);
        plane.drawRect(firstCell.getCellX() + 1, firstCell.getCellY() + 1, 0, 0);

        plane.dispose();

        try {
            ImageIO.write(bImg, "png", new File(path + "maze.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Cell {
    private final int cellX, cellY;

    public Cell(final int x, final int y) {
        this.cellX = x;
        this.cellY = y;
    }

    public Cell(final int[] indexes) {
        this.cellX = indexes[0];
        this.cellY = indexes[1];
    }

    public int getCellX() {
        return cellX;
    }

    public int getCellY() {
        return cellY;
    }

}

enum Direction {

    UP(new int[]{0, -1}),
    DOWN(new int[]{0, 1}),
    LEFT(new int[]{-1, 0}),
    RIGHT(new int[]{1, 0});

    private final int[] dir;
    Direction(final int[] dir) {
        this.dir = dir;
    }

    public int[] getDir() {
        return dir;
    }
}
