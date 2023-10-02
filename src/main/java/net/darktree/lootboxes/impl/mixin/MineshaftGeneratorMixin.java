package net.darktree.lootboxes.impl.mixin;

import net.darktree.lootboxes.LootBoxes;
import net.minecraft.block.BlockState;
import net.minecraft.structure.MineshaftGenerator;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MineshaftGenerator.MineshaftCorridor.class)
public abstract class MineshaftGeneratorMixin extends StructurePiece {

	@Shadow @Final private int length;

	protected MineshaftGeneratorMixin(StructurePieceType type, int length, BlockBox boundingBox) {
		super(type, length, boundingBox);
	}

	@Inject(method="generate", at=@At("TAIL"))
	private void lootboxes_generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox box, ChunkPos chunkPos, BlockPos pivot, CallbackInfo ci) {
		if (LootBoxes.SETTINGS.add_to_mineshaft) {
			final double chance = LootBoxes.SETTINGS.mineshaft_spawn_chance / 100.0;
			final BlockState urn = LootBoxes.URN_BLOCK.getDefaultState();

			for (int z = 1; z <= this.length * 5 - 1; z += 6) {
				int x = random.nextBoolean() ? 0 : 2;

				if (this.getBlockAt(world, x, 0, z, box).isAir() && !getBlockAt(world, x, -1, z, box).isAir()) {
					randomlyPlace(world, urn, x, 0, z, chance, box, random);
				}
			}
		}
	}

	@Unique
	private void randomlyPlace(StructureWorldAccess world, BlockState state, int x, int y, int z, double chance, BlockBox box, Random random) {
		if (random.nextFloat() < chance) {
			addBlock(world, state, x, y, z, box);
		}
	}

}
