package org.BeefSupreme.Kit;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.BeefSupreme.Main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

public class Bomber {
	
	public static void sendInfo(Player p){
		p.sendMessage("    ");
		p.sendMessage("    ");
		p.sendMessage("    ");
		p.sendMessage("    ");
		p.sendMessage("    ");
		p.sendMessage("    ");
		p.sendMessage(ChatColor.GREEN + "--------------[ " + ChatColor.AQUA + "Bomber" + ChatColor.GREEN + " ]--------------");
		p.sendMessage("    ");
		p.sendMessage(ChatColor.BOLD + "Skill: " + ChatColor.YELLOW + "Makes an explosion at your feet that breaks blocks.");
		p.sendMessage("    ");
		p.sendMessage(ChatColor.BOLD + "Special Skill: " + ChatColor.YELLOW + "Spawns a ''Bomber Sheep'' that blows up and throws blocks everywhere.");
		p.sendMessage("    ");
		p.sendMessage(ChatColor.GREEN + "------------------------------------");
		if (p.hasPermission("knock.Bomber")){
		Main.Kit.remove(p.getName());
		Main.Kit.put(p.getName(), "Bomber");
			p.sendMessage(Main.tag + ChatColor.AQUA + "Kit set to: " + ChatColor.YELLOW + "Bomber");
		}else{
			p.sendMessage(Main.tag + ChatColor.RED + "You must buy this kit in the shop.");
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void executeBasic(Player p){
		if (Main.Cooldown.contains(p.getName())){
			p.sendMessage(Main.tag + ChatColor.YELLOW + "Skill still in cooldown!");
		}else{
		Main.Cooldown.add(p.getName());
		p.setExp(1F);
int x = p.getLocation().getBlockX();
int y = p.getLocation().getBlockY();
int z = p.getLocation().getBlockZ();
final List<Location> Blocks = new ArrayList<Location>();
Location loc1 = new Location(p.getWorld(), x, y - 1, z);
Location loc2 = new Location(p.getWorld(), x + 1, y - 1, z);
Location loc3 = new Location(p.getWorld(), x, y - 1, z + 1);
Location loc4 = new Location(p.getWorld(), x + 1, y - 1, z + 1);
Location loc5 = new Location(p.getWorld(), x - 1, y - 1, z);
Location loc6 = new Location(p.getWorld(), x, y - 1, z - 1);
Location loc7 = new Location(p.getWorld(), x - 1, y - 1, z - 1);
Location loc8 = new Location(p.getWorld(), x + 1, y - 1, z - 1);
Location loc9 = new Location(p.getWorld(), x - 1, y - 1, z + 1);
Blocks.add(loc1);
Blocks.add(loc2);
Blocks.add(loc3);
Blocks.add(loc4);
Blocks.add(loc5);
Blocks.add(loc6);
Blocks.add(loc7);
Blocks.add(loc8);
Blocks.add(loc9);
for (Location loc : Blocks){
	Block b = loc.getBlock();
	Random r = new Random();
	int chance = r.nextInt(2) + 1;
	if (!b.getType().equals(Material.AIR) && chance == 1){
		float x1 = (float) -0.5 + (float) (Math.random() * ((0.5 - -0.5) + 1));
		float z1 = (float) -0.5 + (float) (Math.random() * ((0.5 - -0.5) + 1));
			FallingBlock falling = p.getWorld().spawnFallingBlock(loc.add(0, 1, 0), b.getType(), b.getData());
			falling.setVelocity(new Vector(x1, 1, z1));
			falling.setDropItem(false);
		b.setType(Material.AIR);
		Firework(loc);
	}
}
for (Entity e : p.getNearbyEntities(3, 3, 3)){
	if (e instanceof LivingEntity){
		e.setVelocity(e.getVelocity().setY(1.3));
	}
}
p.setVelocity(p.getVelocity().setY(1.3));

	}
	}
	
	@SuppressWarnings("deprecation")
	public static void executeSpecial(Location loc){
		LivingEntity e = loc.getWorld().spawnCreature(loc, EntityType.SHEEP);
		Sheep sheep = (Sheep) e;
		e.setCustomName("§4§lBOMBER SHEEP!");
		e.setCustomNameVisible(true);
		sheep.setSheared(true);
		Main.SheepBlowup.put(sheep, 15);
	}
	
	public static void giveItems(Player p){
		ItemStack stick = new ItemStack(Material.STICK);
		ItemMeta stickm = stick.getItemMeta();
		stickm.addEnchant(Enchantment.KNOCKBACK, 1, false);
		stickm.setDisplayName("§bStick of Knockback");
		List<String> lore = new ArrayList<String>();
		lore.add("§eUse this stick to knock");
		lore.add("§epeople off the map, remember");
		lore.add("§ethat you have a §a%3 §echance");
		lore.add("§eof doing double knockback.");
		stickm.setLore(lore);
		stick.setItemMeta(stickm);
		p.getInventory().addItem(stick);
		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		ItemMeta swordm = sword.getItemMeta();
		swordm.setDisplayName("§bBombers Skill");
		lore.clear();
		lore.add("§eRight click the ground");
		lore.add("§eto make a small explosion");
		lore.add("§eat your feet that will break");
		lore.add("§ea few blocks and launch up");
		lore.add("§enearby players!");
		swordm.setLore(lore);
		sword.setItemMeta(swordm);
		p.getInventory().addItem(sword);
		ItemStack egg = new ItemStack(Material.EGG, 2);
		ItemMeta eggm = egg.getItemMeta();
		eggm.setDisplayName("§bBombers Special Skill");
		lore.clear();
		lore.add("§eThrow an egg to spawn a");
		lore.add("§aBomber Sheep §ethat will blink");
		lore.add("§ered and then explode launching");
		lore.add("§eblocks everywhere.");
		eggm.setLore(lore);
		egg.setItemMeta(eggm);
		p.getInventory().addItem(egg);
		ItemStack hat = (new ItemStack(Material.LEATHER_HELMET));
		LeatherArmorMeta hatm = (LeatherArmorMeta) hat.getItemMeta();
		hatm.setColor(Color.RED);
		hat.setItemMeta(hatm);
		p.getInventory().setHelmet(hat);
		p.updateInventory();
	}
	
	public static void Firework(Location loc){
		Firework fw = (Firework) loc.getWorld().spawn(loc, Firework.class);
		FireworkEffect effect = FireworkEffect.builder().trail(true).flicker(false).withColor(Color.ORANGE, Color.BLACK).with(Type.STAR).build();
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

}
