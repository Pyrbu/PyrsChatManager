package lol.pyr.chatmanager.groups;

import org.bukkit.OfflinePlayer;

public class EmptyMetaProvider implements MetaProvider {
    @Override
    public String getGroup(OfflinePlayer player) {
        return "";
    }

    @Override
    public String getPrefix(OfflinePlayer player) {
        return "";
    }

    @Override
    public String getSuffix(OfflinePlayer player) {
        return "";
    }
}
