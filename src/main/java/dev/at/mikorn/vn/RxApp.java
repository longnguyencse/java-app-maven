package dev.at.mikorn.vn;

import dev.at.mikorn.vn.service.implement.IfpApiService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;

/**
 * Author Mikorn vietnam
 * Created on 12-Jun-18.
 */

public class RxApp {
    public static void main(String[] args) {
        String username = "eugenp";
//        new IfpApiService().getTopContributors(username).subscribe(System.out::println);

        RxApp app = new RxApp();
        app.uploadFile("D:\\learn\\data\\example\\zip\\Original_file_1528871273105_74_93.zip");
    }
    private void uploadFile(String path) {
        // create upload service client

        // use the FileUtils to get the actual file by uri
        File file = new File(path);


        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("video", file.getName(), requestFile);
//        body.
        // add another part within the multipart request
        String fileType = "video";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), fileType);

        // finally, execute the request
        new IfpApiService().triggerUploadingToHdfs(description, body).subscribe(result -> {
            System.out.println("result: " + result.getMessage());
        }, throwable -> {
            System.out.println("fail");
        }, () -> {
            System.out.println("adafasfa");
        });

    }

}
