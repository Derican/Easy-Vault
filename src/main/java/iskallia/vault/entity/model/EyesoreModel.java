// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.entity.model;

import net.minecraft.entity.Entity;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.network.datasync.DataParameter;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.util.math.MathHelper;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.model.Model;
import java.util.LinkedList;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import java.util.List;
import iskallia.vault.entity.EyesoreEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;

public class EyesoreModel extends SegmentedModel<EyesoreEntity>
{
    public int tentaclesRemaining;
    private final List<ModelRenderer> tentacles;
    private final ImmutableList<ModelRenderer> segments;
    private final ModelRenderer body;
    private final ModelRenderer tentacles_0;
    private final ModelRenderer tentacles_1;
    private final ModelRenderer tentacles_2;
    private final ModelRenderer tentacles_3;
    private final ModelRenderer tentacles_4;
    private final ModelRenderer tentacles_5;
    private final ModelRenderer tentacles_6;
    private final ModelRenderer tentacles_7;
    private final ModelRenderer tentacles_8;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer LeftEyestalks;
    private final ModelRenderer cube_r7;
    private final ModelRenderer cube_r8;
    private final ModelRenderer RightEyestalks;
    private final ModelRenderer cube_r9;
    private final ModelRenderer cube_r10;
    private final ModelRenderer tentacles_1_r1;
    private final ModelRenderer tentacles_0_r1;
    private final ModelRenderer tentacles_2_r1;
    private final ModelRenderer tentacles_1_r2;
    private final ModelRenderer tentacles_3_r1;
    private final ModelRenderer tentacles_2_r2;
    private final ModelRenderer tentacles_4_r1;
    private final ModelRenderer tentacles_3_r2;
    private final ModelRenderer tentacles_5_r1;
    private final ModelRenderer tentacles_4_r2;
    private final ModelRenderer tentacles_6_r1;
    private final ModelRenderer tentacles_5_r2;
    private final ModelRenderer tentacles_7_r1;
    private final ModelRenderer tentacles_6_r2;
    private final ModelRenderer tentacles_8_r1;
    private final ModelRenderer tentacles_7_r2;
    private final ModelRenderer tentacles_9_r1;
    private final ModelRenderer tentacles_8_r2;
    
    public EyesoreModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.tentacles = new LinkedList<ModelRenderer>();
        this.tentaclesRemaining = 9;
        final ImmutableList.Builder<ModelRenderer> segmentBuilder = ImmutableList.builder();
        (this.body = new ModelRenderer(this)).setPos(0.0f, 16.0f, 0.0f);
        this.body.texOffs(14, 32).addBox(-3.0f, -5.0f, 8.0f, 6.0f, 4.0f, 1.0f, 0.0f, false);
        this.body.texOffs(48, 7).addBox(-3.0f, -9.0f, 6.0f, 6.0f, 3.0f, 3.0f, 0.0f, false);
        this.body.texOffs(0, 48).addBox(-3.0f, -9.0f, 1.0f, 6.0f, 1.0f, 4.0f, 0.0f, false);
        this.body.texOffs(0, 48).addBox(-3.0f, -9.0f, -4.0f, 6.0f, 1.0f, 4.0f, 0.0f, false);
        this.body.texOffs(41, 47).addBox(-3.0f, -9.0f, -9.0f, 6.0f, 2.0f, 4.0f, 0.0f, false);
        this.body.texOffs(14, 32).addBox(-3.0f, 0.0f, 8.0f, 6.0f, 4.0f, 1.0f, 0.0f, false);
        this.body.texOffs(70, 30).addBox(-3.0f, 6.0f, -9.0f, 6.0f, 1.0f, 1.0f, 0.0f, false);
        this.body.texOffs(48, 0).addBox(-3.0f, 6.0f, -7.0f, 6.0f, 4.0f, 3.0f, 0.0f, false);
        this.body.texOffs(56, 53).addBox(-6.0f, 6.0f, -7.0f, 2.0f, 6.0f, 3.0f, 0.0f, false);
        this.body.texOffs(46, 53).addBox(4.0f, 6.0f, -7.0f, 2.0f, 6.0f, 3.0f, 0.0f, false);
        this.body.texOffs(70, 28).addBox(-8.0f, 5.0f, -9.0f, 6.0f, 1.0f, 1.0f, 0.0f, false);
        this.body.texOffs(66, 2).addBox(2.0f, 5.0f, -9.0f, 6.0f, 1.0f, 1.0f, 0.0f, false);
        this.body.texOffs(14, 37).addBox(-8.0f, 1.0f, -9.0f, 6.0f, 1.0f, 1.0f, 0.0f, false);
        this.body.texOffs(48, 13).addBox(-3.0f, 2.0f, -9.0f, 6.0f, 1.0f, 1.0f, 0.0f, false);
        this.body.texOffs(19, 45).addBox(-5.0f, -7.5f, -11.0f, 10.0f, 3.0f, 3.0f, 0.0f, false);
        this.body.texOffs(42, 42).addBox(-6.0f, -5.75f, -10.0f, 12.0f, 3.0f, 2.0f, 0.0f, false);
        this.body.texOffs(37, 32).addBox(-5.0f, -4.5f, -12.0f, 10.0f, 2.0f, 4.0f, 0.0f, false);
        this.body.texOffs(63, 0).addBox(2.0f, 1.0f, -9.0f, 6.0f, 1.0f, 1.0f, 0.0f, false);
        this.body.texOffs(0, 0).addBox(-8.0f, -8.0f, -8.0f, 16.0f, 16.0f, 16.0f, 0.0f, false);
        (this.cube_r1 = new ModelRenderer(this)).setPos(-6.5f, 5.5f, -8.5f);
        this.body.addChild(this.cube_r1);
        this.setRotationAngle(this.cube_r1, 0.6981f, 0.0f, 0.0f);
        this.cube_r1.texOffs(42, 66).addBox(12.5f, -4.5f, 0.5f, 1.0f, 5.0f, 1.0f, 0.0f, false);
        this.cube_r1.texOffs(24, 59).addBox(7.5f, -3.5f, -0.5f, 1.0f, 5.0f, 1.0f, 0.0f, false);
        this.cube_r1.texOffs(0, 48).addBox(4.5f, -1.5f, -0.5f, 1.0f, 3.0f, 1.0f, 0.0f, false);
        this.cube_r1.texOffs(36, 66).addBox(-0.5f, -8.5f, 0.5f, 2.0f, 9.0f, 1.0f, 0.0f, false);
        (this.cube_r2 = new ModelRenderer(this)).setPos(-4.5f, 2.6823f, -8.2535f);
        this.body.addChild(this.cube_r2);
        this.setRotationAngle(this.cube_r2, -0.7418f, 0.0f, 0.0f);
        this.cube_r2.texOffs(26, 51).addBox(7.5f, -1.5f, -0.5f, 1.0f, 2.0f, 1.0f, 0.0f, false);
        this.cube_r2.texOffs(37, 32).addBox(0.5f, -1.5f, -0.5f, 1.0f, 3.0f, 1.0f, 0.0f, false);
        (this.cube_r3 = new ModelRenderer(this)).setPos(6.0f, 7.0f, -6.0f);
        this.body.addChild(this.cube_r3);
        this.setRotationAngle(this.cube_r3, 0.0f, 0.0f, -0.3927f);
        this.cube_r3.texOffs(66, 51).addBox(0.0f, 0.0f, 7.0f, 2.0f, 6.0f, 2.0f, 0.0f, false);
        this.cube_r3.texOffs(14, 70).addBox(1.0f, -2.0f, 3.0f, 2.0f, 6.0f, 2.0f, 0.0f, false);
        this.cube_r3.texOffs(70, 41).addBox(0.0f, -1.0f, -1.0f, 2.0f, 6.0f, 2.0f, 0.0f, false);
        (this.cube_r4 = new ModelRenderer(this)).setPos(-6.0f, 9.0f, -6.0f);
        this.body.addChild(this.cube_r4);
        this.setRotationAngle(this.cube_r4, 0.0f, 0.0f, 0.3927f);
        this.cube_r4.texOffs(0, 32).addBox(-3.0f, -3.0f, 7.0f, 2.0f, 6.0f, 2.0f, 0.0f, false);
        this.cube_r4.texOffs(22, 71).addBox(-3.0f, -3.0f, -1.0f, 2.0f, 6.0f, 2.0f, 0.0f, false);
        (this.cube_r5 = new ModelRenderer(this)).setPos(-8.389f, 6.9281f, -2.0f);
        this.body.addChild(this.cube_r5);
        this.setRotationAngle(this.cube_r5, 0.0f, 0.0f, 0.3927f);
        this.cube_r5.texOffs(54, 68).addBox(-1.0f, -3.0f, -1.0f, 2.0f, 6.0f, 2.0f, 0.0f, false);
        (this.cube_r6 = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.body.addChild(this.cube_r6);
        this.setRotationAngle(this.cube_r6, 0.3927f, 0.0f, 0.0f);
        this.cube_r6.texOffs(0, 32).addBox(8.0f, -6.0f, -3.0f, 3.0f, 8.0f, 8.0f, 0.0f, false);
        this.cube_r6.texOffs(22, 32).addBox(11.0f, -4.0f, -3.0f, 4.0f, 5.0f, 7.0f, 0.0f, false);
        this.cube_r6.texOffs(16, 51).addBox(-15.0f, -3.0f, -7.0f, 3.0f, 4.0f, 4.0f, 0.0f, false);
        this.cube_r6.texOffs(22, 32).addBox(-15.0f, -4.0f, -3.0f, 4.0f, 5.0f, 7.0f, 0.0f, false);
        this.cube_r6.texOffs(0, 32).addBox(-11.0f, -6.0f, -3.0f, 3.0f, 8.0f, 8.0f, 0.0f, false);
        (this.LeftEyestalks = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.body.addChild(this.LeftEyestalks);
        (this.cube_r7 = new ModelRenderer(this)).setPos(7.0f, 3.0f, 4.0f);
        this.LeftEyestalks.addChild(this.cube_r7);
        this.setRotationAngle(this.cube_r7, 0.0f, 0.0f, 0.3927f);
        this.cube_r7.texOffs(61, 29).addBox(7.0f, -1.5f, -2.0f, 3.0f, 3.0f, 3.0f, 0.0f, false);
        this.cube_r7.texOffs(44, 38).addBox(1.0f, -1.0f, -1.0f, 6.0f, 2.0f, 2.0f, 0.0f, false);
        (this.cube_r8 = new ModelRenderer(this)).setPos(7.0f, 5.0f, 0.0f);
        this.LeftEyestalks.addChild(this.cube_r8);
        this.setRotationAngle(this.cube_r8, 0.0f, 0.0f, 0.6109f);
        this.cube_r8.texOffs(61, 29).addBox(7.0f, -1.6f, -2.0f, 3.0f, 3.0f, 3.0f, 0.0f, false);
        this.cube_r8.texOffs(44, 38).addBox(1.0f, -1.0f, -1.0f, 6.0f, 2.0f, 2.0f, 0.0f, false);
        (this.RightEyestalks = new ModelRenderer(this)).setPos(0.0f, 0.0f, 0.0f);
        this.body.addChild(this.RightEyestalks);
        (this.cube_r9 = new ModelRenderer(this)).setPos(-7.5f, 3.0f, 5.0f);
        this.RightEyestalks.addChild(this.cube_r9);
        this.setRotationAngle(this.cube_r9, 0.0f, 0.0f, -0.3927f);
        this.cube_r9.texOffs(61, 29).addBox(-8.5f, -1.5f, -2.0f, 3.0f, 3.0f, 3.0f, 0.0f, false);
        this.cube_r9.texOffs(57, 47).addBox(-5.5f, -1.0f, -1.0f, 5.0f, 2.0f, 2.0f, 0.0f, false);
        (this.cube_r10 = new ModelRenderer(this)).setPos(-7.0f, 5.0f, 1.0f);
        this.RightEyestalks.addChild(this.cube_r10);
        this.setRotationAngle(this.cube_r10, 0.0f, 0.0f, -0.6109f);
        this.cube_r10.texOffs(61, 29).addBox(-10.0f, -1.5f, -2.0f, 3.0f, 3.0f, 3.0f, 0.0f, false);
        this.cube_r10.texOffs(44, 38).addBox(-7.0f, -1.0f, -1.0f, 6.0f, 2.0f, 2.0f, 0.0f, false);
        (this.tentacles_0 = new ModelRenderer(this)).setPos(-3.8f, 7.0f, -5.0f);
        this.body.addChild(this.tentacles_0);
        (this.tentacles_1_r1 = new ModelRenderer(this)).setPos(0.0f, -22.0469f, -2.9417f);
        this.tentacles_0.addChild(this.tentacles_1_r1);
        this.setRotationAngle(this.tentacles_1_r1, 0.2618f, 0.0f, 0.0f);
        this.tentacles_1_r1.texOffs(63, 65).addBox(-1.5f, -2.4659f, -1.2412f, 3.0f, 3.0f, 3.0f, 0.0f, false);
        (this.tentacles_0_r1 = new ModelRenderer(this)).setPos(-0.2f, -14.0f, 0.0f);
        this.tentacles_0.addChild(this.tentacles_0_r1);
        this.setRotationAngle(this.tentacles_0_r1, 0.2618f, 0.0f, 0.0f);
        this.tentacles_0_r1.texOffs(46, 62).addBox(-0.8f, -8.0f, -1.0f, 2.0f, 9.0f, 2.0f, 0.0f, false);
        (this.tentacles_1 = new ModelRenderer(this)).setPos(1.3f, 7.0f, -5.0f);
        this.body.addChild(this.tentacles_1);
        (this.tentacles_2_r1 = new ModelRenderer(this)).setPos(0.0f, -24.3238f, 3.1091f);
        this.tentacles_1.addChild(this.tentacles_2_r1);
        this.setRotationAngle(this.tentacles_2_r1, -0.2182f, 0.0f, 0.0f);
        this.tentacles_2_r1.texOffs(24, 65).addBox(-1.5f, -2.2599f, -2.6927f, 3.0f, 3.0f, 3.0f, 0.0f, false);
        (this.tentacles_1_r2 = new ModelRenderer(this)).setPos(0.0f, -13.5f, 0.0f);
        this.tentacles_1.addChild(this.tentacles_1_r2);
        this.setRotationAngle(this.tentacles_1_r2, -0.2182f, 0.0f, 0.0f);
        this.tentacles_1_r2.texOffs(38, 53).addBox(-1.0f, -10.5f, -1.0f, 2.0f, 11.0f, 2.0f, 0.0f, false);
        (this.tentacles_2 = new ModelRenderer(this)).setPos(6.3f, 7.0f, -5.0f);
        this.body.addChild(this.tentacles_2);
        (this.tentacles_3_r1 = new ModelRenderer(this)).setPos(2.2f, -21.2104f, -0.5f);
        this.tentacles_2.addChild(this.tentacles_3_r1);
        this.setRotationAngle(this.tentacles_3_r1, 0.0f, 0.0f, 0.2618f);
        this.tentacles_3_r1.texOffs(64, 22).addBox(-1.7588f, -2.4659f, -1.5f, 3.0f, 3.0f, 3.0f, 0.0f, false);
        (this.tentacles_2_r2 = new ModelRenderer(this)).setPos(0.0f, -14.0f, 0.0f);
        this.tentacles_2.addChild(this.tentacles_2_r2);
        this.setRotationAngle(this.tentacles_2_r2, 0.0f, 0.0f, 0.2618f);
        this.tentacles_2_r2.texOffs(6, 66).addBox(-1.0f, -7.0f, -1.0f, 2.0f, 8.0f, 2.0f, 0.0f, false);
        (this.tentacles_3 = new ModelRenderer(this)).setPos(-6.3f, 7.0f, 0.0f);
        this.body.addChild(this.tentacles_3);
        (this.tentacles_4_r1 = new ModelRenderer(this)).setPos(-1.9101f, -23.3329f, -0.5f);
        this.tentacles_3.addChild(this.tentacles_4_r1);
        this.setRotationAngle(this.tentacles_4_r1, 0.0f, 0.0f, -0.1745f);
        this.tentacles_4_r1.texOffs(63, 10).addBox(-1.3264f, -2.4848f, -1.5f, 3.0f, 3.0f, 3.0f, 0.0f, false);
        (this.tentacles_3_r2 = new ModelRenderer(this)).setPos(0.0f, -13.5f, 0.0f);
        this.tentacles_3.addChild(this.tentacles_3_r2);
        this.setRotationAngle(this.tentacles_3_r2, 0.0f, 0.0f, -0.1745f);
        this.tentacles_3_r2.texOffs(16, 59).addBox(-1.0f, -9.5f, -1.0f, 2.0f, 9.0f, 2.0f, 0.0f, false);
        (this.tentacles_4 = new ModelRenderer(this)).setPos(-1.3f, 7.0f, 0.0f);
        this.body.addChild(this.tentacles_4);
        (this.tentacles_5_r1 = new ModelRenderer(this)).setPos(-0.6766f, -26.9978f, -2.9512f);
        this.tentacles_4.addChild(this.tentacles_5_r1);
        this.setRotationAngle(this.tentacles_5_r1, 0.2182f, 0.0f, -0.0436f);
        this.tentacles_5_r1.texOffs(63, 59).addBox(-1.4564f, -2.6918f, -2.2601f, 3.0f, 3.0f, 3.0f, 0.0f, false);
        (this.tentacles_4_r2 = new ModelRenderer(this)).setPos(0.0f, -12.5f, 0.0f);
        this.tentacles_4.addChild(this.tentacles_4_r2);
        this.setRotationAngle(this.tentacles_4_r2, 0.2182f, 0.0f, -0.0436f);
        this.tentacles_4_r2.texOffs(8, 0).addBox(-1.0f, -14.5f, -1.0f, 2.0f, 13.0f, 2.0f, 0.0f, false);
        (this.tentacles_5 = new ModelRenderer(this)).setPos(3.8f, 7.0f, 0.0f);
        this.body.addChild(this.tentacles_5);
        (this.tentacles_6_r1 = new ModelRenderer(this)).setPos(3.3406f, -23.9672f, -1.6311f);
        this.tentacles_5.addChild(this.tentacles_6_r1);
        this.setRotationAngle(this.tentacles_6_r1, 0.0873f, 0.0f, 0.2618f);
        this.tentacles_6_r1.texOffs(64, 16).addBox(-1.7588f, -2.4623f, -1.4158f, 3.0f, 3.0f, 3.0f, 0.0f, false);
        (this.tentacles_5_r2 = new ModelRenderer(this)).setPos(0.0f, -12.5f, 0.0f);
        this.tentacles_5.addChild(this.tentacles_5_r2);
        this.setRotationAngle(this.tentacles_5_r2, 0.0873f, 0.0f, 0.2618f);
        this.tentacles_5_r2.texOffs(8, 53).addBox(-1.0f, -11.5f, -1.0f, 2.0f, 11.0f, 2.0f, 0.0f, false);
        (this.tentacles_6 = new ModelRenderer(this)).setPos(-3.8f, 7.0f, 5.0f);
        this.body.addChild(this.tentacles_6);
        (this.tentacles_7_r1 = new ModelRenderer(this)).setPos(-3.7529f, -26.0059f, -0.5f);
        this.tentacles_6.addChild(this.tentacles_7_r1);
        this.setRotationAngle(this.tentacles_7_r1, 0.0f, 0.0f, -0.2618f);
        this.tentacles_7_r1.texOffs(63, 4).addBox(-1.2412f, -2.4659f, -1.5f, 3.0f, 3.0f, 3.0f, 0.0f, false);
        (this.tentacles_6_r2 = new ModelRenderer(this)).setPos(0.0f, -13.0f, 0.0f);
        this.tentacles_6.addChild(this.tentacles_6_r2);
        this.setRotationAngle(this.tentacles_6_r2, 0.0f, 0.0f, -0.2618f);
        this.tentacles_6_r2.texOffs(0, 53).addBox(-1.0f, -13.0f, -1.0f, 2.0f, 12.0f, 2.0f, 0.0f, false);
        (this.tentacles_7 = new ModelRenderer(this)).setPos(1.3f, 7.0f, 5.0f);
        this.body.addChild(this.tentacles_7);
        (this.tentacles_8_r1 = new ModelRenderer(this)).setPos(0.0f, -25.0255f, 3.5827f);
        this.tentacles_7.addChild(this.tentacles_8_r1);
        this.setRotationAngle(this.tentacles_8_r1, -0.3054f, 0.0f, 0.0f);
        this.tentacles_8_r1.texOffs(54, 62).addBox(-1.5f, -2.4537f, -1.8007f, 3.0f, 3.0f, 3.0f, 0.0f, false);
        (this.tentacles_7_r2 = new ModelRenderer(this)).setPos(0.0f, -13.0f, 0.0f);
        this.tentacles_7.addChild(this.tentacles_7_r2);
        this.setRotationAngle(this.tentacles_7_r2, -0.3054f, 0.0f, 0.0f);
        this.tentacles_7_r2.texOffs(30, 51).addBox(-1.0f, -12.0f, -1.0f, 2.0f, 12.0f, 2.0f, 0.0f, false);
        (this.tentacles_8 = new ModelRenderer(this)).setPos(6.3f, 7.0f, 5.0f);
        this.body.addChild(this.tentacles_8);
        (this.tentacles_9_r1 = new ModelRenderer(this)).setPos(3.0029f, -26.0453f, 1.9387f);
        this.tentacles_8.addChild(this.tentacles_9_r1);
        this.setRotationAngle(this.tentacles_9_r1, -0.1745f, 0.0f, 0.2182f);
        this.tentacles_9_r1.texOffs(62, 35).addBox(-1.7164f, -2.4615f, -1.6695f, 3.0f, 3.0f, 3.0f, 0.0f, false);
        (this.tentacles_8_r2 = new ModelRenderer(this)).setPos(0.0f, -13.5f, 0.0f);
        this.tentacles_8.addChild(this.tentacles_8_r2);
        this.setRotationAngle(this.tentacles_8_r2, -0.1745f, 0.0f, 0.2182f);
        this.tentacles_8_r2.texOffs(0, 0).addBox(-1.0f, -12.5f, -1.0f, 2.0f, 13.0f, 2.0f, 0.0f, false);
        this.tentacles.add(this.tentacles_4);
        this.tentacles.add(this.tentacles_1);
        this.tentacles.add(this.tentacles_5);
        this.tentacles.add(this.tentacles_0);
        this.tentacles.add(this.tentacles_7);
        this.tentacles.add(this.tentacles_6);
        this.tentacles.add(this.tentacles_8);
        this.tentacles.add(this.tentacles_3);
        this.tentacles.add(this.tentacles_2);
        segmentBuilder.addAll(this.tentacles);
        this.segments = segmentBuilder.build();
    }
    
    public void setupAnim(@Nonnull final EyesoreEntity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch) {
        final int tentaclesToRender = MathHelper.clamp(this.tentaclesRemaining, 0, 9);
        final UUID targetPlayer = (entity.getEntityData().get(EyesoreEntity.LASER_TARGET)).orElse(null);
        for (int i = 0; i < tentaclesToRender; ++i) {
            final ModelRenderer tentacle = this.tentacles.get(i);
            tentacle.visible = true;
            if (targetPlayer == null || entity.level.getPlayerByUUID(targetPlayer) == null) {
                tentacle.xRot = 0.1f * MathHelper.sin(ageInTicks * 0.3f + i);
            }
        }
        for (int i = tentaclesToRender; i < this.tentacles.size(); ++i) {
            final ModelRenderer tentacle = this.tentacles.get(i);
            tentacle.visible = false;
        }
        this.body.yRot = netHeadYaw * 0.017453292f;
        this.body.xRot = headPitch * 0.017453292f;
    }
    
    public void renderToBuffer(@Nonnull final MatrixStack matrixStack, @Nonnull final IVertexBuilder buffer, final int packedLight, final int packedOverlay, final float red, final float green, final float blue, final float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }
    
    public void setRotationAngle(final ModelRenderer modelRenderer, final float x, final float y, final float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
    
    @Nonnull
    public Iterable<ModelRenderer> parts() {
        return this.segments;
    }
}
