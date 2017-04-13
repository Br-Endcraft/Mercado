package me.jonasxpx.mercado;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import me.jonasxpx.mercado.Tools.ItemStackSerial;

public class SellItem implements Serializable, Cloneable{

	private static final long serialVersionUID = -5547616001468372029L;

	private List<HashMap<Map<String, Object>, Map<String, Object>>> item;
	private final long price;
	private final String player;
	private final String date;
	private boolean restrict = false;
	
	public SellItem(ItemStack item, long price, String player) {
		this.setItem(ItemStackSerial.serialize(item));
		this.price = price;
		this.player = player;
		Calendar c = Calendar.getInstance();
		date = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
	}
	public SellItem(ItemStack item, long price, String player, boolean restrict) {
		this.setItem(ItemStackSerial.serialize(item));
		this.price = price;
		this.player = player;
		this.restrict = restrict;
		Calendar c = Calendar.getInstance();
		date = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
	}
	public SellItem(ItemStack item, double price, String player) {
		this.setItem(ItemStackSerial.serialize(item));
		this.price = new Double(price).longValue();
		this.player = player;
		Calendar c = Calendar.getInstance();
		date = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
	}

	public ItemStack getItem() {
		return ItemStackSerial.deserialize(item);
	}

	public void setItem(List<HashMap<Map<String, Object>, Map<String, Object>>> list) {
		this.item = list;
	}

	public long getPrice() {
		return price;
	}

	public String getPlayer() {
		return player;
	}

	public String getDate() {
		return date;
	}
	
	public boolean isRestrict(){
		return this.restrict;
	}
	
	public void setRestrict(boolean r){
		this.restrict = r;
	}
	
	@Override
	protected SellItem clone() {
		try {
			return (SellItem) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
