package dev.at.mikorn.vn.model.builder;

import dev.at.mikorn.vn.model.QcModel;

/**
 * Author Mikorn vietnam
 * Created on 13-Jun-18.
 */

public final class QcModelBuilder {
    private String fileId;
    private int revision;
    private int accept;

    private QcModelBuilder() {
    }

    public static QcModelBuilder aQcModel() {
        return new QcModelBuilder();
    }

    public QcModelBuilder withFileId(String fileId) {
        this.fileId = fileId;
        return this;
    }

    public QcModelBuilder withRevision(int revision) {
        this.revision = revision;
        return this;
    }

    public QcModelBuilder withAccept(int accept) {
        this.accept = accept;
        return this;
    }

    public QcModel build() {
        QcModel qcModel = new QcModel();
        qcModel.setFileId(fileId);
        qcModel.setRevision(revision);
        qcModel.setAccept(accept);
        return qcModel;
    }
}
