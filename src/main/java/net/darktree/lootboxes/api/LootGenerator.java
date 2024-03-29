package net.darktree.lootboxes.api;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@FunctionalInterface
public interface LootGenerator {

	/**
	 * This method is called for every drop entry for a given loot box.
	 * It can get called multiple times if after the iteration
	 * of all the entries no item was generated by any of them.
	 *
	 * @param stacks the array of stack to drop
	 * @param world the world the loot box is in
	 * @param pos the position of the loot box block
	 * @param random a Random instance, acquired from the world
	 * @param entity the entity that broke the urn (nullable)
	 * @param moved indicates if the urn was picked up and moved by the player using Silk Touch
	 */
	void generate(List<ItemStack> stacks, World world, BlockPos pos, Random random, @Nullable Entity entity, boolean moved);

}
