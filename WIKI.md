## Config
Loot Boxes by default generate Urns in some vanilla structures (Jungle Temples, Desert Temples and Mineshaft Corridors)
you can disable that (or tweak the chance) using the config file `loot_boxes.properites`

The list of available options:
```properties
# Boolean, controls whether loot boxes should drop experience when broken
drop_experience=true/false

# Boolean, controls whether urns should generate in jungle temples
add_to_jungle_temples=true/false

# Number in range 0-100, controls the chance of individual urns generating in the jungle temple
jungle_temple_spawn_chance=0/100

# Boolean, controls whether urns should generate in desert temples
add_to_desert_temples=true/false

# Number in range 0-100, controls the chance of individual urns generating in the desert temple
desert_temple_spawn_chance=0/100

# Boolean, controls whether urns should generate in mineshaft corridors
add_to_mineshaft=true/false

# Number in range 0-100, controls the chance of individual urns generating in the mineshaft corridors
mineshaft_spawn_chance=0/100
```

## JSON API
The JSON Datapack system can be used to add (and remove) drops from the Loot Boxes blocks.
If you are a mod developer, a modpack maker or just someone who like to tinker with the game you can use this system.
You can see the entries added using this system by this mod [here](src/main/resources/data/loot_boxes/drops/).

#### Adding new items to the Loot Box
Under `data/loot_boxes/drops` create a new json file, for example: `wheat.json`,
inside that file place this code (Excluding the comments!)

```josn5
{
    // type of this drop, must be one of "TRASH", "COMMON", "TREASURE"
    "type": "TRASH",
    
    // the types of loot boxes to which this drop applies, for now only "URN" exists
    "targets": ["URN"],
    
    // the item ID to drop
    "item": "minecraft:wheat",
    
    // the chance (procentage) that this item will get dropped, from 0 to 100
    "chance": 10.0
    
    // optional, custom NBT data to add to the item
    "nbt": "{display:{Lore:['[{\"text\":\"Custom NBT tag!\",\"italic\":false}]']}}"
    
    // optional, if set to true this entry will not generate for urns placed by players in survival mode
    "untouched": false
}
```

Now there should be a 10% chance wheat would drop when breaking Urns.  
**Note**: If you create a drop entry with the same name as one of the already existing ones, it will replace it

#### Removing a drop entirely
If you want to remove one of the drops (for example `data/loot_boxes/drops/diamonds.json`) 
create a file with the same name and put an empty JSON object inside (`{}`), 
when you do that the old entry will get removed. This also works for entries added with the Java API.

## Java API
Sometimes when you need additional control over what items are dropped, for example 
if you want to drop some item only in some biomes or at some Y-levels a JSON file will not suffice, 
for that exact reason exists the Java API.

To be able to use it you will first need to add Loot Boxes to your `build.gradle`:
```gradle
repositories {
    maven {
        allowInsecureProtocol = true
        url 'http://maven.darktree.net'
    }
}

dependencies {
    modImplementation "net.darktree:lootboxes:0.1.2"
    // include "net.darktree:lootboxes:0.1.2" // optional, bundle Loot Boxes inside of your mod
}
```

#### Adding drops using the Java API
When using the Java API you directly provide an `ItemStack` generator, so you have to decide yourself
when to add your item to the output array (you can also add multiple).

```java
LootBoxes.register(LootBoxType.URN, "modid_custom_generator", (stacks, world, pos, random, entity, moved) -> {
	if (random.nextBoolean()) { // 50% spawn chance
		stacks.add(new ItemStack(Items.DIRT));
	}
});
```

The `move` property can be used to detect if the loot box was picked up by a player
with Silk Touch and placed somewhere else.

**Note**: Generators defined using the Java API can still be overwritten from the datapack 
by creating a drops entry with the same name as the generator.

#### Adding Loot Boxes to XYZ
If you want to (and I highly encourage it!) add those beautiful blocks to you mod you can access them 
from the `LootBoxes` class (e.g. `LootBoxes.URN_BLOCK`). If you don't want to depend on the lootboxes mod directly
(or embed it - yes, I'm fine with that) you can access the blocks from the block registry (`Registry.BLOCK.get(new Identifier("loot_boxes", "urn"))`)
just make sure not to do it in the initializer but when you need it (also make sure that Loot Boxes are actually loaded -
`FabricLoader.getInstance().isModLoaded("loot_boxes")`)

## Notes
- What if for some storage reason I hate life and would prefer to use the vanilla loot table instead? 
You can nuke all of this by providing a vanilla loot table for the `loot_boxes:urn` block. But please don't
do it.
- Why make such a (relatively speaking) complex mod to add a single block? I do plan to add more loot box types,
this was made in a hurry as I needed to split urns off another mod that was nearing its release - Stylish Occult.
