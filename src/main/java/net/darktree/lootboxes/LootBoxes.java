package net.darktree.lootboxes;

import net.darktree.lootboxes.api.LootBoxType;
import net.darktree.lootboxes.api.LootGenerator;
import net.darktree.lootboxes.impl.Sounds;
import net.darktree.lootboxes.impl.block.UrnBlock;
import net.darktree.lootboxes.impl.config.Settings;
import net.darktree.lootboxes.impl.loot.LootGeneratorSupplier;
import net.darktree.lootboxes.impl.loot.LootProvider;
import net.darktree.lootboxes.impl.loot.LootResourceLoader;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LootBoxes implements ModInitializer {

	public static final Settings SETTINGS = new Settings();
	public static final Logger LOGGER = LogManager.getLogger("Loot Boxes");
	public static final String NAMESPACE = "loot_boxes";

	public static final Block URN_BLOCK = new UrnBlock(FabricBlockSettings.of(Material.GLASS).mapColor(DyeColor.BROWN).strength(0.1f, 6.0f).sounds(Sounds.URN));
	public static final Item URN_ITEM = new BlockItem(URN_BLOCK, new Item.Settings().rarity(Rarity.UNCOMMON));

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, id("urn"), URN_BLOCK);
		Registry.register(Registries.ITEM, id("urn"), URN_ITEM);

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
			content.add(URN_ITEM);
		});

		LootProvider.init();
	}

	public static Identifier id(String path) {
		return new Identifier(NAMESPACE, path);
	}

	/**
	 * Used to add loot generators from code
	 * without using JSON files
	 *
	 * @param target the loot box type (for now there is only {@link LootBoxType#URN})
	 * @param name the name of this generator so that data packs can override it
	 * @param generator the generator itself
	 */
	public static void register(LootBoxType target, String name, LootGenerator generator) {
		LootResourceLoader.SUPPLIERS.add(new LootGeneratorSupplier(target, name, generator));
	}

}
