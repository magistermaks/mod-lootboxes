package net.darktree.lootboxes.impl.loot;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.darktree.lootboxes.LootBoxes;
import net.darktree.lootboxes.api.LootBoxType;
import net.darktree.lootboxes.api.LootGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class LootEntry implements LootGenerator {

	public final Type type;
	public final LootBoxType[] targets;
	public final ItemStack stack;
	public final float chance;
	public final Identifier id;

	public LootEntry(Type type, LootBoxType[] targets, ItemStack stack, float chance, Identifier id) {
		this.type = type;
		this.targets = targets;
		this.stack = stack;
		this.chance = chance;
		this.id = id;
	}

	public void generate(List<ItemStack> stacks, World world, BlockPos pos, Random random, @Nullable Entity entity) {
		if (random.nextFloat() < this.chance) {
			stacks.add(stack);
		}
	}

	public static class Json {

		public Type type;
		public LootBoxType[] targets;
		public String item;
		public float chance;
		public String nbt;

		public LootEntry build(Identifier id) {
			ItemStack stack = new ItemStack(Registry.ITEM.get(new Identifier(item)));

			if (nbt != null) {
				try {
					stack.setNbt(StringNbtReader.parse(nbt));
				} catch (CommandSyntaxException exception) {
					LootBoxes.LOGGER.warn("Invalid NBT tag supplied in loot box drop entry '" + id + "'!", exception);
				}
			}

			if (stack.isEmpty()) {
				LootBoxes.LOGGER.warn("Empty stack used in loot box drop entry '" + id + "'!");
			}

			return new LootEntry(type, targets, stack, chance / 100f, id);
		}

	}

	public enum Type {
		TREASURE,
		COMMON,
		TRASH
	}

}
