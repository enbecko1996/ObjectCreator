package com.enbecko.modcreator.linalg;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.util.vector.Quaternion;

import javax.annotation.Nullable;

/**
 * Created by Niclas on 15.11.2016.
 */
public abstract class vec3 <T extends Number> extends vec_n<T> {
    public vec3() {
        super(3);
    }

    public vec3(double x, double y, double z, boolean floor) {
        super(floor, x, y, z);
    }

    public vec3(vec3 other, boolean floor) {
        super(other, floor);
    }

    public vec3(Vec3d other, boolean floor) {
        super(floor, other.xCoord, other.yCoord, other.zCoord);
    }

    public vec3(vec4 other, boolean normalize, boolean floor) {
        super(floor,
                normalize ? other.getXD() / other.getWD() : other.getXD(),
                normalize ? other.getYD() / other.getWD() : other.getYD(),
                normalize ? other.getZD() / other.getWD() : other.getZD());
    }

    public double angleTo360(vec3 other, vec3 normal) {
        vec3 cross = this.cross(other, false);
        double det = normal.dot(cross);
        double dot = this.dot(other);
        return Math.atan2(det, dot) + Math.PI;
    }

    public vec4 angleTo360(vec3 other) {
        vec3 normal = (vec3) this.cross(other, false).normalize();
        vec3 normalTmp = new vec3.DoubleVec(normal);
        if (normal.getXD() < 0) {
            normal.mulToThis(-1);
        } else if (normal.getYD() < 0) {
            normal.mulToThis(-1);
        } else if (normal.getZD() < 0) {
            normal.mulToThis(-1);
        }
        return new vec4.DoubleVec(normalTmp.getXD(), normalTmp.getYD(), normalTmp.getZD(), this.angleTo360(other, normal));
    }

    @Override
    public int getSize() {
        return 3;
    }

    public double getXD() {
        return this.vec[0];
    }

    public double getYD() {
        return this.vec[1];
    }

    public double getZD() {
        return this.vec[2];
    }

    public float getXF() {
        return (float) this.vec[0];
    }

    public float getYF() {
        return (float) this.vec[1];
    }

    public float getZF() {
        return (float) this.vec[2];
    }

    public abstract vec3 cross(vec3 other, boolean floor);

    public vec3 update(double x, double y, double z) {
        return this.update(x, y, z, false);
    }
    public vec3 update(double x, double y, double z, boolean floor) {
        return (vec3) super.update(0, floor, x, y, z);
    }

    public vec3 update(vec4 other, boolean normalize, boolean floor) {
        return (vec3) super.update(0, floor,
                normalize ? other.getXD() / other.getWD() : other.getXD(),
                normalize ? other.getYD() / other.getWD() : other.getYD(),
                normalize ? other.getZD() / other.getWD() : other.getZD());
    }

    public vec3 update(Vec3d other, boolean floor) {
        return (vec3) super.update(0, floor, other.xCoord, other.yCoord, other.zCoord);
    }

    public double updateAndGetDelta(Vec3d other, boolean floor) {
        double out = Math.sqrt(Math.pow(other.xCoord - this.vec[0], 2) + Math.pow(other.yCoord - this.vec[1], 2) + Math.pow(other.zCoord - this.vec[2], 2));
        this.update(other, floor);
        return out;
    }

    public abstract void setX(double x);
    public abstract void setY(double y);
    public abstract void setZ(double z);

    public static vec3 newVecWithPrecision(vecPrec prec, vec3 old, boolean floor) {
        switch (prec) {
            case BYTE:
                return new vec3.ByteVec(old, floor);
            case SHORT:
                return new vec3.ShortVec(old, floor);
            case INT:
                return new vec3.IntVec(old, floor);
            case LONG:
                return new vec3.LongVec(old, floor);
            case FLOAT:
                return new vec3.FloatVec(old, floor);
            default:
                return new vec3.DoubleVec(old, floor);
        }
    }

    public static vec3 newVecWithPrecision(vecPrec prec, double x, double y, double z, boolean floor) {
        switch (prec) {
            case BYTE:
                return new vec3.ByteVec(x, y, z, floor);
            case SHORT:
                return new vec3.ShortVec(x, y, z, floor);
            case INT:
                return new vec3.IntVec(x, y, z, floor);
            case LONG:
                return new vec3.LongVec(x, y, z, floor);
            case FLOAT:
                return new vec3.FloatVec(x, y, z, floor);
            default:
                return new vec3.DoubleVec(x, y, z, floor);
        }
    }

    @Override
    @Nullable
    public vec_n mulAndMakeNew(vecPrec prec, double length, boolean floor) {
        switch (prec) {
            case BYTE:
                return new vec3.ByteVec(this, floor).mulToThis(length);
            case SHORT:
                return new vec3.ShortVec(this, floor).mulToThis(length);
            case INT:
                return new vec3.IntVec(this, floor).mulToThis(length);
            case LONG:
                return new vec3.LongVec(this, floor).mulToThis(length);
            case FLOAT:
                return new vec3.FloatVec(this, floor).mulToThis(length);
            default:
                return new vec3.DoubleVec(this, floor).mulToThis(length);
        }
    }

    @Override
    @Nullable
    public vec_n addAndMakeNew(vecPrec prec, vec_n other, boolean floor) {
        switch (prec) {
            case BYTE:
                return new vec3.ByteVec(this, floor).addToThis(other);
            case SHORT:
                return new vec3.ShortVec(this, floor).addToThis(other);
            case INT:
                return new vec3.IntVec(this, floor).addToThis(other);
            case LONG:
                return new vec3.LongVec(this, floor).addToThis(other);
            case FLOAT:
                return new vec3.FloatVec(this, floor).addToThis(other);
            default:
                return new vec3.DoubleVec(this, floor).addToThis(other);
        }
    }

    //@Override
    @Nullable
    public vec_n addAndMakeNew(vecPrec prec, boolean floor, double ... cont) {
        switch (prec) {
            case BYTE:
                return new vec3.ByteVec(this, floor).addToThis(cont);
            case SHORT:
                return new vec3.ShortVec(this, floor).addToThis(cont);
            case INT:
                return new vec3.IntVec(this, floor).addToThis(cont);
            case LONG:
                return new vec3.LongVec(this, floor).addToThis(cont);
            case FLOAT:
                return new vec3.FloatVec(this, floor).addToThis(cont);
            default:
                return new vec3.DoubleVec(this, floor).addToThis(cont);
        }
    }

    @Override
    @Nullable
    public vec_n subAndMakeNew(vecPrec prec, vec_n other, boolean floor) {
        switch (prec) {
            case BYTE:
                return new vec3.ByteVec(this, floor).subFromThis(other);
            case SHORT:
                return new vec3.ShortVec(this, floor).subFromThis(other);
            case INT:
                return new vec3.IntVec(this, floor).subFromThis(other);
            case LONG:
                return new vec3.LongVec(this, floor).subFromThis(other);
            case FLOAT:
                return new vec3.FloatVec(this, floor).subFromThis(other);
            default:
                return new vec3.DoubleVec(this, floor).subFromThis(other);
        }
    }

    @Override
    @Nullable
    public vec_n divAndMakeNew(vecPrec prec, double length, boolean floor) {
        switch (prec) {
            case BYTE:
                return new vec3.ByteVec(this, floor).divToThis(length);
            case SHORT:
                return new vec3.ShortVec(this, floor).divToThis(length);
            case INT:
                return new vec3.IntVec(this, floor).divToThis(length);
            case LONG:
                return new vec3.LongVec(this, floor).divToThis(length);
            case FLOAT:
                return new vec3.FloatVec(this, floor).divToThis(length);
            default:
                return new vec3.DoubleVec(this, floor).divToThis(length);
        }
    }

    @Override
    public vec3<T> setChangeable(boolean changeable) {
        this.isChangeable = changeable;
        return this;
    }

    public static class IntVec extends vec3<Integer> {
        private BlockPos blockPos;

        public IntVec() {
            super();
        }

        public IntVec(double x, double y, double z, boolean floor) {
            super(x, y, z, floor);
        }

        public IntVec(vec3 other, boolean floor) {
            super(other, floor);
        }

        public IntVec(vec3 other) {
            super(other, false);
        }

        public IntVec(Vec3d other, boolean floor) {
            super(other, floor);
        }

        public IntVec(Vec3d other) {
            super(other, false);
        }

        public IntVec(vec4 other, boolean normalize, boolean floor) {
            super(other, normalize, floor);
        }

        public IntVec(vec4 other, boolean normalize) {
            super(other, normalize, false);
        }

        public IntVec(int x, int y, int z) {
            super(x, y, z, false);
        }

        public IntVec(BlockPos pos) {
            super(pos.getX(), pos.getY(), pos.getZ(), false);
        }

        @Override
        public vec3.IntVec cross(vec3 other, boolean floor) {
            return new vec3.IntVec((this.vec[1] * other.getZD() - this.vec[2] * other.getYD()), this.vec[2] * other.getXD() - this.vec[0] * other.getZD(), this.vec[0] * other.getYD() - this.vec[1] * other.getXD(), floor);
        }

        @Override
        public void setX(double x) {
            if(this.isChangeable) {
                this.vec[0] = (int) x;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setY(double y) {
            if(this.isChangeable) {
                this.vec[1] = (int) y;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setZ(double z) {
            if(this.isChangeable) {
                this.vec[2] = (int) z;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        public int getX() {
            return (int) this.vec[0];
        }

        public int getY() {
            return (int) this.vec[1];
        }

        public int getZ() {
            return (int) this.vec[2];
        }

        @Override
        public vecPrec getPrecision() {
            return vecPrec.INT;
        }

        @Override
        public void applyPrecision(boolean floor) {
            for (int k = 0; k < this.getSize(); k++) {
                if (floor)
                    this.vec[k] = (int) Math.floor(this.vec[k]);
                else
                    this.vec[k] = (int) this.vec[k];
            }
        }

        @Override
        public void setVec(boolean floor, double... content) {
            if (content.length == 3) {
                for (int k = 0; k < 3; k++)
                    if (floor)
                        this.vec[k] = (int) Math.floor(content[k]);
                    else
                        this.vec[k] = (int) content[k];
            } else {
                throw new RuntimeException("Can't create vec3 from content with not 3 components.");
            }
        }

        public BlockPos asBlockPos() {
            if (this.blockPos == null)
                this.blockPos = new BlockPos(this.getX(), this.getY(), this.getZ());
            return this.blockPos;
        }
    }

    public static class LongVec extends vec3<Long> {
        public LongVec() {
            super();
        }

        public LongVec(double x, double y, double z, boolean floor) {
            super(x, y, z, floor);
        }

        public LongVec(vec3 other, boolean floor) {
            super(other, floor);
        }

        public LongVec(vec3 other) {
            super(other, false);
        }

        public LongVec(Vec3d other, boolean floor) {
            super(other, floor);
        }

        public LongVec(Vec3d other) {
            super(other, false);
        }

        public LongVec(vec4 other, boolean normalize, boolean floor) {
            super(other, normalize, floor);
        }

        public LongVec(vec4 other, boolean normalize) {
            super(other, normalize, false);
        }
        public LongVec(long x, long y, long z) {
            super(x, y, z, false);
        }

        @Override
        public vec3.LongVec cross(vec3 other, boolean floor) {
            return new vec3.LongVec((this.vec[1] * other.getZD() - this.vec[2] * other.getYD()), this.vec[2] * other.getXD() - this.vec[0] * other.getZD(), this.vec[0] * other.getYD() - this.vec[1] * other.getXD(), floor);
        }

        @Override
        public void setX(double x) {
            if(this.isChangeable) {
                this.vec[0] = (long) x;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setY(double y) {
            if(this.isChangeable) {
                this.vec[1] = (long) y;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setZ(double z) {
            if(this.isChangeable) {
                this.vec[2] = (long) z;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        public long getX() {
            return (long) this.vec[0];
        }

        public long getY() {
            return (long) this.vec[1];
        }

        public long getZ() {
            return (long) this.vec[2];
        }

        @Override
        public vecPrec getPrecision() {
            return vecPrec.LONG;
        }

        @Override
        public void applyPrecision(boolean floor) {
            for (int k = 0; k < this.getSize(); k++) {
                if (floor)
                    this.vec[k] = (long) Math.floor(this.vec[k]);
                else
                    this.vec[k] = (long) this.vec[k];
            }
        }

        @Override
        public void setVec(boolean floor, double... content) {
            if (content.length == 3) {
                for (int k = 0; k < 3; k++)
                    if (floor)
                        this.vec[k] = (long) Math.floor(content[k]);
                    else
                        this.vec[k] = (long) content[k];
            } else {
                throw new RuntimeException("Can't create vec3 from content with not 3 components.");
            }
        }

    }

    public static class DoubleVec extends vec3<Double> {
        public DoubleVec() {
            super();
        }

        public DoubleVec(double x, double y, double z, boolean floor) {
            super(x, y, z, floor);
        }

        public DoubleVec(vec3 other) {
            super(other, false);
        }

        public DoubleVec(Vec3d other, boolean floor) {
            super(other, floor);
        }

        public DoubleVec(Vec3d other) {
            super(other, false);
        }

        public DoubleVec(vec4 other, boolean normalize, boolean floor) {
            super(other, normalize, floor);
        }

        public DoubleVec(vec4 other, boolean normalize) {
            super(other, normalize, false);
        }

        public DoubleVec(double x, double y, double z) {
            super(x, y, z, false);
        }

        public DoubleVec(vec3 other, boolean floor) {
            super(other, floor);
        }


        @Override
        public vec3.DoubleVec cross(vec3 other, boolean floor) {
            return new vec3.DoubleVec((this.vec[1] * other.getZD() - this.vec[2] * other.getYD()), this.vec[2] * other.getXD() - this.vec[0] * other.getZD(), this.vec[0] * other.getYD() - this.vec[1] * other.getXD(), floor);
        }

        @Override
        public void setX(double x) {
            if(this.isChangeable) {
                this.vec[0] = x;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setY(double y) {
            if(this.isChangeable) {
                this.vec[1] = y;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setZ(double z) {
            if(this.isChangeable) {
                this.vec[2] = z;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        public double getX() {
            return this.vec[0];
        }

        public double getY() {
            return this.vec[1];
        }

        public double getZ() {
            return this.vec[2];
        }

        @Override
        public vecPrec getPrecision() {
            return vecPrec.DOUBLE;
        }

        @Override
        public void applyPrecision(boolean floor) {
        }

        @Override
        public void setVec(boolean floor, double... content) {
            if (content.length == 3) {
                System.arraycopy(content, 0, this.vec, 0, 3);
            } else {
                throw new RuntimeException("Can't create vec3 from content with not 3 components.");
            }
        }
    }

    public static class FloatVec extends vec3<Float> {
        public FloatVec() {
            super();
        }

        public FloatVec(double x, double y, double z, boolean floor) {
            super(x, y, z, floor);
        }

        public FloatVec(Vec3d other, boolean floor) {
            super(other, floor);
        }

        public FloatVec(Vec3d other) {
            super(other, false);
        }

        public FloatVec(vec3 other, boolean floor) {
            super(other, floor);
        }

        public FloatVec(vec3 other) {
            super(other, false);
        }

        public FloatVec(vec4 other, boolean normalize, boolean floor) {
            super(other, normalize, floor);
        }

        public FloatVec(vec4 other, boolean normalize) {
            super(other, normalize, false);
        }

        public FloatVec(float x, float y, float z) {
            super(x, y, z, false);
        }

        @Override
        public vec3.FloatVec cross(vec3 other, boolean floor) {
            return new vec3.FloatVec((this.vec[1] * other.getZD() - this.vec[2] * other.getYD()), this.vec[2] * other.getXD() - this.vec[0] * other.getZD(), this.vec[0] * other.getYD() - this.vec[1] * other.getXD(), floor);
        }

        @Override
        public void setX(double x) {
            if(this.isChangeable) {
                this.vec[0] = (float) x;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setY(double y) {
            if(this.isChangeable) {
                this.vec[1] = (float) y;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setZ(double z) {
            if(this.isChangeable) {
                this.vec[2] = (float) z;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        public float getX() {
            return (float) this.vec[0];
        }

        public float getY() {
            return (float) this.vec[1];
        }

        public float getZ() {
            return (float) this.vec[2];
        }

        @Override
        public vecPrec getPrecision() {
            return vecPrec.FLOAT;
        }

        @Override
        public void applyPrecision(boolean floor) {
            for (int k = 0; k < this.getSize(); k++)
                this.vec[k] = (float) this.vec[k];
        }

        @Override
        public void setVec(boolean floor, double... content) {
            if (content.length == 3) {
                for (int k = 0; k < 3; k++)
                    this.vec[k] = (float) content[k];
            } else {
                throw new RuntimeException("Can't create vec3 from content with not 3 components.");
            }
        }
    }

    public static class ShortVec extends vec3<Short> {
        public ShortVec() {
            super();
        }

        public ShortVec(double x, double y, double z, boolean floor) {
            super(x, y, z, floor);
        }

        public ShortVec(Vec3d other, boolean floor) {
            super(other, floor);
        }

        public ShortVec(Vec3d other) {
            super(other, false);
        }

        public ShortVec(vec3 other, boolean floor) {
            super(other, floor);
        }

        public ShortVec(vec3 other) {
            super(other, false);
        }

        public ShortVec(vec4 other, boolean normalize, boolean floor) {
            super(other, normalize, floor);
        }

        public ShortVec(vec4 other, boolean normalize) {
            super(other, normalize, false);
        }

        public ShortVec(short x, short y, short z) {
            super(x, y, z, false);
        }

        @Override
        public vec3.ShortVec cross(vec3 other, boolean floor) {
            return new vec3.ShortVec((this.vec[1] * other.getZD() - this.vec[2] * other.getYD()), this.vec[2] * other.getXD() - this.vec[0] * other.getZD(), this.vec[0] * other.getYD() - this.vec[1] * other.getXD(), floor);
        }

        @Override
        public void setX(double x) {
            if(this.isChangeable) {
                this.vec[0] = (short) x;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setY(double y) {
            if(this.isChangeable) {
                this.vec[1] = (short) y;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setZ(double z) {
            if(this.isChangeable) {
                this.vec[2] = (short) z;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        public short getX() {
            return (short) this.vec[0];
        }

        public short getY() {
            return (short) this.vec[1];
        }

        public short getZ() {
            return (short) this.vec[2];
        }

        @Override
        public vecPrec getPrecision() {
            return vecPrec.SHORT;
        }

        @Override
        public void applyPrecision(boolean floor) {
            for (int k = 0; k < this.getSize(); k++) {
                if (floor)
                    this.vec[k] = (short) Math.floor(this.vec[k]);
                else
                    this.vec[k] = (short) this.vec[k];this.vec[k] = (short) this.vec[k];
            }
        }

        @Override
        public void setVec(boolean floor, double... content) {
            if (content.length == 3) {
                for (int k = 0; k < 3; k++)
                    if (floor)
                        this.vec[k] = (short) Math.floor(content[k]);
                    else
                        this.vec[k] = (short) content[k];
            } else {
                throw new RuntimeException("Can't create vec3 from content with not 3 components.");
            }
        }
    }

    public static class ByteVec extends vec3<Byte> {
        public ByteVec() {
            super();
        }

        public ByteVec(double x, double y, double z, boolean floor) {
            super(x, y, z, floor);
        }

        public ByteVec(Vec3d other, boolean floor) {
            super(other, floor);
        }

        public ByteVec(Vec3d other) {
            super(other, false);
        }

        public ByteVec(vec3 other, boolean floor) {
            super(other, floor);
        }

        public ByteVec(vec3 other) {
            super(other, false);
        }

        public ByteVec(vec4 other, boolean normalize, boolean floor) {
            super(other, normalize, floor);
        }

        public ByteVec(vec4 other, boolean normalize) {
            super(other, normalize, false);
        }

        public ByteVec(byte x, byte y, byte z) {
            super(x, y, z, false);
        }

        @Override
        public vec3.ByteVec cross(vec3 other, boolean floor) {
            return new vec3.ByteVec((this.vec[1] * other.getZD() - this.vec[2] * other.getYD()), this.vec[2] * other.getXD() - this.vec[0] * other.getZD(), this.vec[0] * other.getYD() - this.vec[1] * other.getXD(), floor);
        }

        @Override
        public void setX(double x) {
            if(this.isChangeable) {
                this.vec[0] = (byte) x;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setY(double y) {
            if(this.isChangeable) {
                this.vec[1] = (byte) y;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setZ(double z) {
            if(this.isChangeable) {
                this.vec[2] = (byte) z;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        public byte getX() {
            return (byte) this.vec[0];
        }

        public byte getY() {
            return (byte) this.vec[1];
        }

        public byte getZ() {
            return (byte) this.vec[2];
        }

        @Override
        public vecPrec getPrecision() {
            return vecPrec.BYTE;
        }

        @Override
        public void applyPrecision(boolean floor) {
            for (int k = 0; k < this.getSize(); k++){
                if (floor)
                    this.vec[k] = (byte) Math.floor(this.vec[k]);
                else
                    this.vec[k] = (byte) this.vec[k];this.vec[k] = (short) this.vec[k];
            }
        }

        @Override
        public void setVec(boolean floor, double... content) {
            if (content.length == 3) {
                for (int k = 0; k < 3; k++) {
                    if (floor)
                        this.vec[k] = (byte) Math.floor(content[k]);
                    else
                        this.vec[k] = (byte) content[k];
                }
            } else {
                throw new RuntimeException("Can't create vec3 from content with not 3 components.");
            }
        }
    }
}