package lol.pyr.chatmanager.groups;

import org.bukkit.OfflinePlayer;

public interface MetaProvider {
    String getGroup(OfflinePlayer player);
    String getPrefix(OfflinePlayer player);
    String getSuffix(OfflinePlayer player);
}
