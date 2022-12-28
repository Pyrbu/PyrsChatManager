package lol.pyr.chatmanager.meta;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class VaultMetaProvider implements MetaProvider {
    private final Chat chat;
    private final Permission permission;

    @SuppressWarnings("ConstantConditions")
    public VaultMetaProvider() {
        if (!isSupported()) throw new RuntimeException("Tried to instanciate a VaultMetaProvider while not supported");
        chat = Bukkit.getServicesManager().getRegistration(Chat.class).getProvider();
        permission = Bukkit.getServicesManager().getRegistration(Permission.class).getProvider();
    }

    @Override
    public String getGroup(OfflinePlayer player) {
        return permission.getPrimaryGroup(null, player);
    }

    /*
     * Daisychaining through the player prefix -> group prefix is done
     * to support the case of Vault implementations with no inheretance
     */
    @Override
    public String getPrefix(OfflinePlayer player) {
        String playerPrefix = chat.getPlayerPrefix(null, player);
        if (playerPrefix != null && playerPrefix.length() > 0) return playerPrefix;
        String group = getGroup(player);
        String groupPrefix = chat.getGroupPrefix((String) null, group);
        if (groupPrefix != null && groupPrefix.length() > 0) return groupPrefix;
        return "";
    }

    /*
     * Daisychaining through the player suffix -> group suffix is done
     * to support the case of Vault implementations with no inheretance
     */
    @Override
    public String getSuffix(OfflinePlayer player) {
        String playerSuffix = chat.getPlayerSuffix(null, player);
        if (playerSuffix != null && playerSuffix.length() > 0) return playerSuffix;
        String group = getGroup(player);
        String groupSuffix = chat.getGroupSuffix((String) null, group);
        if (groupSuffix != null && groupSuffix.length() > 0) return groupSuffix;
        return "";
    }

    public static boolean isSupported() {
        return Bukkit.getPluginManager().isPluginEnabled("Vault") &&
                Bukkit.getServicesManager().isProvidedFor(Chat.class) &&
                Bukkit.getServicesManager().isProvidedFor(Permission.class);
    }
}
