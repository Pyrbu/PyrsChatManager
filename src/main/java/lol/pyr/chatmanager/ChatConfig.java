package lol.pyr.chatmanager;

import lol.pyr.chatmanager.filter.AlikePair;
import lol.pyr.chatmanager.filter.Filter;
import lol.pyr.chatmanager.util.ColorUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Getter
@RequiredArgsConstructor
public class ChatConfig {
    private final ChatPlugin plugin;

    private boolean chatFormattingEnabled;
    private String chatFormattingFormat;
    private final Map<String, String> chatFormattingGroupFormats = new HashMap<>();

    private boolean chatFilterEnabled;
    private Filter.RemovalStrategy chatFilterRemovalStrategy;
    private final Set<AlikePair> chatFilterAlikePairs = new HashSet<>();
    private final Set<String> chatFilterFilteredWords = new HashSet<>();
    private Character chatFilterReplacementCharacter;
    private boolean chatFilterNotifyStaff;
    private String chatFilterNotifyStaffFormat;
    private final List<String> chatFilterPunishCommands = new ArrayList<>();

    private String broadcastMessage;
    private String chatClearMessage;
    private String muteChatMessage;

    @SuppressWarnings("ConstantConditions")
    public void load(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        chatFormattingEnabled = config.getBoolean("chat-formatting.enabled");
        chatFormattingFormat = config.getString("chat-formatting.chat-format");
        chatFormattingGroupFormats.clear();
        ConfigurationSection groupFormatsSection = config.getConfigurationSection("chat-formatting.group-formats");
        if (groupFormatsSection != null) for (String group : groupFormatsSection.getKeys(false)) chatFormattingGroupFormats.put(group, groupFormatsSection.getString(group));


        chatFilterEnabled = config.getBoolean("chat-filter.enabled");
        chatFilterAlikePairs.clear();
        if (config.contains("chat-filter.alike-pairs")) for (String l :  config.getStringList("chat-filter.alike-pairs")) chatFilterAlikePairs.add(new AlikePair(l.toCharArray()));
        chatFilterFilteredWords.clear();
        if (config.contains("chat-filter.filtered-words")) chatFilterFilteredWords.addAll(config.getStringList("chat-filter.filtered-words"));
        chatFilterRemovalStrategy = Filter.RemovalStrategy.fromString(config.getString("chat-filter.removal-strategy"));
        chatFilterReplacementCharacter = config.getString("chat-filter.replacement-character").charAt(0);
        chatFilterNotifyStaff = config.getBoolean("chat-filter.notify-staff");
        chatFilterNotifyStaffFormat = ColorUtils.translateAll(config.getString("chat-filter.notify-staff-format"));
        chatFilterPunishCommands.clear();
        if (config.contains("chat-filter.punish-commands")) chatFilterPunishCommands.addAll(config.getStringList("chat-filter.punish-commands"));

        broadcastMessage = ColorUtils.translateAll(String.join("\n", config.getStringList("commands.broadcast-messages")));
        chatClearMessage = ColorUtils.translateAll(String.join("\n", config.getStringList("commands.chat-clear-messages")));
        muteChatMessage = ColorUtils.translateAll(String.join("\n", config.getStringList("commands.mute-chat-messages")));

        plugin.getLogger().info("Loaded configuration");
    }

    @SuppressWarnings("ConstantConditions")
    public static void fixUp(ChatPlugin plugin, File file) {
        InputStream stream = plugin.getResource("config.yml");
        if (stream == null) throw new RuntimeException("config.yml missing from jar");

        YamlConfiguration jarConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));
        YamlConfiguration config = file.exists() ? YamlConfiguration.loadConfiguration(file) : new YamlConfiguration();

        if (config.contains("config-version") && jarConfig.getString("config-version").equalsIgnoreCase(config.getString("config-version"))) return;
        config.set("config-version", null);

        for (String path : config.getKeys(true)) jarConfig.set(path, config.get(path));

        try {
            jarConfig.save(file);
            plugin.getLogger().info("Fixed up configuration");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
