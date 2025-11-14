package com.talhanation.recruits.compat;

import com.talhanation.recruits.Main;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import io.redspace.ironsspellbooks.entity.spells.fireball.MagicFireball;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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

public class DemonStaffWeapon implements IWeapon {
    
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
        return 60;
    }

    @Override
    public int getWeaponLoadTime() {
        return 30;
    }

    @Override
    public float getProjectileSpeed() {
        return 1.5F;
    }

    @Override
    public AbstractHurtingProjectile getProjectile(LivingEntity shooter) {
        // Return null since we handle MagicFireball separately
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
        // Not used for MagicFireball
        return projectile;
    }

    @Override
    public AbstractArrow shootArrow(LivingEntity shooter, AbstractArrow projectile, double x, double y, double z) {
        return null;
    }

    @Override
    public SoundEvent getShootSound() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    public SoundEvent getLoadSound() {
        return SoundEvents.BOOK_PAGE_TURN;
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

        // Cast magic fireball when holding a staff
        if (isStaffItem(mainHand)) {
            castMagicFireballSpell(shooter, x, y, z);
        }
    }
    
    private void castMagicFireballSpell(AbstractRecruitEntity shooter, double x, double y, double z) {
        try {
            Level level = shooter.getCommandSenderWorld();
            MagicFireball fireball = new MagicFireball(level, shooter);
            
            // Set damage and explosion radius (adjust these values as needed)
            fireball.setDamage(10.0F); // You can adjust this value
            fireball.setExplosionRadius(3); // You can adjust this value
            
            // Calculate direction
            double dx = x - shooter.getX();
            double dy = y - (shooter.getY() + shooter.getEyeHeight());
            double dz = z - shooter.getZ();
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
            
            if (distance > 0.0D) {
                Vec3 direction = new Vec3(dx / distance, dy / distance, dz / distance);
                fireball.shoot(direction);
            }
            
            fireball.setPos(shooter.getX(), shooter.getY() + shooter.getEyeHeight(), shooter.getZ());
            level.addFreshEntity(fireball);
            shooter.playSound(getShootSound(), 1.0F, 1.0F);
            
            Main.LOGGER.debug("StaffWeapon: Cast magic fireball successfully");
        } catch (Exception e) {
            Main.LOGGER.error("StaffWeapon: Error casting magic fireball: {}", e.getMessage());
        }
    }
}