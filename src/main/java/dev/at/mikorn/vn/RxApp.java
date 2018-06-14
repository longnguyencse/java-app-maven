package dev.at.mikorn.vn;

import com.google.gson.Gson;
import dev.at.mikorn.vn.service.implement.IfpApiService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author Mikorn vietnam
 * Created on 12-Jun-18.
 */

public class RxApp {
    private static final String MULTI_PART_FORM_DATA = "multipart/form-data";

    public static void main(String[] args) {
        String username = "eugenp";
//        new IfpApiService().getTopContributors(username).subscribe(System.out::println);

        RxApp app = new RxApp();
        //app.uploadFile("D:\\learn\\data\\example\\zip\\Original_file_1528871273105_74_93.zip");
        app.saveFrameLayer("D:\\learn\\data\\example\\zip\\Original_file_1528871273105_74_93.zip");
    }

    private void saveFrameLayer(String path) {
        // use the FileUtils to get the actual file by uri
        File file = new File(path);
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("video", file.getName(), requestFile);

        RequestBody revision =      RequestBody.create(MediaType.parse("multipart/form-data"), "1");
        RequestBody fileId = RequestBody.create(MediaType.parse("multipart/form-data"), file.getName());
        List<String> listFramename = Arrays.asList("ada", "dada", "dadaaaa");

        String json = new Gson().toJson(listFramename);
        RequestBody frameId = RequestBody.create(MediaType.parse("multipart/form-data"), json);

//        new IfpApiService().saveFrameLayer(revision, fileId, body, frameId ).subscribe(result -> {
//            System.out.println("result: " + result.getMessage());
//        }, throwable -> {
//            System.out.println("fail");
//        }, () -> { // next
//            System.out.println("adafasfa");
//        });
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

        // add another part within the multipart request
        String fileType = "video";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), fileType);

        // finally, execute the request
//        new IfpApiService().saveFile(description, body).subscribe(result -> {
//            System.out.println("result: " + result.getMessage());
//        }, throwable -> {
//            System.out.println("fail");
//        }, () -> { // next
//            System.out.println("adafasfa");
//        });

    }

}
