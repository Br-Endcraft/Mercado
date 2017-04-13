package me.jonasxpx.mercado;

import org.bukkit.inventory.ItemStack;

public enum ItemType {
	
	next_page(VirtualChest.nextPage, "next_page", "§6", " -->"),
	previus_page(VirtualChest.previusPage, "previus_page", "§6<-- ", ""),
	remove_item(VirtualChest.remove, "cancel_sell", "§c", ""),
	admin_remove(VirtualChest.adminDelete, "delete_admin", "§4", ""),
	back_item(VirtualChest.backItem, "back_page", "§b<-- ", "");
	
	public ItemStack item;
	public String prefix,suffix,lang_selector;
	
	private ItemType(ItemStack i, String lang_selector, String prefix, String suffix) {
		this.item = i;
		this.prefix = prefix;
		this.suffix = suffix;
		this.lang_selector = lang_selector;
	}
	
}
