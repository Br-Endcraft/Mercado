package me.jonasxpx.mercado;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import me.jonasxpx.geoplayers.GeoAPI;
import me.jonasxpx.geoplayers.RegionCodes;

public abstract class VirtualChest {
	
	public static final int maxitens = 45;
	public static ItemStack nextPage;
	public static ItemStack previusPage;
	public static ItemStack backItem;
	public static ItemStack remove;
	public static ItemStack adminDelete;
	
	public static SellItem getItem(int index){
		return Mercado.getMercadoItens().get(index);
	}
	
	/**
	 *  MÉTODO RESPONSÁVEL POR COLOCAR O ITEM DO JOGADOR À VENDA.
	 *  
	 * @param player
	 * @param item
	 * @param price
	 * @param r ATIVAR A RESTRIÇÃO (JOGADORES COM PERMISÃO DE VENDA NÃO CONSEGUEM COMPRAR)
	 */
	public static void putToSell(Player player, ItemStack item, long price, boolean r){
		ItemStack toSell = item.clone();
		player.getInventory().setItemInHand(new ItemStack(Material.AIR));
		Mercado.getStaticLogger().log(Level.INFO, player.getName() + " colocou um item à venda " + item.getType().name());
		boolean done = false;
		for(SellItem s : Mercado.getMercadoItens()){
			if(s.getItem().getType() == Material.AIR){
				Mercado.getMercadoItens().set(Mercado.getMercadoItens().indexOf(s), new SellItem(toSell, price, player.getName(), r));
				done = true;
				break;
			}
		}
		if(!done)Mercado.getMercadoItens().add(new SellItem(toSell, price, player.getName()));
		Mercado.currentMensages++;
	}
	
	
	/**
	 * ABRE UMA LISTA COM OS ITENS À VENDA
	 * 
	 * @param page PAGINA SEMPRE MAIOR QUE > 0
	 * @param player O JOGADOR QUE ABRIU A LOJA
	 * @return RETORNA UM INVENTÁRIO COM OS ITENS A VENDA.
	 */
	public static Inventory newListInventory(int page, Player player){
		Inventory inv = Bukkit.createInventory(null, 54, "-- Mercado " + page + " --");
		boolean nextPage = false;
	
		/*
		 * 
		 * CODIGO MORTO/ANTIGO 
		 * 
		 * int count = 0;
		for(int x = (page == 1 ? 0 : (page - 1) * maxitens+1); x <= (page == 1 ? maxitens : page  * maxitens); x++){
			if(x >= Mercado.getMercadoItens().size()){
				nextPage = true;
				break;
			}
			inv.setItem(count++, createFormatedItem(Mercado.getMercadoItens().get(x), false, player));
		}*/

		int maxpagina = page * maxitens;
		int count = (page - 1) * maxitens;
		int count_slot = 0;
		
		/*Mercado.getStaticLogger().log(Level.INFO, "MaxPagina > " + maxpagina);
		Mercado.getStaticLogger().log(Level.INFO, "count > " + count);
		Mercado.getStaticLogger().log(Level.INFO, "Stack size > " + Mercado.getMercadoItens().size());*/
		
		
		while(count < maxpagina){
			if(count >= Mercado.getMercadoItens().size()){
				nextPage = true;
				break;
			}
			inv.setItem(count_slot++, createFormatedItem(Mercado.getMercadoItens().get(count++), false, player));
		}
		
		
		
		/** itens de páginação **/
		
		if(nextPage == false)inv.setItem(52, Mercado.geoPlayer ? Mercado.getItem(GeoAPI.getRegion(player), ItemType.next_page) : Mercado.getItem(ItemType.next_page));
		if(page != 1)inv.setItem(46, Mercado.geoPlayer ? Mercado.getItem(GeoAPI.getRegion(player), ItemType.previus_page) : Mercado.getItem(ItemType.previus_page));
		return inv;
	}
	
	
	/**
	 * INVENTÁRIO COM O ITEM SELECIONADO PELO JOGADOR.
	 * 
	 * @param player
	 * @param slot
	 * @return
	 */
	public static Inventory newBuyInventory(Player player, int slot){
		Inventory inv = Bukkit.createInventory(null, 54, "-- Compra "+ slot +"  --");
		Mercado.getStaticLogger().log(Level.INFO, player.getName() + " clicked at " + slot);
		SellItem clicked = getItem(slot);
		inv.setItem(22, createFormatedItem(clicked, clicked.isRestrict(), player));
		if(clicked.getPlayer().equalsIgnoreCase(player.getName())){
			inv.setItem(40, Mercado.geoPlayer ? Mercado.getItem(GeoAPI.getRegion(player), ItemType.remove_item) : Mercado.getItem(ItemType.remove_item));
		}
		if(player.hasPermission("mercado.admin")){
			inv.setItem(42, Mercado.geoPlayer ? Mercado.getItem(GeoAPI.getRegion(player), ItemType.admin_remove) : Mercado.getItem(ItemType.admin_remove));
		}
		
		inv.setItem(38, Mercado.geoPlayer ? Mercado.getItem(GeoAPI.getRegion(player), ItemType.back_item) : Mercado.getItem(ItemType.back_item));
		return inv;
	}
	
	public static void removeSell(Player player, int slot, ItemStack showItem){
		SellItem clicked = getItem(slot);
		ItemStack item = clicked.getItem();
		RegionCodes rc = GeoAPI.getRegion(player);
		if(!clicked.getPlayer().equalsIgnoreCase(player.getName())){
			player.sendMessage(Mercado.getLanguageKey(rc, "notowner"));
			return;
		}
		if(item.getType() != showItem.getType() || item.getEnchantments().size() != showItem.getEnchantments().size()){
			player.sendMessage(Mercado.getLanguageKey(rc, "cant_sell"));
			return;
		}
		Mercado.getStaticLogger().log(Level.INFO, player.getName() + " removou uma venda Slot: " + slot + " Item: " + item.getType().name());
		player.getInventory().addItem(clicked.getItem());
		Mercado.getMercadoItens().set(slot, new SellItem(new ItemStack(Material.AIR), 0, "EndCraft"));
	}
	
	public static void adminRemove(Player player, int slot){
		if(!player.hasPermission("mercado.admin")){
			return;
		}
		player.sendMessage(Mercado.getLanguageKey(GeoAPI.getRegion(player), "removed"));
		Mercado.getMercadoItens().set(slot, new SellItem(new ItemStack(Material.AIR), 0, "EndCraft"));
	}
	
	private static ItemStack createFormatedItem(SellItem item, boolean restrito, Player player){
		ItemStack f = item.getItem().clone();
		if(f.getType() == Material.AIR)return f;
		
		RegionCodes rc = GeoAPI.getRegion(player);
		ItemMeta meta = f.getItemMeta();
		List<String> lore = Lists.newArrayList();
		if(meta != null && meta.hasLore()){
			lore.addAll(meta.getLore());
		}
		lore.add("");
		lore.add("§6\u2554\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550 $$ \u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2557");
		lore.add("");
		if(restrito)
			lore.add("§4Item Restrito.");
		lore.add(Mercado.getLanguageKey(rc, "chest_price").replaceFirst("%price%", NumberFormat.getInstance().format(item.getPrice())));
		lore.add(Mercado.getLanguageKey(rc, "chest_seller").replaceFirst("%seller%", item.getPlayer()));
		lore.add(Mercado.getLanguageKey(rc, "chest_posted").replaceFirst("%date%",item.getDate()));
		lore.add("");
		lore.add("§6\u255A\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550 $$ \u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u255D");
		meta.setLore(lore);
		f.setItemMeta(meta);
		return f;
	}
	
	public static boolean Buy(Player player, int slot, ItemStack clickedItem){
		SellItem toBuy;
		RegionCodes rc = GeoAPI.getRegion(player);
		try{
			toBuy = getItem(slot);
		}catch (IndexOutOfBoundsException e) {
			player.sendMessage(Mercado.getLanguageKey(rc, "not_exists"));
			return true;
		}
		if(clickedItem.getType() != toBuy.getItem().getType()){
			player.sendMessage(Mercado.getLanguageKey(rc, "not_exists"));
			return true;
		}
		if(toBuy.getPlayer().equalsIgnoreCase(player.getName())){
			player.sendMessage("§cVocê não pode comprar um item que você está vendendo.");
			return true;
		}
		if(toBuy.isRestrict() && !player.hasPermission("mercado.restrito")){
			player.sendMessage("§cVocê não pode comprar este item.");
			return true;
		}
		if(Mercado.economy.getBalance(player) < toBuy.getPrice()){
			player.sendMessage(Mercado.getLanguageKey(rc, "nomoney"));
			return true;
		}
		if(!isClearInventory(player)){
			player.sendMessage("§cSem slots no inventário.");
			return true;
		}
		Mercado.getStaticLogger().log(Level.INFO, player.getName() + " comprou um "+ clickedItem.getType().name() +" de " + toBuy.getPlayer() + " por " + toBuy.getPrice());
		Mercado.economy.withdrawPlayer(player, toBuy.getPrice());
		OfflinePlayer seller = Bukkit.getOfflinePlayer(toBuy.getPlayer());
		Mercado.economy.depositPlayer(seller, toBuy.getPrice());
		if(seller.isOnline()){
			Bukkit.getPlayerExact(seller.getName()).sendMessage(Mercado.getLanguageKey(GeoAPI.getRegion(seller.getPlayer()), "seller_sell")
					.replaceFirst("%item%", toBuy.getItem().getType().name())
					.replaceFirst("%price%", Long.toString(toBuy.getPrice()))
					.replaceFirst("%buyer_name%", player.getName()));
		}
		player.getInventory().addItem(toBuy.getItem());
		Mercado.getMercadoItens().set(slot, new SellItem(new ItemStack(Material.AIR), 0, "EndCraft"));
		player.sendMessage(Mercado.getLanguageKey(rc, "buy").replaceFirst("%price%", Long.toString(toBuy.getPrice())));
		return false;
	}
	
	public static void removeAirBlocks(){
		List<SellItem> toRemove = new ArrayList<>();
		Mercado.getMercadoItens().forEach(item -> {
			if(item.getItem().getType() == Material.AIR)
			toRemove.add(item);
		});
		for(SellItem t : toRemove)
			Mercado.getMercadoItens().remove(t);
	}
	
	private static boolean isClearInventory(Player p){
		for(ItemStack i : p.getInventory().getContents()){
			if(i == null || i.getType() == Material.AIR){
				return true;
			}
		}
		return false;
	}
	
}
