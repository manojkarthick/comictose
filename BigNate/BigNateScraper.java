package BigNate;

import org.joda.time.Days;

import java.io.File;
import java.security.CodeSource;
import java.util.HashMap;

/**
 * Created by manojkarthicks on 6/11/2016.
 */
public class BigNateScraper {
    public static int NUM_OF_THREADS = 100;
    public static int noOfDays;
    public static int perThreadDays;
    public static String filesPath;

    public static void startPoint() throws Exception {
        CodeSource codeSource = BigNateScraper.class.getProtectionDomain().getCodeSource();
        File jarFile = new File(codeSource.getLocation().toURI().getPath());
        String jarDir = jarFile.getParentFile().getPath();
        String folderName = "BigNate";
        File file = new File(jarDir + "\\" + folderName);
        file.mkdir();
        filesPath = jarDir + "\\" + folderName; //Change filepath here
        org.joda.time.LocalDate startDate = new org.joda.time.LocalDate(1991, 01, 07);
        org.joda.time.LocalDate endDate = org.joda.time.LocalDate.now();
        System.out.println("Downloading to: " + filesPath);
        System.out.println("\n Downloading. This may take some time depending on your Internet Connection..");
        noOfDays = Days.daysBetween(startDate, endDate).getDays();
        float perThreadDaysFloat = ((float) noOfDays / NUM_OF_THREADS);
        perThreadDays = (int) Math.ceil(perThreadDaysFloat);
        Thread[] threadList = new Thread[NUM_OF_THREADS];
        org.joda.time.LocalDate start = new org.joda.time.LocalDate();
        org.joda.time.LocalDate end = new org.joda.time.LocalDate();
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            if (i == 0) {
                start = startDate;
                end = start.plusDays(perThreadDays);
                threadList[i] = new BigNateThread(i, filesPath, start, end);
            } else if (i > 0 && i < (NUM_OF_THREADS - 1)) {
                start = end.plusDays(1);
                end = start.plusDays(perThreadDays);
                if (end.isAfter(endDate)) {
                    end = endDate;
                    threadList[i] = new BigNateThread(i, filesPath, start, end);
                    continue;
                } else {
                    threadList[i] = new BigNateThread(i, filesPath, start, end);
                }
            } else {
                start = end.plusDays(1);
                end = endDate;
                threadList[i] = new BigNateThread(i, filesPath, start, end);
            }
        }
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            threadList[i].join();
        }
        System.out.println("Download Complete. Happy Reading!");
    }
}
