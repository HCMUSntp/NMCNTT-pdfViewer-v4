package com.example.haituan.nmcntt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class ReadInputActivity extends AppCompatActivity {
    private String mName;
    private int mRow;
    private int mCol;
    private double data[];
    private LinearLayout linearLayoutRoot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_input);

        //2 dòng này chỉ là thêm cái nút quay về
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            mName = intent.getStringExtra("TenBien");
            mRow = intent.getIntExtra("SoDong", 0);
            mCol = intent.getIntExtra("SoCot", 0);

            Toast.makeText(this, "Show value: " + mName + " " + mCol + " " + mRow, Toast.LENGTH_SHORT).show();
        }
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Muốn chỉnh sửa các biểu tượng OK để lưu biến trở về thì vào cái này menu.read_input
        getMenuInflater().inflate(R.menu.read_input, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //hàm chuyển đơn vị thôi
    int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    // hàm khởi tạo giao diện
    public void init() {
        //khởi tạo 1 cái linearLayout ngoài cùng để chứa mọi thứ trong Activity
        linearLayoutRoot = new LinearLayout(this);
        linearLayoutRoot.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayoutRoot.setOrientation(LinearLayout.VERTICAL);
        linearLayoutRoot.setGravity(Gravity.CENTER);
        linearLayoutRoot.setBackgroundColor(getResources().getColor(R.color.backgroundinput)); // set background

        for (int i = 0; i < mRow; i++) {
            // Mỗi hàng của bảng cũng là 1 cái linearlayout theo hướng ngang
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            for (int j = 0; j < mCol; j++) {
                EditText editText = new EditText(this);
                editText.setWidth(dpToPx(60, this));
                editText.setHeight(dpToPx(60, this));
                editText.setTextSize(dpToPx(24, this));
                editText.setGravity(Gravity.CENTER);
                //editText.setPadding(10, 10, 10, 10);
                editText.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
                editText.setHint("0");
                editText.setId(i * mCol + j);

                // thêm cái textview vào linearLayout
                linearLayout.addView(editText);
            }

            // thêm mỗi dòng vào cái layout ngoài cùng.
            linearLayoutRoot.addView(linearLayout);
        }

        // Cái này để cho có khoảng cách thôi
        TextView textView = new TextView(this);
        textView.setHeight(100);
        linearLayoutRoot.addView(textView);


        // Tạo button để scan
        Button btnCamera = new Button(this);
        btnCamera.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btnCamera.setText("Scan");
        btnCamera.setWidth(dpToPx(200, this));
        btnCamera.setHeight(dpToPx(50, this));
        linearLayoutRoot.addView(btnCamera);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                for (int i = 0; i < mRow * mCol; i++) {
                    EditText editText = findViewById(i);
                    editText.setText("0");
                }
                startActivityForResult(intent, 0);
            }
        });

        setContentView(linearLayoutRoot);

    }


    // xử lí phần scan nhận kết quả trả về.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        //imageView.setImageBitmap(bitmap);

        TextRecognizer txtRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!txtRecognizer.isOperational()) {
            Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray items = txtRecognizer.detect(frame);
            StringBuilder strBuilder = new StringBuilder();
            int res = 0;
            for (int i = 0; i < items.size(); i++){

                TextBlock item = (TextBlock) items.valueAt(i);
                strBuilder.append(item.getValue());
                strBuilder.append("/");

                for (Text line : item.getComponents()) {
                    //extract scanned text lines here
                    Log.v("lines", line.getValue());
                    for (Text element : line.getComponents()) {
                        if (res >= mCol * mRow) break;
                        TextView textView = findViewById(res++);
                        textView.setText(element.getValue());
                        //extract scanned text words here
                        Log.v("element", element.getValue());

                    }
                }
            }
            //txtView.setText(strBuilder.toString().substring(0, strBuilder.toString().length() - 1));
            setContentView(linearLayoutRoot);
        }
    }

    // xử lí các nút ở thanh phía trên
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.OK:
                getData();
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

    // 1 hàm để t chuẩn hóa lại dữ liệu thôi
    void getData() {
        data = new double[mCol * mRow];
        for (int i = 0; i < mRow; i++)
            for (int j = 0; j < mCol; j++) {
                EditText editText = findViewById(i * mCol + j);
                if (!editText.getText().toString().isEmpty())
                    data[i * mCol + j] = Integer.parseInt(editText.getText().toString());
            }
    }
}
