package net.Conorsmine.com;

import net.Conorsmine.com.GameManagers.Teams.TeamManager;
import net.Conorsmine.com.WorldSetup.WorldManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager {

    public static void registerCommands(JavaPlugin pl) {
        pl.getCommand("reset").setExecutor(new MapResetCommand());
        pl.getCommand("start").setExecutor(new GameStartCommand());
    }
}


class MapResetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;

        WorldManager.switchMaps();

        return true;
    }
}

class GameStartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        for (Player p : Main.INSTANCE.getServer().getOnlinePlayers()) {
            p.teleport(TeamManager.getPlayerTeam(p).getSpawn());
        }

        return true;
    }
}