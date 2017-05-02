package me.jonasxpx.mercado;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import me.jonasxpx.geoplayers.GeoAPI;
import me.jonasxpx.geoplayers.RegionCodes;
import net.milkbowl.vault.economy.Economy;

public class Mercado extends JavaPlugin{
	
	
	public static FileConfiguration file;
    public static Economy economy = null;
    public static ArrayList<Material> bannedItens = new ArrayList<>();
    public static HashMap<String, HashMap<String, String>> location = new HashMap<>();
    public static int currentMensages = 0;
    public static double minAmount = 0;
    public static boolean geoPlayer;
    public static FileConfiguration lang;
    private static Mercado instance;
    
	@SuppressWarnings("static-access")
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new Events(), this);
		getCommand("mercado").setExecutor(new Commands());
		file = getConfig();
		loadConfig();
		CachedMercado.loadCached(new File(getDataFolder(), "currentsellitens.dat"));
		setupEconomy();
		loadPageItens();
		this.instance = this;
		geoPlayer = getServer().getPluginManager().getPlugin("GeoPlayers") != null;
		bannedItens.add(Material.FIREWORK);
		bannedItens.add(Material.LEATHER_HELMET);
		bannedItens.add(Material.LEATHER_CHESTPLATE);
		bannedItens.add(Material.LEATHER_LEGGINGS);
		bannedItens.add(Material.LEATHER_BOOTS);
		bannedItens.add(Material.INK_SACK);
		bannedItens.add(Material.GRASS);
		bannedItens.add(Material.POTION);
		saveResource("location.yml", true);
		//reloadConfig();
		lang = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "location.yml"));
		for(String s : lang.getConfigurationSection("lang").getKeys(false)){
			HashMap<String, String> t_hash = new HashMap<>();
			for(String keys : Lists.newArrayList(lang.getConfigurationSection("lang." + s).getKeys(true))){
				t_hash.put(keys, lang.getString("lang." + s + "." + keys));
			}
			location.put(s, t_hash);
		}
		task();
		getServer().getConsoleSender().sendMessage("§cMercado was enabled.");
	}
	
	@Override
	public void onDisable() {
		VirtualChest.removeAirBlocks();
		CachedMercado.save(new File(getDataFolder(), "currentsellitens.dat"));
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<SellItem> getMercadoItens(){
		return (ArrayList<SellItem>) CachedMercado.getCached();
	}
	private boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
	
	public void loadPageItens(){
		VirtualChest.nextPage = new ItemStack(Material.WOOL, 1, (short)5);

		VirtualChest.previusPage = new ItemStack(Material.WOOL, 1, (short)14);

		VirtualChest.remove = new ItemStack(Material.WOOL, 1, (short)14);

		VirtualChest.adminDelete = new ItemStack(Material.STICK);

		VirtualChest.backItem = new ItemStack(Material.WOOL, 1, (short)2);
	}
	
	private void loadConfig(){
		if(!file.contains("DinheiroMinimo")){
			file.set("DinheiroMinimo", 250000.0D);
			saveConfig();
		}
		minAmount = file.getDouble("DinheiroMinimo");
	}
	
	public static ItemStack getItem(RegionCodes rc, ItemType i){
		HashMap<String, String> l = location.containsKey(rc.name().toLowerCase()) ? location.get(rc.name().toLowerCase()) : location.get(RegionCodes.US.name().toLowerCase());
		ItemStack item = i.item;
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(i.prefix + l.get(i.lang_selector) + i.suffix);
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack getItem(ItemType i){
		HashMap<String, String> l = location.get(RegionCodes.BR.name().toLowerCase());
		ItemStack item = i.item;
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(i.prefix + l.get(i.lang_selector) + i.suffix);
		item.setItemMeta(meta);
		return item;
	}
	
	public static String getLanguageKey(RegionCodes rc, String key){
		HashMap<String, String> l = location.containsKey(rc.name().toLowerCase()) ? location.get(rc.name().toLowerCase()) : location.get(RegionCodes.US.name().toLowerCase());
		if(!l.containsKey(key)){
			return ("§f[LANG] erro, key: " + key);
		}
		return ChatColor.translateAlternateColorCodes('&', l.get(key));
	}
	
	public static Logger getStaticLogger(){
		return instance.getLogger();
	}
	
	private void task(){
		new BukkitRunnable() {
			@Override
			public void run() {
				if(currentMensages != 0){
					Lists.newArrayList(Bukkit.getOnlinePlayers()).forEach(play -> {
						play.sendMessage(getLanguageKey(GeoAPI.getRegion(play), "broadcast").replaceFirst("%currentMensages%", Integer.toString(currentMensages)));
					});
					currentMensages = 0;
				}
			}
		}.runTaskTimer(this, 0, 20 * 30);
	}
}
