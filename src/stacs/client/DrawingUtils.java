package stacs.client;

public class DrawingUtils {
    
    private static double c1 = 1 / Math.sqrt(6);
    private static double r3 = Math.sqrt(3);
    private static double r2 = Math.sqrt(2);
    
    public static int transformX(double x, double y, double z, double scale, double trans){
        double val = r3 * x + -r3 * z;
        return (int)(c1 * val * scale + trans);
    }
    
    public static int transformY(double x, double y, double z, double scale, double trans){
        double val = x + 2 * y + z;
        return (int)(c1 * val * scale + trans);
    }
    
    public static int transformZ(double x, double y, double z, double scale, double trans){
        double val = x - y +  z;
        return (int)(c1 * r2 * val * scale + trans);
    }
}
