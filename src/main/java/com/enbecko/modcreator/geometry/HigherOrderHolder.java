package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.Main_ModCreator;
import com.enbecko.modcreator.linalg.vec3;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by enbec on 21.02.2017.
 */
public class HigherOrderHolder extends CubicContentHolderGeometry implements ContentHolder <CubicContentHolderGeometry>{
    private CubicContentHolderGeometry[][][] content;
    HigherOrderHolder parent;

    public HigherOrderHolder(Bone parentBone, vec3.IntVec positionInBoneCoords, byte order, boolean isMaxOrder) {
        super(parentBone, positionInBoneCoords, order, isMaxOrder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CubicContentHolderGeometry> getContent() {
        List out = new ArrayList<CubicContentHolderGeometry>();
        for (int k = 0; k < Main_ModCreator.contentCubesPerCube; k++) {
            for (int l = 0; l < Main_ModCreator.contentCubesPerCube; l++) {
                out.addAll(Arrays.asList(this.content[k][l]).subList(0, Main_ModCreator.contentCubesPerCube));
            } 
        }
        return out;
    }

    @Override
    public boolean addContent(@Nonnull CubicContentHolderGeometry content) {
        vec3.IntVec pos = (vec3.IntVec) new vec3.IntVec(content.getPositionInBoneCoords()).subFromThis(this.positionInBoneCoords);
        int k = pos.getX() / this.getSize(), l = pos.getY() / this.getSize(), m = pos.getZ() / this.getSize();
        if (k < 0 || k >= Main_ModCreator.contentCubesPerCube || l < 0 || l >= Main_ModCreator.contentCubesPerCube || m < 0 || m >= Main_ModCreator.contentCubesPerCube)
            throw new RuntimeException("This: " + content + " doesn't belong here: " + this);
        if (this.content[k][l][m] == null) {
            this.content[k][l][m] = content;
            return true;
        } else {
            throw new RuntimeException("Can't put a CubicContentHolder in HigherOrderHolder where there already is one " + this +", k = " + k + ", l = " + l + ", m = " + m);
        }
    }

    @Override
    public boolean removeContent(@Nonnull CubicContentHolderGeometry content) {
        vec3.IntVec pos = (vec3.IntVec) new vec3.IntVec(content.getPositionInBoneCoords()).subFromThis(this.positionInBoneCoords);
        int k = pos.getX() / this.getSize(), l = pos.getY() / this.getSize(), m = pos.getZ() / this.getSize();
        if (k < 0 || k >= Main_ModCreator.contentCubesPerCube || l < 0 || l >= Main_ModCreator.contentCubesPerCube || m < 0 || m >= Main_ModCreator.contentCubesPerCube)
            throw new RuntimeException("This: " + content + " doesn't belong here: " + this);
        if (this.content[k][l][m] == content) {
            this.content[k][l][m] = null;
            return true;
        } else {
            throw new RuntimeException("Can't delete a CubicContentHolder in HigherOrderHolder where there is none " + this +", k = " + k + ", l = " + l + ", m = " + m);
        }
    }

    @Override
    public int getParentCount() {
        return 1;
    }

    @Override
    public ContentHolder getParent(int pos) {
        return this.parent;
    }

    public HigherOrderHolder getParent() {
        return this.parent;
    }

    @Override
    public boolean addParent(ContentHolder contentHolder) {
        return false;
    }

    public void setParent(HigherOrderHolder contentHolder) {
        this.parent = contentHolder;
    }
}


