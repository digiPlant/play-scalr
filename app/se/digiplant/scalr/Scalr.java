package se.digiplant.scalr;

import se.digiplant.scalr.api.Resizer;
import java.io.File;
import play.libs.*;

public class Scalr {

    /**
     * Creates and caches image in local cache directory
     * @param path The path to where the images are stored
     * @param file The filePath relative to the path variable (the same as the play Assets Controller)
     * @param width The width of the frame that we want the image to fit within
     * @param height The height of the frame that we want the image to fit within
     * @param mode Any of the Scale modes such as AUTOMATIC, FIT_TO_WIDTH, FIT_TO_HEIGHT
     * @param method Any of the Scalr Methods, The standard is the highest possible
     * @return a File if everything when well
     */
    public static File get(String path, String file, int width, int height, Resizer.Mode mode, Resizer.Method method) {
        return Scala.orNull(se.digiplant.scalr.api.Scalr.get(path, file, width, height, mode, method));
    }

    public static File get(String path, String file, int width, int height, Resizer.Mode mode) {
        return get(path, file, width, height, mode, Resizer.Method.ULTRA_QUALITY);
    }

    public static File get(String path, String file, int width, int height) {
        return get(path, file, width, height, Resizer.Mode.AUTOMATIC);
    }

    public static File get(String path, String file, int width) {
        return get(path, file, width, 0);
    }

    /**
     *
     * @param fileuid The unique play-res file identifier
     * @param source The play-res source name
     * @param width The width of the frame that we want the image to fit within
     * @param height The height of the frame that we want the image to fit within
     * @param mode Any of the Scale modes such as AUTOMATIC, FIT_TO_WIDTH, FIT_TO_HEIGHT
     * @param method Any of the Scalr Methods, The standard is the highest possible
     * @return a File if everything when well
     */
    public static File getRes(String fileuid, String source, int width, int height, Resizer.Mode mode, Resizer.Method method) {
        return Scala.orNull(se.digiplant.scalr.api.Scalr.getRes(fileuid, source, width, height, mode, method));
    }

    public static File getRes(String fileuid, String source, int width, int height, Resizer.Mode mode) {
        return getRes(fileuid, source, width, height, mode, Resizer.Method.ULTRA_QUALITY);
    }

    public static File getRes(String fileuid, String source, int width, int height) {
        return getRes(fileuid, source, width, height, Resizer.Mode.AUTOMATIC);
    }

    public static File getRes(String fileuid, String source, int width) {
        return getRes(fileuid, source, width, 0);
    }
}
