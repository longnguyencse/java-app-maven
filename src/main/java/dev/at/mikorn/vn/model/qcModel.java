package dev.at.mikorn.vn.model;

import com.google.gson.annotations.SerializedName;

/**
 * Author Mikorn vietnam
 * Created on 13-Jun-18.
 */

public class QcModel {

    @SerializedName("file_id")
    private String fileId;

    private int revision;
    private int accept;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public int getAccept() {
        return accept;
    }

    public void setAccept(int accept) {
        this.accept = accept;
    }
}
