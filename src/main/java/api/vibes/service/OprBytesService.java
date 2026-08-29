package api.vibes.service;

import java.util.*;
import java.io.File;

/**
 * Service class for managing Bytes.
 */
public class OprBytesService {

    public OprBytesService() {
    }

    /*\/ fins de testes; */
    public static void printFileSize(File file) {
        if (file.exists()) {
            // size of a file (in bytes)
            long bytes = file.length();
            long kilobytes = (bytes / 1024);
            long megabytes = (kilobytes / 1024);
            long gigabytes = (megabytes / 1024);
            long terabytes = (gigabytes / 1024);
            long petabytes = (terabytes / 1024);
            long exabytes = (petabytes / 1024);
            long zettabytes = (exabytes / 1024);
            long yottabytes = (zettabytes / 1024);

            System.out.println(file.getName() + ":");
            System.out.println(String.format("%,d bytes", bytes));
            System.out.println(String.format("%,d kilobytes", kilobytes));
            System.out.println(String.format("%,d megabytes", megabytes));
            System.out.println(String.format("%,d gigabytes", gigabytes));
            System.out.println(String.format("%,d terabytes", terabytes));
            System.out.println(String.format("%,d petabytes", petabytes));
            System.out.println(String.format("%,d exabytes", exabytes));
            System.out.println(String.format("%,d zettabytes", zettabytes));
            System.out.println(String.format("%,d yottabytes", yottabytes));

        } else {
            System.out.println("File does not exist!");
        }
    }

    /*\/ verificar se o tamanho de bytes alcançou N GB(gigabytes); */
    public boolean checarTamanhoBytesGB(long bytes, int maxGB) {
        if (bytes > 0) {
            // size of a file (in bytes)
            // long bytes = file.length();
            long kilobytes = (bytes / 1024);
            long megabytes = (kilobytes / 1024);
            long gigabytes = (megabytes / 1024);
            long terabytes = (gigabytes / 1024);
            long petabytes = (terabytes / 1024);
            long exabytes = (petabytes / 1024);
            long zettabytes = (exabytes / 1024);
            long yottabytes = (zettabytes / 1024);

            if(gigabytes > 0){
                return (gigabytes > maxGB);
            }
        }
        return false;
    }
}
