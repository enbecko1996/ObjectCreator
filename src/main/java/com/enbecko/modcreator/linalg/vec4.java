package com.enbecko.modcreator.linalg;

import javax.annotation.Nullable;

/**
 * Created by Niclas on 15.11.2016.
 */
public abstract class vec4<T extends Number> extends vec_n<T> {
    public vec4() {
        super(4);
    }

    public vec4(double r, double g, double b, double a, boolean floor) {
        super(floor, r, g, b);
    }

    public vec4(vec4 other, boolean floor) {
        super(other, floor);
    }

    @Override
    public int getSize() {
        return 4;
    }

    public double getRD() {
        return this.vec[0];
    }

    public double getGD() {
        return this.vec[1];
    }

    public double getBD() {
        return this.vec[2];
    }

    public double getAD() {
        return this.vec[3];
    }

    public float getRF() {
        return (float) this.vec[0];
    }

    public float getGF() {
        return (float) this.vec[1];
    }

    public float getBF() {
        return (float) this.vec[2];
    }

    public float getAF() {
        return (float) this.vec[3];
    }

    public void update(double r, double g, double b, double a) {
        this.update(r, g, b, a, false);
    }
    public abstract void update(double r, double g, double b, double a, boolean floor);
    public abstract void setR(double r);
    public abstract void setG(double g);
    public abstract void setB(double b);
    public abstract void setA(double a);

    public static vec4 newVecWithPrecision(vecPrec prec, vec4 old, boolean floor) {
        switch (prec) {
            case BYTE:
                return new vec4.ByteVec(old, floor);
            case SHORT:
                return new vec4.ShortVec(old, floor);
            case INT:
                return new vec4.IntVec(old, floor);
            case LONG:
                return new vec4.LongVec(old, floor);
            case FLOAT:
                return new vec4.FloatVec(old, floor);
            default:
                return new vec4.DoubleVec(old, floor);
        }
    }

    @Override
    @Nullable
    public vec_n mulAndMakeNew(vecPrec prec, double length, boolean floor) {
        switch (prec) {
            case BYTE:
                return new vec4.ByteVec(this, floor).mulToThis(length);
            case SHORT:
                return new vec4.ShortVec(this, floor).mulToThis(length);
            case INT:
                return new vec4.IntVec(this, floor).mulToThis(length);
            case LONG:
                return new vec4.LongVec(this, floor).mulToThis(length);
            case FLOAT:
                return new vec4.FloatVec(this, floor).mulToThis(length);
            default:
                return new vec4.DoubleVec(this, floor).mulToThis(length);
        }
    }

    @Override
    @Nullable
    public vec_n addAndMakeNew(vecPrec prec, vec_n other, boolean floor) {
        switch (prec) {
            case BYTE:
                return new vec4.ByteVec(this, floor).addToThis(other);
            case SHORT:
                return new vec4.ShortVec(this, floor).addToThis(other);
            case INT:
                return new vec4.IntVec(this, floor).addToThis(other);
            case LONG:
                return new vec4.LongVec(this, floor).addToThis(other);
            case FLOAT:
                return new vec4.FloatVec(this, floor).addToThis(other);
            default:
                return new vec4.DoubleVec(this, floor).addToThis(other);
        }
    }

    //@Override
    @Nullable
    public vec_n addAndMakeNew(vecPrec prec, boolean floor, double ... cont) {
        switch (prec) {
            case BYTE:
                return new vec4.ByteVec(this, floor).addToThis(cont);
            case SHORT:
                return new vec4.ShortVec(this, floor).addToThis(cont);
            case INT:
                return new vec4.IntVec(this, floor).addToThis(cont);
            case LONG:
                return new vec4.LongVec(this, floor).addToThis(cont);
            case FLOAT:
                return new vec4.FloatVec(this, floor).addToThis(cont);
            default:
                return new vec4.DoubleVec(this, floor).addToThis(cont);
        }
    }

    @Override
    @Nullable
    public vec_n subAndMakeNew(vecPrec prec, vec_n other, boolean floor) {
        switch (prec) {
            case BYTE:
                return new vec4.ByteVec(this, floor).subFromThis(other);
            case SHORT:
                return new vec4.ShortVec(this, floor).subFromThis(other);
            case INT:
                return new vec4.IntVec(this, floor).subFromThis(other);
            case LONG:
                return new vec4.LongVec(this, floor).subFromThis(other);
            case FLOAT:
                return new vec4.FloatVec(this, floor).subFromThis(other);
            default:
                return new vec4.DoubleVec(this, floor).subFromThis(other);
        }
    }

    @Override
    @Nullable
    public vec_n divAndMakeNew(vecPrec prec, double length, boolean floor) {
        switch (prec) {
            case BYTE:
                return new vec4.ByteVec(this, floor).divToThis(length);
            case SHORT:
                return new vec4.ShortVec(this, floor).divToThis(length);
            case INT:
                return new vec4.IntVec(this, floor).divToThis(length);
            case LONG:
                return new vec4.LongVec(this, floor).divToThis(length);
            case FLOAT:
                return new vec4.FloatVec(this, floor).divToThis(length);
            default:
                return new vec4.DoubleVec(this, floor).divToThis(length);
        }
    }

    @Override
    public vec4<T> setChangeable(boolean changeable) {
        this.isChangeable = changeable;
        return this;
    }

    public static class IntVec extends vec4<Integer> {
        public IntVec() {
            super();
        }

        public IntVec(double r, double g, double b, double a, boolean floor) {
            super(r, g, b, a, floor);
        }

        public IntVec(vec4 other, boolean floor) {
            super(other, floor);
        }

        public IntVec(vec4 other) {
            super(other, false);
        }

        public IntVec(int r, int g, int b, int a) {
            super(r, g, b, a, false);
        }

        @Override
        public void update(double r, double g, double b, double a, boolean floor) {
            if(this.isChangeable) {
                if (floor) {
                    this.vec[0] = (int) Math.floor(r);
                    this.vec[1] = (int) Math.floor(g);
                    this.vec[2] = (int) Math.floor(b);
                    this.vec[3] = (int) Math.floor(a);
                } else {
                    this.vec[0] = (int) r;
                    this.vec[1] = (int) g;
                    this.vec[2] = (int) b;
                    this.vec[3] = (int) a;
                }
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setR(double r) {
            if(this.isChangeable) {
                this.vec[0] = (int) r;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setG(double g) {
            if(this.isChangeable) {
                this.vec[1] = (int) g;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setB(double b) {
            if(this.isChangeable) {
                this.vec[2] = (int) b;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setA(double a) {
            if(this.isChangeable) {
                this.vec[3] = (int) a;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        public int getR() {
            return (int) this.vec[0];
        }

        public int getG() {
            return (int) this.vec[1];
        }

        public int getB() {
            return (int) this.vec[2];
        }

        public int getA() {
            return (int) this.vec[3];
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
                throw new RuntimeException("Can't create vec4 from content with not 3 components.");
            }
        }
    }

    public static class LongVec extends vec4<Long> {
        public LongVec() {
            super();
        }

        public LongVec(double r, double g, double b, double a, boolean floor) {
            super(r, g, b, a, floor);
        }

        public LongVec(vec4 other, boolean floor) {
            super(other, floor);
        }

        public LongVec(vec4 other) {
            super(other, false);
        }

        public LongVec(long r, long g, long b, long a) {
            super(r, g, b, a, false);
        }
        
        @Override
        public void update(double r, double g, double b, double a, boolean floor) {
            if(this.isChangeable) {
                if (floor) {
                    this.vec[0] = (long) Math.floor(r);
                    this.vec[1] = (long) Math.floor(g);
                    this.vec[2] = (long) Math.floor(b);
                    this.vec[3] = (long) Math.floor(a);
                } else {
                    this.vec[0] = (long) r;
                    this.vec[1] = (long) g;
                    this.vec[2] = (long) b;
                    this.vec[3] = (long) a;
                }
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setR(double r) {
            if(this.isChangeable) {
                this.vec[0] = (long) r;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setG(double g) {
            if(this.isChangeable) {
                this.vec[1] = (long) g;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setB(double b) {
            if(this.isChangeable) {
                this.vec[2] = (long) b;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setA(double a) {
            if(this.isChangeable) {
                this.vec[3] = (long) a;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }


        public long getR() {
            return (long) this.vec[0];
        }

        public long getG() {
            return (long) this.vec[1];
        }

        public long getB() {
            return (long) this.vec[2];
        }

        public long getA() {
            return (long) this.vec[3];
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
                throw new RuntimeException("Can't create vec4 from content with not 3 components.");
            }
        }
    }

    public static class DoubleVec extends vec4<Double> {
        public DoubleVec() {
            super();
        }

        public DoubleVec(double r, double g, double b, double a, boolean floor) {
            super(r, g, b, a, floor);
        }

        public DoubleVec(vec4 other) {
            super(other, false);
        }

        public DoubleVec(double r, double g, double b, double a) {
            super(r, g, b, a, false);
        }

        public DoubleVec(vec4 other, boolean floor) {
            super(other, floor);
        }
        
        @Override
        public void update(double r, double g, double b, double a, boolean floor) {
            if(this.isChangeable) {
                this.vec[0] = r;
                this.vec[1] = g;
                this.vec[2] = b;
                this.vec[3] = a;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setR(double r) {
            if(this.isChangeable) {
                this.vec[0] = r;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setG(double g) {
            if(this.isChangeable) {
                this.vec[1] = g;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setB(double b) {
            if(this.isChangeable) {
                this.vec[2] = b;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setA(double a) {
            if(this.isChangeable) {
                this.vec[3] = a;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        public double getR() {
            return this.vec[0];
        }

        public double getG() {
            return this.vec[1];
        }

        public double getB() {
            return this.vec[2];
        }

        public double getA() {
            return this.vec[3];
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
                throw new RuntimeException("Can't create vec4 from content with not 3 components.");
            }
        }
    }

    public static class FloatVec extends vec4<Float> {
        public FloatVec() {
            super();
        }

        public FloatVec(double r, double g, double b, double a, boolean floor) {
            super(r, g, b, a, floor);
        }

        public FloatVec(vec4 other, boolean floor) {
            super(other, floor);
        }

        public FloatVec(vec4 other) {
            super(other, false);
        }

        public FloatVec(float r, float g, float b, float a) {
            super(r, g, b, a, false);
        }

        @Override
        public void update(double r, double g, double b, double a, boolean floor) {
            if(this.isChangeable) {
                this.vec[0] = (float) r;
                this.vec[1] = (float) g;
                this.vec[2] = (float) b;
                this.vec[3] = (float) a;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setR(double r) {
            if(this.isChangeable) {
                this.vec[0] = (float) r;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setG(double g) {
            if(this.isChangeable) {
                this.vec[1] = (float) g;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setB(double b) {
            if(this.isChangeable) {
                this.vec[2] = (float) b;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setA(double a) {
            if(this.isChangeable) {
                this.vec[3] = (float) a;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        public float getR() {
            return (float) this.vec[0];
        }

        public float getG() {
            return (float) this.vec[1];
        }

        public float getB() {
            return (float) this.vec[2];
        }

        public float getA() {
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
                throw new RuntimeException("Can't create vec4 from content with not 3 components.");
            }
        }
    }

    public static class ShortVec extends vec4<Short> {
        public ShortVec() {
            super();
        }

        public ShortVec(double r, double g, double b, double a, boolean floor) {
            super(r, g, b, a, floor);
        }

        public ShortVec(vec4 other, boolean floor) {
            super(other, floor);
        }

        public ShortVec(vec4 other) {
            super(other, false);
        }

        public ShortVec(short r, short g, short b, short a) {
            super(r, g, b, a, false);
        }
        
        @Override
        public void update(double r, double g, double b, double a, boolean floor) {
            if(this.isChangeable) {
                if (floor) {
                    this.vec[0] = (short) Math.floor(r);
                    this.vec[1] = (short) Math.floor(g);
                    this.vec[2] = (short) Math.floor(b);
                    this.vec[3] = (short) Math.floor(a);
                } else {
                    this.vec[0] = (short) r;
                    this.vec[1] = (short) g;
                    this.vec[2] = (short) b;
                    this.vec[3] = (short) a;
                }
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setR(double r) {
            if(this.isChangeable) {
                this.vec[0] = (short) r;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setG(double g) {
            if(this.isChangeable) {
                this.vec[1] = (short) g;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setB(double b) {
            if(this.isChangeable) {
                this.vec[2] = (short) b;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setA(double a) {
            if(this.isChangeable) {
                this.vec[3] = (short) a;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        public short getR() {
            return (short) this.vec[0];
        }

        public short getG() {
            return (short) this.vec[1];
        }

        public short getB() {
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
                throw new RuntimeException("Can't create vec4 from content with not 3 components.");
            }
        }
    }

    public static class ByteVec extends vec4<Byte> {
        public ByteVec() {
            super();
        }

        public ByteVec(double r, double g, double b, double a, boolean floor) {
            super(r, g, b, a, floor);
        }

        public ByteVec(vec4 other, boolean floor) {
            super(other, floor);
        }

        public ByteVec(vec4 other) {
            super(other, false);
        }

        public ByteVec(byte r, byte g, byte b, byte a) {
            super(r, g, b, a, false);
        }

        @Override
        public void update(double r, double g, double b, double a, boolean floor) {
            if(this.isChangeable) {
                if (floor) {
                    this.vec[0] = (byte) Math.floor(r);
                    this.vec[1] = (byte) Math.floor(g);
                    this.vec[2] = (byte) Math.floor(b);
                    this.vec[3] = (byte) Math.floor(a);
                } else {
                    this.vec[0] = (byte) r;
                    this.vec[1] = (byte) g;
                    this.vec[2] = (byte) b;
                    this.vec[3] = (byte) a;
                }
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setR(double r) {
            if(this.isChangeable) {
                this.vec[0] = (byte) r;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setG(double g) {
            if(this.isChangeable) {
                this.vec[1] = (byte) g;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setB(double b) {
            if(this.isChangeable) {
                this.vec[2] = (byte) b;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        @Override
        public void setA(double a) {
            if(this.isChangeable) {
                this.vec[3] = (byte) a;
            } else {
                throw new RuntimeException("Can't change unchangeable vecs " + this);
            }
        }

        public byte getR() {
            return (byte) this.vec[0];
        }

        public byte getG() {
            return (byte) this.vec[1];
        }

        public byte getB() {
            return (byte) this.vec[2];
        }

        public byte getA() {
            return (byte) this.vec[3];
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
                throw new RuntimeException("Can't create vec4 from content with not 3 components.");
            }
        }
    }
}