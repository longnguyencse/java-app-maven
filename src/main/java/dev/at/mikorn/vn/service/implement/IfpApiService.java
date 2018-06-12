package dev.at.mikorn.vn.service.implement;

import dev.at.mikorn.vn.model.Contributor;
import dev.at.mikorn.vn.service.IfpAPI;
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

}
