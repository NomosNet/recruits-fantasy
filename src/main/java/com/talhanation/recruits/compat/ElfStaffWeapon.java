package com.talhanation.recruits.compat;

import com.talhanation.recruits.Main;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import io.redspace.ironsspellbooks.entity.spells.icicle.IcicleProjectile;
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

public class ElfStaffWeapon implements IWeapon {
    
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
        // Return null since we handle IcicleProjectile separately
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
        // Not used for IcicleProjectile
        return projectile;
    }

    @Override
    public AbstractArrow shootArrow(LivingEntity shooter, AbstractArrow projectile, double x, double y, double z) {
        return null;
    }

    @Override
    public SoundEvent getShootSound() {
        return SoundEvents.GLASS_BREAK; // Более подходящий звук для ледяных сосулек
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

        // Cast icicle spell when holding a staff
        if (isStaffItem(mainHand)) {
            castIcicleSpell(shooter, x, y, z);
        }
    }
    
    private void castIcicleSpell(AbstractRecruitEntity shooter, double x, double y, double z) {
        try {
            Level level = shooter.getCommandSenderWorld();
            
            // Create icicle projectile
            IcicleProjectile icicle = new IcicleProjectile(level, shooter);
            
            // Set damage (adjust value as needed)
            icicle.setDamage(8.0F); // Немного меньше урона чем у фаерболла, но с эффектом заморозки
            
            // Set position at eye level
            icicle.setPos(shooter.getX(), shooter.getY() + shooter.getEyeHeight() - icicle.getBoundingBox().getYsize() * 0.5f, shooter.getZ());
            
            // Calculate direction
            double dx = x - shooter.getX();
            double dy = y - (shooter.getY() + shooter.getEyeHeight());
            double dz = z - shooter.getZ();
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
            
            if (distance > 0.0D) {
                Vec3 direction = new Vec3(dx / distance, dy / distance, dz / distance);
                icicle.shoot(direction);
            }
            
            // Set no gravity like the original icicle spell
            icicle.setNoGravity(true);
            
            level.addFreshEntity(icicle);
            shooter.playSound(getShootSound(), 1.0F, 1.0F);
            
            Main.LOGGER.debug("StaffWeapon: Cast icicle spell successfully");
        } catch (Exception e) {
            Main.LOGGER.error("StaffWeapon: Error casting icicle spell: {}", e.getMessage());
        }
    }
}