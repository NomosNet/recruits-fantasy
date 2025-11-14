package com.talhanation.recruits.entities;

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
import java.util.List;
import java.util.function.Predicate;

public class DemonRecruitEntity extends AbstractRecruitEntity {

    private final Predicate<ItemEntity> ALLOWED_ITEMS = (item) ->
            (!item.hasPickUpDelay() && item.isAlive() && getInventory().canAddItem(item.getItem()) && this.wantsToPickUp(item.getItem()));

    public DemonRecruitEntity(EntityType<? extends AbstractRecruitEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // Add shield usage for demon recruits
        this.goalSelector.addGoal(2, new UseShield(this));
    }

    //ATTRIBUTES
    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D) // Customize health
                .add(Attributes.MOVEMENT_SPEED, 0.32D) // Customize speed
                .add(ForgeMod.SWIM_SPEED.get(), 0.3D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.15D) // Customize knockback resistance
                .add(Attributes.ATTACK_DAMAGE, 1.0D) // Customize attack damage
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(ForgeMod.ENTITY_REACH.get(), 0D)
                .add(Attributes.ATTACK_SPEED);
    }

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
        this.setCustomName(Component.literal("Demon Recruit"));
        // TODO: Add cost config in RecruitsServerConfig if needed
        // this.setCost(RecruitsServerConfig.DemonRecruitCost.get());
        this.setCost(100); // Temporary default cost

        this.setEquipment();
        this.setDropEquipment();
        this.setRandomSpawnBonus();
        this.setPersistenceRequired();

        this.setGroup(1); // Group 1 for melee units, Group 2 for ranged units

        // Add permanent fire resistance - unique to demon recruits
        this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false, true));

        AbstractRecruitEntity.applySpawnValues(this);
        // Force demon-specific skin variants 40-45
        this.setVariant(this.random.nextInt(40, 46));
    }

    @Override
    public boolean wantsToPickUp(ItemStack itemStack) {
        // Customize what items this unit can pick up
        if((itemStack.getItem() instanceof SwordItem && this.getMainHandItem().isEmpty()) ||
          (itemStack.getItem() instanceof AxeItem && this.getMainHandItem().isEmpty()) ||
          (itemStack.getItem() instanceof ShieldItem && this.getOffhandItem().isEmpty()))
            return !hasSameTypeOfItem(itemStack);

        else return super.wantsToPickUp(itemStack);
    }

    public Predicate<ItemEntity> getAllowedItems(){
        return ALLOWED_ITEMS;
    }

    @Override
    public boolean canHoldItem(ItemStack itemStack){
        // Prevent this unit from holding ranged weapons
        return !(itemStack.getItem() instanceof CrossbowItem || itemStack.getItem() instanceof BowItem);
    }

    public List<List<String>> getEquipment(){
        // TODO: Add equipment config in RecruitsServerConfig if needed
        // return RecruitsServerConfig.DemonRecruitStartEquipments.get();
        // For now, return a simple default
        return List.of(List.of("minecraft:iron_sword", "", "", "", "", ""));
    }

    @Override
    public void tick() {
        super.tick();
        // Ensure fire resistance is always active (reapply if somehow removed)
        if (!this.level().isClientSide && !this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false, true));
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if (isIronSpellbooksFireDamage(damageSource)) {
            return true;
        }
        return super.isInvulnerableTo(damageSource);
    }

    private static boolean isIronSpellbooksFireDamage(DamageSource damageSource) {
        if (!Main.isSpellbooksLoaded || damageSource == null) 
            return false;
        
        try {
            Class<?> srcClass = damageSource.getClass();
            String className = srcClass.getName().toLowerCase();
            
            // Проверяем, что это действительно урон от Iron's Spellbooks
            if (!className.contains("ironsspellbooks") || 
                !className.contains("damage") || 
                !className.contains("spell")) {
                return false;
            }

            // Приоритет 1: проверка через getElement()
            try {
                java.lang.reflect.Method getElement = srcClass.getMethod("getElement");
                Object element = getElement.invoke(damageSource);
                if (element != null && element.toString().equalsIgnoreCase("FIRE")) {
                    return true;
                }
            } catch (NoSuchMethodException ignored) {}

            // Приоритет 2: проверка через getSchool()  
            try {
                java.lang.reflect.Method getSchool = srcClass.getMethod("getSchool");
                Object school = getSchool.invoke(damageSource);
                if (school != null && school.toString().toLowerCase().contains("fire")) {
                    return true;
                }
            } catch (NoSuchMethodException ignored) {}

            // Приоритет 3: проверка через getSpellId()
            try {
                java.lang.reflect.Method getSpellId = srcClass.getMethod("getSpellId");
                Object spellId = getSpellId.invoke(damageSource);
                if (spellId != null) {
                    String spellStr = spellId.toString().toLowerCase();
                    if (spellStr.contains("fire") || spellStr.contains("flame") || 
                        spellStr.contains("burn") || spellStr.contains("inferno")) {
                        return true;
                    }
                }
            } catch (NoSuchMethodException ignored) {}

            // Приоритет 4: проверка msgId как запасной вариант
            String msgId = damageSource.getMsgId().toLowerCase();
            if (msgId.contains("irons_spellbooks") && 
                (msgId.contains("fire") || msgId.contains("flame") || 
                msgId.contains("burn") || msgId.contains("hellfire"))) {
                return true;
            }

        } catch (Exception e) {
            // При любой ошибке - считаем что это не огненный урон
            Main.LOGGER.error("Error checking Iron's Spellbooks damage type", e);
        }
        
        return false;
    }
}

