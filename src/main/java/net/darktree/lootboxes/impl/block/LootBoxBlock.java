package net.darktree.lootboxes.impl.block;

import net.darktree.interference.api.DefaultLoot;
import net.darktree.lootboxes.LootBoxes;
import net.darktree.lootboxes.api.LootBoxType;
import net.darktree.lootboxes.impl.loot.LootProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.List;

public abstract class LootBoxBlock extends Block implements DefaultLoot, Waterloggable {

	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final BooleanProperty MOVED = BooleanProperty.of("moved");

	public final LootBoxType type;

	public LootBoxBlock(Settings settings, LootBoxType type) {
		super(settings);
		this.type = type;

		setDefaultState(getDefaultState().with(WATERLOGGED, false).with(MOVED, false));
	}

	public void dropExperience(ServerWorld world, BlockPos pos) {
		if (LootBoxes.SETTINGS.drop_experience) {
			dropExperience(world, pos, MathHelper.nextBetween(world.getRandom(), 2, 10));
		}
	}

	protected int getStackCount() {
		return 1;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
		builder.add(MOVED);
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
		BlockPos pos = new BlockPos(builder.get(LootContextParameters.ORIGIN));

		return LootProvider.get(this, tool, world, pos, entity, state.get(MOVED), getStackCount());
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		if (state.get(WATERLOGGED)) {
			return Fluids.WATER.getStill(false);
		}

		return super.getFluidState(state);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluid = ctx.getWorld().getFluidState(ctx.getBlockPos());
		PlayerEntity player = ctx.getPlayer();
		boolean moved = player == null || !player.isCreative();

		return getDefaultState().with(MOVED, moved).with(WATERLOGGED, fluid.getFluid() == Fluids.WATER);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) {
			world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}

}
