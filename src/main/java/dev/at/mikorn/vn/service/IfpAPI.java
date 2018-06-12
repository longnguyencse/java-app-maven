package dev.at.mikorn.vn.service;

import dev.at.mikorn.vn.model.Contributor;
import dev.at.mikorn.vn.model.Repository;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

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

    @POST("/files/save")
    Observable<List> savefile();

// test api
    @GET("users/{user}/repos")
    Observable<List<Repository>> listRepos(@Path("user") String user);

    @GET("repos/{user}/{repo}/contributors")
    Observable<List<Contributor>> listRepoContributors(
            @Path("user") String user,
            @Path("repo") String repo);


}
