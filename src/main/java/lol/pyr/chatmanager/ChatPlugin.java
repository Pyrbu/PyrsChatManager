package lol.pyr.chatmanager;

import lol.pyr.chatmanager.commands.BroadcastCommand;
import lol.pyr.chatmanager.commands.ChatClearCommand;
import lol.pyr.chatmanager.commands.ReloadConfigCommand;
import lol.pyr.chatmanager.filter.Filter;
import lol.pyr.chatmanager.filter.FilterListener;
import lol.pyr.chatmanager.formatting.FormattingListener;
import lol.pyr.chatmanager.meta.EmptyMetaProvider;
import lol.pyr.chatmanager.meta.LuckPermsMetaProvider;
import lol.pyr.chatmanager.meta.MetaProvider;
import lol.pyr.chatmanager.meta.VaultMetaProvider;
import lol.pyr.extendedcommands.CommandManager;
import lol.pyr.extendedcommands.MessageKey;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class ChatPlugin extends JavaPlugin {

    @Getter private ChatConfig chatConfig;
    @Getter private Filter chatFilter;
    @Getter private MetaProvider metaProvider;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onEnable() {
        Logger logger = getServer().getLogger();
        logger.info(ChatColor.DARK_RED + "  __   __  _   _");
        logger.info(ChatColor.DARK_RED + " |__) |   | \\ / |  " + ChatColor.RED + getDescription().getName() + " " + ChatColor.DARK_RED + "v" + getDescription().getVersion());
        logger.info(ChatColor.DARK_RED + " |    |__ |     |  " + ChatColor.GRAY + "Made with " + ChatColor.RED + "\u2764" + ChatColor.GRAY + " by " + String.join(", ", getDescription().getAuthors()));
        logger.info("");

        if (!getDataFolder().exists()) getDataFolder().mkdirs();

        File configFile = new File(getDataFolder(), "config.yml");
        ChatConfig.fixUp(this, configFile); // Also saves a new config if one isn't already there
        chatConfig = new ChatConfig(this);
        chatConfig.load(configFile);

        chatFilter = new Filter(this);

        setupMetaProvider();
        registerListeners();
        registerCommands();

        // bStats (shock emoji)
        new Metrics(this, 17214);
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new FormattingListener(this), this);
        pm.registerEvents(new FilterListener(this), this);
    }

    private void registerCommands() {
        CommandManager<ChatPlugin> manager = new CommandManager<>(this);
        manager.registerDefaultParsers();
        manager.setMessageResolver(MessageKey.NOT_ENOUGH_ARGS, ctx -> ctx.getCurrentUsage().length() == 0 ? ChatColor.RED + "Incorrect usage" :  ChatColor.RED + "Incorrect usage: /" + ctx.getCurrentUsage());
        manager.setMessageResolver(MessageKey.SENDER_REQUIRED_PLAYER, context -> ChatColor.RED + "This command can only be used by players");
        manager.setDefaultResolver(ctx -> ctx.getCurrentUsage().length() == 0 ? ChatColor.RED + "Incorrect usage" :  ChatColor.RED + "Incorrect usage: /" + ctx.getCurrentUsage());

        manager.registerCommand("pcmreload", new ReloadConfigCommand());
        manager.registerCommand("clearchat", new ChatClearCommand());
        manager.registerCommand("broadcast", new BroadcastCommand());
    }

    public void setupMetaProvider() {
        if (LuckPermsMetaProvider.isSupported()) metaProvider = new LuckPermsMetaProvider();
        else if (VaultMetaProvider.isSupported()) metaProvider = new VaultMetaProvider();
        else metaProvider = new EmptyMetaProvider();
    }
}
