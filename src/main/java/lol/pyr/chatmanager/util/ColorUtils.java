package lol.pyr.chatmanager.util;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {
    private static final Pattern HEX = Pattern.compile("#[0-9a-fA-F]{6}");

    private static String toChatColor(String hexString) {
        StringBuilder builder = new StringBuilder("§x");
        for(Character c : hexString.substring(1).toCharArray()) {
            builder.append("§").append(c);
        }
        return builder.toString();
    }

    public static String translateAll(String string) {
        return ChatColor.translateAlternateColorCodes('&', translateHex(string));
    }

    public static String translateHex(String string) {
        String text = string.replace("&#", "#");
        Matcher matcher = HEX.matcher(string);
        while(matcher.find()) {
            String hexcode = matcher.group();
            text = text.replace(hexcode, toChatColor(hexcode));
        }
        return text;
    }

    // Slightly modified version of ChatColor.translateAlternateColorCodes()
    public static String translateColors(String text) {
        char[] b = text.toCharArray();
        for (int i = 0; i < b.length - 1; i++) if (b[i] == '&' && "0123456789AaBbCcDdEeFf".indexOf(b[i + 1]) > -1) {
            b[i] = ChatColor.COLOR_CHAR;
            b[i + 1] = Character.toLowerCase(b[i + 1]);
        }
        return new String(b);
    }

    // Slightly modified version of ChatColor.translateAlternateColorCodes()
    public static String translateMagic(String text) {
        char[] b = text.toCharArray();
        for (int i = 0; i < b.length - 1; i++) if (b[i] == '&' && "KkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
            b[i] = ChatColor.COLOR_CHAR;
            b[i + 1] = Character.toLowerCase(b[i + 1]);
        }
        return new String(b);
    }
}