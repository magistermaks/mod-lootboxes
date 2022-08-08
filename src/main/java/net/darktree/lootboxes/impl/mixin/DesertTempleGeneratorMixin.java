package net.darktree.lootboxes.impl.mixin;

import net.darktree.lootboxes.LootBoxes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.DesertTempleGenerator;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(DesertTempleGenerator.class)
public abstract class DesertTempleGeneratorMixin extends StructurePiece {

	protected DesertTempleGeneratorMixin(StructurePieceType type, int length, BlockBox boundingBox) {
			super(type, length, boundingBox);
		}

		@Inject(method="generate", at=@At("TAIL"))
		private void lootboxes_generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox box, ChunkPos chunkPos, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
			if (LootBoxes.SETTINGS.add_to_desert_temples) {
				final double chance = LootBoxes.SETTINGS.desert_temple_spawn_chance / 100.0;
				final BlockState urn = LootBoxes.URN_BLOCK.getDefaultState();
				final int offset = 3;

				randomlyPlace(world, urn, 10 - offset, 5, 10 - offset, chance, box, random);
				randomlyPlace(world, urn, 10 + offset, 5, 10 + offset, chance, box, random);
				randomlyPlace(world, urn, 10 - offset, 5, 10 + offset, chance, box, random);
				randomlyPlace(world, urn, 10 + offset, 5, 10 - offset, chance, box, random);
			}
		}

		@Unique
		private void randomlyPlace(StructureWorldAccess world, BlockState state, int x, int y, int z, double chance, BlockBox box, Random random) {
			if (random.nextFloat() < chance) {
				addBlock(world, state, x, y, z, box);
			}
		}

}
