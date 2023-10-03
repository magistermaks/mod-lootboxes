package net.darktree.lootboxes.impl;

import net.darktree.lootboxes.LootBoxes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class Sounds {

	public static final BlockSoundGroup URN = new BlockSoundGroup(0.85f, 0.90f,
			register("urn_broken"),
			SoundEvents.BLOCK_STONE_STEP,
			SoundEvents.BLOCK_STONE_PLACE,
			SoundEvents.BLOCK_STONE_HIT,
			SoundEvents.BLOCK_STONE_FALL
	);

	private static SoundEvent register(String name) {
		Identifier id = LootBoxes.id(name);
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
	}

}
