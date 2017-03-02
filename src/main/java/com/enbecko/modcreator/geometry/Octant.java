package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.linalg.vec3;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by enbec on 01.03.2017.
 */
public class Octant extends Content.CuboidContent implements ContentHolder<CubicContentHolderGeometry> {
    private final OCTANTS type;
    private final List<CubicContentHolderGeometry> content = new ArrayList<CubicContentHolderGeometry>();
    private boolean isActive = false;

    public Octant(Bone parentBone, vec3 positonInBoneCoords, double xSize, double ySize, double zSize, OCTANTS type) {
        super(parentBone, positonInBoneCoords, xSize, ySize, zSize);
        this.type = type;
    }

    @Override
    public void createGeometry() {
        vec3 pos = this.getPositionInBoneCoords();
        this.makeCorners(true);
        this.makeCubicEdgesAndFacesAutoUpdate();
    }

    @Override
    public List<CubicContentHolderGeometry> getContent() {
        return this.content;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean active) {
        if (!this.isActive && active) {
            this.canCreateOctantOrThrowRuntimeExc(this.positionInBoneCoords, this.xSize, this.ySize, this.zSize);
            this.isActive = true;
        } else {
            this.isActive = active;
        }
    }

    @Override
    public boolean addContent(@Nonnull CubicContentHolderGeometry content) {
        if (!this.isActive())
            throw new RuntimeException("Can't make changes to inactive Octant " + this);
        if (!this.content.contains(content)) {
            this.content.add(content);
            double xMin = this.getMinX(), yMin = this.getMinY(), zMin = this.getMinZ(), xMax = this.getMaxX(), yMax = this.getMaxY(), zMax = this.getMaxZ();
            if (this.content.size() == 1) {
                xMin = content.getMinX();
                yMin = content.getMinY();
                zMin = content.getMinZ();
                xMax = content.getMaxX();
                yMax = content.getMaxY();
                zMax = content.getMaxZ();
            } else {
                if (content.getMaxX() > xMax)
                    xMax = content.getMaxX();
                if (content.getMaxY() > yMax)
                    yMax = content.getMaxY();
                if (content.getMaxZ() > zMax)
                    zMax = content.getMaxZ();
                if (content.getMinX() < xMin)
                    xMin = content.getMinX();
                if (content.getMinY() < yMin)
                    yMin = content.getMinY();
                if (content.getMinZ() < zMin)
                    zMin = content.getMinZ();
            }
            this.canCreateOctantOrThrowRuntimeExc(xMin, yMin, zMin, xMax, yMax, zMax);
            this.update(xMin, yMin, zMin, xMax, yMax, zMax);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeContent(@Nonnull CubicContentHolderGeometry content) {
        if (!this.isActive())
            throw new RuntimeException("Can't make changes to inactive Octant " + this);
        if (this.content.contains(content)) {
            this.content.remove(content);
            if (this.content.size() <= 0) {
                this.getParentBone().octantEmpty(this);
                return true;
            }
            if (content.getMinX() == this.getMinX() || content.getMinY() == this.getMinY() || content.getMinZ() == this.getMinZ() ||
                    content.getMaxX() == this.getMaxX() || content.getMaxY() == this.getMaxY() || content.getMaxZ() == this.getMaxZ()) {
                double xMin = Double.POSITIVE_INFINITY, yMin = Double.POSITIVE_INFINITY, zMin = Double.POSITIVE_INFINITY,
                        xMax = Double.NEGATIVE_INFINITY, yMax = Double.NEGATIVE_INFINITY, zMax = Double.NEGATIVE_INFINITY;
                for (CubicContentHolderGeometry cur : this.content) {
                    if (cur.getMaxX() > xMax)
                        xMax = cur.getMaxX();
                    if (cur.getMaxY() > yMax)
                        yMax = cur.getMaxY();
                    if (cur.getMaxZ() > zMax)
                        zMax = cur.getMaxZ();
                    if (cur.getMinX() < xMin)
                        xMin = cur.getMinX();
                    if (cur.getMinY() < yMin)
                        yMin = cur.getMinY();
                    if (cur.getMinZ() < zMin)
                        zMin = cur.getMinZ();
                }
                this.canCreateOctantOrThrowRuntimeExc(xMin, yMin, zMin, xMax, yMax, zMax);
                this.update(xMin, yMin, zMin, xMax, yMax, zMax);
            }
            return true;
        }
        return false;
    }

    private void canCreateOctantOrThrowRuntimeExc(double xMin, double yMin, double zMin,
                                                  double xMax, double yMax, double zMax) throws RuntimeException {
        switch (this.type) {
            case I:
                if (xMin < 0 || xMax < 0 || yMax < 0 || yMin < 0 || zMax < 0 || zMin < 0)
                    throw new RuntimeException("Octant " + this.type + " can not be made with: xMin = " +
                            xMin + ", yMin = " + yMin + ", zMin = " + zMin + ", xMax = " + xMax + ", yMax = " + yMax + ", zMax = " + zMax);
                break;
            case II:
                if (xMin > 0 || xMax > 0 || yMax < 0 || yMin < 0 || zMax < 0 || zMin < 0)
                    throw new RuntimeException("Octant " + this.type + " can not be made with: xMin = " +
                            xMin + ", yMin = " + yMin + ", zMin = " + zMin + ", xMax = " + xMax + ", yMax = " + yMax + ", zMax = " + zMax);
                break;
            case III:
                if (xMin > 0 || xMax > 0 || yMax > 0 || yMin > 0 || zMax < 0 || zMin < 0)
                    throw new RuntimeException("Octant " + this.type + " can not be made with: xMin = " +
                            xMin + ", yMin = " + yMin + ", zMin = " + zMin + ", xMax = " + xMax + ", yMax = " + yMax + ", zMax = " + zMax);
                break;
            case IV:
                if (xMin < 0 || xMax < 0 || yMax > 0 || yMin > 0 || zMax < 0 || zMin < 0)
                    throw new RuntimeException("Octant " + this.type + " can not be made with: xMin = " +
                            xMin + ", yMin = " + yMin + ", zMin = " + zMin + ", xMax = " + xMax + ", yMax = " + yMax + ", zMax = " + zMax);
                break;
            case V:
                if (xMin < 0 || xMax < 0 || yMax < 0 || yMin < 0 || zMax > 0 || zMin > 0)
                    throw new RuntimeException("Octant " + this.type + " can not be made with: xMin = " +
                            xMin + ", yMin = " + yMin + ", zMin = " + zMin + ", xMax = " + xMax + ", yMax = " + yMax + ", zMax = " + zMax);
                break;
            case VI:
                if (xMin > 0 || xMax > 0 || yMax < 0 || yMin < 0 || zMax > 0 || zMin > 0)
                    throw new RuntimeException("Octant " + this.type + " can not be made with: xMin = " +
                            xMin + ", yMin = " + yMin + ", zMin = " + zMin + ", xMax = " + xMax + ", yMax = " + yMax + ", zMax = " + zMax);
                break;
            case VII:
                if (xMin > 0 || xMax > 0 || yMax > 0 || yMin > 0 || zMax > 0 || zMin > 0)
                    throw new RuntimeException("Octant " + this.type + " can not be made with: xMin = " +
                            xMin + ", yMin = " + yMin + ", zMin = " + zMin + ", xMax = " + xMax + ", yMax = " + yMax + ", zMax = " + zMax);
                break;
            case VIII:
                if (xMin < 0 || xMax < 0 || yMax > 0 || yMin > 0 || zMax > 0 || zMin > 0)
                    throw new RuntimeException("Octant " + this.type + " can not be made with: xMin = " +
                            xMin + ", yMin = " + yMin + ", zMin = " + zMin + ", xMax = " + xMax + ", yMax = " + yMax + ", zMax = " + zMax);
                break;
        }
    }

    private void canCreateOctantOrThrowRuntimeExc(vec3 pos,
                                                  double xSize, double ySize, double zSize) throws RuntimeException {
        double xMin = pos.getXD(), yMin = pos.getYD(), zMin = pos.getZD(),
                xMax = xMin + xSize, yMax = yMin + ySize, zMax = zMin + zSize;
        this.canCreateOctantOrThrowRuntimeExc(xMin, yMin, zMin, xMax, yMax, zMax);
    }

    public enum OCTANTS {
        I, II, III, IV, V, VI, VII, VIII;
    }

    public String toString() {
        return "Octant " + this.type + ": " + this.getGeometryInfo() + "\nContent: " + Arrays.toString(this.content.toArray());
    }
}
