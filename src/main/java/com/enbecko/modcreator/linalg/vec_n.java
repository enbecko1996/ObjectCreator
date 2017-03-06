package com.enbecko.modcreator.linalg;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Created by enbec on 23.02.2017.
 */
public abstract class vec_n<T extends Number> {
    T[] genVec;
    final int size;
    boolean isChangeable = true;
    final double[] vec;

    public vec_n(int size) {
        this.size = size;
        this.vec = new double[size];
    }

    public vec_n(boolean floor, double... content) {
        this.size = content.length;
        this.vec = new double[size];
        this.setVec(floor, content);
    }

    public vec_n(vec_n other, boolean floor) {
        this.size = other.getSize();
        this.vec = new double[size];
        this.setVec(floor, other.getVecD());
    }

    public vec_n(boolean ident, int size, int identWhere) {
        this.size = size;
        double[] vec = new double[size];
        if(identWhere < vec.length)
            vec[identWhere] = 1;
        else
            throw new RuntimeException("Can't create identity vector: vecLength = " + vec.length + ", makeOne = " + identWhere);
        this.vec = new double[size];
        this.setVec(false, vec);
    }

    public vec_n invert() {
        this.mulToThis(-1);
        return this;
    }

    public vec_n mulToThis(double length) {
        if (!this.isChangeable) {
            throw new RuntimeException("you can't change unchangeable vecs" + this);
        } else {
            double[] vec = this.getVecD();
            for (int k = 0; k < vec.length; k++)
                vec[k] *= length;
            this.applyPrecision(false);
        }
        return this;
    }

    public vec_n<T> setChangeable(boolean changeable) {
        this.isChangeable = changeable;
        return this;
    }

    public vec_n divToThis(double length) {
        if (!this.isChangeable) {
            throw new RuntimeException("you can't change unchangeable vecs" + this);
        } else {
            double[] vec = this.getVecD();
            for (int k = 0; k < vec.length; k++)
                vec[k] /= length;
            this.applyPrecision(false);
        }
        return this;
    }

    public vec_n subFromThis(vec_n other) {
        if (!this.isChangeable) {
            throw new RuntimeException("you can't change unchangeable vecs" + this);
        } else {
            double[] otherVec = other.getVecD();
            double[] vec = this.getVecD();
            if (otherVec.length <= vec.length) {
                for (int k = 0; k < other.getSize(); k++)
                    vec[k] -= otherVec[k];
            } else {
                throw new RuntimeException("Subtracting a higher dim. vector from a lower one is not possible!" + this + " " + other);
            }
        }
        return this;
    }

    public vec_n addToThis(@Nonnull vec_n other) {
        if (!this.isChangeable) {
            throw new RuntimeException("you can't change unchangeable vecs" + this);
        } else {
            double[] otherVec = other.getVecD();
            double[] vec = this.getVecD();
            if (otherVec.length <= vec.length) {
                for (int k = 0; k < other.getSize(); k++)
                    vec[k] += otherVec[k];
            } else {
                throw new RuntimeException("Adding a higher dim. vector to a lower one is not possible!" + this + " " + other);
            }
        }
        return this;
    }

    public vec_n addToThis(double ... cont) {
        if (!this.isChangeable) {
            throw new RuntimeException("you can't change unchangeable vecs" + this);
        } else {
            if (cont.length <= vec.length) {
                for (int k = 0; k < cont.length; k++)
                    vec[k] += cont[k];
            } else {
                throw new RuntimeException("Adding a higher dim. vector to a lower one is not possible!" + this + " " + Arrays.toString(cont));
            }
        }
        return this;
    }

    public vec_n addToThis(double add) {
        if (!this.isChangeable) {
            throw new RuntimeException("you can't change unchangeable vecs" + this);
        } else {
            for (int k = 0; k < vec.length; k++)
                vec[k] += add;
        }
        return this;
    }

    @Nullable
    public abstract vec_n mulAndMakeNew(vecPrec precision, double length, boolean floor);

    @Nullable
    public abstract vec_n addAndMakeNew(vecPrec precision, vec_n other, boolean floor);

    @Nullable
    public abstract vec_n subAndMakeNew(vecPrec precision, vec_n other, boolean floor);

    @Nullable
    public abstract vec_n divAndMakeNew(vecPrec precision, double length, boolean floor);

    public vec_n update(vec_n other, boolean floor) {
        return this.update(0, floor, other.getVecD());
    }

    public vec_n update(vec_n other) {
        return this.update(0, false, other.getVecD());
    }


    public vec_n update(double... cont) {
        return this.update(0, false, cont);
    }

    public vec_n update(int off, boolean floor, double... cont) {
        if (cont.length + off <= this.vec.length)
            System.arraycopy(cont, 0, this.vec, off, cont.length);
        this.applyPrecision(floor);
        return this;
    }

    public abstract vecPrec getPrecision();

    public T[] getGenericVec() {
        return this.genVec;
    }

    public double dot(vec_n other) {
        double[] otherVec = other.getVecD();
        double[] vec = this.getVecD();
        if (otherVec.length == vec.length) {
            double out = 0;
            for (int k = 0; k < vec.length; k++) {
                out += vec[k] * otherVec[k];
            }
            return out;
        } else
            throw new RuntimeException("cannot do dot on different sized vectors" + this + " " + other);
    }

    public String toString() {
        return Arrays.toString(this.vec);
    }

    public vec_n normalize() {
        this.mulToThis(1d / this.length());
        return this;
    }

    public double[] getVecD() {
        return this.vec;
    }

    public double getAtD(int pos) {
        if (pos < this.getSize()) {
            return this.vec[pos];
        }
        throw new RuntimeException("Can't get value at " + pos + " in vector " + this);
    }

    public int getSize() {
        return this.size;
    }

    public double length() {
        double sum = 0;
        for (int k = 0; k < this.getSize(); k++)
            sum += Math.pow(this.vec[k], 2);
        return Math.sqrt(sum);
    }

    public abstract void applyPrecision(boolean floor);

    public abstract void setVec(boolean floor, double... content);

    public enum vecPrec {
        BYTE, SHORT, INT, LONG, FLOAT, DOUBLE;
    }
}
