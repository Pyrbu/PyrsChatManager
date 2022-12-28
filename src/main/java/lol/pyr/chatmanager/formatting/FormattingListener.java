package lol.pyr.chatmanager.formatting;

import lol.pyr.chatmanager.ChatConfig;
import lol.pyr.chatmanager.ChatPlugin;
import lol.pyr.chatmanager.meta.MetaProvider;
import lol.pyr.chatmanager.util.ColorUtils;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@RequiredArgsConstructor
public class FormattingListener implements Listener {
    private final ChatPlugin plugin;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ChatConfig config = plugin.getChatConfig();

        if (!config.isChatFormattingEnabled()) return;
        String message = event.getMessage();
        MetaProvider meta = plugin.getMetaProvider();
        String group;
        String format = (meta == null || !config.getChatFormattingGroupFormats().containsKey(group = meta.getGroup(player))) ?
                config.getChatFormattingFormat() : config.getChatFormattingGroupFormats().get(group);

        if (event.getPlayer().hasPermission("pcm.chatformatting.colors")) message = ColorUtils.translateColors(message);
        if (event.getPlayer().hasPermission("pcm.chatformatting.hex")) message = ColorUtils.translateHex(message);
        if (event.getPlayer().hasPermission("pcm.chatformatting.magic")) message = ColorUtils.translateMagic(message);

        format = format.replace("{username}", player.getName());
        format = format.replace("{displayname}", player.getDisplayName());

        if (meta != null) {
            format = format.replace("{prefix}", meta.getPrefix(player));
            format = format.replace("{suffix}", meta.getSuffix(player));
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            format = PlaceholderAPI.setPlaceholders(player, format);

        format = ColorUtils.translateAll(format);
        format = format.replace("{message}", message);

        event.setFormat(format.replace("%", "%%"));
    }
}
