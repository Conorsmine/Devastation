package net.Conorsmine.com.GameManagers;

import net.Conorsmine.com.GameManagers.Teams.TeamManager;
import net.Conorsmine.com.Main;
import net.Conorsmine.com.WorldSetup.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.UUID;

public class Sidebar {

//    protected static TeamManager manager = WorldManager.getCurrentMap().getTeamManager();

    public static Scoreboard create() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective setup = scoreboard.registerNewObjective("Info", "dummy", ChatColor.GOLD + "§nDevastation", RenderType.INTEGER);
        setup.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score dummy = setup.getScore("");
        Score timer = setup.getScore(ChatColor.GREEN + "00" + ChatColor.GOLD + ":" + ChatColor.GREEN + "30");
//        Score redLifes = setup.getScore(ChatColor.RED + "Remaining: " + ChatColor.RESET + manager.getRedTeam().getLifes());
//        Score blueLifes = setup.getScore(ChatColor.BLUE + "Remaining: " + ChatColor.RESET + manager.getBlueTeam().getLifes());

        dummy.setScore(6);
        timer.setScore(5);
        dummy.setScore(4);
//        redLifes.setScore(3);
//        blueLifes.setScore(2);

        return scoreboard;
    }

    public static void update() {
        Scoreboard gameInfo = Sidebar.create();

        for (Player p : Main.INSTANCE.getServer().getOnlinePlayers()) {
            p.setScoreboard(gameInfo);
        }
    }

//    public static void setManager(TeamManager manager) {
//        Sidebar.manager = manager;
//    }
}
