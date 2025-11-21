package com.codewithancho.api.spigot.base.item.leveling;

public class LevelingMath {

    public static boolean isLevelUpPossible(double currentExperience, double experienceNeeded) {
        return currentExperience >= experienceNeeded;
    }

    public static double calculateRemainingExperience(double currentExperience, double experienceNeeded) {
        return experienceNeeded - currentExperience;
    }

    public static double getExperienceProgress(double currentExperience, double experienceNeeded) {
        if (experienceNeeded <= 0) {
            return 0;
        }

        double percentage = (currentExperience / experienceNeeded) * 100;

        if(percentage > 100) {
            percentage = 100;
        }
        if (percentage < 0) {
            percentage = 0;
        }

        return percentage;
    }
}
