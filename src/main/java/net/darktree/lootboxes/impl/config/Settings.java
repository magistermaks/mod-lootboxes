package net.darktree.lootboxes.impl.config;

import net.darktree.lootboxes.LootBoxes;

public class Settings {

	private final SimpleConfig CONFIG = SimpleConfig.of(LootBoxes.NAMESPACE).provider((name) ->
		"# See the list of available config options on the mod's github page"
	).request();

	// Jungle Temple
	public final boolean add_to_jungle_temples = CONFIG.getOrDefault("add_to_jungle_temples", true);
	public final double jungle_temple_spawn_chance = CONFIG.getOrDefault("jungle_temple_spawn_chance", 40.0);

	// Desert Temple
	public final boolean add_to_desert_temples = CONFIG.getOrDefault("add_to_jungle_temples", true);
	public final double desert_temple_spawn_chance = CONFIG.getOrDefault("jungle_temple_spawn_chance", 80.0);

}
