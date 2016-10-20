package com.ishabaev.qrcodes.table;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.barcode.Barcode;
import com.ishabaev.qrcodes.R;
import com.ishabaev.qrcodes.WebViewActivity;
import com.ishabaev.qrcodes.model.QrCode;

import java.util.ArrayList;
import java.util.List;

import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

public class TableActivity extends AppCompatActivity implements TableView, BarcodeRetriever {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 2;

    private View mRootLL;
    private RecyclerView mTableRV;
    private TableAdapter mTableAdapter;
    private TablePresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        findViews();
        BarcodeCapture barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(R.id.scanner_view);
        barcodeCapture.setRetrieval(this);
        initTableRecyclerView();
        initPresenter();
        checkPermissions();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRetrieved(final Barcode barcode) {
        mPresenter.retrieveBarcode(barcode);
    }

    @Override
    public void addQrCode(@NonNull QrCode qrCode) {
        mTableAdapter.add(qrCode);
    }

    @Override
    public void onRetrievedMultiple(final Barcode closetToClick, final List<BarcodeGraphic> barcodeGraphics) {
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
    }

    @Override
    public void onRetrievedFailed(String reason) {
    }

    private void findViews() {
        mRootLL = findViewById(R.id.root);
        mTableRV = (RecyclerView) findViewById(R.id.table);
    }

    private void initTableRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        mTableAdapter = new TableAdapter(new ArrayList<>());
        mTableRV.setLayoutManager(layoutManager);
        mTableRV.setItemAnimator(animator);
        mTableRV.setAdapter(mTableAdapter);
        mTableAdapter.setOnClickListener((qrCode, view) ->
                WebViewActivity.showActivity(this, qrCode.getText()));
    }

    private void initPresenter() {
        mPresenter = new TablePresenter(this);
        mPresenter.loadQrCodes();
    }

    private void showPermissionMessage() {
        String messageText = getString(R.string.need_camera_permission);
        String actionText = getString(R.string.request_permission);
        Snackbar.make(mRootLL, messageText, Snackbar.LENGTH_INDEFINITE)
                .setAction(actionText, v -> checkPermissions())
                .show();
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        int hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        BarcodeCapture barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(R.id.scanner_view);
        barcodeCapture.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    showPermissionMessage();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void showQrCodes(@NonNull List<QrCode> qrCodes) {
        mTableAdapter.changeDataset(qrCodes);
    }

    @Override
    public void refreshStatusCodes(@NonNull List<QrCode> qrCodes) {
        mTableAdapter.refreshStatusCodes(qrCodes);
    }

}
