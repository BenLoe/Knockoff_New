package org.BeefSupreme.Main;

import org.bukkit.configuration.file.FileConfiguration;

import BSLib.BeefSupreme;

public class Files{
	public static BeefSupreme bs = BeefSupreme.getPlugin(BeefSupreme.class);
		public static Main plugin;
		public Files(Main instance){
			plugin = instance;
		}
		
		public static FileConfiguration config(){
			return plugin.getConfig();
		}
		
		public static void saveConfig(){
			plugin.saveConfig();
		}
		
	}