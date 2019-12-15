package diploma;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FileUtil {
    public static void writeToFile(byte[] bytes, String dest){

        try {

            File file = new File(dest);
            // Initialize a pointer
            // in file using OutputStream
            OutputStream
                    os
                    = new FileOutputStream(file);

            // Starts writing the bytes in it
            os.write(bytes);
            // Close the file
            os.close();
        }

        catch (Exception e) {
            System.out.println("Exception: " + e);
        }

    }

}
