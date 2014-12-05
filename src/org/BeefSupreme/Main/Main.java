package org.BeefSupreme.Main;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import me.confuser.barapi.BarAPI;

import org.BeefSupreme.Kit.Bomber;
import org.BeefSupreme.Kit.Hulk;
import org.BeefSupreme.Kit.Miner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class Main extends JavaPlugin implements Listener {
	
	public static int NumberIngame;
	public static String GameState = "Waiting";
	public static int Countdown;
	public static List<String> PlayersIngame = new ArrayList<String>();
	public static List<String> PlayersSpectating = new ArrayList<String>();
	public static HashMap<Sheep, Integer> SheepBlowup = new HashMap<Sheep, Integer>();
	public static HashMap<String, String> Kit = new HashMap<String, String>();
	public static HashMap<String, Integer> score = new HashMap<String, Integer>();
	public static List<UUID> tnts = new ArrayList<UUID>();
	public static List<Location> Changing = new ArrayList<Location>();
	public static List<Location> Changing3 = new ArrayList<Location>();
	public static List<Location> Changing2 = new ArrayList<Location>();
	public static String tag = ChatColor.AQUA + "[" + ChatColor.DARK_AQUA + "Knockoff" + ChatColor.AQUA + "]: ";
	public static List<String> HulkSmash = new ArrayList<String>();
	public static List<String> Cooldown = new ArrayList<String>();
	public final static HashMap<String, Integer> Cooldowntime = new HashMap<String, Integer>();
	public final Files files = new Files(this);
	public final Events events = new Events(this);
	public final Game game = new Game(this);
	public static Location place1;
	public static Location place2;
	
	@SuppressWarnings("deprecation")
	public void onEnable(){
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		PlayersIngame.clear();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run(){
				for (Player p : Bukkit.getOnlinePlayers()){
					if (PlayersIngame.contains(p.getName())){
						if (p.getExp() < 0.9F && Kit.get(p.getName()).equals("Miner")){
							p.setExp(p.getExp() + 0.025f);
						}
					}
				if (Cooldown.contains(p.getName())){
				if (p.getExp() < 0.1F){
					p.setExp(0F);
					Cooldown.remove(p.getName());
				}else{
					if (PlayersIngame.contains(p.getName())){
					if (Kit.get(p.getName()).equals("Bomber")){
					p.setExp(p.getExp() - 0.025F);
					}else if (Kit.get(p.getName()).equals("Hulk")){
						p.setExp(p.getExp() - 0.010F);
					}
					}
				}
				}
				if (HulkSmash.contains(p.getName())){
					ParticleEffect.ANGRY_VILLAGER.display(p.getLocation().add(0, 1, 0), 16, 1, 1, 1, 1, 5);
				}
				}
				for(Sheep sheep : SheepBlowup.keySet()){
					if (SheepBlowup.get(sheep) == 0){
						SheepBlowup.remove(sheep);
					Entity e =sheep.getWorld().spawnEntity(sheep.getLocation(), EntityType.PRIMED_TNT);
					TNTPrimed tnt = (TNTPrimed) e;
					tnt.setFuseTicks(0);
					tnts.add(e.getUniqueId());
					Game.SheepFirework(sheep.getLocation());
					sheep.damage(20.0);
					}else{
					switch(SheepBlowup.get(sheep)){
					case 10:{
						sheep.setSheared(false);
						sheep.setColor(DyeColor.RED);
					}
					break;
					case 9:{
						sheep.setSheared(false);
						sheep.setColor(DyeColor.RED);
					}
					break;
					case 8:{
						sheep.setSheared(true);
					}
					break;
					case 7:{
						sheep.setSheared(true);
					}
					break;
					case 6:{
						sheep.setSheared(false);
						sheep.setColor(DyeColor.RED);
					}
					break;
					case 5:{
						sheep.setSheared(false);
						sheep.setColor(DyeColor.RED);
					}
					break;
					case 4:{
						sheep.setSheared(true);
					}
					break;
					case 3:{
						sheep.setSheared(true);
					}
					break;
					case 2:{
						sheep.setSheared(false);
						sheep.setColor(DyeColor.RED);
					}
					break;
					case 1:{
						sheep.setSheared(false);
						sheep.setColor(DyeColor.RED);
					}
					}
					int newnumber = SheepBlowup.get(sheep) - 1;
					SheepBlowup.remove(sheep);
					SheepBlowup.put(sheep, newnumber);
				}
				}
				if (Changing3.size() != 0){
					if (Changing3.size() == 1){
						Block b = Changing3.get(0).getBlock();
						Changing3.remove(b.getLocation());
						b.getWorld().spawnFallingBlock(b.getLocation().add(0, 1, 0), b.getType(), b.getData()).setVelocity(new Vector(0, 0.4, 0));
						b.setType(Material.AIR);
						Changing3.remove(b);
					}else{
				Random r = new Random();
				int i = r.nextInt(Changing3.size());
				Block b = Changing3.get(i).getBlock();
				Changing3.remove(b.getLocation());
				b.getWorld().spawnFallingBlock(b.getLocation().add(0, 1, 0), b.getType(), b.getData()).setVelocity(new Vector(0, 0.4, 0));
				b.setType(Material.AIR);
				}
				}
				for (int i = 0; i < Changing2.size(); i++){
					Location loc = Changing2.get(i);
					Block b = loc.getBlock();
						b.setData((byte)14);
						Changing2.remove(loc);
						Changing3.add(loc);
				}
				for (int i = 0; i < Changing.size(); i++){
					Location loc = Changing.get(i);
					Block b = loc.getBlock();
					b.setData((byte) 1);
					Changing.remove(loc);
					Changing2.add(loc);
				}
			}
		}, 10l, 2l);
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable(){
			public void run() {
				if (getConfig().getBoolean("Enabled")){
				if (GameState.equals("Waiting")){
					NumberIngame = Bukkit.getOnlinePlayers().length;
					if (NumberIngame >= getConfig().getInt("Playerstostart")){
						GameState = "Starting";
						for (Player p : Bukkit.getOnlinePlayers()){
									p.sendMessage(tag + ChatColor.YELLOW + "Game starting in 15 seconds.");
									BarAPI.setMessage(p, ChatColor.DARK_AQUA + "| Knockoff | " + ChatColor.YELLOW + "Game starting in " + ChatColor.GREEN + Countdown + ChatColor.YELLOW + " seconds.", 1F);
						}
					}else{
						for (Player p : Bukkit.getOnlinePlayers()){
									int players2start = getConfig().getInt("Playerstostart") - NumberIngame;
									BarAPI.setMessage(p, ChatColor.DARK_AQUA + "| Knockoff | " + ChatColor.YELLOW + "Waiting for " + ChatColor.GREEN + players2start + ChatColor.YELLOW + " more players." , 1F);
						}
					}
				}
				if (GameState.equals("Starting")){
					if (NumberIngame < getConfig().getInt("Playerstostart")){
						GameState = "Waiting";
					}
					if (Countdown == 0){
						for (Player p : Bukkit.getOnlinePlayers()){
									p.sendMessage(tag + ChatColor.YELLOW + "Let the game begin!");
									p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, 1);
									BarAPI.setMessage(p, ChatColor.DARK_AQUA + "| Knockoff | " + ChatColor.YELLOW + "Game Starting!", 1);
									Random r = new Random();
									int i = r.nextInt(4) + 1;
									int x = r.nextInt(1) + 1;
									int z = r.nextInt(1) + 1;
									Location spawn1 = Game.getLocation("spawn1");
									Location spawn2 = Game.getLocation("spawn2");
									Location spawn3 = Game.getLocation("spawn3");
									Location spawn4 = Game.getLocation("spawn4");
									switch(i){
									case 1:{
										p.teleport(spawn1.add(x, 0, z));
									}
									break;
									case 2:{
										p.teleport(spawn2.add(x, 0, z));
									}
									break;
									case 3:{
										p.teleport(spawn3.add(x, 0, z));
									}
									break;
									case 4:{
										p.teleport(spawn4.add(x, 0, z));
									}
								}
						}
						GameState = "Warmup";
						Countdown = 5;
					}else{
						for (Player p : Bukkit.getOnlinePlayers()){
									BarAPI.setMessage(p, ChatColor.DARK_AQUA + "| Knockoff | " + ChatColor.YELLOW + "Game starting in " + ChatColor.GREEN + Countdown + ChatColor.YELLOW + " seconds.", (float) 6.66 * Countdown);
						}
						if (Countdown == 5 || Countdown == 4 || Countdown == 3 || Countdown == 2 || Countdown ==1){
							for (Player p : Bukkit.getOnlinePlayers()){
										p.sendMessage(tag + ChatColor.YELLOW + "Game starting in " + Countdown + " seconds.");
										if (Countdown == 3 || Countdown == 2 || Countdown ==1){
											p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
								}
							}
						}
						Countdown = Countdown - 1;
					}
				}
				if (GameState.equals("Warmup")){
					if (Countdown == 0){
						GameState = "Ingame";
						for (Player p : Bukkit.getOnlinePlayers()){
									if (!Kit.containsKey(p.getName())){
										Kit.put(p.getName(), "Miner");
									}
									p.sendMessage(tag + ChatColor.GREEN + "Game starting! Using kit: " + ChatColor.AQUA + Kit.get(p.getName()));
									//TODO add stats change
									if (Kit.get(p.getName()).equals("Miner")){
										Miner.giveItems(p);
									}
									if (Kit.get(p.getName()).equals("Bomber")){
										Bomber.giveItems(p);
									}
									if (Kit.get(p.getName()).equals("Hulk")){
										Hulk.giveItems(p);
									}
									p.updateInventory();
									p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10000, 2));
							}
						}else{
							for (Player p : Bukkit.getOnlinePlayers()){
										p.sendMessage(tag + ChatColor.GREEN + "Receiving items in " + Countdown + " seconds!");
							}
							Countdown = Countdown - 1;
						}
				}
				if (GameState.equals("Ingame")){
					for (Player p : Bukkit.getOnlinePlayers()){
								BarAPI.setMessage(p, ChatColor.DARK_AQUA + "| Knockoff | " + ChatColor.GREEN + NumberIngame + ChatColor.YELLOW + " Players remain!", 100F);
					}
					if (NumberIngame == 1){
						for (Player p : Bukkit.getOnlinePlayers()){
							if (PlayersIngame.contains(p.getName())){
										Game.playerWin(p);
										break;
							}
						}
					}
					if (NumberIngame == 0){
						GameState = "Win";
						Countdown = 10;
						Location Place1 = Game.getLocation("1");
						Location Place2 = Game.getLocation("2");
						int x1 = Math.min(Place1.getBlockX(), Place2.getBlockX());
						int y1 = Math.min(Place1.getBlockY(), Place2.getBlockY());
						int z1 = Math.min(Place1.getBlockZ(), Place2.getBlockZ());
						//MAXIMUM
						int x2 = Math.max(Place1.getBlockX(), Place2.getBlockX());
						int y2 = Math.max(Place1.getBlockY(), Place2.getBlockY());
						int z2 = Math.max(Place1.getBlockZ(), Place2.getBlockZ());
						List<Location> blocks = new ArrayList<Location>();
						for (int x = x1; x <= x2; x++) {
						    for (int y = y1; y <= y2; y++) {
						        for (int z = z1; z <= z2; z++) {
						           blocks.add(Place1.getWorld().getBlockAt(x, y, z).getLocation()); 
						        }
						    }
						}
						for (Player p1 : Bukkit.getOnlinePlayers()){
									p1.sendMessage(ChatColor.AQUA + "✦" + ChatColor.GREEN + "------------------" + ChatColor.AQUA + "✦");
									p1.sendMessage("    ");
									p1.sendMessage("§a§lWINNER: ");
									p1.sendMessage(ChatColor.AQUA + "Well... nobody won...");
									p1.sendMessage("    ");
									p1.sendMessage(ChatColor.AQUA + "✦" + ChatColor.GREEN + "------------------" + ChatColor.AQUA + "✦");
									BarAPI.removeBar(p1);
					}
						for (int i = 0; i < blocks.size(); i++){
							Location loc1 = blocks.get(i);
							Block b = loc1.getBlock();
							Location loc2 = loc1.add(200, 0, 0);
							Block b2 = loc2.getBlock();
							if (b.getType() != b2.getType() || b.getData() != b2.getData() && !(b2.getState() instanceof Sign)){
								b.setType(b2.getType());
								b.setData(b2.getData());
							}
						}
					}
				}
				if (GameState.equals("Win")){
					int newi = Countdown - 1;
					if (newi == 0){
						for (Player p : Bukkit.getOnlinePlayers()){
							//TODO Say stat change
							sendPlayerToMain(p);
						}
						GameState = "Waiting";
						Location Place1 = Game.getLocation("1");
						Location Place2 = Game.getLocation("2");
						int x1 = Math.min(Place1.getBlockX(), Place2.getBlockX());
						int y1 = Math.min(Place1.getBlockY(), Place2.getBlockY());
						int z1 = Math.min(Place1.getBlockZ(), Place2.getBlockZ());
						//MAXIMUM
						int x2 = Math.max(Place1.getBlockX(), Place2.getBlockX());
						int y2 = Math.max(Place1.getBlockY(), Place2.getBlockY());
						int z2 = Math.max(Place1.getBlockZ(), Place2.getBlockZ());
						List<Location> blocks = new ArrayList<Location>();
						for (int x = x1; x <= x2; x++) {
						    for (int y = y1; y <= y2; y++) {
						        for (int z = z1; z <= z2; z++) {
						           blocks.add(Place1.getWorld().getBlockAt(x, y, z).getLocation()); 
						        }
						    }
						}
						for (int i = 0; i < blocks.size(); i++){
							Location loc1 = blocks.get(i);
							Block b = loc1.getBlock();
							Location loc2 = loc1.add(200, 0, 0);
							Block b2 = loc2.getBlock();
							if (b.getType() != b2.getType() || b.getData() != b2.getData() && !(b2.getState() instanceof Sign)){
								b.setType(b2.getType());
								b.setData(b2.getData());
							}
						}
						NumberIngame = 0;
						PlayersIngame.clear();
						Bukkit.shutdown();
					}else if(newi > 3){
						Location spawn1 = Game.getLocation("spawn1");
						Location spawn2 = Game.getLocation("spawn2");
						Location spawn3 = Game.getLocation("spawn3");
						Location spawn4 = Game.getLocation("spawn4");
						Game.rfirework(spawn1);
						Game.rfirework(spawn2);
						Game.rfirework(spawn3);
						Game.rfirework(spawn4);
					}
					Countdown = newi;		
	            }
			}
			}
		}, 0l, 20l);
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new Events(this), this);
		saveDefaultConfig();
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd,
			String Label, String[] args){
		if (sender instanceof Player){
			Player p = (Player) sender;
			if (Label.equalsIgnoreCase("Leave") || Label.equalsIgnoreCase("Hub") || Label.equalsIgnoreCase("Spawn")){
				sendPlayerToMain(p);
			}
			if (Label.equalsIgnoreCase("Knock")){
				if (p.hasPermission("Knock.Admin")){
					if (args.length == 0){
						p.sendMessage(ChatColor.AQUA + "------" + ChatColor.YELLOW + " Knockoff " + ChatColor.AQUA + "------");
						p.sendMessage(ChatColor.GREEN + "/knock" + ChatColor.YELLOW + " - Shows this message");
						p.sendMessage(ChatColor.GREEN + "/knock spawn (1-4)" + ChatColor.YELLOW + " - Set a spawn for an arena");
						p.sendMessage(ChatColor.GREEN + "/knock lobby" + ChatColor.YELLOW + " - Set the lobby for an arena");
						p.sendMessage(ChatColor.GREEN + "/knock enable" + ChatColor.YELLOW + " - Enable an arena");
						p.sendMessage(ChatColor.GREEN + "/knock disable" + ChatColor.YELLOW + " - Disable an arena");
						return true;
				}
					if (args.length == 1){
						if (args[0].equalsIgnoreCase("spawn")){
							p.sendMessage("Wrong");
							return true;
						}
						if (args[0].equalsIgnoreCase("lobby")){
							p.sendMessage(ChatColor.GREEN + "Lobby set to your location");
							reloadConfig();
							getConfig().set("Lobby.x", p.getLocation().getBlockX());
							getConfig().set("Lobby.y", p.getLocation().getBlockY());
							getConfig().set("Lobby.z", p.getLocation().getBlockZ());
							getConfig().set("Lobby.world", p.getWorld().getName());
							saveConfig();
							return true;
						}
						if (args[0].equalsIgnoreCase("enable")){
							if (!getConfig().getBoolean("Enabled")){
								p.sendMessage(ChatColor.GREEN + "Arena enabled");
								reloadConfig();
								getConfig().set("Enabled", true);
								saveConfig();
							}else{
								p.sendMessage(ChatColor.RED + "The arena is already enabled.");
								p.sendMessage(ChatColor.RED + "To disable the arena do " + ChatColor.AQUA + "/knock disable");
							}
							return true;
						}
						if (args[0].equalsIgnoreCase("diable")){
							if (getConfig().getBoolean("Enabled")){
								p.sendMessage(ChatColor.GREEN + "Arena disabled");
								reloadConfig();
								getConfig().set("Enabled", false);
								saveConfig();
							}else{
								p.sendMessage(ChatColor.RED + "The arena is already disabled.");
								p.sendMessage(ChatColor.RED + "To disable the arena do " + ChatColor.AQUA + "/knock enable");
							}
							return true;
						}
						if (args[0].equalsIgnoreCase("save")){
							if (place1 != null && place2 != null){
								getConfig().set("1.world", place1.getWorld().getName());
								getConfig().set("1.x", place1.getBlockX());
								getConfig().set("1.y", place1.getBlockY());
								getConfig().set("1.z", place1.getBlockZ());
								getConfig().set("2.world", place2.getWorld().getName());
								getConfig().set("2.x", place2.getBlockX());
								getConfig().set("2.y", place2.getBlockY());
								getConfig().set("2.z", place2.getBlockZ());
								saveConfig();
								List<Location> block = new ArrayList<Location>();
								Location Place1 = Game.getLocation("1");
								Location Place2 = Game.getLocation("2");
								//MINIMUM
								int x1 = Math.min(Place1.getBlockX(), Place2.getBlockX());
								int y1 = Math.min(Place1.getBlockY(), Place2.getBlockY());
								int z1 = Math.min(Place1.getBlockZ(), Place2.getBlockZ());
								//MAXIMUM
								int x2 = Math.max(Place1.getBlockX(), Place2.getBlockX());
								int y2 = Math.max(Place1.getBlockY(), Place2.getBlockY());
								int z2 = Math.max(Place1.getBlockZ(), Place2.getBlockZ());
								p.sendMessage(ChatColor.AQUA + "Saving...");
								 
								for (int x = x1; x <= x2; x++) {
								    for (int y = y1; y <= y2; y++) {
								        for (int z = z1; z <= z2; z++) {
								           block.add(Place1.getWorld().getBlockAt(x, y, z).getLocation()); 
								        }
								    }
								}
								for (Location loc : block){
									Block b = loc.getBlock();
									Location loc2 = loc.add(200, 0, 0);
									loc2.getBlock().setType(b.getType());
									loc2.getBlock().setData(b.getData());
								}
								p.sendMessage(ChatColor.GREEN + "Save complete.");
							}else{
								p.sendMessage(ChatColor.RED + "Make a selection with your Blaze Rod.");
							}
						}
						p.sendMessage(ChatColor.RED + "What are you even trying??");
					}
					if (args.length == 2){
						if (args[0].equalsIgnoreCase("spawn")){
							int i;
							try{
							i = Integer.parseInt(args[1]);
							}catch(Exception e){
								p.sendMessage(ChatColor.RED + "Incorrect syntax!");
								p.sendMessage(ChatColor.RED + "Syntax: " + ChatColor.AQUA + "/knock spawn (1-4)");
								return true;
							}
							if (i > 4 || i < 1){
								p.sendMessage(ChatColor.RED + "Spawn number must be from 1-4.");
								p.sendMessage(ChatColor.RED + "Syntax: " + ChatColor.AQUA + "/knock spawn (1-4)");
								return true;
							}else{
								p.sendMessage(ChatColor.GREEN + "Set spawn " + args[1] + " to your location");
								reloadConfig();
								getConfig().set("spawn" + args[1] + ".x", p.getLocation().getBlockX());
								getConfig().set("spawn" + args[1] + ".y", p.getLocation().getBlockY());
								getConfig().set("spawn" + args[1] + ".z", p.getLocation().getBlockZ());
								getConfig().set("spawn" + args[1] + ".world", p.getWorld().getName());
								saveConfig();
							}
							return true;
						}
						p.sendMessage(ChatColor.RED + "What are you even trying??");
					}
				}else{
					p.sendMessage(ChatColor.RED + "You can't use these commands");
					return true;
				}
			}
			return true;
		}else{
			System.out.println("Nope");
			return true;
		}
		}
	public static void sendPlayerToMain(Player p){
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
        try {
			out.writeUTF("Connect");
	        out.writeUTF("main");
		} catch (IOException e) {
			e.printStackTrace();
		}
		p.sendPluginMessage(Main.getPlugin(Main.class), "BungeeCord", b.toByteArray());
	}
	public void SendScoreChange(Player p, int number){
		if (Bukkit.getOnlinePlayers().length == 100){
		 ByteArrayDataOutput out = ByteStreams.newDataOutput();
		  out.writeUTF("Forward");
		  out.writeUTF("ALL");
		  out.writeUTF("KnockoffScore");

		  ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
		  DataOutputStream msgout = new DataOutputStream(msgbytes);
		  try {
			msgout.writeUTF(p.getName());
			  msgout.writeShort(number);
		} catch (IOException e) {
			e.printStackTrace();
		}

		  out.writeShort(msgbytes.toByteArray().length);
		  out.write(msgbytes.toByteArray());
		  p.sendPluginMessage(this, "BungeeCord", out.toByteArray());
	}else{
		
	}
	}

}
