package com.enbecko.modcreator.linalg;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Created by enbec on 22.02.2017.
 */
public class Matrix_MxN {
    final vec_n_DOUBLE[] rows, columns;
    final int m, n;
    private static MxN_FACTORY the_MxN_MxN_FACTORY;

    private Matrix_MxN(boolean ident, int m, int n) {
        if (m <= 0 || n <= 0)
            throw new RuntimeException("This is not a valid matrix: m = " + m + ", n = " + n);
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

    private Matrix_MxN(boolean stub, boolean stub2, @Nonnull vec3... rows) {
        this.m = rows.length;
        int size = -1;
        for (int k = 0; k < m; k++) {
            if (rows[k] instanceof vec_n_DOUBLE) {
                double[] vec = ((vec_n_DOUBLE) rows[k]).getVec();
                if (k == 0)
                    size = vec.length;
                else {
                    if (vec.length != size || size < 0)
                        throw new RuntimeException("trying to create Matrix from rows with wrong length(s)" + size + Arrays.toString(vec));
                }
            }
        }
        if (size < 0)
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

    private Matrix_MxN(boolean stub, @Nonnull vec3... columns) {
        this.n = columns.length;
        int size = -1;
        for (int k = 0; k < n; k++) {
            if (columns[k] instanceof vec_n_DOUBLE) {
                double[] vec = ((vec_n_DOUBLE) columns[k]).getVec();
                if (k == 0)
                    size = vec.length;
                else {
                    if (vec.length != size || size < 0)
                        throw new RuntimeException("trying to create Matrix from columns with wrong length(s)" + size + Arrays.toString(vec));
                }
            }
        }
        if (size < 0)
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

    @Nullable
    public vec_n_DOUBLE getRowAt(int pos) {
        if(pos < this.getRowCount())
            return this.rows[pos];
        return null;
    }

    @Nullable
    public vec_n_DOUBLE getColumnAt(int pos) {
        if(pos < this.getColumncount())
            return this.columns[pos];
        return null;
    }

    public boolean setRow(int pos, vec_n_DOUBLE row) {
        try {
            if (pos < this.getRowCount() && row.getLength() == this.getColumncount()) {
                this.rows[pos].update(row);
                double[] vec = row.getVec();
                for (int nn = 0; nn < this.getColumncount(); nn++) {
                    this.columns[nn].getVec()[pos] = vec[nn];
                }
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't set row " + row + " at " + pos + " in matrix " + this);
        }
        return false;
    }

    public boolean setColumn(int pos, vec_n_DOUBLE column) {
        try {
            if (pos < this.getColumncount() && column.getLength() == this.getRowCount()) {
                this.columns[pos].update(column);
                double[] vec = column.getVec();
                for (int mm = 0; mm < this.getRowCount(); mm++) {
                    this.rows[mm].getVec()[pos] = vec[mm];
                }
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't insert column " + column + " at " + pos + " in matrix " + this);
        }
        return false;
    }

    public boolean set(int row, int column, double value) {
        try {
            if (row < this.getRowCount() && column < this.getColumncount()) {
                this.rows[row].getVec()[column] = value;
                this.columns[column].getVec()[row] = value;
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't set value at " + row + "," + column + " in matrix " + this);
        }
        return false;
    }

    public Matrix_MxN multiplyToThisAndMakeNewMatrix(Matrix_MxN rhs) {
        if (this.getRowCount() == rhs.getColumncount()) {
            Matrix_MxN out = MxN_FACTORY.makeEmpty_MxN(this.getRowCount(), rhs.getColumncount());
            for (int mm = 0; mm < out.getRowCount(); mm++) {
                for (int nn = 0; nn < out.getColumncount(); nn++) {
                    out.set(mm, nn, this.getRowAt(mm).dot(rhs.getColumnAt(nn)));
                }
            }
            return out;
        } else {
            throw new RuntimeException("Can't multiply those matrices: lhs = " + this.getRowCount() + "x" + this.getColumncount() + " rhs = " + rhs.getRowCount() + "x" + rhs.getColumncount());
        }
    }

    public static class MxN_FACTORY {
        public static Matrix_MxN makeMatrixFromRows_MxN(@Nonnull vec3... rows) {
            return new Matrix_MxN(true, true, rows);
        }

        public static Matrix_MxN makeMatrixFromColumns_MxN(@Nonnull vec3... columns) {
            return new Matrix_MxN(true, columns);
        }

        @Deprecated
        public static Matrix_MxN makeMatrixFromRowsAndColumns_MxN(@Nonnull vec3[] rows, @Nonnull vec3[] columns) {
            return new Matrix_MxN(rows, columns);
        }

        public static Matrix_MxN makeIdent_MxN(int m, int n) {
            return new Matrix_MxN(true, m, n);
        }

        public static Matrix_MxN makeEmpty_MxN(int m, int n) {
            return new Matrix_MxN(false, m, n);
        }
    }


    public static class Matrix_NxN extends Matrix_MxN {

        public Matrix_NxN(boolean ident, int size) {
            super(ident, size, size);
        }

        private Matrix_NxN(boolean stub, boolean stub2, @Nonnull vec3... rows) {
            super(stub, stub2, rows);
            if (this.getRowCount() != this.getColumncount())
                throw new RuntimeException("Tried to create NxN matrix with MxN components (row) " + this);
        }

        private Matrix_NxN(boolean stub, @Nonnull vec3... columns) {
            super(stub, columns);
            if (this.getRowCount() != this.getColumncount())
                throw new RuntimeException("Tried to create NxN matrix with MxN components (row) " + this);
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

        public static class NxN_FACTORY {
            public static Matrix_NxN makeMatrixFromRows_NxN(@Nonnull vec3... rows) {
                return new Matrix_NxN(true, true, rows);
            }

            public static Matrix_NxN makeMatrixFromColumns_NxN(@Nonnull vec3... columns) {
                return new Matrix_NxN(true, columns);
            }

            public static Matrix_NxN makeIdent_NxN(int size) {
                return new Matrix_NxN(true, size);
            }

            public static Matrix_NxN makeEmpty_NxN(int size) {
                return new Matrix_NxN(false, size);
            }
        }
    }


    public static void main(String[] args) {
        Matrix_NxN test = Matrix_NxN.NxN_FACTORY.makeIdent_NxN(4);
        System.out.println(test);
    }
}
