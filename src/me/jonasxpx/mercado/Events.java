package me.jonasxpx.mercado;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Events implements Listener{

	
	@EventHandler
	public void onClick(InventoryClickEvent e){
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR){
			return;
		}
		Player player = (Player)e.getWhoClicked();
		if(e.getView().getTopInventory().getName().startsWith("-- Mercado")){
			e.setCancelled(true);
			if(e.getRawSlot() > e.getView().getTopInventory().getSize()){
				return;
			}
			int page = Integer.parseInt(e.getInventory().getName().replaceAll("[\\D]", ""));
		
			if(e.getCurrentItem().isSimilar(VirtualChest.nextPage)){
				//e.getView().close();
				e.getView().getPlayer().openInventory(VirtualChest.newListInventory(++page, player));
				return;
			} else if(e.getCurrentItem().isSimilar(VirtualChest.previusPage)){
				//e.getView().close();
				e.getView().getPlayer().openInventory(VirtualChest.newListInventory(page == 1 ? 1 : --page, player));
				return;
			}
			
			e.getView().close();
			//OPEN NEW INVENTORY
			try{
				if(Mercado.getMercadoItens().get(page == 1 ? e.getSlot() : e.getSlot() + 45 * (page - 1)).getItem().getType() == Material.AIR){
					((Player)e.getWhoClicked()).sendMessage("§cOps, o item não existe mais");
					return;
				}
				e.getView().getPlayer().openInventory(VirtualChest.newBuyInventory((Player) e.getView().getPlayer(), page == 1 ? e.getSlot() : e.getSlot() + 45 * (page - 1)));
			
			}catch (IndexOutOfBoundsException ex) {
				((Player)e.getWhoClicked()).sendMessage("§cOps, o item não existe mais");
			}
			return;
		}
		if(e.getView().getTopInventory().getName().startsWith("-- Compra")){
			e.setCancelled(true);
			if(e.getRawSlot() > e.getView().getTopInventory().getSize()){
				return;
			}
			int slot = Integer.parseInt(e.getInventory().getName().substring(10, getDigit(e.getInventory().getName(), 10, 13)));
			if(e.getCurrentItem().isSimilar(VirtualChest.remove)){
				try{
					VirtualChest.removeSell((Player) e.getWhoClicked(), slot, e.getView().getTopInventory().getItem(22));
				}catch (IndexOutOfBoundsException ex) {
					((Player)e.getWhoClicked()).sendMessage("§cOps, o item não pode ser removido.");
					ex.printStackTrace();
				}
				e.getView().getPlayer().openInventory(VirtualChest.newListInventory(1, player));
				
				return;
			}
			if(e.getCurrentItem().isSimilar(VirtualChest.adminDelete)){
				VirtualChest.adminRemove(((Player)e.getWhoClicked()), slot);
				e.getView().getPlayer().openInventory(VirtualChest.newListInventory(1, player));
				return;
			}
			if(e.getCurrentItem().isSimilar(VirtualChest.backItem)){
				e.getView().getPlayer().openInventory(VirtualChest.newListInventory(1, player));
				return;
			}
			boolean r = VirtualChest.Buy((Player)e.getWhoClicked(), slot, e.getCurrentItem());
			e.getView().close();
			if(!r)
				((Player)e.getWhoClicked()).openInventory(VirtualChest.newListInventory(1, player));
			return;
		}
	}
	
	public static int getDigit(String s, int min, int max){
		for(int x = max; x >= min; x--){
			//System.out.println(x);
			if(!Character.isDigit(s.charAt(x))){
				continue;
			}
			return x + 1;
		}
		return -1;
	}
	
	
}
