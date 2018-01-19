import java.io.File;

import org.opencv.core.Mat;

import stuyvision.ModuleRunner;
import stuyvision.capture.DeviceCaptureSource;
import stuyvision.capture.ImageCaptureSource;
import stuyvision.gui.VisionGui;

public class Main {
    public static void main(String[] args) {
        ModuleRunner runner = new ModuleRunner(5);
        // processCubeImages(runner);
        processLineImages(runner);
        // processCamera(runner);
        VisionGui.begin(args, runner);
    }

    public static File[] getFiles(String path) {
        path = System.getProperty("user.dir") + path;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            path = path.replace('/', '\\');
        }
        File directory = new File(path);
        File[] directoryListing = directory.listFiles();

        return directoryListing;
    }

    public static void processCubeImages(ModuleRunner runner) {
        String imagesDirectory = "/pics/";

        // System.out.println("Getting images from " + imagesDirectory);

        File[] imgs = getFiles(imagesDirectory);
        Mat frame = new Mat();

        /* for (int i = 0; i < imgs.length && i < 3; i++) {
            String path = System.getProperty("user.dir") + imagesDirectory + imgs[i].getName();
            System.out.println(imgs[i].getName() + " - " + path);
            CubeVision v = new CubeVision();

            ImageCaptureSource img = new ImageCaptureSource(path);
            img.readFrame(frame);

            v.run(frame);
            runner.addMapping(img, v);
        }*/

        String path = System.getProperty("user.dir") + imagesDirectory + "row.png";
        CubeVision v = new CubeVision();

        ImageCaptureSource img = new ImageCaptureSource(path);
        img.readFrame(frame);

        v.run(frame);
        runner.addMapping(img, v);
    }

    public static void processLineImages(ModuleRunner runner) {
        String imagesDirectory = "/pics/line/";

        // System.out.println("Getting images from " + imagesDirectory);

        File[] imgs = getFiles(imagesDirectory);
        Mat frame = new Mat();

        for (int i = 0; i < imgs.length && i < 4; i++) {
            String path = System.getProperty("user.dir") + imagesDirectory + imgs[i].getName();
            System.out.println(imgs[i].getName() + " - " + path);
            LineVision v = new LineVision();

            ImageCaptureSource img = new ImageCaptureSource(path);
            img.readFrame(frame);

            v.run(frame);
            runner.addMapping(img, v);
        }

    }

    public static void processCamera(ModuleRunner runner) {
        DeviceCaptureSource camera = new DeviceCaptureSource(0);
        Mat frame = new Mat();
        camera.readFrame(frame);

        CubeVision v = new CubeVision();
        runner.addMapping(camera, v);
    }

}
