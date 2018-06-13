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
//        new IfpApiService().triggerUploadingToHdfs("video",
//                new File("D:\\learn\\data\\example\\zip\\Original_file_1528809017894_338_10.zip")
//        ).subscribe(System.out::println);
        RxApp app = new RxApp();
        app.uploadFile("D:\\learn\\data\\example\\zip\\Original_file_1528809017894_338_10.zip");
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
        new IfpApiService().triggerUploadingToHdfs(description, body).subscribe(System.out::println);



//        Call<ResponseBody> call = cobaltServices.upload(description, body);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call,
//                                   Response<ResponseBody> response) {
//                Log.v("Upload", "success");
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("Upload error:", t.getMessage());
//            }
//        });
    }

}
