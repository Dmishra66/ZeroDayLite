package game;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class BadgeManager {
    private Set<String> unlockedBadges = new HashSet<>();

    public void unlockBadge(String badgeName) {
        unlockedBadges.add(badgeName);
    }

    public void showBadges() {
        if (unlockedBadges.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No badges unlocked yet!");
        } else {
            JOptionPane.showMessageDialog(null, "Unlocked Badges:\n" + String.join(", ", unlockedBadges));
        }
    }
}
