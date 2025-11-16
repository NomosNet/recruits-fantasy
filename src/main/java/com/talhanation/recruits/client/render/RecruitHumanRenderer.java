package com.talhanation.recruits.client.render;
import com.mojang.blaze3d.vertex.PoseStack;
import com.talhanation.recruits.Main;
import com.talhanation.recruits.client.events.ClientEvent;
import com.talhanation.recruits.client.render.layer.RecruitHumanBiomeLayer;
import com.talhanation.recruits.client.render.layer.RecruitHumanCompanionLayer;
import com.talhanation.recruits.client.render.layer.RecruitHumanTeamColorLayer;
import com.talhanation.recruits.compat.IWeapon;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import com.talhanation.recruits.entities.CrossBowmanEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;

public class RecruitHumanRenderer extends MobRenderer<AbstractRecruitEntity, HumanoidModel<AbstractRecruitEntity>> {

    private static final ResourceLocation[] TEXTURE = {
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/01-humanrecruits/001-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/01-humanrecruits/002-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/01-humanrecruits/003-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/01-humanrecruits/004-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/01-humanrecruits/005-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/02-humanarcher/006-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/02-humanarcher/007-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/02-humanarcher/008-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/02-humanarcher/009-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/02-humanarcher/010-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/03-humancrossbow/011-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/03-humancrossbow/012-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/03-humancrossbow/013-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/03-humancrossbow/014-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/03-humancrossbow/015-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/04-humanknight/016-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/04-humanknight/017-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/04-humanknight/018-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/04-humanknight/019-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/04-humanknight/020-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/05-humanmagic/021-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/05-humanmagic/022-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/05-humanmagic/023-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/05-humanmagic/024-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/05-humanmagic/025-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/06-demonrecruits/026-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/06-demonrecruits/027-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/06-demonrecruits/028-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/06-demonrecruits/029-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/06-demonrecruits/030-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/07-demonarcher/031-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/07-demonarcher/032-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/07-demonarcher/033-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/07-demonarcher/034-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/07-demonarcher/035-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/08-demonknight/036-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/08-demonknight/037-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/08-demonknight/038-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/08-demonknight/039-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/08-demonknight/040-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/09-demongiant/041-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/09-demongiant/042-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/09-demongiant/043-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/09-demongiant/044-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/09-demongiant/045-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/10-demonmagic/046-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/10-demonmagic/047-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/10-demonmagic/048-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/10-demonmagic/049-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/10-demonmagic/050-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/11-elfrecruits/051-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/11-elfrecruits/052-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/11-elfrecruits/053-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/11-elfrecruits/054-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/11-elfrecruits/055-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/12-elfarcher/056-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/12-elfarcher/057-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/12-elfarcher/058-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/12-elfarcher/059-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/12-elfarcher/060-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/13-elfknight/061-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/13-elfknight/062-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/13-elfknight/063-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/13-elfknight/064-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/13-elfknight/065-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/14-elfnomad/066-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/14-elfnomad/067-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/14-elfnomad/068-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/14-elfnomad/069-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/14-elfnomad/070-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/15-elfmagic/071-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/15-elfmagic/072-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/15-elfmagic/073-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/15-elfmagic/074-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/15-elfmagic/075-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/16-animalrecruits/076-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/16-animalrecruits/077-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/16-animalrecruits/078-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/16-animalrecruits/079-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/16-animalrecruits/080-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/17-animalknight/081-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/17-animalknight/082-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/17-animalknight/083-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/17-animalknight/084-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/17-animalknight/085-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/18-animalarcher/086-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/18-animalarcher/087-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/18-animalarcher/088-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/18-animalarcher/089-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/18-animalarcher/090-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/19-animalnomad/091-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/19-animalnomad/092-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/19-animalnomad/093-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/19-animalnomad/094-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/19-animalnomad/095-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/20-animalmagic/096-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/20-animalmagic/097-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/20-animalmagic/098-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/20-animalmagic/099-animal.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/20-animalmagic/100-animal.png")
    };

    @Override
    public ResourceLocation getTextureLocation(AbstractRecruitEntity recruit) {
        return TEXTURE[recruit.getVariant()];
    }
    public RecruitHumanRenderer(EntityRendererProvider.Context mgr) {
        super(mgr, new HumanoidModel<>((mgr.bakeLayer(ModelLayers.PLAYER))), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel(mgr.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(mgr.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), mgr.getModelManager()));
        this.addLayer(new RecruitHumanTeamColorLayer(this));
        this.addLayer(new RecruitHumanBiomeLayer(this));
        this.addLayer(new RecruitHumanCompanionLayer(this));
        //this.addLayer(new ArrowLayer<>(mgr, this));
        this.addLayer(new ItemInHandLayer<>(this, mgr.getItemInHandRenderer()));
        this.addLayer(new CustomHeadLayer<>(this, mgr.getModelSet(), mgr.getItemInHandRenderer()));

    }


    public void render(AbstractRecruitEntity recruit, float p_117789_, float p_117790_, PoseStack p_117791_, MultiBufferSource p_117792_, int p_117793_) {
        this.setModelProperties(recruit);
        super.render(recruit, p_117789_, p_117790_, p_117791_, p_117792_, p_117793_);
    }

    private void setModelProperties(AbstractRecruitEntity recruit) {
        HumanoidModel<AbstractRecruitEntity> model = this.getModel();

        model.setAllVisible(true);
        model.crouching = recruit.isCrouching();
        HumanoidModel.ArmPose humanoidmodel$armpose = getArmPose(recruit, InteractionHand.MAIN_HAND);
        HumanoidModel.ArmPose humanoidmodel$armpose1 = getArmPose(recruit, InteractionHand.OFF_HAND);
        if (humanoidmodel$armpose.isTwoHanded()) {
            humanoidmodel$armpose1 = recruit.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
        }
        if (recruit.getMainArm() == HumanoidArm.RIGHT) {
            model.rightArmPose = humanoidmodel$armpose;
            model.leftArmPose = humanoidmodel$armpose1;
        } else {
            model.rightArmPose = humanoidmodel$armpose1;
            model.leftArmPose = humanoidmodel$armpose;
        }
    }

    private static HumanoidModel.ArmPose getArmPose(AbstractRecruitEntity recruit, InteractionHand hand) {
        ItemStack itemstack = recruit.getItemInHand(hand);
        boolean isMusket = IWeapon.isMusketModWeapon(itemstack) && (recruit instanceof CrossBowmanEntity crossBowman)  && crossBowman.isAggressive();
        if (itemstack.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else {
            if (recruit.getUsedItemHand() == hand && recruit.getUseItemRemainingTicks() > 0) {
                UseAnim useanim = itemstack.getUseAnimation();
                if (useanim == UseAnim.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }

                if (useanim == UseAnim.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }

                if (useanim == UseAnim.SPEAR) {
                    return HumanoidModel.ArmPose.THROW_SPEAR;
                }

                if (useanim == UseAnim.CROSSBOW && hand == recruit.getUsedItemHand() || isMusket) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }

                if (useanim == UseAnim.SPYGLASS) {
                    return HumanoidModel.ArmPose.SPYGLASS;
                }
            } else if (!recruit.swinging && itemstack.is(Items.CROSSBOW) && CrossbowItem.isCharged(itemstack) || isMusket) {
                return HumanoidModel.ArmPose.CROSSBOW_HOLD;
            }

            HumanoidModel.ArmPose forgeArmPose = net.minecraftforge.client.extensions.common.IClientItemExtensions.of(itemstack).getArmPose(recruit, hand, itemstack);
            if (forgeArmPose != null) return forgeArmPose;

            return HumanoidModel.ArmPose.ITEM;
        }
    }

}