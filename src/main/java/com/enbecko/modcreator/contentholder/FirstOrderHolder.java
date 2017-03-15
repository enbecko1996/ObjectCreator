package com.enbecko.modcreator.contentholder;

import com.enbecko.modcreator.GlobalRenderSetting;
import com.enbecko.modcreator.LocalRenderSetting;
import com.enbecko.modcreator.Log;
import com.enbecko.modcreator.linalg.RayTrace3D;
import com.enbecko.modcreator.linalg.vec3;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbec on 21.02.2017.
 */
public class FirstOrderHolder extends CubicContentHolderGeometry implements ContentHolder<Content> {
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
            toAdd.addParent(this);
            return this.addNewChild(toAdd);
        } else
            throw new RuntimeException(toAdd + " is not in here: " + this);
    }

    @Override
    public int getContentCount() {
        return this.content.size();
    }

    @Override
    public boolean addNewChild(@Nonnull Content content) {
        if (!this.content.contains(content)) {
            for (Content other : this.content) {
                if (other.isColliding(content)) {
                    Log.d(Log.LogEnums.CONTENTHOLDER, "The Content you want to add: " + content +" is colliding with " + other);
                    return false;
                }
            }
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
    public void render(VertexBuffer buffer, LocalRenderSetting... localRenderSettings) {
        if (GlobalRenderSetting.getRenderMode() == GlobalRenderSetting.RenderMode.DEBUG)
            super.render(buffer);
        for (Content child : this.content) {
            child.render(buffer, localRenderSettings);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderContentWithExceptions(VertexBuffer buffer, @Nullable List<Content> exceptions, LocalRenderSetting... localRenderSettings) {
        if (GlobalRenderSetting.getRenderMode() == GlobalRenderSetting.RenderMode.DEBUG)
            super.render(buffer);
        for (Content child : this.content) {
            if(exceptions == null || !exceptions.contains(child))
                child.render(buffer);
        }
    }

    @Override
    public Content getRayTraceResult(RayTrace3D rayTrace3D) {
        double smallestDist = Double.POSITIVE_INFINITY;
        Content tmpResult = null;
        for (Content content : this.content) {
            vec3 pos;
            if ((pos = content.checkIfCrosses(rayTrace3D)) != null) {
                double d = pos.subFromThis(rayTrace3D.getOnPoint()).length();
                if (d < smallestDist) {
                    smallestDist = d;
                    tmpResult = content;
                }
            }
        }
        return tmpResult;
    }

    public String toString() {
        return "FirstOrderHolder " + this.getGeometryInfo();
    }
}
