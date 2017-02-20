package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.linalg.vec3;
import jdk.nashorn.internal.ir.Block;
import org.w3c.dom.ranges.Range;

import javax.annotation.Nullable;
import java.util.ArrayList;

import static com.enbecko.modcreator.geometry.SearchTree.Sortmode.XYZ;

/**
 * Created by enbec on 07.01.2017.
 */
public class SearchTree {
    private static ArrayList<CoordinatedList<CoordinatedList<Block3D>>> xyzSort = new ArrayList<CoordinatedList<CoordinatedList<Block3D>>>();
    private static ArrayList<CoordinatedList<CoordinatedList<Block3D>>> yxzSort = new ArrayList<CoordinatedList<CoordinatedList<Block3D>>>();
    private static ArrayList<CoordinatedList<CoordinatedList<Block3D>>> zyxSort = new ArrayList<CoordinatedList<CoordinatedList<Block3D>>>();
    private static ArrayList<CoordinatedList<CoordinatedList<Block3D>>> xzySort = new ArrayList<CoordinatedList<CoordinatedList<Block3D>>>();
    private static ArrayList<CoordinatedList<CoordinatedList<Block3D>>> yzxSort = new ArrayList<CoordinatedList<CoordinatedList<Block3D>>>();
    private static ArrayList<CoordinatedList<CoordinatedList<Block3D>>> zxySort = new ArrayList<CoordinatedList<CoordinatedList<Block3D>>>();
    private final vec3.Int result = new vec3.Int();

    @Nullable
    public Block3D getBlockAt(final vec3.Int pos, Sortmode mode) {
        ArrayList<CoordinatedList<CoordinatedList<Block3D>>> list = mode.getMySortList();
        vec3.Int sortedVec = mode.getSortedVec(pos, result);
        if (list != null && sortedVec != null) {
            for (CoordinatedList<CoordinatedList<Block3D>> xList : list) {
                if (xList.coordinate == sortedVec.getX() || xList.coordinate + xList.getExtendsInNext() > sortedVec.getX()) {
                    for (int y = 0; y < xList.size(); y++) {
                        if (xList.get(y).coordinate == sortedVec.getY() || xList.coordinate + xList.getExtendsInNext() > sortedVec.getX()) {
                            CoordinatedList<Block3D> xyList = xList.get(y);
                            for (int z = 0; z < xyList.size(); z++) {
                                if (xyList.get(z).position.z == sortedVec.getZ()) {
                                    return xyList.get(z);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public Block3D getBlockAt(final vec3.Int pos, Sortmode mode) {
        ArrayList<CoordinatedList<CoordinatedList<Block3D>>> list = mode.getMySortList();
        vec3.Int sortedVec = XYZ.getSortedVec(pos, result);
        ArrayList<CoordinatedList<CoordinatedList<Block3D>>> preSortXLists = new ArrayList<CoordinatedList<CoordinatedList<Block3D>>>();
        ArrayList<CoordinatedList<Block3D>> preSortXYLists = new ArrayList<CoordinatedList<Block3D>>();
        if (list != null && sortedVec != null) {
            for (CoordinatedList<CoordinatedList<Block3D>> xList : list) {
                if (xList.coordinate + xList.getExtendsInNext() >= sortedVec.getX() && xList.coordinate <= sortedVec.getX()){
                    preSortXLists.add(xList);
                }
            }
            for (CoordinatedList<CoordinatedList<Block3D>> yList : preSortXLists) {
                for (int y = 0; y < yList.size(); y++) {
                    if (yList.get(y).coordinate >= sortedVec.getY() && yList.get(y).coordinate <= sortedVec.getY() + range.yR || (yList.get(y).coordinate <= sortedVec.getY() && yList.get(y).coordinate >= sortedVec.getY() + range.yR)) {
                        preSortXYLists.add(yList.get(y));
                    }
                }
            }
            for (CoordinatedList<Block3D> xyList : preSortXYLists) {
                for (int z = 0; z < xyList.size(); z++) {
                    if (xyList.get(z).position.z >= sortedVec.getZ() && xyList.get(z).position.z <= sortedVec.getZ() + range.zR || (xyList.get(z).position.z <= sortedVec.getZ() && xyList.get(z).position.z >= sortedVec.getZ() + range.zR)) {
                        blockz.add(xyList.get(z));
                    }
                }
            }
            return blockz;
        }
        return null;
    }

    public Block3D getBlockAt(int x, int y, int z, Sortmode mode) {
        return this.getBlockAt(new vec3.Int(x, y, z), mode);
    }

    public Block3D getBlockAt(int x, int y, int z) {
        return this.getBlockAt(new vec3.Int(x, y, z), XYZ);
    }

    public Block3D getBlockAt(vec3.Int inp) {
        return this.getBlockAt(inp, XYZ);
    }

    @Nullable
    public ArrayList<Block3D> getBlockInRange(final vec3.Int pos, final Range3D range) {
        ArrayList<CoordinatedList<CoordinatedList<Block3D>>> list = XYZ.getMySortList();
        vec3.Int sortedVec = XYZ.getSortedVec(pos, result);
        ArrayList<CoordinatedList<CoordinatedList<Block3D>>> preSortXLists = new ArrayList<CoordinatedList<CoordinatedList<Block3D>>>();
        ArrayList<CoordinatedList<Block3D>> preSortXYLists = new ArrayList<CoordinatedList<Block3D>>();
        ArrayList<Block3D> blockz = new ArrayList<Block3D>();
        if (list != null && sortedVec != null) {
            for (CoordinatedList<CoordinatedList<Block3D>> xList : list) {
                if (xList.coordinate >= sortedVec.getX() && xList.coordinate <= sortedVec.getX() + range.xR || (xList.coordinate <= sortedVec.getX() && xList.coordinate >= sortedVec.getX() + range.xR)) {
                    preSortXLists.add(xList);
                }
            }
            for (CoordinatedList<CoordinatedList<Block3D>> yList : preSortXLists) {
                for (int y = 0; y < yList.size(); y++) {
                    if (yList.get(y).coordinate >= sortedVec.getY() && yList.get(y).coordinate <= sortedVec.getY() + range.yR || (yList.get(y).coordinate <= sortedVec.getY() && yList.get(y).coordinate >= sortedVec.getY() + range.yR)) {
                        preSortXYLists.add(yList.get(y));
                    }
                }
            }
            for (CoordinatedList<Block3D> xyList : preSortXYLists) {
                for (int z = 0; z < xyList.size(); z++) {
                    if (xyList.get(z).position.z >= sortedVec.getZ() && xyList.get(z).position.z <= sortedVec.getZ() + range.zR || (xyList.get(z).position.z <= sortedVec.getZ() && xyList.get(z).position.z >= sortedVec.getZ() + range.zR)) {
                        blockz.add(xyList.get(z));
                    }
                }
            }
            return blockz;
        }
        return null;
    }

    public ArrayList<Block3D> getBlockInRange(int x, int y, int z, Range3D range3D) {
        return this.getBlockInRange(new vec3.Int(x, y, z), range3D);
    }

    public ArrayList<Block3D> getBlockInRange(int x, int y, int z) {
        return this.getBlockInRange(new vec3.Int(x, y, z), new Range3D(1,1,1));
    }

    public ArrayList<Block3D> getBlockInRange(vec3.Int inp) {
        return this.getBlockInRange(inp, new Range3D(1, 1, 1));
    }

    public boolean setBlockAt() {

    }

    public static final class Range3D {
        final int xR, yR, zR;
        public Range3D(int xR, int yR, int zR) {
            if(xR != 0)
                this.xR = xR;
            else
                this.xR = 1;
            if(yR != 0)
                this.yR = yR;
            else
                this.yR = 1;
            if(zR != 0)
                this.zR = zR;
            else
                this.zR = 1;
        }
    }

    public enum Sortmode {
        XYZ, XZY, YXZ, YZX, ZXY, ZYX;

        @Nullable
        public ArrayList<CoordinatedList<CoordinatedList<Block3D>>> getMySortList() {
            switch (this) {
                case XYZ:
                    return xyzSort;
                case XZY:
                    return xzySort;
                case YXZ:
                    return yxzSort;
                case YZX:
                    return yzxSort;
                case ZXY:
                    return zxySort;
                case ZYX:
                    return zyxSort;
                default:
                    return null;
            }
        }

        @Nullable
        public vec3.Int getSortedVec(vec3.Int input, vec3.Int result) {
            switch (this) {
                case XYZ:
                    return result.update(input.x, input.y, input.z);
                case XZY:
                    return result.update(input.x, input.z, input.y);
                case YXZ:
                    return result.update(input.y, input.x, input.z);
                case YZX:
                    return result.update(input.y, input.z, input.x);
                case ZXY:
                    return result.update(input.z, input.x, input.y);
                case ZYX:
                    return result.update(input.z, input.y, input.x);
                default:
                    return null;
            }
        }
    }

    private static final class CoordinatedList <E> extends ArrayList{
        final int coordinate;
        private int extendsInNext;

        CoordinatedList(int coordinate) {
            this.coordinate = coordinate;
        }

        @SuppressWarnings("unchecked")
        public E get(int index) {
            return (E) super.get(index);
        }

        public void setExtendsInNext(int ext) {
            if(ext >= 0)
                this.extendsInNext = ext;
            else
                this.extendsInNext = 0;
        }

        public int getExtendsInNext() {
            return this.extendsInNext;
        }
    }
}
