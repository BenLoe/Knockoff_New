package org.BeefSupreme.Kit;

import java.util.ArrayList;
import java.util.List;

import org.BeefSupreme.Main.Main;
import org.BeefSupreme.Main.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

public class Hulk {

	public static void sendInfo(Player p){
		p.sendMessage("    ");
		p.sendMessage("    ");
		p.sendMessage("    ");
		p.sendMessage("    ");
		p.sendMessage("    ");
		p.sendMessage("    ");
		p.sendMessage(ChatColor.GREEN + "--------------[ " + ChatColor.AQUA + "Hulk" + ChatColor.GREEN + " ]--------------");
		p.sendMessage("    ");
		p.sendMessage(ChatColor.BOLD + "Skill: " + ChatColor.YELLOW + "Throws you in the air and when you land you smash the blocks around you.");
		p.sendMessage("    ");
		p.sendMessage(ChatColor.BOLD + "Special Skill: " + ChatColor.YELLOW + "Smashes a mountain of blocks throwing players around.");
		p.sendMessage("    ");
		p.sendMessage(ChatColor.GREEN + "----------------------------------");
		if (p.hasPermission("knock.Hulk")){
		Main.Kit.remove(p.getName());
		Main.Kit.put(p.getName(), "Hulk");
			p.sendMessage(Main.tag + ChatColor.AQUA + "Kit set to: " + ChatColor.YELLOW + "Hulk");
		}else{
			p.sendMessage(Main.tag + ChatColor.RED + "You must buy this kit in the shop.");
		}
	}
	
	public static void executeBasic(Player p){
		if (Main.Cooldown.contains(p.getName())){
			p.sendMessage(Main.tag + ChatColor.YELLOW + "Skill still in cooldown!");
			for (int i = 1; i > 20; i = i +1){
			Block b = p.getLocation().add(0, i, 0).getBlock();
			if (!b.getType().equals(Material.AIR)){
				p.sendMessage(Main.tag + ChatColor.YELLOW + "There is a block over your head, you cant do this skill!");
				return;
			}
			}
		}else{
			Main.Cooldown.add(p.getName());
			Main.HulkSmash.remove(p.getName());
			p.teleport(p.getLocation().add(0, 5, 0));
			p.setExp(1F);
			Main.HulkSmash.add(p.getName());
			p.setVelocity(p.getVelocity().setY(1.9));
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void executeBasic2(Player p){
		Main.HulkSmash.remove(p.getName());
		Location loc = p.getLocation().subtract(0, 1, 0);
		int bX = loc.getBlockX();
		int bY = loc.getBlockY();
		int bZ = loc.getBlockZ();
		for (int x = bX - 3; x <= bX + 3; x++){
			for (int y = bY - 3; y <= bY + 3; y++){
				for (int z = bZ - 3; z <= bZ + 3; z++){
					double distance = ((bX-x)*(bX-x) + ((bZ-z)*(bZ-z)) + ((bY-y)*(bY-y)));
					if (distance < 3*3){
						Location loc1 = new Location(loc.getWorld(), x, y, z);
						Block b = loc1.getBlock();
						if (loc1.getY() == loc.getY() && !(loc1.getBlockX() == bX && loc1.getBlockZ() == bZ)){
						loc1.getWorld().spawnFallingBlock(loc1.add(0, 1, 0), b.getType() , b.getData()).setVelocity(new Vector(0, 0.4, 0));
						ParticleEffect.displayBlockCrack(b.getLocation(), b.getTypeId(), b.getData(), 0.2f, 0.2f, 0.2f, 10);
						p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1f, 1f);
						b.setType(Material.AIR);
					}
				}
			}
		}
	}
		ParticleEffect.SLIME.display(loc.add(0, 5, 0), 15, 3, 0, 3, 0.5f, 100);
		ParticleEffect.SLIME.display(loc.add(0, 5, 0), 15, 3, 0, 3, 0.5f, 100);
		ParticleEffect.SLIME.display(loc.add(0, 5, 0), 15, 3, 0, 3, 0.5f, 100);
}
	
	@SuppressWarnings("deprecation")
	public static void executeSpecial(Location loc, Egg egg){
		World world = loc.getWorld();
		Location mid = loc.add(0, -1, 0);
		Location side1 = loc.add(1, -1, 0);
		Location side2 = loc.add(-1, -1, 1);
		Location side3 = loc.add(0, -1, 1);
		Location side4 = loc.add(1, -1, 1);
		Location side5 = loc.add(-1, -1, 0);
		Location side6 = loc.add(0, -1, -1);
		Location side7 = loc.add(-1, -1, -1);
		Location side8 = loc.add(1, -1, -1);
		Location out1 = loc.add(2, -1, 0);
		Location out12 = loc.add(2, -1, 1);
		Location out2 = loc.add(2, -1, -1);
		Location out3 = loc.add(0, -1, 2);
		Location out4 = loc.add(1, -1, 2);
		Location out5 = loc.add(-1, -1, 2);
		Location out6 = loc.add(-2, -1, 1);
		Location out7 = loc.add(-2, -1, 0);
		Location out8 = loc.add(-2, -1, -1);
		Location out9 = loc.add(1, -1, -2);
		Location out10 = loc.add(0, -1, -2);
		Location out11 = loc.add(-1, -1, -2);
		world.spawnFallingBlock(mid.add(0, 1, 0), mid.getBlock().getType(), mid.getBlock().getData()).setVelocity(new Vector(0, 1, 0));
		world.spawnFallingBlock(side1.add(0, 1, 0), side1.getBlock().getType(), side1.getBlock().getData()).setVelocity(new Vector(0, 0.7, 0));
		world.spawnFallingBlock(side2.add(0, 1, 0), side2.getBlock().getType(), side2.getBlock().getData()).setVelocity(new Vector(0, 0.7, 0));
		world.spawnFallingBlock(side3.add(0, 1, 0), side3.getBlock().getType(), side3.getBlock().getData()).setVelocity(new Vector(0, 0.7, 0));
		world.spawnFallingBlock(side4.add(0, 1, 0), side4.getBlock().getType(), side4.getBlock().getData()).setVelocity(new Vector(0, 0.7, 0));
		world.spawnFallingBlock(side5.add(0, 1, 0), side5.getBlock().getType(), side5.getBlock().getData()).setVelocity(new Vector(0, 0.7, 0));
		world.spawnFallingBlock(side6.add(0, 1, 0), side6.getBlock().getType(), side6.getBlock().getData()).setVelocity(new Vector(0, 0.7, 0));
		world.spawnFallingBlock(side7.add(0, 1, 0), side7.getBlock().getType(), side7.getBlock().getData()).setVelocity(new Vector(0, 0.7, 0));
		world.spawnFallingBlock(side8.add(0, 1, 0), side8.getBlock().getType(), side8.getBlock().getData()).setVelocity(new Vector(0, 0.7, 0));
		world.spawnFallingBlock(out1.add(0, 1, 0), out1.getBlock().getType(), out1.getBlock().getData()).setVelocity(new Vector(0, 0.5, 0));
		world.spawnFallingBlock(out2.add(0, 1, 0), out2.getBlock().getType(), out2.getBlock().getData()).setVelocity(new Vector(0, 0.5, 0));
		world.spawnFallingBlock(out3.add(0, 1, 0), out3.getBlock().getType(), out3.getBlock().getData()).setVelocity(new Vector(0, 0.5, 0));
		world.spawnFallingBlock(out4.add(0, 1, 0), out4.getBlock().getType(), out4.getBlock().getData()).setVelocity(new Vector(0, 0.5, 0));
		world.spawnFallingBlock(out5.add(0, 1, 0), out5.getBlock().getType(), out5.getBlock().getData()).setVelocity(new Vector(0, 0.5, 0));
		world.spawnFallingBlock(out6.add(0, 1, 0), out6.getBlock().getType(), out6.getBlock().getData()).setVelocity(new Vector(0, 0.5, 0));
		world.spawnFallingBlock(out7.add(0, 1, 0), out7.getBlock().getType(), out7.getBlock().getData()).setVelocity(new Vector(0, 0.5, 0));
		world.spawnFallingBlock(out8.add(0, 1, 0), out8.getBlock().getType(), out8.getBlock().getData()).setVelocity(new Vector(0, 0.5, 0));
		world.spawnFallingBlock(out9.add(0, 1, 0), out9.getBlock().getType(), out9.getBlock().getData()).setVelocity(new Vector(0, 0.5, 0));
		world.spawnFallingBlock(out10.add(0, 1, 0), out10.getBlock().getType(), out10.getBlock().getData()).setVelocity(new Vector(0, 0.5, 0));
		world.spawnFallingBlock(out11.add(0, 1, 0), out11.getBlock().getType(), out11.getBlock().getData()).setVelocity(new Vector(0, 0.5, 0));
		world.spawnFallingBlock(out12.add(0, 1, 0), out12.getBlock().getType(), out12.getBlock().getData()).setVelocity(new Vector(0, 0.5, 0));
		mid.getBlock().setType(Material.AIR);
		side1.getBlock().setType(Material.AIR);
		side2.getBlock().setType(Material.AIR);
		side3.getBlock().setType(Material.AIR);
		side4.getBlock().setType(Material.AIR);
		side5.getBlock().setType(Material.AIR);
		side6.getBlock().setType(Material.AIR);
		side7.getBlock().setType(Material.AIR);
		side8.getBlock().setType(Material.AIR);
		out1.getBlock().setType(Material.AIR);
		out2.getBlock().setType(Material.AIR);
		out3.getBlock().setType(Material.AIR);
		out4.getBlock().setType(Material.AIR);
		out5.getBlock().setType(Material.AIR);
		out6.getBlock().setType(Material.AIR);
		out7.getBlock().setType(Material.AIR);
		out8.getBlock().setType(Material.AIR);
		out9.getBlock().setType(Material.AIR);
		out10.getBlock().setType(Material.AIR);
		out11.getBlock().setType(Material.AIR);
		out12.getBlock().setType(Material.AIR);
		for (Entity e : egg.getNearbyEntities(4.0, 4.0, 4.0)){
			if (e.getType().equals(EntityType.PLAYER)){
				e.setVelocity(e.getVelocity().setY(1.5));
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
		swordm.setDisplayName("§bHulk's Skill");
		lore.clear();
		lore.add("§eRight click to be thrown");
		lore.add("§eup into air and to");
		lore.add("§esmash down.");
		swordm.setLore(lore);
		sword.setItemMeta(swordm);
		p.getInventory().addItem(sword);
		ItemStack egg = new ItemStack(Material.EGG, 2);
		ItemMeta eggm = egg.getItemMeta();
		eggm.setDisplayName("§bHulk's Special Skill");
		lore.clear();
		lore.add("§eThrow this egg to make");
		lore.add("§ea mountain of blocks fly");
		lore.add("§eup throwing players around.");
		eggm.setLore(lore);
		egg.setItemMeta(eggm);
		p.getInventory().addItem(egg);
		ItemStack hat = (new ItemStack(Material.LEATHER_HELMET));
		LeatherArmorMeta hatm = (LeatherArmorMeta) hat.getItemMeta();
		hatm.setColor(Color.LIME);
		hat.setItemMeta(hatm);
		p.getInventory().setHelmet(hat);
		p.updateInventory();
	}
}
