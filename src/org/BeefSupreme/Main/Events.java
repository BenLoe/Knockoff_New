package org.BeefSupreme.Main;

import java.util.Random;

import org.BeefSupreme.Kit.Bomber;
import org.BeefSupreme.Kit.Hulk;
import org.BeefSupreme.Kit.Miner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class Events implements Listener{
	
	public static Main plugin;
	public Events(Main instance){
		plugin = instance;
	}
	
	@EventHandler
	public void loginEvent(PlayerLoginEvent event){
		Player p = event.getPlayer();
		if (Main.GameState.equals("Ingame") || Main.GameState.equals("Warmup") || Main.GameState.equals("Win")){
			event.disallow(Result.KICK_OTHER, "Ingame!");
		}
		if (Main.GameState.equals("Waiting") || Main.GameState.equals("Starting")){
			if (Main.NumberIngame == Files.config().getInt("Maxplayers")){
				if (p.hasPermission("Game.joinfull")){
					for (Player p1 : Bukkit.getOnlinePlayers()){
						if (!p1.hasPermission("Game.joinfull")){
							for (; ;){
						Random r = new Random();
						int player = r.nextInt(Bukkit.getOnlinePlayers().length);
						Player p2 = Bukkit.getOnlinePlayers()[player];
						if (!p2.hasPermission("Game.joinfull")){
							p2.sendMessage(ChatColor.RED + "You have been sent to the main server to make room for a vip or staff member.");
							Main.sendPlayerToMain(p2);
							break;
						}
						}
						return;
						}
					}
					event.disallow(Result.KICK_OTHER, "Game is full of vips or staff members, please choose another.");
				}else{
					event.disallow(Result.KICK_OTHER, "Game full, to join full games you must buy vip.");
				}
			}
		}
	}
	@EventHandler
	public void clickEvent(PlayerInteractEvent event){
		final Player p = event.getPlayer();
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock().getState() instanceof Sign){
			Sign sign = (Sign) event.getClickedBlock().getState();
			if (sign.getLine(0).equals("[Kit]") && (Main.GameState.equals("Waiting") || Main.GameState.equals("Starting"))){
				if (sign.getLine(1).equals("Miner")){
					Miner.sendInfo(p);
				}
				if (sign.getLine(1).equals("Bomber")){
					Bomber.sendInfo(p);
				}
				if (sign.getLine(1).equals("Hulk")){
					Hulk.sendInfo(p);
				}
			}
		}
		if (Main.GameState.equals("Ingame")){
			if (p.getInventory().getItemInHand().getType().equals(Material.IRON_SWORD)){
				if (Main.Kit.get(p.getName()).equals("Miner")){
					Miner.executeBasic(p);
				}
				if (Main.Kit.get(p.getName()).equals("Bomber")){
					Bomber.executeBasic(p);
				}
				if (Main.Kit.get(p.getName()).equals("Hulk")){
					Hulk.executeBasic(p);
				}
			}
		}
		if (p.getInventory().getItemInHand().getType().equals(Material.BLAZE_ROD)){
			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				p.sendMessage(ChatColor.GREEN + "Place 1 set.");
				Main.place1 = event.getClickedBlock().getLocation();
			}
			if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
				p.sendMessage(ChatColor.GREEN + "Place 2 set.");
				Main.place2 = event.getClickedBlock().getLocation();
			}
		}
		
	}
	
	@EventHandler
	public void eggShoot(PlayerEggThrowEvent event){
		Player p = event.getPlayer();
		Location loc = event.getEgg().getLocation();
		if (Main.GameState.equals("Ingame")){
			event.setHatching(false);
			if (Main.Kit.get(p.getName()).equals("Miner")){
				Miner.executeSpecial(loc);
			}
			if (Main.Kit.get(p.getName()).equals("Bomber")){
				Bomber.executeSpecial(loc);
			}
			if (Main.Kit.get(p.getName()).equals("Hulk")){
				Hulk.executeSpecial(loc, event.getEgg());
			}
		}
	}
	
	@EventHandler
	public void damageEvent(EntityDamageEvent event){
		if (event.getEntity() instanceof Player){
			Player p = (Player) event.getEntity();
			if (Main.PlayersIngame.contains(p.getName())){
				if (event.getCause().equals(DamageCause.FALL) && Main.HulkSmash.contains(p.getName())){
					event.setCancelled(true);
					Hulk.executeBasic2(p);
				}
				}
			if (event.getCause().equals(DamageCause.FALL) || event.getCause().equals(DamageCause.BLOCK_EXPLOSION)){
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void damageByEntity(EntityDamageByEntityEvent event){
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player){
			Player p = (Player) event.getEntity();
			Player Damager = (Player) event.getDamager();
			if (Main.PlayersIngame.contains(p.getName())){
				if (Damager.getInventory().getItemInHand().getType().equals(Material.STICK)){
					event.setDamage(0.0);
				}else{
					event.setCancelled(true);
				}
			}
		}
	}
	@SuppressWarnings("static-access")
	@EventHandler
	public void playerMove(PlayerMoveEvent event){
		Player p = event.getPlayer();
		if (Main.GameState.equals("Ingame")){
				if (event.getTo().getBlockY() <= Files.config().getInt("Ytodie")){
					Game.playerDie(p);
				}
		}
         if (Main.GameState.equals("Win") || Main.GameState.equals("Warmup")){
				if (event.getTo().getBlockY() <= Files.config().getInt("Ytodie")){
				p.teleport(Game.getLocation("spawn1"));
				}
			}
	}
	@EventHandler
	public void playerJoin(PlayerJoinEvent event){
		Game.playerJoinGame(event.getPlayer());
	}
	
	@EventHandler
	public void playerLeave(PlayerQuitEvent event){
		Game.playerLeaveGame(event.getPlayer());
	}
	
	@EventHandler
	public void ontab(ServerListPingEvent event){
		if (Main.GameState.equals("Waiting") || Main.GameState.equals("Starting")){
			if (Main.NumberIngame != Files.config().getInt("Maxplayers")){
				event.setMotd("§a§l[Join]");
			}else{
				event.setMotd("§9§l[Full]");
			}
		}else if (Main.GameState.equals("Win")){
			event.setMotd("§4§lRESTARTING");
		}else{
			event.setMotd("§7[In-Game]");
		}
		}
	@EventHandler
	public void blockBreak(BlockBreakEvent event){
		if (!event.getPlayer().isOp()){
		event.setCancelled(true);
		}
	}
}
