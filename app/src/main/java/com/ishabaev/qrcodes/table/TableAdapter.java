package com.ishabaev.qrcodes.table;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishabaev.qrcodes.R;
import com.ishabaev.qrcodes.model.QrCode;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableViewHolder> {

    private List<QrCode> mQrCodes;

    private OnItemClickListener mOnItemClickListener;

    private View.OnClickListener mOnClickListener = v -> {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onClick((QrCode) v.getTag(R.id.root), v);
        }
    };

    public void setOnClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(@NonNull QrCode qrCode, @NonNull View view);
    }

    public TableAdapter(@NonNull List<QrCode> qrCodes) {
        mQrCodes = qrCodes;
    }

    public void changeDataset(@NonNull List<QrCode> qrCodes) {
        mQrCodes.clear();
        mQrCodes.addAll(qrCodes);
        notifyDataSetChanged();
    }

    public void add(@NonNull QrCode qrCodes) {
        mQrCodes.add(qrCodes);
        notifyDataSetChanged();
    }

    public void refreshStatusCodes(@NonNull List<QrCode> qrCodes) {
        notifyDataSetChanged();
    }

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_table, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {
        holder.bind(mQrCodes.get(position));
        holder.root.setOnClickListener(mOnClickListener);
        holder.root.setTag(R.id.root, mQrCodes.get(position));
    }

    @Override
    public int getItemCount() {
        return mQrCodes.size();
    }
}
