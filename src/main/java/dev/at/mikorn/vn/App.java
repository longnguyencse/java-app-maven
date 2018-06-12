package dev.at.mikorn.vn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Author Mikorn vietnam
 * Created on 05-Jun-18.
 */

public class App {
    private static Logger logger = LogManager.getLogger(App.class);


    public static void main(String[] args) {

        logger.info(String.format("Currency work space: %s", System.getProperty("user.dir")));


        App app = new App();
//        app.getFileInDirectory(new File("D:\\learn\\data\\example"));
        app.fetchProperties();

        /**System.out.println("Press Any Key To Continue...");
        new Scanner(System.in).nextLine();**/
    }

    private void getFileInDirectory(File filePath) {
        if (null == filePath) {
            return;
        }
        if (filePath.isFile()) {
            // todo with file
            logger.info("file" + filePath);
            return;
        }
        for (File fileEx: filePath.listFiles()) {
            getFileInDirectory(fileEx);
        }
    }

    private void fetchProperties() {
        // Create and load default properties
        Properties defaultProps = new Properties();
        try {
            FileInputStream in = new FileInputStream("./conf/bootstrap.conf");
            defaultProps.load(in);
            defaultProps.entrySet().forEach(objectObjectEntry -> {
                logger.info(objectObjectEntry.getKey() + " : " + objectObjectEntry.getValue());
            });

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
