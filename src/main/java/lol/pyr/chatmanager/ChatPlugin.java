package lol.pyr.chatmanager;

import lol.pyr.chatmanager.filter.ChatFilter;
import lol.pyr.chatmanager.groups.EmptyMetaProvider;
import lol.pyr.chatmanager.groups.LuckPermsMetaProvider;
import lol.pyr.chatmanager.groups.MetaProvider;
import lol.pyr.chatmanager.groups.VaultMetaProvider;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class ChatPlugin extends JavaPlugin {

    @Getter private ChatConfig chatConfig;
    @Getter private ChatFilter chatFilter;
    @Getter private MetaProvider metaProvider;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onEnable() {
        Logger logger = getServer().getLogger();
        logger.info(                "  __   __  _   _");
        logger.info(ChatColor.RED + " |__) |   | \\ / |  " + ChatColor.RED + getDescription().getName() + " " + ChatColor.DARK_RED + "v" + getDescription().getVersion());
        logger.info(ChatColor.RED + " |    |__ |     |  " + ChatColor.GRAY + "Made with " + ChatColor.RED + "\u2764 " + ChatColor.GRAY + " by " + String.join(", ", getDescription().getAuthors()));
        logger.info("");

        if (!getDataFolder().exists()) getDataFolder().mkdirs();

        File configFile = new File(getDataFolder(), "config.yml");
        ChatConfig.fixUp(this, configFile); // Also saves a new config if one isn't already there
        chatConfig = new ChatConfig(this);
        chatConfig.load(configFile);

        chatFilter = new ChatFilter(this);

        setupMetaProvider();
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
    }

    public void setupMetaProvider() {
        if (LuckPermsMetaProvider.isSupported()) metaProvider = new LuckPermsMetaProvider();
        else if (VaultMetaProvider.isSupported()) metaProvider = new VaultMetaProvider();
        else metaProvider = new EmptyMetaProvider();
    }
}
