package lol.pyr.chatmanager.filter;

import java.util.*;

public class AlikePair {
    private final Set<Character> chars = new HashSet<>();

    public AlikePair(Character... chars) {
        this.chars.addAll(Arrays.asList(chars));
    }

    public AlikePair(Collection<String> collection) {
        for (String s : collection) if (s.length() == 1) chars.add(s.charAt(0));
    }

    public Set<Character> getChars() {
        return chars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlikePair alikePair = (AlikePair) o;
        return Objects.equals(chars, alikePair.chars);
    }
}
