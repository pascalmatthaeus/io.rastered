package io.rastered.app.core;

import java.io.BufferedReader;
import java.io.StringReader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.json.*;

public class FilterParameters 
{
    protected int [] parameters;
    
    public int [] get() { return parameters; }
    
    public FilterParameters(HttpServletRequest request)
    {
        String requestBody;
        try (BufferedReader requestReader = request.getReader())
        {
            var requestBodyStringBuilder = new StringBuilder();
            String line;
            while ((line = requestReader.readLine()) != null)
            {
                requestBodyStringBuilder.append(line);
            }
            requestBody = requestBodyStringBuilder.toString();
        
            try (var reqStringReader = new StringReader(requestBody);
                var jsonReader = Json.createReader(reqStringReader);)
            {
                JsonArray jsonParamList = 
                    jsonReader.readObject().getJsonArray("params");
                int amountParameters = jsonParamList.size();
                
                parameters = new int [amountParameters];
                for (int i=0;i<amountParameters;i++)
                {
                    parameters[i] = Integer.parseInt(jsonParamList
                        .asJsonArray()
                        .getString(i));
                }
            } catch(Exception e) 
            { 
                System.out.println(
                    "Error parsing parameters from json request body!"
                );
                e.printStackTrace(); 
            } 
        } catch(Exception e)
        {
            System.out.println(
                "Error reading from HTTP request input stream!"
            );
            e.printStackTrace();
        }
    }
}
