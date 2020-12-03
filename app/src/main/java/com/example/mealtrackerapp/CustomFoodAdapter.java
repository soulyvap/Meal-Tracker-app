package com.example.mealtrackerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.sql.RowId;
import java.util.ArrayList;
import java.util.List;

public class CustomFoodAdapter extends ArrayAdapter<FoodLog> {
    private Context context;
    private List<FoodLog> arrayList;
    int resource;

    public CustomFoodAdapter(@NonNull Context context, int resource, @NonNull List<FoodLog> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrayList = objects;
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public FoodLog getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item_with_delete, null);

        TextView text = (TextView) convertView.findViewById(R.id.list_item_text);
        text.setText(arrayList.get(position).getStringGrams());

        ImageView button = (ImageView) convertView.findViewById(R.id.list_item_delete);
        button.setTag(position);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positionToRemove = (int) v.getTag();
                arrayList.remove(positionToRemove);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
