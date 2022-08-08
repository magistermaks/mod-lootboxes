package net.darktree.lootboxes.api;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public interface LootGenerator {

	void generate(List<ItemStack> stacks, World world, BlockPos pos, Random random, @Nullable Entity entity);

}
