package iskallia.vault.block.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class OmegaStatueModel
        extends EntityModel<Entity> {
    private ModelRenderer crystal;
    private ModelRenderer crystal2;
    private ModelRenderer crystal3;
    private ModelRenderer crystal4;
    private ModelRenderer crystal5;
    private ModelRenderer crystal6;
    private ModelRenderer crystal7;
    private ModelRenderer crystal8;
    private ModelRenderer crystal9;
    private ModelRenderer crystal10;
    private ModelRenderer crystal11;
    private ModelRenderer crystal12;
    private ModelRenderer medcrystal;
    private ModelRenderer medcrystal2;
    private ModelRenderer medcrystal3;
    private ModelRenderer medcrystal4;
    private ModelRenderer medcrystal5;
    private ModelRenderer medcrystal6;
    private ModelRenderer medcrystal7;
    private ModelRenderer medcrystal8;
    private ModelRenderer medcrystal9;
    private ModelRenderer medcrystal10;
    private ModelRenderer medcrystal11;
    private ModelRenderer medcrystal12;
    private ModelRenderer medcrystal13;
    private ModelRenderer medcrystal14;
    private ModelRenderer largecrystal;
    private ModelRenderer largecrystal2;
    private ModelRenderer largecrystal3;
    private ModelRenderer largecrystal4;
    private ModelRenderer largecrystal5;
    private ModelRenderer largecrystal6;
    private ModelRenderer largecrystal7;
    private ModelRenderer largecrystal8;
    private ModelRenderer largecrystal9;
    private ModelRenderer largecrystal10;
    private ModelRenderer largecrystal11;
    private ModelRenderer largecrystal12;
    private ModelRenderer largecrystal13;
    private ModelRenderer largecrystal14;
    private ModelRenderer medcrystal15;
    private ModelRenderer medcrystal16;
    private ModelRenderer medcrystal17;
    private ModelRenderer medcrystal18;
    private ModelRenderer medcrystal19;
    private ModelRenderer medcrystal20;
    private ModelRenderer medcrystal21;
    private ModelRenderer medcrystal22;
    private ModelRenderer largecrystal15;
    private ModelRenderer largecrystal16;
    private ModelRenderer largecrystal17;
    private ModelRenderer largecrystal18;
    private ModelRenderer largecrystal19;
    private ModelRenderer largecrystal20;
    private ModelRenderer largecrystal21;
    private ModelRenderer largecrystal22;
    private ModelRenderer medcrystal23;
    private ModelRenderer crystal13;
    private ModelRenderer crystal14;
    private ModelRenderer crystal15;
    private ModelRenderer crystal16;
    private ModelRenderer crystal17;
    private ModelRenderer crystal18;
    private ModelRenderer largecrystal23;
    private ModelRenderer medcrystal24;
    private ModelRenderer medcrystal25;
    private ModelRenderer medcrystal26;
    private ModelRenderer crystal19;
    private ModelRenderer medcrystal27;
    private ModelRenderer crystal20;
    private ModelRenderer crystal21;
    private ModelRenderer medcrystal28;
    private ModelRenderer medcrystal29;
    private ModelRenderer medcrystal30;
    private ModelRenderer medcrystal31;
    private ModelRenderer medcrystal32;
    private ModelRenderer medcrystal33;
    private ModelRenderer medcrystal34;
    private ModelRenderer medcrystal35;
    private ModelRenderer medcrystal36;
    private ModelRenderer medcrystal37;
    private ModelRenderer medcrystal38;
    private ModelRenderer medcrystal39;
    private ModelRenderer largecrystal24;
    private ModelRenderer largecrystal25;
    private ModelRenderer medcrystal40;
    private ModelRenderer medcrystal41;
    private ModelRenderer largecrystal26;
    private ModelRenderer largecrystal27;
    private ModelRenderer crystal22;
    private ModelRenderer crystal23;
    private ModelRenderer crystal24;
    private ModelRenderer crystal25;
    private ModelRenderer largecrystal28;
    private ModelRenderer medcrystal42;
    private ModelRenderer medcrystal43;
    private ModelRenderer ground2;
    private ModelRenderer ground3;
    private ModelRenderer ground4;
    private ModelRenderer ground5;
    private ModelRenderer ground6;
    private ModelRenderer ground7;
    private ModelRenderer ground8;
    private ModelRenderer ground9;
    private ModelRenderer ground10;
    private ModelRenderer medcrystal44;
    private ModelRenderer medcrystal45;
    private ModelRenderer medcrystal46;
    private ModelRenderer medcrystal47;
    private ModelRenderer largecrystal29;
    private ModelRenderer largecrystal30;
    private ModelRenderer largecrystal31;
    private ModelRenderer largecrystal32;
    private ModelRenderer medcrystal48;
    private ModelRenderer medcrystal49;
    private ModelRenderer smallchunk;
    private ModelRenderer smallchunk2;
    private ModelRenderer smallchunk3;
    private ModelRenderer smallchunk4;
    private ModelRenderer smallchunk5;
    private ModelRenderer smallchunk6;
    private ModelRenderer smallchunk7;
    private ModelRenderer smallchunk8;
    private ModelRenderer smallchunk9;
    private ModelRenderer smallchunk10;
    private ModelRenderer smallchunk11;
    private ModelRenderer smallchunk12;
    private ModelRenderer smallchunk13;
    private ModelRenderer smallchunk14;
    private ModelRenderer smallchunk15;
    private ModelRenderer smallchunk16;
    private ModelRenderer smallchunk17;
    private ModelRenderer medcrystal50;
    private ModelRenderer medcrystal51;
    private ModelRenderer bb_main;

    public OmegaStatueModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        initCrystals();
        initGround();
        initMediumCrystals();
        initSmallChunks();
    }


    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }


    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.crystal.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal2.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal3.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal4.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal5.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal6.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal7.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal8.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal9.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal10.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal11.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal12.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal2.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal3.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal4.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal5.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal6.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal7.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal8.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal9.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal10.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal11.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal12.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal13.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal14.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal2.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal3.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal4.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal5.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal6.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal7.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal8.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal9.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal10.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal11.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal12.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal13.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal14.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal15.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal16.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal17.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal18.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal19.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal20.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal21.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal22.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal15.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal16.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal17.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal18.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal19.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal20.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal21.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal22.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal23.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal13.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal14.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal15.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal16.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal17.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal18.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal23.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal24.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal25.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal26.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal19.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal27.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal20.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal21.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal28.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal29.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal30.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal31.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal32.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal33.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal34.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal35.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal36.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal37.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal38.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal39.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal24.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal25.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal40.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal41.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal26.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal27.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal22.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal23.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal24.render(matrixStack, buffer, packedLight, packedOverlay);
        this.crystal25.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal28.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal42.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal43.render(matrixStack, buffer, packedLight, packedOverlay);
        this.ground2.render(matrixStack, buffer, packedLight, packedOverlay);
        this.ground3.render(matrixStack, buffer, packedLight, packedOverlay);
        this.ground4.render(matrixStack, buffer, packedLight, packedOverlay);
        this.ground5.render(matrixStack, buffer, packedLight, packedOverlay);
        this.ground6.render(matrixStack, buffer, packedLight, packedOverlay);
        this.ground7.render(matrixStack, buffer, packedLight, packedOverlay);
        this.ground8.render(matrixStack, buffer, packedLight, packedOverlay);
        this.ground9.render(matrixStack, buffer, packedLight, packedOverlay);
        this.ground10.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal44.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal45.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal46.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal47.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal29.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal30.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal31.render(matrixStack, buffer, packedLight, packedOverlay);
        this.largecrystal32.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal48.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal49.render(matrixStack, buffer, packedLight, packedOverlay);
        this.smallchunk.render(matrixStack, buffer, packedLight, packedOverlay);
        this.smallchunk2.render(matrixStack, buffer, packedLight, packedOverlay);
        this.smallchunk3.render(matrixStack, buffer, packedLight, packedOverlay);
        this.smallchunk4.render(matrixStack, buffer, packedLight, packedOverlay);
        this.smallchunk5.render(matrixStack, buffer, packedLight, packedOverlay);
        this.smallchunk6.render(matrixStack, buffer, packedLight, packedOverlay);
        this.smallchunk7.render(matrixStack, buffer, packedLight, packedOverlay);
        this.smallchunk8.render(matrixStack, buffer, packedLight, packedOverlay);
        this.smallchunk9.render(matrixStack, buffer, packedLight, packedOverlay);
        this.smallchunk10.render(matrixStack, buffer, packedLight, packedOverlay);
        this.smallchunk11.render(matrixStack, buffer, packedLight, packedOverlay);
        this.smallchunk12.render(matrixStack, buffer, packedLight, packedOverlay);
        this.smallchunk13.render(matrixStack, buffer, packedLight, packedOverlay);
        this.smallchunk14.render(matrixStack, buffer, packedLight, packedOverlay);
        this.smallchunk15.render(matrixStack, buffer, packedLight, packedOverlay);
        this.smallchunk16.render(matrixStack, buffer, packedLight, packedOverlay);
        this.smallchunk17.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal50.render(matrixStack, buffer, packedLight, packedOverlay);
        this.medcrystal51.render(matrixStack, buffer, packedLight, packedOverlay);
        this.bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }


    private void initCrystals() {
        this.crystal = new ModelRenderer((Model) this);
        this.crystal.setPos(-19.0F, 19.0F, -24.0F);
        this.crystal.texOffs(14, 2).addBox(-1.0F, -8.75F, 1.0F, 2.0F, 12.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r1 = new ModelRenderer((Model) this);
        cube_r1.setPos(0.0F, 0.0F, 0.0F);
        this.crystal.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.4363F, 0.0F, 0.0F);
        cube_r1.texOffs(14, 2).addBox(-1.0F, -6.75F, -1.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r2 = new ModelRenderer((Model) this);
        cube_r2.setPos(0.0F, -1.0F, 4.0F);
        this.crystal.addChild(cube_r2);
        setRotationAngle(cube_r2, -0.3927F, 0.0F, 0.0F);
        cube_r2.texOffs(14, 2).addBox(-1.0F, -5.75F, -1.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r3 = new ModelRenderer((Model) this);
        cube_r3.setPos(1.25F, 0.25F, 2.0F);
        this.crystal.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.0F, 0.4363F);
        cube_r3.texOffs(14, 2).addBox(-0.25F, -6.75F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r4 = new ModelRenderer((Model) this);
        cube_r4.setPos(-3.0F, -4.75F, 2.0F);
        this.crystal.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, -0.3491F);
        cube_r4.texOffs(14, 2).addBox(-1.75F, -1.25F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);

        this.crystal2 = new ModelRenderer((Model) this);
        this.crystal2.setPos(21.0F, 19.0F, -24.0F);
        this.crystal2.texOffs(28, 4).addBox(-1.0F, -9.25F, 1.0F, 2.0F, 13.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r5 = new ModelRenderer((Model) this);
        cube_r5.setPos(0.0F, 0.0F, 0.0F);
        this.crystal2.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.4363F, 0.0F, 0.0F);
        cube_r5.texOffs(28, 4).addBox(-1.0F, -7.25F, -1.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r6 = new ModelRenderer((Model) this);
        cube_r6.setPos(0.0F, -1.0F, 4.0F);
        this.crystal2.addChild(cube_r6);
        setRotationAngle(cube_r6, -0.3927F, 0.0F, 0.0F);
        cube_r6.texOffs(28, 4).addBox(-1.0F, -6.25F, -1.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r7 = new ModelRenderer((Model) this);
        cube_r7.setPos(1.25F, 0.25F, 2.0F);
        this.crystal2.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 0.0F, 0.4363F);
        cube_r7.texOffs(28, 4).addBox(-0.25F, -7.25F, -1.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r8 = new ModelRenderer((Model) this);
        cube_r8.setPos(-3.0F, -4.75F, 2.0F);
        this.crystal2.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, 0.0F, -0.3491F);
        cube_r8.texOffs(28, 4).addBox(-1.75F, -1.75F, -1.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);

        this.crystal3 = new ModelRenderer((Model) this);
        this.crystal3.setPos(15.0F, 19.0F, -22.0F);
        this.crystal3.texOffs(28, 4).addBox(-1.0F, -4.75F, 1.0F, 1.0F, 8.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r9 = new ModelRenderer((Model) this);
        cube_r9.setPos(0.0F, 0.0F, 0.0F);
        this.crystal3.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.4363F, 0.0F, 0.0F);
        cube_r9.texOffs(28, 4).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r10 = new ModelRenderer((Model) this);
        cube_r10.setPos(0.0F, -1.0F, 4.0F);
        this.crystal3.addChild(cube_r10);
        setRotationAngle(cube_r10, -0.3927F, 0.0F, 0.0F);
        cube_r10.texOffs(28, 4).addBox(-1.0F, -1.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r11 = new ModelRenderer((Model) this);
        cube_r11.setPos(1.25F, 0.25F, 2.0F);
        this.crystal3.addChild(cube_r11);
        setRotationAngle(cube_r11, 0.0F, 0.0F, 0.4363F);
        cube_r11.texOffs(28, 4).addBox(-0.25F, -2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r12 = new ModelRenderer((Model) this);
        cube_r12.setPos(-3.0F, -4.75F, 2.0F);
        this.crystal3.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.0F, 0.0F, -0.3491F);
        cube_r12.texOffs(28, 4).addBox(-1.75F, 2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        this.crystal4 = new ModelRenderer((Model) this);
        this.crystal4.setPos(-13.0F, 19.0F, -22.0F);
        this.crystal4.texOffs(14, 2).addBox(-1.0F, -4.75F, 1.0F, 1.0F, 8.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r13 = new ModelRenderer((Model) this);
        cube_r13.setPos(0.0F, 0.0F, 0.0F);
        this.crystal4.addChild(cube_r13);
        setRotationAngle(cube_r13, 0.4363F, 0.0F, 0.0F);
        cube_r13.texOffs(14, 2).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r14 = new ModelRenderer((Model) this);
        cube_r14.setPos(0.0F, -1.0F, 4.0F);
        this.crystal4.addChild(cube_r14);
        setRotationAngle(cube_r14, -0.3927F, 0.0F, 0.0F);
        cube_r14.texOffs(14, 2).addBox(-1.0F, -1.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r15 = new ModelRenderer((Model) this);
        cube_r15.setPos(1.25F, 0.25F, 2.0F);
        this.crystal4.addChild(cube_r15);
        setRotationAngle(cube_r15, 0.0F, 0.0F, 0.4363F);
        cube_r15.texOffs(14, 2).addBox(-0.25F, -2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r16 = new ModelRenderer((Model) this);
        cube_r16.setPos(-3.0F, -4.75F, 2.0F);
        this.crystal4.addChild(cube_r16);
        setRotationAngle(cube_r16, 0.0F, 0.0F, -0.3491F);
        cube_r16.texOffs(14, 2).addBox(-1.75F, 2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        this.crystal5 = new ModelRenderer((Model) this);
        this.crystal5.setPos(-21.0F, 19.0F, -18.0F);
        this.crystal5.texOffs(14, 2).addBox(-1.0F, -4.75F, 1.0F, 1.0F, 8.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r17 = new ModelRenderer((Model) this);
        cube_r17.setPos(0.0F, 0.0F, 0.0F);
        this.crystal5.addChild(cube_r17);
        setRotationAngle(cube_r17, 0.4363F, 0.0F, 0.0F);
        cube_r17.texOffs(14, 2).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r18 = new ModelRenderer((Model) this);
        cube_r18.setPos(0.0F, -1.0F, 4.0F);
        this.crystal5.addChild(cube_r18);
        setRotationAngle(cube_r18, -0.3927F, 0.0F, 0.0F);
        cube_r18.texOffs(14, 2).addBox(-1.0F, -1.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r19 = new ModelRenderer((Model) this);
        cube_r19.setPos(1.25F, 0.25F, 2.0F);
        this.crystal5.addChild(cube_r19);
        setRotationAngle(cube_r19, 0.0F, 0.0F, 0.4363F);
        cube_r19.texOffs(14, 2).addBox(-0.25F, -2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r20 = new ModelRenderer((Model) this);
        cube_r20.setPos(-3.0F, -4.75F, 2.0F);
        this.crystal5.addChild(cube_r20);
        setRotationAngle(cube_r20, 0.0F, 0.0F, -0.3491F);
        cube_r20.texOffs(14, 2).addBox(-1.75F, 2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        this.crystal6 = new ModelRenderer((Model) this);
        this.crystal6.setPos(21.0F, 19.0F, -18.0F);
        this.crystal6.texOffs(28, 4).addBox(-1.0F, -4.75F, 1.0F, 1.0F, 8.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r21 = new ModelRenderer((Model) this);
        cube_r21.setPos(0.0F, 0.0F, 0.0F);
        this.crystal6.addChild(cube_r21);
        setRotationAngle(cube_r21, 0.4363F, 0.0F, 0.0F);
        cube_r21.texOffs(28, 4).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r22 = new ModelRenderer((Model) this);
        cube_r22.setPos(0.0F, -1.0F, 4.0F);
        this.crystal6.addChild(cube_r22);
        setRotationAngle(cube_r22, -0.3927F, 0.0F, 0.0F);
        cube_r22.texOffs(28, 4).addBox(-1.0F, -1.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r23 = new ModelRenderer((Model) this);
        cube_r23.setPos(1.25F, 0.25F, 2.0F);
        this.crystal6.addChild(cube_r23);
        setRotationAngle(cube_r23, 0.0F, 0.0F, 0.4363F);
        cube_r23.texOffs(28, 4).addBox(-0.25F, -2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r24 = new ModelRenderer((Model) this);
        cube_r24.setPos(-3.0F, -4.75F, 2.0F);
        this.crystal6.addChild(cube_r24);
        setRotationAngle(cube_r24, 0.0F, 0.0F, -0.3491F);
        cube_r24.texOffs(28, 4).addBox(-1.75F, 2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        this.crystal7 = new ModelRenderer((Model) this);
        this.crystal7.setPos(-21.0F, 19.0F, 20.0F);
        this.crystal7.texOffs(0, 0).addBox(-1.0F, -9.5F, 1.0F, 2.0F, 13.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r25 = new ModelRenderer((Model) this);
        cube_r25.setPos(0.0F, 0.0F, 0.0F);
        this.crystal7.addChild(cube_r25);
        setRotationAngle(cube_r25, 0.4363F, 0.0F, 0.0F);
        cube_r25.texOffs(2, 2).addBox(-1.0F, -7.5F, -1.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r26 = new ModelRenderer((Model) this);
        cube_r26.setPos(0.0F, -1.0F, 4.0F);
        this.crystal7.addChild(cube_r26);
        setRotationAngle(cube_r26, -0.3927F, 0.0F, 0.0F);
        cube_r26.texOffs(3, 3).addBox(-1.0F, -6.5F, -1.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r27 = new ModelRenderer((Model) this);
        cube_r27.setPos(1.25F, 0.25F, 2.0F);
        this.crystal7.addChild(cube_r27);
        setRotationAngle(cube_r27, 0.0F, 0.0F, 0.4363F);
        cube_r27.texOffs(2, 2).addBox(-0.25F, -7.5F, -1.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r28 = new ModelRenderer((Model) this);
        cube_r28.setPos(-3.0F, -4.75F, 2.0F);
        this.crystal7.addChild(cube_r28);
        setRotationAngle(cube_r28, 0.0F, 0.0F, -0.3491F);
        cube_r28.texOffs(2, 2).addBox(-1.75F, -2.0F, -1.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);

        this.crystal8 = new ModelRenderer((Model) this);
        this.crystal8.setPos(21.0F, 19.0F, 20.0F);
        this.crystal8.texOffs(38, 5).addBox(-1.0F, -9.25F, 1.0F, 2.0F, 13.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r29 = new ModelRenderer((Model) this);
        cube_r29.setPos(0.0F, 0.0F, 0.0F);
        this.crystal8.addChild(cube_r29);
        setRotationAngle(cube_r29, 0.4363F, 0.0F, 0.0F);
        cube_r29.texOffs(38, 5).addBox(-1.0F, -7.25F, -1.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r30 = new ModelRenderer((Model) this);
        cube_r30.setPos(0.0F, -1.0F, 4.0F);
        this.crystal8.addChild(cube_r30);
        setRotationAngle(cube_r30, -0.3927F, 0.0F, 0.0F);
        cube_r30.texOffs(38, 5).addBox(-1.0F, -6.25F, -1.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r31 = new ModelRenderer((Model) this);
        cube_r31.setPos(1.25F, 0.25F, 2.0F);
        this.crystal8.addChild(cube_r31);
        setRotationAngle(cube_r31, 0.0F, 0.0F, 0.4363F);
        cube_r31.texOffs(38, 5).addBox(-0.25F, -7.25F, -1.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r32 = new ModelRenderer((Model) this);
        cube_r32.setPos(-3.0F, -4.75F, 2.0F);
        this.crystal8.addChild(cube_r32);
        setRotationAngle(cube_r32, 0.0F, 0.0F, -0.3491F);
        cube_r32.texOffs(38, 5).addBox(-1.75F, -1.75F, -1.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);

        this.crystal9 = new ModelRenderer((Model) this);
        this.crystal9.setPos(15.0F, 19.0F, 18.0F);
        this.crystal9.texOffs(38, 5).addBox(-1.0F, -4.75F, 1.0F, 1.0F, 8.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r33 = new ModelRenderer((Model) this);
        cube_r33.setPos(0.0F, 0.0F, 0.0F);
        this.crystal9.addChild(cube_r33);
        setRotationAngle(cube_r33, 0.4363F, 0.0F, 0.0F);
        cube_r33.texOffs(38, 5).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r34 = new ModelRenderer((Model) this);
        cube_r34.setPos(0.0F, -1.0F, 4.0F);
        this.crystal9.addChild(cube_r34);
        setRotationAngle(cube_r34, -0.3927F, 0.0F, 0.0F);
        cube_r34.texOffs(38, 5).addBox(-1.0F, -1.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r35 = new ModelRenderer((Model) this);
        cube_r35.setPos(1.25F, 0.25F, 2.0F);
        this.crystal9.addChild(cube_r35);
        setRotationAngle(cube_r35, 0.0F, 0.0F, 0.4363F);
        cube_r35.texOffs(38, 5).addBox(-0.25F, -2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r36 = new ModelRenderer((Model) this);
        cube_r36.setPos(-3.0F, -4.75F, 2.0F);
        this.crystal9.addChild(cube_r36);
        setRotationAngle(cube_r36, 0.0F, 0.0F, -0.3491F);
        cube_r36.texOffs(38, 5).addBox(-1.75F, 2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        this.crystal10 = new ModelRenderer((Model) this);
        this.crystal10.setPos(20.0F, 19.0F, 13.0F);
        this.crystal10.texOffs(38, 5).addBox(-1.0F, -4.75F, 1.0F, 1.0F, 8.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r37 = new ModelRenderer((Model) this);
        cube_r37.setPos(0.0F, 0.0F, 0.0F);
        this.crystal10.addChild(cube_r37);
        setRotationAngle(cube_r37, 0.4363F, 0.0F, 0.0F);
        cube_r37.texOffs(38, 5).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r38 = new ModelRenderer((Model) this);
        cube_r38.setPos(0.0F, -1.0F, 4.0F);
        this.crystal10.addChild(cube_r38);
        setRotationAngle(cube_r38, -0.3927F, 0.0F, 0.0F);
        cube_r38.texOffs(38, 5).addBox(-1.0F, -1.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r39 = new ModelRenderer((Model) this);
        cube_r39.setPos(1.25F, 0.25F, 2.0F);
        this.crystal10.addChild(cube_r39);
        setRotationAngle(cube_r39, 0.0F, 0.0F, 0.4363F);
        cube_r39.texOffs(38, 5).addBox(-0.25F, -2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r40 = new ModelRenderer((Model) this);
        cube_r40.setPos(-3.0F, -4.75F, 2.0F);
        this.crystal10.addChild(cube_r40);
        setRotationAngle(cube_r40, 0.0F, 0.0F, -0.3491F);
        cube_r40.texOffs(38, 5).addBox(-1.75F, 2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        this.crystal11 = new ModelRenderer((Model) this);
        this.crystal11.setPos(-19.0F, 19.0F, 13.0F);
        this.crystal11.texOffs(2, 2).addBox(-1.0F, -4.75F, 1.0F, 1.0F, 8.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r41 = new ModelRenderer((Model) this);
        cube_r41.setPos(0.0F, 0.0F, 0.0F);
        this.crystal11.addChild(cube_r41);
        setRotationAngle(cube_r41, 0.4363F, 0.0F, 0.0F);
        cube_r41.texOffs(2, 2).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r42 = new ModelRenderer((Model) this);
        cube_r42.setPos(0.0F, -1.0F, 4.0F);
        this.crystal11.addChild(cube_r42);
        setRotationAngle(cube_r42, -0.3927F, 0.0F, 0.0F);
        cube_r42.texOffs(2, 2).addBox(-1.0F, -1.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r43 = new ModelRenderer((Model) this);
        cube_r43.setPos(1.25F, 0.25F, 2.0F);
        this.crystal11.addChild(cube_r43);
        setRotationAngle(cube_r43, 0.0F, 0.0F, 0.4363F);
        cube_r43.texOffs(2, 2).addBox(-0.25F, -2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r44 = new ModelRenderer((Model) this);
        cube_r44.setPos(-3.0F, -4.75F, 2.0F);
        this.crystal11.addChild(cube_r44);
        setRotationAngle(cube_r44, 0.0F, 0.0F, -0.3491F);
        cube_r44.texOffs(2, 2).addBox(-1.75F, 2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        this.crystal12 = new ModelRenderer((Model) this);
        this.crystal12.setPos(-14.0F, 19.0F, 20.5F);
        this.crystal12.texOffs(2, 2).addBox(-1.0F, -4.75F, 1.0F, 1.0F, 8.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r45 = new ModelRenderer((Model) this);
        cube_r45.setPos(0.0F, 0.0F, 0.0F);
        this.crystal12.addChild(cube_r45);
        setRotationAngle(cube_r45, 0.4363F, 0.0F, 0.0F);
        cube_r45.texOffs(2, 2).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r46 = new ModelRenderer((Model) this);
        cube_r46.setPos(0.0F, -1.0F, 4.0F);
        this.crystal12.addChild(cube_r46);
        setRotationAngle(cube_r46, -0.3927F, 0.0F, 0.0F);
        cube_r46.texOffs(4, 1).addBox(-1.0F, -1.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r47 = new ModelRenderer((Model) this);
        cube_r47.setPos(1.25F, 0.25F, 2.0F);
        this.crystal12.addChild(cube_r47);
        setRotationAngle(cube_r47, 0.0F, 0.0F, 0.4363F);
        cube_r47.texOffs(2, 2).addBox(-0.25F, -2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r48 = new ModelRenderer((Model) this);
        cube_r48.setPos(-3.0F, -4.75F, 2.0F);
        this.crystal12.addChild(cube_r48);
        setRotationAngle(cube_r48, 0.0F, 0.0F, -0.3491F);
        cube_r48.texOffs(2, 2).addBox(-1.75F, 2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        this.medcrystal = new ModelRenderer((Model) this);
        this.medcrystal.setPos(-4.5F, 20.25F, 19.75F);
        setRotationAngle(this.medcrystal, 0.0F, -0.9599F, 0.0F);


        ModelRenderer cube_r49 = new ModelRenderer((Model) this);
        cube_r49.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal.addChild(cube_r49);
        setRotationAngle(cube_r49, 0.0F, 0.0F, 0.2618F);
        cube_r49.texOffs(2, 2).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r50 = new ModelRenderer((Model) this);
        cube_r50.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal.addChild(cube_r50);
        setRotationAngle(cube_r50, 0.0F, 0.0F, -0.2182F);
        cube_r50.texOffs(2, 2).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal2 = new ModelRenderer((Model) this);
        this.medcrystal2.setPos(-10.75F, 20.25F, 23.0F);
        setRotationAngle(this.medcrystal2, 0.0F, 0.48F, 0.0F);


        ModelRenderer cube_r51 = new ModelRenderer((Model) this);
        cube_r51.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal2.addChild(cube_r51);
        setRotationAngle(cube_r51, 0.0F, 0.0F, 0.2618F);
        cube_r51.texOffs(2, 2).addBox(0.5F, -4.25F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r52 = new ModelRenderer((Model) this);
        cube_r52.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal2.addChild(cube_r52);
        setRotationAngle(cube_r52, 0.0F, 0.0F, -0.2182F);
        cube_r52.texOffs(2, 2).addBox(-0.75F, -5.25F, -0.75F, 1.0F, 7.0F, 1.0F, 0.0F, false);

        this.medcrystal3 = new ModelRenderer((Model) this);
        this.medcrystal3.setPos(22.25F, 20.25F, -19.0F);


        ModelRenderer cube_r53 = new ModelRenderer((Model) this);
        cube_r53.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal3.addChild(cube_r53);
        setRotationAngle(cube_r53, 0.0F, 0.0F, 0.2618F);
        cube_r53.texOffs(28, 4).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r54 = new ModelRenderer((Model) this);
        cube_r54.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal3.addChild(cube_r54);
        setRotationAngle(cube_r54, 0.0F, 0.0F, -0.2182F);
        cube_r54.texOffs(28, 4).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal4 = new ModelRenderer((Model) this);
        this.medcrystal4.setPos(22.25F, 20.25F, 18.0F);


        ModelRenderer cube_r55 = new ModelRenderer((Model) this);
        cube_r55.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal4.addChild(cube_r55);
        setRotationAngle(cube_r55, 0.0F, 0.0F, 0.2618F);
        cube_r55.texOffs(38, 5).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r56 = new ModelRenderer((Model) this);
        cube_r56.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal4.addChild(cube_r56);
        setRotationAngle(cube_r56, 0.0F, 0.0F, -0.2182F);
        cube_r56.texOffs(38, 5).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal5 = new ModelRenderer((Model) this);
        this.medcrystal5.setPos(22.25F, 20.25F, 12.0F);
        setRotationAngle(this.medcrystal5, 0.0F, 1.1781F, 0.0F);


        ModelRenderer cube_r57 = new ModelRenderer((Model) this);
        cube_r57.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal5.addChild(cube_r57);
        setRotationAngle(cube_r57, 0.0F, 0.0F, 0.2618F);
        cube_r57.texOffs(38, 5).addBox(0.5F, -4.0F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r58 = new ModelRenderer((Model) this);
        cube_r58.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal5.addChild(cube_r58);
        setRotationAngle(cube_r58, 0.0F, 0.0F, -0.2182F);
        cube_r58.texOffs(38, 5).addBox(-0.75F, -5.0F, -0.75F, 1.0F, 7.0F, 1.0F, 0.0F, false);

        this.medcrystal6 = new ModelRenderer((Model) this);
        this.medcrystal6.setPos(22.75F, 20.25F, -13.0F);
        setRotationAngle(this.medcrystal6, 0.0F, -1.8762F, 0.0F);


        ModelRenderer cube_r59 = new ModelRenderer((Model) this);
        cube_r59.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal6.addChild(cube_r59);
        setRotationAngle(cube_r59, 0.0F, 0.0F, 0.2618F);
        cube_r59.texOffs(28, 4).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r60 = new ModelRenderer((Model) this);
        cube_r60.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal6.addChild(cube_r60);
        setRotationAngle(cube_r60, 0.0F, 0.0F, -0.2182F);
        cube_r60.texOffs(28, 4).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal7 = new ModelRenderer((Model) this);
        this.medcrystal7.setPos(11.75F, 20.25F, -23.0F);
        setRotationAngle(this.medcrystal7, 0.0F, -2.7925F, 0.0F);


        ModelRenderer cube_r61 = new ModelRenderer((Model) this);
        cube_r61.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal7.addChild(cube_r61);
        setRotationAngle(cube_r61, 0.0F, 0.0F, 0.2618F);
        cube_r61.texOffs(28, 4).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r62 = new ModelRenderer((Model) this);
        cube_r62.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal7.addChild(cube_r62);
        setRotationAngle(cube_r62, 0.0F, 0.0F, -0.2182F);
        cube_r62.texOffs(28, 4).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal8 = new ModelRenderer((Model) this);
        this.medcrystal8.setPos(-10.25F, 20.25F, -23.0F);
        setRotationAngle(this.medcrystal8, 0.0F, 2.3562F, 0.0F);


        ModelRenderer cube_r63 = new ModelRenderer((Model) this);
        cube_r63.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal8.addChild(cube_r63);
        setRotationAngle(cube_r63, 0.0F, 0.0F, 0.2618F);
        cube_r63.texOffs(14, 2).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r64 = new ModelRenderer((Model) this);
        cube_r64.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal8.addChild(cube_r64);
        setRotationAngle(cube_r64, 0.0F, 0.0F, -0.2182F);
        cube_r64.texOffs(14, 2).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal9 = new ModelRenderer((Model) this);
        this.medcrystal9.setPos(-15.25F, 20.25F, -23.0F);
        setRotationAngle(this.medcrystal9, 0.0F, 0.1745F, 0.0F);


        ModelRenderer cube_r65 = new ModelRenderer((Model) this);
        cube_r65.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal9.addChild(cube_r65);
        setRotationAngle(cube_r65, 0.0F, 0.0F, 0.2618F);
        cube_r65.texOffs(14, 2).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r66 = new ModelRenderer((Model) this);
        cube_r66.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal9.addChild(cube_r66);
        setRotationAngle(cube_r66, 0.0F, 0.0F, -0.2182F);
        cube_r66.texOffs(14, 2).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal10 = new ModelRenderer((Model) this);
        this.medcrystal10.setPos(-22.75F, 20.25F, -20.0F);
        setRotationAngle(this.medcrystal10, 0.0F, 1.4399F, 0.0F);


        ModelRenderer cube_r67 = new ModelRenderer((Model) this);
        cube_r67.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal10.addChild(cube_r67);
        setRotationAngle(cube_r67, 0.0F, 0.0F, 0.2618F);
        cube_r67.texOffs(14, 2).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r68 = new ModelRenderer((Model) this);
        cube_r68.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal10.addChild(cube_r68);
        setRotationAngle(cube_r68, 0.0F, 0.0F, -0.2182F);
        cube_r68.texOffs(14, 2).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal11 = new ModelRenderer((Model) this);
        this.medcrystal11.setPos(-22.75F, 20.25F, 19.0F);
        setRotationAngle(this.medcrystal11, 0.0F, 1.4399F, 0.0F);


        ModelRenderer cube_r69 = new ModelRenderer((Model) this);
        cube_r69.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal11.addChild(cube_r69);
        setRotationAngle(cube_r69, 0.0F, 0.0F, 0.2618F);
        cube_r69.texOffs(2, 2).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r70 = new ModelRenderer((Model) this);
        cube_r70.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal11.addChild(cube_r70);
        setRotationAngle(cube_r70, 0.0F, 0.0F, -0.2182F);
        cube_r70.texOffs(2, 2).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal12 = new ModelRenderer((Model) this);
        this.medcrystal12.setPos(-16.75F, 20.25F, 19.0F);
        setRotationAngle(this.medcrystal12, 0.0F, 1.4399F, 0.0F);


        ModelRenderer cube_r71 = new ModelRenderer((Model) this);
        cube_r71.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal12.addChild(cube_r71);
        setRotationAngle(cube_r71, 0.0F, 0.0F, 0.2618F);
        cube_r71.texOffs(2, 2).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r72 = new ModelRenderer((Model) this);
        cube_r72.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal12.addChild(cube_r72);
        setRotationAngle(cube_r72, 0.0F, 0.0F, -0.2182F);
        cube_r72.texOffs(2, 2).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal13 = new ModelRenderer((Model) this);
        this.medcrystal13.setPos(17.25F, 20.25F, 18.0F);
        setRotationAngle(this.medcrystal13, 0.0F, 1.4399F, 0.0F);


        ModelRenderer cube_r73 = new ModelRenderer((Model) this);
        cube_r73.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal13.addChild(cube_r73);
        setRotationAngle(cube_r73, 0.0F, 0.0F, 0.2618F);
        cube_r73.texOffs(38, 5).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r74 = new ModelRenderer((Model) this);
        cube_r74.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal13.addChild(cube_r74);
        setRotationAngle(cube_r74, 0.0F, 0.0F, -0.2182F);
        cube_r74.texOffs(38, 5).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal14 = new ModelRenderer((Model) this);
        this.medcrystal14.setPos(17.25F, 20.25F, -17.0F);
        setRotationAngle(this.medcrystal14, 0.0F, 1.4399F, 0.0F);


        ModelRenderer cube_r75 = new ModelRenderer((Model) this);
        cube_r75.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal14.addChild(cube_r75);
        setRotationAngle(cube_r75, 0.0F, 0.0F, 0.2618F);
        cube_r75.texOffs(28, 4).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r76 = new ModelRenderer((Model) this);
        cube_r76.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal14.addChild(cube_r76);
        setRotationAngle(cube_r76, 0.0F, 0.0F, -0.2182F);
        cube_r76.texOffs(28, 4).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.largecrystal = new ModelRenderer((Model) this);
        this.largecrystal.setPos(-22.375F, 19.0F, 13.625F);
        this.largecrystal.texOffs(8, 2).addBox(-0.875F, -3.0F, -2.625F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r77 = new ModelRenderer((Model) this);
        cube_r77.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal.addChild(cube_r77);
        setRotationAngle(cube_r77, 0.3927F, 0.0F, 0.0F);
        cube_r77.texOffs(2, 2).addBox(-0.625F, -1.75F, -4.375F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r78 = new ModelRenderer((Model) this);
        cube_r78.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal.addChild(cube_r78);
        setRotationAngle(cube_r78, -0.3491F, 0.0F, 0.0F);
        cube_r78.texOffs(2, 2).addBox(-0.625F, -0.25F, -0.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.largecrystal2 = new ModelRenderer((Model) this);
        this.largecrystal2.setPos(18.625F, 19.0F, 13.625F);
        this.largecrystal2.texOffs(38, 5).addBox(-0.875F, -3.0F, -2.625F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r79 = new ModelRenderer((Model) this);
        cube_r79.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal2.addChild(cube_r79);
        setRotationAngle(cube_r79, 0.3927F, 0.0F, 0.0F);
        cube_r79.texOffs(38, 5).addBox(-0.625F, -1.75F, -4.375F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r80 = new ModelRenderer((Model) this);
        cube_r80.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal2.addChild(cube_r80);
        setRotationAngle(cube_r80, -0.3491F, 0.0F, 0.0F);
        cube_r80.texOffs(38, 5).addBox(-0.625F, -0.25F, -0.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.largecrystal3 = new ModelRenderer((Model) this);
        this.largecrystal3.setPos(19.625F, 19.0F, -11.375F);
        setRotationAngle(this.largecrystal3, 0.0F, 0.6545F, 0.0F);
        this.largecrystal3.texOffs(28, 4).addBox(-0.875F, -3.0F, -2.625F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r81 = new ModelRenderer((Model) this);
        cube_r81.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal3.addChild(cube_r81);
        setRotationAngle(cube_r81, 0.3927F, 0.0F, 0.0F);
        cube_r81.texOffs(28, 4).addBox(-0.625F, -1.75F, -4.375F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r82 = new ModelRenderer((Model) this);
        cube_r82.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal3.addChild(cube_r82);
        setRotationAngle(cube_r82, -0.3491F, 0.0F, 0.0F);
        cube_r82.texOffs(28, 4).addBox(-0.625F, -0.25F, -0.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.largecrystal4 = new ModelRenderer((Model) this);
        this.largecrystal4.setPos(-21.125F, 19.0F, -10.875F);
        setRotationAngle(this.largecrystal4, 0.0F, 0.6545F, 0.0F);
        this.largecrystal4.texOffs(14, 2).addBox(-0.875F, -3.0F, -2.625F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r83 = new ModelRenderer((Model) this);
        cube_r83.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal4.addChild(cube_r83);
        setRotationAngle(cube_r83, 0.3927F, 0.0F, 0.0F);
        cube_r83.texOffs(14, 2).addBox(-0.625F, -1.75F, -4.375F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r84 = new ModelRenderer((Model) this);
        cube_r84.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal4.addChild(cube_r84);
        setRotationAngle(cube_r84, -0.3491F, 0.0F, 0.0F);
        cube_r84.texOffs(14, 2).addBox(-0.625F, -0.25F, -0.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.largecrystal5 = new ModelRenderer((Model) this);
        this.largecrystal5.setPos(-6.125F, 19.0F, 23.875F);
        setRotationAngle(this.largecrystal5, 0.0F, 0.6545F, 0.0F);
        this.largecrystal5.texOffs(2, 2).addBox(-0.875F, -3.0F, -2.625F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r85 = new ModelRenderer((Model) this);
        cube_r85.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal5.addChild(cube_r85);
        setRotationAngle(cube_r85, 0.3927F, 0.0F, 0.0F);
        cube_r85.texOffs(2, 2).addBox(-0.625F, -1.75F, -4.375F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r86 = new ModelRenderer((Model) this);
        cube_r86.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal5.addChild(cube_r86);
        setRotationAngle(cube_r86, -0.3491F, 0.0F, 0.0F);
        cube_r86.texOffs(2, 2).addBox(-0.625F, -0.25F, -0.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.largecrystal6 = new ModelRenderer((Model) this);
        this.largecrystal6.setPos(13.625F, 19.0F, 23.875F);
        setRotationAngle(this.largecrystal6, 0.0F, 0.6545F, 0.0F);
        this.largecrystal6.texOffs(38, 5).addBox(-0.875F, -3.0F, -2.625F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r87 = new ModelRenderer((Model) this);
        cube_r87.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal6.addChild(cube_r87);
        setRotationAngle(cube_r87, 0.3927F, 0.0F, 0.0F);
        cube_r87.texOffs(38, 5).addBox(-0.625F, -1.75F, -4.375F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r88 = new ModelRenderer((Model) this);
        cube_r88.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal6.addChild(cube_r88);
        setRotationAngle(cube_r88, -0.3491F, 0.0F, 0.0F);
        cube_r88.texOffs(38, 5).addBox(-0.625F, -0.25F, -0.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.largecrystal7 = new ModelRenderer((Model) this);
        this.largecrystal7.setPos(-10.375F, 19.0F, 18.625F);
        setRotationAngle(this.largecrystal7, 0.0F, 1.3963F, 0.0F);
        this.largecrystal7.texOffs(2, 2).addBox(-0.875F, -3.0F, -2.625F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r89 = new ModelRenderer((Model) this);
        cube_r89.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal7.addChild(cube_r89);
        setRotationAngle(cube_r89, 0.3927F, 0.0F, 0.0F);
        cube_r89.texOffs(2, 2).addBox(-0.625F, -1.75F, -4.375F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r90 = new ModelRenderer((Model) this);
        cube_r90.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal7.addChild(cube_r90);
        setRotationAngle(cube_r90, -0.3491F, 0.0F, 0.0F);
        cube_r90.texOffs(2, 2).addBox(-0.625F, -0.25F, -0.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.largecrystal8 = new ModelRenderer((Model) this);
        this.largecrystal8.setPos(-16.125F, 19.0F, -15.625F);
        setRotationAngle(this.largecrystal8, 0.0F, 0.3491F, 0.0F);
        this.largecrystal8.texOffs(14, 2).addBox(-0.875F, -3.0F, -2.625F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r91 = new ModelRenderer((Model) this);
        cube_r91.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal8.addChild(cube_r91);
        setRotationAngle(cube_r91, 0.3927F, 0.0F, 0.0F);
        cube_r91.texOffs(14, 2).addBox(-0.625F, -1.75F, -4.375F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r92 = new ModelRenderer((Model) this);
        cube_r92.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal8.addChild(cube_r92);
        setRotationAngle(cube_r92, -0.3491F, 0.0F, 0.0F);
        cube_r92.texOffs(14, 2).addBox(-0.625F, -0.25F, -0.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.largecrystal9 = new ModelRenderer((Model) this);
        this.largecrystal9.setPos(12.875F, 19.0F, 16.375F);
        setRotationAngle(this.largecrystal9, 0.0F, -0.6545F, 0.0F);
        this.largecrystal9.texOffs(38, 5).addBox(-0.875F, -3.0F, -2.625F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r93 = new ModelRenderer((Model) this);
        cube_r93.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal9.addChild(cube_r93);
        setRotationAngle(cube_r93, 0.3927F, 0.0F, 0.0F);
        cube_r93.texOffs(38, 5).addBox(-0.625F, -1.75F, -4.375F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r94 = new ModelRenderer((Model) this);
        cube_r94.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal9.addChild(cube_r94);
        setRotationAngle(cube_r94, -0.3491F, 0.0F, 0.0F);
        cube_r94.texOffs(38, 5).addBox(-0.625F, -0.25F, -0.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.largecrystal10 = new ModelRenderer((Model) this);
        this.largecrystal10.setPos(8.875F, 19.0F, -18.125F);
        setRotationAngle(this.largecrystal10, 0.0F, -0.6545F, 0.0F);
        this.largecrystal10.texOffs(28, 4).addBox(-0.875F, -3.0F, -2.625F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r95 = new ModelRenderer((Model) this);
        cube_r95.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal10.addChild(cube_r95);
        setRotationAngle(cube_r95, 0.3927F, 0.0F, 0.0F);
        cube_r95.texOffs(28, 4).addBox(-0.625F, -1.75F, -4.375F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r96 = new ModelRenderer((Model) this);
        cube_r96.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal10.addChild(cube_r96);
        setRotationAngle(cube_r96, -0.3491F, 0.0F, 0.0F);
        cube_r96.texOffs(28, 4).addBox(-0.625F, -0.25F, -0.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.largecrystal11 = new ModelRenderer((Model) this);
        this.largecrystal11.setPos(5.625F, 19.0F, -21.875F);
        setRotationAngle(this.largecrystal11, 0.0F, -1.3963F, 0.0F);
        this.largecrystal11.texOffs(28, 4).addBox(-0.875F, -0.75F, -2.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r97 = new ModelRenderer((Model) this);
        cube_r97.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal11.addChild(cube_r97);
        setRotationAngle(cube_r97, 0.3927F, 0.0F, 0.0F);
        cube_r97.texOffs(28, 4).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r98 = new ModelRenderer((Model) this);
        cube_r98.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal11.addChild(cube_r98);
        setRotationAngle(cube_r98, -0.3491F, 0.0F, 0.0F);
        cube_r98.texOffs(28, 4).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.largecrystal12 = new ModelRenderer((Model) this);
        this.largecrystal12.setPos(-9.375F, 19.0F, -21.875F);
        setRotationAngle(this.largecrystal12, 0.0F, -1.3963F, 0.0F);
        this.largecrystal12.texOffs(14, 2).addBox(-0.875F, -0.75F, -2.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r99 = new ModelRenderer((Model) this);
        cube_r99.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal12.addChild(cube_r99);
        setRotationAngle(cube_r99, 0.3927F, 0.0F, 0.0F);
        cube_r99.texOffs(14, 2).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r100 = new ModelRenderer((Model) this);
        cube_r100.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal12.addChild(cube_r100);
        setRotationAngle(cube_r100, -0.3491F, 0.0F, 0.0F);
        cube_r100.texOffs(14, 2).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.largecrystal13 = new ModelRenderer((Model) this);
        this.largecrystal13.setPos(-9.375F, 19.0F, 19.125F);
        setRotationAngle(this.largecrystal13, 0.0F, -1.3963F, 0.0F);
        this.largecrystal13.texOffs(2, 2).addBox(-0.875F, -0.75F, -2.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r101 = new ModelRenderer((Model) this);
        cube_r101.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal13.addChild(cube_r101);
        setRotationAngle(cube_r101, 0.3927F, 0.0F, 0.0F);
        cube_r101.texOffs(0, 0).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r102 = new ModelRenderer((Model) this);
        cube_r102.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal13.addChild(cube_r102);
        setRotationAngle(cube_r102, -0.3491F, 0.0F, 0.0F);
        cube_r102.texOffs(2, 2).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.largecrystal14 = new ModelRenderer((Model) this);
        this.largecrystal14.setPos(8.625F, 19.0F, 19.625F);
        setRotationAngle(this.largecrystal14, 0.0F, -0.8727F, 0.0F);
        this.largecrystal14.texOffs(38, 5).addBox(-0.875F, -0.75F, -2.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r103 = new ModelRenderer((Model) this);
        cube_r103.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal14.addChild(cube_r103);
        setRotationAngle(cube_r103, 0.3927F, 0.0F, 0.0F);
        cube_r103.texOffs(38, 5).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r104 = new ModelRenderer((Model) this);
        cube_r104.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal14.addChild(cube_r104);
        setRotationAngle(cube_r104, -0.3491F, 0.0F, 0.0F);
        cube_r104.texOffs(38, 5).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.medcrystal15 = new ModelRenderer((Model) this);
        this.medcrystal15.setPos(9.5F, 20.25F, 22.5F);
        setRotationAngle(this.medcrystal15, 0.0F, -1.1781F, 0.0F);


        ModelRenderer cube_r105 = new ModelRenderer((Model) this);
        cube_r105.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal15.addChild(cube_r105);
        setRotationAngle(cube_r105, 0.0F, 0.0F, 0.2618F);
        cube_r105.texOffs(38, 5).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r106 = new ModelRenderer((Model) this);
        cube_r106.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal15.addChild(cube_r106);
        setRotationAngle(cube_r106, 0.0F, 0.0F, -0.2182F);
        cube_r106.texOffs(38, 5).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal16 = new ModelRenderer((Model) this);
        this.medcrystal16.setPos(11.5F, 20.25F, -17.5F);
        setRotationAngle(this.medcrystal16, 0.0F, -1.1781F, 0.0F);


        ModelRenderer cube_r107 = new ModelRenderer((Model) this);
        cube_r107.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal16.addChild(cube_r107);
        setRotationAngle(cube_r107, 0.0F, 0.0F, 0.2618F);
        cube_r107.texOffs(28, 4).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r108 = new ModelRenderer((Model) this);
        cube_r108.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal16.addChild(cube_r108);
        setRotationAngle(cube_r108, 0.0F, 0.0F, -0.2182F);
        cube_r108.texOffs(28, 4).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal17 = new ModelRenderer((Model) this);
        this.medcrystal17.setPos(11.5F, 20.25F, 14.5F);
        setRotationAngle(this.medcrystal17, 0.0F, -2.3126F, 0.0F);


        ModelRenderer cube_r109 = new ModelRenderer((Model) this);
        cube_r109.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal17.addChild(cube_r109);
        setRotationAngle(cube_r109, 0.0F, 0.0F, 0.2618F);
        cube_r109.texOffs(38, 5).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r110 = new ModelRenderer((Model) this);
        cube_r110.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal17.addChild(cube_r110);
        setRotationAngle(cube_r110, 0.0F, 0.0F, -0.2182F);
        cube_r110.texOffs(38, 5).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal18 = new ModelRenderer((Model) this);
        this.medcrystal18.setPos(-14.0F, 20.25F, 15.75F);
        setRotationAngle(this.medcrystal18, 0.0F, -2.3126F, 0.0F);


        ModelRenderer cube_r111 = new ModelRenderer((Model) this);
        cube_r111.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal18.addChild(cube_r111);
        setRotationAngle(cube_r111, 0.0F, 0.0F, 0.2618F);
        cube_r111.texOffs(2, 2).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r112 = new ModelRenderer((Model) this);
        cube_r112.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal18.addChild(cube_r112);
        setRotationAngle(cube_r112, 0.0F, 0.0F, -0.2182F);
        cube_r112.texOffs(2, 2).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal19 = new ModelRenderer((Model) this);
        this.medcrystal19.setPos(-14.0F, 20.25F, -15.25F);
        setRotationAngle(this.medcrystal19, 0.0F, -2.3126F, 0.0F);


        ModelRenderer cube_r113 = new ModelRenderer((Model) this);
        cube_r113.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal19.addChild(cube_r113);
        setRotationAngle(cube_r113, 0.0F, 0.0F, 0.2618F);
        cube_r113.texOffs(14, 2).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r114 = new ModelRenderer((Model) this);
        cube_r114.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal19.addChild(cube_r114);
        setRotationAngle(cube_r114, 0.0F, 0.0F, -0.2182F);
        cube_r114.texOffs(14, 2).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal20 = new ModelRenderer((Model) this);
        this.medcrystal20.setPos(-22.0F, 20.25F, -9.25F);
        setRotationAngle(this.medcrystal20, 0.0F, -2.3126F, 0.0F);


        ModelRenderer cube_r115 = new ModelRenderer((Model) this);
        cube_r115.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal20.addChild(cube_r115);
        setRotationAngle(cube_r115, 0.0F, 0.0F, 0.2618F);
        cube_r115.texOffs(14, 2).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r116 = new ModelRenderer((Model) this);
        cube_r116.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal20.addChild(cube_r116);
        setRotationAngle(cube_r116, 0.0F, 0.0F, -0.2182F);
        cube_r116.texOffs(14, 2).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal21 = new ModelRenderer((Model) this);
        this.medcrystal21.setPos(-22.0F, 20.25F, 7.75F);
        setRotationAngle(this.medcrystal21, 0.0F, -1.0472F, 0.0F);


        ModelRenderer cube_r117 = new ModelRenderer((Model) this);
        cube_r117.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal21.addChild(cube_r117);
        setRotationAngle(cube_r117, 0.0F, 0.0F, 0.2618F);
        cube_r117.texOffs(2, 2).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r118 = new ModelRenderer((Model) this);
        cube_r118.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal21.addChild(cube_r118);
        setRotationAngle(cube_r118, 0.0F, 0.0F, -0.2182F);
        cube_r118.texOffs(2, 2).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal22 = new ModelRenderer((Model) this);
        this.medcrystal22.setPos(21.75F, 20.25F, 8.25F);
        setRotationAngle(this.medcrystal22, 0.0F, -1.0472F, 0.0F);


        ModelRenderer cube_r119 = new ModelRenderer((Model) this);
        cube_r119.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal22.addChild(cube_r119);
        setRotationAngle(cube_r119, 0.0F, 0.0F, 0.2618F);
        cube_r119.texOffs(38, 5).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r120 = new ModelRenderer((Model) this);
        cube_r120.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal22.addChild(cube_r120);
        setRotationAngle(cube_r120, 0.0F, 0.0F, -0.2182F);
        cube_r120.texOffs(38, 5).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.largecrystal15 = new ModelRenderer((Model) this);
        this.largecrystal15.setPos(-19.375F, 19.0F, -12.875F);
        setRotationAngle(this.largecrystal15, 0.0F, -1.3963F, 0.0F);
        this.largecrystal15.texOffs(14, 2).addBox(-0.875F, -3.75F, -2.625F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r121 = new ModelRenderer((Model) this);
        cube_r121.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal15.addChild(cube_r121);
        setRotationAngle(cube_r121, 0.3927F, 0.0F, 0.0F);
        cube_r121.texOffs(14, 2).addBox(-0.625F, -3.75F, -4.375F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r122 = new ModelRenderer((Model) this);
        cube_r122.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal15.addChild(cube_r122);
        setRotationAngle(cube_r122, -0.3491F, 0.0F, 0.0F);
        cube_r122.texOffs(14, 2).addBox(-0.625F, -2.25F, -0.625F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.largecrystal16 = new ModelRenderer((Model) this);
        this.largecrystal16.setPos(-20.375F, 19.0F, 10.625F);
        setRotationAngle(this.largecrystal16, 0.0F, -1.3963F, 0.0F);
        this.largecrystal16.texOffs(2, 2).addBox(-0.875F, -3.75F, -2.625F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r123 = new ModelRenderer((Model) this);
        cube_r123.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal16.addChild(cube_r123);
        setRotationAngle(cube_r123, 0.3927F, 0.0F, 0.0F);
        cube_r123.texOffs(2, 2).addBox(-0.625F, -3.75F, -4.375F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r124 = new ModelRenderer((Model) this);
        cube_r124.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal16.addChild(cube_r124);
        setRotationAngle(cube_r124, -0.3491F, 0.0F, 0.0F);
        cube_r124.texOffs(2, 2).addBox(-0.625F, -2.25F, -0.625F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.largecrystal17 = new ModelRenderer((Model) this);
        this.largecrystal17.setPos(11.625F, 19.0F, 11.625F);
        setRotationAngle(this.largecrystal17, 0.0F, -1.3963F, 0.0F);
        this.largecrystal17.texOffs(38, 5).addBox(-0.875F, -3.75F, -2.625F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r125 = new ModelRenderer((Model) this);
        cube_r125.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal17.addChild(cube_r125);
        setRotationAngle(cube_r125, 0.3927F, 0.0F, 0.0F);
        cube_r125.texOffs(38, 5).addBox(-0.625F, -3.75F, -4.375F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r126 = new ModelRenderer((Model) this);
        cube_r126.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal17.addChild(cube_r126);
        setRotationAngle(cube_r126, -0.3491F, 0.0F, 0.0F);
        cube_r126.texOffs(38, 5).addBox(-0.625F, -2.25F, -0.625F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.largecrystal18 = new ModelRenderer((Model) this);
        this.largecrystal18.setPos(22.625F, 19.0F, 7.625F);
        setRotationAngle(this.largecrystal18, 0.0F, 0.3491F, 0.0F);
        this.largecrystal18.texOffs(38, 5).addBox(-0.875F, -0.75F, -2.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r127 = new ModelRenderer((Model) this);
        cube_r127.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal18.addChild(cube_r127);
        setRotationAngle(cube_r127, 0.3927F, 0.0F, 0.0F);
        cube_r127.texOffs(38, 5).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r128 = new ModelRenderer((Model) this);
        cube_r128.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal18.addChild(cube_r128);
        setRotationAngle(cube_r128, -0.3491F, 0.0F, 0.0F);
        cube_r128.texOffs(38, 5).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.largecrystal19 = new ModelRenderer((Model) this);
        this.largecrystal19.setPos(22.625F, 19.0F, -7.375F);
        setRotationAngle(this.largecrystal19, 0.0F, -0.1745F, 0.0F);
        this.largecrystal19.texOffs(28, 4).addBox(-0.875F, -0.75F, -2.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r129 = new ModelRenderer((Model) this);
        cube_r129.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal19.addChild(cube_r129);
        setRotationAngle(cube_r129, 0.3927F, 0.0F, 0.0F);
        cube_r129.texOffs(28, 4).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r130 = new ModelRenderer((Model) this);
        cube_r130.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal19.addChild(cube_r130);
        setRotationAngle(cube_r130, -0.3491F, 0.0F, 0.0F);
        cube_r130.texOffs(28, 4).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.largecrystal20 = new ModelRenderer((Model) this);
        this.largecrystal20.setPos(-22.375F, 19.0F, -4.375F);
        setRotationAngle(this.largecrystal20, 0.0F, -0.1745F, 0.0F);
        this.largecrystal20.texOffs(14, 2).addBox(-0.875F, -0.75F, -2.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r131 = new ModelRenderer((Model) this);
        cube_r131.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal20.addChild(cube_r131);
        setRotationAngle(cube_r131, 0.3927F, 0.0F, 0.0F);
        cube_r131.texOffs(14, 2).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r132 = new ModelRenderer((Model) this);
        cube_r132.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal20.addChild(cube_r132);
        setRotationAngle(cube_r132, -0.3491F, 0.0F, 0.0F);
        cube_r132.texOffs(14, 2).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.largecrystal21 = new ModelRenderer((Model) this);
        this.largecrystal21.setPos(6.625F, 20.2917F, 22.25F);
        setRotationAngle(this.largecrystal21, 0.0F, -1.309F, 0.0F);
        this.largecrystal21.texOffs(38, 5).addBox(-0.875F, -2.0417F, -0.75F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r133 = new ModelRenderer((Model) this);
        cube_r133.setPos(0.0F, -1.2917F, 1.875F);
        this.largecrystal21.addChild(cube_r133);
        setRotationAngle(cube_r133, 0.3927F, 0.0F, 0.0F);
        cube_r133.texOffs(38, 5).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r134 = new ModelRenderer((Model) this);
        cube_r134.setPos(0.0F, -1.2917F, 1.875F);
        this.largecrystal21.addChild(cube_r134);
        setRotationAngle(cube_r134, -0.3491F, 0.0F, 0.0F);
        cube_r134.texOffs(38, 5).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.largecrystal22 = new ModelRenderer((Model) this);
        this.largecrystal22.setPos(-4.375F, 20.2917F, 22.25F);
        setRotationAngle(this.largecrystal22, 0.0F, -1.7453F, 0.0F);
        this.largecrystal22.texOffs(2, 2).addBox(-0.875F, -2.0417F, -0.75F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r135 = new ModelRenderer((Model) this);
        cube_r135.setPos(0.0F, -1.2917F, 1.875F);
        this.largecrystal22.addChild(cube_r135);
        setRotationAngle(cube_r135, 0.3927F, 0.0F, 0.0F);
        cube_r135.texOffs(2, 2).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r136 = new ModelRenderer((Model) this);
        cube_r136.setPos(0.0F, -1.2917F, 1.875F);
        this.largecrystal22.addChild(cube_r136);
        setRotationAngle(cube_r136, -0.3491F, 0.0F, 0.0F);
        cube_r136.texOffs(2, 2).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.medcrystal23 = new ModelRenderer((Model) this);
        this.medcrystal23.setPos(3.5F, 20.25F, 21.75F);
        setRotationAngle(this.medcrystal23, 0.0F, -2.6616F, 0.0F);


        ModelRenderer cube_r137 = new ModelRenderer((Model) this);
        cube_r137.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal23.addChild(cube_r137);
        setRotationAngle(cube_r137, 0.0F, 0.0F, 0.2618F);
        cube_r137.texOffs(38, 5).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r138 = new ModelRenderer((Model) this);
        cube_r138.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal23.addChild(cube_r138);
        setRotationAngle(cube_r138, 0.0F, 0.0F, -0.2182F);
        cube_r138.texOffs(38, 5).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.crystal13 = new ModelRenderer((Model) this);
        this.crystal13.setPos(-9.475F, 19.425F, 15.25F);
        setRotationAngle(this.crystal13, 0.0F, 0.5672F, 0.0F);
        this.crystal13.texOffs(2, 2).addBox(-0.525F, -5.175F, -1.0F, 1.0F, 8.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r139 = new ModelRenderer((Model) this);
        cube_r139.setPos(0.475F, -0.425F, -2.0F);
        this.crystal13.addChild(cube_r139);
        setRotationAngle(cube_r139, 0.4363F, 0.0F, 0.0F);
        cube_r139.texOffs(2, 2).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r140 = new ModelRenderer((Model) this);
        cube_r140.setPos(0.475F, -1.425F, 2.0F);
        this.crystal13.addChild(cube_r140);
        setRotationAngle(cube_r140, -0.3927F, 0.0F, 0.0F);
        cube_r140.texOffs(2, 2).addBox(-1.0F, -1.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r141 = new ModelRenderer((Model) this);
        cube_r141.setPos(1.725F, -0.175F, 0.0F);
        this.crystal13.addChild(cube_r141);
        setRotationAngle(cube_r141, 0.0F, 0.0F, 0.4363F);
        cube_r141.texOffs(2, 2).addBox(-0.25F, -2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r142 = new ModelRenderer((Model) this);
        cube_r142.setPos(-2.525F, -5.175F, 0.0F);
        this.crystal13.addChild(cube_r142);
        setRotationAngle(cube_r142, 0.0F, 0.0F, -0.3491F);
        cube_r142.texOffs(2, 2).addBox(-1.75F, 2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        this.crystal14 = new ModelRenderer((Model) this);
        this.crystal14.setPos(-18.475F, 19.425F, -8.75F);
        setRotationAngle(this.crystal14, 0.0F, 0.5672F, 0.0F);
        this.crystal14.texOffs(14, 2).addBox(-0.525F, -5.175F, -1.0F, 1.0F, 8.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r143 = new ModelRenderer((Model) this);
        cube_r143.setPos(0.475F, -0.425F, -2.0F);
        this.crystal14.addChild(cube_r143);
        setRotationAngle(cube_r143, 0.4363F, 0.0F, 0.0F);
        cube_r143.texOffs(14, 2).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r144 = new ModelRenderer((Model) this);
        cube_r144.setPos(0.475F, -1.425F, 2.0F);
        this.crystal14.addChild(cube_r144);
        setRotationAngle(cube_r144, -0.3927F, 0.0F, 0.0F);
        cube_r144.texOffs(14, 2).addBox(-1.0F, -1.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r145 = new ModelRenderer((Model) this);
        cube_r145.setPos(1.725F, -0.175F, 0.0F);
        this.crystal14.addChild(cube_r145);
        setRotationAngle(cube_r145, 0.0F, 0.0F, 0.4363F);
        cube_r145.texOffs(14, 2).addBox(-0.25F, -2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r146 = new ModelRenderer((Model) this);
        cube_r146.setPos(-2.525F, -5.175F, 0.0F);
        this.crystal14.addChild(cube_r146);
        setRotationAngle(cube_r146, 0.0F, 0.0F, -0.3491F);
        cube_r146.texOffs(14, 2).addBox(-1.75F, 2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        this.crystal15 = new ModelRenderer((Model) this);
        this.crystal15.setPos(18.525F, 19.425F, -7.75F);
        setRotationAngle(this.crystal15, 0.0F, 0.5672F, 0.0F);
        this.crystal15.texOffs(28, 4).addBox(-0.525F, -5.175F, -1.0F, 1.0F, 8.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r147 = new ModelRenderer((Model) this);
        cube_r147.setPos(0.475F, -0.425F, -2.0F);
        this.crystal15.addChild(cube_r147);
        setRotationAngle(cube_r147, 0.4363F, 0.0F, 0.0F);
        cube_r147.texOffs(28, 4).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r148 = new ModelRenderer((Model) this);
        cube_r148.setPos(0.475F, -1.425F, 2.0F);
        this.crystal15.addChild(cube_r148);
        setRotationAngle(cube_r148, -0.3927F, 0.0F, 0.0F);
        cube_r148.texOffs(28, 4).addBox(-1.0F, -1.75F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r149 = new ModelRenderer((Model) this);
        cube_r149.setPos(1.725F, -0.175F, 0.0F);
        this.crystal15.addChild(cube_r149);
        setRotationAngle(cube_r149, 0.0F, 0.0F, 0.4363F);
        cube_r149.texOffs(28, 4).addBox(-0.25F, -2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r150 = new ModelRenderer((Model) this);
        cube_r150.setPos(-2.525F, -5.175F, 0.0F);
        this.crystal15.addChild(cube_r150);
        setRotationAngle(cube_r150, 0.0F, 0.0F, -0.3491F);
        cube_r150.texOffs(28, 4).addBox(-1.75F, 2.75F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        this.crystal16 = new ModelRenderer((Model) this);
        this.crystal16.setPos(18.525F, 19.425F, 6.25F);
        setRotationAngle(this.crystal16, 0.0F, 2.2253F, 0.0F);
        this.crystal16.texOffs(38, 5).addBox(-0.525F, -3.425F, -1.0F, 1.0F, 7.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r151 = new ModelRenderer((Model) this);
        cube_r151.setPos(0.475F, -0.425F, -2.0F);
        this.crystal16.addChild(cube_r151);
        setRotationAngle(cube_r151, 0.4363F, 0.0F, 0.0F);
        cube_r151.texOffs(38, 5).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r152 = new ModelRenderer((Model) this);
        cube_r152.setPos(0.35F, 0.8847F, 1.0433F);
        this.crystal16.addChild(cube_r152);
        setRotationAngle(cube_r152, -0.6545F, 0.0F, 0.0F);
        cube_r152.texOffs(38, 5).addBox(-0.875F, -2.5F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r153 = new ModelRenderer((Model) this);
        cube_r153.setPos(1.725F, -0.175F, 0.0F);
        this.crystal16.addChild(cube_r153);
        setRotationAngle(cube_r153, 0.0F, 0.0F, 0.4363F);
        cube_r153.texOffs(38, 5).addBox(-0.25F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r154 = new ModelRenderer((Model) this);
        cube_r154.setPos(-2.525F, -5.175F, 0.0F);
        this.crystal16.addChild(cube_r154);
        setRotationAngle(cube_r154, 0.0F, 0.0F, -0.3491F);
        cube_r154.texOffs(38, 5).addBox(-1.75F, 4.5F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        this.crystal17 = new ModelRenderer((Model) this);
        this.crystal17.setPos(-21.475F, 19.425F, 4.25F);
        setRotationAngle(this.crystal17, 0.0F, 2.2253F, 0.0F);
        this.crystal17.texOffs(6, 2).addBox(-0.525F, -3.425F, -1.0F, 1.0F, 7.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r155 = new ModelRenderer((Model) this);
        cube_r155.setPos(0.475F, -0.425F, -2.0F);
        this.crystal17.addChild(cube_r155);
        setRotationAngle(cube_r155, 0.4363F, 0.0F, 0.0F);
        cube_r155.texOffs(2, 2).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r156 = new ModelRenderer((Model) this);
        cube_r156.setPos(0.35F, 0.8847F, 1.0433F);
        this.crystal17.addChild(cube_r156);
        setRotationAngle(cube_r156, -0.6545F, 0.0F, 0.0F);
        cube_r156.texOffs(2, 2).addBox(-0.875F, -2.5F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r157 = new ModelRenderer((Model) this);
        cube_r157.setPos(1.725F, -0.175F, 0.0F);
        this.crystal17.addChild(cube_r157);
        setRotationAngle(cube_r157, 0.0F, 0.0F, 0.4363F);
        cube_r157.texOffs(2, 2).addBox(-0.25F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r158 = new ModelRenderer((Model) this);
        cube_r158.setPos(-2.525F, -5.175F, 0.0F);
        this.crystal17.addChild(cube_r158);
        setRotationAngle(cube_r158, 0.0F, 0.0F, -0.3491F);
        cube_r158.texOffs(2, 2).addBox(-1.75F, 4.5F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        this.crystal18 = new ModelRenderer((Model) this);
        this.crystal18.setPos(4.525F, 19.425F, -19.75F);
        setRotationAngle(this.crystal18, 0.0F, 1.7453F, 0.0F);
        this.crystal18.texOffs(28, 4).addBox(-0.525F, -3.425F, -1.0F, 1.0F, 7.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r159 = new ModelRenderer((Model) this);
        cube_r159.setPos(0.475F, -0.425F, -2.0F);
        this.crystal18.addChild(cube_r159);
        setRotationAngle(cube_r159, 0.4363F, 0.0F, 0.0F);
        cube_r159.texOffs(28, 4).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r160 = new ModelRenderer((Model) this);
        cube_r160.setPos(0.35F, 0.8847F, 1.0433F);
        this.crystal18.addChild(cube_r160);
        setRotationAngle(cube_r160, -0.6545F, 0.0F, 0.0F);
        cube_r160.texOffs(28, 4).addBox(-0.875F, -2.5F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r161 = new ModelRenderer((Model) this);
        cube_r161.setPos(1.725F, -0.175F, 0.0F);
        this.crystal18.addChild(cube_r161);
        setRotationAngle(cube_r161, 0.0F, 0.0F, 0.4363F);
        cube_r161.texOffs(28, 4).addBox(-0.25F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r162 = new ModelRenderer((Model) this);
        cube_r162.setPos(-2.525F, -5.175F, 0.0F);
        this.crystal18.addChild(cube_r162);
        setRotationAngle(cube_r162, 0.0F, 0.0F, -0.3491F);
        cube_r162.texOffs(28, 4).addBox(-1.75F, 4.5F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        this.largecrystal23 = new ModelRenderer((Model) this);
        this.largecrystal23.setPos(-12.125F, 19.0F, -15.125F);
        setRotationAngle(this.largecrystal23, 0.0F, -0.6545F, 0.0F);
        this.largecrystal23.texOffs(14, 2).addBox(-0.875F, -3.0F, -2.625F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r163 = new ModelRenderer((Model) this);
        cube_r163.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal23.addChild(cube_r163);
        setRotationAngle(cube_r163, 0.3927F, 0.0F, 0.0F);
        cube_r163.texOffs(14, 2).addBox(-0.625F, -1.75F, -4.375F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r164 = new ModelRenderer((Model) this);
        cube_r164.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal23.addChild(cube_r164);
        setRotationAngle(cube_r164, -0.3491F, 0.0F, 0.0F);
        cube_r164.texOffs(14, 2).addBox(-0.625F, -0.25F, -0.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.medcrystal24 = new ModelRenderer((Model) this);
        this.medcrystal24.setPos(1.75F, 20.25F, -22.0F);
        setRotationAngle(this.medcrystal24, 0.0F, -2.8798F, 0.0F);


        ModelRenderer cube_r165 = new ModelRenderer((Model) this);
        cube_r165.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal24.addChild(cube_r165);
        setRotationAngle(cube_r165, 0.0F, 0.0F, 0.2618F);
        cube_r165.texOffs(14, 2).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r166 = new ModelRenderer((Model) this);
        cube_r166.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal24.addChild(cube_r166);
        setRotationAngle(cube_r166, 0.0F, 0.0F, -0.2182F);
        cube_r166.texOffs(14, 2).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal25 = new ModelRenderer((Model) this);
        this.medcrystal25.setPos(15.75F, 20.25F, -11.0F);
        setRotationAngle(this.medcrystal25, 0.0F, -1.6581F, 0.0F);


        ModelRenderer cube_r167 = new ModelRenderer((Model) this);
        cube_r167.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal25.addChild(cube_r167);
        setRotationAngle(cube_r167, 0.0F, 0.0F, 0.2618F);
        cube_r167.texOffs(28, 4).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r168 = new ModelRenderer((Model) this);
        cube_r168.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal25.addChild(cube_r168);
        setRotationAngle(cube_r168, 0.0F, 0.0F, -0.2182F);
        cube_r168.texOffs(28, 4).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal26 = new ModelRenderer((Model) this);
        this.medcrystal26.setPos(-15.25F, 20.25F, 12.5F);
        setRotationAngle(this.medcrystal26, 0.0F, -0.4363F, 0.0F);


        ModelRenderer cube_r169 = new ModelRenderer((Model) this);
        cube_r169.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal26.addChild(cube_r169);
        setRotationAngle(cube_r169, 0.0F, 0.0F, 0.2618F);
        cube_r169.texOffs(2, 2).addBox(0.5F, -4.0F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r170 = new ModelRenderer((Model) this);
        cube_r170.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal26.addChild(cube_r170);
        setRotationAngle(cube_r170, 0.0F, 0.0F, -0.2182F);
        cube_r170.texOffs(2, 2).addBox(-0.75F, -5.0F, -0.75F, 1.0F, 7.0F, 1.0F, 0.0F, false);

        this.crystal19 = new ModelRenderer((Model) this);
        this.crystal19.setPos(6.525F, 19.425F, 18.25F);
        setRotationAngle(this.crystal19, 0.0F, 1.7453F, 0.0F);
        this.crystal19.texOffs(38, 5).addBox(-0.525F, -3.425F, -1.0F, 1.0F, 7.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r171 = new ModelRenderer((Model) this);
        cube_r171.setPos(0.475F, -0.425F, -2.0F);
        this.crystal19.addChild(cube_r171);
        setRotationAngle(cube_r171, 0.4363F, 0.0F, 0.0F);
        cube_r171.texOffs(38, 5).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r172 = new ModelRenderer((Model) this);
        cube_r172.setPos(0.35F, 0.8847F, 1.0433F);
        this.crystal19.addChild(cube_r172);
        setRotationAngle(cube_r172, -0.6545F, 0.0F, 0.0F);
        cube_r172.texOffs(38, 5).addBox(-0.875F, -2.5F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r173 = new ModelRenderer((Model) this);
        cube_r173.setPos(1.725F, -0.175F, 0.0F);
        this.crystal19.addChild(cube_r173);
        setRotationAngle(cube_r173, 0.0F, 0.0F, 0.4363F);
        cube_r173.texOffs(38, 5).addBox(-0.25F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r174 = new ModelRenderer((Model) this);
        cube_r174.setPos(-2.525F, -5.175F, 0.0F);
        this.crystal19.addChild(cube_r174);
        setRotationAngle(cube_r174, 0.0F, 0.0F, -0.3491F);
        cube_r174.texOffs(38, 5).addBox(-1.75F, 4.5F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        this.medcrystal27 = new ModelRenderer((Model) this);
        this.medcrystal27.setPos(-20.5F, 20.25F, -5.25F);
        setRotationAngle(this.medcrystal27, 0.0F, -0.9599F, 0.0F);


        ModelRenderer cube_r175 = new ModelRenderer((Model) this);
        cube_r175.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal27.addChild(cube_r175);
        setRotationAngle(cube_r175, 0.0F, 0.0F, 0.2618F);
        cube_r175.texOffs(14, 2).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r176 = new ModelRenderer((Model) this);
        cube_r176.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal27.addChild(cube_r176);
        setRotationAngle(cube_r176, 0.0F, 0.0F, -0.2182F);
        cube_r176.texOffs(14, 2).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.crystal20 = new ModelRenderer((Model) this);
        this.crystal20.setPos(-6.225F, 19.425F, -17.75F);
        setRotationAngle(this.crystal20, 0.0F, 2.2689F, 0.0F);
        this.crystal20.texOffs(14, 2).addBox(-0.525F, -3.425F, -1.0F, 1.0F, 7.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r177 = new ModelRenderer((Model) this);
        cube_r177.setPos(0.475F, -0.425F, -2.0F);
        this.crystal20.addChild(cube_r177);
        setRotationAngle(cube_r177, 0.4363F, 0.0F, 0.0F);
        cube_r177.texOffs(14, 2).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r178 = new ModelRenderer((Model) this);
        cube_r178.setPos(0.35F, 0.8847F, 1.0433F);
        this.crystal20.addChild(cube_r178);
        setRotationAngle(cube_r178, -0.6545F, 0.0F, 0.0F);
        cube_r178.texOffs(14, 2).addBox(-0.875F, -2.5F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r179 = new ModelRenderer((Model) this);
        cube_r179.setPos(1.725F, -0.175F, 0.0F);
        this.crystal20.addChild(cube_r179);
        setRotationAngle(cube_r179, 0.0F, 0.0F, 0.4363F);
        cube_r179.texOffs(14, 2).addBox(-0.25F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r180 = new ModelRenderer((Model) this);
        cube_r180.setPos(-2.525F, -5.175F, 0.0F);
        this.crystal20.addChild(cube_r180);
        setRotationAngle(cube_r180, 0.0F, 0.0F, -0.3491F);
        cube_r180.texOffs(14, 2).addBox(-1.75F, 4.5F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        this.crystal21 = new ModelRenderer((Model) this);
        this.crystal21.setPos(13.775F, 19.425F, -13.75F);
        setRotationAngle(this.crystal21, 0.0F, 1.5708F, 0.0F);
        this.crystal21.texOffs(28, 4).addBox(-0.525F, -3.425F, -1.0F, 1.0F, 7.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r181 = new ModelRenderer((Model) this);
        cube_r181.setPos(0.475F, -0.425F, -2.0F);
        this.crystal21.addChild(cube_r181);
        setRotationAngle(cube_r181, 0.4363F, 0.0F, 0.0F);
        cube_r181.texOffs(28, 4).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r182 = new ModelRenderer((Model) this);
        cube_r182.setPos(0.35F, 0.8847F, 1.0433F);
        this.crystal21.addChild(cube_r182);
        setRotationAngle(cube_r182, -0.6545F, 0.0F, 0.0F);
        cube_r182.texOffs(28, 4).addBox(-0.875F, -2.5F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r183 = new ModelRenderer((Model) this);
        cube_r183.setPos(1.725F, -0.175F, 0.0F);
        this.crystal21.addChild(cube_r183);
        setRotationAngle(cube_r183, 0.0F, 0.0F, 0.4363F);
        cube_r183.texOffs(28, 4).addBox(-0.25F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r184 = new ModelRenderer((Model) this);
        cube_r184.setPos(-2.525F, -5.175F, 0.0F);
        this.crystal21.addChild(cube_r184);
        setRotationAngle(cube_r184, 0.0F, 0.0F, -0.3491F);
        cube_r184.texOffs(28, 4).addBox(-1.75F, 4.5F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        this.medcrystal28 = new ModelRenderer((Model) this);
        this.medcrystal28.setPos(22.25F, 20.25F, -6.0F);
        setRotationAngle(this.medcrystal28, 0.0F, -0.6981F, 0.0F);


        ModelRenderer cube_r185 = new ModelRenderer((Model) this);
        cube_r185.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal28.addChild(cube_r185);
        setRotationAngle(cube_r185, 0.0F, 0.0F, 0.2618F);
        cube_r185.texOffs(28, 4).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r186 = new ModelRenderer((Model) this);
        cube_r186.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal28.addChild(cube_r186);
        setRotationAngle(cube_r186, 0.0F, 0.0F, -0.2182F);
        cube_r186.texOffs(28, 4).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal29 = new ModelRenderer((Model) this);
        this.medcrystal29.setPos(22.25F, 20.25F, 3.0F);
        setRotationAngle(this.medcrystal29, 0.0F, 0.829F, 0.0F);


        ModelRenderer cube_r187 = new ModelRenderer((Model) this);
        cube_r187.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal29.addChild(cube_r187);
        setRotationAngle(cube_r187, 0.0F, 0.0F, 0.2618F);
        cube_r187.texOffs(38, 5).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r188 = new ModelRenderer((Model) this);
        cube_r188.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal29.addChild(cube_r188);
        setRotationAngle(cube_r188, 0.0F, 0.0F, -0.2182F);
        cube_r188.texOffs(38, 5).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal30 = new ModelRenderer((Model) this);
        this.medcrystal30.setPos(-19.75F, 20.25F, 7.0F);
        setRotationAngle(this.medcrystal30, 0.0F, 0.829F, 0.0F);


        ModelRenderer cube_r189 = new ModelRenderer((Model) this);
        cube_r189.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal30.addChild(cube_r189);
        setRotationAngle(cube_r189, 0.0F, 0.0F, 0.2618F);
        cube_r189.texOffs(2, 2).addBox(0.5F, -5.0F, -0.5F, 1.0F, 7.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r190 = new ModelRenderer((Model) this);
        cube_r190.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal30.addChild(cube_r190);
        setRotationAngle(cube_r190, 0.0F, 0.0F, -0.2182F);
        cube_r190.texOffs(2, 2).addBox(-0.75F, -6.0F, -0.75F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.medcrystal31 = new ModelRenderer((Model) this);
        this.medcrystal31.setPos(-10.75F, 20.25F, -14.0F);
        setRotationAngle(this.medcrystal31, 0.0F, 0.829F, 0.0F);


        ModelRenderer cube_r191 = new ModelRenderer((Model) this);
        cube_r191.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal31.addChild(cube_r191);
        setRotationAngle(cube_r191, 0.0F, 0.0F, 0.2618F);
        cube_r191.texOffs(14, 2).addBox(0.5F, -5.0F, -0.5F, 1.0F, 7.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r192 = new ModelRenderer((Model) this);
        cube_r192.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal31.addChild(cube_r192);
        setRotationAngle(cube_r192, 0.0F, 0.0F, -0.2182F);
        cube_r192.texOffs(14, 2).addBox(-0.75F, -6.0F, -0.75F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.medcrystal32 = new ModelRenderer((Model) this);
        this.medcrystal32.setPos(-4.25F, 20.25F, -23.0F);
        setRotationAngle(this.medcrystal32, 0.0F, 0.1745F, 0.0F);


        ModelRenderer cube_r193 = new ModelRenderer((Model) this);
        cube_r193.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal32.addChild(cube_r193);
        setRotationAngle(cube_r193, 0.0F, 0.0F, 0.2618F);
        cube_r193.texOffs(14, 2).addBox(0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r194 = new ModelRenderer((Model) this);
        cube_r194.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal32.addChild(cube_r194);
        setRotationAngle(cube_r194, 0.0F, 0.0F, -0.2182F);
        cube_r194.texOffs(14, 2).addBox(-0.75F, -1.5F, -0.75F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.medcrystal33 = new ModelRenderer((Model) this);
        this.medcrystal33.setPos(16.75F, 20.25F, -23.0F);
        setRotationAngle(this.medcrystal33, 0.0F, 0.1745F, 0.0F);


        ModelRenderer cube_r195 = new ModelRenderer((Model) this);
        cube_r195.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal33.addChild(cube_r195);
        setRotationAngle(cube_r195, 0.0F, 0.0F, 0.2618F);
        cube_r195.texOffs(28, 4).addBox(0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r196 = new ModelRenderer((Model) this);
        cube_r196.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal33.addChild(cube_r196);
        setRotationAngle(cube_r196, 0.0F, 0.0F, -0.2182F);
        cube_r196.texOffs(28, 4).addBox(-0.75F, -1.5F, -0.75F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.medcrystal34 = new ModelRenderer((Model) this);
        this.medcrystal34.setPos(16.75F, 20.25F, 23.0F);
        setRotationAngle(this.medcrystal34, 0.0F, 0.1745F, 0.0F);


        ModelRenderer cube_r197 = new ModelRenderer((Model) this);
        cube_r197.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal34.addChild(cube_r197);
        setRotationAngle(cube_r197, 0.0F, 0.0F, 0.2618F);
        cube_r197.texOffs(38, 5).addBox(0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r198 = new ModelRenderer((Model) this);
        cube_r198.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal34.addChild(cube_r198);
        setRotationAngle(cube_r198, 0.0F, 0.0F, -0.2182F);
        cube_r198.texOffs(38, 5).addBox(-0.75F, -1.5F, -0.75F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.medcrystal35 = new ModelRenderer((Model) this);
        this.medcrystal35.setPos(-18.25F, 20.25F, 23.0F);
        setRotationAngle(this.medcrystal35, 0.0F, 0.1745F, 0.0F);


        ModelRenderer cube_r199 = new ModelRenderer((Model) this);
        cube_r199.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal35.addChild(cube_r199);
        setRotationAngle(cube_r199, 0.0F, 0.0F, 0.2618F);
        cube_r199.texOffs(2, 2).addBox(0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r200 = new ModelRenderer((Model) this);
        cube_r200.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal35.addChild(cube_r200);
        setRotationAngle(cube_r200, 0.0F, 0.0F, -0.2182F);
        cube_r200.texOffs(2, 2).addBox(-0.75F, -1.5F, -0.75F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.medcrystal36 = new ModelRenderer((Model) this);
        this.medcrystal36.setPos(-23.0F, 20.25F, 0.5F);
        setRotationAngle(this.medcrystal36, 0.0F, 1.0908F, 0.0F);


        ModelRenderer cube_r201 = new ModelRenderer((Model) this);
        cube_r201.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal36.addChild(cube_r201);
        setRotationAngle(cube_r201, 0.0F, 0.0F, 0.2618F);
        cube_r201.texOffs(2, 2).addBox(0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r202 = new ModelRenderer((Model) this);
        cube_r202.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal36.addChild(cube_r202);
        setRotationAngle(cube_r202, 0.0F, 0.0F, -0.2182F);
        cube_r202.texOffs(2, 2).addBox(-0.75F, -1.5F, -0.75F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.medcrystal37 = new ModelRenderer((Model) this);
        this.medcrystal37.setPos(15.5F, 20.25F, 9.0F);
        setRotationAngle(this.medcrystal37, 0.0F, 1.0908F, 0.0F);


        ModelRenderer cube_r203 = new ModelRenderer((Model) this);
        cube_r203.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal37.addChild(cube_r203);
        setRotationAngle(cube_r203, 0.0F, 0.0F, 0.2618F);
        cube_r203.texOffs(38, 5).addBox(0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r204 = new ModelRenderer((Model) this);
        cube_r204.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal37.addChild(cube_r204);
        setRotationAngle(cube_r204, 0.0F, 0.0F, -0.2182F);
        cube_r204.texOffs(38, 5).addBox(-0.75F, -1.5F, -0.75F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.medcrystal38 = new ModelRenderer((Model) this);
        this.medcrystal38.setPos(7.0F, 20.25F, -17.25F);
        setRotationAngle(this.medcrystal38, 0.0F, 1.3963F, 0.0F);


        ModelRenderer cube_r205 = new ModelRenderer((Model) this);
        cube_r205.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal38.addChild(cube_r205);
        setRotationAngle(cube_r205, 0.0F, 0.0F, 0.2618F);
        cube_r205.texOffs(28, 4).addBox(0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r206 = new ModelRenderer((Model) this);
        cube_r206.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal38.addChild(cube_r206);
        setRotationAngle(cube_r206, 0.0F, 0.0F, -0.2182F);
        cube_r206.texOffs(28, 4).addBox(-0.75F, -1.5F, -0.75F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.medcrystal39 = new ModelRenderer((Model) this);
        this.medcrystal39.setPos(20.5F, 20.25F, -4.25F);
        setRotationAngle(this.medcrystal39, 0.0F, 1.3963F, 0.0F);


        ModelRenderer cube_r207 = new ModelRenderer((Model) this);
        cube_r207.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal39.addChild(cube_r207);
        setRotationAngle(cube_r207, 0.0F, 0.0F, 0.2618F);
        cube_r207.texOffs(28, 4).addBox(0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r208 = new ModelRenderer((Model) this);
        cube_r208.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal39.addChild(cube_r208);
        setRotationAngle(cube_r208, 0.0F, 0.0F, -0.2182F);
        cube_r208.texOffs(28, 4).addBox(-0.75F, -1.5F, -0.75F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.largecrystal24 = new ModelRenderer((Model) this);
        this.largecrystal24.setPos(22.625F, 19.0F, -0.375F);
        setRotationAngle(this.largecrystal24, 0.0F, -0.1745F, 0.0F);
        this.largecrystal24.texOffs(28, 4).addBox(-0.875F, -0.75F, -2.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r209 = new ModelRenderer((Model) this);
        cube_r209.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal24.addChild(cube_r209);
        setRotationAngle(cube_r209, 0.3927F, 0.0F, 0.0F);
        cube_r209.texOffs(28, 4).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r210 = new ModelRenderer((Model) this);
        cube_r210.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal24.addChild(cube_r210);
        setRotationAngle(cube_r210, -0.3491F, 0.0F, 0.0F);
        cube_r210.texOffs(28, 4).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.largecrystal25 = new ModelRenderer((Model) this);
        this.largecrystal25.setPos(-1.125F, 20.2917F, -22.25F);
        setRotationAngle(this.largecrystal25, 0.0F, 0.7854F, 0.0F);
        this.largecrystal25.texOffs(14, 2).addBox(-0.875F, -2.0417F, -0.75F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r211 = new ModelRenderer((Model) this);
        cube_r211.setPos(0.0F, -1.2917F, 1.875F);
        this.largecrystal25.addChild(cube_r211);
        setRotationAngle(cube_r211, 0.3927F, 0.0F, 0.0F);
        cube_r211.texOffs(14, 2).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r212 = new ModelRenderer((Model) this);
        cube_r212.setPos(0.0F, -1.2917F, 1.875F);
        this.largecrystal25.addChild(cube_r212);
        setRotationAngle(cube_r212, -0.3491F, 0.0F, 0.0F);
        cube_r212.texOffs(14, 2).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.medcrystal40 = new ModelRenderer((Model) this);
        this.medcrystal40.setPos(-2.25F, 20.25F, 23.0F);
        setRotationAngle(this.medcrystal40, 0.0F, 0.1745F, 0.0F);


        ModelRenderer cube_r213 = new ModelRenderer((Model) this);
        cube_r213.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal40.addChild(cube_r213);
        setRotationAngle(cube_r213, 0.0F, 0.0F, 0.2618F);
        cube_r213.texOffs(2, 2).addBox(0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r214 = new ModelRenderer((Model) this);
        cube_r214.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal40.addChild(cube_r214);
        setRotationAngle(cube_r214, 0.0F, 0.0F, -0.2182F);
        cube_r214.texOffs(2, 2).addBox(-0.75F, -1.5F, -0.75F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.medcrystal41 = new ModelRenderer((Model) this);
        this.medcrystal41.setPos(-23.0F, 20.25F, 15.5F);
        setRotationAngle(this.medcrystal41, 0.0F, 1.0908F, 0.0F);


        ModelRenderer cube_r215 = new ModelRenderer((Model) this);
        cube_r215.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal41.addChild(cube_r215);
        setRotationAngle(cube_r215, 0.0F, 0.0F, 0.2618F);
        cube_r215.texOffs(2, 2).addBox(0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r216 = new ModelRenderer((Model) this);
        cube_r216.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal41.addChild(cube_r216);
        setRotationAngle(cube_r216, 0.0F, 0.0F, -0.2182F);
        cube_r216.texOffs(2, 2).addBox(-0.75F, -1.5F, -0.75F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.largecrystal26 = new ModelRenderer((Model) this);
        this.largecrystal26.setPos(-3.375F, 19.0F, -17.375F);
        setRotationAngle(this.largecrystal26, 0.0F, -0.1745F, 0.0F);
        this.largecrystal26.texOffs(14, 2).addBox(-0.875F, -0.75F, -2.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r217 = new ModelRenderer((Model) this);
        cube_r217.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal26.addChild(cube_r217);
        setRotationAngle(cube_r217, 0.3927F, 0.0F, 0.0F);
        cube_r217.texOffs(14, 2).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r218 = new ModelRenderer((Model) this);
        cube_r218.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal26.addChild(cube_r218);
        setRotationAngle(cube_r218, -0.3491F, 0.0F, 0.0F);
        cube_r218.texOffs(14, 2).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.largecrystal27 = new ModelRenderer((Model) this);
        this.largecrystal27.setPos(8.625F, 19.0F, -13.375F);
        setRotationAngle(this.largecrystal27, 0.0F, -0.1745F, 0.0F);
        this.largecrystal27.texOffs(28, 4).addBox(-0.875F, -0.75F, -2.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r219 = new ModelRenderer((Model) this);
        cube_r219.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal27.addChild(cube_r219);
        setRotationAngle(cube_r219, 0.3927F, 0.0F, 0.0F);
        cube_r219.texOffs(28, 4).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r220 = new ModelRenderer((Model) this);
        cube_r220.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal27.addChild(cube_r220);
        setRotationAngle(cube_r220, -0.3491F, 0.0F, 0.0F);
        cube_r220.texOffs(28, 4).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.crystal22 = new ModelRenderer((Model) this);
        this.crystal22.setPos(-12.225F, 19.425F, -9.5F);
        setRotationAngle(this.crystal22, 0.0F, 2.2689F, 0.0F);
        this.crystal22.texOffs(14, 2).addBox(-0.525F, -3.425F, -1.0F, 1.0F, 7.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r221 = new ModelRenderer((Model) this);
        cube_r221.setPos(0.475F, -0.425F, -2.0F);
        this.crystal22.addChild(cube_r221);
        setRotationAngle(cube_r221, 0.4363F, 0.0F, 0.0F);
        cube_r221.texOffs(14, 2).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r222 = new ModelRenderer((Model) this);
        cube_r222.setPos(0.35F, 0.8847F, 1.0433F);
        this.crystal22.addChild(cube_r222);
        setRotationAngle(cube_r222, -0.6545F, 0.0F, 0.0F);
        cube_r222.texOffs(14, 2).addBox(-0.875F, -2.5F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r223 = new ModelRenderer((Model) this);
        cube_r223.setPos(1.725F, -0.175F, 0.0F);
        this.crystal22.addChild(cube_r223);
        setRotationAngle(cube_r223, 0.0F, 0.0F, 0.4363F);
        cube_r223.texOffs(14, 2).addBox(-0.25F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r224 = new ModelRenderer((Model) this);
        cube_r224.setPos(-2.525F, -5.175F, 0.0F);
        this.crystal22.addChild(cube_r224);
        setRotationAngle(cube_r224, 0.0F, 0.0F, -0.3491F);
        cube_r224.texOffs(14, 2).addBox(-1.75F, 4.5F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        this.crystal23 = new ModelRenderer((Model) this);
        this.crystal23.setPos(-10.975F, 19.425F, 9.5F);
        setRotationAngle(this.crystal23, 0.0F, 2.6616F, 0.0F);
        this.crystal23.texOffs(2, 2).addBox(-0.525F, -3.425F, -1.0F, 1.0F, 7.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r225 = new ModelRenderer((Model) this);
        cube_r225.setPos(0.475F, -0.425F, -2.0F);
        this.crystal23.addChild(cube_r225);
        setRotationAngle(cube_r225, 0.4363F, 0.0F, 0.0F);
        cube_r225.texOffs(2, 2).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r226 = new ModelRenderer((Model) this);
        cube_r226.setPos(0.35F, 0.8847F, 1.0433F);
        this.crystal23.addChild(cube_r226);
        setRotationAngle(cube_r226, -0.6545F, 0.0F, 0.0F);
        cube_r226.texOffs(2, 2).addBox(-0.875F, -2.5F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r227 = new ModelRenderer((Model) this);
        cube_r227.setPos(1.725F, -0.175F, 0.0F);
        this.crystal23.addChild(cube_r227);
        setRotationAngle(cube_r227, 0.0F, 0.0F, 0.4363F);
        cube_r227.texOffs(2, 2).addBox(-0.25F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r228 = new ModelRenderer((Model) this);
        cube_r228.setPos(-2.525F, -5.175F, 0.0F);
        this.crystal23.addChild(cube_r228);
        setRotationAngle(cube_r228, 0.0F, 0.0F, -0.3491F);
        cube_r228.texOffs(2, 2).addBox(-1.75F, 4.5F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        this.crystal24 = new ModelRenderer((Model) this);
        this.crystal24.setPos(11.025F, 19.425F, 8.5F);
        setRotationAngle(this.crystal24, 0.0F, 2.6616F, 0.0F);
        this.crystal24.texOffs(38, 5).addBox(-0.525F, -3.425F, -1.0F, 1.0F, 7.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r229 = new ModelRenderer((Model) this);
        cube_r229.setPos(0.475F, -0.425F, -2.0F);
        this.crystal24.addChild(cube_r229);
        setRotationAngle(cube_r229, 0.4363F, 0.0F, 0.0F);
        cube_r229.texOffs(38, 5).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r230 = new ModelRenderer((Model) this);
        cube_r230.setPos(0.35F, 0.8847F, 1.0433F);
        this.crystal24.addChild(cube_r230);
        setRotationAngle(cube_r230, -0.6545F, 0.0F, 0.0F);
        cube_r230.texOffs(38, 5).addBox(-0.875F, -2.5F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r231 = new ModelRenderer((Model) this);
        cube_r231.setPos(1.725F, -0.175F, 0.0F);
        this.crystal24.addChild(cube_r231);
        setRotationAngle(cube_r231, 0.0F, 0.0F, 0.4363F);
        cube_r231.texOffs(38, 5).addBox(-0.25F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r232 = new ModelRenderer((Model) this);
        cube_r232.setPos(-2.525F, -5.175F, 0.0F);
        this.crystal24.addChild(cube_r232);
        setRotationAngle(cube_r232, 0.0F, 0.0F, -0.3491F);
        cube_r232.texOffs(38, 5).addBox(-1.75F, 4.5F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        this.crystal25 = new ModelRenderer((Model) this);
        this.crystal25.setPos(6.025F, 19.425F, -13.5F);
        setRotationAngle(this.crystal25, 0.0F, 2.6616F, 0.0F);
        this.crystal25.texOffs(28, 4).addBox(-0.525F, -3.425F, -1.0F, 1.0F, 7.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r233 = new ModelRenderer((Model) this);
        cube_r233.setPos(0.475F, -0.425F, -2.0F);
        this.crystal25.addChild(cube_r233);
        setRotationAngle(cube_r233, 0.4363F, 0.0F, 0.0F);
        cube_r233.texOffs(28, 4).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r234 = new ModelRenderer((Model) this);
        cube_r234.setPos(0.35F, 0.8847F, 1.0433F);
        this.crystal25.addChild(cube_r234);
        setRotationAngle(cube_r234, -0.6545F, 0.0F, 0.0F);
        cube_r234.texOffs(28, 4).addBox(-0.875F, -2.5F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r235 = new ModelRenderer((Model) this);
        cube_r235.setPos(1.725F, -0.175F, 0.0F);
        this.crystal25.addChild(cube_r235);
        setRotationAngle(cube_r235, 0.0F, 0.0F, 0.4363F);
        cube_r235.texOffs(28, 4).addBox(-0.25F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        ModelRenderer cube_r236 = new ModelRenderer((Model) this);
        cube_r236.setPos(-2.525F, -5.175F, 0.0F);
        this.crystal25.addChild(cube_r236);
        setRotationAngle(cube_r236, 0.0F, 0.0F, -0.3491F);
        cube_r236.texOffs(28, 4).addBox(-1.75F, 4.5F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        this.largecrystal28 = new ModelRenderer((Model) this);
        this.largecrystal28.setPos(-7.375F, 19.0F, 12.375F);
        setRotationAngle(this.largecrystal28, 0.0F, -0.1745F, 0.0F);
        this.largecrystal28.texOffs(2, 2).addBox(-0.875F, -0.75F, -2.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r237 = new ModelRenderer((Model) this);
        cube_r237.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal28.addChild(cube_r237);
        setRotationAngle(cube_r237, 0.3927F, 0.0F, 0.0F);
        cube_r237.texOffs(2, 2).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r238 = new ModelRenderer((Model) this);
        cube_r238.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal28.addChild(cube_r238);
        setRotationAngle(cube_r238, -0.3491F, 0.0F, 0.0F);
        cube_r238.texOffs(2, 2).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.medcrystal42 = new ModelRenderer((Model) this);
        this.medcrystal42.setPos(-16.0F, 20.25F, 7.5F);
        setRotationAngle(this.medcrystal42, 0.0F, -0.2618F, 0.0F);


        ModelRenderer cube_r239 = new ModelRenderer((Model) this);
        cube_r239.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal42.addChild(cube_r239);
        setRotationAngle(cube_r239, 0.0F, 0.0F, 0.2618F);
        cube_r239.texOffs(2, 2).addBox(0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r240 = new ModelRenderer((Model) this);
        cube_r240.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal42.addChild(cube_r240);
        setRotationAngle(cube_r240, 0.0F, 0.0F, -0.2182F);
        cube_r240.texOffs(2, 2).addBox(-0.75F, -1.5F, -0.75F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.medcrystal43 = new ModelRenderer((Model) this);
        this.medcrystal43.setPos(9.75F, 20.25F, 12.5F);
        setRotationAngle(this.medcrystal43, 0.0F, 1.8762F, 0.0F);


        ModelRenderer cube_r241 = new ModelRenderer((Model) this);
        cube_r241.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal43.addChild(cube_r241);
        setRotationAngle(cube_r241, 0.0F, 0.0F, 0.2618F);
        cube_r241.texOffs(38, 5).addBox(0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r242 = new ModelRenderer((Model) this);
        cube_r242.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal43.addChild(cube_r242);
        setRotationAngle(cube_r242, 0.0F, 0.0F, -0.2182F);
        cube_r242.texOffs(38, 5).addBox(-0.75F, -1.5F, -0.75F, 1.0F, 3.0F, 1.0F, 0.0F, false);
    }


    private void initGround() {
        this.ground2 = new ModelRenderer((Model) this);
        this.ground2.setPos(0.0F, 24.0F, 0.0F);
        this.ground2.texOffs(39, 4).addBox(-1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-2.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(-2.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-5.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-7.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-7.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-8.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-5.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(5.0F, -3.5F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 4).addBox(-1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(3.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(4.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 4).addBox(4.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(3.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 4).addBox(0.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-8.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-4.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-6.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(-2.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(0.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(0.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(2.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-3.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-4.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-1.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(5.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(-3.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-5.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-7.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-8.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-7.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-5.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-6.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(2.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(6.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-3.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(2.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(2.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 4).addBox(3.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-5.0F, -4.0F, 6.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(3.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(6.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(5.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(6.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(7.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(7.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(5.0F, -4.0F, -7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(39, 3).addBox(4.0F, -4.0F, 2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(4.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-3.0F, -4.0F, 7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(1.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(39, 4).addBox(1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(39, 3).addBox(5.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(6.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(7.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(39, 3).addBox(7.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-3.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-7.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-8.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-5.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(-2.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(0.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(0.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(1.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(3.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(2.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(2.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 4).addBox(2.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(3.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(4.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 4).addBox(5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 4).addBox(6.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(6.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(7.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(4.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(3.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(-1.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-5.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(4.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(4.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(4.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 4).addBox(6.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(5.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(27, 2).addBox(6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(7.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(6.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(7.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(5.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(0.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-2.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(-1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(0.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(-1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 4).addBox(-4.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-7.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-8.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-8.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-8.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-7.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-4.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-6.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-3.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-6.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(-5.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-8.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(39, 5).addBox(3.0F, -2.75F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(3.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 4).addBox(2.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 4).addBox(-1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(1.0F, -2.75F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(-1.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(0.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(1, 3).addBox(-1.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(0.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-5.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-6.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-5.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-3.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(-3.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(0.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-4.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-7.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-4.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-5.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(-1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 4).addBox(-4.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-6.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 4).addBox(-3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-4.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 4).addBox(-2.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 4).addBox(-2.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(0.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 4).addBox(4.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(0.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(2.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-4.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-7.0F, -2.75F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-6.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-7.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(3, 2).addBox(-8.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-8.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(-3.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(7.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(7.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(7.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-8.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-8.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(5.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(6.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 3).addBox(1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(39, 5).addBox(5.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-7.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-8.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-8.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-7.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-8.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-6.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-7.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(1, 3).addBox(1.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-5.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(2.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-3.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-4.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-6.0F, -2.75F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(1.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-4.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(-2.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(-1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(2.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 4).addBox(0.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-3.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(0.0F, -2.75F, -7.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(0.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(0.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 3).addBox(3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 3).addBox(5.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(5.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(7.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(40, 5).addBox(3.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(39, 2).addBox(5.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(7.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(27, 2).addBox(4.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(7.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(4.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(5.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(40, 3).addBox(4.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(-4.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-5.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-4.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(-2.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(26, 2).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 4).addBox(-2.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground2.texOffs(14, 2).addBox(-4.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(2, 2).addBox(-2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(28, 2).addBox(6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground2.texOffs(38, 2).addBox(6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.ground3 = new ModelRenderer((Model) this);
        this.ground3.setPos(-16.0F, 24.0F, 0.0F);
        this.ground3.texOffs(1, 2).addBox(-1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-2.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-2.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-5.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-7.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-7.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-8.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-5.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(5.0F, -3.5F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(3.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(4.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(4.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(3.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(0.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-8.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-4.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-6.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-2.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(0.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(0.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(2.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-3.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-4.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-1.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(5.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-3.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-5.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-7.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-8.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-7.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-5.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-6.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(2.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(6.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-3.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(2.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(2.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(3.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-5.0F, -4.0F, 6.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(3.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(6.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(5.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(6.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(7.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(7.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(5.0F, -4.0F, -7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(4.0F, -4.0F, 2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(4.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-3.0F, -4.0F, 7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(1.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(5.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(6.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(7.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(7.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-3.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-7.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-8.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-5.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-2.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(0.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(0.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(1.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(3.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(2.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(2.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(2.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(3.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(4.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(6.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(6.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(7.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(4.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(3.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-1.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-5.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(4.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(4.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(4.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(6.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(5.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(7.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(6.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(7.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(5.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(0.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-2.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(0.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-4.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-7.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-8.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-8.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-8.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-8.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-7.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-4.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-6.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-3.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-6.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-5.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-8.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(3.0F, -2.75F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(3.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(2.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(1.0F, -2.75F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-1.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(0.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-1.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(0.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-5.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-6.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-5.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-3.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-3.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(0.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-4.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-7.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-4.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-5.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-4.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-6.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-4.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-2.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-2.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(0.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(4.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(0.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(2.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-4.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-7.0F, -2.75F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-6.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-7.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-8.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-8.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-3.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(7.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(7.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(7.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-8.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-8.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(5.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(6.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(5.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-7.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-8.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-8.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-7.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-8.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-6.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-7.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(1.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-5.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(2.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-3.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-4.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-6.0F, -2.75F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(1.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-4.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-2.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(2.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(0.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-3.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(0.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(0.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(0.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(5.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(5.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(7.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(3.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(5.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(7.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(4.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(7.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(4.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(5.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(4.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-4.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-5.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-4.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-2.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-2.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-4.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(-2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(16, 2).addBox(6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground3.texOffs(1, 2).addBox(6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.ground4 = new ModelRenderer((Model) this);
        this.ground4.setPos(16.0F, 24.0F, 0.0F);
        this.ground4.texOffs(26, 2).addBox(-1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-2.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-2.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-5.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-7.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-7.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-8.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-5.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(5.0F, -3.5F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(3.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(4.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(4.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(3.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(0.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-8.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-4.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-6.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-2.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(0.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(0.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(2.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-3.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-4.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-1.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(5.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-3.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-5.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-7.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-8.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-7.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-5.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-6.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(2.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(6.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-3.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(2.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(2.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(3.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-5.0F, -4.0F, 6.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(3.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(6.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(5.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(6.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(7.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(7.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(5.0F, -4.0F, -7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(4.0F, -4.0F, 2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(4.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-3.0F, -4.0F, 7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(1.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(5.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(6.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(7.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(7.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-3.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-7.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-8.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-5.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-2.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(0.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(0.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(1.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(3.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(2.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(2.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(2.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(3.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(4.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(6.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(6.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(7.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(4.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(3.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-1.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-5.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(4.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(4.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(4.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(6.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(5.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(7.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(6.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(7.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(5.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(0.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-2.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(0.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-4.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-7.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-8.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-8.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-8.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-8.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-7.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-4.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-6.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-3.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-6.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-5.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-8.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(3.0F, -2.75F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(3.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(2.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(1.0F, -2.75F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-1.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(0.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-1.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(0.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-5.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-6.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-5.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-3.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-3.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(0.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-4.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-7.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-4.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-5.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-4.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-6.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-4.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-2.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-2.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(0.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(4.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(0.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(2.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-4.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-7.0F, -2.75F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-6.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-7.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-8.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-8.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-3.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(7.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(7.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(7.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-8.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-8.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(5.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(6.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(5.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-7.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-8.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-8.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(27, 2).addBox(-7.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 5).addBox(-8.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-6.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-7.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(1.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-5.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(2.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-3.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-4.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-6.0F, -2.75F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(1.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-4.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-2.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(2.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(0.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-3.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(0.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(0.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(0.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(5.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(5.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(7.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(3.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(5.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(7.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(4.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(7.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(4.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(5.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(4.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-4.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-5.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-4.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-2.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(-2.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-4.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(39, 2).addBox(-2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground4.texOffs(26, 2).addBox(6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.ground5 = new ModelRenderer((Model) this);
        this.ground5.setPos(16.0F, 24.0F, 16.0F);
        this.ground5.texOffs(37, 4).addBox(-1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-2.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-2.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-5.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-7.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-7.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-8.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-5.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(5.0F, -3.5F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(3.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(4.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(4.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(3.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(0.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-8.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-4.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-6.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-2.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(0.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(0.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(2.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-3.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-4.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-1.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(5.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-3.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-5.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-7.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-8.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-7.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-5.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-6.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(2.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(6.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-3.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(2.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(2.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(3.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-5.0F, -4.0F, 6.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(3.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(6.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(5.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(6.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(7.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(7.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(5.0F, -4.0F, -7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(4.0F, -4.0F, 2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(4.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-3.0F, -4.0F, 7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(1.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(5.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(6.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(7.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(7.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-3.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-7.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-8.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-5.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-2.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(0.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(0.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(1.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(3.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(2.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(2.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(2.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(3.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(4.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(6.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(6.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(7.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(4.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(3.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-1.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-5.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(4.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(4.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(4.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(6.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(5.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(7.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(6.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(7.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(5.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(0.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-2.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(0.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-4.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-7.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-8.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-8.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-8.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-8.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-7.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-4.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-6.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-3.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-6.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-5.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-8.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(3.0F, -2.75F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(3.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(2.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(1.0F, -2.75F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-1.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(0.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-1.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(0.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-5.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-6.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-5.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-3.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-3.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(0.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-4.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-7.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-4.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-5.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-4.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-6.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-4.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-2.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-2.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(0.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(4.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(0.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(2.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-4.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-7.0F, -2.75F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-6.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-7.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-8.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-8.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-3.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(7.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(7.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(7.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-8.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-8.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(5.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(6.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(5.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-7.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-8.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-8.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-7.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-8.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-6.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-7.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(1.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-5.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(2.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-3.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-4.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-6.0F, -2.75F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(1.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-4.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-2.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(2.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(0.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-3.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(0.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(0.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(0.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(5.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(5.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(7.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(3.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(5.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(7.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(4.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(7.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(4.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(5.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(4.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-4.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-5.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-4.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-2.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-2.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-4.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(-2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground5.texOffs(37, 4).addBox(6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.ground6 = new ModelRenderer((Model) this);
        this.ground6.setPos(0.0F, 24.0F, 16.0F);
        this.ground6.texOffs(38, 6).addBox(-1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-2.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(-2.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-5.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-7.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-7.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-8.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-5.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(5.0F, -3.5F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(-1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(3.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(4.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(4.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(3.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(0.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-8.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-4.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-6.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(-2.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(0.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(0.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(2.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-3.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-4.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(-1.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(5.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-3.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-5.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-7.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-8.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-7.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-5.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-6.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(-1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(2.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(6.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-3.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(2.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(2.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(3.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-5.0F, -4.0F, 6.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(3.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(6.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(5.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(6.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(7.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(7.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(5.0F, -4.0F, -7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(4.0F, -4.0F, 2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(4.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-3.0F, -4.0F, 7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(1.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(5.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(6.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(7.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(7.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(-3.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-7.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-8.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-5.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-2.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(0.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(0.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(1.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(3.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(2.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(2.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(2.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(3.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(4.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(6.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(6.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(7.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(4.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(3.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-1.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-5.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(-2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(4.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(4.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(4.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(6.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(5.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(7.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(6.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(7.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(5.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(0.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-2.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(-1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(0.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(-4.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-7.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-8.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-8.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-8.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-8.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-7.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-4.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-6.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-3.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-6.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-5.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-8.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(3.0F, -2.75F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(3.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(2.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(1.0F, -2.75F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(-1.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(0.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-1.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(0.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-5.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-6.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-5.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-3.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-3.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(0.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-4.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-7.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-4.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-5.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-4.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-6.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-4.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-2.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-2.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(0.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(4.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(0.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(2.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-4.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-7.0F, -2.75F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-6.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-7.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-8.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-8.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-3.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(7.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(7.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(7.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-8.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-8.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(5.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(6.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(5.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-7.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-8.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-8.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-7.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-8.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-6.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-7.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(1.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-5.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(2.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(-3.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-4.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-6.0F, -2.75F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(1.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-4.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-2.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(2.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(0.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-3.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(0.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(0.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(0.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(5.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(5.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(7.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(3.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(5.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(7.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(4.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(7.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(4.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(5.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(4.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-4.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-5.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-4.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-2.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-2.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-4.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(38, 6).addBox(-2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground6.texOffs(1, 2).addBox(6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.ground7 = new ModelRenderer((Model) this);
        this.ground7.setPos(-16.0F, 24.0F, 16.0F);
        this.ground7.texOffs(0, 2).addBox(-1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-2.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-2.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-5.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-7.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-7.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-8.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-5.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(5.0F, -3.5F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(3.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(4.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(4.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(3.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(0.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-8.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-4.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-6.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-2.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(0.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(0.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(2.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-3.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-4.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-1.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(5.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-3.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-5.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-7.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-8.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-7.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-5.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-6.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(2.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(6.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-3.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(2.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(2.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(3.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-5.0F, -4.0F, 6.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(3.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(6.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(5.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(6.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(7.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(7.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(5.0F, -4.0F, -7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(4.0F, -4.0F, 2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(4.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-3.0F, -4.0F, 7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(1.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(5.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(6.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(7.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(7.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-3.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-7.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-8.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-5.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-2.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(0.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(0.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(1.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(3.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(2.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(2.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(2.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(3.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(4.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(6.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(6.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(7.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(4.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(3.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-1.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-5.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(4.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(4.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(4.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(6.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(5.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(7.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(6.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(7.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(5.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(0.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-2.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(0.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-4.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-7.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-8.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-8.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-8.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-8.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-7.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-4.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-6.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-3.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-6.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-5.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-8.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(3.0F, -2.75F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(3.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(2.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(1.0F, -2.75F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-1.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(0.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-1.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(0.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-5.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-6.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-5.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-3.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-3.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(0.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-4.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-7.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-4.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-5.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-4.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-6.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-4.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-2.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-2.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(0.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(4.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(0.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(2.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-4.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-7.0F, -2.75F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-6.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-7.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-8.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-8.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-3.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(7.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(7.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(7.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-8.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-8.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(5.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(6.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(5.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-7.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-8.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-8.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-7.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-8.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-6.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-7.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(1.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-5.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(2.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-3.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-4.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-6.0F, -2.75F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(1.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-4.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-2.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(2.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(0.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-3.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(0.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(0.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(0.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(5.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(5.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(7.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(3.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(5.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(7.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(4.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(7.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(4.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(5.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(4.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-4.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-5.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-4.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-2.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-2.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-4.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(-2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground7.texOffs(0, 2).addBox(6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.ground8 = new ModelRenderer((Model) this);
        this.ground8.setPos(-16.0F, 24.0F, -16.0F);
        this.ground8.texOffs(17, 2).addBox(-1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-2.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-2.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-5.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-7.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-7.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-8.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-5.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(5.0F, -3.5F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(3.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(4.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(4.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(3.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(0.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-8.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-4.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-6.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-2.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(0.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(0.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(2.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-3.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-4.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-1.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(5.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-3.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-5.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-7.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-8.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-7.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-5.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-6.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(2.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(6.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-3.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(2.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(2.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(3.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-5.0F, -4.0F, 6.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(3.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(6.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(5.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(6.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(7.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(7.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(5.0F, -4.0F, -7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(4.0F, -4.0F, 2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(4.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-3.0F, -4.0F, 7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(1.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(5.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(6.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(7.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(7.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-3.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-7.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-8.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-5.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-2.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(0.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(0.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(1.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(3.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(2.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(2.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(2.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(3.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(4.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(6.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(6.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(7.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(4.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(3.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-1.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-5.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(4.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(4.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(4.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(6.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(5.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(7.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(6.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(7.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(5.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(0.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-2.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(0.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-4.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-7.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-8.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-8.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-8.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-8.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-7.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-4.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-6.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-3.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-6.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-5.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-8.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(3.0F, -2.75F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(3.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(2.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(1.0F, -2.75F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-1.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(0.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-1.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(0.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-5.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-6.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-5.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-3.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-3.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(0.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-4.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-7.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-4.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-5.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-4.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-6.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-4.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-2.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-2.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(0.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(4.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(0.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(2.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-4.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-7.0F, -2.75F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-6.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-7.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-8.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-8.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-3.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(7.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(7.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(7.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-8.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-8.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(5.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(6.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(5.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-7.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-8.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-8.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-7.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-8.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-6.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-7.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(1.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-5.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(2.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-3.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-4.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-6.0F, -2.75F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(1.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-4.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-2.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(2.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(0.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-3.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(0.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(0.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(0.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(5.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(5.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(7.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(3.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(5.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(7.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(4.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(7.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(4.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(5.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(4.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-4.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-5.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-4.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-2.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-2.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-4.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(-2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground8.texOffs(17, 2).addBox(6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.ground9 = new ModelRenderer((Model) this);
        this.ground9.setPos(0.0F, 24.0F, -16.0F);
        this.ground9.texOffs(26, 3).addBox(-1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(-2.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-2.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-5.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-7.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-7.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-8.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-5.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(5.0F, -3.5F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(3.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(4.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(4.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(3.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(0.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-8.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 2).addBox(-4.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-6.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(-2.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(0.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(0.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(2.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(-3.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-4.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(-1.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(5.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(-3.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-5.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-7.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-8.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-7.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-5.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-6.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(-1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(2.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(6.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-3.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(2.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(2.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(3.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-5.0F, -4.0F, 6.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(3.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(6.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(5.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(6.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(7.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(7.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(5.0F, -4.0F, -7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(4.0F, -4.0F, 2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(4.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-3.0F, -4.0F, 7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(1.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(5.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(6.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(7.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(7.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-3.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-7.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-8.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-5.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(-2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-2.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(0.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(0.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(1.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(3.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(2.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(2.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(2.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(3.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(4.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(6.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(6.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(7.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(4.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(3.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-1.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-5.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 2).addBox(-2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 2).addBox(2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(4.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(4.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(4.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(6.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(5.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(7.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(6.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(7.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(5.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(0.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-2.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(0.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-4.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-7.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-8.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-8.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-8.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-8.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-7.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-4.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 2).addBox(-6.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-3.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-6.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-5.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-8.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(3.0F, -2.75F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(3.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(2.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(-1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(1.0F, -2.75F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-1.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(0.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-1.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(0.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-5.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-6.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-5.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-3.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-3.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 2).addBox(0.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-4.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-7.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-4.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-5.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-4.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-6.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-4.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-2.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(-2.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(0.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(4.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(0.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(2.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-4.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-7.0F, -2.75F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-6.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-7.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-8.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-8.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-3.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(7.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(7.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(7.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-8.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-8.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(5.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(6.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(5.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-7.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-8.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-8.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-7.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-8.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-6.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-7.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(1.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-5.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(2.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-3.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 2).addBox(-4.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-6.0F, -2.75F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(1.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-4.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-2.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(2.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(0.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-3.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(0.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(0.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(0.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(5.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(5.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(7.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(14, 2).addBox(3.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(5.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(7.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(4.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(7.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(4.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(5.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(4.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-4.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-5.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-4.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(-1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-2.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-2.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-4.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(-2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(26, 3).addBox(6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground9.texOffs(15, 2).addBox(6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        this.ground10 = new ModelRenderer((Model) this);
        this.ground10.setPos(16.0F, 24.0F, -16.0F);
        this.ground10.texOffs(30, 5).addBox(-1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-2.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-2.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-5.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(27, 2).addBox(-7.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-7.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-8.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-5.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(5.0F, -3.5F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(27, 3).addBox(6.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(27, 3).addBox(7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(3.0F, -3.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(4.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(4.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(27, 2).addBox(3.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(0.0F, -3.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-8.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-4.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-6.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-2.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(0.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(0.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(2.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-3.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-4.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-1.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(5.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-3.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-5.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-6.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-7.0F, -3.5F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-8.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-7.0F, -3.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-5.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-6.0F, -3.5F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(2.0F, -3.5F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(6.0F, -3.5F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-3.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(2.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(1.0F, -3.5F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(2.0F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(3.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-5.0F, -4.0F, 6.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(3.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(6.0F, -4.0F, 5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(5.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(6.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(7.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(7.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(5.0F, -4.0F, -7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(4.0F, -4.0F, 2.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(4.0F, -4.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-3.0F, -4.0F, 7.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-7.0F, -3.5F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(1.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(1.0F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(5.0F, -3.5F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(6.0F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(27, 3).addBox(7.0F, -3.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(7.0F, -3.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-3.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-7.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-8.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-5.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-2.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(0.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(0.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(1.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(2.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(3.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(2.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(2.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(2.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(3.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(4.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(4.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(6.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(6.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(7.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(4.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(3.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-1.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-5.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(2.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(3.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(4.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(4.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(4.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(6.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(5.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(6.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(7.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(7.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(6.0F, -2.5F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(7.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(5.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-7.0F, -2.5F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(0.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(27, 2).addBox(-2.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-2.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(1.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(0.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-1.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-4.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-7.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-6.0F, -2.5F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-8.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-8.0F, -2.5F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-8.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-8.0F, -2.5F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-7.0F, -2.5F, 3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-4.0F, -2.5F, 4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-6.0F, -2.5F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-5.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-3.0F, -2.5F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-4.0F, -2.5F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-6.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-3.0F, -2.5F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-5.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-8.0F, -2.5F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(3.0F, -2.75F, 0.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(3.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(2.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-1.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(1.0F, -2.75F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-1.0F, -2.75F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(0.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-1.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(0.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-5.0F, -2.5F, -3.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-6.0F, -2.5F, -2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-5.0F, -2.5F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-5.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-3.0F, -2.5F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-3.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(0.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(1.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-4.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-7.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-4.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-5.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-4.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-6.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-4.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-2.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-2.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(0.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(4.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(3.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(0.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(2.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-4.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-7.0F, -2.75F, -5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-6.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-7.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-8.0F, -2.75F, -6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(27, 2).addBox(-8.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-3.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(27, 3).addBox(7.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(7.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(6.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(7.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-8.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-8.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(5.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(6.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(5.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-7.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-8.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-8.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-7.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-8.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-6.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-7.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(1.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-5.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(2.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-3.0F, -2.75F, 5.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-4.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-6.0F, -2.75F, 7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(1.0F, -2.75F, 6.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-4.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-2.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-2.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(27, 2).addBox(-1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(1.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(2.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(0.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(27, 2).addBox(-3.0F, -2.75F, -8.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(0.0F, -2.75F, -7.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(0.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(0.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(1.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(5.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(5.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(7.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(3.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(5.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(7.0F, -3.0F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(27, 2).addBox(4.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(5.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(27, 2).addBox(7.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(4.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(5.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(4.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-4.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-5.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-4.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-1.0F, -2.75F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-2.0F, -2.75F, 2.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-1.0F, -2.75F, -1.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-2.0F, -2.75F, -4.0F, 1.0F, 0.5F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-4.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-2.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(-2.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(30, 5).addBox(6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.ground10.texOffs(27, 3).addBox(6.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
    }


    private void initSmallChunks() {
        this.smallchunk = new ModelRenderer((Model) this);
        this.smallchunk.setPos(4.5F, 20.0F, 15.0F);
        this.smallchunk.texOffs(38, 5).addBox(-1.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r263 = new ModelRenderer((Model) this);
        cube_r263.setPos(0.0F, 0.0F, 0.0F);
        this.smallchunk.addChild(cube_r263);
        setRotationAngle(cube_r263, 0.0F, 0.0F, 0.3491F);
        cube_r263.texOffs(38, 5).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r264 = new ModelRenderer((Model) this);
        cube_r264.setPos(-2.0F, 0.0F, 0.0F);
        this.smallchunk.addChild(cube_r264);
        setRotationAngle(cube_r264, 0.0F, 0.0F, -0.3491F);
        cube_r264.texOffs(38, 5).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r265 = new ModelRenderer((Model) this);
        cube_r265.setPos(-1.0F, 0.0F, -1.0F);
        this.smallchunk.addChild(cube_r265);
        setRotationAngle(cube_r265, 0.3491F, 0.0F, 0.0F);
        cube_r265.texOffs(38, 5).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r266 = new ModelRenderer((Model) this);
        cube_r266.setPos(-1.0F, 0.0F, 1.0F);
        this.smallchunk.addChild(cube_r266);
        setRotationAngle(cube_r266, -0.3491F, 0.0F, 0.0F);
        cube_r266.texOffs(38, 5).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.smallchunk2 = new ModelRenderer((Model) this);
        this.smallchunk2.setPos(-2.5F, 20.0F, 18.0F);
        setRotationAngle(this.smallchunk2, 0.0F, -0.4363F, 0.0F);
        this.smallchunk2.texOffs(2, 2).addBox(-1.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r267 = new ModelRenderer((Model) this);
        cube_r267.setPos(0.0F, 0.0F, 0.0F);
        this.smallchunk2.addChild(cube_r267);
        setRotationAngle(cube_r267, 0.0F, 0.0F, 0.3491F);
        cube_r267.texOffs(2, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r268 = new ModelRenderer((Model) this);
        cube_r268.setPos(-2.0F, 0.0F, 0.0F);
        this.smallchunk2.addChild(cube_r268);
        setRotationAngle(cube_r268, 0.0F, 0.0F, -0.3491F);
        cube_r268.texOffs(2, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r269 = new ModelRenderer((Model) this);
        cube_r269.setPos(-1.0F, 0.0F, -1.0F);
        this.smallchunk2.addChild(cube_r269);
        setRotationAngle(cube_r269, 0.3491F, 0.0F, 0.0F);
        cube_r269.texOffs(2, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r270 = new ModelRenderer((Model) this);
        cube_r270.setPos(-1.0F, 0.0F, 1.0F);
        this.smallchunk2.addChild(cube_r270);
        setRotationAngle(cube_r270, -0.3491F, 0.0F, 0.0F);
        cube_r270.texOffs(2, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.smallchunk3 = new ModelRenderer((Model) this);
        this.smallchunk3.setPos(1.5F, 20.0F, 23.25F);
        setRotationAngle(this.smallchunk3, 0.0F, -0.4363F, 0.0F);
        this.smallchunk3.texOffs(2, 2).addBox(-1.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r271 = new ModelRenderer((Model) this);
        cube_r271.setPos(0.0F, 0.0F, 0.0F);
        this.smallchunk3.addChild(cube_r271);
        setRotationAngle(cube_r271, 0.0F, 0.0F, 0.3491F);
        cube_r271.texOffs(2, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r272 = new ModelRenderer((Model) this);
        cube_r272.setPos(-2.0F, 0.0F, 0.0F);
        this.smallchunk3.addChild(cube_r272);
        setRotationAngle(cube_r272, 0.0F, 0.0F, -0.3491F);
        cube_r272.texOffs(2, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r273 = new ModelRenderer((Model) this);
        cube_r273.setPos(-1.0F, 0.0F, -1.0F);
        this.smallchunk3.addChild(cube_r273);
        setRotationAngle(cube_r273, 0.3491F, 0.0F, 0.0F);
        cube_r273.texOffs(2, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r274 = new ModelRenderer((Model) this);
        cube_r274.setPos(-1.0F, 0.0F, 1.0F);
        this.smallchunk3.addChild(cube_r274);
        setRotationAngle(cube_r274, -0.3491F, 0.0F, 0.0F);
        cube_r274.texOffs(2, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.smallchunk4 = new ModelRenderer((Model) this);
        this.smallchunk4.setPos(2.0F, 20.0F, -15.75F);
        setRotationAngle(this.smallchunk4, 0.0F, -1.789F, 0.0F);
        this.smallchunk4.texOffs(28, 4).addBox(-1.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r275 = new ModelRenderer((Model) this);
        cube_r275.setPos(0.0F, 0.0F, 0.0F);
        this.smallchunk4.addChild(cube_r275);
        setRotationAngle(cube_r275, 0.0F, 0.0F, 0.3491F);
        cube_r275.texOffs(28, 4).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r276 = new ModelRenderer((Model) this);
        cube_r276.setPos(-2.0F, 0.0F, 0.0F);
        this.smallchunk4.addChild(cube_r276);
        setRotationAngle(cube_r276, 0.0F, 0.0F, -0.3491F);
        cube_r276.texOffs(28, 4).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r277 = new ModelRenderer((Model) this);
        cube_r277.setPos(-1.0F, 0.0F, -1.0F);
        this.smallchunk4.addChild(cube_r277);
        setRotationAngle(cube_r277, 0.3491F, 0.0F, 0.0F);
        cube_r277.texOffs(28, 4).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r278 = new ModelRenderer((Model) this);
        cube_r278.setPos(-1.0F, 0.0F, 1.0F);
        this.smallchunk4.addChild(cube_r278);
        setRotationAngle(cube_r278, -0.3491F, 0.0F, 0.0F);
        cube_r278.texOffs(28, 4).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.smallchunk5 = new ModelRenderer((Model) this);
        this.smallchunk5.setPos(-17.0F, 20.0F, 6.25F);
        setRotationAngle(this.smallchunk5, 0.0F, -1.789F, 0.0F);
        this.smallchunk5.texOffs(2, 2).addBox(-1.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r279 = new ModelRenderer((Model) this);
        cube_r279.setPos(0.0F, 0.0F, 0.0F);
        this.smallchunk5.addChild(cube_r279);
        setRotationAngle(cube_r279, 0.0F, 0.0F, 0.3491F);
        cube_r279.texOffs(2, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r280 = new ModelRenderer((Model) this);
        cube_r280.setPos(-2.0F, 0.0F, 0.0F);
        this.smallchunk5.addChild(cube_r280);
        setRotationAngle(cube_r280, 0.0F, 0.0F, -0.3491F);
        cube_r280.texOffs(2, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r281 = new ModelRenderer((Model) this);
        cube_r281.setPos(-1.0F, 0.0F, -1.0F);
        this.smallchunk5.addChild(cube_r281);
        setRotationAngle(cube_r281, 0.3491F, 0.0F, 0.0F);
        cube_r281.texOffs(2, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r282 = new ModelRenderer((Model) this);
        cube_r282.setPos(-1.0F, 0.0F, 1.0F);
        this.smallchunk5.addChild(cube_r282);
        setRotationAngle(cube_r282, -0.3491F, 0.0F, 0.0F);
        cube_r282.texOffs(2, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.smallchunk6 = new ModelRenderer((Model) this);
        this.smallchunk6.setPos(-17.0F, 20.0F, -3.25F);
        setRotationAngle(this.smallchunk6, 0.0F, -1.1345F, 0.0F);
        this.smallchunk6.texOffs(14, 2).addBox(-1.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r283 = new ModelRenderer((Model) this);
        cube_r283.setPos(0.0F, 0.0F, 0.0F);
        this.smallchunk6.addChild(cube_r283);
        setRotationAngle(cube_r283, 0.0F, 0.0F, 0.3491F);
        cube_r283.texOffs(14, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r284 = new ModelRenderer((Model) this);
        cube_r284.setPos(-2.0F, 0.0F, 0.0F);
        this.smallchunk6.addChild(cube_r284);
        setRotationAngle(cube_r284, 0.0F, 0.0F, -0.3491F);
        cube_r284.texOffs(14, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r285 = new ModelRenderer((Model) this);
        cube_r285.setPos(-1.0F, 0.0F, -1.0F);
        this.smallchunk6.addChild(cube_r285);
        setRotationAngle(cube_r285, 0.3491F, 0.0F, 0.0F);
        cube_r285.texOffs(14, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r286 = new ModelRenderer((Model) this);
        cube_r286.setPos(-1.0F, 0.0F, 1.0F);
        this.smallchunk6.addChild(cube_r286);
        setRotationAngle(cube_r286, -0.3491F, 0.0F, 0.0F);
        cube_r286.texOffs(14, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.smallchunk7 = new ModelRenderer((Model) this);
        this.smallchunk7.setPos(-22.0F, 20.0F, -1.5F);
        setRotationAngle(this.smallchunk7, 0.0F, -1.1345F, 0.0F);
        this.smallchunk7.texOffs(14, 2).addBox(-1.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r287 = new ModelRenderer((Model) this);
        cube_r287.setPos(0.0F, 0.0F, 0.0F);
        this.smallchunk7.addChild(cube_r287);
        setRotationAngle(cube_r287, 0.0F, 0.0F, 0.3491F);
        cube_r287.texOffs(14, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r288 = new ModelRenderer((Model) this);
        cube_r288.setPos(-2.0F, 0.0F, 0.0F);
        this.smallchunk7.addChild(cube_r288);
        setRotationAngle(cube_r288, 0.0F, 0.0F, -0.3491F);
        cube_r288.texOffs(14, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r289 = new ModelRenderer((Model) this);
        cube_r289.setPos(-1.0F, 0.0F, -1.0F);
        this.smallchunk7.addChild(cube_r289);
        setRotationAngle(cube_r289, 0.3491F, 0.0F, 0.0F);
        cube_r289.texOffs(14, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r290 = new ModelRenderer((Model) this);
        cube_r290.setPos(-1.0F, 0.0F, 1.0F);
        this.smallchunk7.addChild(cube_r290);
        setRotationAngle(cube_r290, -0.3491F, 0.0F, 0.0F);
        cube_r290.texOffs(14, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.smallchunk8 = new ModelRenderer((Model) this);
        this.smallchunk8.setPos(21.0F, 20.0F, -0.5F);
        setRotationAngle(this.smallchunk8, 0.0F, -1.1345F, 0.0F);
        this.smallchunk8.texOffs(28, 4).addBox(-1.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r291 = new ModelRenderer((Model) this);
        cube_r291.setPos(0.0F, 0.0F, 0.0F);
        this.smallchunk8.addChild(cube_r291);
        setRotationAngle(cube_r291, 0.0F, 0.0F, 0.3491F);
        cube_r291.texOffs(28, 4).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r292 = new ModelRenderer((Model) this);
        cube_r292.setPos(-2.0F, 0.0F, 0.0F);
        this.smallchunk8.addChild(cube_r292);
        setRotationAngle(cube_r292, 0.0F, 0.0F, -0.3491F);
        cube_r292.texOffs(28, 4).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r293 = new ModelRenderer((Model) this);
        cube_r293.setPos(-1.0F, 0.0F, -1.0F);
        this.smallchunk8.addChild(cube_r293);
        setRotationAngle(cube_r293, 0.3491F, 0.0F, 0.0F);
        cube_r293.texOffs(28, 4).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r294 = new ModelRenderer((Model) this);
        cube_r294.setPos(-1.0F, 0.0F, 1.0F);
        this.smallchunk8.addChild(cube_r294);
        setRotationAngle(cube_r294, -0.3491F, 0.0F, 0.0F);
        cube_r294.texOffs(28, 4).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.smallchunk9 = new ModelRenderer((Model) this);
        this.smallchunk9.setPos(18.0F, 20.0F, -3.5F);
        setRotationAngle(this.smallchunk9, 0.0F, -1.1345F, 0.0F);
        this.smallchunk9.texOffs(28, 4).addBox(-1.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r295 = new ModelRenderer((Model) this);
        cube_r295.setPos(0.0F, 0.0F, 0.0F);
        this.smallchunk9.addChild(cube_r295);
        setRotationAngle(cube_r295, 0.0F, 0.0F, 0.3491F);
        cube_r295.texOffs(28, 4).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r296 = new ModelRenderer((Model) this);
        cube_r296.setPos(-2.0F, 0.0F, 0.0F);
        this.smallchunk9.addChild(cube_r296);
        setRotationAngle(cube_r296, 0.0F, 0.0F, -0.3491F);
        cube_r296.texOffs(28, 4).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r297 = new ModelRenderer((Model) this);
        cube_r297.setPos(-1.0F, 0.0F, -1.0F);
        this.smallchunk9.addChild(cube_r297);
        setRotationAngle(cube_r297, 0.3491F, 0.0F, 0.0F);
        cube_r297.texOffs(28, 4).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r298 = new ModelRenderer((Model) this);
        cube_r298.setPos(-1.0F, 0.0F, 1.0F);
        this.smallchunk9.addChild(cube_r298);
        setRotationAngle(cube_r298, -0.3491F, 0.0F, 0.0F);
        cube_r298.texOffs(28, 4).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.smallchunk10 = new ModelRenderer((Model) this);
        this.smallchunk10.setPos(-6.0F, 20.0F, -13.5F);
        setRotationAngle(this.smallchunk10, 0.0F, -1.1345F, 0.0F);
        this.smallchunk10.texOffs(14, 2).addBox(-1.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r299 = new ModelRenderer((Model) this);
        cube_r299.setPos(0.0F, 0.0F, 0.0F);
        this.smallchunk10.addChild(cube_r299);
        setRotationAngle(cube_r299, 0.0F, 0.0F, 0.3491F);
        cube_r299.texOffs(14, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r300 = new ModelRenderer((Model) this);
        cube_r300.setPos(-2.0F, 0.0F, 0.0F);
        this.smallchunk10.addChild(cube_r300);
        setRotationAngle(cube_r300, 0.0F, 0.0F, -0.3491F);
        cube_r300.texOffs(14, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r301 = new ModelRenderer((Model) this);
        cube_r301.setPos(-1.0F, 0.0F, -1.0F);
        this.smallchunk10.addChild(cube_r301);
        setRotationAngle(cube_r301, 0.3491F, 0.0F, 0.0F);
        cube_r301.texOffs(14, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r302 = new ModelRenderer((Model) this);
        cube_r302.setPos(-1.0F, 0.0F, 1.0F);
        this.smallchunk10.addChild(cube_r302);
        setRotationAngle(cube_r302, -0.3491F, 0.0F, 0.0F);
        cube_r302.texOffs(14, 2).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.smallchunk11 = new ModelRenderer((Model) this);
        this.smallchunk11.setPos(15.0F, 20.0F, 4.5F);
        setRotationAngle(this.smallchunk11, 0.0F, -1.1345F, 0.0F);
        this.smallchunk11.texOffs(38, 5).addBox(-1.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r303 = new ModelRenderer((Model) this);
        cube_r303.setPos(0.0F, 0.0F, 0.0F);
        this.smallchunk11.addChild(cube_r303);
        setRotationAngle(cube_r303, 0.0F, 0.0F, 0.3491F);
        cube_r303.texOffs(38, 5).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r304 = new ModelRenderer((Model) this);
        cube_r304.setPos(-2.0F, 0.0F, 0.0F);
        this.smallchunk11.addChild(cube_r304);
        setRotationAngle(cube_r304, 0.0F, 0.0F, -0.3491F);
        cube_r304.texOffs(38, 5).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r305 = new ModelRenderer((Model) this);
        cube_r305.setPos(-1.0F, 0.0F, -1.0F);
        this.smallchunk11.addChild(cube_r305);
        setRotationAngle(cube_r305, 0.3491F, 0.0F, 0.0F);
        cube_r305.texOffs(38, 5).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r306 = new ModelRenderer((Model) this);
        cube_r306.setPos(-1.0F, 0.0F, 1.0F);
        this.smallchunk11.addChild(cube_r306);
        setRotationAngle(cube_r306, -0.3491F, 0.0F, 0.0F);
        cube_r306.texOffs(38, 5).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.smallchunk12 = new ModelRenderer((Model) this);
        this.smallchunk12.setPos(20.0F, 20.0F, 2.5F);
        setRotationAngle(this.smallchunk12, 0.0F, -1.1345F, 0.0F);
        this.smallchunk12.texOffs(38, 5).addBox(-1.5F, -4.0F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r307 = new ModelRenderer((Model) this);
        cube_r307.setPos(0.0F, 0.0F, 0.0F);
        this.smallchunk12.addChild(cube_r307);
        setRotationAngle(cube_r307, 0.0F, 0.0F, 0.3491F);
        cube_r307.texOffs(38, 5).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r308 = new ModelRenderer((Model) this);
        cube_r308.setPos(-2.0F, 0.0F, 0.0F);
        this.smallchunk12.addChild(cube_r308);
        setRotationAngle(cube_r308, 0.0F, 0.0F, -0.3491F);
        cube_r308.texOffs(38, 5).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r309 = new ModelRenderer((Model) this);
        cube_r309.setPos(-1.0F, 0.0F, -1.0F);
        this.smallchunk12.addChild(cube_r309);
        setRotationAngle(cube_r309, 0.3491F, 0.0F, 0.0F);
        cube_r309.texOffs(38, 5).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r310 = new ModelRenderer((Model) this);
        cube_r310.setPos(-1.0F, 0.0F, 1.0F);
        this.smallchunk12.addChild(cube_r310);
        setRotationAngle(cube_r310, -0.3491F, 0.0F, 0.0F);
        cube_r310.texOffs(38, 5).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.smallchunk13 = new ModelRenderer((Model) this);
        this.smallchunk13.setPos(9.0F, 20.0F, -9.5F);
        setRotationAngle(this.smallchunk13, 0.0F, -1.1345F, 0.0F);
        this.smallchunk13.texOffs(28, 4).addBox(-1.5F, -4.0F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r311 = new ModelRenderer((Model) this);
        cube_r311.setPos(0.0F, 0.0F, 0.0F);
        this.smallchunk13.addChild(cube_r311);
        setRotationAngle(cube_r311, 0.0F, 0.0F, 0.3491F);
        cube_r311.texOffs(28, 4).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r312 = new ModelRenderer((Model) this);
        cube_r312.setPos(-2.0F, 0.0F, 0.0F);
        this.smallchunk13.addChild(cube_r312);
        setRotationAngle(cube_r312, 0.0F, 0.0F, -0.3491F);
        cube_r312.texOffs(28, 4).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r313 = new ModelRenderer((Model) this);
        cube_r313.setPos(-1.0F, 0.0F, -1.0F);
        this.smallchunk13.addChild(cube_r313);
        setRotationAngle(cube_r313, 0.3491F, 0.0F, 0.0F);
        cube_r313.texOffs(28, 4).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r314 = new ModelRenderer((Model) this);
        cube_r314.setPos(-1.0F, 0.0F, 1.0F);
        this.smallchunk13.addChild(cube_r314);
        setRotationAngle(cube_r314, -0.3491F, 0.0F, 0.0F);
        cube_r314.texOffs(28, 4).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.smallchunk14 = new ModelRenderer((Model) this);
        this.smallchunk14.setPos(-7.75F, 20.0F, -8.5F);
        setRotationAngle(this.smallchunk14, 0.0F, -1.6581F, 0.0F);
        this.smallchunk14.texOffs(14, 2).addBox(-1.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r315 = new ModelRenderer((Model) this);
        cube_r315.setPos(0.0F, 0.0F, 0.0F);
        this.smallchunk14.addChild(cube_r315);
        setRotationAngle(cube_r315, 0.0F, 0.0F, 0.3491F);
        cube_r315.texOffs(14, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r316 = new ModelRenderer((Model) this);
        cube_r316.setPos(-2.0F, 0.0F, 0.0F);
        this.smallchunk14.addChild(cube_r316);
        setRotationAngle(cube_r316, 0.0F, 0.0F, -0.3491F);
        cube_r316.texOffs(14, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r317 = new ModelRenderer((Model) this);
        cube_r317.setPos(-1.0F, 0.0F, -1.0F);
        this.smallchunk14.addChild(cube_r317);
        setRotationAngle(cube_r317, 0.3491F, 0.0F, 0.0F);
        cube_r317.texOffs(14, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r318 = new ModelRenderer((Model) this);
        cube_r318.setPos(-1.0F, 0.0F, 1.0F);
        this.smallchunk14.addChild(cube_r318);
        setRotationAngle(cube_r318, -0.3491F, 0.0F, 0.0F);
        cube_r318.texOffs(14, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.smallchunk15 = new ModelRenderer((Model) this);
        this.smallchunk15.setPos(-7.75F, 20.0F, 7.5F);
        setRotationAngle(this.smallchunk15, 0.0F, -1.6581F, 0.0F);
        this.smallchunk15.texOffs(2, 2).addBox(-1.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r319 = new ModelRenderer((Model) this);
        cube_r319.setPos(0.0F, 0.0F, 0.0F);
        this.smallchunk15.addChild(cube_r319);
        setRotationAngle(cube_r319, 0.0F, 0.0F, 0.3491F);
        cube_r319.texOffs(2, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r320 = new ModelRenderer((Model) this);
        cube_r320.setPos(-2.0F, 0.0F, 0.0F);
        this.smallchunk15.addChild(cube_r320);
        setRotationAngle(cube_r320, 0.0F, 0.0F, -0.3491F);
        cube_r320.texOffs(2, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r321 = new ModelRenderer((Model) this);
        cube_r321.setPos(-1.0F, 0.0F, -1.0F);
        this.smallchunk15.addChild(cube_r321);
        setRotationAngle(cube_r321, 0.3491F, 0.0F, 0.0F);
        cube_r321.texOffs(2, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r322 = new ModelRenderer((Model) this);
        cube_r322.setPos(-1.0F, 0.0F, 1.0F);
        this.smallchunk15.addChild(cube_r322);
        setRotationAngle(cube_r322, -0.3491F, 0.0F, 0.0F);
        cube_r322.texOffs(2, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.smallchunk16 = new ModelRenderer((Model) this);
        this.smallchunk16.setPos(6.75F, 20.0F, 11.0F);
        setRotationAngle(this.smallchunk16, 0.0F, -1.6581F, 0.0F);
        this.smallchunk16.texOffs(38, 5).addBox(-1.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r323 = new ModelRenderer((Model) this);
        cube_r323.setPos(0.0F, 0.0F, 0.0F);
        this.smallchunk16.addChild(cube_r323);
        setRotationAngle(cube_r323, 0.0F, 0.0F, 0.3491F);
        cube_r323.texOffs(38, 5).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r324 = new ModelRenderer((Model) this);
        cube_r324.setPos(-2.0F, 0.0F, 0.0F);
        this.smallchunk16.addChild(cube_r324);
        setRotationAngle(cube_r324, 0.0F, 0.0F, -0.3491F);
        cube_r324.texOffs(38, 5).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r325 = new ModelRenderer((Model) this);
        cube_r325.setPos(-1.0F, 0.0F, -1.0F);
        this.smallchunk16.addChild(cube_r325);
        setRotationAngle(cube_r325, 0.3491F, 0.0F, 0.0F);
        cube_r325.texOffs(38, 5).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r326 = new ModelRenderer((Model) this);
        cube_r326.setPos(-1.0F, 0.0F, 1.0F);
        this.smallchunk16.addChild(cube_r326);
        setRotationAngle(cube_r326, -0.3491F, 0.0F, 0.0F);
        cube_r326.texOffs(38, 5).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.smallchunk17 = new ModelRenderer((Model) this);
        this.smallchunk17.setPos(14.75F, 20.0F, -7.0F);
        setRotationAngle(this.smallchunk17, 0.0F, -1.6581F, 0.0F);
        this.smallchunk17.texOffs(28, 4).addBox(-1.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r327 = new ModelRenderer((Model) this);
        cube_r327.setPos(0.0F, 0.0F, 0.0F);
        this.smallchunk17.addChild(cube_r327);
        setRotationAngle(cube_r327, 0.0F, 0.0F, 0.3491F);
        cube_r327.texOffs(28, 4).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r328 = new ModelRenderer((Model) this);
        cube_r328.setPos(-2.0F, 0.0F, 0.0F);
        this.smallchunk17.addChild(cube_r328);
        setRotationAngle(cube_r328, 0.0F, 0.0F, -0.3491F);
        cube_r328.texOffs(28, 4).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r329 = new ModelRenderer((Model) this);
        cube_r329.setPos(-1.0F, 0.0F, -1.0F);
        this.smallchunk17.addChild(cube_r329);
        setRotationAngle(cube_r329, 0.3491F, 0.0F, 0.0F);
        cube_r329.texOffs(28, 4).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r330 = new ModelRenderer((Model) this);
        cube_r330.setPos(-1.0F, 0.0F, 1.0F);
        this.smallchunk17.addChild(cube_r330);
        setRotationAngle(cube_r330, -0.3491F, 0.0F, 0.0F);
        cube_r330.texOffs(28, 4).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.medcrystal50 = new ModelRenderer((Model) this);
        this.medcrystal50.setPos(-20.0F, 20.25F, -1.25F);
        setRotationAngle(this.medcrystal50, 0.0F, -2.0508F, 0.0F);


        ModelRenderer cube_r331 = new ModelRenderer((Model) this);
        cube_r331.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal50.addChild(cube_r331);
        setRotationAngle(cube_r331, 0.0F, 0.0F, 0.2618F);
        cube_r331.texOffs(14, 2).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r332 = new ModelRenderer((Model) this);
        cube_r332.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal50.addChild(cube_r332);
        setRotationAngle(cube_r332, 0.0F, 0.0F, -0.2182F);
        cube_r332.texOffs(14, 2).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal51 = new ModelRenderer((Model) this);
        this.medcrystal51.setPos(0.75F, 20.25F, 20.0F);
        setRotationAngle(this.medcrystal51, 0.0F, 0.1745F, 0.0F);


        ModelRenderer cube_r333 = new ModelRenderer((Model) this);
        cube_r333.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal51.addChild(cube_r333);
        setRotationAngle(cube_r333, 0.0F, 0.0F, 0.2618F);
        cube_r333.texOffs(38, 5).addBox(0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r334 = new ModelRenderer((Model) this);
        cube_r334.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal51.addChild(cube_r334);
        setRotationAngle(cube_r334, 0.0F, 0.0F, -0.2182F);
        cube_r334.texOffs(38, 5).addBox(-0.75F, -1.5F, -0.75F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        this.bb_main = new ModelRenderer((Model) this);
        this.bb_main.setPos(0.0F, 24.0F, 0.0F);
        this.bb_main.texOffs(0, 14).addBox(-24.0F, -2.0F, -24.0F, 48.0F, 2.0F, 48.0F, 0.0F, false);
    }

    private void initMediumCrystals() {
        this.medcrystal44 = new ModelRenderer((Model) this);
        this.medcrystal44.setPos(7.25F, 20.25F, 14.0F);
        setRotationAngle(this.medcrystal44, 0.0F, 1.8326F, 0.0F);


        ModelRenderer cube_r243 = new ModelRenderer((Model) this);
        cube_r243.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal44.addChild(cube_r243);
        setRotationAngle(cube_r243, 0.0F, 0.0F, 0.2618F);
        cube_r243.texOffs(38, 5).addBox(0.5F, -5.0F, -0.5F, 1.0F, 7.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r244 = new ModelRenderer((Model) this);
        cube_r244.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal44.addChild(cube_r244);
        setRotationAngle(cube_r244, 0.0F, 0.0F, -0.2182F);
        cube_r244.texOffs(38, 5).addBox(-0.75F, -6.0F, -0.75F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.medcrystal45 = new ModelRenderer((Model) this);
        this.medcrystal45.setPos(10.25F, 20.25F, -13.0F);
        setRotationAngle(this.medcrystal45, 0.0F, 1.8326F, 0.0F);


        ModelRenderer cube_r245 = new ModelRenderer((Model) this);
        cube_r245.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal45.addChild(cube_r245);
        setRotationAngle(cube_r245, 0.0F, 0.0F, 0.2618F);
        cube_r245.texOffs(28, 4).addBox(0.5F, -5.0F, -0.5F, 1.0F, 7.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r246 = new ModelRenderer((Model) this);
        cube_r246.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal45.addChild(cube_r246);
        setRotationAngle(cube_r246, 0.0F, 0.0F, -0.2182F);
        cube_r246.texOffs(28, 4).addBox(-0.75F, -6.0F, -0.75F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        this.medcrystal46 = new ModelRenderer((Model) this);
        this.medcrystal46.setPos(-5.75F, 20.25F, 17.0F);
        setRotationAngle(this.medcrystal46, 0.0F, 1.0036F, 0.0F);


        ModelRenderer cube_r247 = new ModelRenderer((Model) this);
        cube_r247.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal46.addChild(cube_r247);
        setRotationAngle(cube_r247, 0.0F, 0.0F, 0.2618F);
        cube_r247.texOffs(2, 2).addBox(0.5F, -3.25F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r248 = new ModelRenderer((Model) this);
        cube_r248.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal46.addChild(cube_r248);
        setRotationAngle(cube_r248, 0.0F, 0.0F, -0.2182F);
        cube_r248.texOffs(2, 2).addBox(-0.75F, -4.25F, -0.75F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        this.medcrystal47 = new ModelRenderer((Model) this);
        this.medcrystal47.setPos(12.5F, 20.25F, -10.25F);
        setRotationAngle(this.medcrystal47, 0.0F, -0.3491F, 0.0F);


        ModelRenderer cube_r249 = new ModelRenderer((Model) this);
        cube_r249.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal47.addChild(cube_r249);
        setRotationAngle(cube_r249, 0.0F, 0.0F, 0.2618F);
        cube_r249.texOffs(28, 4).addBox(0.5F, -3.25F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r250 = new ModelRenderer((Model) this);
        cube_r250.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal47.addChild(cube_r250);
        setRotationAngle(cube_r250, 0.0F, 0.0F, -0.2182F);
        cube_r250.texOffs(28, 4).addBox(-0.75F, -4.25F, -0.75F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        this.largecrystal29 = new ModelRenderer((Model) this);
        this.largecrystal29.setPos(-9.875F, 19.0F, -10.375F);
        setRotationAngle(this.largecrystal29, 0.0F, -0.48F, 0.0F);
        this.largecrystal29.texOffs(14, 2).addBox(-0.875F, -0.75F, -2.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r251 = new ModelRenderer((Model) this);
        cube_r251.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal29.addChild(cube_r251);
        setRotationAngle(cube_r251, 0.3927F, 0.0F, 0.0F);
        cube_r251.texOffs(14, 2).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r252 = new ModelRenderer((Model) this);
        cube_r252.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal29.addChild(cube_r252);
        setRotationAngle(cube_r252, -0.3491F, 0.0F, 0.0F);
        cube_r252.texOffs(14, 2).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.largecrystal30 = new ModelRenderer((Model) this);
        this.largecrystal30.setPos(15.125F, 19.0F, 7.625F);
        setRotationAngle(this.largecrystal30, 0.0F, 0.6981F, 0.0F);
        this.largecrystal30.texOffs(38, 5).addBox(-0.875F, -0.75F, -2.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r253 = new ModelRenderer((Model) this);
        cube_r253.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal30.addChild(cube_r253);
        setRotationAngle(cube_r253, 0.3927F, 0.0F, 0.0F);
        cube_r253.texOffs(38, 5).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r254 = new ModelRenderer((Model) this);
        cube_r254.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal30.addChild(cube_r254);
        setRotationAngle(cube_r254, -0.3491F, 0.0F, 0.0F);
        cube_r254.texOffs(38, 5).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.largecrystal31 = new ModelRenderer((Model) this);
        this.largecrystal31.setPos(6.125F, 19.0F, 14.625F);
        setRotationAngle(this.largecrystal31, 0.0F, 0.6981F, 0.0F);
        this.largecrystal31.texOffs(38, 5).addBox(-0.875F, -0.75F, -2.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r255 = new ModelRenderer((Model) this);
        cube_r255.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal31.addChild(cube_r255);
        setRotationAngle(cube_r255, 0.3927F, 0.0F, 0.0F);
        cube_r255.texOffs(38, 5).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r256 = new ModelRenderer((Model) this);
        cube_r256.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal31.addChild(cube_r256);
        setRotationAngle(cube_r256, -0.3491F, 0.0F, 0.0F);
        cube_r256.texOffs(38, 5).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.largecrystal32 = new ModelRenderer((Model) this);
        this.largecrystal32.setPos(-0.625F, 19.0F, 21.625F);
        setRotationAngle(this.largecrystal32, 0.0F, 0.6981F, 0.0F);
        this.largecrystal32.texOffs(2, 2).addBox(-0.875F, -0.75F, -2.625F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r257 = new ModelRenderer((Model) this);
        cube_r257.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal32.addChild(cube_r257);
        setRotationAngle(cube_r257, 0.3927F, 0.0F, 0.0F);
        cube_r257.texOffs(2, 2).addBox(-0.625F, -0.75F, -4.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r258 = new ModelRenderer((Model) this);
        cube_r258.setPos(0.0F, 0.0F, 0.0F);
        this.largecrystal32.addChild(cube_r258);
        setRotationAngle(cube_r258, -0.3491F, 0.0F, 0.0F);
        cube_r258.texOffs(2, 2).addBox(-0.625F, 0.75F, -0.625F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        this.medcrystal48 = new ModelRenderer((Model) this);
        this.medcrystal48.setPos(-5.5F, 20.25F, 11.75F);
        setRotationAngle(this.medcrystal48, 0.0F, -1.1345F, 0.0F);


        ModelRenderer cube_r259 = new ModelRenderer((Model) this);
        cube_r259.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal48.addChild(cube_r259);
        setRotationAngle(cube_r259, 0.0F, 0.0F, 0.2618F);
        cube_r259.texOffs(2, 2).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r260 = new ModelRenderer((Model) this);
        cube_r260.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal48.addChild(cube_r260);
        setRotationAngle(cube_r260, 0.0F, 0.0F, -0.2182F);
        cube_r260.texOffs(2, 2).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        this.medcrystal49 = new ModelRenderer((Model) this);
        this.medcrystal49.setPos(-0.25F, 20.25F, -19.5F);
        setRotationAngle(this.medcrystal49, 0.0F, -1.1345F, 0.0F);


        ModelRenderer cube_r261 = new ModelRenderer((Model) this);
        cube_r261.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal49.addChild(cube_r261);
        setRotationAngle(cube_r261, 0.0F, 0.0F, 0.2618F);
        cube_r261.texOffs(28, 4).addBox(0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelRenderer cube_r262 = new ModelRenderer((Model) this);
        cube_r262.setPos(0.0F, 0.0F, 0.0F);
        this.medcrystal49.addChild(cube_r262);
        setRotationAngle(cube_r262, 0.0F, 0.0F, -0.2182F);
        cube_r262.texOffs(28, 4).addBox(-0.75F, -3.0F, -0.75F, 1.0F, 5.0F, 1.0F, 0.0F, false);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\model\OmegaStatueModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */