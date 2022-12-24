package lol.pyr.chatmanager.filter;

import lol.pyr.chatmanager.ChatPlugin;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatFilter {
    private final ChatPlugin plugin;

    private boolean containsAlike(String message, String word) {
        message = message.toLowerCase();
        word = word.toLowerCase();
        outer:
        for (int i = 0; i <= message.length() - word.length(); i++) {
            for (int same = 0; same < word.length(); same++) if (!areAlike(message.charAt(i + same), word.charAt(same))) continue outer;
            return true;
        }
        return false;
    }

    public boolean isBad(String message) {
        for (String word : plugin.getChatConfig().getChatFilterFilteredWords()) if (containsAlike(message, word)) return true;
        return false;
    }

    public String replaceFiltered(String message, String word, Character replacing) {
        message = message.toLowerCase();
        word = word.toLowerCase();
        char[] chars = message.toCharArray();
        outer:
        for (int i = 0; i <= message.length() - word.length(); i++) {
            for (int same = 0; same < word.length(); same++) if (!areAlike(message.charAt(i + same), word.charAt(same))) continue outer;
            for (int same = 0; same < word.length(); same++) chars[i + same] = replacing;
        }
        return new String(chars);
    }

    public String replaceAllFiltered(String message, Character replacing) {
        for (String word : plugin.getChatConfig().getChatFilterFilteredWords()) message = replaceFiltered(message, word, replacing);
        return message;
    }

    private boolean areAlike(Character c1, Character c2) {
        return alikePairFor(c1).equals(alikePairFor(c2));
    }

    private AlikePair alikePairFor(Character c) {
        for (AlikePair pair : plugin.getChatConfig().getChatFilterAlikePairs()) if (pair.getChars().contains(c)) return pair;
        return new AlikePair(c);
    }

    public enum RemovalStrategy {
        CANCEL_MESSAGE, REPLACE_FILTERED;

        public static RemovalStrategy fromString(String string) {
            try {
                if (string == null) throw new IllegalArgumentException();
                return valueOf(string.toUpperCase());
            } catch (IllegalArgumentException exception) {
                return CANCEL_MESSAGE;
            }
        }
    }
}
