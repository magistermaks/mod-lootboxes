package net.darktree.lootboxes.impl.loot;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.darktree.lootboxes.LootBoxes;
import net.darktree.lootboxes.api.LootBoxType;
import net.darktree.lootboxes.api.LootGenerator;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LootResourceLoader implements SimpleSynchronousResourceReloadListener {

	private static final Gson GSON = new Gson();
	private static final Map<LootBoxType, Map<Identifier, LootGenerator>> LOOT = Stream.of(LootBoxType.values()).collect(Collectors.toMap(Function.identity(), type -> new HashMap<>()));

	public static final List<LootGeneratorSupplier> SUPPLIERS = new ArrayList<>();
	
	@Override
	public void reload(ResourceManager manager) {
		for (Map<Identifier, LootGenerator> map : LOOT.values()) {
			map.clear();
		}

		for (LootGeneratorSupplier supplier : SUPPLIERS) {
			LOOT.get(supplier.type).put(new Identifier(LootBoxes.NAMESPACE, "drops/" + supplier.name), supplier.generator);
		}

		for(Identifier id : manager.findResources("drops", path -> path.endsWith(".json"))) {
			if (id.getNamespace().equals(LootBoxes.NAMESPACE)) {
				try(InputStream stream = manager.getResource(id).getInputStream()) {
					apply(id, new InputStreamReader(stream, StandardCharsets.UTF_8));
				} catch(Exception e) {
					LootBoxes.LOGGER.error("Error occurred while loading resource json: " + id, e);
				}
			}
		}
	}

	public void apply(Identifier identifier, Reader reader) {
		JsonParser jsonParser = new JsonParser();
		JsonElement element = jsonParser.parse(reader);

		// check if the entry is empty (for removal)
		if (element.getAsJsonObject().size() == 0) {
			for (Map<Identifier, LootGenerator> generators : LOOT.values()) {
				generators.remove(identifier);
			}
		} else {
			LootEntry entry = GSON.fromJson(element, LootEntry.Json.class).build(identifier);

			for (LootBoxType type : entry.targets) {
				LOOT.get(type).put(identifier, entry);
			}
		}
	}

	@Override
	public Identifier getFabricId() {
		return LootBoxes.id("lootbox");
	}

	public Collection<LootGenerator> getEntries(LootBoxType type) {
		return LOOT.get(type).values();
	}

}
