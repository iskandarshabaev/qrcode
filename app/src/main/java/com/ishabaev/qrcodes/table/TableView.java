package com.ishabaev.qrcodes.table;


import android.support.annotation.NonNull;

import com.ishabaev.qrcodes.model.QrCode;

import java.util.List;

public interface TableView {

    void showQrCodes(@NonNull List<QrCode> qrCodes);

    void refreshStatusCodes(@NonNull List<QrCode> qrCodes);

    void addQrCode(@NonNull QrCode qrCode);

}
