package lol.pyr.chatmanager.commands;

import lol.pyr.chatmanager.ChatPlugin;
import lol.pyr.extendedcommands.CommandContext;
import lol.pyr.extendedcommands.api.ExtendedExecutor;

public class ChatClearCommand implements ExtendedExecutor<ChatPlugin> {
    @Override
    public void run(CommandContext<ChatPlugin> context) {
        context.getPlugin();
    }
}
