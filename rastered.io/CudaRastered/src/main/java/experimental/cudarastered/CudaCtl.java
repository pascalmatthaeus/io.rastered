package experimental.cudarastered;


public class CudaCtl 
{
    public Cuda cuda;
    public Api api;
    public CudaCtl(CudaTechnique technique)
    {
        api = new Api();
        cuda = new Cuda(api.image.width,api.image.height);
        cuda.currTechnique = technique;
    }
    
    public void filter(int param)
    {
        cuda.launch(cuda.currTechnique, param,api.image.rgb);
        api.image.getStream(cuda.hostOutput);
    }
}
