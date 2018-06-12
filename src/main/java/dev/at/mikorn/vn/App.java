package dev.at.mikorn.vn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

/**
 * Author Mikorn vietnam
 * Created on 05-Jun-18.
 */

public class App {
    private static Logger logger = LogManager.getLogger(App.class);


    public static void main(String[] args) {

        logger.info(System.getProperty("user.dir"));

        // Create and load default properties
        Properties defaultProps = new Properties();
        try {
            FileInputStream in = new FileInputStream("./src/main/resources/conf/bootstrap.conf");
            defaultProps.load(in);
            defaultProps.entrySet().forEach(objectObjectEntry -> {
                logger.info(objectObjectEntry.getKey() + " : " + objectObjectEntry.getValue());
            });

            in.close();
            // create application properties with default
            Properties applicationPros = new Properties(defaultProps);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Press Any Key To Continue...");
        new Scanner(System.in).nextLine();
    }

}
