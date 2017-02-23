package com.enbecko.modcreator.linalg;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Created by enbec on 22.02.2017.
 */
public class vec_n_DOUBLE extends vec_n<Double>{
    public vec_n_DOUBLE(int size) {
        super(size);
    }

    public vec_n_DOUBLE(boolean ident, int size, int identWhere) {
        super(ident, size, identWhere);
    }

    public vec_n_DOUBLE(double  ... content) {
        super(content);
    }

    public vec_n_DOUBLE(vec_n other) {
        super(other);
    }

    public vec_n_DOUBLE copy() {
        return new vec_n_DOUBLE(this);
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

    public String toString() {
        return Arrays.toString(this.vec);
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public void applyPrecision() {

    }

    @Override
    public void setVec(double... content) {
        if (content.length == this.size) {
            for (int k = 0; k < this.size; k++)
                this.vec[k] = content[k];
        } else {
            throw new RuntimeException("Can't create vec3 from content with not 3 components.");
        }
    }

    @Override
    @Nullable
    public vec_n mulAndMakeNew(vecPrec precision, double length) {
        vec_n calc = new vec_n_DOUBLE(this).mulToThis(length);
        return calc;
    }

    @Override
    @Nullable
    public vec_n addAndMakeNew(vecPrec precision, vec_n other) {
        vec_n calc = new vec_n_DOUBLE(this).addToThis(other);
        return calc;
    }

    @Override
    @Nullable
    public vec_n subAndMakeNew(vecPrec precision, vec_n other) {
        vec_n calc = new vec_n_DOUBLE(this).subFromThis(other);
        return calc;
    }

    @Override
    @Nullable
    public vec_n divAndMakeNew(vecPrec precision, double length) {
        vec_n calc = new vec_n_DOUBLE(this).divToThis(length);
        return calc;
    }
}
