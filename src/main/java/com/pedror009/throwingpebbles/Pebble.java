package com.pedror009.throwingpebbles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Pebble extends Item {
    private final int pebbleType;

    public Pebble (int pebbleType, Settings settings) {
        super (settings);
        this.pebbleType = pebbleType;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        //faz som ao jogar o item
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BLOCK_STONE_BREAK, SoundCategory.AMBIENT, 0.5F, 1F);
        //cooldown entre cada lançamento
        playerEntity.getItemCooldownManager().set(this, 10);


        if (!world.isClient) {
            PebbleEntity pebbleEntity = new PebbleEntity(pebbleType, 1, world, playerEntity);
            pebbleEntity.setItem(itemStack);
            pebbleEntity.setProperties(playerEntity, playerEntity.pitch, playerEntity.yaw, 0.0F, 0.7F, 0F);
            world.spawnEntity(pebbleEntity); // spawns entity
        }

        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!playerEntity.abilities.creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    public int getPebbleType() {
        return pebbleType;
    }
}
