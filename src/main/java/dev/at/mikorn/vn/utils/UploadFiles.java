package dev.at.mikorn.vn.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;


//import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;

/**
 * This example shows how to upload files using POST requests 
 * with encryption type "multipart/form-data".
 * For more details please read the full tutorial
 * on https://javatutorial.net/java-file-upload-rest-service
 * @author javatutorial.net
 */
public class UploadFiles {

	public static void main(String[] args) throws IOException {
//		String filePath = "D:\\tmp\\IFP_test_data\\load_frame_layers";
		String filePath = ".\\data\\annotated";
		String filePathGtt = "D:\\learn\\data\\annotated\\IMG_4513_01680.gtt";
		String url = "";
		
		String frameId = "";
		String fileName = "";
		Integer index = 1;
		Integer version = 0;
		Integer fromIndex = 1;
		Integer toIndex = 10;
		Boolean isCon1 = true;
//		url = "http://localhost:8081/files/save";
//		filePath = "D:\\tmp\\IFP_test_data";
//		fromIndex = 11;
//		toIndex = 20;
		// do upload org files
		/*for(int i = fromIndex; i <= toIndex; i++) {
			fileName = "video_test_" + i + ".zip";
			isCon1 = true;
			index = 1;
			uploadMutiFiles(filePath + "\\" + fileName, url, frameId, fileName, version);
			System.out.println(2);
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			// TODO: handle exception
		}*/
		url = "http://192.168.1.220:8084/files/save_frame_layer";
		// do annotating : upload layer
		
		for(int i = fromIndex; i <= toIndex; i++) {
			fileName = "video_test_" + i + ".zip";
			isCon1 = true;
			index = 1;
			for(int n = 31; n < 101; n++) {
				for(int m = 1 ; m < 64; m++) {
					if(index > 64) {
//						isCon1 = false;
//						break;
					}
					if(index > 512) {
						isCon1 = false;
						break;
					}
					String fileNameSub = n + "_" + m + ".gtt";
					frameId = index + ".png";
					String path = filePath + "\\" + fileNameSub;
					File fileDes = new File(filePath + "\\" + fileNameSub);

					if (!fileDes.exists()) {
						File source = new File(filePathGtt);
						Files.copy(source.toPath(), fileDes.toPath());
					}

					uploadMutiFiles(filePath + "\\" + fileNameSub, url, frameId, fileName, version);
					index ++ ;
				}
				if(isCon1 == false) {
					System.out.println(1);
					break;
				}
			}
			System.out.println(2);
		}
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// do annotated 
		
		/*String anUrl = "http://localhost:8081/files/annotated";
		
		for(int i = 20; i < 70; i++) {
			fileName = "video_test_" + i + ".zip";
			try {
				doAnnotated(anUrl, fileName, 0);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			// TODO: handle exception
		}
			// do annotated 
			
			String acceptUrl = "http://localhost:8081/files/qc_accept_annotated";
			
			for(int i = 20; i < 70; i++) {
				fileName = "video_test_" + i + ".zip";
				try {
					doPassed(acceptUrl, fileName, 0);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}*/
	}





	public static void uploadMutiFiles (String filePath, String url, String frameId, String fileName, Integer revision) {
		// the file we want to upload
				File inFile = new File(filePath);
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(inFile);
					DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
					
					// server back-end URL
					HttpPost httppost = new HttpPost(url);
					MultipartEntity entity = new MultipartEntity();

					// set the file input stream and file name as arguments
					entity.addPart("file", new InputStreamBody(fis, inFile.getName()));
					
					// set new part
					entity.addPart("file_type", new StringBody("video"));
					entity.addPart("file_id", new StringBody(fileName));
					entity.addPart("frame_id ", new StringBody(frameId));
					entity.addPart("revision ", new StringBody(revision.toString()));
					httppost.setEntity(entity);

					// execute the request
					HttpResponse response = httpclient.execute(httppost);
					
					int statusCode = response.getStatusLine().getStatusCode();
					HttpEntity responseEntity = response.getEntity();
					String responseString = EntityUtils.toString(responseEntity, "UTF-8");
					
					System.out.println("[" + statusCode + "] " + responseString + " :: fileName = " + fileName + " :: iframeId = " + frameId);
					
				} catch (ClientProtocolException e) {
					System.err.println("Unable to make connection");
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println("Unable to read file");
					e.printStackTrace();
				} finally {
					try {
						if (fis != null) fis.close();
					} catch (IOException e) {}
				}
	}
	
	private static void doAnnotated(String url, String fileName, Integer version) throws Exception{
		
        
        try {
			DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
			
			// server back-end URL
			HttpPost httppost = new HttpPost(url);
			
			JSONObject json = new JSONObject();
			json.put("file_id", fileName);
			json.put("revision", version);
			String payload =json.toString();
//			payload = "data=" + payload;
	        StringEntity entity = new StringEntity(payload,
	                ContentType.APPLICATION_FORM_URLENCODED);
	        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httppost.setEntity(entity);

			// execute the request
			HttpResponse response = httpclient.execute(httppost);
			
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity responseEntity = response.getEntity();
			String responseString = EntityUtils.toString(responseEntity, "UTF-8");
			
			System.out.println("Annotated ::: [" + statusCode + "] " + responseString + " :: fileName = " + fileName );
			
		} catch (ClientProtocolException e) {
			System.err.println("Unable to make connection");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Unable to read file");
			e.printStackTrace();
		} finally {
			
		}
	}
	
public static void doPassed(String url, String fileName, Integer version) throws Exception{
		
        
        try {
			DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
			
			// server back-end URL
			HttpPost httppost = new HttpPost(url);
			
			JSONObject json = new JSONObject();
			json.put("file_id", fileName);
			json.put("revision", version);
			json.put("accept", 1);
			String payload =json.toString();
//			payload = "data=" + payload;
	        StringEntity entity = new StringEntity(payload,
	                ContentType.APPLICATION_FORM_URLENCODED);
	        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httppost.setEntity(entity);

			// execute the request
			HttpResponse response = httpclient.execute(httppost);
			
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity responseEntity = response.getEntity();
			String responseString = EntityUtils.toString(responseEntity, "UTF-8");
			
			System.out.println("Accepted ::: [" + statusCode + "] " + responseString + " :: fileName = " + fileName );
			
		} catch (ClientProtocolException e) {
			System.err.println("Unable to make connection");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Unable to read file");
			e.printStackTrace();
		} finally {
			
		}
	}

public static void doDownloadFiles(String url, String fileName, Integer version) throws Exception{
	
    
    try {
		DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
		
		// server back-end URL
		HttpGet httpget = new HttpGet(url);
		
		JSONObject json = new JSONObject();
		json.put("file_id", fileName);
		json.put("revision", version);
		json.put("accept", 1);
		String payload =json.toString();
		BasicHttpParams httpParams = new BasicHttpParams();
		httpParams.setParameter("file_id", fileName);
		httpParams.setIntParameter("revision", version);
		httpget.setParams(httpParams);

		// execute the request
		HttpResponse response = httpclient.execute(httpget);
		
		int statusCode = response.getStatusLine().getStatusCode();
		HttpEntity responseEntity = response.getEntity();
		String responseString = EntityUtils.toString(responseEntity, "UTF-8");
		
		System.out.println("Accepted ::: [" + statusCode + "] " + responseString + " :: fileName = " + fileName );
		
	} catch (ClientProtocolException e) {
		System.err.println("Unable to make connection");
		e.printStackTrace();
	} catch (IOException e) {
		System.err.println("Unable to read file");
		e.printStackTrace();
	} finally {
		
	}
}
}
