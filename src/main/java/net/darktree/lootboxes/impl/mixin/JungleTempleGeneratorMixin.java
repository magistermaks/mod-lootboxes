package net.darktree.lootboxes.impl.mixin;

import net.darktree.lootboxes.LootBoxes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.JungleTempleGenerator;
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

@Mixin(JungleTempleGenerator.class)
public abstract class JungleTempleGeneratorMixin extends StructurePiece {

	protected JungleTempleGeneratorMixin(StructurePieceType type, int length, BlockBox boundingBox) {
		super(type, length, boundingBox);
	}

	@Inject(method="generate", at=@At("TAIL"))
	private void lootboxes_generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (LootBoxes.SETTINGS.add_to_jungle_temples) {
			final double chance = LootBoxes.SETTINGS.jungle_temple_spawn_chance / 100.0;
			final BlockState urn = LootBoxes.URN_BLOCK.getDefaultState();

			// top floor front
			randomlyPlace(world, urn, 2, 4, 2, chance, boundingBox, random);
			randomlyPlace(world, urn, 9, 4, 2, chance, boundingBox, random);

			// top floor back
			randomlyPlace(world, urn, 9, 4, 12, chance, boundingBox, random);
			randomlyPlace(world, urn, 2, 4, 12, chance, boundingBox, random);

			// bottom floor back
			randomlyPlace(world, urn, 8, 1, 11, chance, boundingBox, random);
			randomlyPlace(world, urn, 3, 1, 11, chance, boundingBox, random);
		}
	}

	@Unique
	private void randomlyPlace(StructureWorldAccess world, BlockState state, int x, int y, int z, double chance, BlockBox box, Random random) {
		if (random.nextFloat() < chance) {
			addBlock(world, state, x, y, z, box);
		}
	}

}
