package dev.at.mikorn.vn.service;

import dev.at.mikorn.vn.model.Contributor;
import dev.at.mikorn.vn.model.QcModel;
import dev.at.mikorn.vn.model.Repository;
import dev.at.mikorn.vn.model.Result;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.*;
import rx.Observable;

import java.io.File;
import java.util.List;

/**
 * Author Mikorn vietnam
 * Created on 12-Jun-18.
 */

public interface IfpAPI {

    enum SERVER_CONFIG {
        IFP("http://ifp-nifi.at.mikorn.com", 8083);


        String urlEndPoit;
        int port;
        SERVER_CONFIG(String urlEndPoint, int port) {
            SERVER_CONFIG.this.urlEndPoit   = urlEndPoint;
            SERVER_CONFIG.this.port         = port;
        }

        public String getUrlEndPoit() {
            return urlEndPoit;
        }
        public int getPort() {
            return port;
        }
    }
    // 3.1 Upload original file on the web (binary zip file)
    @Multipart
    @POST("/files/save")
    Observable<Result> savefile(@Part RequestBody file_type,
                                @Part("file") MultipartBody.Part file);

    // 3.10. Save a file frame layer (.gtt compress file) with revision (labeled data)
    @POST("/files/save_frame_layer")
    Observable<Result> saveFrameLayer(@Part RequestBody revision, @Part RequestBody file_id,
                                      @Part RequestBody file, @Part RequestBody frame_id);

    // 3.13 QC Tool: Make checking a file|image with revision (Temporary flow)
    @POST("/files/qc_checking")
    Observable<Result> qcChecking(@Body QcModel qcModel);

    // 3.14 QC Tool: Accept/Decline a file|image with revision
    @POST("/files/qc_accept_annotated")
    Observable<Result> qcAcceptAnnotated (@Body QcModel qcModel);


    // test api github
    @GET("users/{user}/repos")
    Observable<List<Repository>> listRepos(@Path("user") String user);

    @GET("repos/{user}/{repo}/contributors")
    Observable<List<Contributor>> listRepoContributors(
            @Path("user") String user,
            @Path("repo") String repo);


}
