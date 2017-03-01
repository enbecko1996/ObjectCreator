package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.linalg.vec3;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbec on 21.02.2017.
 */
public class FirstOrderHolder extends CubicContentHolder implements ContentHolder <Content> {
    private List<Content> content = new ArrayList<Content>();
    HigherOrderHolder parent;

    public FirstOrderHolder(Bone parentBone, vec3.ByteVec positionInParentInOrdersOfEdgeLength, vec3.IntVec positionInBoneCoords, boolean isMaxOrder) {
        super(parentBone, positionInParentInOrdersOfEdgeLength, positionInBoneCoords, (byte) 1, isMaxOrder);
    }

    @Override
    public List<Content> getContent() {
        return this.content;
    }

    @Override
    public boolean addContent(@Nonnull Content content) {
        return false;
    }

    @Override
    public int getParentCount() {
        return 1;
    }

    @Override
    public HigherOrderHolder getParent(int pos) {
        return this.parent;
    }

    public CubicContentHolder getParent() {
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
