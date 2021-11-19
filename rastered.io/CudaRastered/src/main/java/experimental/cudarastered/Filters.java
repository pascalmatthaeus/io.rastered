package experimental.cudarastered;

public class Filters 
{
    public static CudaTechnique boxBlur = new CudaTechnique(
            "/home/pascal/Documents/boxBlur.ptx", 
            new String[]{
                "blurH",
                "blurV"},
            new boolean[]{
                true,
                false});
    
    public static CudaTechnique exposure = new CudaTechnique(
            "/home/pascal/Documents/exposure.ptx", 
            new String[]{
                "exposure"},
            new boolean[]{
                true});
    
    public static CudaTechnique gamma = new CudaTechnique(
            "/home/pascal/Documents/gamma.ptx", 
            new String[]{
                "gammaPow"},
            new boolean[]{
                true});
}
