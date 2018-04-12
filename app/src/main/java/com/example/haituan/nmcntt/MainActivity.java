package com.example.haituan.nmcntt;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haituan.nmcntt.adapter.CustomAdapter;
import com.example.haituan.nmcntt.model.Contact;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int REQUEST_CODE = 1994;
    private int phuongThuc = 0; // coi mình đang ở cái tùy chọn tính toán nào
    private int stack[] = new int [10];
    private int    top = 0;
    private ListView lv_bien;
    private CustomAdapter customAdapter;
    private ArrayList<Contact> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        // File xử lí giao diện của activity_main là content_main.xml
        // Content_main.xml là cái màn hình ở giữa chứa listview ý
        //

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //show Dialog tạo biến mới.
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle("Tạo biến mới");
                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_read_input);
                final EditText edtTenBien = dialog.findViewById(R.id.TenBien);
                final EditText edtSoDong = dialog.findViewById(R.id.SoDong);
                final EditText edtSoCot = dialog.findViewById(R.id.SoCot);
                Button btnNhapBien = dialog.findViewById(R.id.btnNhapBien);

                //xử lý sự kiện khi ấn nút nhập biến
                btnNhapBien.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (edtTenBien.getText().toString().isEmpty()) {
                            Toast.makeText(dialog.getContext(), "Chưa điền tên.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (edtSoCot.getText().toString().isEmpty() || edtSoDong.getText().toString().isEmpty()) {
                            Toast.makeText(dialog.getContext(), "Điền số dòng số cột.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String mName = edtTenBien.getText().toString();
                        int mRow = Integer.parseInt(edtSoDong.getText().toString());
                        int mCol = Integer.parseInt(edtSoCot.getText().toString());

                        if (mRow == 0 || mCol == 0) {
                            Toast.makeText(dialog.getContext(), "Số dòng, số cột phải lớn hơn 0.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        dialog.dismiss();
                        Intent intent = new Intent(dialog.getContext(), ReadInputActivity.class);

                        intent.putExtra("TenBien", mName);
                        intent.putExtra("SoDong", mRow);
                        intent.putExtra("SoCot", mCol);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                });
                dialog.show();
            }
        });

        //hiển thị cái navigation (mặc định sinh ra là nó tạo vậy rồi)
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Design và xử lí sự kiện của cái listview chứa biến
        lv_bien = findViewById(R.id.lv_bien);

        double a[][] = new double[][] {{1, 0}, {0, 1}};
        Contact contact1 = new Contact("I2", 2, 2, a);
        arrayList = new ArrayList<>();
        arrayList.add(contact1);
        //muốn chỉnh sửa lại giao diện từng dòng của listview thì vào cái layout sau.
        customAdapter = new CustomAdapter(this, R.layout.row_listview, arrayList);

        lv_bien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (phuongThuc) {
                    case 0: truyenOutput(arrayList.get(i)); break;
                    case 1: Add(i); break;
                    case 2: Sub(i); break;
                    case 3: Mul(i); break;
                    case 4: BacThang(i); break;
                    case 5: BacThangRG(i); break;
                    case 6: NghichDao(i); break;
                    case 7: Det(i); break;
                }

            }
        });
        lv_bien.setAdapter(customAdapter);
    }

    // Các phương thức Cộng trừ nhân chia ....
    void Add(int pos) {
        TextView textView = findViewById(R.id.tv_man_hinh);
        if (top == 0) {
            textView.setText(arrayList.get(pos).getmName());
            stack[top] = pos;
            top = 1;
        } else {
            String text = textView.getText().toString();
            text = text + " + " + arrayList.get(pos).getmName();
            textView.setText(text);

            double ans[][] = Contact.add(arrayList.get(stack[0]).getData(), arrayList.get(pos).getData());
            if (ans == null) {
                Toast.makeText(this, "Các mảng khác kích thước", Toast.LENGTH_SHORT).show();
            } else {
                Contact contact = new Contact("Ans_add", arrayList.get(pos).getmRow(),
                        arrayList.get(pos).getmCol(), ans);
                truyenOutput(contact);
            }

            top = 0;
            textView.setText("");
        }
    }

    void Sub(int pos) {
        TextView textView = findViewById(R.id.tv_man_hinh);
        if (top == 0) {
            textView.setText(arrayList.get(pos).getmName());
            stack[top] = pos;
            top = 1;
        } else {
            String text = textView.getText().toString();
            text = text + " - " + arrayList.get(pos).getmName();
            textView.setText(text);

            double ans[][] = Contact.sub(arrayList.get(stack[0]).getData(), arrayList.get(pos).getData());
            if (ans == null) {
                Toast.makeText(this, "Các mảng khác kích thước", Toast.LENGTH_SHORT).show();
            } else {
                Contact contact = new Contact("Ans_sub ", arrayList.get(pos).getmRow(),
                        arrayList.get(pos).getmCol(), ans);
                truyenOutput(contact);
            }

            top = 0;
            textView.setText("");
        }
    }

    void Mul(int pos) {
        TextView textView = findViewById(R.id.tv_man_hinh);
        if (top == 0) {
            textView.setText(arrayList.get(pos).getmName());
            stack[top] = pos;
            top = 1;
        } else {
            String text = textView.getText().toString();
            text = text + " * " + arrayList.get(pos).getmName();
            textView.setText(text);

            double ans[][] = Contact.mul(arrayList.get(stack[0]).getData(), arrayList.get(pos).getData());
            if (ans == null) {
                Toast.makeText(this, "Các mảng không phù hợp kích thước", Toast.LENGTH_SHORT).show();
            } else {
                Contact contact = new Contact("Ans_mul", arrayList.get(pos).getmRow(),
                        arrayList.get(pos).getmCol(), ans);
                truyenOutput(contact);
            }

            top = 0;
            textView.setText("");
        }
    }

    void BacThang(int pos) {
//        double ans[][] = Contact.bacThang(arrayList.get(pos).getData());
//        Contact contact = new Contact("Ans_BT", arrayList.get(pos).getmRow(),
//                arrayList.get(pos).getmCol(), ans);
//        truyenOutput(contact);
        Contact contact = arrayList.get(pos);
        Intent intent = new Intent(MainActivity.this, ShowSolution.class);
        intent.putExtra("a", contact);
        startActivity(intent);
    }

    void BacThangRG(int pos) {
        double ans[][] = Contact.bacThangRG(arrayList.get(pos).getData());
        Contact contact = new Contact("Ans_BTRG", arrayList.get(pos).getmRow(),
                arrayList.get(pos).getmCol(), ans);
        truyenOutput(contact);
    }

    void NghichDao(int pos) {
        if (arrayList.get(pos).getmRow() != arrayList.get(pos).getmCol()) {
            Toast.makeText(this, "Đây không phải là ma trân vuông.", Toast.LENGTH_SHORT).show();
        }

        double ans[][] = Contact.nghichDao(arrayList.get(pos).getData());
        if (ans == null) {
            Toast.makeText(this, "Ma trận không khả nghịch.", Toast.LENGTH_SHORT).show();
        }

        Contact contact = new Contact("Ans_nghich_dao", arrayList.get(pos).getmRow(),
                arrayList.get(pos).getmCol(), ans);
        truyenOutput(contact);
    }

    void Det(int pos ) {
        if (arrayList.get(pos).getmRow() != arrayList.get(pos).getmCol()) {
            Toast.makeText(this, "Đây không phải là ma trận vuông.", Toast.LENGTH_SHORT).show();
        }
        double detA = Contact.det(arrayList.get(pos).getData());
        Toast.makeText(this, "Det = " + detA, Toast.LENGTH_LONG).show();
    }

    //xử lí truyền dữ liệu giữa các activity
    void truyenOutput(Contact contact) {
        Intent intent = new Intent(MainActivity.this, OutputActivity.class);
        intent.putExtra("TenBien", contact.getmName());
        intent.putExtra("SoDong", contact.getmRow());
        intent.putExtra("SoCot", contact.getmCol());

        double data1[][];
        double data[] = new double[contact.getmRow() * contact.getmCol()];
        data1 = contact.getData();
        for (int x = 0; x < contact.getmRow(); x++)
            for (int y = 0; y < contact.getmCol(); y++) data[x * contact.getmCol() + y] = data1[x][y];
        intent.putExtra("data", data);
        startActivityForResult(intent, REQUEST_CODE);
    }

    // tự tạo
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //tự tạo
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Xử lí khi chọn các mục của thanh Navigation
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        TextView textView = findViewById(R.id.tv_man_hinh);
        textView.setVisibility(View.VISIBLE);
        top = 0;

        if (id == R.id.nav_home) {
            textView.setVisibility(View.INVISIBLE);
            phuongThuc = 0;
        } else if (id == R.id.nav_add) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("Cộng hai ma trận.");
            phuongThuc = 1;
        } else if (id == R.id.nav_sub) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("Trừ hai ma trận.");
            phuongThuc = 2;
        } else if (id == R.id.nav_mul) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("Nhân hai ma trân.");
            phuongThuc = 3;
        } else if (id == R.id.nav_bac_thang) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("Ma trận bậc thang.");
            phuongThuc = 4;
        } else if (id == R.id.nav_bac_thang_RG) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("Ma trận bậc thang rút gọn.");
            phuongThuc = 5;
        } else if (id == R.id.nav_nghich_dao) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("Ma trận nghịch đảo.");
            phuongThuc = 6;
        } else if (id == R.id.nav_det) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("Định thức của ma trận.");
            phuongThuc = 7;
        } else if (id == R.id.nav_tai_lieu) {
            Intent intent = new Intent(this, TaiLieu.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // Nhận giữ liệu khi sau khi activity output trả về để thêm vào listview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE){
            if(resultCode==RESULT_OK){
                String mName = data.getStringExtra("mName");
                int mRow = data.getIntExtra("mRow", 0);
                int mCol = data.getIntExtra("mCol", 0);
                double[] a = data.getDoubleArrayExtra("data");

                double aa[][] = new double[mRow][mCol];
                for (int i = 0; i < mRow; i++)
                    for (int j = 0; j < mCol; j++) aa[i][j] = a[i * mCol + j];
                Contact contact = new Contact(mName, mRow, mCol, aa);
                arrayList.add(contact);
                customAdapter.notifyDataSetChanged();
            }
        }

    }
}
