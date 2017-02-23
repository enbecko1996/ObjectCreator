package com.enbecko.modcreator.linalg;

import javax.annotation.Nullable;

/**
 * Created by Niclas on 15.11.2016.
 */
public abstract class vec3 <T extends Number> extends vec_n<T> {
    public vec3() {
        super(3);
    }

    public vec3(double x, double y, double z) {
        super(x, y, z);
    }

    public vec3(vec3 other) {
        super(other);
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

    public double getXF() {
        return (float) this.vec[0];
    }

    public double getYF() {
        return (float) this.vec[1];
    }

    public double getZF() {
        return (float) this.vec[2];
    }

    public abstract vec3 cross(vec3 other);

    public abstract void update(double x, double y, double z);
    public abstract void setX(double x);
    public abstract void setY(double y);
    public abstract void setZ(double z);

    public static vec3 newVecWithPrecision(vecPrec prec, vec3 old) {
        switch (prec) {
            case BYTE:
                return new vec3.ByteVec(old);
            case SHORT:
                return new vec3.ShortVec(old);
            case INT:
                return new vec3.IntVec(old);
            case LONG:
                return new vec3.LongVec(old);
            case FLOAT:
                return new vec3.FloatVec(old);
            default:
                return new vec3.DoubleVec(old);
        }
    }

    @Override
    @Nullable
    public vec_n mulAndMakeNew(vecPrec prec, double length) {
        switch (prec) {
            case BYTE:
                return new vec3.ByteVec(this).mulToThis(length);
            case SHORT:
                return new vec3.ShortVec(this).mulToThis(length);
            case INT:
                return new vec3.IntVec(this).mulToThis(length);
            case LONG:
                return new vec3.LongVec(this).mulToThis(length);
            case FLOAT:
                return new vec3.FloatVec(this).mulToThis(length);
            default:
                return new vec3.DoubleVec(this).mulToThis(length);
        }
    }

    @Override
    @Nullable
    public vec_n addAndMakeNew(vecPrec prec, vec_n other) {
        switch (prec) {
            case BYTE:
                return new vec3.ByteVec(this).addToThis(other);
            case SHORT:
                return new vec3.ShortVec(this).addToThis(other);
            case INT:
                return new vec3.IntVec(this).addToThis(other);
            case LONG:
                return new vec3.LongVec(this).addToThis(other);
            case FLOAT:
                return new vec3.FloatVec(this).addToThis(other);
            default:
                return new vec3.DoubleVec(this).addToThis(other);
        }
    }

    @Override
    @Nullable
    public vec_n subAndMakeNew(vecPrec prec, vec_n other) {
        switch (prec) {
            case BYTE:
                return new vec3.ByteVec(this).subFromThis(other);
            case SHORT:
                return new vec3.ShortVec(this).subFromThis(other);
            case INT:
                return new vec3.IntVec(this).subFromThis(other);
            case LONG:
                return new vec3.LongVec(this).subFromThis(other);
            case FLOAT:
                return new vec3.FloatVec(this).subFromThis(other);
            default:
                return new vec3.DoubleVec(this).subFromThis(other);
        }
    }

    @Override
    @Nullable
    public vec_n divAndMakeNew(vecPrec prec, double length) {
        switch (prec) {
            case BYTE:
                return new vec3.ByteVec(this).divToThis(length);
            case SHORT:
                return new vec3.ShortVec(this).divToThis(length);
            case INT:
                return new vec3.IntVec(this).divToThis(length);
            case LONG:
                return new vec3.LongVec(this).divToThis(length);
            case FLOAT:
                return new vec3.FloatVec(this).divToThis(length);
            default:
                return new vec3.DoubleVec(this).divToThis(length);
        }
    }

    public vec3<T> setChangeable(boolean changeable) {
        this.isChangeable = changeable;
        return this;
    }

    public static class IntVec extends vec3<Integer> {
        public IntVec() {
            super();
        }

        public IntVec(double x, double y, double z) {
            super(x, y, z);
        }

        public IntVec(vec3 other) {
            super(other);
        }
        
        @Override
        public vec3.IntVec cross(vec3 other) {
            return new vec3.IntVec((this.vec[1] * other.getZD() - this.vec[2] * other.getYD()), this.vec[2] * other.getXD() - this.vec[0] * other.getZD(), this.vec[0] * other.getYD() - this.vec[1] * other.getXD());
        }

        @Override
        public void update(double x, double y, double z) {
            if(this.isChangeable) {
                this.vec[0] = (int) x;
                this.vec[1] = (int) y;
                this.vec[2] = (int) z;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
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
        public void applyPrecision() {
            for (int k = 0; k < this.getSize(); k++)
                this.vec[k] = (int) this.vec[k];
        }

        @Override
        public void setVec(double... content) {
            if (content.length == 3) {
                for (int k = 0; k < 3; k++)
                    this.vec[k] = (int) content[k];
            } else {
                throw new RuntimeException("Can't create vec3 from content with not 3 components.");
            }
        }
    }

    public static class LongVec extends vec3<Long> {
        public LongVec() {
            super();
        }

        public LongVec(double x, double y, double z) {
            super(x, y, z);
        }

        public LongVec(vec3 other) {
            super(other);
        }

        @Override
        public vec3.LongVec cross(vec3 other) {
            return new vec3.LongVec((this.vec[1] * other.getZD() - this.vec[2] * other.getYD()), this.vec[2] * other.getXD() - this.vec[0] * other.getZD(), this.vec[0] * other.getYD() - this.vec[1] * other.getXD());
        }
        
        @Override
        public void update(double x, double y, double z) {
            if(this.isChangeable) {
                this.vec[0] = (long) x;
                this.vec[1] = (long) y;
                this.vec[2] = (long) z;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
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
        public void applyPrecision() {
            for (int k = 0; k < this.getSize(); k++)
                this.vec[k] = (long) this.vec[k];
        }

        @Override
        public void setVec(double... content) {
            if (content.length == 3) {
                for (int k = 0; k < 3; k++)
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

        public DoubleVec(double x, double y, double z) {
            super(x, y, z);
        }

        public DoubleVec(vec3 other) {
            super(other);
        }

        @Override
        public vec3.DoubleVec cross(vec3 other) {
            return new vec3.DoubleVec((this.vec[1] * other.getZD() - this.vec[2] * other.getYD()), this.vec[2] * other.getXD() - this.vec[0] * other.getZD(), this.vec[0] * other.getYD() - this.vec[1] * other.getXD());
        }
        
        @Override
        public void update(double x, double y, double z) {
            if(this.isChangeable) {
                this.vec[0] = x;
                this.vec[1] = y;
                this.vec[2] = z;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
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
        public void applyPrecision() {
        }

        @Override
        public void setVec(double... content) {
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

        public FloatVec(double x, double y, double z) {
            super(x, y, z);
        }

        public FloatVec(vec3 other) {
            super(other);
        }

        @Override
        public vec3.FloatVec cross(vec3 other) {
            return new vec3.FloatVec((this.vec[1] * other.getZD() - this.vec[2] * other.getYD()), this.vec[2] * other.getXD() - this.vec[0] * other.getZD(), this.vec[0] * other.getYD() - this.vec[1] * other.getXD());
        }
        
        @Override
        public void update(double x, double y, double z) {
            if(this.isChangeable) {
                this.vec[0] = (float) x;
                this.vec[1] = (float) y;
                this.vec[2] = (float) z;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
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
        public void applyPrecision() {
            for (int k = 0; k < this.getSize(); k++)
                this.vec[k] = (float) this.vec[k];
        }

        @Override
        public void setVec(double... content) {
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

        public ShortVec(double x, double y, double z) {
            super(x, y, z);
        }

        public ShortVec(vec3 other) {
            super(other);
        }

        @Override
        public vec3.ShortVec cross(vec3 other) {
            return new vec3.ShortVec((this.vec[1] * other.getZD() - this.vec[2] * other.getYD()), this.vec[2] * other.getXD() - this.vec[0] * other.getZD(), this.vec[0] * other.getYD() - this.vec[1] * other.getXD());
        }
        
        @Override
        public void update(double x, double y, double z) {
            if(this.isChangeable) {
                this.vec[0] = (short) x;
                this.vec[1] = (short) y;
                this.vec[2] = (short) z;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
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
        public void applyPrecision() {
            for (int k = 0; k < this.getSize(); k++)
                this.vec[k] = (short) this.vec[k];
        }

        @Override
        public void setVec(double... content) {
            if (content.length == 3) {
                for (int k = 0; k < 3; k++)
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

        public ByteVec(double x, double y, double z) {
            super(x, y, z);
        }

        public ByteVec(vec3 other) {
            super(other);
        }

        @Override
        public vec3.ByteVec cross(vec3 other) {
            return new vec3.ByteVec((this.vec[1] * other.getZD() - this.vec[2] * other.getYD()), this.vec[2] * other.getXD() - this.vec[0] * other.getZD(), this.vec[0] * other.getYD() - this.vec[1] * other.getXD());
        }
        
        @Override
        public void update(double x, double y, double z) {
            if(this.isChangeable) {
                this.vec[0] = (byte) x;
                this.vec[1] = (byte) y;
                this.vec[2] = (byte) z;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
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
        public void applyPrecision() {
            for (int k = 0; k < this.getSize(); k++)
                this.vec[k] = (byte) this.vec[k];
        }

        @Override
        public void setVec(double... content) {
            if (content.length == 3) {
                for (int k = 0; k < 3; k++)
                    this.vec[k] = (byte) content[k];
            } else {
                throw new RuntimeException("Can't create vec3 from content with not 3 components.");
            }
        }
    }
}