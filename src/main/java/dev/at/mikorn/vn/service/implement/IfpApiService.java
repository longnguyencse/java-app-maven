package dev.at.mikorn.vn.service.implement;

import dev.at.mikorn.vn.model.Contributor;
import dev.at.mikorn.vn.model.QcModel;
import dev.at.mikorn.vn.model.Result;
import dev.at.mikorn.vn.service.IfpAPI;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Author Mikorn vietnam
 * Created on 12-Jun-18.
 */

public class IfpApiService {
    private IfpAPI ifpAPI;
    private final static String VIDEO_TYPE = "video";

    public IfpApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("https://api.github.com/")
                .baseUrl(String.format("%s:%s",IfpAPI.SERVER_CONFIG.IFP.getUrlEndPoit(),
                        IfpAPI.SERVER_CONFIG.IFP.getPort()))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ifpAPI = retrofit.create(IfpAPI.class);
    }

   public Observable<String> getTopContributors(String userName) {
        return ifpAPI.listRepos(userName).flatMapIterable(x -> x)
                .flatMap(repo -> ifpAPI.listRepoContributors(userName, repo.getName()))
                .flatMapIterable(x -> x).filter(c -> c.getContributions() > 100)
                .sorted((a, b) -> b.getContributions() - a.getContributions())
                .map(Contributor::getName)
                .distinct();
    }

    // flow 3.1 --> 3.10 --> 3.13 --> 3.14
    // 3.1
    public Observable<Result> saveFile(RequestBody fileType, MultipartBody.Part file) {
        return ifpAPI.savefile(fileType, file);
    }
    // 3.10
    public Observable<Result> saveFrameLayer(RequestBody revision, RequestBody file_id, MultipartBody.Part file,
                                             RequestBody frame_id) {
        return ifpAPI.saveFrameLayer(revision, file_id, file, frame_id);
    }
    // 3.13
    public Observable<Result> qcChecking(QcModel qcModel) {
        return ifpAPI.qcChecking(qcModel);
    }
    // 3.14
    public Observable<Result> qcAcceptAnnotated(QcModel qcModel) {
        return ifpAPI.qcAcceptAnnotated(qcModel);
    }
}
