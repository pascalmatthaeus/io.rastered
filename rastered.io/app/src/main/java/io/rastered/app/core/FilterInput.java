package io.rastered.app.core;

import java.util.HashMap;

public class FilterInput 
{
    private HashMap <String, Texture> textures;
    private HashMap <String, Float> parameters;
    
    public FilterInput( HashMap<String, Texture> textures,
        HashMap <String, Float> parameters )
    {
        this.textures = textures;
        this.parameters = parameters;
    }
    
    protected HashMap<String, Texture> getTextures() { return textures; }
    
    protected HashMap<String, Float> getParameters() { return parameters; }
}
