package com.enbecko.modcreator.contentholder;

import com.enbecko.modcreator.GlobalRenderSetting;
import com.enbecko.modcreator.LocalRenderSetting;
import com.enbecko.modcreator.Visible.IGridded;
import com.enbecko.modcreator.linalg.RayTrace3D;
import com.enbecko.modcreator.linalg.vec3;
import com.enbecko.modcreator.minecraft.Main_BlockHeroes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by enbec on 21.02.2017.
 */
public class FirstOrderHolder extends CubicContentHolderGeometry implements ContentHolder<Content> {
    private List<Content> content = new ArrayList<Content>();
    private final IGridded[][][] gridded = new IGridded[Main_BlockHeroes.contentCubesPerCube][Main_BlockHeroes.contentCubesPerCube][Main_BlockHeroes.contentCubesPerCube];
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
            if (content instanceof IGridded) {
                IGridded gridded = (IGridded)content;
                vec3.IntVec tmp = (vec3.IntVec) new vec3.IntVec(gridded.getPosition()).subFromThis(this.getPositionInBoneCoords());
                for (int x = 0; x < (gridded.getXDim() > Main_BlockHeroes.contentCubesPerCube - tmp.getX() ? Main_BlockHeroes.contentCubesPerCube - tmp.getX() : gridded.getXDim()); x++) {
                    for (int y = 0; y < (gridded.getYDim() > Main_BlockHeroes.contentCubesPerCube - tmp.getY() ? Main_BlockHeroes.contentCubesPerCube - tmp.getY() : gridded.getYDim()); y++) {
                        for (int z = 0; z < (gridded.getZDim() > Main_BlockHeroes.contentCubesPerCube - tmp.getZ() ? Main_BlockHeroes.contentCubesPerCube - tmp.getZ() : gridded.getZDim()); z++) {
                            if (this.gridded[tmp.getX() + x][tmp.getY() + y][tmp.getZ() + z] == null) {
                                this.gridded[tmp.getX() + x][tmp.getY() + y][tmp.getZ() + z] = gridded;
                            } else
                                throw new RuntimeException("There seems to be an Gridded object where you want to add one " + Arrays.deepToString(this.gridded) +" " + content + ", x = " + (tmp.getX() + x)+ ", y = " + (tmp.getY() + y)+ ", z = " + (tmp.getZ() + z));
                        }
                    }
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
            if (content instanceof IGridded) {
                IGridded gridded = (IGridded)content;
                vec3.IntVec tmp = (vec3.IntVec) new vec3.IntVec(gridded.getPosition()).subFromThis(this.getPositionInBoneCoords());
                for (int x = 0; x < (gridded.getXDim() > Main_BlockHeroes.contentCubesPerCube - tmp.getX() ? Main_BlockHeroes.contentCubesPerCube - tmp.getX() : gridded.getXDim()); x++) {
                    for (int y = 0; y < (gridded.getYDim() > Main_BlockHeroes.contentCubesPerCube - tmp.getY() ? Main_BlockHeroes.contentCubesPerCube - tmp.getY() : gridded.getYDim()); y++) {
                        for (int z = 0; z < (gridded.getZDim() > Main_BlockHeroes.contentCubesPerCube - tmp.getZ() ? Main_BlockHeroes.contentCubesPerCube - tmp.getZ() : gridded.getZDim()); z++) {
                            if (this.gridded[tmp.getX() + x][tmp.getY() + y][tmp.getZ() + z] != null) {
                                this.gridded[tmp.getX() + x][tmp.getY() + y][tmp.getZ() + z] = null;
                            } else
                                throw new RuntimeException("There seems to be nothing where you want to remove one gridded " + Arrays.deepToString(this.gridded) +" " + content + ", x = " + (tmp.getX() + x)+ ", y = " + (tmp.getY() + y)+ ", z = " + (tmp.getZ() + z));
                        }
                    }
                }
            }
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
    public void render(LocalRenderSetting... localRenderSettings) {
        if (GlobalRenderSetting.getRenderMode() == GlobalRenderSetting.RenderMode.DEBUG)
            super.render();
        for (Content child : this.content) {
            child.render();
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
                if (d < smallestDist)
                    tmpResult = content;
            }
        }
        return tmpResult;
    }
}
