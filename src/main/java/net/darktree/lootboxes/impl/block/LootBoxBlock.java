package net.darktree.lootboxes.impl.block;

import net.darktree.interference.api.DefaultLoot;
import net.darktree.lootboxes.api.LootBoxType;
import net.darktree.lootboxes.impl.loot.LootProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public abstract class LootBoxBlock extends Block implements DefaultLoot {

	public final LootBoxType type;

	public LootBoxBlock(Settings settings, LootBoxType type) {
		super(settings);
		this.type = type;
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

	@Override
	public List<ItemStack> getDefaultStacks(BlockState state, LootContext.Builder builder, Identifier identifier, LootContext lootContext, ServerWorld serverWorld, LootTable lootTable) {
		ItemStack tool = builder.get(LootContextParameters.TOOL);
		Entity entity = builder.getNullable(LootContextParameters.THIS_ENTITY);
		World world = builder.getWorld();

		if (entity instanceof PlayerEntity player && player.isCreative()) {
			return Collections.emptyList();
		}

		return LootProvider.get(this, tool, world, entity);
	}

}
