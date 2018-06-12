package dev.at.mikorn.vn.model;

import com.google.gson.annotations.SerializedName;

/**
 * Author Mikorn vietnam
 * Created on 12-Jun-18.
 */

public class Contributor {
    @SerializedName("login")
    private String name;

    private Integer contributions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getContributions() {
        return contributions;
    }

    public void setContributions(Integer contributions) {
        this.contributions = contributions;
    }

    @Override
    public String toString() {
        return "Contributer [name=" + name + ", contributions=" + contributions + "]";
    }
}
