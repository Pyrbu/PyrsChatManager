package lol.pyr.chatmanager.commands;

import lol.pyr.chatmanager.ChatPlugin;
import lol.pyr.extendedcommands.CommandContext;
import lol.pyr.extendedcommands.api.ExtendedExecutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatClearCommand implements ExtendedExecutor<ChatPlugin> {
    @Override
    public void run(CommandContext<ChatPlugin> context) {
        String msg = "\n".repeat(500) + context.getPlugin().getChatConfig().getChatClearMessage()
                .replace("{player}", context.getSender().getName());
        Bukkit.getScheduler().runTaskAsynchronously(context.getPlugin(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) player.sendMessage(msg);
        });
    }
}
