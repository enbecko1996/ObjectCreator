package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.minecraft.Main_BlockHeroes;
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
    ContentHolder parent;

    public HigherOrderHolder(Bone parentBone, vec3.IntVec positionInBoneCoords, byte order, boolean isMaxOrder) {
        super(parentBone, positionInBoneCoords, order, isMaxOrder);
    }

    @Override
    public int getContentCount() {
        int out = 0;
        for (int k = 0; k < Main_BlockHeroes.contentCubesPerCube; k++) {
            for (int l = 0; l < Main_BlockHeroes.contentCubesPerCube; l++) {
                for (int m = 0; m < Main_BlockHeroes.contentCubesPerCube; m++) {
                    if (this.content[k][l][m] != null)
                        out++;
                }
            }
        }
        return out;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CubicContentHolderGeometry> getContent() {
        List out = new ArrayList<CubicContentHolderGeometry>();
        for (int k = 0; k < Main_BlockHeroes.contentCubesPerCube; k++) {
            for (int l = 0; l < Main_BlockHeroes.contentCubesPerCube; l++) {
                out.addAll(Arrays.asList(this.content[k][l]).subList(0, Main_BlockHeroes.contentCubesPerCube));
            } 
        }
        return out;
    }

    @Override
    public boolean addContent(@Nonnull vec3 decisiveVec, @Nonnull Content toAdd) {
        return false;
    }

    @Override
    public boolean addNewChild(@Nonnull CubicContentHolderGeometry content) {
        vec3.IntVec pos = (vec3.IntVec) new vec3.IntVec(content.getPositionInBoneCoords()).subFromThis(this.positionInBoneCoords);
        int k = pos.getX() / this.getSize(), l = pos.getY() / this.getSize(), m = pos.getZ() / this.getSize();
        if (k < 0 || k >= Main_BlockHeroes.contentCubesPerCube || l < 0 || l >= Main_BlockHeroes.contentCubesPerCube || m < 0 || m >= Main_BlockHeroes.contentCubesPerCube)
            throw new RuntimeException("This: " + content + " doesn't belong here: " + this);
        if (this.content[k][l][m] == null) {
            this.content[k][l][m] = content;
            return true;
        } else {
            throw new RuntimeException("Can't put a CubicContentHolder in HigherOrderHolder where there already is one " + this +", k = " + k + ", l = " + l + ", m = " + m);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean removeChild(@Nonnull CubicContentHolderGeometry content) {
        vec3.IntVec pos = (vec3.IntVec) new vec3.IntVec(content.getPositionInBoneCoords()).subFromThis(this.positionInBoneCoords);
        int k = pos.getX() / this.getSize(), l = pos.getY() / this.getSize(), m = pos.getZ() / this.getSize();
        if (k < 0 || k >= Main_BlockHeroes.contentCubesPerCube || l < 0 || l >= Main_BlockHeroes.contentCubesPerCube || m < 0 || m >= Main_BlockHeroes.contentCubesPerCube)
            throw new RuntimeException("This: " + content + " doesn't belong here: " + this);
        if (this.content[k][l][m] == content) {
            this.content[k][l][m] = null;
            List<CubicContentHolderGeometry> tmp = this.getContent();
            if (tmp.size() == 1)
                this.getParent().askForOrderDegrade(this, tmp.get(0));
            return true;
        } else {
            throw new RuntimeException("Can't delete a CubicContentHolder in HigherOrderHolder where there is none " + this +", k = " + k + ", l = " + l + ", m = " + m);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void askForOrderDegrade(@Nonnull CubicContentHolderGeometry asker, CubicContentHolderGeometry degradeTo) {
        List<CubicContentHolderGeometry> tmp = this.getContent();
        if (tmp.contains(asker)) {
            if (tmp.size() == 1) {
                this.getParent().askForOrderDegrade(this, degradeTo);
            }
        } else {
            throw new RuntimeException("Someone asking for Degrade which is none of my childs. " + this +", " + asker);
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

    public ContentHolder getParent() {
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


