package me.jonasxpx.mercado.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class ItemStackSerial {

	public static List<HashMap<Map<String, Object>, Map<String, Object>>> serialize(ItemStack item) {
		HashMap<Map<String, Object>, Map<String, Object>> hash = new HashMap<>();
		Map<String, Object> meta = null;
		List<HashMap<Map<String, Object>, Map<String, Object>>> map = new ArrayList<>();
		meta = (item.hasItemMeta() ? item.getItemMeta().serialize() : null);
		item.setItemMeta(null);
		hash.put(item.serialize(), meta);
		map.add(hash);
		return map;
	}
	
	public static ItemStack deserialize(List<HashMap<Map<String, Object>, Map<String, Object>>> w){
		ItemStack item;
		Map<String, Object> itemHash = w.get(0).keySet().iterator().next();
		Map<String, Object> itemMetaHash = w.get(0).get(itemHash);
		item = ItemStack.deserialize(itemHash);
		if(itemMetaHash != null)item.setItemMeta((ItemMeta) ConfigurationSerialization.deserializeObject(itemMetaHash, ConfigurationSerialization.getClassByAlias("ItemMeta")));
		return item;
	}

}
