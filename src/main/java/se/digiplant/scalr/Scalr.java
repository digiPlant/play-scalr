package se.digiplant.scalr;

import java.io.File;

import static play.libs.Scala.orNull;

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
    public static File get(String path, String file, int width, int height, org.imgscalr.Scalr.Mode mode, org.imgscalr.Scalr.Method method) {
        return orNull(se.digiplant.scalr.api.Scalr.get(path, file, width, height, mode, method, play.api.Play.unsafeApplication()));
    }

    public static File get(String path, String file, int width, int height, org.imgscalr.Scalr.Mode mode) {
        return get(path, file, width, height, mode, org.imgscalr.Scalr.Method.ULTRA_QUALITY);
    }

    public static File get(String path, String file, int width, int height) {
        return get(path, file, width, height);
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
    public static File getResource(String fileuid, String source, int width, int height, org.imgscalr.Scalr.Mode mode, org.imgscalr.Scalr.Method method) {
        return orNull(se.digiplant.scalr.api.Scalr.getResource(fileuid, source, width, height, mode, method, play.api.Play.unsafeApplication()));
    }

    public static File getResource(String fileuid, String source, int width, int height, org.imgscalr.Scalr.Mode mode) {
        return get(fileuid, source, width, height, mode, org.imgscalr.Scalr.Method.ULTRA_QUALITY);
    }

    public static File getResource(String fileuid, String source, int width, int height) {
        return getResource(fileuid, source, width, height);
    }

    public static File getResource(String fileuid, String source, int width) {
        return getResource(fileuid, source, width, 0);
    }
}
