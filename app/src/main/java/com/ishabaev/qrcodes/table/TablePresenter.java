package com.ishabaev.qrcodes.table;


import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.webkit.URLUtil;

import com.google.android.gms.vision.barcode.Barcode;
import com.ishabaev.qrcodes.api.ApiFactory;
import com.ishabaev.qrcodes.model.QrCode;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ishabaev.qrcodes.model.StatusCode.UNDEFINED;

public class TablePresenter {

    private TableView mView;

    public TablePresenter(@NonNull TableView view) {
        mView = view;
    }

    public void loadQrCodes() {
        Realm.getDefaultInstance()
                .where(QrCode.class)
                .findAllAsync()
                .asObservable()
                .filter(RealmResults::isLoaded)
                .first()
                .flatMap(res -> Observable.just(Realm.getDefaultInstance().copyFromRealm(res)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleQrCodes, this::showError);
    }

    public void retrieveBarcode(Barcode barcode) {
        QrCode qrCode = new QrCode(barcode.displayValue, barcode.format,
                System.currentTimeMillis(), UNDEFINED);
        Observable.just(qrCode)
                .filter(code -> Realm.getDefaultInstance().where(QrCode.class)
                        .equalTo("text", code.getText())
                        .findAll().size() == 0)
                .doOnNext(code -> Realm.getDefaultInstance()
                        .executeTransaction(realm -> realm.insertOrUpdate(qrCode)))
                .filter(code -> code.getText().length() > 0)
                .flatMap(code -> {
                    if (URLUtil.isValidUrl(code.getText())) {
                        return network(code);
                    } else {
                        return Observable.just(code);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::addQrCode, this::showError);
    }

    private Observable<QrCode> network(QrCode qrCode) {
        return Observable.just(qrCode)
                .flatMap(code -> Observable.zip(Observable.just(code),
                        ApiFactory.getApi().makeRequest(code.getText()), Pair::new))
                .doOnNext(pair -> pair.first.setStatusCode(pair.second.code()))
                .flatMap(pair -> Observable.just(pair.first))
                .onErrorResumeNext(throwable -> Observable.just(qrCode))
                .doOnNext(code -> Realm.getDefaultInstance()
                        .executeTransaction(realm -> realm.insertOrUpdate(code)));
    }

    private void handleQrCodes(@NonNull List<QrCode> qrCodes) {
        mView.showQrCodes(qrCodes);
        Observable.from(qrCodes)
                .filter(qrCode -> URLUtil.isValidUrl(qrCode.getText()))
                .filter(qrCode -> qrCode.getStatusCode() == UNDEFINED)
                .flatMap(qrCode ->
                        Observable.zip(
                                Observable.just(qrCode),
                                ApiFactory.getApi().makeRequest(qrCode.getText()), Pair::new))
                .doOnNext(pair -> pair.first.setStatusCode(pair.second.code()))
                .flatMap(pair -> Observable.just(pair.first))
                .toList()
                .filter(list -> list.size() > 0)
                .doOnNext(qrCodeList -> Realm.getDefaultInstance()
                        .executeTransaction(realm -> realm.insertOrUpdate(qrCodeList)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qrCodeList -> mView.refreshStatusCodes(qrCodeList), this::showError);
    }

    private void showError(Throwable throwable) {
        throwable.printStackTrace();
    }
}
