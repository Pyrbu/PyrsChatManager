package lol.pyr.chatmanager.filter;

import lol.pyr.chatmanager.ChatConfig;
import lol.pyr.chatmanager.ChatPlugin;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@RequiredArgsConstructor
public class FilterListener implements Listener {
    private final ChatPlugin plugin;

    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent event) {
        ChatConfig config = plugin.getChatConfig();
        if (!config.isChatFilterEnabled()) return;

        Player player = event.getPlayer();
        String message = event.getMessage();

        switch (plugin.getChatConfig().getChatFilterRemovalStrategy()) {
            case CANCEL_MESSAGE -> {
                if (!plugin.getChatFilter().isBad(message)) return;
                event.setCancelled(true);
                if (config.isChatFilterNotifyStaff()) notifyStaff(player, event.getMessage());
                sendPunishCommands(player);
            }
            case REPLACE_FILTERED -> {
                message = plugin.getChatFilter().replaceAllFiltered(message, plugin.getChatConfig().getChatFilterReplacementCharacter());
                if (message.equals(event.getMessage())) return;
                if (config.isChatFilterNotifyStaff()) notifyStaff(player, event.getMessage());
                sendPunishCommands(player);
                event.setMessage(message);
            }
        }
    }

    private void sendPunishCommands(Player player) {
        for (String command : plugin.getChatConfig().getChatFilterPunishCommands()) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
    }

    private void notifyStaff(Player player, String message) {
        String notifyMsg = plugin.getChatConfig().getChatFilterNotifyStaffFormat()
                .replace("{player}", player.getName())
                .replace("{message}", message);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            notifyMsg = PlaceholderAPI.setPlaceholders(player, notifyMsg);

        for (Player staff : Bukkit.getOnlinePlayers()) if (staff.hasPermission("pcm.chatfilter.notify")) staff.sendMessage(notifyMsg);
    }
}
