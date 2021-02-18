package com.example.phonefreedom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class AppPickerAdapter extends ArrayAdapter {

    private ArrayList<AppPickerDataModel> dataset;
    Context context;

    private static class ViewHolder {
        TextView appName;
        CheckBox checkBox;
    }

    public AppPickerAdapter(ArrayList dataset, Context context) {
        super(context, R.layout.row_item, dataset);
        this.dataset = dataset;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public AppPickerDataModel getItem(int position) {
        return dataset.get(position);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
            viewHolder.appName = convertView.findViewById(R.id.row_item_app_name);
            viewHolder.checkBox = convertView.findViewById(R.id.row_item_checkbox);

            result=convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        AppPickerDataModel item = getItem(position);


        viewHolder.appName.setText(item.appName);
        viewHolder.checkBox.setChecked(item.checked);


        return result;
    }
}


