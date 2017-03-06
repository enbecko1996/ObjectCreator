package com.enbecko.modcreator.contentholder;

import com.enbecko.modcreator.GlobalRenderSetting;
import com.enbecko.modcreator.LocalRenderSetting;
import com.enbecko.modcreator.linalg.vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbec on 21.02.2017.
 */
public class FirstOrderHolder extends CubicContentHolderGeometry implements ContentHolder <Content> {
    private List<Content> content = new ArrayList<Content>();
    ContentHolder parent;

    public FirstOrderHolder(Bone parentBone, vec3.IntVec positionInBoneCoords, boolean isMaxOrder) {
        super(parentBone, positionInBoneCoords, (byte) 1, isMaxOrder);
    }

    @Override
    public List<Content> getContent() {
        return this.content;
    }

    @Override
    public boolean addContent(@Nonnull vec3 decisiveVec, @Nonnull Content toAdd) {
        if (this.isInside(decisiveVec)) {
            if (!this.content.contains(toAdd)) {
                toAdd.addParent(this);
                return this.addNewChild(toAdd);
            }
        } else
            throw new RuntimeException(toAdd + " is not in here: " + this);
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
    public ContentHolder getParent(int pos) {
        return this.parent;
    }

    public ContentHolder getParent() {
        return this.parent;
    }

    @Override
    public boolean addParent(ContentHolder higherOrderHolder) {
        if (higherOrderHolder != null) {
            this.parent = higherOrderHolder;
            return true;
        } else
            throw new RuntimeException("Can't make a null parent " + this);
    }

    public void setParent(HigherOrderHolder contentHolder) {
        this.parent = contentHolder;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(GlobalRenderSetting renderPass, LocalRenderSetting... localRenderSettings) {
        if (renderPass.getRenderMode() == GlobalRenderSetting.RenderMode.DEBUG)
            super.render(renderPass);
        for (Content child : this.content) {
            child.render(renderPass);
        }
    }
}
