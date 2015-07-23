package org.BeefSupreme.Main;

import java.lang.reflect.Field;
import java.util.Random;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffectType;

public class Game {
	public static Main plugin;
	public Game(Main instance){
		plugin = instance;
	}
	public static void sendMessage(String Message){
		for(Player p : Bukkit.getOnlinePlayers()){
			if (Main.PlayersIngame.contains(p.getName()) || Main.PlayersSpectating.contains(p.getName())){
				p.sendMessage(Message);
			}
		}
	}
	
	public static Location getLocation(String type){
		Location loc = new Location(Bukkit.getWorld(Files.config().getString(type + ".world")), Files.config().getInt(type + ".x"), Files.config().getInt(type + ".y"), Files.config().getInt(type + ".z"));
		return loc;
	}

	public static boolean isPlayerAlive(Player p){
		if (Main.PlayersIngame.contains(p.getName())){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isPlayerSpectating(Player p){
		if (Main.PlayersSpectating.contains(p.getName())){
			return true;
		}else{
			return false;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void playerJoinGame(Player p){
		p.getInventory().clear();
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.updateInventory();
		p.teleport(getLocation("Lobby"));
		Main.PlayersIngame.add(p.getName());
		Main.NumberIngame = Main.NumberIngame + 1;
		Main.Kit.put(p.getName(), "Miner");
		Main.score.put(p.getName(), 0);
		sendMessage(Main.tag + ChatColor.YELLOW + p.getName() + " joined " + ChatColor.AQUA + "(" + ChatColor.YELLOW + Bukkit.getOnlinePlayers().size() + ChatColor.AQUA + "/" + ChatColor.YELLOW + Files.config().getInt("Maxplayers") + ChatColor.AQUA + ").");
		if (Main.GameState.equals("Waiting") && Main.NumberIngame >= Files.config().getInt("Playerstostart")){
			Main.GameState = "Starting";
			sendMessage(Main.tag + ChatColor.AQUA + "Game starting in 15 seconds.");
			Main.Countdown = 15;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void playerLeaveGame(Player p){
		p.removePotionEffect(PotionEffectType.JUMP);
		p.getInventory().clear();
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.updateInventory();
		if (Main.GameState.equals("Waiting") || Main.GameState.equals("Starting")){
			Main.PlayersIngame.remove(p.getName());
			Main.NumberIngame = Main.NumberIngame - 1;
			sendMessage(Main.tag + ChatColor.YELLOW + p.getName() + " left the game.");
			if (Main.GameState.equals("Starting")){
				if (Main.NumberIngame < Files.config().getInt("Playerstostart")){
					Main.GameState = "Waiting";
					Main.Countdown = 15;
				}
			}
		}else{
			if (Main.PlayersIngame.contains(p.getName())){
				sendMessage(Main.tag + ChatColor.YELLOW + p.getName() + " left the game.");
				Main.NumberIngame = Main.NumberIngame - 1;
				if (Main.NumberIngame == 1){
					for (Player p1 : Bukkit.getOnlinePlayers()){
						if (Main.PlayersIngame.contains(p.getName())){
									playerWin(p1);
									break;
								}
							}
					}
			}else if (Main.PlayersSpectating.contains(p.getName())){
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static synchronized void playerDie(Player p){
		p.removePotionEffect(PotionEffectType.JUMP);
		Main.PlayersIngame.remove(p.getName());
		Main.PlayersSpectating.add(p.getName());
		if (Main.HulkSmash.contains(p.getName())){
		Main.HulkSmash.remove(p.getName());
		}
		p.teleport(Game.getLocation("Lobby"));
		p.setHealth(20.0);
		p.getInventory().clear();
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.updateInventory();
		String death = "";
		Random r = new Random();
		int message = r.nextInt(5) + 1;
		switch(message){
		case 1:{
			death = Main.tag + ChatColor.AQUA + p.getName() + ChatColor.RED + " didn't see the holes and fell off.";
		}
		break;
		case 2:{
			death = Main.tag + ChatColor.AQUA + p.getName() + ChatColor.RED + " wanted to know what the void is like.";
		}
		break;
		case 3:{
			death = Main.tag + ChatColor.AQUA + p.getName() + ChatColor.RED + " forgot to tie their shoes and tripped.";
		}
		break;
		case 4:{
			death = Main.tag + ChatColor.AQUA + p.getName() + ChatColor.RED + " tried to fly but did not succeed.";
		}
		break;
		case 5:{
			death = Main.tag + ChatColor.AQUA + p.getName() + ChatColor.RED + " just figured out what the afterlife is like.";
		}
		}
		Main.NumberIngame = Main.NumberIngame - 1;
		for (Player p1 : Bukkit.getOnlinePlayers()){
				p1.sendMessage(death);
				p1.sendMessage(Main.tag + ChatColor.AQUA + Main.NumberIngame + ChatColor.YELLOW + " players remain.");
				BarAPI.removeBar(p1);
				BarAPI.setMessage(p1, ChatColor.DARK_AQUA + "| Knockoff | " + ChatColor.AQUA + p.getName() + " is eliminated!", 2);
				if (p1.getName() != p.getName() && Main.PlayersIngame.contains(p.getName())){
					Files.bs.addCoins(p, 4); 
				}
			}
		if (Main.NumberIngame == 1){
			for (Player p1 : Bukkit.getOnlinePlayers()){
				if (Main.PlayersIngame.contains(p.getName())){
							playerWin(p1);
							break;
						}
					}
			}
	}
	
	@SuppressWarnings("deprecation")
	public static void playerWin(Player p){
		try {
			Files.bs.addCoins(p, 35);
		} catch (Exception e) {
			e.printStackTrace();
		}
		p.removePotionEffect(PotionEffectType.JUMP);
		p.getInventory().clear();
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		Main.HulkSmash.remove(p.getName());
		p.updateInventory();
		Main.Countdown = 10;
		Main.GameState = "Win";
		for (Player p1 : Bukkit.getOnlinePlayers()){
					p1.sendMessage(ChatColor.AQUA + "✦" + ChatColor.GREEN + "------------------" + ChatColor.AQUA + "✦");
					p1.sendMessage("    ");
					p1.sendMessage("§a§lWINNER: ");
					p1.sendMessage(ChatColor.AQUA + p.getName() + " has won the game!");
					p1.sendMessage("    ");
					p1.sendMessage(ChatColor.AQUA + "✦" + ChatColor.GREEN + "------------------" + ChatColor.AQUA + "✦");
					BarAPI.removeBar(p);
					p.removePotionEffect(PotionEffectType.JUMP);
	}		
	}
			
		
		
	
	public static void SheepFirework(Location loc){
			Firework fw = (Firework) loc.getWorld().spawn(loc, Firework.class);
			FireworkEffect effect = FireworkEffect.builder().trail(true).flicker(false).withColor(Color.RED, Color.BLACK).with(Type.BURST).build();
			FireworkMeta fwm = fw.getFireworkMeta();
			fwm.clearEffects();
			fwm.addEffect(effect);
			Field f1;
			try {
				f1 = fwm.getClass().getDeclaredField("power");
				f1.setAccessible(true);
				try {
					f1.set(fwm, -1);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			fw.setFireworkMeta(fwm);
			}
	public static void Firework(Location loc){
		Firework fw = (Firework) loc.getWorld().spawn(loc, Firework.class);
		FireworkEffect effect = FireworkEffect.builder().trail(true).flicker(false).withColor(Color.AQUA, Color.WHITE).with(Type.BALL_LARGE).build();
		FireworkMeta fwm = fw.getFireworkMeta();
		fwm.clearEffects();
		fwm.setPower(1);
		fwm.addEffect(effect);
		fw.setFireworkMeta(fwm);
		}

	}
