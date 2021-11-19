package experimental.cudarastered;

import static jcuda.driver.JCudaDriver.*;
import jcuda.*;
import jcuda.driver.*;
import static jcuda.driver.CUaddress_mode.*;
import static jcuda.driver.CUarray_format.*;
import static jcuda.driver.CUfilter_mode.*;
import static jcuda.driver.CUmemorytype.*;


public class Cuda 
{
    public CudaTechnique currTechnique;
    
    public int imageWidth;
    public int imageHeight;
    
    public CUcontext context;
    public CUmodule module;
    public CUfunction[] function;
    public boolean[] fetchFromOrig;
    
    private CUdeviceptr deviceOutput;
    
    private final int blockSizeX = 256;
    private int gridSizeX;
    
    private CUtexref tex;
    private CUarray arr;
    private CUDA_MEMCPY2D copyArr;
    
    private Pointer kernelParameters;
    private float [] rgb;
    public float [] hostOutput;
    
    public Cuda(int imageWidth, int imageHeight)
    {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }
    
    
    public void init()
    {
        JCudaDriver.setExceptionsEnabled(true);
        cuInit(0);
        CUdevice device = new CUdevice();
        cuDeviceGet(device,0);
        if (context==null){context = new CUcontext(); System.out.println("creating new context");}
        module = new CUmodule();
        cuCtxCreate(context,0,device);
        gridSizeX = (int)Math.ceil((double)rgb.length / blockSizeX);
        
        setupCtx();
    }
    
    public void initTechnique(String ptxFileName, String[] technique, boolean[] fetchOrig)
    {
        cuModuleLoad(module,ptxFileName);
        function = new CUfunction[technique.length];
        fetchFromOrig = new boolean[technique.length];
        for (int i=0; i<technique.length; i++)
        {
            function[i] = new CUfunction();
            cuModuleGetFunction(function[i],module,technique[i]);
            fetchFromOrig[i] = fetchOrig[i];
        }
    }
    
   
    
    private void setupCtx()
    {
        initTechnique(currTechnique.ptxFileName,currTechnique.krnlFuncs,currTechnique.fetchFromOrig);
        System.out.println("switching context");
        tex = new CUtexref();
        arr = new CUarray();
        
        CUDA_ARRAY_DESCRIPTOR ad = new CUDA_ARRAY_DESCRIPTOR();
        ad.Format = CU_AD_FORMAT_FLOAT;
        ad.Width = imageWidth;
        ad.Height = imageHeight;
        ad.NumChannels = 1;
        
        cuArrayCreate(arr,ad);
        copyArr = new CUDA_MEMCPY2D();
        copyArr.srcMemoryType = CU_MEMORYTYPE_HOST;
        copyArr.dstMemoryType = CU_MEMORYTYPE_ARRAY;
        copyArr.srcHost = Pointer.to(rgb);
        copyArr.dstArray = arr;
        copyArr.WidthInBytes = ad.Width*Sizeof.FLOAT;
        copyArr.Height = ad.Height;
        
        cuModuleGetTexRef(tex,module,"tex");
        cuTexRefSetFilterMode(tex, CU_TR_FILTER_MODE_LINEAR);
        cuTexRefSetAddressMode(tex, 0, CU_TR_ADDRESS_MODE_CLAMP);
        cuTexRefSetAddressMode(tex, 1, CU_TR_ADDRESS_MODE_CLAMP);
        cuTexRefSetFormat(tex, CU_AD_FORMAT_FLOAT, 1);
        cuTexRefSetFlags(tex, CU_TRSF_NORMALIZED_COORDINATES);
        cuTexRefSetArray(tex, arr, CU_TRSA_OVERRIDE_FORMAT);
        
        cuMemcpy2D(copyArr);
        
        // Get GPU constants pointer, copy constants HtoD
        CUdeviceptr iConstMemPtr = new CUdeviceptr();
        CUdeviceptr fConstMemPtr = new CUdeviceptr();
        long iConstMemSizeArr[] = {0};
        long fConstMemSizeArr[] = {0};
        cuModuleGetGlobal(iConstMemPtr,iConstMemSizeArr,module,"iConstants");
        cuModuleGetGlobal(fConstMemPtr,fConstMemSizeArr,module,"fConstants");
        int iConstMemSize = (int) iConstMemSizeArr[0];
        int fConstMemSize = (int) fConstMemSizeArr[0];
        int[] iHostData = new int[]{imageWidth, imageHeight,rgb.length};
        float[] fHostData = new float[]{1.0f/imageWidth,1.0f/imageHeight};
        cuMemcpyHtoD(iConstMemPtr,Pointer.to(iHostData),iConstMemSize);
        cuMemcpyHtoD(fConstMemPtr,Pointer.to(fHostData),fConstMemSize);
        
        // Copy input, allocate GPU mem for output
        hostOutput = new float[rgb.length];
        deviceOutput = new CUdeviceptr();
        cuMemAlloc(deviceOutput, rgb.length * Sizeof.FLOAT);
    }
    
    public void launch(CudaTechnique technique, int param, float[] rgbInput)
    {
        rgb = rgbInput;
        if (context==null)
        {
            currTechnique = technique;
            init();
        }
        if (currTechnique != technique)
        {
            System.out.println("currentTechnique != technique. Re- setting up context.");
            currTechnique = technique;
            cuTexRefDestroy(tex);
            cuArrayDestroy(arr);
            cuModuleUnload(module);
            cuMemFree(deviceOutput);
            
            setupCtx();
        }
        cuCtxSetCurrent(context);
        System.out.println(Pointer.to(context));
        System.out.println(Thread.currentThread().getId());
        
        kernelParameters = Pointer.to(
            Pointer.to(deviceOutput),
            Pointer.to(new int[]{param})
        );
        
        for (int i=0;i<function.length;i++)
        {
            if (!fetchFromOrig[i])
            {
                copyArr.srcHost = Pointer.to(hostOutput);
                cuMemcpy2D(copyArr);
            }
            
            cuLaunchKernel(function[i],
            gridSizeX,  1, 1,      // Grid dimension
            blockSizeX, 1, 1,      // Block dimension
            0, null,               // Shared memory size and stream
            kernelParameters, null // Kernel- and extra parameters
            );
            
            cuCtxSynchronize();
            
            cuMemcpyDtoH(Pointer.to(hostOutput), deviceOutput,
                rgb.length * Sizeof.FLOAT);
        }
        
        copyArr.srcHost = Pointer.to(rgb);
        cuMemcpy2D(copyArr);
        
        //cuCtxDestroy(context);
        //context=null;
    }
}
