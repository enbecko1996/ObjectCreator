package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.linalg.Face3D;
import com.enbecko.modcreator.linalg.Line3D;
import com.enbecko.modcreator.linalg.vec3;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbec on 21.02.2017.
 */
public abstract class Content {
    protected final vec3[] cornersInBoneCoords;
    final vec3 positionInBoneCoords;
    private final List<ContentHolder> parents = new ArrayList<ContentHolder>();
    @Nonnull
    private final Bone parentBone;
    protected Line3D[] edgesInBoneCoords;
    protected Face3D[] facesInBoneCoords;
    private boolean canChangePosition;
    private boolean canChangeGeometry;

    public Content(Bone parentBone, vec3 positionInBoneCoords, int size, boolean canChangePosition) {
        this.positionInBoneCoords = vec3.newVecWithPrecision(positionInBoneCoords.getPrecision(), positionInBoneCoords, false).setChangeable(canChangePosition);
        this.parentBone = parentBone;
        this.cornersInBoneCoords = new vec3[size];
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

    public void setCanChangeGeometry(boolean canChangeGeometry) {
        this.canChangeGeometry = canChangeGeometry;
        for (int k = 0; k < this.cornersInBoneCoords.length; k++) {
            this.cornersInBoneCoords[k].setChangeable(canChangeGeometry);
        }
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
        return this.cornersInBoneCoords[pos];
    }

    public Line3D getEdge(int pos) {
        return this.edgesInBoneCoords[pos];
    }

    public abstract boolean isColliding(Content other);

    public abstract boolean isFullInside(Content other);

    public abstract boolean isInside(vec3 vec);

    public double getMaxX() {
        double max = Double.NEGATIVE_INFINITY, tmp;
        for (vec3 cornersInBoneCoord : this.cornersInBoneCoords) {
            if ((tmp = cornersInBoneCoord.getXD()) > max)
                max = tmp;
        }
        return max;
    }

    public double getMaxY() {
        double max = Double.NEGATIVE_INFINITY, tmp;
        for (vec3 cornersInBoneCoord : this.cornersInBoneCoords) {
            if ((tmp = cornersInBoneCoord.getYD()) > max)
                max = tmp;
        }
        return max;
    }

    public double getMaxZ() {
        double max = Double.NEGATIVE_INFINITY, tmp;
        for (vec3 cornersInBoneCoord : this.cornersInBoneCoords) {
            if ((tmp = cornersInBoneCoord.getZD()) > max)
                max = tmp;
        }
        return max;
    }

    public double getMinX() {
        double min = Double.POSITIVE_INFINITY, tmp;
        for (vec3 cornersInBoneCoord : this.cornersInBoneCoords) {
            if ((tmp = cornersInBoneCoord.getXD()) < min)
                min = tmp;
        }
        return min;
    }

    public double getMinY() {
        double min = Double.POSITIVE_INFINITY, tmp;
        for (vec3 cornersInBoneCoord : this.cornersInBoneCoords) {
            if ((tmp = cornersInBoneCoord.getYD()) < min)
                min = tmp;
        }
        return min;
    }

    public double getMinZ() {
        double min = Double.POSITIVE_INFINITY, tmp;
        for (vec3 cornersInBoneCoord : this.cornersInBoneCoords) {
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
    public abstract void createGeometry();

    public abstract void updateGeometry();

    public int getCornerCount() {
        return this.cornersInBoneCoords.length;
    }

    public int getEdgesCount() {
        return this.edgesInBoneCoords.length;
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
            this.updateGeometry();
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
            this.updateGeometry();
        }

        @Override
        public void updateGeometry() {
            vec3 pos = this.getPositionInBoneCoords();
            //FRONT FACE COUNTERCLOCKWISE (CCW)
            this.cornersInBoneCoords[0].update(pos.getXD(), pos.getYD(), pos.getZD(), false);
            this.cornersInBoneCoords[1].update(pos.getXD(), pos.getYD(), pos.getZD() + zSize, false);
            this.cornersInBoneCoords[2].update(pos.getXD(), pos.getYD() + ySize, pos.getZD() + zSize, false);
            this.cornersInBoneCoords[3].update(pos.getXD(), pos.getYD() + ySize, pos.getZD(), false);

            //BACK FACE CLOCKWISE (CW)
            this.cornersInBoneCoords[4].update(pos.getXD() + xSize, pos.getYD(), pos.getZD() + zSize, false);
            this.cornersInBoneCoords[5].update(pos.getXD() + xSize, pos.getYD(), pos.getZD(), false);
            this.cornersInBoneCoords[6].update(pos.getXD() + xSize, pos.getYD() + ySize, pos.getZD(), false);
            this.cornersInBoneCoords[7].update(pos.getXD() + xSize, pos.getYD() + ySize, pos.getZD() + zSize, false);
        }

        public void makeCorners(boolean changeable) {
            vec3 pos = this.getPositionInBoneCoords();
            //FRONT FACE COUNTERCLOCKWISE (CCW)
            this.cornersInBoneCoords[0] = new vec3.DoubleVec(pos.getXD(), pos.getYD(), pos.getZD(), false);
            this.cornersInBoneCoords[1] = new vec3.DoubleVec(pos.getXD(), pos.getYD(), pos.getZD() + zSize, false);
            this.cornersInBoneCoords[2] = new vec3.DoubleVec(pos.getXD(), pos.getYD() + ySize, pos.getZD() + zSize, false);
            this.cornersInBoneCoords[3] = new vec3.DoubleVec(pos.getXD(), pos.getYD() + ySize, pos.getZD(), false);

            //BACK FACE CLOCKWISE (CW)
            this.cornersInBoneCoords[4] = new vec3.DoubleVec(pos.getXD() + xSize, pos.getYD(), pos.getZD() + zSize, false);
            this.cornersInBoneCoords[5] = new vec3.DoubleVec(pos.getXD() + xSize, pos.getYD(), pos.getZD(), false);
            this.cornersInBoneCoords[6] = new vec3.DoubleVec(pos.getXD() + xSize, pos.getYD() + ySize, pos.getZD(), false);
            this.cornersInBoneCoords[7] = new vec3.DoubleVec(pos.getXD() + xSize, pos.getYD() + ySize, pos.getZD() + zSize, false);
            this.setCanChangeGeometry(changeable);
        }

        @Deprecated
        public void makeCubicEdgesAndFacesNoUpdate() {
            this.edgesInBoneCoords = new Line3D[12];
            this.facesInBoneCoords = new Face3D[6];
            //FRONT FACE FROM LOW_LEFT CCW TO LOW_LEFT
            this.edgesInBoneCoords[0] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[0], this.cornersInBoneCoords[1]);
            this.edgesInBoneCoords[1] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[1], this.cornersInBoneCoords[2]);
            this.edgesInBoneCoords[2] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[2], this.cornersInBoneCoords[3]);
            this.edgesInBoneCoords[3] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[3], this.cornersInBoneCoords[0]);

            //BACK FACE FROM LOW_LEFT CW TO LOW_LEFT
            this.edgesInBoneCoords[4] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[4], this.cornersInBoneCoords[5]);
            this.edgesInBoneCoords[5] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[5], this.cornersInBoneCoords[6]);
            this.edgesInBoneCoords[6] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[6], this.cornersInBoneCoords[7]);
            this.edgesInBoneCoords[7] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[7], this.cornersInBoneCoords[4]);

            //EDGE FROM FRONT TO BACK BEGINNING IN FRONT LOW LEFT
            //GOING CCW BACK TO LOW LEFT
            this.edgesInBoneCoords[8] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[0], this.cornersInBoneCoords[5]);
            this.edgesInBoneCoords[9] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[1], this.cornersInBoneCoords[4]);
            this.edgesInBoneCoords[10] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[2], this.cornersInBoneCoords[7]);
            this.edgesInBoneCoords[11] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[3], this.cornersInBoneCoords[6]);

            //FRONT, RIGHT, BACK, LEFT, TOP, BOTTOM
            this.facesInBoneCoords[0] = new Face3D.FaceNoUpdate(this.cornersInBoneCoords[0], this.cornersInBoneCoords[1], this.cornersInBoneCoords[2], this.cornersInBoneCoords[3]);
            this.facesInBoneCoords[1] = new Face3D.FaceNoUpdate(this.cornersInBoneCoords[1], this.cornersInBoneCoords[4], this.cornersInBoneCoords[7], this.cornersInBoneCoords[2]);
            this.facesInBoneCoords[2] = new Face3D.FaceNoUpdate(this.cornersInBoneCoords[4], this.cornersInBoneCoords[5], this.cornersInBoneCoords[6], this.cornersInBoneCoords[7]);
            this.facesInBoneCoords[3] = new Face3D.FaceNoUpdate(this.cornersInBoneCoords[5], this.cornersInBoneCoords[0], this.cornersInBoneCoords[3], this.cornersInBoneCoords[6]);
            this.facesInBoneCoords[4] = new Face3D.FaceNoUpdate(this.cornersInBoneCoords[3], this.cornersInBoneCoords[2], this.cornersInBoneCoords[7], this.cornersInBoneCoords[6]);
            this.facesInBoneCoords[5] = new Face3D.FaceNoUpdate(this.cornersInBoneCoords[5], this.cornersInBoneCoords[4], this.cornersInBoneCoords[1], this.cornersInBoneCoords[0]);
        }

        @Deprecated
        public void makeCubicEdgesAndFacesAutoUpdate() {
            this.edgesInBoneCoords = new Line3D[12];
            this.facesInBoneCoords = new Face3D[6];
            //FRONT FACE FROM LOW_LEFT CCW TO LOW_LEFT
            this.edgesInBoneCoords[0] = new Line3D.Line3DAutoUpdateOnVecChange(this.cornersInBoneCoords[0], this.cornersInBoneCoords[1]);
            this.edgesInBoneCoords[1] = new Line3D.Line3DAutoUpdateOnVecChange(this.cornersInBoneCoords[1], this.cornersInBoneCoords[2]);
            this.edgesInBoneCoords[2] = new Line3D.Line3DAutoUpdateOnVecChange(this.cornersInBoneCoords[2], this.cornersInBoneCoords[3]);
            this.edgesInBoneCoords[3] = new Line3D.Line3DAutoUpdateOnVecChange(this.cornersInBoneCoords[3], this.cornersInBoneCoords[0]);

            //BACK FACE FROM LOW_LEFT CW TO LOW_LEFT
            this.edgesInBoneCoords[4] = new Line3D.Line3DAutoUpdateOnVecChange(this.cornersInBoneCoords[4], this.cornersInBoneCoords[5]);
            this.edgesInBoneCoords[5] = new Line3D.Line3DAutoUpdateOnVecChange(this.cornersInBoneCoords[5], this.cornersInBoneCoords[6]);
            this.edgesInBoneCoords[6] = new Line3D.Line3DAutoUpdateOnVecChange(this.cornersInBoneCoords[6], this.cornersInBoneCoords[7]);
            this.edgesInBoneCoords[7] = new Line3D.Line3DAutoUpdateOnVecChange(this.cornersInBoneCoords[7], this.cornersInBoneCoords[4]);

            //EDGE FROM FRONT TO BACK BEGINNING IN FRONT LOW LEFT
            //GOING CCW BACK TO LOW LEFT
            this.edgesInBoneCoords[8] = new Line3D.Line3DAutoUpdateOnVecChange(this.cornersInBoneCoords[0], this.cornersInBoneCoords[5]);
            this.edgesInBoneCoords[9] = new Line3D.Line3DAutoUpdateOnVecChange(this.cornersInBoneCoords[1], this.cornersInBoneCoords[4]);
            this.edgesInBoneCoords[10] = new Line3D.Line3DAutoUpdateOnVecChange(this.cornersInBoneCoords[2], this.cornersInBoneCoords[7]);
            this.edgesInBoneCoords[11] = new Line3D.Line3DAutoUpdateOnVecChange(this.cornersInBoneCoords[3], this.cornersInBoneCoords[6]);

            //FRONT, RIGHT, BACK, LEFT, TOP, BOTTOM
            //ALL CCW if viewed as Front
            this.facesInBoneCoords[0] = new Face3D.FaceAutoUpdateOnVecChange(this.cornersInBoneCoords[0], this.cornersInBoneCoords[1], this.cornersInBoneCoords[2], this.cornersInBoneCoords[3]);
            this.facesInBoneCoords[1] = new Face3D.FaceAutoUpdateOnVecChange(this.cornersInBoneCoords[1], this.cornersInBoneCoords[4], this.cornersInBoneCoords[7], this.cornersInBoneCoords[2]);
            this.facesInBoneCoords[2] = new Face3D.FaceAutoUpdateOnVecChange(this.cornersInBoneCoords[4], this.cornersInBoneCoords[5], this.cornersInBoneCoords[6], this.cornersInBoneCoords[7]);
            this.facesInBoneCoords[3] = new Face3D.FaceAutoUpdateOnVecChange(this.cornersInBoneCoords[5], this.cornersInBoneCoords[0], this.cornersInBoneCoords[3], this.cornersInBoneCoords[6]);
            this.facesInBoneCoords[4] = new Face3D.FaceAutoUpdateOnVecChange(this.cornersInBoneCoords[3], this.cornersInBoneCoords[2], this.cornersInBoneCoords[7], this.cornersInBoneCoords[6]);
            this.facesInBoneCoords[5] = new Face3D.FaceAutoUpdateOnVecChange(this.cornersInBoneCoords[5], this.cornersInBoneCoords[4], this.cornersInBoneCoords[1], this.cornersInBoneCoords[0]);
        }

        @Override
        public double getMaxX() {
            return this.cornersInBoneCoords[4].getXD();
        }

        @Override
        public double getMaxY() {
            return this.cornersInBoneCoords[2].getYD();
        }

        @Override
        public double getMaxZ() {
            return this.cornersInBoneCoords[1].getZD();
        }

        @Override
        public double getMinX() {
            return this.cornersInBoneCoords[0].getXD();
        }

        @Override
        public double getMinY() {
            return this.cornersInBoneCoords[0].getYD();
        }

        @Override
        public double getMinZ() {
            return this.cornersInBoneCoords[0].getZD();
        }

        @Override
        public boolean isColliding(Content content) {
            for (int k = 0; k < content.getCornerCount(); k++) {
                vec3 act = content.getCorner(k);
                if (this.isInside(act))
                    return true;
            }
            return false;
        }

        @Override
        public boolean isFullInside(Content content) {
            for (int k = 0; k < content.getCornerCount(); k++) {
                vec3 act = content.getCorner(k);
                if (!this.isInside(act))
                    return false;
            }
            return true;
        }

        @Override
        public boolean isInside(vec3 vec) {
            if (!(vec.getYD() <= this.getCorner(3).getYD() && vec.getYD() >= this.getCorner(0).getYD() && vec.getXD() <= this.getCorner(4).getXD() && vec.getXD() >= this.getCorner(0).getXD() && vec.getZD() <= this.getCorner(1).getZD() && vec.getZD() >= this.getCorner(0).getZD()))
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
