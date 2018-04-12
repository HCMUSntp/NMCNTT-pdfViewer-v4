package com.example.haituan.nmcntt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OutputActivity extends AppCompatActivity {
    private String mName;
    private int mRow;
    private int mCol;
    private double data[];

    //giao diện cái này tương tự với ReadInput
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);

        //tạo cái nút quay về
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();
        if (intent != null) {
            mName = intent.getStringExtra("TenBien");
            mRow = intent.getIntExtra("SoDong", 0);
            mCol = intent.getIntExtra("SoCot", 0);
            data = intent.getDoubleArrayExtra("data");

            //Toast.makeText(this, "Show value: " + mName + " " + mCol + " " + mRow, Toast.LENGTH_SHORT).show();
        }

        init(mRow, mCol);
    }

    // khởi tạo phần đồ họa
    // Mọi thứ tương tự phần của ReadInput
    public void init(int n, int m) {
        LinearLayout linearLayoutRoot = new LinearLayout(this);
        linearLayoutRoot.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayoutRoot.setOrientation(LinearLayout.VERTICAL);
        linearLayoutRoot.setGravity(Gravity.CENTER);
        linearLayoutRoot.setBackgroundColor(getResources().getColor(R.color.backgroundinput));


        for (int i = 0; i < n; i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            for (int j = 0; j < m; j++) {
                TextView textView = new EditText(this);
                textView.setWidth(50);
                textView.setHeight(100);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(10, 10, 10, 10);
                textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                textView.setText(String.valueOf(Math.round(data[i * mCol + j] * 100) / 100));
                textView.setId(i * m + j);
                linearLayout.addView(textView);
            }

            linearLayoutRoot.addView(linearLayout);
            setContentView(linearLayoutRoot);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.output, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.OK:
                Intent intent = new Intent();
                intent.putExtra("mName", mName);
                intent.putExtra("mRow", mRow);
                intent.putExtra("mCol", mCol);
                intent.putExtra("data",data);
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
