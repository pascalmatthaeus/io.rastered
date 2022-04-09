package io.rastered.app.service;

import io.rastered.app.core.Presets;
import java.util.Arrays;
import jakarta.json.Json;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetPresetsServlet extends HttpServlet 
{
    protected String jsonFilterList;
    
    @Override
    public void init()
    {
        var jsonPresetBuilder = Json.createObjectBuilder();
        var jsonPresetArrayBuilder = Json.createArrayBuilder();
        
        Arrays.stream( Presets.values() )
            .forEachOrdered( (filterPreset) -> {
                jsonPresetBuilder.add(
                        "internalName", filterPreset.name()
                ).add(
                        "friendlyName", filterPreset.getUserFacingName()
                );
                jsonPresetArrayBuilder.add( jsonPresetBuilder.build() );
            }
        );
        
        jsonFilterList = jsonPresetArrayBuilder.build().toString();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, 
        HttpServletResponse response) throws ServletException
    {
        try ( var responseWriter = response.getWriter() ) {
            responseWriter.print( jsonFilterList );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
