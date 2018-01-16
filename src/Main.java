import java.io.File;

import org.opencv.core.Mat;

import stuyvision.ModuleRunner;
import stuyvision.capture.DeviceCaptureSource;
import stuyvision.capture.ImageCaptureSource;
import stuyvision.gui.VisionGui;

public class Main {
    public static void main(String[] args) {
        ModuleRunner runner = new ModuleRunner(5);
        processImages(runner);
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

    public static void processImages(ModuleRunner runner) {
        String imagesDirectory = "/pics/";

        // System.out.println("Getting images from " + imagesDirectory);

        File[] imgs = getFiles(imagesDirectory);
        Mat frame = new Mat();

        /*
        for (int i = 0; i < imgs.length && i < 3; i++) {
            String path = System.getProperty("user.dir") + imagesDirectory + imgs[i].getName();
            System.out.println(imgs[i].getName() + " - " + path);
            Vision v = new Vision();

            ImageCaptureSource img = new ImageCaptureSource(path);
            img.readFrame(frame);

            v.run(frame);
            runner.addMapping(img, v);
        }
        */
        String path = System.getProperty("user.dir") + imagesDirectory + "row.png";
        Vision v = new Vision();

        ImageCaptureSource img = new ImageCaptureSource(path);
        img.readFrame(frame);

        v.run(frame);
        runner.addMapping(img, v);
    }

    public static void processCamera(ModuleRunner runner) {
        DeviceCaptureSource camera = new DeviceCaptureSource(0);
        Mat frame = new Mat();
        camera.readFrame(frame);

        Vision v = new Vision();
        runner.addMapping(camera, v);
    }

}
