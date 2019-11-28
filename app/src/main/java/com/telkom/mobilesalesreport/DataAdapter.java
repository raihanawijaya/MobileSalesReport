package com.telkom.mobilesalesreport;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    Context mcontext;
    List<DataClass> listData;

    public DataAdapter(List<DataClass> listData) {
        this.listData = listData;
    }

    public void setmData(List<DataClass> mData) {
        this.listData = mData;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        /*View v;
        v = LayoutInflater.from(mcontext).inflate(R.layout.items_a,viewGroup,false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;*/
        mcontext = viewGroup.getContext();
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.items_a, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        final DataClass dataClass = listData.get(i);
        viewHolder.tv_trxDate.setText(listData.get(i).getTrxDate());
        viewHolder.tv_article.setText(listData.get(i).getArticle());
        viewHolder.tv_qty.setText(listData.get(i).getQty());
        viewHolder.tv_price.setText(listData.get(i).getPrice());
        viewHolder.lay_item_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go_to_b_detail = new Intent(mcontext, EditTransactionActivity.class);
                go_to_b_detail.putExtra("itemTrxCode",dataClass.getTrxCode());
                go_to_b_detail.putExtra("itemDate",dataClass.getTrxDate());
                go_to_b_detail.putExtra("itemArticle",dataClass.getArticle());
                go_to_b_detail.putExtra("itemPrice",dataClass.getPrice());
                go_to_b_detail.putExtra("itemQty",dataClass.getQty());
                go_to_b_detail.putExtra("itemRowId",dataClass.getRowid());
                mcontext.startActivity(go_to_b_detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //8. fill in itemView
        private LinearLayout lay_item_data;
        private TextView tv_trxDate, tv_article, tv_qty, tv_price;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lay_item_data = itemView.findViewById(R.id.lay_item_data);
            tv_trxDate = itemView.findViewById(R.id.tv_trx_date);
            tv_article = itemView.findViewById(R.id.tv_article);
            tv_qty = itemView.findViewById(R.id.tv_qty);
            tv_price = itemView.findViewById(R.id.tv_price);
        }
    }
}
