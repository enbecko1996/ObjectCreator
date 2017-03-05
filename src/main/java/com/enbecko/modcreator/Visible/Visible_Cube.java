package com.enbecko.modcreator.Visible;

import com.enbecko.modcreator.GlobalRenderSetting;
import com.enbecko.modcreator.Rectangle;
import com.enbecko.modcreator.contentholder.Bone;
import com.enbecko.modcreator.contentholder.Content;
import com.enbecko.modcreator.linalg.Face3D;
import com.enbecko.modcreator.linalg.vec3;
import com.enbecko.modcreator.linalg.vec4;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbec on 24.02.2017.
 */
public class Visible_Cube extends Content.CubicContent implements ITextured<Rectangle.Colored> {
    private final int dimension;
    private List<Rectangle.Colored> rectangles = new ArrayList<Rectangle.Colored>();

    public Visible_Cube(Bone parentBone, vec3.IntVec positionInBone, int dimension) {
        super(parentBone, positionInBone, dimension);
        this.dimension = dimension;
        Face3D face = new Face3D.FaceAutoUpdateOnVecChange(new vec3.FloatVec(0,0,0),
                new vec3.FloatVec(0,0,1), new vec3.FloatVec(0,1,1), new vec3.FloatVec(0,1,0));
    }

    @Override
    public Visible_Cube createBoundingGeometry() {
        this.makeCorners(false);
        this.makeCubicEdgesAndFacesNoUpdate();
        this.rectangles.add(new Rectangle.Colored(this.getBoundingFace(Faces.FRONT_X), new vec4.FloatVec(1, 0, 0, 1), true));
        this.rectangles.add(new Rectangle.Colored(this.getBoundingFace(Faces.BACK_X), new vec4.FloatVec(1, 0, 0, 1), true));
        this.rectangles.add(new Rectangle.Colored(this.getBoundingFace(Faces.RIGHT_Z), new vec4.FloatVec(1, 0, 0, 1), true));
        this.rectangles.add(new Rectangle.Colored(this.getBoundingFace(Faces.LEFT_Z), new vec4.FloatVec(1, 0, 0, 1), true));
        this.rectangles.add(new Rectangle.Colored(this.getBoundingFace(Faces.TOP_Y), new vec4.FloatVec(1, 0, 0, 1), true));
        this.rectangles.add(new Rectangle.Colored(this.getBoundingFace(Faces.BOTTOM_Y), new vec4.FloatVec(1, 0, 0, 1), true));
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
        for (Rectangle.Colored rectangle : this.rectangles)
            rectangle.draw(Tessellator.getInstance().getBuffer(), 1);
    }

    @Override
    public List<Rectangle.Colored> getRenderPolygons() {
        return this.rectangles;
    }
}
