package io.rastered.app.core;
    
public enum Presets implements SimpleFilterFunction
{
    GAMMA ("Gamma",
        (target,input) -> 
        {
            byte [] orig = target.getSampleFrom();
            byte [] pixels = target.getData();
            float gamma = (float)input[0];

            for (int i=0;i<pixels.length;i++)
            {
                float fRGB = (orig[i]&0xFF)/255.0f;
                fRGB = (float)Math.pow(fRGB, 1.0f / (gamma/100.0f) );
                pixels[i] = (byte)(fRGB*255.0f);
            }

            target.sampleFrom(pixels);

            return target;
        }
    ),

    EXPOSURE ("Exposure",
        (target,input) -> 
        {
            byte [] orig = target.getSampleFrom();
            byte [] pixels = target.getData();
            float exposure = (float)input[0];

            for (int i=0;i<pixels.length;i++)
            {
                float fRGB = (orig[i]&0xFF)/255.0f;
                fRGB *= (exposure/100.0f);
                pixels[i] = (byte)(fRGB*255.0f);
            }

            target.sampleFrom(pixels);

            return target;
        }
    ),
    
    SHARPNESS ("Edge Sharpness",
        (Texture target,Float[] input) -> 
        {
            byte [] orig = target.getSampleFrom();
            byte [] pixels = target.getData();
            float sharpness = (float)input[0];

            int width = target.getWidth();

            for (int i=0;i<pixels.length;i++)
            {
                float fRGB = (orig[i]&0xFF)/255.0f;
                int fetchIdxU = Math.min( width-1, (i%width)+3);
                int fetchIdxV = i/width;

                float fetch = 
                    (orig[fetchIdxV*width+fetchIdxU]&0xFF)/255.0f;
                float mask = fRGB - (fRGB + fetch)*0.5f;
                mask *= 10.0f;
                fRGB += (mask * (sharpness/100.0f));
                fRGB = Texture.normalize(fRGB);
                pixels[i] = (byte)(fRGB*255.0f);
            }

            target.sampleFrom(pixels);

            return target;
        }
    );

    private final SimpleFilterFunction filter;
    private final String userFacingName;
    
    Presets( String userFacingName, SimpleFilterFunction filter ) {
        this.filter = filter; 
        this.userFacingName = userFacingName;
    }
    
    public String getUserFacingName() { return userFacingName; }
    
    public Texture apply( Texture target, Float... input ) {
        return filter.apply(target, input);
    }
}
