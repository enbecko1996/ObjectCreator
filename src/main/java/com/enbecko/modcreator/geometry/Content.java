package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.GlobalRenderSetting;
import com.enbecko.modcreator.OpenGLHelperEnbecko;
import com.enbecko.modcreator.linalg.Face3D;
import com.enbecko.modcreator.linalg.Line3D;
import com.enbecko.modcreator.linalg.vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbec on 21.02.2017.
 */
public abstract class Content {
    protected final vec3[] boundingCornersInBoneCoords;
    final vec3 positionInBoneCoords;
    private final List<ContentHolder> parents = new ArrayList<ContentHolder>();
    @Nonnull
    private final Bone parentBone;
    protected Line3D[] boundingEdgesInBoneCoords;
    protected Face3D[] boundingFacesInBoneCoords;
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
    public void removeMe() {
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

    @SideOnly(Side.CLIENT)
    public abstract void render(GlobalRenderSetting renderPass);

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
     * Just a Reminder-Method to create geometry for every content.
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

    public abstract static class CuboidContent extends Content {
        double xSize, ySize, zSize;

        protected CuboidContent(Bone parentBone, vec3 positionInBoneCoords, double xSize, double ySize, double zSize) {
            super(parentBone, positionInBoneCoords, 8);
            if (xSize > 0 && ySize > 0 && zSize > 0) {
                this.xSize = xSize;
                this.ySize = ySize;
                this.zSize = zSize;
            } else {
                throw new RuntimeException("This doesn't create a valid cuboid: xSize = " + xSize + ", ySize = " + ySize + ", zSize = " + zSize);
            }
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
            //FRONT FACE COUNTERCLOCKWISE (CCW)
            this.boundingCornersInBoneCoords[0].update(pos.getXD(), pos.getYD(), pos.getZD(), false);
            this.boundingCornersInBoneCoords[1].update(pos.getXD(), pos.getYD(), pos.getZD() + zSize, false);
            this.boundingCornersInBoneCoords[2].update(pos.getXD(), pos.getYD() + ySize, pos.getZD() + zSize, false);
            this.boundingCornersInBoneCoords[3].update(pos.getXD(), pos.getYD() + ySize, pos.getZD(), false);

            //BACK FACE CLOCKWISE (CW)
            this.boundingCornersInBoneCoords[4].update(pos.getXD() + xSize, pos.getYD(), pos.getZD() + zSize, false);
            this.boundingCornersInBoneCoords[5].update(pos.getXD() + xSize, pos.getYD(), pos.getZD(), false);
            this.boundingCornersInBoneCoords[6].update(pos.getXD() + xSize, pos.getYD() + ySize, pos.getZD(), false);
            this.boundingCornersInBoneCoords[7].update(pos.getXD() + xSize, pos.getYD() + ySize, pos.getZD() + zSize, false);
        }

        public void makeCorners(boolean changeable) {
            vec3 pos = this.getPositionInBoneCoords();
            //FRONT FACE COUNTERCLOCKWISE (CCW)
            this.boundingCornersInBoneCoords[0] = new vec3.DoubleVec(pos.getXD(), pos.getYD(), pos.getZD(), false);
            this.boundingCornersInBoneCoords[1] = new vec3.DoubleVec(pos.getXD(), pos.getYD(), pos.getZD() + zSize, false);
            this.boundingCornersInBoneCoords[2] = new vec3.DoubleVec(pos.getXD(), pos.getYD() + ySize, pos.getZD() + zSize, false);
            this.boundingCornersInBoneCoords[3] = new vec3.DoubleVec(pos.getXD(), pos.getYD() + ySize, pos.getZD(), false);

            //BACK FACE CLOCKWISE (CW)
            this.boundingCornersInBoneCoords[4] = new vec3.DoubleVec(pos.getXD() + xSize, pos.getYD(), pos.getZD() + zSize, false);
            this.boundingCornersInBoneCoords[5] = new vec3.DoubleVec(pos.getXD() + xSize, pos.getYD(), pos.getZD(), false);
            this.boundingCornersInBoneCoords[6] = new vec3.DoubleVec(pos.getXD() + xSize, pos.getYD() + ySize, pos.getZD(), false);
            this.boundingCornersInBoneCoords[7] = new vec3.DoubleVec(pos.getXD() + xSize, pos.getYD() + ySize, pos.getZD() + zSize, false);
            this.setCanChangeBoundingGeometry(changeable);
        }

        @Deprecated
        public void makeCubicEdgesAndFacesNoUpdate() {
            this.boundingEdgesInBoneCoords = new Line3D[12];
            this.boundingFacesInBoneCoords = new Face3D[6];
            //FRONT FACE FROM LOW_LEFT CCW TO LOW_LEFT
            this.boundingEdgesInBoneCoords[0] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[1]);
            this.boundingEdgesInBoneCoords[1] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[2]);
            this.boundingEdgesInBoneCoords[2] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[3]);
            this.boundingEdgesInBoneCoords[3] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[0]);

            //BACK FACE FROM LOW_LEFT CW TO LOW_LEFT
            this.boundingEdgesInBoneCoords[4] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[5]);
            this.boundingEdgesInBoneCoords[5] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[6]);
            this.boundingEdgesInBoneCoords[6] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[6], this.boundingCornersInBoneCoords[7]);
            this.boundingEdgesInBoneCoords[7] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[7], this.boundingCornersInBoneCoords[4]);

            //EDGE FROM FRONT TO BACK BEGINNING IN FRONT LOW LEFT
            //GOING CCW BACK TO LOW LEFT
            this.boundingEdgesInBoneCoords[8] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[5]);
            this.boundingEdgesInBoneCoords[9] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[4]);
            this.boundingEdgesInBoneCoords[10] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[7]);
            this.boundingEdgesInBoneCoords[11] = new Line3D.Line3DNoUpdate(this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[6]);

            //FRONT, RIGHT, BACK, LEFT, TOP, BOTTOM
            this.boundingFacesInBoneCoords[0] = new Face3D.FaceNoUpdate(this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[3]);
            this.boundingFacesInBoneCoords[1] = new Face3D.FaceNoUpdate(this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[7], this.boundingCornersInBoneCoords[2]);
            this.boundingFacesInBoneCoords[2] = new Face3D.FaceNoUpdate(this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[6], this.boundingCornersInBoneCoords[7]);
            this.boundingFacesInBoneCoords[3] = new Face3D.FaceNoUpdate(this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[6]);
            this.boundingFacesInBoneCoords[4] = new Face3D.FaceNoUpdate(this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[7], this.boundingCornersInBoneCoords[6]);
            this.boundingFacesInBoneCoords[5] = new Face3D.FaceNoUpdate(this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[0]);
        }

        @Deprecated
        public void makeCubicEdgesAndFacesAutoUpdate() {
            this.boundingEdgesInBoneCoords = new Line3D[12];
            this.boundingFacesInBoneCoords = new Face3D[6];
            //FRONT FACE FROM LOW_LEFT CCW TO LOW_LEFT
            this.boundingEdgesInBoneCoords[0] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[1]);
            this.boundingEdgesInBoneCoords[1] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[2]);
            this.boundingEdgesInBoneCoords[2] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[3]);
            this.boundingEdgesInBoneCoords[3] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[0]);

            //BACK FACE FROM LOW_LEFT CW TO LOW_LEFT
            this.boundingEdgesInBoneCoords[4] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[5]);
            this.boundingEdgesInBoneCoords[5] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[6]);
            this.boundingEdgesInBoneCoords[6] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[6], this.boundingCornersInBoneCoords[7]);
            this.boundingEdgesInBoneCoords[7] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[7], this.boundingCornersInBoneCoords[4]);

            //EDGE FROM FRONT TO BACK BEGINNING IN FRONT LOW LEFT
            //GOING CCW BACK TO LOW LEFT
            this.boundingEdgesInBoneCoords[8] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[5]);
            this.boundingEdgesInBoneCoords[9] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[4]);
            this.boundingEdgesInBoneCoords[10] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[7]);
            this.boundingEdgesInBoneCoords[11] = new Line3D.Line3DAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[6]);

            //FRONT, RIGHT, BACK, LEFT, TOP, BOTTOM
            //ALL CCW if viewed as Front
            this.boundingFacesInBoneCoords[0] = new Face3D.FaceAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[3]);
            this.boundingFacesInBoneCoords[1] = new Face3D.FaceAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[7], this.boundingCornersInBoneCoords[2]);
            this.boundingFacesInBoneCoords[2] = new Face3D.FaceAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[6], this.boundingCornersInBoneCoords[7]);
            this.boundingFacesInBoneCoords[3] = new Face3D.FaceAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[0], this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[6]);
            this.boundingFacesInBoneCoords[4] = new Face3D.FaceAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[3], this.boundingCornersInBoneCoords[2], this.boundingCornersInBoneCoords[7], this.boundingCornersInBoneCoords[6]);
            this.boundingFacesInBoneCoords[5] = new Face3D.FaceAutoUpdateOnVecChange(this.boundingCornersInBoneCoords[5], this.boundingCornersInBoneCoords[4], this.boundingCornersInBoneCoords[1], this.boundingCornersInBoneCoords[0]);
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
        public boolean isColliding(Content content) {
            for (int k = 0; k < content.getBoundingCornerCount(); k++) {
                vec3 act = content.getCorner(k);
                if (this.isInside(act))
                    return true;
            }
            return false;
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

        public String getGeometryInfo() {
            return "{pos = " + this.positionInBoneCoords + ", xSize = " + this.xSize +
                    ", ySize = " + this.ySize + ", zSize = " + this.zSize +"}";
        }

        public String toString() {
            return "CuboidContent: " + this.getGeometryInfo();
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void render(GlobalRenderSetting renderPass) {
            for (Line3D line : this.boundingEdgesInBoneCoords)
                OpenGLHelperEnbecko.drawLine(line, 4);
        }
    }

    /**
     * Created by enbec on 21.02.2017.
     */
    public abstract static class CubicContent extends CuboidContent {
        protected CubicContent(Bone parentBone, vec3.IntVec positonInBoneCoords, int size) {
            super(parentBone, positonInBoneCoords, size, size, size);
        }

        @Override
        public void makeCorners(boolean changeable) {
            super.makeCorners(changeable);
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
