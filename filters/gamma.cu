
// +++ CONSTANTS +++

__device__ __constant__ int iConstants[3];
__device__ __constant__ float fConstants[2];

// +++ TEXTURES +++

texture<float, 2, cudaReadModeElementType> tex;

// +++ HELPERS +++

__device__ float2 saturate(float2 f)
{
    if (f.x > 1.0f) f.x = 1.0f;
    if (f.y > 1.0f) f.y = 1.0f;
    if (f.x < 0.0f) f.x = 0.0f;
    if (f.y < 0.0f) f.y = 0.0f;
    return f;
}

__device__ float lerp(float p1, float p2, float i)
{
    return p2*i+p1*(1.0f-i);
}


// +++ KERNEL FUNCTIONS +++

extern "C"
{
    __global__ void gammaPow(float *out, int param)
    {
        int i = blockIdx.x * blockDim.x + threadIdx.x;
        int n = iConstants[2];
        int u = iConstants[0];
        int v = iConstants[1];
        float2 pxSize = make_float2(fConstants[0],fConstants[1]);
        float x = (float)(i%u)/u;
        float y = ((int)i/u)/(float)v;  // cut x indices
        float2 xy = make_float2(x,y);

        out[i] = powf(tex2D(tex,xy.x,xy.y),1.0f/((float)param/100.0f));

        if (out[i]>1.0f) out[i]=1.0f;
    }
}
