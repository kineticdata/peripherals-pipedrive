package com.kineticdata.bridgehub.adapter.pipedrive;

import com.kineticdata.bridgehub.adapter.BridgeError;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a Rest service helper.
 */
public class PipedriveApiHelper {
    private static final Logger LOGGER = 
        LoggerFactory.getLogger(PipedriveApiHelper.class);
    
    private final String baseUrl;
    
    public PipedriveApiHelper(String baseUrl) {
        
        this.baseUrl = baseUrl;
    }
    
    public JSONObject executeRequest (String path) throws BridgeError{
        
        String url = baseUrl + path;
        JSONObject output;      
        // System time used to measure the request/response time
        long start = System.currentTimeMillis();
        
        try (
            CloseableHttpClient client = HttpClients.createDefault()
        ) {
            HttpResponse response;
            HttpGet get = new HttpGet(url);
            
            response = client.execute(get);
            LOGGER.debug("Recieved response from \"{}\" in {}ms.",
                url,
                System.currentTimeMillis()-start);

            int responseCode = response.getStatusLine().getStatusCode();
            LOGGER.trace("Request response code: " + responseCode);
            
            HttpEntity entity = response.getEntity();
            
            // Confirm that response is a JSON object
            output = parseResponse(EntityUtils.toString(entity));
            
            // Handle all other faild repsonses
            if (responseCode >= 400) {
                handleFailedReqeust(responseCode);
            }
        }
        catch (IOException e) {
            throw new BridgeError(
                "Unable to make a connection to the Azure service server.", e);
        }
        
        return output;
    }
    
    private void handleFailedReqeust (int responseCode) throws BridgeError {
        switch (responseCode) {
            case 400:
                throw new BridgeError("400: Bad Reqeust");
            case 401:
                throw new BridgeError("401: Unauthorized");
            case 404:
                throw new BridgeError("404: Page not found");
            case 405:
                throw new BridgeError("405: Method Not Allowed");
            case 500:
                throw new BridgeError("500 Internal Server Error");
            default:
                throw new BridgeError("Unexpected response from server");
        }
    }
        
    private JSONObject parseResponse(String output) throws BridgeError{
        
        JSONObject responseObj = new JSONObject();
        try {
            responseObj = (JSONObject)JSONValue.parseWithException(output);
        } catch (ParseException e){
            // Assume all 200 responses will be JSON format.
            LOGGER.error("There was a parse exception with the response", e);
        } catch (Exception e) {
            throw new BridgeError("An unexpected error has occured ", e);
        }
        
        return responseObj;
    }
}
