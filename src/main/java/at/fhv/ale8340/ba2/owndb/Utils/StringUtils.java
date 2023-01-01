package at.fhv.ale8340.ba2.owndb.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtils {
    private final Logger logger = (Logger) LoggerFactory.getLogger(StringUtils.class);


    /**
     * Generates A File Path
     * @param mainPath      {@Link String} Path to the main Directory (absolut or relativ)
     * @param pathelements  these elements will be addet with an / to the main path
     * @return
     */
    public static String pathbuilder(String mainPath, String... pathelements){
        StringBuilder sb = new StringBuilder();
        sb.append(mainPath);
        for(String patElement: pathelements){
            sb.append("/");
            sb.append(patElement);
        }

        return sb.toString();
    }

    /**
     * Generates A File Path with .csv ending
     * @param mainPath      {@Link String} Path to the main Directory (absolut or relativ)
     * @param pathelements  these elements will be addet with an / to the main path
     * @return
     */
    public static String pathbuilderPlusFileEnding(String mainPath, String... pathelements) {

        StringBuilder sb = new StringBuilder();
        sb.append(mainPath);
        for(String patElement: pathelements){
            sb.append("/");
            sb.append(patElement);
        }
        sb.append(".csv");

        return sb.toString();
    }
}
