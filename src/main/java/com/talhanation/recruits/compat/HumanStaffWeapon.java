package com.talhanation.recruits.compat;

import com.talhanation.recruits.Main;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import io.redspace.ironsspellbooks.entity.mobs.SummonedVex;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class HumanStaffWeapon implements IWeapon {
    
    private static final Set<String> ALLOWED_STAFFS = new HashSet<>();
    private static final int MAX_VEX_COUNT = 3; // Максимальное количество вексов
    
    static {
        ALLOWED_STAFFS.add("irons_spellbooks:graybeard_staff");
        ALLOWED_STAFFS.add("irons_spellbooks:blood_staff");
        ALLOWED_STAFFS.add("irons_spellbooks:wand");
        ALLOWED_STAFFS.add("irons_spellbooks:arcane_staff");
    }

    @Override
    @Nullable
    public Item getWeapon() {
        return Items.STICK;
    }

    public static boolean isStaffItem(ItemStack stack) {
        if (stack.isEmpty()) return false;
        
        try {
            ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
            if (itemId != null) {
                String itemIdStr = itemId.toString();
                boolean isStaff = ALLOWED_STAFFS.contains(itemIdStr);
                Main.LOGGER.debug("Checking if item is staff: {} -> {}", itemIdStr, isStaff);
                return isStaff;
            }
        } catch (Exception e) {
            Main.LOGGER.error("Error checking staff item: {}", e.getMessage());
        }
        
        return false;
    }

    @Override
    public double getMoveSpeedAmp() {
        return 0.5D;
    }

    @Override
    public int getAttackCooldown() {
        return 200; // Increased cooldown for summon spell
    }

    @Override
    public int getWeaponLoadTime() {
        return 60; // Increased load time for summon spell
    }

    @Override
    public float getProjectileSpeed() {
        return 1.0F; // Not used for summoning
    }

    @Override
    public AbstractHurtingProjectile getProjectile(LivingEntity shooter) {
        return null;
    }

    @Override
    public AbstractArrow getProjectileArrow(LivingEntity shooter) {
        return null;
    }

    @Override
    public boolean isLoaded(ItemStack stack) {
        return isStaffItem(stack);
    }

    @Override
    public void setLoaded(ItemStack stack, boolean loaded) {
        // Staffs don't need loading
    }

    @Override
    public AbstractHurtingProjectile shoot(LivingEntity shooter, AbstractHurtingProjectile projectile, double x, double y, double z) {
        return projectile;
    }

    @Override
    public AbstractArrow shootArrow(LivingEntity shooter, AbstractArrow projectile, double x, double y, double z) {
        return null;
    }

    @Override
    public SoundEvent getShootSound() {
        // Use Summon Vex cast sound
        try {
            Class<?> soundRegistry = Class.forName("io.redspace.ironsspellbooks.registries.SoundRegistry");
            var summonVexCastField = soundRegistry.getDeclaredField("SUMMON_VEX_CAST");
            return (SoundEvent) summonVexCastField.get(null);
        } catch (Exception e) {
            Main.LOGGER.warn("Could not load Summon Vex sound, using default: {}", e.getMessage());
            return SoundEvents.EVOKER_CAST_SPELL;
        }
    }

    @Override
    public SoundEvent getLoadSound() {
        // Use Summon Vex prepare sound
        try {
            Class<?> soundRegistry = Class.forName("io.redspace.ironsspellbooks.registries.SoundRegistry");
            var summonVexPrepareField = soundRegistry.getDeclaredField("SUMMON_VEX_PREPARE");
            return (SoundEvent) summonVexPrepareField.get(null);
        } catch (Exception e) {
            Main.LOGGER.warn("Could not load Summon Vex prepare sound, using default: {}", e.getMessage());
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }
    }

    @Override
    public boolean isGun() {
        return false;
    }

    @Override
    public boolean canMelee() {
        return false;
    }

    @Override
    public boolean isBow() {
        return false;
    }

    @Override
    public boolean isCrossBow() {
        return false;
    }

    @Override
    public void performRangedAttackIWeapon(AbstractRecruitEntity shooter, double x, double y, double z, float projectileSpeed) {
        Main.LOGGER.debug("StaffWeapon: Starting ranged attack for mage");
        ItemStack mainHand = shooter.getMainHandItem();
        
        if (mainHand.isEmpty()) {
            Main.LOGGER.debug("StaffWeapon: Mage has no item in hand");
            return;
        }

        Main.LOGGER.debug("StaffWeapon: Mage has item: {}", ForgeRegistries.ITEMS.getKey(mainHand.getItem()));

        // Cast summon vex when holding a staff
        if (isStaffItem(mainHand)) {
            castSummonVexSpell(shooter);
        }
    }
    
    private void castSummonVexSpell(AbstractRecruitEntity shooter) {
        try {
            Level level = shooter.getCommandSenderWorld();
            
            if (!level.isClientSide) {
                // Проверяем количество живых вексов перед призывом
                int currentVexCount = getAliveVexCount(shooter);
                
                if (currentVexCount >= MAX_VEX_COUNT) {
                    Main.LOGGER.debug("StaffWeapon: Cannot summon vex - already have {} vexes (max: {})", currentVexCount, MAX_VEX_COUNT);
                    shooter.playSound(SoundEvents.EVOKER_PREPARE_ATTACK, 1.0F, 1.0F);
                    return;
                }
                
                // Create SummonedVex entity
                SummonedVex vex = new SummonedVex(level, shooter);
                
                // Set position near the shooter
                Vec3 spawnPos = shooter.position()
                    .add(shooter.getLookAngle().scale(2.0))
                    .add(0, 1.0, 0);
                
                vex.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                
                // Set up the vex properties
                vex.setOwner(shooter);
                vex.setLimitedLife(20 * 60 * 1); // 1 minute lifespan
                
                // Add to world
                level.addFreshEntity(vex);
                shooter.playSound(getShootSound(), 1.0F, 1.0F);
                
                Main.LOGGER.debug("StaffWeapon: Summoned vex successfully at position {}. Total vexes: {}", spawnPos, currentVexCount + 1);
            }
        } catch (Exception e) {
            Main.LOGGER.error("StaffWeapon: Error summoning vex: {}", e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Получает количество живых вексов, принадлежащих стрелку
     */
    private int getAliveVexCount(LivingEntity shooter) {
        Level level = shooter.getCommandSenderWorld();
        UUID shooterUUID = shooter.getUUID();
        
        // Ищем всех вексов в радиусе 50 блоков
        AABB searchArea = shooter.getBoundingBox().inflate(50.0D);
        List<SummonedVex> vexes = level.getEntitiesOfClass(SummonedVex.class, searchArea);
        
        int count = 0;
        for (SummonedVex vex : vexes) {
            LivingEntity owner = vex.getOwner();
            if (vex.isAlive() && owner != null && shooterUUID.equals(owner.getUUID())) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Вспомогательный метод для проверки можно ли призвать нового векса
     */
    public boolean canSummonMoreVexes(LivingEntity shooter) {
        return getAliveVexCount(shooter) < MAX_VEX_COUNT;
    }
}