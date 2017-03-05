package com.enbecko.modcreator.linalg;

import javax.annotation.Nullable;

/**
 * Created by Niclas on 15.11.2016.
 */
public abstract class vec2<T extends Number> extends vec_n<T> {
    public vec2() {
        super(2);
    }

    public vec2(double u, double v, boolean floor) {
        super(floor, u, v);
    }

    public vec2(vec2 other, boolean floor) {
        super(other, floor);
    }

    @Override
    public int getSize() {
        return 2;
    }

    public double getUD() {
        return this.vec[0];
    }

    public double getVD() {
        return this.vec[1];
    }

    public float getUF() {
        return (float) this.vec[0];
    }

    public float getVF() {
        return (float) this.vec[1];
    }
    
    public void update(double u, double v) {
        this.update(u, v, false);
    }
    public abstract void update(double u, double v, boolean floor);
    public abstract void setU(double u);
    public abstract void setV(double v);

    public static vec2 newVecWithPrecision(vecPrec prec, vec2 old, boolean floor) {
        switch (prec) {
            case BYTE:
                return new vec2.ByteVec(old, floor);
            case SHORT:
                return new vec2.ShortVec(old, floor);
            case INT:
                return new vec2.IntVec(old, floor);
            case LONG:
                return new vec2.LongVec(old, floor);
            case FLOAT:
                return new vec2.FloatVec(old, floor);
            default:
                return new vec2.DoubleVec(old, floor);
        }
    }

    @Override
    @Nullable
    public vec_n mulAndMakeNew(vecPrec prec, double length, boolean floor) {
        switch (prec) {
            case BYTE:
                return new vec2.ByteVec(this, floor).mulToThis(length);
            case SHORT:
                return new vec2.ShortVec(this, floor).mulToThis(length);
            case INT:
                return new vec2.IntVec(this, floor).mulToThis(length);
            case LONG:
                return new vec2.LongVec(this, floor).mulToThis(length);
            case FLOAT:
                return new vec2.FloatVec(this, floor).mulToThis(length);
            default:
                return new vec2.DoubleVec(this, floor).mulToThis(length);
        }
    }

    @Override
    @Nullable
    public vec_n addAndMakeNew(vecPrec prec, vec_n other, boolean floor) {
        switch (prec) {
            case BYTE:
                return new vec2.ByteVec(this, floor).addToThis(other);
            case SHORT:
                return new vec2.ShortVec(this, floor).addToThis(other);
            case INT:
                return new vec2.IntVec(this, floor).addToThis(other);
            case LONG:
                return new vec2.LongVec(this, floor).addToThis(other);
            case FLOAT:
                return new vec2.FloatVec(this, floor).addToThis(other);
            default:
                return new vec2.DoubleVec(this, floor).addToThis(other);
        }
    }

    //@Override
    @Nullable
    public vec_n addAndMakeNew(vecPrec prec, boolean floor, double ... cont) {
        switch (prec) {
            case BYTE:
                return new vec2.ByteVec(this, floor).addToThis(cont);
            case SHORT:
                return new vec2.ShortVec(this, floor).addToThis(cont);
            case INT:
                return new vec2.IntVec(this, floor).addToThis(cont);
            case LONG:
                return new vec2.LongVec(this, floor).addToThis(cont);
            case FLOAT:
                return new vec2.FloatVec(this, floor).addToThis(cont);
            default:
                return new vec2.DoubleVec(this, floor).addToThis(cont);
        }
    }

    @Override
    @Nullable
    public vec_n subAndMakeNew(vecPrec prec, vec_n other, boolean floor) {
        switch (prec) {
            case BYTE:
                return new vec2.ByteVec(this, floor).subFromThis(other);
            case SHORT:
                return new vec2.ShortVec(this, floor).subFromThis(other);
            case INT:
                return new vec2.IntVec(this, floor).subFromThis(other);
            case LONG:
                return new vec2.LongVec(this, floor).subFromThis(other);
            case FLOAT:
                return new vec2.FloatVec(this, floor).subFromThis(other);
            default:
                return new vec2.DoubleVec(this, floor).subFromThis(other);
        }
    }

    @Override
    @Nullable
    public vec_n divAndMakeNew(vecPrec prec, double length, boolean floor) {
        switch (prec) {
            case BYTE:
                return new vec2.ByteVec(this, floor).divToThis(length);
            case SHORT:
                return new vec2.ShortVec(this, floor).divToThis(length);
            case INT:
                return new vec2.IntVec(this, floor).divToThis(length);
            case LONG:
                return new vec2.LongVec(this, floor).divToThis(length);
            case FLOAT:
                return new vec2.FloatVec(this, floor).divToThis(length);
            default:
                return new vec2.DoubleVec(this, floor).divToThis(length);
        }
    }

    @Override
    public vec2<T> setChangeable(boolean changeable) {
        this.isChangeable = changeable;
        return this;
    }

    public static class IntVec extends vec2<Integer> {
        public IntVec() {
            super();
        }

        public IntVec(double u, double v, boolean floor) {
            super(u, v, floor);
        }

        public IntVec(vec2 other, boolean floor) {
            super(other, floor);
        }

        public IntVec(vec2 other) {
            super(other, false);
        }

        public IntVec(int u, int v) {
            super(u, v, false);
        }
        
        @Override
        public void update(double u, double v, boolean floor) {
            if(this.isChangeable) {
                if (floor) {
                    this.vec[0] = (int) Math.floor(u);
                    this.vec[1] = (int) Math.floor(v);
                } else {
                    this.vec[0] = (int) u;
                    this.vec[1] = (int) v;
                }
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setU(double u) {
            if(this.isChangeable) {
                this.vec[0] = (int) u;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setV(double v) {
            if(this.isChangeable) {
                this.vec[1] = (int) v;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        public int getU() {
            return (int) this.vec[0];
        }

        public int getV() {
            return (int) this.vec[1];
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
            if (content.length == 2) {
                for (int k = 0; k < 2; k++)
                    if (floor)
                        this.vec[k] = (int) Math.floor(content[k]);
                    else
                        this.vec[k] = (int) content[k];
            } else {
                throw new RuntimeException("Can't create vec2 from content with not 2 components.");
            }
        }
    }

    public static class LongVec extends vec2<Long> {
        public LongVec() {
            super();
        }

        public LongVec(double u, double v, boolean floor) {
            super(u, v, floor);
        }

        public LongVec(vec2 other, boolean floor) {
            super(other, floor);
        }

        public LongVec(vec2 other) {
            super(other, false);
        }

        public LongVec(long u, long v) {
            super(u, v, false);
        }
        
        @Override
        public void update(double u, double v, boolean floor) {
            if(this.isChangeable) {
                if (floor) {
                    this.vec[0] = (long) Math.floor(u);
                    this.vec[1] = (long) Math.floor(v);
                } else {
                    this.vec[0] = (long) u;
                    this.vec[1] = (long) v;
                }
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setU(double u) {
            if(this.isChangeable) {
                this.vec[0] = (long) u;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setV(double v) {
            if(this.isChangeable) {
                this.vec[1] = (long) v;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        public long getU() {
            return (long) this.vec[0];
        }

        public long getV() {
            return (long) this.vec[1];
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
            if (content.length == 2) {
                for (int k = 0; k < 2; k++)
                    if (floor)
                        this.vec[k] = (long) Math.floor(content[k]);
                    else
                        this.vec[k] = (long) content[k];
            } else {
                throw new RuntimeException("Can't create vec2 from content with not 2 components.");
            }
        }
    }

    public static class DoubleVec extends vec2<Double> {
        public DoubleVec() {
            super();
        }

        public DoubleVec(double u, double v, boolean floor) {
            super(u, v, floor);
        }

        public DoubleVec(vec2 other) {
            super(other, false);
        }

        public DoubleVec(double u, double v) {
            super(u, v, false);
        }

        public DoubleVec(vec2 other, boolean floor) {
            super(other, floor);
        }
        
        @Override
        public void update(double u, double v, boolean floor) {
            if(this.isChangeable) {
                this.vec[0] = u;
                this.vec[1] = v;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setU(double u) {
            if(this.isChangeable) {
                this.vec[0] = u;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setV(double v) {
            if(this.isChangeable) {
                this.vec[1] = v;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        public double getU() {
            return this.vec[0];
        }

        public double getV() {
            return this.vec[1];
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
            if (content.length == 2) {
                System.arraycopy(content, 0, this.vec, 0, 2);
            } else {
                throw new RuntimeException("Can't create vec2 from content with not 2 components.");
            }
        }
    }

    public static class FloatVec extends vec2<Float> {
        public FloatVec() {
            super();
        }

        public FloatVec(double u, double v, boolean floor) {
            super(u, v, floor);
        }

        public FloatVec(vec2 other, boolean floor) {
            super(other, floor);
        }

        public FloatVec(vec2 other) {
            super(other, false);
        }

        public FloatVec(float u, float v) {
            super(u, v, false);
        }
        
        @Override
        public void update(double u, double v, boolean floor) {
            if(this.isChangeable) {
                this.vec[0] = (float) u;
                this.vec[1] = (float) v;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setU(double u) {
            if(this.isChangeable) {
                this.vec[0] = (float) u;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setV(double v) {
            if(this.isChangeable) {
                this.vec[1] = (float) v;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }
        
        public float getU() {
            return (float) this.vec[0];
        }

        public float getV() {
            return (float) this.vec[1];
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
            if (content.length == 2) {
                for (int k = 0; k < 2; k++)
                    this.vec[k] = (float) content[k];
            } else {
                throw new RuntimeException("Can't create vec2 from content with not 2 components.");
            }
        }
    }

    public static class ShortVec extends vec2<Short> {
        public ShortVec() {
            super();
        }

        public ShortVec(double u, double v, boolean floor) {
            super(u, v, floor);
        }

        public ShortVec(vec2 other, boolean floor) {
            super(other, floor);
        }

        public ShortVec(vec2 other) {
            super(other, false);
        }

        public ShortVec(short u, short v) {
            super(u, v, false);
        }

        @Override
        public void update(double u, double v, boolean floor) {
            if(this.isChangeable) {
                if (floor) {
                    this.vec[0] = (short) Math.floor(u);
                    this.vec[1] = (short) Math.floor(v);
                } else {
                    this.vec[0] = (short) u;
                    this.vec[1] = (short) v;
                }
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setU(double u) {
            if(this.isChangeable) {
                this.vec[0] = (short) u;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setV(double v) {
            if(this.isChangeable) {
                this.vec[1] = (short) v;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }
        
        public short getU() {
            return (short) this.vec[0];
        }

        public short getV() {
            return (short) this.vec[1];
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
            if (content.length == 2) {
                for (int k = 0; k < 2; k++)
                    if (floor)
                        this.vec[k] = (short) Math.floor(content[k]);
                    else
                        this.vec[k] = (short) content[k];
            } else {
                throw new RuntimeException("Can't create vec2 from content with not 2 components.");
            }
        }
    }

    public static class ByteVec extends vec2<Byte> {
        public ByteVec() {
            super();
        }

        public ByteVec(double u, double v, boolean floor) {
            super(u, v, floor);
        }

        public ByteVec(vec2 other, boolean floor) {
            super(other, floor);
        }

        public ByteVec(vec2 other) {
            super(other, false);
        }

        public ByteVec(byte u, byte v) {
            super(u, v, false);
        }
        
        @Override
        public void update(double u, double v, boolean floor) {
            if(this.isChangeable) {
                if (floor) {
                    this.vec[0] = (byte) Math.floor(u);
                    this.vec[1] = (byte) Math.floor(v);
                } else {
                    this.vec[0] = (byte) u;
                    this.vec[1] = (byte) v;
                }
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setU(double u) {
            if(this.isChangeable) {
                this.vec[0] = (byte) u;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setV(double v) {
            if(this.isChangeable) {
                this.vec[1] = (byte) v;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        public byte getU() {
            return (byte) this.vec[0];
        }

        public byte getV() {
            return (byte) this.vec[1];
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
            if (content.length == 2) {
                for (int k = 0; k < 2; k++) {
                    if (floor)
                        this.vec[k] = (byte) Math.floor(content[k]);
                    else
                        this.vec[k] = (byte) content[k];
                }
            } else {
                throw new RuntimeException("Can't create vec2 from content with not 2 components.");
            }
        }
    }
}