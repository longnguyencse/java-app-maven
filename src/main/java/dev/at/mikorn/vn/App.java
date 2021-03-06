package dev.at.mikorn.vn;

import dev.at.mikorn.vn.configuration.ConfigParams;
import dev.at.mikorn.vn.model.QcModel;
import dev.at.mikorn.vn.model.builder.QcModelBuilder;
import dev.at.mikorn.vn.service.IfpAPI;
import dev.at.mikorn.vn.utils.UploadFiles;
import dev.at.mikorn.vn.service.implement.IfpApiService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Author Mikorn vietnam
 * Created on 05-Jun-18.
 */

public class App {
    private static Logger logger = LogManager.getLogger(App.class);
    private final static String PATH_CONFIG_FILE ="./conf/bootstrap.conf";
    private HashMap<String, String> config;
    private static int numberZipFile = 0;
    private static int iLimitFile ;
    private static String configContent;
    private final static String URL_SERVER = String.format("%s:%s",IfpAPI.SERVER_CONFIG.IFP.getUrlEndPoit(),
            IfpAPI.SERVER_CONFIG.IFP.getPort());


    public static void main(String[] args) {

        logger.info(String.format("Currency work space: %s", System.getProperty("user.dir")));
        App app = new App();
        app.fetchProperties();

        iLimitFile = NumberUtils.toInt(app.config.get(ConfigParams.LIMIT_ZIP_FILE.getParam()), 0);

        if (null != args && args[0].equalsIgnoreCase("downloading")) {
            logger.info("Task downloadingggggggggggggg +++++++++++++++++++++++++++");
            // if downloading
            // create file name, call api, exit :D
            app.triggerDownloading();

            //return
            return;
        }
        logger.info("Task Uppppppploadingggggggggggggg +++++++++++++++++++++++++++");
        String fileNameOriginalZip = app.randomFileName();

        List<File> files = new ArrayList<>();
        app.getFileInDirectory(new File(app.config.get(ConfigParams.FOLDER_DATA.getParam())),
                files);
        boolean isCreateZipFolder = app.isCreateNewFolder(app.config.get(ConfigParams
                .ZIP_FOLDER
                .getParam()));
        if (!isCreateZipFolder) {
            logger.info(String.format("Don't create zip folder: %s, return",
                    app.config.get(ConfigParams
                            .ZIP_FOLDER
                            .getParam())));
            return;
        }
        // we have a list file
        // zip file have number frame in config
        configContent = app.config.get(ConfigParams.IS_DIFFERENT_CONTENT.getParam());

        try {
            app.zipFileHelper(fileNameOriginalZip, files);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /***/
        /**System.out.println("Press Any Key To Continue...");
        new Scanner(System.in).nextLine();*/
    }

    private void triggerDownloading() {
        for (int i = 0; i < iLimitFile; i++) {
            logger.info(String.format("File number: %s", numberZipFile));
            String fileName = randomFileName();
            logger.info(String.format("File name: %s", fileName));

            new IfpApiService(config.get(ConfigParams.URL_PROP.getParam())).loadLabeledDataWithRevision(fileName, 1).subscribe(
              result -> {
                  logger.info(String.format("Success: %s", result.toString()));
              },
                    throwable -> {
                        logger.error(String.format("Fail, e: %s", throwable.getMessage()));
                    },
                    () -> {
                        logger.info("TODO: NEXT");
                    }
            );

            numberZipFile += 1;
        }
    }

    /**
     *
     * @param path
     * @return
     */
    private boolean isCreateNewFolder(String path) {
        File dir = new File(path);

        if (!dir.exists()) {
            boolean successful = dir.mkdir();
            if (successful)
            {
                // creating the directory succeeded
                logger.info("directory was created successfully");
            }
            else
            {
                // creating the directory failed
                logger.info("failed trying to create the directory");
            }
            return successful;
        }

        return true;
    }

    /**
     *
     * @param file
     * @param listFiles
     * @throws IOException
     */
    private void zipFileHelper(String file, List<File> listFiles) throws IOException {
        final String filePath = String.format("%s//%s", config.get(ConfigParams.ZIP_FOLDER.getParam())
                , file);

        if (StringUtils.isBlank(file) || listFiles.size() < 1) {
            logger.info("File name or list file is invaild, end");
            return;
        }
        int numberFrame = NumberUtils.toInt(config.get(ConfigParams.NUMBER_FRAMES.getParam()), 0);
        if (numberFrame == 0) {
            logger.error("Number frame is invalid");
            return;
        }
        if (iLimitFile != 0 && numberZipFile > iLimitFile) {
            return;
        }
        File fileZip = new File(filePath);

        if (fileZip.exists()) {
            boolean isDelete = fileZip.delete();
            if (!isDelete) {
                logger.info("Ten file da trung, xin dat lai ten khac -- prefix-file-create");
                return;
            }
            logger.info("Delete old file, create new file -- prefix-file-create");
        }
        // list contain file in zip file
        List<File> fileinZip = new ArrayList<>();

        if (listFiles.size() > numberFrame) {
            FileOutputStream fos = new FileOutputStream(filePath);
            ZipOutputStream zipOut = new ZipOutputStream(fos);

            for (int i = 0; i < numberFrame; i ++) {
                File fileToZip;
                if (configContent.equals("1")) {
                    fileToZip = new File(listFiles.get(0).getPath());
                    fileinZip.add(listFiles.get(0));
                } else {
                    Random random = new Random();
//                    int index = random.nextInt(listFiles.size()- 1);
                    fileToZip = new File(listFiles.get(i).getPath());
                    fileinZip.add(listFiles.get(i));
                }
               // File fileToZip = new File(listFiles.get(0).getPath());
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
                // remove file
                // can create number file fowlling config
                if (configContent.equals("1")) {
                    listFiles.remove(0);
                }
            }
            zipOut.close();
            fos.close();
            // if number zip file > limit file
            // return
            numberZipFile += 1;
            logger.info(String.format("Number of file: %s", numberZipFile));
            logger.info(String.format("filePath: %s", filePath));
            logger.info(String.format("file name: %s", file));

            // upload server by using API
            // ++ call retrofit service
            uploadOriginalFile(filePath, fileinZip);

            // recursive call
            // ++ create new file name
            String newFile = randomFileName();
            // ++ loop create zip file
            zipFileHelper(newFile, listFiles);
        } else {
            FileOutputStream fos = new FileOutputStream(filePath);
            ZipOutputStream zipOut = new ZipOutputStream(fos);

            // all file remaining
            for (int i =0; i < listFiles.size(); i ++) {
                File fileToZip = new File(listFiles.get(i).getPath());
                fileinZip.add(listFiles.get(i));
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
            // upload server by using api
        }
    }

    /**
     * create file name
     * @return
     */
    private String randomFileName() {
        // file name = prefix-file + _number.
        Random random = new Random();
//        String fileNameOriginalZip = String.format("Original_file_%s_%s_%s.zip",System.currentTimeMillis(),
//                random.nextInt(1000), random.nextInt(100));

//        return fileNameOriginalZip;
        return String.format("%s_%s.zip", config.get(ConfigParams.PREFIX_FILE.getParam()), numberZipFile);
    }

    private void getFileInDirectory(File filePath, List<File> files) {
        if (null == filePath) {
            return;
        }
        if (filePath.isFile()) {
            // todo with file
            //logger.info("file" + filePath);
            files.add(filePath);
            //files.remove(filePath);
            return;
        }
        for (File fileEx: filePath.listFiles()) {
            getFileInDirectory(fileEx, files);
            //logger.info(String.format("Size file: %s", files.size()));
        }
    }

    /**
     * Get properties
     */
    private void fetchProperties() {
        // Create and load default properties
        Properties defaultProps = new Properties();
        try {
            FileInputStream in = new FileInputStream(PATH_CONFIG_FILE);
            defaultProps.load(in);
            config = new HashMap<>();

            config.put(ConfigParams.FOLDER_DATA.getParam(),
                    defaultProps.getProperty(ConfigParams.FOLDER_DATA.getParam()));
            config.put(ConfigParams.NUMBER_FRAMES.getParam(),
                    defaultProps.getProperty(ConfigParams.NUMBER_FRAMES.getParam()));

            config.put(ConfigParams.ZIP_FOLDER.getParam(),
                    defaultProps.getProperty(ConfigParams.ZIP_FOLDER.getParam()));

            config.put(ConfigParams.IS_DIFFERENT_CONTENT.getParam(),
                    defaultProps.getProperty(ConfigParams.IS_DIFFERENT_CONTENT.getParam()));

            config.put(ConfigParams.LIMIT_ZIP_FILE.getParam(),
                    defaultProps.getProperty(ConfigParams.LIMIT_ZIP_FILE.getParam()));

            config.put(ConfigParams.URL_PROP.getParam(),
                    defaultProps.getProperty(ConfigParams.URL_PROP.getParam()));

            config.put(ConfigParams.PREFIX_FILE.getParam(),
                    defaultProps.getProperty(ConfigParams.PREFIX_FILE.getParam()));
            /*
            defaultProps.entrySet().forEach(objectObjectEntry -> {
                logger.info(objectObjectEntry.getKey() + " : " + objectObjectEntry.getValue());
            });
            */
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use api 3.1 : upload original file (.zip)
     * @param path
     * @param listInZip
     * @throws IOException
     */
    private void uploadOriginalFile(String path, List<File> listInZip) throws IOException {
        // create upload service client
        logger.info(String.format("uploadOriginalFile"));

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
        // Files.copy(file.toPath(), file.toPath());
        // finally, execute the request
        new IfpApiService(config.get(ConfigParams.URL_PROP.getParam())).saveFile(description, body).subscribe(result -> {
            System.out.println("result: " + result.getMessage());
        }, throwable -> {
            logger.error(String.format("fail, e %s", throwable.getMessage()));
        }, () -> { // next
            System.out.println("Upload original file success --> Do next");

            // upload annotated file
            try {
                uploadAnnotatedFile(file, listInZip);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }

    /**
     * 3.10: upload annotated file
     * @param file
     * @param fileInZip
     * @throws IOException
     */
    private void uploadAnnotatedFile(File file, List<File> fileInZip) throws IOException {
        String filePath = ".\\data\\annotated\\clone-labbed-file";
        String filePathGtt = ".\\data\\annotated\\IMG_4513_01680.gtt";

        if (null == fileInZip || fileInZip.size() ==0 ) {
            return;
        }
        boolean isCreateFolder = isCreateNewFolder(filePath);
        if (!isCreateFolder) {
            logger.info(String.format("Do not create folder : %s", filePath));
            // return, out flow
            return;
        }
        // if zip have data
        for (File frame: fileInZip) {
            // file
            // create frame file with sample data
            String origFrameName = FilenameUtils.getBaseName(frame.getName());
            String pathFrame = String.format("%s\\%s.zip", filePath, origFrameName);
            File frameFile = new File(pathFrame);
            if (!frameFile.exists()) {
                File source = new File(filePathGtt);
                Files.copy(source.toPath(), frameFile.toPath());
            }

            UploadFiles.uploadMutiFiles(frameFile.getAbsolutePath(),
                    config.get(ConfigParams.URL_PROP.getParam()) + "/files/save_frame_layer"
                    , frame.getName(), file.getName(), 0);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }

        }

        // --> 3.11 --> 3.13 --> 3.14
        try {
            doAnnotated(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 3.13
        makeCheckingFileAnnotated(file, 1);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 3.14
        /** try {
            doUploadFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 3.11: checking
     * @param file
     * @throws Exception
     */
    private void doAnnotated(File file) throws Exception {
        UploadFiles.doAnnotated(config.get(ConfigParams.URL_PROP.getParam())+ "/files/annotated"
                , file.getName(), 0);
    }

    /**
     * 3.13
     * Mark a file|image with revision is annotated
     */
    private void makeCheckingFileAnnotated(File file, int accept) {
        QcModel qcModel = QcModelBuilder.aQcModel().withAccept(accept)
                .withFileId(file.getName())
                .withRevision(0)
                .build();
        new IfpApiService(config.get(ConfigParams.URL_PROP.getParam())).qcChecking(qcModel).subscribe(
                result -> {
                    logger.info("result: " + result.getMessage());
                }, throwable -> {
                    logger.info("fail");
                }, () -> { // next
                    logger.info("Upload original file success --> Do next");
                    try {
                        doUploadFile(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    /**
     * 3.14: qc check passed
     */
    private void doUploadFile(File file) throws Exception {
        UploadFiles.doPassed(config.get(ConfigParams.URL_PROP.getParam())+ "/files/qc_accept_annotated",
                file.getName(), 0);

        // do check download file test
        Thread thread = new Thread(() -> {
            // 2.6 download
            new IfpApiService(config.get(ConfigParams.URL_PROP.getParam())).loadLabeledDataWithRevision(file.getName(), 0).subscribe(
                    result -> { logger.info("result: " + result.getMessage());
                    },
                    throwable -> {logger.info(String.format("Fail : %s", throwable.getMessage()));
                    },
                    () -> {logger.info("Downlaod trigger file success --> Do next");
                    }
            );
        });
       // thread.start();
    }

}
