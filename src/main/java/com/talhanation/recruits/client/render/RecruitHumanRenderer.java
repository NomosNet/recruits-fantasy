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
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/01-animals/01-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/01-animals/02-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/01-animals/03-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/01-animals/04-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/01-animals/05-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/01-animals/06-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/01-animals/07-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/01-animals/08-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/01-animals/09-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/01-animals/10-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/02-snowanimals/11-snow-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/02-snowanimals/12-snow-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/02-snowanimals/13-snow-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/02-snowanimals/14-snow-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/02-snowanimals/15-snow-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/02-snowanimals/16-snow-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/02-snowanimals/17-snow-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/02-snowanimals/18-snow-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/02-snowanimals/19-snow-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/02-snowanimals/20-snow-animals.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/03-elf/21-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/03-elf/22-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/03-elf/23-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/03-elf/24-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/03-elf/25-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/03-elf/26-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/03-elf/27-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/03-elf/28-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/03-elf/29-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/03-elf/30-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/04-blackelf/31-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/04-blackelf/32-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/04-blackelf/33-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/04-blackelf/34-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/04-blackelf/35-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/04-blackelf/36-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/04-blackelf/37-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/04-blackelf/38-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/04-blackelf/39-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/04-blackelf/40-elf.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/05-demon/41-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/05-demon/42-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/05-demon/43-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/05-demon/44-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/05-demon/45-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/05-demon/46-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/05-demon/47-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/05-demon/48-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/05-demon/49-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/05-demon/50-demon.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/06-cultist/51-cultist.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/06-cultist/52-cultist.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/06-cultist/53-cultist.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/06-cultist/54-cultist.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/06-cultist/55-cultist.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/06-cultist/56-cultist.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/06-cultist/57-cultist.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/06-cultist/58-cultist.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/06-cultist/59-cultist.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/06-cultist/60-cultist.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/07-human/61-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/07-human/62-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/07-human/63-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/07-human/64-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/07-human/65-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/07-human/66-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/07-human/67-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/07-human/68-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/07-human/69-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/07-human/70-human.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/08-gnom/71-gnom.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/08-gnom/72-gnom.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/08-gnom/73-gnom.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/08-gnom/74-gnom.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/08-gnom/75-gnom.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/08-gnom/76-gnom.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/08-gnom/77-gnom.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/08-gnom/78-gnom.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/08-gnom/79-gnom.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/08-gnom/80-gnom.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/09-pirat/81-pirat.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/09-pirat/82-pirat.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/09-pirat/83-pirat.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/09-pirat/84-pirat.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/09-pirat/85-pirat.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/09-pirat/86-pirat.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/09-pirat/87-pirat.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/09-pirat/88-pirat.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/09-pirat/89-pirat.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/09-pirat/90-pirat.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/10-demonmag/91-demonmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/10-demonmag/92-demonmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/10-demonmag/93-demonmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/10-demonmag/94-demonmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/10-demonmag/95-demonmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/11-elfmag/96-elfmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/11-elfmag/97-elfmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/11-elfmag/98-elfmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/11-elfmag/99-elfmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/11-elfmag/100-elfmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/12-animalmag/101-animalmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/12-animalmag/102-animalmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/12-animalmag/103-animalmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/12-animalmag/104-animalmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/12-animalmag/105-animalmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/13-humanmag/106-humanmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/13-humanmag/107-humanmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/13-humanmag/108-humanmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/13-humanmag/109-humanmag.png"),
            new ResourceLocation(Main.MOD_ID,"textures/entity/human/13-humanmag/110-humanmag.png"),
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