package dev.at.mikorn.vn.model;

/**
 * Author Mikorn vietnam
 * Created on 12-Jun-18.
 */

public class Repository {
    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Repository [name=" + name + ", description=" + description + "]";
    }
}
