package com.talhanation.recruits.compat;

import com.talhanation.recruits.Main;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class AnimalStaffWeapon implements IWeapon {
    
    private static final Set<String> ALLOWED_STAFFS = new HashSet<>();
    
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
        return 80; // Увеличил кулдаун для молнии
    }

    @Override
    public int getWeaponLoadTime() {
        return 40; // Увеличил время зарядки для молнии
    }

    @Override
    public float getProjectileSpeed() {
        return 1.0F; // Для молнии не важно
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
        return null;
    }

    @Override
    public AbstractArrow shootArrow(LivingEntity shooter, AbstractArrow projectile, double x, double y, double z) {
        return null;
    }

    @Override
    public SoundEvent getShootSound() {
        // Используем звук молнии
        try {
            Class<?> soundRegistryClass = Class.forName("io.redspace.ironsspellbooks.registries.SoundRegistry");
            var lightningSoundField = soundRegistryClass.getField("LIGHTNING_BOLT_CAST");
            return (SoundEvent) lightningSoundField.get(null);
        } catch (Exception e) {
            Main.LOGGER.warn("Could not load lightning sound, using default: {}", e.getMessage());
            return SoundEvents.LIGHTNING_BOLT_THUNDER;
        }
    }

    @Override
    public SoundEvent getLoadSound() {
        // Используем звук зарядки молнии
        try {
            Class<?> soundRegistryClass = Class.forName("io.redspace.ironsspellbooks.registries.SoundRegistry");
            var lightningChargeField = soundRegistryClass.getField("LIGHTNING_BOLT_CHARGE");
            return (SoundEvent) lightningChargeField.get(null);
        } catch (Exception e) {
            Main.LOGGER.warn("Could not load lightning charge sound, using default: {}", e.getMessage());
            return SoundEvents.BOOK_PAGE_TURN;
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

        // Cast lightning spell when holding a staff
        if (isStaffItem(mainHand)) {
            castLightningSpell(shooter, x, y, z);
        }
    }
    
    private void castLightningSpell(AbstractRecruitEntity shooter, double x, double y, double z) {
        try {
            Level level = shooter.getCommandSenderWorld();
            
            // Создаем молнию в позиции цели
            LightningBolt lightningBolt = new LightningBolt(net.minecraft.world.entity.EntityType.LIGHTNING_BOLT, level);
            lightningBolt.setVisualOnly(false); // Молния наносит урон
            lightningBolt.setPos(x, y, z);
            
            // Добавляем молнию в мир
            level.addFreshEntity(lightningBolt);
            
            // Проигрываем звук
            shooter.playSound(getShootSound(), 1.5F, 1.0F);
            
            Main.LOGGER.debug("StaffWeapon: Cast lightning spell successfully");
        } catch (Exception e) {
            Main.LOGGER.error("StaffWeapon: Error casting lightning spell: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}