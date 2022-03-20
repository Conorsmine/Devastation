package net.Conorsmine.com;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    public static JavaPlugin INSTANCE;
    public static Logger LOG;
    public static final String PREFIX = "§7[§6Devastation-Utils§7]§r";
    public static String FOLDER_PATH;

    @Override
    public void onEnable() {
        INSTANCE = this;
        LOG = this.getLogger();
        FOLDER_PATH = this.getServer().getWorldContainer().getAbsolutePath() + File.separator + "plugins" + File.separator + "Devastation" + File.separator;

        Commands.registerCommands(this);
        this.getServer().getPluginManager().registerEvents(new Events(), this);


        LOG.info(PREFIX + " §aStarted successfully.");
    }


    private void setupFiles() {
        new File(this.getDataFolder().getAbsolutePath() + File.separator + "Devastation").mkdir();
    }
}
