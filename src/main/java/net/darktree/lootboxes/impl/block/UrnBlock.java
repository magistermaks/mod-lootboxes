package net.darktree.lootboxes.impl.block;

import net.darktree.interference.Voxels;
import net.darktree.lootboxes.api.LootBoxType;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class UrnBlock extends LootBoxBlock implements Waterloggable {

	private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	private static final VoxelShape COLLISION_SHAPE = Voxels.box(3, 0, 3, 13, 13, 13).build();
	private static final VoxelShape OUTLINE_SHAPE = Voxels.box(4, 0, 4, 12, 1, 12).box(3, 1, 3, 13, 10, 13).box(5, 10, 5, 11, 12, 11).box(4, 12, 4, 12, 13, 12).build();

	public UrnBlock(AbstractBlock.Settings settings) {
		super(settings, LootBoxType.URN);
		setDefaultState(getDefaultState().with(WATERLOGGED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return COLLISION_SHAPE;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return OUTLINE_SHAPE;
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
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		BlockState state = super.getPlacementState(ctx);

		return state != null ? state.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER) : null;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) {
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

}
