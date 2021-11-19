
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

__device__ float blurBox(int radius, float2 xy, int axis)
{
    float2 pxSize = make_float2(fConstants[0],fConstants[1]);
    float out = tex2D(tex,xy.x,xy.y);
    float count = 1.0f;

    for (float i=1.5f;i<(float)radius;i+=2.0f)
    {
        out+=tex2D(tex,xy.x+pxSize.x*i*(1-axis),xy.y+pxSize.y*i*axis);
        out+=tex2D(tex,xy.x-pxSize.x*i*(1-axis),xy.y-pxSize.y*i*axis);
        count+=2.0f;
    }

    out/=count;
    return out;
}

// +++ KERNEL FUNCTIONS +++

extern "C"
{
    __global__ void blurH(float *out, int param)
    {
        int i = blockIdx.x * blockDim.x + threadIdx.x;
        int n = iConstants[2];
        int u = iConstants[0];
        int v = iConstants[1];
        float2 pxSize = make_float2(fConstants[0],fConstants[1]);
        float x = (float)(i%u)/u;
        float y = ((int)i/u)/(float)v;  // cut x indices
        float2 xy = make_float2(x,y);

        out[i] = blurBox(param,xy,0);

        if (out[i]>1.0f) out[i]=1.0f;
    }

    __global__ void blurV(float *out, int param)
    {
        int i = blockIdx.x * blockDim.x + threadIdx.x;
        int n = iConstants[2];
        int u = iConstants[0];
        int v = iConstants[1];
        float2 pxSize = make_float2(fConstants[0],fConstants[1]);
        float x = (float)(i%u)/u;
        float y = ((int)i/u)/(float)v;  // cut x indices
        float2 xy = make_float2(x,y);

        out[i] = blurBox(param,xy,1);

        if (out[i]>1.0f) out[i]=1.0f;
    }
}
