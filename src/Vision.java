import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import stuyvision.VisionModule;
import stuyvision.gui.IntegerSliderVariable;

public class Vision extends VisionModule {
    public IntegerSliderVariable minLightness = new IntegerSliderVariable("Min Lightness", 120, 0, 255);
    public IntegerSliderVariable maxLightness = new IntegerSliderVariable("Max Lightness", 255, 0, 255);

    public IntegerSliderVariable minA = new IntegerSliderVariable("Min A", 86, 0, 255);
    public IntegerSliderVariable maxA = new IntegerSliderVariable("Max A", 132, 0, 255);

    public IntegerSliderVariable minB = new IntegerSliderVariable("Min B", 157, 0, 225);
    public IntegerSliderVariable maxB = new IntegerSliderVariable("Max B", 225, 0, 225);

    public void run(Mat frame) {
        Mat filtered = filterImage(frame);
        drawCube(frame, filtered);
        filtered.release();
    }

    public Mat filterImage(Mat frame) {
        if (hasGuiApp()) {
            postImage(frame, "Image!");
        }

        Mat filtered = new Mat();
        Imgproc.cvtColor(frame, filtered, Imgproc.COLOR_BGR2Lab);

        ArrayList<Mat> channels = new ArrayList<Mat>();
        Core.split(filtered, channels);

        if (hasGuiApp()) {
            postImage(filtered, "Lightness Before Change Frame");
        }

        Imgproc.medianBlur(channels.get(0), channels.get(0), 5);

        // Filter channel by lightness
        Core.inRange(channels.get(0), new Scalar(minLightness.value()), new Scalar(maxLightness.value()),
                channels.get(0));
        if (hasGuiApp()) {
            postImage(channels.get(0), "Lightness-Filtered Frame");
        }

        // Dilate then erode
        Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7, 7));
        Imgproc.dilate(channels.get(0), channels.get(0), dilateKernel);

        Mat erodeKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7, 7));
        Imgproc.erode(channels.get(0), channels.get(0), erodeKernel);

        // Filter channel by a
        Core.inRange(channels.get(1), new Scalar(minA.value()), new Scalar(maxA.value()), channels.get(1));
        if (hasGuiApp()) {
            postImage(channels.get(1), "A-Filtered Frame");
        }

        // Filter channel b
        Core.inRange(channels.get(2), new Scalar(minB.value()), new Scalar(maxB.value()), channels.get(2));
        if (hasGuiApp()) {
            postImage(channels.get(2), "B-Filtered Frame");
        }

        // AND the three channels into channels.get(0)
        Core.bitwise_and(channels.get(0), channels.get(1), filtered);
        Core.bitwise_and(filtered, channels.get(2), filtered);

        Mat dilateKernelF = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7, 7));
        Imgproc.dilate(filtered, filtered, dilateKernelF);

        Mat erodeKernelF = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7, 7));
        Imgproc.erode(filtered, filtered, erodeKernelF);

        if (hasGuiApp()) {
            postImage(filtered, "Final Lab filtering");
        }

        Mat dest = new Mat();
        frame.copyTo(dest, filtered);
        if (hasGuiApp()) {
            postImage(dest, "Masked");
        }

        // Release all Mats created
        for (int i = 0; i < channels.size(); i++) {
            channels.get(i).release();
        }
        dilateKernel.release();
        erodeKernel.release();

        return filtered;

    }

    public void drawCube(Mat original, Mat filtered) {
        Mat drawn = original.clone();
        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(filtered, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f approxCurve = new MatOfPoint2f();

        Imgproc.drawContours(drawn, contours, -1, new Scalar(0, 255, 0), 1);

        if (hasGuiApp()) {
            postImage(drawn, "Detected");
        }

        for (int i = 0; i < contours.size(); i++) {
            contours.get(i).release();
        }

        approxCurve.release();
        drawn.release();
    }
}
