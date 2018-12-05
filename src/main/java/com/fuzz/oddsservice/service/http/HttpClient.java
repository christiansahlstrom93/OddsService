package com.fuzz.oddsservice.service.http;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClient {
    public String getStats() {
        final DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            //Define a HttpGet request; You can choose between HttpPost, HttpDelete or HttpPut also.
            //Choice depends on type of method you will be invoking.
            final HttpGet getRequest = new HttpGet("http://fuzzstorage.scripter.tv/stats");

            getRequest.addHeader("accept", "application/json");

            final HttpResponse response = httpClient.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + statusCode);
            }

            HttpEntity httpEntity = response.getEntity();
            final String apiOutput = EntityUtils.toString(httpEntity);
            return apiOutput;
        } catch (IOException exception) {
            System.out.println(exception);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }
}
