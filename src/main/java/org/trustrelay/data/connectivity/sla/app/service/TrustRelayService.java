package org.trustrelay.data.connectivity.sla.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.trustrelay.data.connectivity.sla.app.dto.Driver;
import org.trustrelay.data.connectivity.sla.app.enums.OutputType;

import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.trustrelay.data.connectivity.sla.app.util.Constant.CONN;
import static org.trustrelay.data.connectivity.sla.app.util.Constant.DRIVER;

@UtilityClass
public class TrustRelayService {

    public static Driver parseFile(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        Driver driver;
        InputStream is;

        try {
            is = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File '" + filePath + "' not found");
        }

        System.out.println("Parsing file '" + filePath + "'");
        String fileContent = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining(System.lineSeparator()));

        try {
            driver = mapper.readValue(fileContent, Driver.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot map json to object: " + fileContent);
        }

        return driver;
    }

    public static Driver constructDriver(String conn, String driverType) {
        JSONObject json = new JSONObject();
        json.put(DRIVER, driverType);
        json.put(CONN, conn);
        ObjectMapper mapper = new ObjectMapper();
        Driver driver;

        try {
            driver = mapper.readValue(json.toString(), Driver.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot map json to object: " + json);
        }

        return driver;
    }

    public static boolean testConnection(String url, String username, String password) {
        try {
            DriverManager.getConnection(url, username, password);
            return true;
        } catch (SQLException e) {
            System.out.println("Connection to driver failed for reason: " + e.getMessage());
        }
        return false;
    }

    public static void produceOutput(Driver driver, boolean isConnected) {
        if (OutputType.STDOUT.getValue().equals(driver.getType()) ||
                (OutputType.WEBHOOK.getValue().equals(driver.getType()) && driver.getUrl() == null)) {
            String status = isConnected ? "SUCCESS" : "FAILURE";
            System.out.println("Connection status for " + driver.getClass().getSimpleName() + ": "  + status);
        } else {
            httpGet(driver);
        }
    }

    public static String parseUrl(String url, String param) {
        Pattern pattern = Pattern.compile(param);
        return pattern.matcher(url)
                .results()
                .map(mr -> mr.group(1))
                .findFirst()
                .orElse(null);
    }

    private void httpGet(Driver driver) {
        HttpGet request = new HttpGet(driver.getUrl());
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

            int status = response.getStatusLine().getStatusCode();
            System.out.println("Http GET to URL '" + driver.getUrl() + "' returned with status " + status);
        } catch (IOException e) {
            System.out.println("Http GET to URL '" + driver.getUrl() + "' failed with error: " + e.getMessage());
        }
    }
}
