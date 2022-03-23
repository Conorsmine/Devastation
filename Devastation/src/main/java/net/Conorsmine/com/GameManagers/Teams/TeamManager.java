package net.Conorsmine.com.GameManagers.Teams;

import net.Conorsmine.com.GameManagers.Sidebar;
import net.Conorsmine.com.Main;
import net.Conorsmine.com.WorldSetup.MapWorld;
import net.Conorsmine.com.WorldSetup.WorldManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class TeamManager {

    private final Team redTeam, blueTeam;
    public static Team specTeam;
    public static HashMap<UUID, Team> playerMap = new HashMap<>();

    public TeamManager() {
        redTeam = new Team(WorldManager.getCurrentMap().getRedSpawn(), Teams.RED, WorldManager.getCurrentMap().getTeamLifes());
        blueTeam = new Team(WorldManager.getCurrentMap().getBlueSpawn(), Teams.BLUE, WorldManager.getCurrentMap().getTeamLifes());
        TeamManager.specTeam = new Team(WorldManager.getCurrentMap().getMapSpawn(), Teams.SPEC, 999);
    }

    public static void giveSpecItem(Player p) {
        ItemStack selector = new ItemStack(Material.PAPER);
        ItemMeta it = selector.getItemMeta();
        it.setDisplayName("§7Team Selector");
        selector.setItemMeta(it);

        p.getInventory().setItem(4, selector);
    }

    public Team getRedTeam() {
        return redTeam;
    }

    public Team getBlueTeam() {
        return blueTeam;
    }

    public static Teams getPlayerTeamType(Player p) {
        return playerMap.get(p.getUniqueId()).getTeam();
    }

    public static Team getPlayerTeam(Player p) {
        return playerMap.get(p.getUniqueId());
    }

    public static void resetPlayerMap() {
        playerMap.clear();
    }

    public static void onJoinEvent(PlayerJoinEvent ev) {
        Player p = ev.getPlayer();
        UUID uuid = p.getUniqueId();
        MapWorld map = WorldManager.getCurrentMap();
        if (playerMap.containsKey(uuid)) {
            Teams team = getPlayerTeamType(p);

            if (team == Teams.RED) {
                p.teleport(map.getRedSpawn());
            }
            else {
                p.teleport(map.getBlueSpawn());
            }


            return;
        }

        playerMap.put(uuid, specTeam);
        p.getInventory().clear();
        giveSpecItem(p);
        p.teleport(map.getMapSpawn());
    }

    public static void onPlayerDeathEvent(PlayerDeathEvent ev) {
        Teams teamType = getPlayerTeamType(ev.getEntity());

        if (teamType == Teams.SPEC) { ev.setKeepInventory(true); return; }
        getPlayerTeam(ev.getEntity()).removeLifes(1);
        Sidebar.update();
    }

    public static void onPlayerRespawn(PlayerRespawnEvent ev) {
        new BukkitRunnable() {
            @Override
            public void run() {
                ev.getPlayer().teleport(getPlayerTeam(ev.getPlayer()).getSpawn());
            }
        }.runTaskLater(Main.INSTANCE, 1L);
    }

    public static void onChatMsg(AsyncPlayerChatEvent ev) {
        ev.setCancelled(true);
        Main.INSTANCE.getServer().broadcastMessage(getPlayerTeamType(ev.getPlayer()).getCol() + "[" + ev.getPlayer().getName() + "]: §r" + ev.getMessage());
    }

    public static void onItemUse(PlayerInteractEvent ev) {
        Player p = ev.getPlayer();
        if (getPlayerTeamType(p) != Teams.SPEC) return;
        if (p.getInventory().getItemInMainHand().getType() != Material.PAPER) return;

        TeamManager manager = WorldManager.getCurrentMap().getTeamManager();
        if (manager.getRedTeam().getTeamList().size() <= manager.getBlueTeam().getTeamList().size()) {
            manager.getRedTeam().addPlayer(p);
        }
        else {
            manager.getBlueTeam().addPlayer(p);
        }

        Teams team = TeamManager.playerMap.get(p.getUniqueId()).getTeam();
        p.sendMessage("Joined team " + team.getCol() + team.name());
    }
}
