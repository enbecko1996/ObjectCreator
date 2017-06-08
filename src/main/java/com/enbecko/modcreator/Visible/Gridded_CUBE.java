package com.enbecko.modcreator.Visible;

import com.enbecko.modcreator.LocalRenderSetting;
import com.enbecko.modcreator.RenderQuadrilateral;
import com.enbecko.modcreator.contentholder.Bone;
import com.enbecko.modcreator.contentholder.Content;
import com.enbecko.modcreator.contentholder.Grid;
import com.enbecko.modcreator.events.ManipulatingEvent;
import com.enbecko.modcreator.linalg.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbec on 24.02.2017.
 */
public class Gridded_CUBE extends Content.CubicContent implements ITextured <RenderQuadrilateral.Colored>, IGridded{
    private final int dimension;
    private List<RenderQuadrilateral.Colored> rectangles = new ArrayList<RenderQuadrilateral.Colored>();
    private final Grid parentGrid;
    private final vec3.IntVec positionInGrid;

    public Gridded_CUBE(Bone parentBone, @Nonnull Grid parentGrid, vec3.IntVec positionInGrid, int dimension) {
        super(parentBone,
                new vec3.DoubleVec(positionInGrid.getX() * parentGrid.getUnitLength(), positionInGrid.getY() * parentGrid.getUnitLength(), positionInGrid.getZ() * parentGrid.getUnitLength()),
                dimension, vec_n.vecPrec.INT);
        this.positionInGrid = positionInGrid;
        this.dimension = dimension;
        this.parentGrid = parentGrid;
    }

    @Override
    public Gridded_CUBE createBoundingGeometry() {
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
    public vec3 getPositionInBoneCoords() {
        return this.positionInBoneCoords;
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
    public void render(VertexBuffer buffer, LocalRenderSetting... localRenderSettings) {
        GlStateManager.bindTexture(0);
        for (Quadrilateral3D face : this.boundingFacesInBoneCoords) {
            if(face != null) {
                if (face.getRenderer() != null) {
                    face.getRenderer().draw(buffer, 1);
                }
            }
        }
        for (Quadrilateral3D face : this.boundingFacesInBoneCoords) {
            if(face != null) {
                if (face.getRenderer() != null) {
                    face.getRenderer().draw(buffer, 1, localRenderSettings);
                }
            }
        }
    }

    @Override
    public List<RenderQuadrilateral.Colored> getRenderPolygons() {
        return this.rectangles;
    }

    public String toString() {
        return "Colored Gridded_CUBE " + this.getGeometryInfo();
    }

    @Override
    public vec3.IntVec getPositionInGridInt() {
        return this.positionInGrid;
    }

    @Override
    public int getXDim() {
        return this.dimension;
    }

    @Override
    public int getYDim() {
        return this.dimension;
    }

    @Override
    public int getZDim() {
        return this.dimension;
    }

    @Override
    public Grid getParentGrid() {
        return this.parentGrid;
    }
}
