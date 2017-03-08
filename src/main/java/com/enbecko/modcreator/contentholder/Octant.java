package com.enbecko.modcreator.contentholder;

import com.enbecko.modcreator.GlobalRenderSetting;
import com.enbecko.modcreator.LocalRenderSetting;
import com.enbecko.modcreator.OpenGLHelperEnbecko;
import com.enbecko.modcreator.events.ManipulatingEvent;
import com.enbecko.modcreator.linalg.Line3D;
import com.enbecko.modcreator.linalg.RayTrace3D;
import com.enbecko.modcreator.linalg.vec_n;
import com.enbecko.modcreator.minecraft.Main_BlockHeroes;
import com.enbecko.modcreator.linalg.vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by enbec on 01.03.2017.
 */
public class Octant extends Content.CuboidContent implements ContentHolder<CubicContentHolderGeometry> {
    private final OCTANTS type;
    private final List<CubicContentHolderGeometry> content = new ArrayList<CubicContentHolderGeometry>();
    private boolean isActive = false;
    private byte highestOrder = 1;
    protected final List<CubicContentHolderGeometry> rayTraceResult = new ArrayList<CubicContentHolderGeometry>();
    protected double[] distance = new double[Main_BlockHeroes.contentCubesPerCube];

    public Octant(Bone parentBone, vec3 positonInBoneCoords, double xSize, double ySize, double zSize, OCTANTS type) {
        super(parentBone, positonInBoneCoords, xSize, ySize, zSize, vec_n.vecPrec.INT);
        this.type = type;
        this.setActive(false);
        this.createBoundingGeometry();
    }

    @Override
    public Octant createBoundingGeometry() {
        this.makeHexahedralEdgesAndFacesAutoUpdate();
        return this;
    }

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
    public List<CubicContentHolderGeometry> getContent() {
        return this.content;
    }

    @Override
    public int getContentCount() {
        return this.content.size();
    }

    public OCTANTS getType() {
        return type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void askForOrderDegrade(@Nonnull CubicContentHolderGeometry asker, CubicContentHolderGeometry degradeTo) {
        List<CubicContentHolderGeometry> tmp = this.getContent();
        if (tmp.contains(asker)) {
            if (tmp.size() == 1) {
                tmp.remove(asker);
                tmp.add(degradeTo);
                degradeTo.addParent(this);
                int size = degradeTo.getSize();
                this.canCreateOctantOrThrowRuntimeExc(degradeTo.getPositionInBoneCoords(), size, size, size);
                this.update(degradeTo.getPositionInBoneCoords(), size, size, size);
                this.highestOrder = degradeTo.getOrder();
            } else {
                List<CubicContentHolderGeometry> tmpContent = new ArrayList<CubicContentHolderGeometry>();
                boolean degrade = false;
                List<CubicContentHolderGeometry> tmpContent2 = new ArrayList<CubicContentHolderGeometry>();
                maxDegrade:
                while (this.highestOrder > degradeTo.getOrder()) {
                    for (CubicContentHolderGeometry holder : tmp) {
                        if (holder instanceof HigherOrderHolder) {
                            List<CubicContentHolderGeometry> childs = ((HigherOrderHolder) holder).getContent();
                            if (childs.size() == 1) {
                                tmpContent2.add(childs.get(0));
                            } else {
                                break maxDegrade;
                            }
                        }
                    }
                    degrade = true;
                    tmpContent.addAll(tmpContent2);
                    tmpContent2.clear();
                    tmp = tmpContent;
                    this.highestOrder--;
                }
                if (degrade) {
                    synchronized (this.content) {
                        this.content.clear();
                        this.content.addAll(tmpContent);
                        this.makeSizeFromNewContentList();
                        for (CubicContentHolderGeometry holder : this.content)
                            holder.setMaxOrder(true).addParent(this);
                        tmpContent.clear();
                        tmpContent2.clear();
                    }
                }
            }
        } else {
            throw new RuntimeException("Someone asking for Degrade which is none of my childs. " + this + ", " + asker);
        }
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean active) {
        if (!this.isActive && active) {
            this.canCreateOctantOrThrowRuntimeExc(this.positionInBoneCoords, this.xSize, this.ySize, this.zSize);
            this.isActive = true;
        } else {
            this.isActive = active;
        }
    }

    @Override
    public boolean addContent(@Nonnull vec3 decisiveVec, @Nonnull Content toAdd) {
        if (!toAdd.isInside(decisiveVec))
            throw new RuntimeException("The decisiveVec " +decisiveVec+ " with which you want to add " + toAdd + " is not even in the content");
        if (this.isVecInHere(decisiveVec)) {
            if (toAdd instanceof CubicContentHolderGeometry && ((CubicContentHolderGeometry)toAdd).getOrder() >= this.highestOrder)
                throw new RuntimeException("Can't add CubicContentHolder with higher order than this's highest order");
            for (CubicContentHolderGeometry aContent : this.content) {
                if (aContent.isInside(decisiveVec)) {
                    if (aContent instanceof ContentHolder) {
                        return ((ContentHolder) aContent).addContent(decisiveVec, toAdd);
                    }
                }
            }
            int size = (int) Math.pow(Main_BlockHeroes.contentCubesPerCube, this.highestOrder);
            vec3 pos = (vec3) new vec3.DoubleVec(decisiveVec).divToThis(size);
            vec3.IntVec pos1 = (vec3.IntVec) new vec3.IntVec(pos, true).mulToThis(size);
            switch (this.highestOrder) {
                case 1:
                    FirstOrderHolder firstOrderHolder = (FirstOrderHolder) new FirstOrderHolder(this.getParentBone(), pos1, true).createBoundingGeometry();
                    firstOrderHolder.addContent(decisiveVec, toAdd);
                    firstOrderHolder.addParent(this);
                    this.addNewChild(firstOrderHolder);
                    break;
                default:
                    HigherOrderHolder higherOrderHolder = (HigherOrderHolder) new HigherOrderHolder(this.getParentBone(), pos1, this.highestOrder, true).createBoundingGeometry();
                    higherOrderHolder.addContent(decisiveVec, toAdd);
                    higherOrderHolder.addParent(this);
                    this.addNewChild(higherOrderHolder);
                    break;
            }
        } else
            throw new RuntimeException("Vec is not in this Octant " + this.type + ", vec = " + decisiveVec);
        return false;
    }

    @Override
    public boolean addNewChild(@Nonnull CubicContentHolderGeometry content) {
        if (!this.isActive())
            throw new RuntimeException("Can't make changes to inactive Octant " + this);
        if (content.getOrder() != this.highestOrder)
            throw new RuntimeException("Can't add content to Octant with wrong order " + this.highestOrder + ", " + content.getOrder());
        if (!this.content.contains(content)) {
            System.out.println("add new Child " + content);
            boolean increaseOrder = false;
            if (this.content.size() > 0) {
                int size = (int) Math.pow(Main_BlockHeroes.contentCubesPerCube, this.highestOrder + 1);
                vec3 pos = (vec3) new vec3.DoubleVec(content.getPositionInBoneCoords()).divToThis(size);
                vec3.IntVec pos1 = (vec3.IntVec) new vec3.IntVec(pos, true).mulToThis(size);
                HigherOrderHolder test = (HigherOrderHolder) new HigherOrderHolder(this.getParentBone(), pos1, (byte) (this.highestOrder + 1), true).createBoundingGeometry();
                List<HigherOrderHolder> tmpContent = new ArrayList<HigherOrderHolder>();
                boolean oneOutside = false, oneInside = false;
                for (CubicContentHolderGeometry tmp : this.content) {
                    if (test.isInside(tmp.getPositionInBoneCoords())) {
                        oneInside = true;
                    } else {
                        oneOutside = true;
                    }
                    if (oneInside && oneOutside) {
                        test.addNewChild(content);
                        test.addParent(this);
                        content.addParent(test);
                        content.setMaxOrder(false);
                        tmpContent.add(test);
                        increaseOrder = true;
                        break;
                    }
                }
                if (increaseOrder) {
                    System.out.println("increase order " + Arrays.toString(this.content.toArray()));
                    this.highestOrder++;
                    double xMin = test.getMinX(), yMin = test.getMinY(), zMin = test.getMinZ(),
                            xMax = test.getMaxX(), yMax = test.getMaxY(), zMax = test.getMaxZ();
                    for (CubicContentHolderGeometry tmp : this.content) {
                        boolean hasParent = false;
                        for (HigherOrderHolder newCont : tmpContent) {
                            if (newCont.isInside(tmp.getPositionInBoneCoords())) {
                                System.out.println((newCont == test) +"  "+ tmp +", "+ content+" "+ Arrays.toString(newCont.getContent().toArray()));
                                newCont.addNewChild(tmp);
                                tmp.addParent(newCont);
                                hasParent = true;
                                break;
                            }
                        }
                        if (!hasParent) {
                            pos.update(tmp.getPositionInBoneCoords()).divToThis(size);
                            pos1.update(pos, true).mulToThis(size);
                            HigherOrderHolder newHolder = (HigherOrderHolder) new HigherOrderHolder(this.getParentBone(), pos1, this.highestOrder, true).createBoundingGeometry();
                            tmpContent.add(newHolder);
                            newHolder.addNewChild(tmp);
                            newHolder.addParent(this);
                            tmp.addParent(newHolder);
                        }
                    }
                    synchronized (this.content) {
                        this.content.clear();
                        this.content.addAll(tmpContent);
                        this.makeSizeFromNewContentList();
                        tmpContent.clear();
                    }
                } else {
                    this.content.add(content);
                    this.updateSizeForNewChild(content);
                    return true;
                }
            } else {
                this.content.add(content);
                this.updateSizeForNewChild(content);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeChild(@Nonnull CubicContentHolderGeometry content) {
        if (!this.isActive())
            throw new RuntimeException("Can't make changes to inactive Octant " + this);
        if (content.getOrder() != this.highestOrder)
            throw new RuntimeException("Can't remove content from Octant with wrong order " + this.highestOrder + ", " + content.getOrder());
        if (this.content.contains(content)) {
            this.content.remove(content);
            if (this.content.size() <= 0) {
                this.getParentBone().octantEmpty(this);
                return true;
            } else if (this.content.size() == 1) {
                if (this.content.get(0) instanceof HigherOrderHolder) {
                    HigherOrderHolder higherOrderHolder = (HigherOrderHolder) this.content.get(0);
                    synchronized (this.content) {
                        this.content.clear();
                        this.content.addAll(higherOrderHolder.getContent());
                        for (CubicContentHolderGeometry holder : this.content)
                            holder.setMaxOrder(true).addParent(this);
                        this.makeSizeFromNewContentList();
                    }

                }
            } else if (content.getMinX() == this.getMinX() || content.getMinY() == this.getMinY() || content.getMinZ() == this.getMinZ() ||
                    content.getMaxX() == this.getMaxX() || content.getMaxY() == this.getMaxY() || content.getMaxZ() == this.getMaxZ()) {
                double xMin = Double.POSITIVE_INFINITY, yMin = Double.POSITIVE_INFINITY, zMin = Double.POSITIVE_INFINITY,
                        xMax = Double.NEGATIVE_INFINITY, yMax = Double.NEGATIVE_INFINITY, zMax = Double.NEGATIVE_INFINITY;
                for (CubicContentHolderGeometry cur : this.content) {
                    if (cur.getMaxX() > xMax)
                        xMax = cur.getMaxX();
                    if (cur.getMaxY() > yMax)
                        yMax = cur.getMaxY();
                    if (cur.getMaxZ() > zMax)
                        zMax = cur.getMaxZ();
                    if (cur.getMinX() < xMin)
                        xMin = cur.getMinX();
                    if (cur.getMinY() < yMin)
                        yMin = cur.getMinY();
                    if (cur.getMinZ() < zMin)
                        zMin = cur.getMinZ();
                }
                this.canCreateOctantOrThrowRuntimeExc(xMin, yMin, zMin, xMax, yMax, zMax);
                this.update(xMin, yMin, zMin, xMax, yMax, zMax);
            }
            return true;
        }
        return false;
    }

    public void updateSizeForNewChild(CubicContentHolderGeometry child) {
        double xMin = this.getMinX(), yMin = this.getMinY(), zMin = this.getMinZ(), xMax = this.getMaxX(), yMax = this.getMaxY(), zMax = this.getMaxZ();
        if (this.content.size() == 1) {
            xMin = child.getMinX();
            yMin = child.getMinY();
            zMin = child.getMinZ();
            xMax = child.getMaxX();
            yMax = child.getMaxY();
            zMax = child.getMaxZ();
        } else {
            if (child.getMaxX() > xMax)
                xMax = child.getMaxX();
            if (child.getMaxY() > yMax)
                yMax = child.getMaxY();
            if (child.getMaxZ() > zMax)
                zMax = child.getMaxZ();
            if (child.getMinX() < xMin)
                xMin = child.getMinX();
            if (child.getMinY() < yMin)
                yMin = child.getMinY();
            if (child.getMinZ() < zMin)
                zMin = child.getMinZ();
        }
        this.canCreateOctantOrThrowRuntimeExc(xMin, yMin, zMin, xMax, yMax, zMax);
        this.update(xMin, yMin, zMin, xMax, yMax, zMax);
    }

    public void makeSizeFromNewContentList() {
        double xMin = this.getMinX(), yMin = this.getMinY(), zMin = this.getMinZ(), xMax = this.getMaxX(), yMax = this.getMaxY(), zMax = this.getMaxZ();
        if (this.content.size() == 1) {
            CubicContentHolderGeometry child = this.content.get(0);
            xMin = child.getMinX();
            yMin = child.getMinY();
            zMin = child.getMinZ();
            xMax = child.getMaxX();
            yMax = child.getMaxY();
            zMax = child.getMaxZ();
        } else {
            xMin = Double.POSITIVE_INFINITY;
            yMin = Double.POSITIVE_INFINITY;
            zMin = Double.POSITIVE_INFINITY;
            xMax = Double.NEGATIVE_INFINITY;
            yMax = Double.NEGATIVE_INFINITY;
            zMax = Double.NEGATIVE_INFINITY;
            for (CubicContentHolderGeometry tmpChild : this.content) {
                if (tmpChild.getMaxX() > xMax)
                    xMax = tmpChild.getMaxX();
                if (tmpChild.getMaxY() > yMax)
                    yMax = tmpChild.getMaxY();
                if (tmpChild.getMaxZ() > zMax)
                    zMax = tmpChild.getMaxZ();
                if (tmpChild.getMinX() < xMin)
                    xMin = tmpChild.getMinX();
                if (tmpChild.getMinY() < yMin)
                    yMin = tmpChild.getMinY();
                if (tmpChild.getMinZ() < zMin)
                    zMin = tmpChild.getMinZ();
            }
        }
        this.canCreateOctantOrThrowRuntimeExc(xMin, yMin, zMin, xMax, yMax, zMax);
        this.update(xMin, yMin, zMin, xMax, yMax, zMax);
    }

    private void canCreateOctantOrThrowRuntimeExc(double xMin, double yMin, double zMin,
                                                  double xMax, double yMax, double zMax) throws RuntimeException {
        switch (this.type) {
            case I:
                if (xMin < 0 || xMax < 0 || yMax < 0 || yMin < 0 || zMax < 0 || zMin < 0)
                    throw new RuntimeException("Octant " + this.type + " can not be made with: xMin = " +
                            xMin + ", yMin = " + yMin + ", zMin = " + zMin + ", xMax = " + xMax + ", yMax = " + yMax + ", zMax = " + zMax);
                break;
            case II:
                if (xMin > 0 || xMax > 0 || yMax < 0 || yMin < 0 || zMax < 0 || zMin < 0)
                    throw new RuntimeException("Octant " + this.type + " can not be made with: xMin = " +
                            xMin + ", yMin = " + yMin + ", zMin = " + zMin + ", xMax = " + xMax + ", yMax = " + yMax + ", zMax = " + zMax);
                break;
            case III:
                if (xMin > 0 || xMax > 0 || yMax > 0 || yMin > 0 || zMax < 0 || zMin < 0)
                    throw new RuntimeException("Octant " + this.type + " can not be made with: xMin = " +
                            xMin + ", yMin = " + yMin + ", zMin = " + zMin + ", xMax = " + xMax + ", yMax = " + yMax + ", zMax = " + zMax);
                break;
            case IV:
                if (xMin < 0 || xMax < 0 || yMax > 0 || yMin > 0 || zMax < 0 || zMin < 0)
                    throw new RuntimeException("Octant " + this.type + " can not be made with: xMin = " +
                            xMin + ", yMin = " + yMin + ", zMin = " + zMin + ", xMax = " + xMax + ", yMax = " + yMax + ", zMax = " + zMax);
                break;
            case V:
                if (xMin < 0 || xMax < 0 || yMax < 0 || yMin < 0 || zMax > 0 || zMin > 0)
                    throw new RuntimeException("Octant " + this.type + " can not be made with: xMin = " +
                            xMin + ", yMin = " + yMin + ", zMin = " + zMin + ", xMax = " + xMax + ", yMax = " + yMax + ", zMax = " + zMax);
                break;
            case VI:
                if (xMin > 0 || xMax > 0 || yMax < 0 || yMin < 0 || zMax > 0 || zMin > 0)
                    throw new RuntimeException("Octant " + this.type + " can not be made with: xMin = " +
                            xMin + ", yMin = " + yMin + ", zMin = " + zMin + ", xMax = " + xMax + ", yMax = " + yMax + ", zMax = " + zMax);
                break;
            case VII:
                if (xMin > 0 || xMax > 0 || yMax > 0 || yMin > 0 || zMax > 0 || zMin > 0)
                    throw new RuntimeException("Octant " + this.type + " can not be made with: xMin = " +
                            xMin + ", yMin = " + yMin + ", zMin = " + zMin + ", xMax = " + xMax + ", yMax = " + yMax + ", zMax = " + zMax);
                break;
            case VIII:
                if (xMin < 0 || xMax < 0 || yMax > 0 || yMin > 0 || zMax > 0 || zMin > 0)
                    throw new RuntimeException("Octant " + this.type + " can not be made with: xMin = " +
                            xMin + ", yMin = " + yMin + ", zMin = " + zMin + ", xMax = " + xMax + ", yMax = " + yMax + ", zMax = " + zMax);
                break;
        }
    }

    private void canCreateOctantOrThrowRuntimeExc(vec3 pos,
                                                  double xSize, double ySize, double zSize) throws RuntimeException {
        double xMin = pos.getXD(), yMin = pos.getYD(), zMin = pos.getZD(),
                xMax = xMin + xSize, yMax = yMin + ySize, zMax = zMin + zSize;
        this.canCreateOctantOrThrowRuntimeExc(xMin, yMin, zMin, xMax, yMax, zMax);
    }

    public boolean isVecInHere(vec3 vec) {
        double x = vec.getXD(), y = vec.getYD(), z = vec.getZD();
        switch (this.type) {
            case I:
                if (x >= 0 && y >= 0 && z >= 0)
                    return true;
                break;
            case II:
                if (x <= 0 && y >= 0 && z >= 0)
                    return true;
                break;
            case III:
                if (x <= 0 && y <= 0 && z >= 0)
                    return true;
                break;
            case IV:
                if (x >= 0 && y <= 0 && z >= 0)
                    return true;
                break;
            case V:
                if (x >= 0 && y >= 0 && z <= 0)
                    return true;
                break;
            case VI:
                if (x <= 0 && y >= 0 && z <= 0)
                    return true;
                break;
            case VII:
                if (x <= 0 && y <= 0 && z <= 0)
                    return true;
                break;
            case VIII:
                if (x >= 0 && y <= 0 && z <= 0)
                    return true;
                break;
        }
        return false;
    }

    public String toString() {
        return "Octant " + this.type + ": " + this.getGeometryInfo() + "\nContent: " + Arrays.toString(this.content.toArray());
    }

    @Override
    public void manipulateMe(ManipulatingEvent event, RayTrace3D rayTrace3D) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(LocalRenderSetting... localRenderSettings) {
        if (GlobalRenderSetting.getRenderMode() == GlobalRenderSetting.RenderMode.DEBUG) {
            for (Line3D line : this.boundingEdgesInBoneCoords)
                OpenGLHelperEnbecko.drawLine(line, this.isActive() ? OpenGLHelperEnbecko.GREEN : OpenGLHelperEnbecko.RED, 4);
        }
        for (CubicContentHolderGeometry child : this.content)
            child.render();
    }

    public enum OCTANTS {
        I, II, III, IV, V, VI, VII, VIII;
    }
}
