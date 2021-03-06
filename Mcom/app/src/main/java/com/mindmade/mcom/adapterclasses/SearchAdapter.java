package com.mindmade.mcom.adapterclasses;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mindmade.mcom.R;
import com.mindmade.mcom.activity.ProductDescriptionActivity;
import com.mindmade.mcom.utilclasses.Const;
import com.mindmade.mcom.utilclasses.model.Products;
import com.mindmade.mcom.utilclasses.model.SearchModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mindmade technologies.
 */

public class SearchAdapter  extends BaseAdapter {

    private List<SearchModel.Search> data;

    private String[] typeAheadData;

    LayoutInflater inflater;
    Context mContext;

    public SearchAdapter(Context context, List<SearchModel.Search> passData) {
        mContext=context;
        inflater = LayoutInflater.from(mContext);
        data = passData;
        Log.d("Success","Name::: "+data.get(0).getName());
        //typeAheadData = mContext.getResources().getStringArray(R.array.state_array_full);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
/*
        String currentListData = (String) getItem(position);
        Log.d("Success",""+currentListData);*/
        Log.d("Success",""+data.get(position).getName());
        mViewHolder.textView.setText(data.get(position).getName());
        mViewHolder.textView.setPadding(2,2,2,2);
        mViewHolder.textView.setTextColor(ContextCompat.getColor(mContext, R.color.searchTextcolor));

        mViewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Success", "MMM" + data.get(position).getId());
                Intent nextIntent = new Intent(mContext, ProductDescriptionActivity.class);
                nextIntent.putExtra(Const.PRODUCT_NAME, String.valueOf(data.get(position).getName())) ;
                nextIntent.putExtra(Const.PRODUCT_ID_KEY, String.valueOf(data.get(position).getId()));
                mContext.startActivity(nextIntent);
            }
        });

        return convertView;
    }

    public void clearAdapter(){
        data.clear();
        notifyDataSetChanged();
    }
    private class MyViewHolder {
        TextView textView;

        public MyViewHolder(View convertView) {
            textView = (TextView) convertView.findViewById(android.R.id.text1);
        }
    }


}
