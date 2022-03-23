package net.Conorsmine.com.GameManagers.Teams;

import org.bukkit.ChatColor;

import java.util.Random;

public enum Teams {

    SPEC(ChatColor.AQUA),
    RED(ChatColor.RED),
    BLUE(ChatColor.BLUE);

    private static final Random rand = new Random();
    final ChatColor col;
    Teams(ChatColor col) {
        this.col = col;
    }

    public ChatColor getCol() {
        return col;
    }
}
