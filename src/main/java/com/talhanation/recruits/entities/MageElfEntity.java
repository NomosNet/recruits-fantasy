package com.talhanation.recruits.entities;

import com.talhanation.recruits.entities.ai.compat.RecruitElfRangedSpellAttackGoal;
import com.talhanation.recruits.compat.ElfStaffWeapon;

import com.talhanation.recruits.Main;
import com.talhanation.recruits.config.RecruitsServerConfig;
import com.talhanation.recruits.entities.ai.UseShield;
import com.talhanation.recruits.pathfinding.AsyncGroundPathNavigation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class MageElfEntity extends AbstractRecruitEntity {

    private final Predicate<ItemEntity> ALLOWED_ITEMS = (item) ->
            (!item.hasPickUpDelay() && item.isAlive() && getInventory().canAddItem(item.getItem()) && this.wantsToPickUp(item.getItem()));

    public MageElfEntity(EntityType<? extends AbstractRecruitEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new RecruitElfRangedSpellAttackGoal(this, 16.0D));
    }

    //ATTRIBUTES
    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(ForgeMod.SWIM_SPEED.get(), 0.3D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.05D)
                .add(Attributes.ATTACK_DAMAGE, 1.5D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(ForgeMod.ENTITY_REACH.get(), 0D)
                .add(Attributes.ATTACK_SPEED);
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficultyInstance, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag nbt) {
        RandomSource randomsource = world.getRandom();
        SpawnGroupData ilivingentitydata = super.finalizeSpawn(world, difficultyInstance, reason, data, nbt);
        ((AsyncGroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        this.populateDefaultEquipmentEnchantments(randomsource, difficultyInstance);
        this.initSpawn();
        return ilivingentitydata;
    }

    @Override
    public void initSpawn() {
        this.setCustomName(Component.literal("ElfMage"));
        this.setCost(RecruitsServerConfig.MageCost.get());
        this.setEquipment();
        this.setDropEquipment();
        this.setRandomSpawnBonus();
        this.setPersistenceRequired();
        this.setGroup(2);
        AbstractRecruitEntity.applySpawnValues(this);
        this.setVariant(this.random.nextInt(95, 100));
    }

    @Override
    public boolean wantsToPickUp(ItemStack itemStack) {
        // Allow picking up staffs from Iron's Spellbooks
        if (Main.isSpellbooksLoaded) {
            if (ElfStaffWeapon.isStaffItem(itemStack)) {
                return true;
            }
        }
        return super.wantsToPickUp(itemStack);
    }

    @Override
    public boolean canHoldItem(ItemStack itemStack) {
        // Allow holding staffs from Iron's Spellbooks
        if (Main.isSpellbooksLoaded) {
            if (ElfStaffWeapon.isStaffItem(itemStack)) {
                return true;
            }
        }
        return super.canHoldItem(itemStack);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
        // Дополнительная логика для стартового снаряжения мага
    }

    public Predicate<ItemEntity> getAllowedItems(){
        return ALLOWED_ITEMS;
    }

    public java.util.List<java.util.List<String>> getEquipment(){
        return RecruitsServerConfig.MageStartEquipments.get();
    }

    // Этот метод теперь не используется, но оставлен для совместимости
    public double getMeleeStartRange() {
        return 8.0D;
    }
}