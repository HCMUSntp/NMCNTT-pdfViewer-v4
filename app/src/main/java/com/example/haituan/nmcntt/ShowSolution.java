package com.example.haituan.nmcntt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.haituan.nmcntt.model.Contact;

// show đáp án chi tiết
public class ShowSolution extends AppCompatActivity {
    private Contact contact;
    private LinearLayout linearLayoutRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_solution);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            contact = intent.getParcelableExtra("a");
        }

        linearLayoutRoot = findViewById(R.id.show_linear_layout);
        linearLayoutRoot.setGravity(Gravity.CENTER);
        linearLayoutRoot.setBackgroundColor(getResources().getColor(R.color.backgroundinput));
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //hàm tính toán để in đáp án chi tiết
    //chỉ là viết lại cái fuction tìm Bậc Thang và thêm vào những chổ cần in ra bảng.
    void init() {
        int m = contact.getmRow();
        int n = contact.getmCol();

        double[][] ans = contact.getData();

        int i = 0, j = 0;

        while (i < m && j < n) {
            int tm = -1;
            for (int k = m - 1; k >= i; k--) {
                if (ans[k][j] != 0 && (tm == -1 || Contact.isOK(ans[k]))) tm = k;
            }
            if (tm == -1) {
                j++;
                continue;
            }

            if (tm != i) {
                for (int z = 0; z < n; z++) {
                    double tmp = ans[i][z]; ans[i][z] = ans[tm][z]; ans[tm][z] = tmp;
                }
                printMatrix(ans);
            }

            for (int k = i + 1; k < m; k++) {
                double di = ans[i][j];
                double dk = ans[k][j];
                for (int z = 0; z < n; z++) ans[k][z] = ans[k][z] - dk * ans[i][z] / di;
            }

            printMatrix(ans);
            i++; j++;
        }
    }

    // hàm in ra một cái matrix
    void printMatrix(double[][] a) {
        int m = a.length;
        int n = a[0].length;

        for (int i = 0; i < m; i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            for (int j = 0; j < n; j++) {
                TextView textView = new TextView(this);
                textView.setWidth(dpToPx(60, this));
                textView.setHeight(dpToPx(60, this));
                textView.setTextSize(dpToPx(20, this));
                textView.setGravity(Gravity.CENTER);
                textView.setText(Double.toString(Math.round(a[i][j] * 100) / 100));
                linearLayout.addView(textView);
            }

            linearLayoutRoot.addView(linearLayout);
        }

        TextView textView = new TextView(this);
        textView.setHeight(dpToPx(20, this));
        linearLayoutRoot.addView(textView);
    }

    int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
