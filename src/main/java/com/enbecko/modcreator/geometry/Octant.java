package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.linalg.vec3;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by enbec on 01.03.2017.
 */
public class Octant extends Content.CuboidContent implements ContentHolder <CubicContentHolder>{
    private List<CubicContentHolder> content;
    private final OCTANTS type;

    protected Octant(Bone parentBone, vec3.IntVec positonInBoneCoords, double xSize, double ySize, double zSize, OCTANTS type) {
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
    public List<CubicContentHolder> getContent() {
        return this.content;
    }

    @Override
    public boolean addContent(@Nonnull CubicContentHolder content) {
        if (!this.content.contains(content)) {
            this.content.add(content);
            double xMin = this.getMinX(), yMin = this.getMinY(), zMin = this.getMinZ(), xMax = this.getMaxX(), yMax = this.getMaxY(), zMax = this.getMaxZ();
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
            switch (this.type) {
                case I:
                    if (xMin < 0 || xMax < 0 || yMax < 0 || yMin < 0 || zMax < 0 || zMin < 0)
                        throw new RuntimeException("Octant " + this.type + " can not add " + content);
                case II:
                    if (xMin > 0 || xMax > 0 || yMax < 0 || yMin < 0 || zMax < 0 || zMin < 0)
                        throw new RuntimeException("Octant " + this.type + " can not add " + content);
                case III:
                    if (xMin > 0 || xMax > 0 || yMax > 0 || yMin > 0 || zMax < 0 || zMin < 0)
                        throw new RuntimeException("Octant " + this.type + " can not add " + content);
                case IV:
                    if (xMin < 0 || xMax < 0 || yMax > 0 || yMin > 0 || zMax < 0 || zMin < 0)
                        throw new RuntimeException("Octant " + this.type + " can not add " + content);
                case V:
                    if (xMin < 0 || xMax < 0 || yMax < 0 || yMin < 0 || zMax > 0 || zMin > 0)
                        throw new RuntimeException("Octant " + this.type + " can not add " + content);
                case VI:
                    if (xMin > 0 || xMax > 0 || yMax < 0 || yMin < 0 || zMax > 0 || zMin > 0)
                        throw new RuntimeException("Octant " + this.type + " can not add " + content);
                case VII:
                    if (xMin > 0 || xMax > 0 || yMax > 0 || yMin > 0 || zMax > 0 || zMin > 0)
                        throw new RuntimeException("Octant " + this.type + " can not add " + content);
                case VIII:
                    if (xMin < 0 || xMax < 0 || yMax > 0 || yMin > 0 || zMax > 0 || zMin > 0)
                        throw new RuntimeException("Octant " + this.type + " can not add " + content);
            }
            this.update(xMin, yMin, zMin, xMax, yMax, zMax);
            return true;
        }
        return false;
    }

    public enum OCTANTS {
        I, II, III, IV, V, VI, VII, VIII;
    }
}
