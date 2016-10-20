package com.ishabaev.qrcodes.table;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;

import com.ishabaev.qrcodes.R;
import com.ishabaev.qrcodes.model.QrCode;

import static com.ishabaev.qrcodes.model.StatusCode.UNDEFINED;

public class TableViewHolder extends RecyclerView.ViewHolder {

    private TextView mStatusCodeTV;
    private TextView mUrlTV;
    public View root;

    public TableViewHolder(@NonNull View view) {
        super(view);
        root = view;
        mStatusCodeTV = (TextView) view.findViewById(R.id.status_code);
        mUrlTV = (TextView) view.findViewById(R.id.url);
    }

    public void bind(@NonNull QrCode qrCode) {
        String statusCode = qrCode.getStatusCode() == UNDEFINED ? "" : "" + qrCode.getStatusCode();
        mStatusCodeTV.setText(statusCode);
        mUrlTV.setText(qrCode.getText());
        root.setEnabled(URLUtil.isValidUrl(qrCode.getText()));
    }

}
