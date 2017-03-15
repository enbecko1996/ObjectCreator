package com.enbecko.modcreator.contentholder;

import com.enbecko.modcreator.LocalRenderSetting;
import com.enbecko.modcreator.OpenGLHelperEnbecko;
import com.enbecko.modcreator.events.BlockSetModes.BlockSetMode;
import com.enbecko.modcreator.events.ManipulatingEvent;
import com.enbecko.modcreator.linalg.*;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbec on 21.02.2017.
 */
public abstract class Content {
    protected final vec3[] boundingCornersInBoneCoords;
    protected final vec3 positionInBoneCoords;
    private final List<ContentHolder> parents = new ArrayList<ContentHolder>();
    @Nonnull
    protected final Bone parentBone;
    protected Line3D[] boundingEdgesInBoneCoords;
    protected Quadrilateral3D[] boundingFacesInBoneCoords;
    private boolean canChangePosition;
    private boolean canChangeBoundingGeometry;

    public Content(Bone parentBone, vec3 positionInBoneCoords, int size, boolean canChangePosition) {
        this.positionInBoneCoords = vec3.newVecWithPrecision(positionInBoneCoords.getPrecision(), positionInBoneCoords, false).setChangeable(canChangePosition);
        this.parentBone = parentBone;
        this.boundingCornersInBoneCoords = new vec3[size];
        this.canChangePosition = canChangePosition;
    }

    public Content(Bone parentBone, vec3 positionInBoneCoords, int size) {
        this(parentBone, positionInBoneCoords, size, true);
    }

    public Content(Bone parentBone, ContentHolder parent, vec3 positionInBoneCoords, int size) {
        this(parentBone, positionInBoneCoords, size);
        this.addParent(parent);
    }

    public void setCanChangePosition(boolean canChangePosition) {
        this.canChangePosition = canChangePosition;
        this.positionInBoneCoords.setChangeable(canChangePosition);
    }

    public void setCanChangeBoundingGeometry(boolean canChangeGeometry) {
        this.canChangeBoundingGeometry = canChangeGeometry;
        for (int k = 0; k < this.boundingCornersInBoneCoords.length; k++) {
            this.boundingCornersInBoneCoords[k].setChangeable(canChangeGeometry);
        }
    }

    @SuppressWarnings("unchecked")
    void removeMe() {
        for (int k = 0; k < this.getParentCount(); k++)
            this.getParent(k).removeChild(this);
    }

    public void updatePosition(double x, double y, double z) {
        this.positionInBoneCoords.update(x, y, z);
    }

    public void updatePosition(vec3 other) {
        this.positionInBoneCoords.update(other);
    }

    @Nonnull
    public Bone getParentBone() {
        return this.parentBone;
    }

    public vec3 getPositionInBoneCoords() {
        return this.positionInBoneCoords;
    }

    public vec3 getCorner(int pos) {
        return this.boundingCornersInBoneCoords[pos];
    }

    public Line3D getEdge(int pos) {
        return this.boundingEdgesInBoneCoords[pos];
    }

    public abstract boolean isColliding(Content other);

    public abstract boolean isFullInside(Content other);

    public abstract boolean isInside(vec3 vec);

    public abstract vec3 checkIfCrosses(RayTrace3D rayTrace3D);

    public abstract FaceCrossPosAngle getCrossedFaceVecAndAngle(RayTrace3D rayTrace3D, BlockSetMode editMode);

    @SideOnly(Side.CLIENT)
    public abstract void manipulateMe(ManipulatingEvent event, RayTrace3D rayTrace3D);

    @SideOnly(Side.CLIENT)
    public abstract void render(VertexBuffer buffer, LocalRenderSetting... localRenderSettings);

    public double getMaxX() {
        double max = Double.NEGATIVE_INFINITY, tmp;
        for (vec3 cornersInBoneCoord : this.boundingCornersInBoneCoords) {
            if ((tmp = cornersInBoneCoord.getXD()) > max)
                max = tmp;
        }
        return max;
    }

    public double getMaxY() {
        double max = Double.NEGATIVE_INFINITY, tmp;
        for (vec3 cornersInBoneCoord : this.boundingCornersInBoneCoords) {
            if ((tmp = cornersInBoneCoord.getYD()) > max)
                max = tmp;
        }
        return max;
    }

    public double getMaxZ() {
        double max = Double.NEGATIVE_INFINITY, tmp;
        for (vec3 cornersInBoneCoord : this.boundingCornersInBoneCoords) {
            if ((tmp = cornersInBoneCoord.getZD()) > max)
                max = tmp;
        }
        return max;
    }

    public double getMinX() {
        double min = Double.POSITIVE_INFINITY, tmp;
        for (vec3 cornersInBoneCoord : this.boundingCornersInBoneCoords) {
            if ((tmp = cornersInBoneCoord.getXD()) < min)
                min = tmp;
        }
        return min;
    }

    public double getMinY() {
        double min = Double.POSITIVE_INFINITY, tmp;
        for (vec3 cornersInBoneCoord : this.boundingCornersInBoneCoords) {
            if ((tmp = cornersInBoneCoord.getYD()) < min)
                min = tmp;
        }
        return min;
    }

    public double getMinZ() {
        double min = Double.POSITIVE_INFINITY, tmp;
        for (vec3 cornersInBoneCoord : this.boundingCornersInBoneCoords) {
            if ((tmp = cornersInBoneCoord.getZD()) < min)
                min = tmp;
        }
        return min;
    }

    /**
     * TODO
     * Just a Reminder-Method to create contentholder for every content.
     * Get's called automatically
     */
    public abstract Content createBoundingGeometry();

    public abstract void updateBoundingGeometry();

    public int getBoundingCornerCount() {
        return this.boundingCornersInBoneCoords.length;
    }

    public int getBoundingEdgesCount() {
        return this.boundingEdgesInBoneCoords.length;
    }

    public boolean addParent(ContentHolder contentHolder) {
        if (!this.parents.contains(contentHolder)) {
            this.parents.add(contentHolder);
            return true;
        }
        return false;
    }

    public int getParentCount() {
        return this.parents.size();
    }

    public ContentHolder getParent(int pos) {
        return this.parents.get(pos);
    }

    public abstract static class PolyhedralContent extends Content {
        public PolyhedralContent(Bone parentBone, vec3 positionInBoneCoords, boolean canChangePos, vec3... vec3s) {
            super(parentBone, positionInBoneCoords, vec3s.length, canChangePos);
            for (int k = 0; k < this.boundingCornersInBoneCoords.length; k++) {
                this.boundingCornersInBoneCoords[k] = vec3s[k];
            }
        }

        public PolyhedralContent(Bone parentBone, vec3 positionInBoneCoords, vec3... vec3s) {
            super(parentBone, positionInBoneCoords, vec3s.length);
            for (int k = 0; k < this.boundingCornersInBoneCoords.length; k++)
                this.boundingCornersInBoneCoords[k] = vec3s[k];
        }
    }

    public abstract static class HexahedralContent extends PolyhedralContent {

        /**
         * Per Convention a Hexahedral has the faces with v1, v2, v3, v4 and v5, v6, v7, v8.
         * The other faces are made by those connections: v1 - v5, v2 - v6, v3 - v7, v4 - v8.
         */
        public HexahedralContent(Bone parentBone, vec3 positionInBoneCoords, boolean canChangePos, vec3 v1, vec3 v2, vec3 v3, vec3 v4, vec3 v5, vec3 v6, vec3 v7, vec3 v8) {
            super(parentBone, positionInBoneCoords, canChangePos, v1, v2, v3, v4, v5, v6, v7, v8);
        }

        public HexahedralContent(Bone parentBone, vec3 positionInBoneCoords, vec3 v1, vec3 v2, vec3 v3, vec3 v4, vec3 v5, vec3 v6, vec3 v7, vec3 v8) {
            super(parentBone, positionInBoneCoords, v1, v2, v3, v4, v5, v6, v7, v8);
        }

        @Deprecated
        public void makeHexahedralEdgesAndFacesNoUpdate() {
            this.boundingEdgesInBoneCoords = new Line3D[12];
            this.boundingFacesInBoneCoords = new Quadrilateral3D[6];
            //FRONT_X FACE FROM LOW_LEFT CCW TO LOW_LEFT
            this.boundingEdgesInBoneCoords[0] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[1]);
            this.boundingEdgesInBoneCoords[1] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[2]);
            this.boundingEdgesInBoneCoords[2] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[3]);
            this.boundingEdgesInBoneCoords[3] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[0]);

            //BACK_X FACE FROM LOW_LEFT CW TO LOW_LEFT
            this.boundingEdgesInBoneCoords[4] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[5]);
            this.boundingEdgesInBoneCoords[5] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[6]);
            this.boundingEdgesInBoneCoords[6] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[6], this.boundingCornersInBoneCoords[7]);
            this.boundingEdgesInBoneCoords[7] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[7], this.boundingCornersInBoneCoords[4]);

            //EDGE FROM FRONT_X TO BACK_X BEGINNING IN FRONT_X LOW LEFT
            //GOING CCW BACK_X TO LOW LEFT
            this.boundingEdgesInBoneCoords[8] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[5]);
            this.boundingEdgesInBoneCoords[9] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[4]);
            this.boundingEdgesInBoneCoords[10] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[7]);
            this.boundingEdgesInBoneCoords[11] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[6]);

            //FRONT_X, RIGHT_Z, BACK_X, LEFT, TOP_Y, BOTTOM
            this.boundingFacesInBoneCoords[0] = new Quadrilateral3D.NoUpdate(this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[3]);
            this.boundingFacesInBoneCoords[1] = new Quadrilateral3D.NoUpdate(this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[7], this.boundingCornersInBoneCoords[2]);
            this.boundingFacesInBoneCoords[2] = new Quadrilateral3D.NoUpdate(this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[6], this.boundingCornersInBoneCoords[7]);
            this.boundingFacesInBoneCoords[3] = new Quadrilateral3D.NoUpdate(this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[6]);
            this.boundingFacesInBoneCoords[4] = new Quadrilateral3D.NoUpdate(this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[7], this.boundingCornersInBoneCoords[6]);
            this.boundingFacesInBoneCoords[5] = new Quadrilateral3D.NoUpdate(this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[0]);
        }

        @Deprecated
        public void makeHexahedralEdgesAndFacesAutoUpdate() {
            this.boundingEdgesInBoneCoords = new Line3D[12];
            this.boundingFacesInBoneCoords = new Quadrilateral3D[6];
            //FRONT_X FACE FROM LOW_LEFT CCW TO LOW_LEFT
            this.boundingEdgesInBoneCoords[0] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[1]);
            this.boundingEdgesInBoneCoords[1] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[2]);
            this.boundingEdgesInBoneCoords[2] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[3]);
            this.boundingEdgesInBoneCoords[3] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[0]);

            //BACK_X FACE FROM LOW_LEFT CW TO LOW_LEFT
            this.boundingEdgesInBoneCoords[4] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[5]);
            this.boundingEdgesInBoneCoords[5] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[6]);
            this.boundingEdgesInBoneCoords[6] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[6], this.boundingCornersInBoneCoords[7]);
            this.boundingEdgesInBoneCoords[7] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[7], this.boundingCornersInBoneCoords[4]);

            //EDGE FROM FRONT_X TO BACK_X BEGINNING IN FRONT_X LOW LEFT
            //GOING CCW BACK_X TO LOW LEFT
            this.boundingEdgesInBoneCoords[8] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[5]);
            this.boundingEdgesInBoneCoords[9] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[4]);
            this.boundingEdgesInBoneCoords[10] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[7]);
            this.boundingEdgesInBoneCoords[11] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[6]);

            //FRONT_X, RIGHT_Z, BACK_X, LEFT, TOP_Y, BOTTOM
            //ALL CCW if viewed as Front
            this.boundingFacesInBoneCoords[0] = new Quadrilateral3D.AutoUpdate(this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[3]);
            this.boundingFacesInBoneCoords[1] = new Quadrilateral3D.AutoUpdate(this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[7], this.boundingCornersInBoneCoords[2]);
            this.boundingFacesInBoneCoords[2] = new Quadrilateral3D.AutoUpdate(this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[6], this.boundingCornersInBoneCoords[7]);
            this.boundingFacesInBoneCoords[3] = new Quadrilateral3D.AutoUpdate(this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[6]);
            this.boundingFacesInBoneCoords[4] = new Quadrilateral3D.AutoUpdate(this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[7], this.boundingCornersInBoneCoords[6]);
            this.boundingFacesInBoneCoords[5] = new Quadrilateral3D.AutoUpdate(this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[0]);
        }

        @Override
        public boolean isColliding(Content content) {
            return content.getMaxX() > this.getMinX() && content.getMaxY() > this.getMinY() && content.getMaxZ() > this.getMinZ() &&
                    content.getMinX() < this.getMaxX() && content.getMinY() < this.getMaxY() && content.getMinZ() < this.getMaxZ();
        }

        @Override
        public boolean isFullInside(Content content) {
            for (int k = 0; k < content.getBoundingCornerCount(); k++) {
                vec3 act = content.getCorner(k);
                if (!this.isInside(act))
                    return false;
            }
            return true;
        }

        @Override
        public boolean isInside(vec3 vec) {
            if ((vec.getYD() < this.getMaxY() && vec.getYD() >= this.getMinY() && vec.getXD() < this.getMaxX() && vec.getXD() >= this.getMinX() && vec.getZD() < this.getMaxZ() && vec.getZD() >= this.getMinZ()))
                return true;
            return false;
        }
    }

    public abstract static class CuboidContent extends HexahedralContent {
        double xSize, ySize, zSize;

        protected CuboidContent(Bone parentBone, vec3 pos, double xSize, double ySize, double zSize, vec_n.vecPrec prec) {
            super(parentBone, pos,
                    //FRONT_X FACE COUNTERCLOCKWISE (CCW)
                    vec3.newVecWithPrecision(prec, pos.getXD(), pos.getYD(), pos.getZD(), false),
                    vec3.newVecWithPrecision(prec, pos.getXD(), pos.getYD(), pos.getZD() + zSize, false),
                    vec3.newVecWithPrecision(prec, pos.getXD(), pos.getYD() + ySize, pos.getZD() + zSize, false),
                    vec3.newVecWithPrecision(prec, pos.getXD(), pos.getYD() + ySize, pos.getZD(), false),

                    //BACK_X FACE CLOCKWISE (CW)
                    vec3.newVecWithPrecision(prec, pos.getXD() + xSize, pos.getYD(), pos.getZD() + zSize, false),
                    vec3.newVecWithPrecision(prec, pos.getXD() + xSize, pos.getYD(), pos.getZD(), false),
                    vec3.newVecWithPrecision(prec, pos.getXD() + xSize, pos.getYD() + ySize, pos.getZD(), false),
                    vec3.newVecWithPrecision(prec, pos.getXD() + xSize, pos.getYD() + ySize, pos.getZD() + zSize, false));
            if (xSize >= 0 && ySize >= 0 && zSize >= 0) {
                this.xSize = xSize;
                this.ySize = ySize;
                this.zSize = zSize;
            } else {
                throw new RuntimeException("This doesn't create a valid cuboid: xSize = " + xSize + ", ySize = " + ySize + ", zSize = " + zSize);
            }
        }

        @Override
        @Deprecated
        public void makeHexahedralEdgesAndFacesAutoUpdate() {
            this.boundingEdgesInBoneCoords = new Line3D[12];
            this.boundingFacesInBoneCoords = new Quadrilateral3D[6];
            //FRONT_X FACE FROM LOW_LEFT CCW TO LOW_LEFT
            this.boundingEdgesInBoneCoords[0] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[1]);
            this.boundingEdgesInBoneCoords[1] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[2]);
            this.boundingEdgesInBoneCoords[2] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[3]);
            this.boundingEdgesInBoneCoords[3] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[0]);

            //BACK_X FACE FROM LOW_LEFT CW TO LOW_LEFT
            this.boundingEdgesInBoneCoords[4] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[5]);
            this.boundingEdgesInBoneCoords[5] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[6]);
            this.boundingEdgesInBoneCoords[6] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[6], this.boundingCornersInBoneCoords[7]);
            this.boundingEdgesInBoneCoords[7] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[7], this.boundingCornersInBoneCoords[4]);

            //EDGE FROM FRONT_X TO BACK_X BEGINNING IN FRONT_X LOW LEFT
            //GOING CCW BACK_X TO LOW LEFT
            this.boundingEdgesInBoneCoords[8] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[5]);
            this.boundingEdgesInBoneCoords[9] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[4]);
            this.boundingEdgesInBoneCoords[10] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[7]);
            this.boundingEdgesInBoneCoords[11] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[6]);

            //FRONT_X, RIGHT_Z, BACK_X, LEFT, TOP_Y, BOTTOM
            //ALL CCW if viewed as Front
            this.boundingFacesInBoneCoords[0] = new Quadrilateral3D.AutoUpdate.Symmetrical(this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[3]);
            this.boundingFacesInBoneCoords[1] = new Quadrilateral3D.AutoUpdate.Symmetrical(this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[7], this.boundingCornersInBoneCoords[2]);
            this.boundingFacesInBoneCoords[2] = new Quadrilateral3D.AutoUpdate.Symmetrical(this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[6], this.boundingCornersInBoneCoords[7]);
            this.boundingFacesInBoneCoords[3] = new Quadrilateral3D.AutoUpdate.Symmetrical(this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[6]);
            this.boundingFacesInBoneCoords[4] = new Quadrilateral3D.AutoUpdate.Symmetrical(this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[7], this.boundingCornersInBoneCoords[6]);
            this.boundingFacesInBoneCoords[5] = new Quadrilateral3D.AutoUpdate.Symmetrical(this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[0]);
        }

        public void updateSize(double xSize, double ySize, double zSize) {
            if (xSize > 0 && ySize > 0 && zSize > 0) {
                this.xSize = xSize;
                this.ySize = ySize;
                this.zSize = zSize;
            } else {
                throw new RuntimeException("This doesn't create a valid cuboid: xSize = " + xSize + ", ySize = " + ySize + ", zSize = " + zSize);
            }
            this.updateBoundingGeometry();
        }

        public void update(double xMin, double yMin, double zMin, double xMax, double yMax, double zMax) {
            this.xSize = xMax - xMin;
            this.ySize = yMax - yMin;
            this.zSize = zMax - zMin;
            if (!(xSize > 0 && ySize > 0 && zSize > 0)) {
                throw new RuntimeException("This doesn't create a valid cuboid: xSize = " + xSize + ", ySize = " + ySize + ", zSize = " + zSize);
            }
            this.updatePosition(xMin, yMin, zMin);
            this.updateSize(this.xSize, this.ySize, this.zSize);
            this.updateBoundingGeometry();
        }

        public void update(vec3 pos, double xSize, double ySize, double zSize) {
            double xMin = pos.getXD(), yMin = pos.getYD(), zMin = pos.getZD(),
                    xMax = xMin + xSize, yMax = yMin + ySize, zMax = zMin + zSize;
            this.update(xMin, yMin, zMin, xMax, yMax, zMax);
        }

        @Override
        public void updateBoundingGeometry() {
            vec3 pos = this.getPositionInBoneCoords();
            //FRONT_X FACE COUNTERCLOCKWISE (CCW)
            this.boundingCornersInBoneCoords[0].update(pos.getXD(), pos.getYD(), pos.getZD(), false);
            this.boundingCornersInBoneCoords[1].update(pos.getXD(), pos.getYD(), pos.getZD() + zSize, false);
            this.boundingCornersInBoneCoords[2].update(pos.getXD(), pos.getYD() + ySize, pos.getZD() + zSize, false);
            this.boundingCornersInBoneCoords[3].update(pos.getXD(), pos.getYD() + ySize, pos.getZD(), false);

            //BACK_X FACE CLOCKWISE (CW)
            this.boundingCornersInBoneCoords[4].update(pos.getXD() + xSize, pos.getYD(), pos.getZD() + zSize, false);
            this.boundingCornersInBoneCoords[5].update(pos.getXD() + xSize, pos.getYD(), pos.getZD(), false);
            this.boundingCornersInBoneCoords[6].update(pos.getXD() + xSize, pos.getYD() + ySize, pos.getZD(), false);
            this.boundingCornersInBoneCoords[7].update(pos.getXD() + xSize, pos.getYD() + ySize, pos.getZD() + zSize, false);
        }

        @Override
        public double getMaxX() {
            return this.getPositionInBoneCoords().getXD() + this.xSize;
        }

        @Override
        public double getMaxY() {
            return this.getPositionInBoneCoords().getYD() + this.ySize;
        }

        @Override
        public double getMaxZ() {
            return this.getPositionInBoneCoords().getZD() + this.zSize;
        }

        @Override
        public double getMinX() {
            return this.getPositionInBoneCoords().getXD();
        }

        @Override
        public double getMinY() {
            return this.getPositionInBoneCoords().getYD();
        }

        @Override
        public double getMinZ() {
            return this.getPositionInBoneCoords().getZD();
        }

        @Override
        @Nullable
        public vec3 checkIfCrosses(RayTrace3D rayTrace3D) {
            vec3 vec = rayTrace3D.getVec();
            vec3 out;
            if (vec.getXD() > 0) {
                if ((out = this.getBoundingFace(Faces.FRONT_X).checkIfCrosses(rayTrace3D)) != null)
                    return out;
            } else if (vec.getXD() < 0)
                if ((out = this.getBoundingFace(Faces.BACK_X).checkIfCrosses(rayTrace3D)) != null)
                    return out;
            if (vec.getYD() > 0) {
                if ((out = this.getBoundingFace(Faces.BOTTOM_Y).checkIfCrosses(rayTrace3D)) != null)
                    return out;
            } else if (vec.getYD() < 0)
                if ((out = this.getBoundingFace(Faces.TOP_Y).checkIfCrosses(rayTrace3D)) != null)
                    return out;
            if (vec.getZD() > 0) {
                if ((out = this.getBoundingFace(Faces.LEFT_Z).checkIfCrosses(rayTrace3D)) != null)
                    return out;
            } else if (vec.getZD() < 0)
                if ((out = this.getBoundingFace(Faces.RIGHT_Z).checkIfCrosses(rayTrace3D)) != null)
                    return out;
            return null;
        }

        @Override
        public FaceCrossPosAngle getCrossedFaceVecAndAngle(RayTrace3D rayTrace3D, BlockSetMode editMode) {
            vec3 vec = rayTrace3D.getVec();
            vec3 tmp;
            Polygon3D tmp2;
            if (vec.getXD() > 0) {
                if ((tmp = (tmp2 = this.getBoundingFace(Faces.FRONT_X)).checkIfCrosses(rayTrace3D)) != null)
                    return new FaceCrossPosAngle(tmp2, tmp, tmp2.getAngleAndAngleNormal(rayTrace3D));
            } else if (vec.getXD() < 0)
                if ((tmp = (tmp2 = this.getBoundingFace(Faces.BACK_X)).checkIfCrosses(rayTrace3D)) != null)
                    return new FaceCrossPosAngle(tmp2, tmp, tmp2.getAngleAndAngleNormal(rayTrace3D));
            if (vec.getYD() > 0) {
                if ((tmp = (tmp2 = this.getBoundingFace(Faces.BOTTOM_Y)).checkIfCrosses(rayTrace3D)) != null)
                    return new FaceCrossPosAngle(tmp2, tmp, tmp2.getAngleAndAngleNormal(rayTrace3D));
            } else if (vec.getYD() < 0)
                if ((tmp = (tmp2 = this.getBoundingFace(Faces.TOP_Y)).checkIfCrosses(rayTrace3D)) != null)
                    return new FaceCrossPosAngle(tmp2, tmp, tmp2.getAngleAndAngleNormal(rayTrace3D));
            if (vec.getZD() > 0) {
                if ((tmp = (tmp2 = this.getBoundingFace(Faces.LEFT_Z)).checkIfCrosses(rayTrace3D)) != null)
                    return new FaceCrossPosAngle(tmp2, tmp, tmp2.getAngleAndAngleNormal(rayTrace3D));
            } else if (vec.getZD() < 0)
                if ((tmp = (tmp2 = this.getBoundingFace(Faces.RIGHT_Z)).checkIfCrosses(rayTrace3D)) != null)
                    return new FaceCrossPosAngle(tmp2, tmp, tmp2.getAngleAndAngleNormal(rayTrace3D));
            return null;
        }

        public String getGeometryInfo() {
            return "{pos = " + this.positionInBoneCoords + ", xSize = " + this.xSize +
                    ", ySize = " + this.ySize + ", zSize = " + this.zSize + "}";
        }

        public Quadrilateral3D getBoundingFace(Faces face) {
            switch (face) {
                case BACK_X:
                    return this.boundingFacesInBoneCoords[2];
                case TOP_Y:
                    return this.boundingFacesInBoneCoords[4];
                case RIGHT_Z:
                    return this.boundingFacesInBoneCoords[1];
                case FRONT_X:
                    return this.boundingFacesInBoneCoords[0];
                case BOTTOM_Y:
                    return this.boundingFacesInBoneCoords[5];
                case LEFT_Z:
                    return this.boundingFacesInBoneCoords[3];
            }
            throw new RuntimeException("no such face: " + face);
        }

        public Quadrilateral3D getBoundingFace(int pos) {
            return this.boundingFacesInBoneCoords[pos];
        }

        public String toString() {
            return "CuboidContent: " + this.getGeometryInfo();
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void render(VertexBuffer buffer, LocalRenderSetting... localRenderSettings) {
            for (Line3D line : this.boundingEdgesInBoneCoords)
                OpenGLHelperEnbecko.drawLine(line, 4);
        }

        public enum Faces {
            BACK_X, TOP_Y, RIGHT_Z, FRONT_X, BOTTOM_Y, LEFT_Z;
        }
    }

    /**
     * Created by enbec on 21.02.2017.
     */
    public abstract static class CubicContent extends CuboidContent {
        protected CubicContent(Bone parentBone, vec3.IntVec positonInBoneCoords, double size, vec_n.vecPrec prec) {
            super(parentBone, positonInBoneCoords, size, size, size, prec);
        }

        @Override
        public void updateSize(double xSize, double ySize, double zSize) {
            throw new RuntimeException("This is no valid method for a CUBIC content");
        }

        public void updateSize(int size) {
            super.updateSize(size, size, size);
        }
    }
}
