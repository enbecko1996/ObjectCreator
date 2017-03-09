package com.enbecko.modcreator.linalg;

import com.enbecko.modcreator.Log;
import com.enbecko.modcreator.Log.LogEnums;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Created by enbec on 22.02.2017.
 */
public class Matrix {
    public static void main(String[] args) {
        vec_n_DOUBLE[] rows = {new vec_n_DOUBLE(1, 2, 3), new vec_n_DOUBLE(4, 5, 6), new vec_n_DOUBLE(7, 8, 8)};
        Matrix_NxN test = NxN_FACTORY.makeMatrixFromRows(rows);
        Log.d(LogEnums.MATH, test);
        test.doLUDecomposition();
        long startLU = System.currentTimeMillis();
        int testCount = 1000000;
        System.out.print("0/" + testCount);
        vec_n_DOUBLE rhs = new vec_n_DOUBLE(1, 2, 3);
        for (int k = 0; k < testCount; k++) {
            //System.out.print("\r"+k+"/"+testCount);
            test.doLUDecomposition();
            //test.solveLGS_fromLU(rhs);
            test.getInverse_fromLU();
        }
        Log.d(LogEnums.MATH, "\nLU time: " + (System.currentTimeMillis() - startLU) + " " + test.solveLGS_fromLU(rhs));
        long startGauss = System.currentTimeMillis();
        System.out.print("0/" + testCount);
        for (int k = 0; k < testCount; k++) {
            //System.out.print("\r"+k+"/"+testCount);
            //test.solveLGS_Gaussian(rhs);
            test.getInverse_Gaussian();
        }
        Log.d(LogEnums.MATH, "\nGauss time: " + (System.currentTimeMillis() - startLU) + " " + test.solveLGS_Gaussian(rhs));
    }

    public static class MxN_FACTORY {
        public static Matrix_MxN makeMatrixFromRows(@Nonnull vec_n... rows) {
            return new Matrix_MxN(true, true, rows);
        }

        public static Matrix_MxN makeMatrixFromMatrix(@Nonnull Matrix_MxN matrix) {
            if (matrix.isValid())
                return new Matrix_MxN(true, true, matrix.getRows());
            throw new RuntimeException("This matrix is not valid: " + matrix);
        }

        public static Matrix_MxN makeMatrixFromColumns(@Nonnull vec_n... columns) {
            return new Matrix_MxN(true, columns);
        }

        @Deprecated
        public static Matrix_MxN makeMatrixFromRowsAndColumns(@Nonnull vec_n[] rows, @Nonnull vec_n[] columns) {
            return new Matrix_MxN(rows, columns);
        }

        public static Matrix_MxN makeIdent(int m, int n) {
            return new Matrix_MxN(true, m, n);
        }

        public static Matrix_MxN makeEmpty(int m, int n) {
            return new Matrix_MxN(false, m, n);
        }
    }

    public static class NxN_FACTORY {
        public static Matrix_NxN makeMatrixFromRows(@Nonnull vec_n... rows) {
            return new Matrix_NxN(true, true, rows);
        }

        public static Matrix_NxN makeMatrixFromColumns(@Nonnull vec_n... columns) {
            return new Matrix_NxN(true, columns);
        }

        public static Matrix_NxN makeIdent(int size) {
            return new Matrix_NxN(true, size);
        }

        public static Matrix_NxN makeEmpty(int size) {
            return new Matrix_NxN(false, size);
        }

        public static Matrix_NxN makeMatrixFromMatrix(@Nonnull Matrix_NxN matrix) {
            if (matrix.isValid())
                return new Matrix_NxN(true, true, matrix.getRows());
            throw new RuntimeException("This matrix is not valid: " + matrix);
        }
    }

    public static class Matrix_MxN {
        final vec_n_DOUBLE[] rows, columns;
        final int m, n;

        private Matrix_MxN(boolean ident, int m, int n) {
            if (m <= 0 || n <= 0)
                throw new RuntimeException("This is not a valid matrix: m = " + m + ", n = " + n);
            this.m = m;
            this.n = n;
            this.rows = new vec_n_DOUBLE[m];
            for (int mm = 0; mm < this.rows.length; mm++) {
                if (ident) {
                    if (mm < n)
                        this.rows[mm] = new vec_n_DOUBLE(true, n, mm);
                    else
                        this.rows[mm] = new vec_n_DOUBLE(n);

                } else {
                    this.rows[mm] = new vec_n_DOUBLE(n);
                }
            }
            this.columns = new vec_n_DOUBLE[n];
            for (int nn = 0; nn < this.columns.length; nn++) {
                if (ident) {
                    if (nn < m)
                        this.columns[nn] = new vec_n_DOUBLE(true, m, nn);
                    else
                        this.columns[nn] = new vec_n_DOUBLE(m);

                } else {
                    this.columns[nn] = new vec_n_DOUBLE(m);
                }
            }
        }

        @Deprecated
        private Matrix_MxN(vec_n[] rows, vec_n[] columns) {
            this.m = rows.length;
            this.n = columns.length;
            this.rows = new vec_n_DOUBLE[m];
            for (int mm = 0; mm < this.rows.length; mm++) {
                if (rows[mm] instanceof vec_n_DOUBLE && ((vec_n_DOUBLE) rows[mm]).getVecD().length == n)
                    this.rows[mm] = new vec_n_DOUBLE(rows[mm]);
                else
                    throw new RuntimeException("The row vecs content has a different size then the needed columns" + m + " " + n + rows[mm]);

            }
            this.columns = new vec_n_DOUBLE[n];
            for (int nn = 0; nn < this.columns.length; nn++) {
                if (columns[nn] instanceof vec_n_DOUBLE && ((vec_n_DOUBLE) columns[nn]).getVecD().length == m)
                    this.columns[nn] = new vec_n_DOUBLE(columns[nn]);
                else
                    throw new RuntimeException("The column vecs content has a different size then the needed row" + m + " " + n + columns[nn]);
            }
        }

        private Matrix_MxN(boolean stub, boolean stub2, @Nonnull vec_n... rows) {
            this.m = rows.length;
            int size = -1;
            for (int k = 0; k < m; k++) {
                double[] vec = rows[k].getVecD();
                if (k == 0)
                    size = vec.length;
                else {
                    if (vec.length != size || size < 0)
                        throw new RuntimeException("trying to create Matrix from rows with wrong length(s)" + size + Arrays.toString(vec));
                }
            }
            if (size < 0)
                throw new RuntimeException("trying to create Matrix with wrong rows" + Arrays.toString(rows));
            this.n = size;
            this.rows = new vec_n_DOUBLE[m];
            for (int mm = 0; mm < this.rows.length; mm++) {
                this.rows[mm] = new vec_n_DOUBLE(rows[mm]);
            }
            this.columns = new vec_n_DOUBLE[n];
            for (int nn = 0; nn < this.columns.length; nn++) {
                double[] column = new double[m];
                for (int k = 0; k < m; k++)
                    column[k] = this.rows[k].getVecD()[nn];
                this.columns[nn] = new vec_n_DOUBLE(column);
            }
        }

        private Matrix_MxN(boolean stub, @Nonnull vec_n... columns) {
            this.n = columns.length;
            int size = -1;
            for (int k = 0; k < n; k++) {
                double[] vec = columns[k].getVecD();
                if (k == 0)
                    size = vec.length;
                else {
                    if (vec.length != size || size < 0)
                        throw new RuntimeException("trying to create Matrix from columns with wrong length(s)" + size + Arrays.toString(vec));
                }

            }
            if (size < 0)
                throw new RuntimeException("trying to create Matrix with wrong columns" + Arrays.toString(columns));
            this.m = size;
            this.columns = new vec_n_DOUBLE[n];
            for (int nn = 0; nn < this.columns.length; nn++) {
                this.columns[nn] = new vec_n_DOUBLE(columns[nn]);
            }
            this.rows = new vec_n_DOUBLE[m];
            for (int mm = 0; mm < this.rows.length; mm++) {
                double[] row = new double[n];
                for (int k = 0; k < n; k++)
                    row[k] = this.columns[k].getVecD()[mm];
                this.rows[mm] = new vec_n_DOUBLE(row);
            }
        }

        public boolean compare(Matrix_MxN other) {
            if (this.isValid() && other.isValid() && this.getRowCount() == other.getRowCount() && this.getColumncount() == other.getColumncount()) {
                for (int mm = 0; mm < this.m; mm++) {
                    double[] rowVec = this.rows[mm].getVecD();
                    double[] otherRowVec = other.rows[mm].getVecD();
                    for (int nn = 0; nn < this.n; nn++) {
                        if (rowVec[nn] != otherRowVec[nn])
                            return false;
                    }
                }
            } else {
                return false;
            }
            return true;
        }

        public boolean isValid() {
            try {
                for (int mm = 0; mm < this.m; mm++) {
                    for (int nn = 0; nn < this.n; nn++) {
                        if (this.rows[mm].getVecD()[nn] != this.columns[nn].getVecD()[mm])
                            return false;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Matrix is problematic " + this);
            }
            return true;
        }

        public boolean isSameSize(Matrix_MxN other) {
            return this.getRowCount() == other.getRowCount() && this.getColumncount() == other.getColumncount();
        }

        public int getRowCount() {
            return m;
        }

        public int getColumncount() {
            return n;
        }

        @Nullable
        public vec_n_DOUBLE getRowAt(int pos) {
            if (pos < this.getRowCount())
                return this.rows[pos];
            return null;
        }

        @Nullable
        public vec_n_DOUBLE getColumnAt(int pos) {
            if (pos < this.getColumncount())
                return this.columns[pos];
            return null;
        }

        public vec_n_DOUBLE[] getRows() {
            return this.rows;
        }

        public vec_n_DOUBLE[] getColumns() {
            return this.columns;
        }

        public Matrix_MxN update(Matrix_MxN other) {
            if (this.isSameSize(other)) {
                for (int mm = 0; mm < this.getRowCount(); mm++)
                    this.setRow(mm, other.getRowAt(mm));
            }
            return this;
        }

        public void updateRows(int off, vec_n_DOUBLE ... rows) {
            if (off + rows.length <= this.getRowCount()) {
                for (int k = off; k < rows.length; k++) {
                    this.setRow(k, rows[k - off]);
                }
            } else
                throw new RuntimeException("Can't update " + rows.length + " rows from " + off + ". this.rowCount: " + this.getRowCount());
        }

        public void updateColumns(int off, vec_n_DOUBLE ... columns) {
            if (off + columns.length <= this.getColumncount()) {
                for (int k = off; k < columns.length; k++) {
                    this.setColumn(k, columns[k - off]);
                }
            } else
                throw new RuntimeException("Can't update " + columns.length + " columns from " + off + ". this.columnCount: " + this.getColumncount());
        }

        public void setRow(int pos, vec_n row) {
            try {
                if (pos < this.getRowCount() && row.getSize() == this.getColumncount()) {
                    this.rows[pos].update(row, false);
                    double[] vec = row.getVecD();
                    for (int nn = 0; nn < this.getColumncount(); nn++) {
                        this.columns[nn].getVecD()[pos] = vec[nn];
                    }
                } else
                    throw new RuntimeException("Can't set row " + row + " at column " + pos + " in matrix " + this);
            } catch (Exception e) {
                throw new RuntimeException("Can't set row " + row + " at column " + pos + " in matrix " + this);
            }
        }

        public void setColumn(int pos, vec_n column) {
            try {
                if (pos < this.getColumncount() && column.getSize() == this.getRowCount()) {
                    this.columns[pos].update(column, false);
                    double[] vec = column.getVecD();
                    for (int mm = 0; mm < this.getRowCount(); mm++) {
                        this.rows[mm].getVecD()[pos] = vec[mm];
                    }
                } else
                    throw new RuntimeException("Can't set column " + column + " at row " + pos + " in matrix " + this);

            } catch (Exception e) {
                throw new RuntimeException("Can't set column " + column + " at row " + pos + " in matrix " + this);
            }
        }

        public void swapRows(int row1, int row2) {
            vec_n_DOUBLE tmpRow = this.getRowAt(row1).copy();
            this.setRow(row1, this.getRowAt(row2));
            this.setRow(row2, tmpRow);
        }

        public void swapColumns(int col1, int col2) {
            vec_n_DOUBLE tmpCol = this.getColumnAt(col1).copy();
            this.setRow(col1, this.getRowAt(col2));
            this.setRow(col2, tmpCol);
        }

        public void set(int row, int column, double value) {
            try {
                if (row < this.getRowCount() && column < this.getColumncount()) {
                    this.rows[row].getVecD()[column] = value;
                    this.columns[column].getVecD()[row] = value;
                } else
                    throw new RuntimeException("Can't set value at " + row + "," + column + " in matrix " + this);
            } catch (Exception e) {
                throw new RuntimeException("Can't set value at " + row + "," + column + " in matrix " + this);
            }
        }

        public double get(int row, int column) {
            try {
                if (row < this.getRowCount() && column < this.getColumncount()) {
                    return this.rows[row].getVecD()[column];
                } else
                    throw new RuntimeException("No value at " + row + "," + column + " in matrix " + this);
            } catch (Exception e) {
                throw new RuntimeException("No value at " + row + "," + column + " in matrix " + this);
            }
        }

        public Matrix_MxN multiplyToThisAndMakeNewMatrix(Matrix_MxN rhs) {
            if (this.getColumncount() == rhs.getRowCount()) {
                Matrix_MxN out = MxN_FACTORY.makeEmpty(this.getRowCount(), rhs.getColumncount());
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

        public vec_n_DOUBLE multiplyWithVector(vec_n rhs) {
            if (this.getRowCount() == rhs.getSize()) {
                vec_n_DOUBLE out = new vec_n_DOUBLE(rhs.getSize());
                double[] vec = out.getVecD();
                for (int k = 0; k < vec.length; k++)
                    vec[k] = this.getRowAt(k).dot(rhs);
                return out;
            } else {
                throw new RuntimeException("Can't multiply this matrix with vector: lhs = " + this.getRowCount() + "x" + this.getColumncount() + " rhs = " + rhs.getSize());
            }
        }

        public vec_n multiplyWithVector(vec_n toFill, double ... comp) {
            if (this.getRowCount() == comp.length && toFill.getSize() == comp.length) {
                for (int k = 0; k < comp.length; k++) {
                    toFill.set(k, this.getRowAt(k).dot(comp));
                }
                return toFill;
            } else {
                throw new RuntimeException("Can't multiply this matrix with vector: lhs = " + this.getRowCount() + "x" + this.getColumncount() + " rhs = " + Arrays.toString(comp));
            }
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            int[] maxLength = new int[this.columns.length];
            for (int m = 0; m < this.rows.length; m++) {
                for (int n = 0; n < this.columns.length; n++) {
                    double[] vec = this.rows[m].getVecD();
                    if ((vec[n] + "").length() > maxLength[n])
                        maxLength[n] = (vec[n] + "").length();
                }
            }
            for (int m = 0; m < this.rows.length; m++) {
                builder.append("\n| ");
                for (int n = 0; n < this.columns.length; n++) {
                    double[] vec = this.rows[m].getVecD();
                    int len = (vec[n] + "").length(), diff = maxLength[n] - len + 1;
                    builder.append("" + vec[n]);
                    for (int k = 0; k < diff; k++)
                        builder.append(' ');
                }
                builder.append("|");
            }
            builder.append("\n");
            return builder.toString();
        }
    }

    public static class Matrix_NxN extends Matrix_MxN {
        Matrix_NxN LU_rhs = null;
        Matrix_NxN LU_lhs = null;
        Matrix_NxN LU_permutation = null;
        Matrix_NxN ident_Stub = null;

        private Matrix_NxN(boolean ident, int size) {
            super(ident, size, size);
        }

        private Matrix_NxN(boolean stub, boolean stub2, @Nonnull vec_n... rows) {
            super(stub, stub2, rows);
            if (this.getRowCount() != this.getColumncount())
                throw new RuntimeException("Tried to create NxN matrix with MxN components (row) " + this);
        }

        private Matrix_NxN(boolean stub, @Nonnull vec_n... columns) {
            super(stub, columns);
            if (this.getRowCount() != this.getColumncount())
                throw new RuntimeException("Tried to create NxN matrix with MxN components (row) " + this);
        }

        public Matrix_NxN translate(double ... comp) {
            if (comp.length <= this.m) {
                this.setColumn(this.n - 1, this.getColumnAt(this.n - 1).addToThis(comp));
                return this;
            } else
                throw new RuntimeException("Translate component is too big " + this +" "+comp);
        }

        @Deprecated
        public Matrix_NxN getInverse_Gaussian() {
            vec_n_DOUBLE[] tmpColumns = new vec_n_DOUBLE[2 * this.getSize()];
            Matrix_NxN ident = NxN_FACTORY.makeIdent(this.getSize());
            System.arraycopy(this.getColumns(), 0, tmpColumns, 0, this.getSize());
            System.arraycopy(ident.getColumns(), 0, tmpColumns, this.getSize(), this.getSize());
            Matrix_MxN gaussMat = MxN_FACTORY.makeMatrixFromColumns(tmpColumns);
            gaussMat = this.doGaussian(gaussMat);
            vec_n_DOUBLE[] outColumns = new vec_n_DOUBLE[this.getSize()];
            for (int k = 0; k < this.getSize(); k++) {
                outColumns[k] = gaussMat.getColumnAt(this.getSize() + k);
            }
            return NxN_FACTORY.makeMatrixFromColumns(outColumns);
        }

        @Deprecated
        public vec_n_DOUBLE solveLGS_Gaussian(vec_n rhs) {
            vec_n[] tmpColumns = new vec_n[this.getSize() + 1];
            System.arraycopy(this.getColumns(), 0, tmpColumns, 0, this.getSize());
            tmpColumns[tmpColumns.length - 1] = rhs;
            Matrix_MxN gaussMat = MxN_FACTORY.makeMatrixFromColumns(tmpColumns);
            gaussMat = this.doGaussian(gaussMat);
            return gaussMat.getColumnAt(this.getSize());
        }

        @Deprecated
        public Matrix_MxN doGaussian(Matrix_MxN gaussMat) {
            int size = this.getRowCount();
            for (int k = 0; k < size; k++) {
                vec_n_DOUBLE tmpCol = gaussMat.getColumnAt(k);
                double[] col = tmpCol.getVecD();
                //PIVOTING
                boolean foundPivot = true;
                if (col[k] == 0) {
                    foundPivot = false;
                    double biggest = 0;
                    int swapRow = k;
                    for (int l = 0; k + l < size; l++) {
                        //take first NonNull Pivot
                        if (col[k + l] != 0) {
                            swapRow = k + l;
                            foundPivot = true;
                        }
                    }
                    if (foundPivot) {
                        gaussMat.swapRows(k, swapRow);
                    }
                }
                //Eliminating
                vec_n_DOUBLE topRow = gaussMat.getRowAt(k);
                double[] topRowVec = topRow.getVecD();
                if (topRowVec[k] != 1)
                    gaussMat.setRow(k, (vec_n_DOUBLE) topRow.mulToThis(1d / topRowVec[k]));
                vec_n_DOUBLE topRowCopy = gaussMat.getRowAt(k).copy();
                for (int p = k + 1; p < size; p++) {
                    vec_n_DOUBLE row = gaussMat.getRowAt(p);
                    double fac = topRowCopy.getVecD()[k] != 0 ? -(row.getVecD()[k] / topRowCopy.getVecD()[k]) : 0;
                    gaussMat.setRow(p, (vec_n_DOUBLE) row.addToThis(topRowCopy.mulToThis(fac)));
                    topRowCopy.mulToThis(fac != 0 && !Double.isInfinite(fac) && !Double.isNaN(fac) ? 1 / fac : 1);
                }
            }
            for (int k = 0; k < size; k++) {
                vec_n_DOUBLE botRowCopy = gaussMat.getRowAt(size - 1 - k).copy();
                for (int p = k + 1; p < size; p++) {
                    vec_n_DOUBLE row = gaussMat.getRowAt(size - 1 - p);
                    double fac = botRowCopy.getVecD()[size - 1 - k] != 0 ? -(row.getVecD()[size - 1 - k] / botRowCopy.getVecD()[size - 1 - k]) : 0;
                    gaussMat.setRow(size - 1 - p, (vec_n_DOUBLE) row.addToThis(botRowCopy.mulToThis(fac)));
                    botRowCopy.mulToThis(fac != 0 && !Double.isInfinite(fac) && !Double.isNaN(fac) ? 1 / fac : 1);
                }
            }
            return gaussMat;
        }

        /**
         * MOTIVATION:
         * usually the Bone will not nor change position while working, so a LU decomposed Matrix to solve the
         * equation Ax = b = LUx is faster then doing the Gauss elimination every time.
         */
        public void doLUDecomposition() {
            if (LU_lhs == null) {
                LU_rhs = NxN_FACTORY.makeMatrixFromMatrix(this);
                LU_lhs = NxN_FACTORY.makeIdent(this.getSize());
                LU_permutation = NxN_FACTORY.makeIdent(this.getSize());
                ident_Stub = NxN_FACTORY.makeIdent(this.getSize());
            } else {
                this.LU_rhs.update(this);
                this.LU_lhs.update(ident_Stub);
                this.LU_permutation.update(ident_Stub);
            }
            int size = this.getRowCount();
            for (int k = 0; k < size; k++) {
                vec_n_DOUBLE tmpCol = LU_rhs.getColumnAt(k);
                double[] col = tmpCol.getVecD();
                //PIVOTING
                if (col[k] == 0) {
                    boolean foundPivot = false;
                    double biggest = 0;
                    int swapRow = k;
                    for (int l = 0; k + l < size; l++) {
                        //take first NonNull Pivot
                        if (col[k + l] != 0) {
                            swapRow = k + l;
                            foundPivot = true;
                            break;
                        }
                        //take biggest Pivot
                        /**
                         if (col[k + l] != 0 && Math.abs(col[k + l]) > biggest) {
                         biggest = Math.abs(col[k + l]);
                         swapRow = k + l;
                         foundPivot = true;
                         }
                         */
                    }
                    if (foundPivot) {
                        LU_rhs.swapRows(k, swapRow);
                        LU_permutation.swapRows(k, swapRow);
                    }
                }
                //Eliminating
                vec_n_DOUBLE topRowCopy = LU_rhs.getRowAt(k).copy();
                for (int p = k + 1; p < size; p++) {
                    vec_n_DOUBLE row = LU_rhs.getRowAt(p);
                    if (row.getVecD()[k] != 0) {
                        double fac = topRowCopy.getVecD()[k] != 0 ? -(row.getVecD()[k] / topRowCopy.getVecD()[k]) : 0;
                        LU_rhs.setRow(p, row.addToThis(topRowCopy.mulToThis(fac)));
                        topRowCopy.mulToThis(fac != 0 && !Double.isInfinite(fac) && !Double.isNaN(fac) ? 1 / fac : 1);
                        LU_lhs.set(p, k, -fac);
                    }
                }
            }
            //DEBUG
            //Log.d(Log.LogEnums.MATH, "lhs: "+this.LU_lhs+" rhs: "+this.LU_rhs+" permut: "+this.LU_permutation);
        }

        public Matrix_NxN invert() {
            this.doLUDecomposition();
            vec_n_DOUBLE[] outColumns = new vec_n_DOUBLE[this.getSize()];
            for (int k = 0; k < outColumns.length; k++)
                outColumns[k] = this.solveLGS_fromLU(new vec_n_DOUBLE(true, this.getSize(), k));
            this.updateColumns(0, outColumns);
            return this;
        }

        public Matrix_NxN getInverse_fromLU() {
            this.doLUDecomposition();
            vec_n_DOUBLE[] outColumns = new vec_n_DOUBLE[this.getSize()];
            for (int k = 0; k < outColumns.length; k++)
                outColumns[k] = this.solveLGS_fromLU(new vec_n_DOUBLE(true, this.getSize(), k));
            return NxN_FACTORY.makeMatrixFromColumns(outColumns);
        }

        public vec_n_DOUBLE solveLGS_fromLU(vec_n rhs) {
            if (this.LU_permutation != null && this.LU_lhs != null && this.LU_rhs != null) {
                //solve Ly = Pb
                vec_n_DOUBLE y = new vec_n_DOUBLE(rhs.getSize());
                vec_n_DOUBLE Pb = this.LU_permutation.multiplyWithVector(rhs);
                double[] yVec = y.getVecD();
                double[] PbVec = Pb.getVecD();
                for (int k = 0; k < yVec.length; k++) {
                    double sumOfLower = 0;
                    for (int i = 0; i <= k - 1; i++)
                        sumOfLower += yVec[i] * this.LU_lhs.get(k, i);
                    yVec[k] = (PbVec[k] - sumOfLower) / this.LU_lhs.get(k, k);
                }
                //solving Rx = y
                vec_n_DOUBLE x = new vec_n_DOUBLE(rhs.getSize());
                double[] xVec = x.getVecD();
                for (int k = 0; k < xVec.length; k++) {
                    double sumOfLower = 0;
                    for (int i = 0; i <= k - 1; i++)
                        sumOfLower += xVec[xVec.length - 1 - i] * this.LU_rhs.get(xVec.length - 1 - k, xVec.length - 1 - i);
                    xVec[xVec.length - 1 - k] = (yVec[xVec.length - 1 - k] - sumOfLower) / this.LU_rhs.get(xVec.length - 1 - k, xVec.length - 1 - k);
                }
                return x;
            } else {
                throw new RuntimeException("Can't solve LGS when LU decomp. wasn't done " + this);
            }
        }

        public int getSize() {
            if (this.getRowCount() == this.getColumncount())
                return this.getRowCount();
            else
                throw new RuntimeException("Unfortunately this NxN-Matrix seems to have become MxN");
        }
    }
}
