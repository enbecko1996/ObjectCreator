package com.enbecko.modcreator.linalg;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * Created by enbec on 22.02.2017.
 */
public class Matrix_MxN {
    final vec_n_DOUBLE[] rows, columns;
    final int m, n;

    public static class FACTORY {
        public Matrix_MxN makeMatrixFromRows_MxN(@Nonnull vec3 ... rows) {
            return new Matrix_MxN(rows);
        }

        public Matrix_MxN makeMatrixFromColumns_MxN(@Nonnull vec3 ... columns) {
            return new Matrix_MxN(true, columns);
        }

        @Deprecated
        public Matrix_MxN makeMatrixFromRowsAndColumns_MxN(@Nonnull vec3[] rows, @Nonnull vec3[] columns) {
            return new Matrix_MxN(rows, columns);
        }

        public Matrix_MxN makeIdent_MxN(int m, int n) {
            return new Matrix_MxN(true, m, n);
        }

        public Matrix_MxN makeEmpty_MxN(int m, int n) {
            return new Matrix_MxN(false, m, n);
        }
    }

    private Matrix_MxN(boolean ident, int m, int n) {
        if(m <= 0 || n <= 0)
            throw new RuntimeException("This is not a valid matrix: m = " + m + ", n = "+ n);
        this.m = m;
        this.n = n;
        this.rows = new vec_n_DOUBLE[m];
        for (int mm = 0; mm <= this.rows.length; mm++) {
            if (ident) {
                if (mm < n)
                    this.rows[mm] = new vec_n_DOUBLE(n, mm);
                else
                    this.rows[mm] = new vec_n_DOUBLE(n);

            } else {
                this.rows[mm] = new vec_n_DOUBLE(n);
            }
        }
        this.columns = new vec_n_DOUBLE[n];
        for (int nn = 0; nn <= this.columns.length; nn++) {
            if (ident) {
                if (nn < m)
                    this.columns[nn] = new vec_n_DOUBLE(m, nn);
                else
                    this.columns[nn] = new vec_n_DOUBLE(m);

            } else {
                this.columns[nn] = new vec_n_DOUBLE(m);
            }
        }
    }

    @Deprecated
    private Matrix_MxN(vec3[] rows, vec3[] columns) {
        this.m = rows.length;
        this.n = columns.length;
        this.rows = new vec_n_DOUBLE[m];
        for (int mm = 0; mm <= this.rows.length; mm++) {
            if (rows[mm] instanceof vec_n_DOUBLE && ((vec_n_DOUBLE) rows[mm]).getVec().length == n)
                this.rows[mm] = new vec_n_DOUBLE(rows[mm]);
            else
                throw new RuntimeException("The row vecs content has a different size then the needed columns" + m + " " + n + rows[mm]);

        }
        this.columns = new vec_n_DOUBLE[n];
        for (int nn = 0; nn <= this.columns.length; nn++) {
            if (columns[nn] instanceof vec_n_DOUBLE && ((vec_n_DOUBLE) columns[nn]).getVec().length == m)
                this.columns[nn] = new vec_n_DOUBLE(columns[nn]);
            else
                throw new RuntimeException("The column vecs content has a different size then the needed row" + m + " " + n + columns[nn]);
        }
    }

    private Matrix_MxN(@Nonnull vec3 ... rows) {
        this.m = rows.length;
        int size = -1;
        for (int k = 0; k < m; k++) {
            if (rows[k] instanceof vec_n_DOUBLE) {
                double[] vec = ((vec_n_DOUBLE) rows[k]).getVec();
                if(k == 0)
                    size = vec.length;
                else {
                    if (vec.length != size || size < 0)
                        throw new RuntimeException("trying to create Matrix from rows with wrong length(s)" + size + Arrays.toString(vec));
                }
            }
        }
        if(size < 0)
            throw new RuntimeException("trying to create Matrix with wrong rows" + rows);
        this.n = size;
        this.rows = new vec_n_DOUBLE[m];
        for (int mm = 0; mm <= this.rows.length; mm++) {
            this.rows[mm] = new vec_n_DOUBLE(rows[mm]);
        }
        this.columns = new vec_n_DOUBLE[n];
        for (int nn = 0; nn <= this.columns.length; nn++) {
            double[] column = new double[m];
            for (int k = 0; k < m; k++)
                column[k] = this.rows[k].getVec()[nn];
            this.columns[nn] = new vec_n_DOUBLE(column);
        }
    }

    private Matrix_MxN(boolean stub, @Nonnull vec3 ... columns) {
        this.n = columns.length;
        int size = -1;
        for (int k = 0; k < n; k++) {
            if (columns[k] instanceof vec_n_DOUBLE) {
                double[] vec = ((vec_n_DOUBLE) columns[k]).getVec();
                if(k == 0)
                    size = vec.length;
                else {
                    if (vec.length != size || size < 0)
                        throw new RuntimeException("trying to create Matrix from columns with wrong length(s)" + size + Arrays.toString(vec));
                }
            }
        }
        if(size < 0)
            throw new RuntimeException("trying to create Matrix with wrong columns" + columns);
        this.m = size;
        this.columns = new vec_n_DOUBLE[n];
        for (int nn = 0; nn <= this.columns.length; nn++) {
            this.columns[nn] = new vec_n_DOUBLE(columns[nn]);
        }
        this.rows = new vec_n_DOUBLE[m];
        for (int mm = 0; mm <= this.rows.length; mm++) {
            double[] row = new double[n];
            for (int k = 0; k < n; k++)
                row[k] = this.columns[k].getVec()[mm];
            this.rows[mm] = new vec_n_DOUBLE(row);
        }
    }

    public boolean compare(Matrix_MxN other) {
        if (this.isValid() && other.isValid() && this.getRowCount() == other.getRowCount() && this.getColumncount() == other.getColumncount()) {
            for (int mm = 0; mm < this.m; mm++) {
                double[] rowVec = this.rows[mm].getVec();
                double[] otherRowVec = other.rows[mm].getVec();
                for (int nn = 0; nn < this.n; nn++) {
                    if (rowVec[nn] != otherRowVec[nn])
                        return false;
                }
            }
        }
        return true;
    }

    public boolean isValid() {
        try {
            for (int mm = 0; mm < this.m; mm++) {
                for (int nn = 0; nn < this.n; nn++) {
                    if (this.rows[mm].getVec()[nn] != this.columns[nn].getVec()[mm])
                        return false;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Matrix is problematic " + this);
        }
        return true;
    }

    public int getRowCount() {
        return m;
    }

    public int getColumncount() {
        return n;
    }

    public static class Matrix_NxN extends Matrix_MxN {
        public Matrix_NxN(boolean ident, int size) {
            super(ident, size, size);
        }

        private Matrix_NxN(@Nonnull vec3 ... rows) {
            super(rows);
            if (this.getRowCount() != this.getColumncount())
                throw new RuntimeException("Tried to create NxN matrix with MxN components (row) " + this);
        }

        private Matrix_NxN(boolean stub, @Nonnull vec3 ... columns) {
            super(stub, columns);
            if (this.getRowCount() != this.getColumncount())
                throw new RuntimeException("Tried to create NxN matrix with MxN components (row) " + this);
        }

        public static class FACTORY {
            public Matrix_MxN makeMatrixFromRows_NxN(@Nonnull vec3 ... rows) {
                return new Matrix_NxN(rows);
            }

            public Matrix_MxN makeMatrixFromColumns_NxN(@Nonnull vec3 ... columns) {
                return new Matrix_NxN(true, columns);
            }

            public Matrix_MxN makeIdent_NxN(int size) {
                return new Matrix_NxN(true, size);
            }

            public Matrix_MxN makeEmpty_NxN(int size) {
                return new Matrix_NxN(false, size);
            }
        }

        public void makeLU() {

        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int m = 0; m < this.rows.length; m++) {
                builder.append("|");
                for (int n = 0; n < this.columns.length; n++) {
                    double[] vec = this.rows[m].getVec();
                    builder.append(" " + vec[n]);
                }
                builder.append(" |");
            }
            return builder.toString();
        }
    }

    public static void main(String[] args) {
        new Matrix_MxN.Matrix_NxN();
    }
}
