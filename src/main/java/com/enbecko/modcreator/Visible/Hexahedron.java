package com.enbecko.modcreator.Visible;

import com.enbecko.modcreator.contentholder.Bone;
import com.enbecko.modcreator.contentholder.Content;
import com.enbecko.modcreator.linalg.vec3;
import com.enbecko.modcreator.linalg.vec_n;

import java.util.List;

/**
 * Created by Niclas on 05.03.2017.
 */
public class Hexahedron extends Content.CuboidContent implements ITextured {
    protected Hexahedron(Bone parentBone, vec3 positionInBoneCoords, double xSize, double ySize, double zSize) {
        super(parentBone, positionInBoneCoords, xSize, ySize, zSize, vec_n.vecPrec.DOUBLE);
    }

    @Override
    public List getRenderPolygons() {
        return null;
    }

    @Override
    public Content createBoundingGeometry() {
        this.makeHexahedralEdgesAndFacesAutoUpdate();
        return this;
    }

    public static class Gridded extends Hexahedron{
        protected Gridded(Bone parentBone, vec3 positionInBoneCoords, int xSize, int ySize, int zSize) {
            super(parentBone, positionInBoneCoords, xSize, ySize, zSize);
        }

        public void updateSize() {

        }
    }
}
