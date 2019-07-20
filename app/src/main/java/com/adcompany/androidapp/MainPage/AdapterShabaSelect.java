package com.adcompany.androidapp.MainPage;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;


import java.util.ArrayList;

import com.adcompany.androidapp.R;

public class AdapterShabaSelect extends BaseAdapter {

    Context context;
    ArrayList<ListShabaSelect> listShabaSelect;
    LayoutInflater inflater;

    Typeface font_Bold;


    public AdapterShabaSelect(Context context, ArrayList<ListShabaSelect> listShabaSelect){

        this.context = context;
        this.listShabaSelect=listShabaSelect;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listShabaSelect.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            AdapterShabaSelect.ViewHolder holder;

            if (convertView==null)
            {
                holder=new AdapterShabaSelect.ViewHolder();
                convertView=inflater.inflate(R.layout.shaba_item_select,null);

                holder.TextViewShabaCardSelect=(TextView)convertView.findViewById(R.id.TextViewShabaCardSelect);
                holder.RadioButtonShabaCardSelect=(RadioButton) convertView.findViewById(R.id.RadioButtonShabaCardSelect);
                convertView.setTag(holder);
            }
            else
            {
                holder=(AdapterShabaSelect.ViewHolder)convertView.getTag();
            }
            String number=listShabaSelect.get(position).getNumber();
            String FirstNumber=number.substring(0,5);
            String LastNumber=number.substring(number.length()-5);
            holder.TextViewShabaCardSelect.setText("IR"+FirstNumber+"----------"+LastNumber);


            if (listShabaSelect.get(position).isSelect())
                holder.RadioButtonShabaCardSelect.setChecked(true);
            else
                holder.RadioButtonShabaCardSelect.setChecked(false);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return convertView;
    }
    public void updateRecords(ArrayList<ListShabaSelect> listShabaSelect)
    {
        try {
            this.listShabaSelect=listShabaSelect;

            notifyDataSetChanged();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private class ViewHolder{
        TextView TextViewShabaCardSelect;
        RadioButton RadioButtonShabaCardSelect;
    }
}
