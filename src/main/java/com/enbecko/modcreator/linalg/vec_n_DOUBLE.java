package com.enbecko.modcreator.linalg;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Created by enbec on 22.02.2017.
 */
public class vec_n_DOUBLE extends vec3{
    private final double[] vec;

    public vec_n_DOUBLE(int size) {
        this.vec = new double[size];
    }

    public vec_n_DOUBLE(boolean ident, int size, int identWhere) {
        this.vec = new double[size];
        if(identWhere < this.vec.length)
            this.vec[identWhere] = 1;
        else
            throw new RuntimeException("Can't create identity vector: vecLength = " + this.vec.length + ", makeOne = " + identWhere);
    }

    public vec_n_DOUBLE(double  ... content) {
        this.vec = new double[content.length];
        System.arraycopy(content, 0, this.vec, 0, content.length);
    }

    public vec_n_DOUBLE(vec3 other) {
        if((other instanceof vec_n_DOUBLE)) {
            double[] vec_n_double = ((vec_n_DOUBLE) other).getVec();
            this.vec = new double[vec_n_double.length];
            System.arraycopy(vec_n_double, 0, this.vec, 0, vec_n_double.length);
        } else {
            this.vec = new double[3];
            this.vec[0] = other.getXD();
            this.vec[1] = other.getYD();
            this.vec[2] = other.getZD();
        }
    }

    public vec_n_DOUBLE copy() {
        return new vec_n_DOUBLE(this);
    }

    public double[] getVec() {
        return this.vec;
    }

    @Override
    @Nullable
    public vec3Gen getGenericVec() {
        return null;
    }

    @Override
    @Nullable
    public Number getPreciseX() {
        if(this.vec.length > 0)
            return this.vec[0];
        return null;
    }

    @Override
    @Nullable
    public Number getPreciseY() {
        if(this.vec.length > 1)
            return this.vec[1];
        return null;
    }

    @Override
    @Nullable
    public Number getPreciseZ() {
        if(this.vec.length > 2)
            return this.vec[2];
        return null;
    }

    @Override
    public double getXD() {
        if(this.vec.length > 0)
            return this.vec[0];
        return 0;
    }

    @Override
    public void setXD(double x) {
        if(this.vec.length > 0)
            this.vec[0] = x;
    }

    @Override
    public double getYD() {
        if(this.vec.length > 1)
            return this.vec[1];
        return 0;
    }

    @Override
    public void setYD(double y) {
        if(this.vec.length > 1)
            this.vec[1] = y;
    }

    @Override
    public double getZD() {
        if(this.vec.length > 2)
            return this.vec[2];
        return 0;
    }

    @Override
    public void setZD(double z) {
        if(this.vec.length > 2)
            this.vec[2] = z;
    }

    @Override
    public vec_n_DOUBLE mulToThis(double length) {
        if (!this.isChangeable) {
            throw new RuntimeException("you can't change unchangeable vecs" + this);
        } else {
            for (int k = 0; k < this.vec.length; k++)
                this.vec[k] *= length;
        }
        return this;
    }

    @Override
    public vec_n_DOUBLE divToThis(double length) {
        if (!this.isChangeable) {
            throw new RuntimeException("you can't change unchangeable vecs" + this);
        } else {
            for (int k = 0; k < this.vec.length; k++)
                this.vec[k] /= length;
        }
        return this;
    }

    @Deprecated
    @Override
    public vec3 mulToThis(vec3 other) {
        if (!this.isChangeable) {
            throw new RuntimeException("you can't change unchangeable vecs" + this);
        } else {
            double[] vec_n_double = null;
            if((other instanceof vec_n_DOUBLE))
                vec_n_double = ((vec_n_DOUBLE) other).getVec();
            if (vec_n_double == null && this.vec.length >= 3 || (vec_n_double != null && vec_n_double.length <= this.vec.length)) {
                this.setXD(this.getXD() * other.getXD());
                this.setYD(this.getYD() * other.getYD());
                this.setZD(this.getZD() * other.getZD());
            } else {
                throw new RuntimeException("Multiplying a higher dim. vector with a lower one is not possible!" + this + " " + other);
            }
            if(vec_n_double != null && vec_n_double.length > 3) {
                if (this.vec.length >= vec_n_double.length) {
                    for (int k = 3; k < vec_n_double.length; k++)
                        this.vec[k] *= vec_n_double[k];
                } else {
                    throw new RuntimeException("Multiplying a higher dim. vector with a lower one is not possible!" + this + " " + other);
                }
            }
        }
        return this;
    }

    @Deprecated
    @Override
    public vec3 divToThis(vec3 other) {
        if (!this.isChangeable) {
            throw new RuntimeException("you can't change unchangeable vecs" + this);
        } else {
            double[] vec_n_double = null;
            if((other instanceof vec_n_DOUBLE))
                vec_n_double = ((vec_n_DOUBLE) other).getVec();
            if (vec_n_double == null && this.vec.length >= 3 || (vec_n_double != null && vec_n_double.length <= this.vec.length)) {
                this.setXD(this.getXD() / other.getXD());
                this.setYD(this.getYD() / other.getYD());
                this.setZD(this.getZD() / other.getZD());
            } else {
                throw new RuntimeException("Dividing a higher dim. vector from a lower one is not possible!" + this + " " + other);
            }
            if(vec_n_double != null && vec_n_double.length > 3) {
                if (this.vec.length >= vec_n_double.length) {
                    for (int k = 3; k < vec_n_double.length; k++)
                        this.vec[k] /= vec_n_double[k];
                } else {
                    throw new RuntimeException("Dividing a higher dim. vector from a lower one is not possible!" + this + " " + other);
                }
            }
        }
        return this;    }

    @Override
    public vec_n_DOUBLE subFromThis(vec3 other) {
        if (!this.isChangeable) {
            throw new RuntimeException("you can't change unchangeable vecs" + this);
        } else {
            double[] vec_n_double = null;
            if((other instanceof vec_n_DOUBLE))
                    vec_n_double = ((vec_n_DOUBLE) other).getVec();
            if (vec_n_double == null && this.vec.length >= 3 || (vec_n_double != null && vec_n_double.length <= this.vec.length)) {
                this.setXD(this.getXD() - other.getXD());
                this.setYD(this.getYD() - other.getYD());
                this.setZD(this.getZD() - other.getZD());
            } else {
                throw new RuntimeException("Subtracting a higher dim. vector from a lower one is not possible!" + this + " " + other);
            }
            if(vec_n_double != null && vec_n_double.length > 3) {
                if (this.vec.length >= vec_n_double.length) {
                    for (int k = 3; k < vec_n_double.length; k++)
                        this.vec[k] -= vec_n_double[k];
                } else {
                    throw new RuntimeException("Subtracting a higher dim. vector from a lower one is not possible!" + this + " " + other);
                }
            }
        }
        return this;
    }

    @Override
    public vec_n_DOUBLE addToThis(vec3 other) {
        if (!this.isChangeable) {
            throw new RuntimeException("you can't change unchangeable vecs" + this);
        } else {
            double[] vec_n_double = null;
            if((other instanceof vec_n_DOUBLE))
                vec_n_double = ((vec_n_DOUBLE) other).getVec();
            if (vec_n_double == null && this.vec.length >= 3 || (vec_n_double != null && vec_n_double.length <= this.vec.length)) {
                this.setXD(this.getXD() + other.getXD());
                this.setYD(this.getYD() + other.getYD());
                this.setZD(this.getZD() + other.getZD());
            } else {
                throw new RuntimeException("Adding a higher dim. vector from a lower one is not possible!" + this + " " + other);
            }
            if(vec_n_double != null && vec_n_double.length > 3) {
                if (this.vec.length >= vec_n_double.length) {
                    for (int k = 3; k < vec_n_double.length; k++)
                        this.vec[k] += vec_n_double[k];
                } else {
                    throw new RuntimeException("Adding a higher dim. vector from a lower one is not possible!" + this + " " + other);
                }
            }
        }
        return this;
    }

    @Nullable
    public vec_n_DOUBLE mulAndMakeNew(vecPrec precision, double length) {
        vec_n_DOUBLE calc = new vec_n_DOUBLE(this).mulToThis(length);
        return calc;
    }

    @Nullable
    public vec3 addAndMakeNew(vecPrec precision, vec3 other) {
        vec_n_DOUBLE calc = new vec_n_DOUBLE(this).addToThis(other);
        return calc;
    }

    @Nullable
    public vec3 subAndMakeNew(vecPrec precision, vec3 other) {
        vec_n_DOUBLE calc = new vec_n_DOUBLE(this).subFromThis(other);
        return calc;
    }

    @Nullable
    public vec3 divAndMakeNew(vecPrec precision, double length) {
        vec_n_DOUBLE calc = new vec_n_DOUBLE(this).divToThis(length);
        return calc;
    }

    @Override
    public vec_n_DOUBLE update(double x, double y, double z) {
        this.setXD(x);
        this.setYD(y);
        this.setZD(z);
        return this;
    }

    public vec_n_DOUBLE update(int off, double ... cont) {
        if(cont.length + off <= this.vec.length)
            System.arraycopy(cont, 0, this.vec, off, cont.length);
        return this;
    }

    @Override
    public vecPrec getPrecision() {
        return vecPrec.DOUBLE;
    }

    @Override
    public double dot(vec3 other) {
        if (other instanceof vec_n_DOUBLE) {
            double[] vec = ((vec_n_DOUBLE) other).getVec();
            if (this.vec.length == vec.length) {
                double out = 0;
                for (int k = 0; k < vec.length; k++) {
                    out += this.vec[k] * vec[k];
                }
                return out;
            } else
                throw new RuntimeException("cannot do dot on different sized vectors" + this + " " + other);
        } else if (this.vec.length == 3) {
            return super.dot(other);
        } else
            throw new RuntimeException("cannot do dot on different sized vectors" + this + " " + other);
    }

    public String toString() {
        return Arrays.toString(this.vec);
    }

    public int getLength() {
        return this.vec.length;
    }
}
