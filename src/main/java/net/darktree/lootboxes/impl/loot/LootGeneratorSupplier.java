package net.darktree.lootboxes.impl.loot;

import net.darktree.lootboxes.api.LootBoxType;
import net.darktree.lootboxes.api.LootGenerator;

public class LootGeneratorSupplier {

	public final LootBoxType type;
	public final String name;
	public final LootGenerator generator;

	public LootGeneratorSupplier(LootBoxType type, String name, LootGenerator generator) {
		this.type = type;
		this.name = name;
		this.generator = generator;
	}

}
