package com.enbecko.modcreator.contentholder;

import com.enbecko.modcreator.GlobalRenderSetting;
import com.enbecko.modcreator.LocalRenderSetting;
import com.enbecko.modcreator.Log;
import com.enbecko.modcreator.Log.LogEnums;
import com.enbecko.modcreator.linalg.RayTrace3D;
import com.enbecko.modcreator.linalg.vec_n;
import com.enbecko.modcreator.minecraft.Main_BlockHeroes;
import com.enbecko.modcreator.linalg.vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbec on 21.02.2017.
 */
public class HigherOrderHolder extends CubicContentHolderGeometry implements ContentHolder <CubicContentHolderGeometry>{
    private CubicContentHolderGeometry[][][] content = new CubicContentHolderGeometry[Main_BlockHeroes.contentCubesPerCube][Main_BlockHeroes.contentCubesPerCube][Main_BlockHeroes.contentCubesPerCube];
    ContentHolder parent;
    protected final List<CubicContentHolderGeometry> rayTraceResult = new ArrayList<CubicContentHolderGeometry>();
    protected double[] distance = new double[Main_BlockHeroes.contentCubesPerCube];

    public HigherOrderHolder(Bone parentBone, vec3.IntVec positionInBoneCoords, byte order, boolean isMaxOrder) {
        super(parentBone, positionInBoneCoords, order, isMaxOrder);
    }

    @Override
    public Content getRayTraceResult(RayTrace3D rayTrace3D) {
        List<CubicContentHolderGeometry> holders = this.getContent();
        this.rayTraceResult.clear();
        for (int l = 0; l < distance.length; l++) {
            if (distance[l] != 0)
                distance[l] = 0;
            else
                break;
        }
        vec3 pos;
        for (CubicContentHolderGeometry holder : holders) {
            if (holder.isInside(rayTrace3D.getOnPoint())) {
                this.rayTraceResult.add(0, holder);
                double tmp = distance[0];
                for (int l = 1; l < distance.length; l++) {
                    if (l > 0 && distance[l - 1] != 0) {
                        double tt = distance[l];
                        distance[l] = tmp;
                        tmp = tt;
                    } else
                        break;
                }
            }
            if ((pos = holder.checkIfCrosses(rayTrace3D)) != null) {
                double d = pos.subFromThis(rayTrace3D.getOnPoint()).length();
                int k = 0;
                for (; k < distance.length; k++) {
                    if (distance[k] == 0 || distance[k] > d) {
                        if (distance[k] != 0) {
                            double tmp = distance[k];
                            for (int l = k + 1; l < distance.length; l++) {
                                if (l > 0 && distance[l - 1] != 0) {
                                    double tt = distance[l];
                                    distance[l] = tmp;
                                    tmp = tt;
                                } else
                                    break;
                            }
                        }
                        distance[k] = d;
                        break;
                    }
                }
                this.rayTraceResult.add(k, holder);
            }
        }
        for (CubicContentHolderGeometry holder : this.rayTraceResult) {
            Content result;
            if ((result = holder.getRayTraceResult(rayTrace3D)) != null)
                return result;
        }
        return null;
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
                for (int m = 0; m < Main_BlockHeroes.contentCubesPerCube; m++) {
                    if (this.content[k][l][m] != null)
                        out.add(this.content[k][l][m]);
                }
            } 
        }
        return out;
    }

    @Override
    public boolean addContent(@Nonnull vec3 decisiveVec, @Nonnull Content toAdd) {
        if (this.isInside(decisiveVec)) {
            for (int k = 0; k < Main_BlockHeroes.contentCubesPerCube; k++) {
                for (int l = 0; l < Main_BlockHeroes.contentCubesPerCube; l++) {
                    for (CubicContentHolderGeometry aContent : this.content[k][l]) {
                        if (aContent != null) {
                            if (aContent.isInside(decisiveVec)) {
                                if (aContent instanceof ContentHolder) {
                                    Log.d(LogEnums.CONTENTHOLDER, this + " \nadd in already existing child: " + aContent + " content = " + toAdd);
                                    return ((ContentHolder) aContent).addContent(decisiveVec, toAdd);
                                }
                            }
                        }
                    }
                }
            }
            vec3.IntVec pos = (vec3.IntVec) new vec3.IntVec(decisiveVec, true).subFromThis(this.positionInBoneCoords);
            int size = (this.getSize() / Main_BlockHeroes.contentCubesPerCube);
            int k = pos.getX() / size, l = pos.getY() / size, m = pos.getZ() / size;
            vec3.IntVec pos1 = (vec3.IntVec) this.positionInBoneCoords.addAndMakeNew(vec_n.vecPrec.INT, false, k * size, l * size, m * size);
            if (k < 0 || k >= Main_BlockHeroes.contentCubesPerCube || l < 0 || l >= Main_BlockHeroes.contentCubesPerCube || m < 0 || m >= Main_BlockHeroes.contentCubesPerCube)
                throw new RuntimeException("This: " + toAdd + " doesn't belong here: " + this + ", k = " + k + ", l = " + l + ", m = " + m + ", " + pos);
            switch (this.getOrder()) {
                case 2:
                    FirstOrderHolder firstOrderHolder = (FirstOrderHolder) new FirstOrderHolder(this.getParentBone(), pos1, true).createBoundingGeometry();
                    firstOrderHolder.addParent(this);
                    this.addNewChild(firstOrderHolder);
                    return firstOrderHolder.addContent(decisiveVec, toAdd);
                default:
                    HigherOrderHolder higherOrderHolder = (HigherOrderHolder) new HigherOrderHolder(this.getParentBone(), pos1, (byte) (this.getOrder() - 1), false).createBoundingGeometry();
                    higherOrderHolder.addParent(this);
                    this.addNewChild(higherOrderHolder);
                    return higherOrderHolder.addContent(decisiveVec, toAdd);
            }
        } else
            throw new RuntimeException(toAdd + " is not in here: " + this);
    }

    @Override
    public boolean addNewChild(@Nonnull CubicContentHolderGeometry content) {
        if (content.getOrder() != this.getOrder() - 1)
            throw new RuntimeException("The ContentHolder you want to add must have a order which is one lower than this's " + this + ", " + content);
        vec3.IntVec pos = (vec3.IntVec) new vec3.IntVec(content.getPositionInBoneCoords()).subFromThis(this.positionInBoneCoords);
        int k = pos.getX() / content.getSize(), l = pos.getY() / content.getSize(), m = pos.getZ() / content.getSize();
        if (k < 0 || k >= Main_BlockHeroes.contentCubesPerCube || l < 0 || l >= Main_BlockHeroes.contentCubesPerCube || m < 0 || m >= Main_BlockHeroes.contentCubesPerCube)
            throw new RuntimeException("This: " + content + " doesn't belong here: " + this);
        if (this.content[k][l][m] == null) {
            this.content[k][l][m] = content;
            return true;
        } else {
            throw new RuntimeException("Can't put a CubicContentHolder in HigherOrderHolder where there already is one " + this +", " + this.content[k][l][m] + ", " + content +  ", k = " + k + ", l = " + l + ", m = " + m);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean removeChild(@Nonnull CubicContentHolderGeometry content) {
        vec3.IntVec pos = (vec3.IntVec) new vec3.IntVec(content.getPositionInBoneCoords()).subFromThis(this.positionInBoneCoords);
        int k = pos.getX() / content.getSize(), l = pos.getY() / content.getSize(), m = pos.getZ() / content.getSize();
        if (k < 0 || k >= Main_BlockHeroes.contentCubesPerCube || l < 0 || l >= Main_BlockHeroes.contentCubesPerCube || m < 0 || m >= Main_BlockHeroes.contentCubesPerCube)
            throw new RuntimeException("This: " + content + " doesn't belong here: " + this);
        if (this.content[k][l][m] == content) {
            this.content[k][l][m] = null;
            List<CubicContentHolderGeometry> tmp = this.getContent();
            if (tmp.size() == 1) {
                this.getParent().askForOrderDegrade(this, tmp.get(0));
            }
            else if (tmp.size() == 0) {
                this.getParent().removeChild(this);
            }
            return true;
        } else {
            throw new RuntimeException("Can't delete a CubicContentHolder in HigherOrderHolder where it isn't " + this + ", " + content + ", " + this.content[k][l][m] + ", k = " + k + ", l = " + l + ", m = " + m);
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
        for (int k = 0; k < Main_BlockHeroes.contentCubesPerCube; k++) {
            for (int l = 0; l < Main_BlockHeroes.contentCubesPerCube; l++) {
                for (int m = 0; m < Main_BlockHeroes.contentCubesPerCube; m++) {
                    if (this.content[k][l][m] != null)
                        this.content[k][l][m].render();
                }
            }
        }
    }
}


