package net.Conorsmine.com.GameManagers.Teams;

import org.bukkit.ChatColor;

public enum Teams {

    SPEC(ChatColor.AQUA),
    RED(ChatColor.RED),
    BLUE(ChatColor.BLUE);

    final ChatColor col;
    Teams(ChatColor col) {
        this.col = col;
    }

    public ChatColor getCol() {
        return col;
    }
}
