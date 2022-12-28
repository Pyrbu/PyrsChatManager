package lol.pyr.chatmanager.meta;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class LuckPermsMetaProvider implements MetaProvider {
    private final LuckPerms luckPerms;

    public LuckPermsMetaProvider() {
        if (!isSupported()) throw new RuntimeException("Tried to instanciate a LuckPermsMetaProvider while not supported");
        luckPerms = LuckPermsProvider.get();
    }

    @Override
    public String getGroup(OfflinePlayer player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return "";
        return user.getPrimaryGroup();
    }

    @Override
    public String getPrefix(OfflinePlayer player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return "";
        String prefix = user.getCachedData().getMetaData().getPrefix();
        return prefix == null ? "" : prefix;
    }

    @Override
    public String getSuffix(OfflinePlayer player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return "";
        String suffix = user.getCachedData().getMetaData().getSuffix();
        return suffix == null ? "" : suffix;
    }

    public static boolean isSupported() {
        return Bukkit.getPluginManager().isPluginEnabled("LuckPerms");
    }
}
