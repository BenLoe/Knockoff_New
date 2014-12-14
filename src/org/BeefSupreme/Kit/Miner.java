package org.BeefSupreme.Kit;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.BeefSupreme.Main.Files;
import org.BeefSupreme.Main.Main;
import org.BeefSupreme.Main.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class Miner {
	
	public static void sendInfo(Player p){
		p.sendMessage("    ");
		p.sendMessage("    ");
		p.sendMessage("    ");
		p.sendMessage("    ");
		p.sendMessage("    ");
		p.sendMessage("    ");
		p.sendMessage(ChatColor.GREEN + "--------------[ " + ChatColor.AQUA + "Miner" + ChatColor.GREEN + " ]--------------");
		p.sendMessage("    ");
		p.sendMessage(ChatColor.BOLD + "Skill: " + ChatColor.YELLOW + "Breaks block you are pointing your sword at.");
		p.sendMessage("    ");
		p.sendMessage(ChatColor.BOLD + "Special Skill: " + ChatColor.YELLOW + "Makes a 3x3 area that breaks quickly.");
		p.sendMessage("    ");
		p.sendMessage(ChatColor.GREEN + "----------------------------------");
		Main.Kit.remove(p.getName());
		Main.Kit.put(p.getName(), "Miner");
			p.sendMessage(Main.tag + ChatColor.AQUA + "Kit set to: " + ChatColor.YELLOW + "Miner");
	}
	@SuppressWarnings("deprecation")
	public static void executeBasic(Player p){
		if (p.getExp() < 0.20F){
			p.sendMessage(Main.tag + ChatColor.YELLOW + "You need more energy" + ChatColor.GRAY + " (Xp Bar)");
		}else{
	for (Block b : p.getLineOfSight(null, 20)){
		if (!b.getType().equals(Material.AIR)){
			p.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
			ParticleEffect.displayBlockCrack(b.getLocation(), b.getTypeId(), b.getData(), 0.2f, 0.2f, 0.2f, 80);
			b.setType(Material.AIR);
				p.setExp(p.getExp() - 0.20f);
			break;
		}
	}
}
	}
	
	@SuppressWarnings("deprecation")
	public static void executeSpecial(Location loc){
		int bX = loc.getBlockX();
		int bY = loc.getBlockY();
		int bZ = loc.getBlockZ();
		for (int x = bX - 2; x <= bX + 2; x++){
			for (int y = bY - 2; y <= bY + 2; y++){
				for (int z = bZ - 2; z <= bZ + 2; z++){
					double distance = ((bX-x)*(bX-x) + ((bZ-z)*(bZ-z)) + ((bY-y)*(bY-y)));
					if (distance < 2*2){
						Location loc1 = new Location(loc.getWorld(), x, y, z);
						Block b = loc1.getBlock();
						if (b.getType() != Material.AIR){
							b.setType(Material.STAINED_CLAY);
							b.setData((byte)4);
							Main.Changing.add(loc1);
						}
					}
				}
			}
		}
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
		swordm.setDisplayName("§bMiners Skill");
		lore.clear();
		lore.add("§eAim at a block and");
		lore.add("§eright click to mine");
		lore.add("§ethe block.");
		swordm.setLore(lore);
		sword.setItemMeta(swordm);
		p.getInventory().addItem(sword);
		ItemStack egg = new ItemStack(Material.EGG, 2);
		ItemMeta eggm = egg.getItemMeta();
		eggm.setDisplayName("§bMiners Special Skill");
		lore.clear();
		lore.add("§eThrow this egg to make");
		lore.add("§ea 3x3 square that will randomly");
		lore.add("§ebe mined slowely.");
		eggm.setLore(lore);
		egg.setItemMeta(eggm);
		p.getInventory().addItem(egg);
		ItemStack hat = (new ItemStack(Material.LEATHER_HELMET));
		LeatherArmorMeta hatm = (LeatherArmorMeta) hat.getItemMeta();
		hatm.setColor(Color.WHITE);
		hat.setItemMeta(hatm);
		p.getInventory().setHelmet(hat);
		p.updateInventory();
	}
	public static void Firework(Location loc){
		Firework fw = (Firework) loc.getWorld().spawn(loc, Firework.class);
		FireworkEffect effect = FireworkEffect.builder().trail(true).flicker(false).withColor(Color.NAVY, Color.GRAY).with(Type.BURST).build();
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
