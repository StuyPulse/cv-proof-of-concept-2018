import stuyvision.ModuleRunner;
import stuyvision.gui.VisionGui;

public class Main {
    public static void main(String[] args) {
        ModuleRunner runner = new ModuleRunner(5);
        VisionGui.begin(args, runner);
    }
}
