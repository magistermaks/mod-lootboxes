package net.darktree.lootboxes.impl.loot;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.darktree.lootboxes.LootBoxes;
import net.darktree.lootboxes.api.LootBoxType;
import net.darktree.lootboxes.api.LootGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LootEntry implements LootGenerator {

	public final LootBoxType[] targets;

	private final Type type;
	private final ItemStack stack;
	private final float chance;
	private final boolean untouched;

	public LootEntry(Type type, LootBoxType[] targets, ItemStack stack, float chance, boolean untouched) {
		this.type = type;
		this.targets = targets;
		this.stack = stack;
		this.chance = chance;
		this.untouched = untouched;
	}

	public void generate(List<ItemStack> stacks, World world, BlockPos pos, Random random, @Nullable Entity entity, boolean moved) {
		float luck = 0;

		if (this.untouched && moved) {
			return;
		}

		if (entity instanceof PlayerEntity player) {
			luck += player.hasStatusEffect(StatusEffects.LUCK) ? 1 : 0;
			luck -= player.hasStatusEffect(StatusEffects.UNLUCK) ? 1 : 0;
		}

		if (random.nextFloat() < getChance(luck)) {
			stacks.add(stack.copy());
		}
	}

	private float getChance(float luck) {
		return this.chance * switch (this.type) {
			case TRASH -> (1 - luck * 0.5f);
			case COMMON -> 1;
			case TREASURE -> (1 + luck * 0.5f);
		};
	}

	public static class Json {

		public Type type;
		public LootBoxType[] targets;
		public String item;
		public float chance;
		public String nbt = null;
		public boolean untouched = false;

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

			return new LootEntry(type, targets, stack, chance / 100f, untouched);
		}

	}

	public enum Type {
		TREASURE,
		COMMON,
		TRASH
	}

}
