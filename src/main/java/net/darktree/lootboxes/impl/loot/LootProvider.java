package net.darktree.lootboxes.impl.loot;

import net.darktree.lootboxes.LootBoxes;
import net.darktree.lootboxes.api.LootGenerator;
import net.darktree.lootboxes.impl.block.LootBoxBlock;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LootProvider {

	private static final LootResourceLoader LOADER = new LootResourceLoader();

	public static List<ItemStack> get(LootBoxBlock box, ItemStack tool, World world, BlockPos pos, @Nullable Entity entity) {
		if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, tool) > 0) {
			return Collections.singletonList(new ItemStack(box.asItem()));
		}

		List<ItemStack> stacks = new ArrayList<>();
		int attempts = 0;

		while (stacks.isEmpty()) {
			if (attempts > 16) {
				LootBoxes.LOGGER.error("Loot generation for target: '{}' took more than 16 attempts and was aborted!", box.type);
				return Collections.emptyList();
			}

			for (LootGenerator entry : LOADER.getEntries(box.type)) {
				entry.generate(stacks, world, pos, world.getRandom(), entity);
			}

			attempts ++;
		}

		if (attempts > 4) {
			LootBoxes.LOGGER.warn("Loot generation for target: '{}' took more than 4 attempts.", box.type);
		}

		return stacks;
	}

	public static void init() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(LOADER);
	}

}
