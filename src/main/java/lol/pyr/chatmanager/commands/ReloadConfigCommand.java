package lol.pyr.chatmanager.commands;

import lol.pyr.chatmanager.ChatPlugin;
import lol.pyr.extendedcommands.CommandContext;
import lol.pyr.extendedcommands.api.ExtendedExecutor;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

import java.io.File;

@RequiredArgsConstructor
public class ReloadConfigCommand implements ExtendedExecutor<ChatPlugin> {
    @Override
    public void run(CommandContext<ChatPlugin> context) {
        context.getPlugin().getChatConfig().load(new File(context.getPlugin().getDataFolder(), "config.yml"));
        context.getSender().sendMessage(ChatColor.GREEN + "Successfully reloaded PCM configuration");
    }
}
