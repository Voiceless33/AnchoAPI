package com.codewithancho.api.spigot.base.item.leveling;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class AdvancedItemLeveling {

    ItemStack item;
    public AdvancedItemLeveling(ItemStack item) {
        this.item = item;
    }

    // These are the variables that define the leveling system
    private NamespacedKey CURRENT_LEVEL = new NamespacedKey("anchoapi", "current_level");
    private NamespacedKey EXPERIENCE_PER_LEVEL = new NamespacedKey("anchoapi", "experience_per_level");
    private NamespacedKey CURRENT_EXPERIENCE = new NamespacedKey("anchoapi", "current_experience");
    private NamespacedKey HAS_EXPERIENCE_SYSTEM =  new NamespacedKey("anchoapi", "has_experience_system");
    private NamespacedKey MAX_LEVEL = new NamespacedKey("anchoapi", "max_level");
    private NamespacedKey TOTAL_EXPERIENCE = new NamespacedKey("anchoapi", "total_experience");

    public void setupItemExpProgression(int startingLevel, int experiencePerLevel, int maxLevel) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc =  meta.getPersistentDataContainer();
        if (pdc.has(HAS_EXPERIENCE_SYSTEM, PersistentDataType.INTEGER)) {
            return;
        }

        pdc.set(EXPERIENCE_PER_LEVEL, PersistentDataType.INTEGER, experiencePerLevel);
        pdc.set(CURRENT_LEVEL, PersistentDataType.INTEGER, startingLevel);
        pdc.set(CURRENT_EXPERIENCE, PersistentDataType.INTEGER, 0);
        pdc.set(TOTAL_EXPERIENCE,  PersistentDataType.INTEGER, 0);

        if (maxLevel != 0) {

            if (maxLevel < startingLevel) {
                if (maxLevel <= 0) throw new IllegalArgumentException("The Max Level must be greater than the Starting Level");
            }
            if (maxLevel < 0) {
                throw new IllegalArgumentException("The Max Level must be greater than 0");
            }

            pdc.set(MAX_LEVEL, PersistentDataType.INTEGER, maxLevel);
        }

        if (maxLevel == 0) {
            pdc.set(MAX_LEVEL, PersistentDataType.INTEGER, maxLevel);
        } else {
            pdc.set(HAS_EXPERIENCE_SYSTEM, PersistentDataType.INTEGER, 1);
            item.setItemMeta(meta);
        }
    }

    public void addExperience(int amount) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc =  meta.getPersistentDataContainer();
        pdc.set(CURRENT_EXPERIENCE, PersistentDataType.INTEGER, pdc.get(CURRENT_EXPERIENCE, PersistentDataType.INTEGER) + amount);
        pdc.set(TOTAL_EXPERIENCE, PersistentDataType.INTEGER, pdc.get(TOTAL_EXPERIENCE, PersistentDataType.INTEGER) + amount);
        item.setItemMeta(meta);
    }

    public void levelUp () {
        if (item != null) {
            if (!item.getItemMeta().getPersistentDataContainer().has(HAS_EXPERIENCE_SYSTEM, PersistentDataType.INTEGER)) {
                return;
            }

            if (hasReachedMaxLevel(item)) return;

            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer pdc =  meta.getPersistentDataContainer();
            int currentXp = pdc.get(CURRENT_EXPERIENCE, PersistentDataType.INTEGER);
            int neededXp = pdc.get(EXPERIENCE_PER_LEVEL, PersistentDataType.INTEGER) * (pdc.get(CURRENT_LEVEL, PersistentDataType.INTEGER) + 1);
            int currentLevel = pdc.get(CURRENT_LEVEL, PersistentDataType.INTEGER);

            while (currentXp >= neededXp && !hasReachedMaxLevel(item)) {
                currentXp -= neededXp;
                currentLevel++;
                neededXp = pdc.get(EXPERIENCE_PER_LEVEL, PersistentDataType.INTEGER) * (currentLevel + 1);

                pdc.set(CURRENT_LEVEL, PersistentDataType.INTEGER, currentLevel);
                pdc.set(CURRENT_EXPERIENCE, PersistentDataType.INTEGER, currentXp);
            }
            item.setItemMeta(meta);
        }
    }

    public boolean hasReachedMaxLevel(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc =  meta.getPersistentDataContainer();

        if (pdc.get(MAX_LEVEL, PersistentDataType.INTEGER) == 0) {
            return false;
        }

        int currentLevel = pdc.get(CURRENT_LEVEL, PersistentDataType.INTEGER);
        int maxLevel =  pdc.get(MAX_LEVEL, PersistentDataType.INTEGER);

        if (currentLevel >= maxLevel) {
            return true;
        }
        return false;
    }

    public boolean hasExperienceSystem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc =  meta.getPersistentDataContainer();
        if (pdc.has(HAS_EXPERIENCE_SYSTEM, PersistentDataType.INTEGER)) return  true;
        return false;
    }

    public int getMaxLevel(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc =  meta.getPersistentDataContainer();
        return  pdc.get(MAX_LEVEL, PersistentDataType.INTEGER);
    }

    public int getCurrentLevel() {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc =  meta.getPersistentDataContainer();
        return pdc.get(CURRENT_LEVEL, PersistentDataType.INTEGER);
    }

    public int getCurrentExperience() {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc =  meta.getPersistentDataContainer();
        return pdc.get(CURRENT_EXPERIENCE, PersistentDataType.INTEGER);
    }

    public int getTotalExperience() {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc =  meta.getPersistentDataContainer();
        return pdc.get(TOTAL_EXPERIENCE, PersistentDataType.INTEGER);
    }
}
