package experimental.cudarastered;



public class CudaTechnique
{
    public String ptxFileName; 
    public String[] krnlFuncs;
    public boolean[] fetchFromOrig;
    public CudaTechnique(String ptxFileName, String[] krnlFuncs, boolean[] fetchFromOrig)
    {
        this.fetchFromOrig = fetchFromOrig;
        this.ptxFileName = ptxFileName;
        this.krnlFuncs = krnlFuncs;
    }
}
