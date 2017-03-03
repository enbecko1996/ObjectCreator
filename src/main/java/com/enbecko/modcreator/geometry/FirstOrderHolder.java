package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.linalg.vec3;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbec on 21.02.2017.
 */
public class FirstOrderHolder extends CubicContentHolderGeometry implements ContentHolder <Content> {
    private List<Content> content = new ArrayList<Content>();
    HigherOrderHolder parent;

    public FirstOrderHolder(Bone parentBone, vec3.IntVec positionInBoneCoords, boolean isMaxOrder) {
        super(parentBone, positionInBoneCoords, (byte) 1, isMaxOrder);
    }

    @Override
    public List<Content> getContent() {
        return this.content;
    }

    @Override
    public boolean addContent(@Nonnull vec3 decisiveVec, @Nonnull Content toAdd) {
        return false;
    }

    @Override
    public int getContentCount() {
        return this.content.size();
    }

    @Override
    public boolean addNewChild(@Nonnull Content content) {
        if (!this.content.contains(content)) {
            this.content.add(content);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeChild(@Nonnull Content content) {
        if (this.content.contains(content)) {
            this.content.remove(content);
            if (this.content.size() <= 0)
                this.getParent().removeChild(this);
            return true;
        }
        return false;
    }

    @Override
    public void askForOrderDegrade(@Nonnull Content asker, CubicContentHolderGeometry degradeTo) {

    }

    @Override
    public int getParentCount() {
        return 1;
    }

    @Override
    public HigherOrderHolder getParent(int pos) {
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
