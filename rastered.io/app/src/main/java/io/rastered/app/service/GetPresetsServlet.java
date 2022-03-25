package io.rastered.app.service;

import io.rastered.app.core.Presets;
import java.io.IOException;
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
        var jsonFilterListBuilder = Json.createObjectBuilder();
        Arrays.stream( Presets.values() )
            .forEachOrdered( (filterPreset) -> {
                jsonFilterListBuilder.add( filterPreset.name(),
                    filterPreset.getUserFacingName()
                );
            }
        );
        jsonFilterList = jsonFilterListBuilder.build().toString();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, 
        HttpServletResponse response) throws ServletException, IOException 
    {
        response.getWriter().print( jsonFilterList );
    }
}
