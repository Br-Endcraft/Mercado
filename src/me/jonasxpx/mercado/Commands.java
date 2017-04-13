package me.jonasxpx.mercado;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jonasxpx.geoplayers.GeoAPI;

public class Commands implements CommandExecutor{

	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player){
			Player p = (Player)sender;
			if(args.length == 1 && args[0].equalsIgnoreCase("fix") && sender.hasPermission("mercado.admin")){
				VirtualChest.removeAirBlocks();
				sender.sendMessage("§cBlocos de ar removidos");
				return true;
			}
			if(args.length >= 1 && args[0].equalsIgnoreCase("vender")){
				if(!sender.hasPermission("mercado.vender")){
					sender.sendMessage("§cVocê não pode usar este comando.");
					return true;
				}
				if(args.length == 1){
					sender.sendMessage("§cVocê precisa informar um valor.\n"
							+ "§6/mercado vender <valor> [restrito<sim>]");
					return true;
				}
				try{
					if(p.getItemInHand().getType() == Material.AIR){
						p.sendMessage(Mercado.getLanguageKey(GeoAPI.getRegion(p), "hold_item"));
						return true;
					}
					if(Mercado.bannedItens.contains(p.getItemInHand().getType())){
						p.sendMessage("§cEsse item não pode ser vendido no mercado.");
						return true;
					}
					if(Long.parseLong(args[1]) < 0){
						p.sendMessage("§cEsse valor é inválido.");
						return true;
					}
					VirtualChest.putToSell(p, p.getInventory().getItemInHand(), Long.parseLong(args[1]), args.length == 3 && args[2].equalsIgnoreCase("sim") ? true:false);
					p.sendMessage("§bVocê colocou um item a venda.");
				}catch (NumberFormatException e) {
					sender.sendMessage("§cValor inválido.");
					return true;
				}
				return true;
			}
			try{
				((Player)sender).openInventory(VirtualChest.newListInventory(args.length == 1 ? (Integer.parseInt(args[0]) <= 0 ? 1 : Integer.parseInt(args[0]))  : 1, p));
			}catch (NumberFormatException e) {
				sender.sendMessage("Use /mercado [Página]");
			}
			return true;
		}
		return false;
	}
}
