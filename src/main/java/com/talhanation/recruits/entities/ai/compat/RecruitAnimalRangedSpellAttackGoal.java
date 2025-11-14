package com.talhanation.recruits.entities.ai.compat;

import com.talhanation.recruits.Main;
import com.talhanation.recruits.compat.AnimalStaffWeapon;
import com.talhanation.recruits.entities.MageAnimalEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class RecruitAnimalRangedSpellAttackGoal extends Goal {
    private final MageAnimalEntity mage;
    private final double speedModifier;
    private int seeTime;
    private int attackTime = -1;
    private LivingEntity target;
    private final AnimalStaffWeapon weapon;
    private final double stopRange;

    public RecruitAnimalRangedSpellAttackGoal(MageAnimalEntity mage, double stopRange) {
        this.weapon = new AnimalStaffWeapon();
        this.mage = mage;
        this.speedModifier = this.weapon.getMoveSpeedAmp();
        this.stopRange = stopRange;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
    }

    public boolean canUse() {
        LivingEntity livingentity = this.mage.getTarget();
        boolean shouldRanged = mage.getShouldRanged();
        if (livingentity != null && livingentity.isAlive() && shouldRanged) {
            return livingentity.distanceTo(this.mage) >= stopRange &&
                   this.canAttackMovePos() &&
                   !this.mage.needsToGetFood() &&
                   !this.mage.getShouldMount() &&
                   this.isStaffInHand();
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.mage.setAggressive(true);
        this.seeTime = 0;
        this.attackTime = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.seeTime = 0;
        this.attackTime = -1;
        this.mage.setAggressive(false);
        this.mage.stopUsingItem();
    }

    protected boolean isStaffInHand() {
        ItemStack itemStack = mage.getItemBySlot(EquipmentSlot.MAINHAND);
        return AnimalStaffWeapon.isStaffItem(itemStack);
    }

    public void tick() {
        this.target = this.mage.getTarget();

        if (target == null || !target.isAlive()) {
            return;
        }

        boolean canSee = this.mage.getSensing().hasLineOfSight(target);
        double distanceToTarget = this.mage.distanceToSqr(target.getX(), target.getY(), target.getZ());

        if (canSee) {
            ++this.seeTime;
        } else {
            this.seeTime = 0;
        }

        if (distanceToTarget <= (stopRange * stopRange) && this.seeTime >= 20) {
            this.mage.getNavigation().stop();
        } else {
            this.mage.getNavigation().moveTo(this.target, this.speedModifier);
        }

        this.mage.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        if (--this.attackTime <= 0 && this.seeTime >= -60) {
            if (!canSee) {
                return;
            }

            // Cast lightning spell at target
            double x = target.getX();
            double y = target.getY();
            double z = target.getZ();

            Main.LOGGER.debug("RecruitRangedSpellAttackGoal: About to cast lightning at target " + target.getName() + " at position " + x + ", " + y + ", " + z);
            Main.LOGGER.debug("RecruitRangedSpellAttackGoal: Mage position: " + mage.getX() + ", " + mage.getY() + ", " + mage.getZ());

            // Play load sound before casting
            mage.playSound(weapon.getLoadSound(), 1.0F, 1.0F);

            // Use weapon's spell casting method for lightning
            weapon.performRangedAttackIWeapon(mage, x, y, z, 1.0F);

            Main.LOGGER.debug("RecruitRangedSpellAttackGoal: Lightning spell casting completed");

            this.attackTime = weapon.getAttackCooldown() + mage.getRandom().nextInt(20);
        }
    }

    private boolean canAttackMovePos() {
        BlockPos movePos = mage.getMovePos();
        if (movePos == null) {
            return true;
        }
        double distanceToMovePos = mage.distanceToSqr(Vec3.atCenterOf(movePos));
        return distanceToMovePos <= 9.0D;
    }
}