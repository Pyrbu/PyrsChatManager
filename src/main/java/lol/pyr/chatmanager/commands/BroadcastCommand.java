package lol.pyr.chatmanager.commands;

import lol.pyr.chatmanager.ChatPlugin;
import lol.pyr.extendedcommands.CommandContext;
import lol.pyr.extendedcommands.api.ExtendedExecutor;
import lol.pyr.extendedcommands.exception.CommandExecutionException;
import org.bukkit.Bukkit;

public class BroadcastCommand implements ExtendedExecutor<ChatPlugin> {
    @Override
    public void run(CommandContext<ChatPlugin> context) throws CommandExecutionException {
        context.setCurrentUsage(context.getLabel() + " <message>");
        if (context.argSize() == 0) throw new CommandExecutionException("");
        Bukkit.broadcastMessage(context.getPlugin().getChatConfig().getBroadcastMessage().replace("{message}", context.dumpAllArgs()));
    }
}
