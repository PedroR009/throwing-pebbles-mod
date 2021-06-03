package com.pedror009.throwingpebbles;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;


public class Slingshot extends ToolItem implements ToolMaterial {

    public Slingshot (ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) user;
            ItemStack pebbles = null;
            Pebble pebbleItem = null;

            for (int i = 0; i < playerEntity.inventory.size(); i++) {
                if(playerEntity.inventory.getStack(i).getItem() instanceof Pebble) {
                    pebbles = playerEntity.inventory.getStack(i);
                    pebbleItem= (Pebble)pebbles.getItem();
                    break;
                }
            }
            if (!(pebbles == null) || playerEntity.abilities.creativeMode) {
                if ((pebbles == null)) {
                    pebbles = new ItemStack(ModItems.PEBBLE);
                    pebbleItem= (Pebble)pebbles.getItem();
                }

                float distance = ((300 - remainingUseTicks)*0.5F) / 10;
                if (distance > 2.5) {distance = 2.5F;}
                if (distance >= 0.1D) {
                    if (!world.isClient) {
                        stack.damage(1, playerEntity, (p) -> {
                            p.sendToolBreakStatus(playerEntity.getActiveHand());
                        });

                        PebbleEntity pebbleEntity = new PebbleEntity(pebbleItem.getPebbleType(), (int)distance*2, world, playerEntity);
                        pebbleEntity.setItem(pebbles);
                        pebbleEntity.setProperties(playerEntity, playerEntity.pitch, playerEntity.yaw, 0.0F, distance, 0F);
                        world.spawnEntity(pebbleEntity);
                    }

                    world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (RANDOM.nextFloat() * 0.4F + 1.2F) + distance);
                    if (!playerEntity.abilities.creativeMode) {
                        pebbles.decrement(1);
                        if (pebbles.isEmpty()) {
                            playerEntity.inventory.removeOne(pebbles);
                        }
                    }
                }
            }
        }
        super.onStoppedUsing ( stack, world, user, remainingUseTicks );
    }

    @Override
    public int getMaxUseTime(ItemStack itemStack){
        return 300;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack used = user.getStackInHand ( hand );
        boolean bl = false;
        for (int i = 0; i < user.inventory.size(); i++) {
            if(user.inventory.getStack(i).getItem() instanceof Pebble) {
                bl = true;
                break;
            }
        }
        if (!user.abilities.creativeMode&&!bl){
            return TypedActionResult.fail ( used );
        }else {
            user.setCurrentHand ( hand );
            return TypedActionResult.consume ( used );
        }
    }

    @Override
    public int getDurability() {
        return 150;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 0;
    }

    @Override
    public float getAttackDamage() {
        return 0;
    }

    @Override
    public int getMiningLevel() {
        return 0;
    }

    @Override
    public int getEnchantability() {
        return super.getEnchantability ();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems (Items.STICK );
    }
}
