package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.linalg.vec3;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by enbec on 21.02.2017.
 */
public class HigherOrderHolder extends CubicContentHolderGeometry implements ContentHolder <CubicContentHolderGeometry>{
    private List<CubicContentHolderGeometry> content;
    HigherOrderHolder parent;

    public HigherOrderHolder(Bone parentBone, vec3.ByteVec positionInParentInOrdersOfEdgeLength, vec3.IntVec positionInBoneCoords, byte order, boolean isMaxOrder) {
        super(parentBone, positionInParentInOrdersOfEdgeLength, positionInBoneCoords, order, isMaxOrder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CubicContentHolderGeometry> getContent() {
        return this.content;
    }

    @Override
    public boolean addContent(@Nonnull CubicContentHolderGeometry content) {
        if (!this.content.contains(content)) {
            this.content.add(content);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeContent(@Nonnull CubicContentHolderGeometry content) {
        if (this.content.contains(content)) {
            this.content.remove(content);
            if (this.content.size() <= 0)
                this.getParent().removeContent(this);
            return true;
        }
        return false;
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


