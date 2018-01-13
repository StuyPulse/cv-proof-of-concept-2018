import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import stuyvision.VisionModule;
import stuyvision.gui.IntegerSliderVariable;


public class Vision extends VisionModule{
	public IntegerSliderVariable minHue = new IntegerSliderVariable("Min Hue", 0, 0, 255);
	public IntegerSliderVariable maxHue = new IntegerSliderVariable("Max Hue", 255, 0, 255);
	
	public IntegerSliderVariable minSaturation = new IntegerSliderVariable("Min Saturation", 0, 0, 255);
	public IntegerSliderVariable maxSaturation = new IntegerSliderVariable("Max Saturation", 255, 0, 255);
	
	public IntegerSliderVariable minValue = new IntegerSliderVariable("Min Value", 0, 0, 255);
	public IntegerSliderVariable maxValue = new IntegerSliderVariable("Max Value", 255, 0, 255);
	
	
	public void run(Mat frame) {
        Mat filtered = filterImage(frame);
        filtered.release();
	}
			
	public Mat filterImage(Mat frame) {
		System.out.println(frame);
		if(hasGuiApp()) {
			postImage(frame, "Image!");
		}
		
		Mat filtered = new Mat();
		Imgproc.cvtColor(frame, filtered, Imgproc.COLOR_BGR2HSV);
		
		ArrayList<Mat> channels = new ArrayList<Mat>();
		Core.split(frame, channels);
		
        Imgproc.medianBlur(channels.get(0), channels.get(0), 5);
        
     // Filter channel by hue
        Core.inRange(channels.get(0), new Scalar(minHue.value()), new Scalar(maxHue.value()), channels.get(0));
        if (hasGuiApp()) {
            postImage(channels.get(0), "Hue-Filtered Frame");
        }
        
        // Dilate then erode
        Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.dilate(channels.get(0), channels.get(0), dilateKernel);

        Mat erodeKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7, 7));
        Imgproc.erode(channels.get(0), channels.get(0), erodeKernel);
        
        // Filter channel by Saturation
        Core.inRange(channels.get(1), new Scalar(minSaturation.value()), new Scalar(maxSaturation.value()), channels.get(1));
        if (hasGuiApp()) {
        		postImage(channels.get(1), "Saturation-Filtered Frame");
        }
        
        // Filter channel Value
        Core.inRange(channels.get(2), new Scalar(minValue.value()), new Scalar(maxValue.value()), channels.get(2));
        if (hasGuiApp()) {
        		postImage(channels.get(2), "Value-Filtered Frame");
        }
        
        // AND the three channels into channels.get(0)
        Core.bitwise_and(channels.get(0), channels.get(1), channels.get(0));
        Core.bitwise_and(channels.get(0), channels.get(2), channels.get(0));
       
        if(hasGuiApp()) {
        postImage(filtered, "Final HSV filtering");
        }


        // Release all Mats created
        for (int i = 0; i < channels.size(); i++) {
            channels.get(i).release();
        }
        dilateKernel.release();
        erodeKernel.release();

        return filtered;
      
	}
}
