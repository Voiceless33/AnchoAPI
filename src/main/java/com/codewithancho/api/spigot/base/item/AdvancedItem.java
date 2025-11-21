package com.codewithancho.api.spigot.base.item;

import com.codewithancho.api.spigot.base.item.leveling.AdvancedItemLeveling;
import com.codewithancho.api.spigot.managers.ChatManager;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdvancedItem {

    @Getter
    private AdvancedItemLeveling leveling;
    private final ItemStack item;

    @Getter
    private NamespacedKey API_ITEM_IDENTIFIER = new NamespacedKey("anchoapi", "api_item");
    @Getter
    private NamespacedKey SOULBOUND_ITEM_IDENTIFIER = new NamespacedKey("anchoapi", "soul_bound");

    public AdvancedItem(ItemStack item) {
        this.item = item;
        leveling = new AdvancedItemLeveling(item);
    }

    public void addNamespacedData(NamespacedKey namespacedKey, PersistentDataType persistentDataType, String value) {
        ItemMeta meta = item.getItemMeta();
        if (meta.getPersistentDataContainer().has(namespacedKey)) {
            Logger.getLogger(AdvancedItem.class.getName()).log(Level.INFO, "Item already has the key: " + namespacedKey);
            return;
        }
        meta.getPersistentDataContainer().set(namespacedKey, persistentDataType, value);
        item.setItemMeta(meta);
    }

    public void removeEnchant(Enchantment enchantment) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.removeEnchant(enchantment);
        item.setItemMeta(itemMeta);
    }

    public void addEnchant(Enchantment enchantment, int level) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addEnchant(enchantment, level, true);
        item.setItemMeta(itemMeta);
    }

    public void removeItemFlags(ItemFlag[] itemFlags) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.removeItemFlags(itemFlags);
        item.setItemMeta(meta);
    }

    public void addItemFlags(ItemFlag[] itemFlags) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.addItemFlags(itemFlags);
        item.setItemMeta(meta);
    }

    /**
     * @param uuid The UUID of the player that is supposed to be the owner of the item
     */
    public void setItemOwner(UUID uuid) {
        if (SOULBOUND_ITEM_IDENTIFIER == null)  throw new NullPointerException("SOULBOUND_ITEM_IDENTIFIER WAS NOT SET");
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.getPersistentDataContainer().set(SOULBOUND_ITEM_IDENTIFIER, PersistentDataType.STRING, uuid.toString());
        item.setItemMeta(meta);
    }

    /**
     * @return Returns true or false, depending if the item is soul bound to somebody
     */
    public boolean isItemSoulbound() {
        if (this.SOULBOUND_ITEM_IDENTIFIER == null) return false;
        return item.getItemMeta().getPersistentDataContainer().has(SOULBOUND_ITEM_IDENTIFIER, PersistentDataType.STRING);
    }

    /**
     * @return Check if the item is an API item
     */
    public boolean isAPIItem() {
        if (API_ITEM_IDENTIFIER == null) return false;
        return this.item.getItemMeta().getPersistentDataContainer().has(API_ITEM_IDENTIFIER, PersistentDataType.STRING);
    }

    /**
     * @param name The display name you'd like your Item to have
     */
    public void setName (String name) {
        item.getItemMeta().setDisplayName(ChatManager.colored(name));
    }

    /**
     * @param enabled Gives you the ability to toggle between if you want the items to glow or not
     */
    public void setGlow(boolean enabled) {
        ItemMeta meta =  this.item.getItemMeta();

        if (enabled) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            return;
        } else {
            meta.removeEnchant(Enchantment.UNBREAKING);
            meta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }

        this.item.setItemMeta(meta);
    }

    /**
     * @return Returns the name of the item
     */
    public String getItemName () {
        return item.getItemMeta().getDisplayName();
    }
}
