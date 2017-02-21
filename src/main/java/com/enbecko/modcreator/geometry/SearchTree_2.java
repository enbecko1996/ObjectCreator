package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.Main_ModCreator;
import com.enbecko.modcreator.linalg.vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Created by enbec on 07.01.2017.
 */
public class SearchTree_2 {

    private static BlockMatrix3x3[] xFaceyzSorted = new BlockMatrix3x3[Main_ModCreator.maxBlockSize];
    private static BlockMatrix3x3[] xFacezySorted = new BlockMatrix3x3[Main_ModCreator.maxBlockSize];
    private static BlockMatrix3x3[] yFacexzSorted = new BlockMatrix3x3[Main_ModCreator.maxBlockSize];
    private static BlockMatrix3x3[] yFacezxSorted = new BlockMatrix3x3[Main_ModCreator.maxBlockSize];
    private static BlockMatrix3x3[] zFacexySorted = new BlockMatrix3x3[Main_ModCreator.maxBlockSize];
    private static BlockMatrix3x3[] zFaceyxSorted = new BlockMatrix3x3[Main_ModCreator.maxBlockSize];
    private BlockMatrix3x3[][] allSortedLists = new BlockMatrix3x3[6][];

    public SearchTree_2() {
        this.allSortedLists[0] = xFaceyzSorted;
        this.allSortedLists[1] = xFacezySorted;
        this.allSortedLists[2] = yFacexzSorted;
        this.allSortedLists[3] = yFacezxSorted;
        this.allSortedLists[4] = zFacexySorted;
        this.allSortedLists[5] = zFaceyxSorted;
        for (BlockMatrix3x3[] matrix3x3 : allSortedLists) {
            for (int k = 0; k < matrix3x3.length; k++)
                matrix3x3[k] = new BlockMatrix3x3(k);
        }
    }

    public boolean isBlockInRange(Block3D block, Sortmode mode) {
        BlockMatrix3x3[] list = mode.getMySortList();
        if (list != null) {
            for (BlockMatrix3x3 aList : list) {
                if (aList.isBlockInRange(block.position, new Range3D(block.dimension, block.dimension, block.dimension), mode))
                    return true;
            }
        }
        return false;
    }

    public void setBlock(Block3D block) {
        if (block.dimension > 0 && block.dimension < Main_ModCreator.maxBlockSize) {
            if (this.isBlockInRange(block, Sortmode.XYZ)) {
                for (BlockMatrix3x3[] matrix3x3 : allSortedLists) {
                    matrix3x3.setBlock(block);
                }
            }
        }
    }

    public static final class Range3D{
        int xR, yR, zR;
        public Range3D(int xR, int yR, int zR) {
            if(xR > 0)
                this.xR = xR;
            else
                this.xR = 1;
            if(yR > 0)
                this.yR = yR;
            else
                this.yR = 1;
            if(zR > 0)
                this.zR = zR;
            else
                this.zR = 1;
        }

        public Range3D update(int x, int y, int z) {
            if(xR > 0)
                this.xR = xR;
            else
                this.xR = 1;
            if(yR > 0)
                this.yR = yR;
            else
                this.yR = 1;
            if(zR > 0)
                this.zR = zR;
            else
                this.zR = 1;
            return this;
        }
    }

    public enum Sortmode {
        XYZ, XZY, YXZ, YZX, ZXY, ZYX;

        @Nullable
        public vec3.Int makeSortedVec(vec3.Int input, vec3.Int result) {
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

        @Nullable
        public Range3D makeSortedRange(Range3D input, Range3D result) {
            switch (this) {
                case XYZ:
                    return result.update(input.xR, input.yR, input.zR);
                case XZY:
                    return result.update(input.xR, input.zR, input.yR);
                case YXZ:
                    return result.update(input.yR, input.xR, input.zR);
                case YZX:
                    return result.update(input.yR, input.zR, input.xR);
                case ZXY:
                    return result.update(input.zR, input.xR, input.yR);
                case ZYX:
                    return result.update(input.zR, input.yR, input.xR);
                default:
                    return null;
            }
        }

        @Nullable
        public BlockMatrix3x3[] getMySortList() {
            switch (this) {
                case XYZ:
                    return xFaceyzSorted;
                case XZY:
                    return xFacezySorted;
                case YXZ:
                    return yFacexzSorted;
                case YZX:
                    return yFacezxSorted;
                case ZXY:
                    return zFacexySorted;
                case ZYX:
                    return zFaceyxSorted;
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

    private static final class BlockMatrix3x3 {
        final int blockSize;
        private final ArrayList<BlockMatrix2x2> blockFaces = new ArrayList<BlockMatrix2x2>();

        public BlockMatrix3x3(int blockSize) {
            this.blockSize = blockSize;
        }

        public boolean isBlockInRange(vec3.Int pos, Range3D range, Sortmode mode) {
            vec3.Int sortedVec = new vec3.Int();
            mode.makeSortedVec(pos, sortedVec);
            Range3D sortedRange = new Range3D(0,0,0);
            mode.makeSortedRange(range, sortedRange);
            for (BlockMatrix2x2 face : this.blockFaces) {
                if (face.coordinate + this.blockSize >= sortedVec.getX() && face.coordinate <= sortedVec.getX() + range.xR) {
                    if(face.isBlockInRange(sortedVec, blockSize, sortedRange, mode)) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Nullable
        public Block3D getBlockAt(vec3.Int pos, Sortmode mode) {
            vec3.Int sortedVec = new vec3.Int();
            mode.makeSortedVec(pos, sortedVec);
            Block3D result;
            for (BlockMatrix2x2 face : this.blockFaces) {
                if (face.coordinate + this.blockSize >= sortedVec.getX() && face.coordinate <= sortedVec.getX()) {
                    if((result = face.getBlockAt(sortedVec, blockSize, mode)) != null) {
                        return result;
                    }
                }
            }
            return null;
        }
    }

    private static final class BlockMatrix2x2 {
        private final int coordinate;
        private final ArrayList<BlockLine> blockLines = new ArrayList<BlockLine>();

        public BlockMatrix2x2(int coordinate) {
            this.coordinate = coordinate;
        }

        private boolean isBlockInRange(vec3.Int sortedVec, int blockSize, Range3D sortedRange, Sortmode mode) {
            for (BlockLine line : blockLines) {
                if(line.coordinate + blockSize >= sortedVec.getY() && line.coordinate <= sortedVec.getY() + sortedRange.yR) {
                    if(line.isBlockInRange(sortedVec, blockSize, sortedRange, mode))
                        return true;
                }
            }
            return true;
        }

        @Nullable
        private Block3D getBlockAt(vec3.Int sortedVec, int blockSize, Sortmode mode) {
            Block3D result;
            for (BlockLine line : blockLines) {
                if(line.coordinate + blockSize >= sortedVec.getY() && line.coordinate <= sortedVec.getY()) {
                    if((result = line.getBlockAt(sortedVec, blockSize, mode)) != null)
                        return result;
                }
            }
            return null;
        }
    }

    private static final class BlockLine {
        private final int coordinate;
        private final ArrayList<Block3D> blocks = new ArrayList<Block3D>();

        public BlockLine(int coordinate) {
            this.coordinate = coordinate;
        }

        private boolean isBlockInRange(vec3.Int sortedVec, int blockSize, Range3D sortedRange, Sortmode mode) {
            vec3.Int sortBlockVec = new vec3.Int();
            for (Block3D block : blocks) {
                sortBlockVec = mode.makeSortedVec(block.position, sortBlockVec);
                if(sortBlockVec.getZ() + blockSize >= sortedVec.getZ() && sortBlockVec.getZ() <= sortedVec.getZ() + sortedRange.zR) {
                    return true;
                }
            }
            return false;
        }

        @Nullable
        private Block3D getBlockAt(vec3.Int sortedVec, int blockSize, Sortmode mode) {
            vec3.Int sortBlockVec = new vec3.Int();
            for (Block3D block : blocks) {
                sortBlockVec = mode.makeSortedVec(block.position, sortBlockVec);
                if(sortBlockVec.getZ() + blockSize >= sortedVec.getZ() && sortBlockVec.getZ() <= sortedVec.getZ()) {
                    return block;
                }
            }
            return null;
        }
    }
}
