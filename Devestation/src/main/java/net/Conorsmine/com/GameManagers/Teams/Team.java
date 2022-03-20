package net.Conorsmine.com.GameManagers.Teams;

import net.Conorsmine.com.WorldSetup.WorldManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.LinkedList;

public class Team {

    private final Location spawn;
    private final Teams team;
    private final ChatColor teamCol;
    private final int maxLifes;
    private int lifes;
    private LinkedList<Player> teamList = new LinkedList<>();

    public Team(Location spawn, Teams team, int maxLifes) {
        this.spawn = spawn;
        this.team = team;
        this.teamCol = team.getCol();
        this.maxLifes = maxLifes;
        this.lifes = maxLifes;
    }

    public void addPlayer(Player p) {
        this.teamList.add(p);
        TeamManager.playerMap.put(p.getUniqueId(), this);
        p.setCustomName(teamCol + p.getName());
        p.setCustomNameVisible(true);
    }

    public void removePlayer(Player p) {
        this.teamList.remove(p);
    }

    public Location getSpawn() {
        return spawn;
    }

    public ChatColor getTeamCol() {
        return teamCol;
    }

    public int getMaxLifes() {
        return maxLifes;
    }

    public int getLifes() {
        return lifes;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
    }

    public void removeLifes(int amount) {
        this.lifes -= amount;
    }

    public LinkedList<Player> getTeamList() {
        return teamList;
    }

    public Teams getTeam() {
        return team;
    }
}
