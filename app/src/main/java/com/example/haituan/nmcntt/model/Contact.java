package com.example.haituan.nmcntt.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HaiTuan on 27/03/2018.
 */

// không cần để ý file này chỉ là thư viện
public class Contact implements Parcelable {
    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {

        @Override
        public Contact createFromParcel(Parcel parcel) {
            return new Contact(parcel);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
    private String mName;
    private int    mRow;
    private int    mCol;
    private double data[][];

    public Contact(String mName, int mRow, int mCol, double[][] data) {
        this.mName = mName;
        this.mRow = mRow;
        this.mCol = mCol;
        this.data = data;
    }

    public Contact (Parcel parcel) {
        mName = parcel.readString();
        mRow = parcel.readInt();
        mCol = parcel.readInt();
        data = new double[mRow][mCol];
        for (int i = 0; i < mRow; i++)
            for (int j = 0; j < mCol; j++) data[i][j] = parcel.readDouble();
    }

    public static boolean isOK(double[] a) {
        int n = a.length;
        double min = 1000000000;
        for (int i = 0; i < n; i++)
            if (a[i] != 0 && min > Math.abs(a[i])) min = Math.abs(a[i]);

        if (min == 1000000000)
            return false;
        for (int i = 0; i < n; i++) {
            double x = a[i] / min;
            int y = (int) x;
            if (x != y) return false;
        }
        return true;
    }

    public static boolean isDonVi(double a[][]) {
        if (a.length != a[0].length)
            return false;
        for (int i = 0; i < a.length; i++)
            for (int j =0; j < a[i].length; j++)
                if ((i == j && a[i][j] != 1) || (i != j && a[i][j] != 0)) return false;
        return true;
    }

    public static boolean cmp(double a[][], double b[][]) {
        if (a.length != b.length || a[0].length != b[0].length) return false;

        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < b.length; j++)
                if (a[i][j] != b[i][j]) return false;
        return true;
    }

    public static double[][] add(double a[][], double b[][]) {
        if (a.length != b.length || a[0].length != b[0].length) return null;

        int m = a.length;
        int n = a[0].length;
        double ans[][] = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) ans[i][j] = a[i][j] + b[i][j];
        }
        return ans;
    }

    public static double[][] sub(double a[][], double b[][]) {
        if (a.length != b.length || a[0].length != b[0].length) return null;

        int m = a.length;
        int n = a[0].length;
        double ans[][] = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) ans[i][j] = a[i][j] - b[i][j];
        }
        return ans;
    }

    public static double[][] mul(double a[][], double b[][]) {
        int m = a.length;
        int n = a[0]. length;
        int p = b.length;
        int q = b[0].length;
        if (n != p) return null;

        double ans[][] = new double[m][q];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < q; j++) {
                ans[i][j] = 0;
                for (int z = 0; z < n; z++) ans[i][j] += a[i][z] * b[z][j];
            }
        return ans;
    }

    public static double[][] bacThang(double a[][]) {
        int m = a.length;
        int n = a[0].length;

        double[][] ans = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) ans[i][j] = a[i][j];
        int i = 0, j = 0;

        while (i < m && j < n) {
            int tm = -1;
            for (int k = m - 1; k >= i; k--) {
                if (ans[k][j] != 0 && (tm == -1 || isOK(ans[k]))) tm = k;
            }
            if (tm == -1) {
                j++;
                continue;
            }

            if (tm != i) {
                for (int z = 0; z < n; z++) {
                    double tmp = ans[i][z]; ans[i][z] = ans[tm][z]; ans[tm][z] = tmp;
                }
            }

            for (int k = i + 1; k < m; k++) {
                double di = ans[i][j];
                double dk = ans[k][j];
                for (int z = 0; z < n; z++) ans[k][z] = ans[k][z] - dk * ans[i][z] / di;
            }

            i++; j++;
        }
        return ans;
    }

    public static double[][] bacThangRG(double a[][]) {
        int m = a.length;
        int n = a[0].length;

        double[][] ans = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) ans[i][j] = a[i][j];
        int i = 0, j = 0;

        while (i < m && j < n) {
            int tm = -1;
            for (int k = m - 1; k >= i; k--) {
                if (ans[k][j] != 0 && (tm == -1 || isOK(ans[k]))) tm = k;
            }
            if (tm == -1) {
                j++;
                continue;
            }

            if (tm != i) {
                for (int z = 0; z < n; z++) {
                    double tmp = ans[i][z]; ans[i][z] = ans[tm][z]; ans[tm][z] = tmp;
                }
            }

            double di = ans[i][j];
            for (int z = 0; z < n; z++) ans[i][z] = ans[i][z] / di;

            for (int k = 0; k < m; k++) {
                if (k == i) continue;
                double dk = ans[k][j];
                for (int z = 0; z < n; z++) ans[k][z] = ans[k][z] - dk * ans[i][z];
            }

            i++; j++;
        }
        return ans;
    }

    public static int rank(double a[][]) {
        int ans = 0;
        double res[][] = Contact.bacThang(a);
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a[i].length; j++)
                if (res[i][j] != 0) {
                    ans++;
                    break;
                }
        return ans;
    }

    public static double[][] nghichDao(double a[][]) {
        if (a.length != a[0].length) return null;
        int n = a.length;
        double res[][] = new double[n][n * 2];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) res[i][j] = a[i][j];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) res[i][j + n] = (i == j) ? 1 :0;
        res = Contact.bacThangRG(res);
        if (res[n - 1][n - 1] == 0) return null;

        double ans[][] = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) ans[i][j] = res[i][j + n];
        return ans;
    }

    public static double det(double a[][]) {
        int n = a.length;
        if (a.length == 1) {
            return a[0][0];
        }

        double ans = 0;
        double b[][] = new double [n - 1][n - 1];
        for (int i = 0; i < n; i++) {
            for (int x = 1; x < n; x++) {
                for (int y = 0; y < n; y++) {
                    if (y < i) b[x - 1][y] = a[x][y];
                    else if (y > i) b[x - 1][y - 1] = a[x][y];
                }
            }

            if (i % 2 == 0) ans += a[0][i] * det(b);
            else ans -= a[0][i] * det(b);
        }

        return ans;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(mName);
        dest.writeInt(mRow);
        dest.writeInt(mCol);
        for (int i = 0; i < mRow; i++)
            for (int j = 0; j < mCol; j++) dest.writeDouble(data[i][j]);
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmRow() {
        return mRow;
    }

    public void setmRow(int mRow) {
        this.mRow = mRow;
    }

    public int getmCol() {
        return mCol;
    }

    public void setmCol(int mCol) {
        this.mCol = mCol;
    }

    public double[][] getData() {
        return data;
    }

    public void setData(double[][] data) {
        this.data = data;
    }
}
