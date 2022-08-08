package net.darktree.lootboxes.impl.loot;

import net.darktree.lootboxes.api.LootBoxType;
import net.darktree.lootboxes.api.LootGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class LootEntry implements LootGenerator {

	public final Type type;
	public final LootBoxType[] targets;
	public final Item item;
	public final float chance;
	public final Identifier id;

	public LootEntry(Type type, LootBoxType[] targets, Item item, float chance, Identifier id) {
		this.type = type;
		this.targets = targets;
		this.item = item;
		this.chance = chance;
		this.id = id;
	}

	public void generate(List<ItemStack> stacks, World world, Random random, @Nullable Entity entity) {
		if (random.nextFloat() < this.chance) {
			stacks.add(new ItemStack(item));
		}
	}

	public static class Json {

		public Type type;
		public LootBoxType[] targets;
		public String item;
		public float chance;

		public LootEntry build(Identifier id) {
			return new LootEntry(type, targets, Registry.ITEM.get(new Identifier(item)), chance / 100f, id);
		}

	}

	public enum Type {
		TREASURE,
		COMMON,
		TRASH
	}

}
