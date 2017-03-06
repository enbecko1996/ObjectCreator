package com.enbecko.modcreator.Visible;

import com.enbecko.modcreator.LocalRenderSetting;
import com.enbecko.modcreator.LocalRenderSetting.CrossedByRayTrace;
import com.enbecko.modcreator.RenderQuadrilateral;
import com.enbecko.modcreator.contentholder.Bone;
import com.enbecko.modcreator.contentholder.Content;
import com.enbecko.modcreator.events.ManipulatingEvent;
import com.enbecko.modcreator.linalg.*;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbec on 24.02.2017.
 */
public class Cube extends Content.CubicContent implements ITextured <RenderQuadrilateral.Colored> {
    private final int dimension;
    private List<RenderQuadrilateral.Colored> rectangles = new ArrayList<RenderQuadrilateral.Colored>();

    public Cube(Bone parentBone, vec3.IntVec positionInBone, int dimension) {
        super(parentBone, positionInBone, dimension, vec_n.vecPrec.INT);
        this.dimension = dimension;
        Quadrilateral3D face = new Quadrilateral3D.AutoUpdateOnVecChange(new vec3.FloatVec(0,0,0),
                new vec3.FloatVec(0,0,1), new vec3.FloatVec(0,1,1), new vec3.FloatVec(0,1,0));
    }

    @Override
    public Cube createBoundingGeometry() {
        this.makeHexahedralEdgesAndFacesNoUpdate();
        Quadrilateral3D face;
        this.rectangles.add((RenderQuadrilateral.Colored) (face = this.getBoundingFace(Faces.FRONT_X)).setRenderer(new RenderQuadrilateral.Colored(face, new vec4.FloatVec(1, 0, 0, 1), true)));
        this.rectangles.add((RenderQuadrilateral.Colored) (face = this.getBoundingFace(Faces.BACK_X)).setRenderer(new RenderQuadrilateral.Colored(face, new vec4.FloatVec(1, 0, 0, 1), true)));
        this.rectangles.add((RenderQuadrilateral.Colored) (face = this.getBoundingFace(Faces.RIGHT_Z)).setRenderer(new RenderQuadrilateral.Colored(face, new vec4.FloatVec(1, 0, 0, 1), true)));
        this.rectangles.add((RenderQuadrilateral.Colored) (face = this.getBoundingFace(Faces.LEFT_Z)).setRenderer(new RenderQuadrilateral.Colored(face, new vec4.FloatVec(1, 0, 0, 1), true)));
        this.rectangles.add((RenderQuadrilateral.Colored) (face = this.getBoundingFace(Faces.TOP_Y)).setRenderer(new RenderQuadrilateral.Colored(face, new vec4.FloatVec(1, 0, 0, 1), true)));
        this.rectangles.add((RenderQuadrilateral.Colored) (face = this.getBoundingFace(Faces.BOTTOM_Y)).setRenderer(new RenderQuadrilateral.Colored(face, new vec4.FloatVec(1, 0, 0, 1), true)));
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
    public void manipulateMe(ManipulatingEvent event, RayTrace3D rayTrace3D) {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(LocalRenderSetting... localRenderSettings) {
        for (Quadrilateral3D face : this.boundingFacesInBoneCoords) {
            if(face != null) {
                if (face.getRenderer() != null)
                    face.getRenderer().draw(Tessellator.getInstance().getBuffer(), 1);
            }
        }
        for (LocalRenderSetting localRenderSetting : localRenderSettings) {
            switch (localRenderSetting.getType()) {
                case CROSSED_BY_RAYTRACE:
                    Quadrilateral3D crossed = this.getCrossedFace(((CrossedByRayTrace)localRenderSetting).getTheRayTrace());
                    if (crossed != null && crossed.getRenderer() != null)
                        crossed.getRenderer().draw(Tessellator.getInstance().getBuffer(), 1, localRenderSetting);
                    break;
            }
        }
    }

    @Override
    public List<RenderQuadrilateral.Colored> getRenderPolygons() {
        return this.rectangles;
    }
}
