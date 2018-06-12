package dev.at.mikorn.vn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * http://www.baeldung.com/java-compress-and-uncompress
 * Author Mikorn vietnam
 * Created on 12-Jun-18.
 */

public class ZipMultipleFiles {

    public static void main(String[] args) throws IOException {
        List<String> srcFiles = Arrays.asList("X:/ifp/tool-test-nifi/src/main/resources/test1.txt",
                "X:/ifp/tool-test-nifi/src/main/resources/text2.txt");
        FileOutputStream fos = new FileOutputStream("multiCompressed.zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        System.out.println(System.getProperty("user.dir"));
        for (String srcFile : srcFiles) {
            File fileToZip = new File(srcFile);
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
        zipOut.close();
        fos.close();
    }
}
