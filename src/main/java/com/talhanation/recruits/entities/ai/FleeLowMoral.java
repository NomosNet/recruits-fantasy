package com.talhanation.recruits.entities.ai;

import com.talhanation.recruits.entities.AbstractRecruitEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class FleeLowMoral extends Goal {
    private final AbstractRecruitEntity recruit;
    private boolean isFleeing = false;
    private Vec3 fleePosition;
    private int fleeCooldown = 0;
    private Vec3 startFleePosition; // Позиция, с которой начался побег

    public FleeLowMoral(AbstractRecruitEntity recruit) {
        this.recruit = recruit;
    }

    @Override
    public boolean canUse() {
        // Активируется когда мораль <= 0, нет кулдауна и не бежит уже
        return recruit.getMorale() <= 0 && fleeCooldown <= 0 && !isFleeing;
    }

    @Override
    public boolean canContinueToUse() {
        // Продолжает работать пока мораль <= 0 и все еще бежит
        return recruit.getMorale() <= 0 && isFleeing && !hasReachedSafety();
    }

    @Override
    public void start() {
        if (recruit.getCommandSenderWorld().random.nextDouble() < 0.8) { // 80% шанс побега
            calculateFleePosition();
            startFleePosition = recruit.position(); // Запоминаем откуда бежим
            
            if (recruit.getNavigation().moveTo(fleePosition.x, fleePosition.y, fleePosition.z, 1.8D)) {
                isFleeing = true;
                recruit.setFleeing(true);
                recruit.setTarget(null); // Очищаем цель
                recruit.setFollowState(0); // Сбрасываем состояние следования
                
                // Звук паники
                if (recruit.getRandom().nextDouble() < 0.3) {
                    recruit.playSound(recruit.getHurtSound(recruit.damageSources().generic()), 1.0F, 0.8F);
                }
            }
        } else {
            // Если не убежал, все равно даем кулдаун
            fleeCooldown = 100;
        }
    }

    @Override
    public void tick() {
        if (isFleeing) {
            // Проверяем, убежал ли достаточно далеко
            if (hasReachedSafety()) {
                stop();
                return;
            }
            
            // Если навигация завершилась, но не достигли безопасности, пытаемся снова
            if (recruit.getNavigation().isDone() && !hasReachedSafety()) {
                calculateFleePosition();
                recruit.getNavigation().moveTo(fleePosition.x, fleePosition.y, fleePosition.z, 1.8D);
            }
        }
        
        // Уменьшаем кулдаун
        if (fleeCooldown > 0) {
            fleeCooldown--;
        }
    }

    @Override
    public void stop() {
        if (isFleeing) {
            // Восстанавливаем мораль до 20 когда достиг безопасности
            recruit.setMoral(20.0F);
            
            // Сбрасываем состояние
            isFleeing = false;
            recruit.setFleeing(false);
            fleeCooldown = 400; // 20 секунд кулдауна
            
            // Останавливаем навигацию
            recruit.getNavigation().stop();
            
            // Возвращаем в нормальное состояние
            if (recruit.getOwner() != null) {
                recruit.setFollowState(1); // Возвращаем к следованию за владельцем
            } else {
                recruit.setFollowState(0); // Или к свободному блужданию
            }
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    private void calculateFleePosition() {
        // Генерируем случайное направление (50 блоков)
        double angle = recruit.getRandom().nextDouble() * Math.PI * 2;
        double fleeX = recruit.getX() + Math.cos(angle) * 50;
        double fleeZ = recruit.getZ() + Math.sin(angle) * 50;
        
        // Находим безопасную Y координату
        Level world = recruit.getCommandSenderWorld();
        int fleeY = world.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int)fleeX, (int)fleeZ);
        
        fleePosition = new Vec3(fleeX, fleeY, fleeZ);
    }

    private boolean hasReachedSafety() {
        if (startFleePosition == null || fleePosition == null) return false;
        
        // Проверяем, убежал ли на 50 блоков от начальной точки ИЛИ достиг целевой позиции
        double distanceFromStart = recruit.distanceToSqr(startFleePosition);
        double distanceToTarget = recruit.distanceToSqr(fleePosition);
        
        return distanceFromStart >= 2500.0D || // 50^2 = 2500
               distanceToTarget < 9.0D; // В пределах 3 блоков от цели
    }
}