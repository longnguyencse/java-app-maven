package dev.at.mikorn.vn;

import dev.at.mikorn.vn.service.implement.IfpApiService;

/**
 * Author Mikorn vietnam
 * Created on 12-Jun-18.
 */

public class RxApp {
    public static void main(String[] args) {
        String username = "eugenp";
        new IfpApiService().getTopContributors(username).subscribe(System.out::println);
    }
}
