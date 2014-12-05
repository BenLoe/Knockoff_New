package org.BeefSupreme.Main;

import java.lang.reflect.Field;
import java.util.Random;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
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
		sendMessage(Main.tag + ChatColor.YELLOW + p.getName() + " joined " + ChatColor.AQUA + "(" + ChatColor.YELLOW + Bukkit.getOnlinePlayers().length + ChatColor.AQUA + "/" + ChatColor.YELLOW + Files.config().getInt("Maxplayers") + ChatColor.AQUA + ").");
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
		//TODO stat change
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
		plugin.SendScoreChange(p, 5);
		p.sendMessage("§a§l+ 5 score.");
		Files.bs.addCoins(p, 35);
		p.removePotionEffect(PotionEffectType.JUMP);
		//TODO change stats
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
					f1.set(fwm, -2);
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
	
	public static void rfirework(Location loc){
		Firework fwr = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwmr = fwr.getFireworkMeta();
     
        //Our random generator
        Random r = new Random(); 

        //Get the type
        int rt = r.nextInt(5) + 1;
        Type type = Type.BALL;     
        if (rt == 1) type = Type.BALL;
        if (rt == 2) type = Type.BALL_LARGE;
        if (rt == 3) type = Type.BURST;
        if (rt == 4) type = Type.CREEPER;
        if (rt == 5) type = Type.STAR;
     
        //Get our random colours 
        int r1i = r.nextInt(17) + 1;
        int r2i = r.nextInt(17) + 1;
        Color c1 = getColor(r1i);
        Color c2 = getColor(r2i);
     
        //Create our effect with this
        FireworkEffect effectr = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
     
        //Then apply the effect to the meta
        fwmr.addEffect(effectr);
     
        //Generate some random power and set it
        int rp = r.nextInt(2) + 1;
        fwmr.setPower(rp);
     
        //Then apply this to our rocket
        fwr.setFireworkMeta(fwmr);         
    }                 
	public static Color getColor(int i) {
		Color c = null;
		if(i==1){
		c=Color.AQUA;
		}
		if(i==2){
		c=Color.BLACK;
		}
		if(i==3){
		c=Color.BLUE;
		}
		if(i==4){
		c=Color.FUCHSIA;
		}
		if(i==5){
		c=Color.GRAY;
		}
		if(i==6){
		c=Color.GREEN;
		}
		if(i==7){
		c=Color.LIME;
		}
		if(i==8){
		c=Color.MAROON;
		}
		if(i==9){
		c=Color.NAVY;
		}
		if(i==10){
		c=Color.OLIVE;
		}
		if(i==11){
		c=Color.ORANGE;
		}
		if(i==12){
		c=Color.PURPLE;
		}
		if(i==13){
		c=Color.RED;
		}
		if(i==14){
		c=Color.SILVER;
		}
		if(i==15){
		c=Color.TEAL;
		}
		if(i==16){
		c=Color.WHITE;
		}
		if(i==17){
		c=Color.YELLOW;
		}
		 
		return c;
		}

	}
