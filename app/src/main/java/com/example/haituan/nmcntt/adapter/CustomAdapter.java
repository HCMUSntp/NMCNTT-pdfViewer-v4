package com.example.haituan.nmcntt.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haituan.nmcntt.R;
import com.example.haituan.nmcntt.model.Contact;

import java.util.ArrayList;

/**
 * Created by HaiTuan on 27/03/2018.
 */

public class CustomAdapter extends ArrayAdapter <Contact> {
    private Context context;
    private int     resource;
    private ArrayList<Contact> arrContact;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Contact> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrContact = objects;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    // Cái này để khởi tạo cái dữ liệu lên cho một dòng của listview
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.row_listview, parent, false);
        //cái ảnh nằm bên trái
        ImageView imv_iconlist = convertView.findViewById(R.id.imv_iconlist);
        // tên
        TextView tv_name = convertView.findViewById(R.id.tv_name);
        TextView tv_row = convertView.findViewById(R.id.tv_row);
        TextView tv_col = convertView.findViewById(R.id.tv_col);

        Contact contact = arrContact.get(position);

        tv_name.setText(contact.getmName());
        tv_row.setText(String.format("Dòng = %d", contact.getmRow()));
        tv_col.setText(String.format("Cột = %d", contact.getmCol()));
        // xử lý trường hợp gì đấy thì set lại cái ImageView ở đây luôn
        // set imv_iconlist

        return convertView;
    }
}
