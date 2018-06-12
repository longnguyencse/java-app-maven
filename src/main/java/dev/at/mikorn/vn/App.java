package dev.at.mikorn.vn;

import dev.at.mikorn.vn.configuration.ConfigParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Author Mikorn vietnam
 * Created on 05-Jun-18.
 */

public class App {
    private static Logger logger = LogManager.getLogger(App.class);
    private static final String PATH_CONFIG_FILE ="./conf/bootstrap.conf";
    private HashMap<String, String> config;

    public static void main(String[] args) {

        logger.info(String.format("Currency work space: %s", System.getProperty("user.dir")));

        Random random = new Random();
        /** for (int i =0 ; i < 1000; i++) {
            String fileNameOriginalZip = String.format("Original_file_%s_%s_%s",System.currentTimeMillis(),
                    random.nextInt(1000), random.nextInt(100));
            logger.info(String.format("File generate: %s", fileNameOriginalZip));
        }*/

        String fileNameOriginalZip = String.format("Original_file_%s_%s_%s.zip",System.currentTimeMillis(),
                random.nextInt(1000), random.nextInt(100));

        App app = new App();
        app.fetchProperties();

        List<File> files = new ArrayList<>();
        app.getFileInDirectory(new File(app.config.get(ConfigParams.FOLDER_DATA.getParam())),
                files);
        app.isCreateFolderZip();
        // we have a list file
        // zip file have number frame in config
        try {
            app.zipFileHelper(fileNameOriginalZip, files);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**System.out.println("Press Any Key To Continue...");
        new Scanner(System.in).nextLine();**/
    }

    private boolean isCreateFolderZip() {
        File dir = new File(config.get(ConfigParams
                .ZIP_FOLDER
                .getParam()));
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
        if (listFiles.size() > numberFrame) {
            FileOutputStream fos = new FileOutputStream(filePath);
            ZipOutputStream zipOut = new ZipOutputStream(fos);

            for (int i = 0; i < numberFrame; i ++) {
                File fileToZip = new File(listFiles.get(0).getPath());
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
                listFiles.remove(0);
            }
            zipOut.close();
            fos.close();
            // upload server by using API
            // ++ call retrofit service



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

    private String randomFileName() {
        Random random = new Random();
        String fileNameOriginalZip = String.format("Original_file_%s_%s_%s.zip",System.currentTimeMillis(),
                random.nextInt(1000), random.nextInt(100));
        return fileNameOriginalZip;
    }

    private void createZipFile() {

    }

    private void getFileInDirectory(File filePath, List<File> files) {
        if (null == filePath) {
            return;
        }
        if (filePath.isFile()) {
            // todo with file
            logger.info("file" + filePath);
            files.add(filePath);
            //files.remove(filePath);
            return;
        }
        for (File fileEx: filePath.listFiles()) {
            getFileInDirectory(fileEx, files);
            logger.info(String.format("Size file: %s", files.size()));
        }
    }

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
}
