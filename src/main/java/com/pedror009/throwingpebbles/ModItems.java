package com.pedror009.throwingpebbles;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final String ModID = "throwing_pebbles";

    public static final Identifier PacketID = new Identifier(ModID, "spawn_packet");

    public static final EntityType<PebbleEntity> PebbleEntityType = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ModID, "pebbles_entity"),
            FabricEntityTypeBuilder.<PebbleEntity>create(SpawnGroup.MISC, PebbleEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the projectile
                    .trackRangeBlocks(4).trackedUpdateRate(10) // necessary for all thrown projectiles (as it prevents it from breaking, lol)
                    .build() // VERY IMPORTANT DONT DELETE FOR THE LOVE OF GOD PSLSSSSSS
    );

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
            new Identifier(ModID, "pebble_mod_item_group"), () -> new ItemStack(ModItems.PEBBLE));


    //ItemGroup.MISC
    public static final Item PEBBLE = new Pebble(1, new FabricItemSettings().group(ModItems.ITEM_GROUP));
    public static final Item PEBBLE_MAGMA = new Pebble(2, new FabricItemSettings().group(ModItems.ITEM_GROUP));
    public static final Item PEBBLE_FISH = new Pebble(3, new FabricItemSettings().group(ModItems.ITEM_GROUP));
    public static final ToolItem SLINGSHOT = new Slingshot(ToolMaterials.WOOD, 0, 0, new FabricItemSettings().group(ModItems.ITEM_GROUP));

    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(ModID, "pebble_fish"), PEBBLE_FISH);
        Registry.register(Registry.ITEM, new Identifier(ModID, "pebble_magma"), PEBBLE_MAGMA);
        Registry.register(Registry.ITEM, new Identifier(ModID, "slingshot"), SLINGSHOT);
        Registry.register(Registry.ITEM, new Identifier(ModID, "pebble"), PEBBLE);
    }
}
