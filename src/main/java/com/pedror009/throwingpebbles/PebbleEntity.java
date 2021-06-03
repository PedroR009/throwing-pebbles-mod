package com.pedror009.throwingpebbles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class PebbleEntity extends ThrownItemEntity {
    public static final int PEBBLE_NORMAL = 1;
    public static final int PEBBLE_MAGMA = 2;
    public static final int PEBBLE_FISH = 3;
    private final int pebbleType;
    private final int pebbleDamage;

    public PebbleEntity(EntityType<? extends  ThrownItemEntity> entityType, World world) {
        super(entityType, world);
        pebbleType = 0;
        pebbleDamage = 0;
    }

    public PebbleEntity(int pebbleType, int pebbleDamage, World world, LivingEntity owner) {
        super(ModItems.PebbleEntityType, owner, world);
        this.pebbleType = pebbleType;
        this.pebbleDamage = pebbleDamage;
    }

    public PebbleEntity(World world, double x, double y, double z) {
        super(ModItems.PebbleEntityType, x, y, z, world); // null will be changed later
        pebbleType = 0;
        pebbleDamage = 0;
    }

    protected void onEntityHit(EntityHitResult entityHitResult) { // called on entity hit.
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity(); // sets a new Entity instance as the EntityHitResult (victim)
        entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), (float)pebbleDamage); // deals damage

        if (entity instanceof LivingEntity) { // checks if entity is an instance of LivingEntity (meaning it is not a boat or minecart)
            ((LivingEntity) entity).addStatusEffect((new StatusEffectInstance(StatusEffects.SLOWNESS, 10 * 3, 2))); // applies a status effect
            ((LivingEntity) entity).addStatusEffect((new StatusEffectInstance(StatusEffects.BLINDNESS, 10 * 3, 2))); // applies a status effect
            switch (pebbleType) {
                case PEBBLE_NORMAL:
                    world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_STONE_BREAK, SoundCategory.AMBIENT, 0.5F, 1F);
                    break;
                case PEBBLE_MAGMA:
                    entity.setOnFireFor(15);
                    world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.AMBIENT, 0.5F, 1F);
                    break;
                case PEBBLE_FISH:
                    ((LivingEntity) entity).addStatusEffect((new StatusEffectInstance(StatusEffects.POISON, 70 * 3, 2)));
                    world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_PUFFER_FISH_FLOP, SoundCategory.AMBIENT, 0.5F, 1F);
            }
        }
    }

    protected void onCollision(HitResult hitResult) { // called on collision with a block
        super.onCollision(hitResult);
        if (!this.world.isClient) { // checks if the world is client
            this.world.sendEntityStatus(this, (byte)3); // particle?
            this.remove(); // kills the projectile
        }

    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.PEBBLE;
    }

    @Override
    public Packet createSpawnPacket() {
        return EntitySpawnPacket.create(this, ThrowingPebblesClient.PacketID);
    }
}