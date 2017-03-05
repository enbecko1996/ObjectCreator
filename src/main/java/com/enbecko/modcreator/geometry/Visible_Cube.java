package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.GlobalRenderSetting;
import com.enbecko.modcreator.linalg.vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by enbec on 24.02.2017.
 */
public class Visible_Cube extends Content.CubicContent implements ITextured {
    private final int dimension;

    public Visible_Cube(Bone parentBone, vec3.IntVec positionInBone, int dimension) {
        super(parentBone, positionInBone, dimension);
        this.dimension = dimension;
    }

    @Override
    public Visible_Cube createBoundingGeometry() {
        this.makeCorners(false);
        this.makeCubicEdgesAndFacesNoUpdate();
        return this;
    }

    @Override
    public vec3.IntVec getPositionInBoneCoords() {
        return (vec3.IntVec) this.positionInBoneCoords;
    }

    @Override
    public vec3.IntVec getCorner(int pos) {
        return (vec3.IntVec) this.boundingCornersInBoneCoords[pos];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(GlobalRenderSetting renderPass) {
        super.render(renderPass);
    }
}
