package com.excavator.freeze;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
//import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
//import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class Freeze extends JavaPlugin implements Listener {
	
	private final List<UUID> frozen = new ArrayList<>();
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("ss")) {
			return true;
		}
		if(!(sender instanceof Player)) {
			sender.sendMessage("This can't be run through console!");
			return true;
		}
		Player player = ((Player) sender);
		
		if(!player.hasPermission("ss.use")) {
			player.sendMessage(ChatColor.RED + "No permission!");
			return true;
		}
		
		
		if(!(args.length > 0)) {
			player.sendMessage(ChatColor.RED + "Please provide a name!");
			return true;
		}
		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			player.sendMessage(ChatColor.RED + "The user is offline or the name is invalid!");
			return true;
		}
		if(frozen.contains(target.getUniqueId())) {				//toggle
			frozen.remove(target.getUniqueId()); 
			player.sendMessage(ChatColor.RED + "You unfroze " + ChatColor.AQUA + target.getName());
			target.sendMessage(ChatColor.RED + "You have been unfrozen!");
			return true;
		}
		frozen.add(target.getUniqueId()); //frozen
		Inventory gui = Bukkit.createInventory(target, 9, ChatColor.RED + "You have been frozen!");
		ItemStack item = new ItemStack(Material.BARRIER);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_RED + "YOU ARE FROZEN!");
		ArrayList<String> lores = new ArrayList<>();
		lores.add(ChatColor.GOLD + "Contact our staff in 5 minutes on discord.shootmc.xyz or you will recieve a permanent ban.");
		meta.setLore(lores);
		item.setItemMeta(meta);
		
		ItemStack[] items = {item,item,item,item,item,item,item,item,item};
		gui.setContents(items);
		target.openInventory(gui);
		player.sendMessage(ChatColor.RED + "You froze " + ChatColor.AQUA + target.getName());
		target.sendMessage(ChatColor.RED + "You have been frozen!");
		return true;
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		Player player = (Player) e.getPlayer();
		Inventory gui = Bukkit.createInventory(player, 9, ChatColor.RED + "You have been frozen!");
		ItemStack item = new ItemStack(Material.BARRIER);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_RED + "YOU ARE FROZEN!");
		ArrayList<String> lores = new ArrayList<>();
		lores.add(ChatColor.GOLD + "Contact our staff in 5 minutes on Discord, or you will recieve a permanent ban.");
		meta.setLore(lores);
		item.setItemMeta(meta);
		
		ItemStack[] items = {item,item,item,item,item,item,item,item,item};
		gui.setContents(items);

		if(frozen.contains(player.getUniqueId())) {
			new BukkitRunnable() {
			    public void run() {
					player.openInventory(gui);
			    }
			}.runTaskLater(this, 2l);

		}
		
	}
	@EventHandler
	public void clickEvent(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		if(frozen.contains(player.getUniqueId())) {
		e.setCancelled(true);
		}
	}
	/*
	 this is for movement blocking, currently not needed
	 
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		Location from = e.getFrom();
		Location to = e.getTo();
		
		if(frozen.contains(player.getUniqueId())) {
			if(to.getBlockX() == from.getBlockX() && to.getBlockZ() == from.getBlockZ() && to.getBlockY() == from.getBlockY()) {
				return;
			}
				e.setCancelled(true);
				player.sendMessage(ChatColor.RED + "You can't move while frozen!");
			
		}
		
		
		

		}
	*/
	

}
