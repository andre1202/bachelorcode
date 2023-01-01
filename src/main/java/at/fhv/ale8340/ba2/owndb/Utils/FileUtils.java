package at.fhv.ale8340.ba2.owndb.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Timestamp;

public class FileUtils {
    private final Logger logger = (Logger) LoggerFactory.getLogger(FileUtils.class);

    public static File getFileByTimeStamp(String mainPath, Timestamp timestamp){

        String year = String.valueOf(timestamp.toLocalDateTime().getYear());
        String month = String.valueOf(timestamp.toLocalDateTime().getMonth().getValue());
        String day = String.valueOf(timestamp.toLocalDateTime().getDayOfMonth());
        String pathToFile = StringUtils.pathbuilderPlusFileEnding(mainPath, year, month, day);
        File file = null;

        try {
            file = new File(pathToFile);
            if(file.exists()){
                return file;
            }else{
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            System.out.println(file.exists());
        } catch(Exception e) {
            e.printStackTrace();
        }
//        System.out.println("File: " + file);
        return file;
    }
}
