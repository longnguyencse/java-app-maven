package dev.at.mikorn.vn.configuration;

/**
 * Author Mikorn vietnam
 * Created on 12-Jun-18.
 */

public enum ConfigParams {
    FOLDER_DATA("folder-data-dummy"),
    ZIP_FOLDER("zip-folder"),
    NUMBER_FRAMES("number-frame-annotated-file");

    String param;

    ConfigParams(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }
}
