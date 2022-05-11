package org.trustrelay.data.connectivity.sla.app;

import org.trustrelay.data.connectivity.sla.app.dto.Driver;
import org.trustrelay.data.connectivity.sla.app.service.TrustRelayService;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.trustrelay.data.connectivity.sla.app.util.Constant.*;

public class Main {

    public static void main(String[] args) {
        Map<String, String> params = Arrays.stream(args)
                .map(arg -> arg.split("="))
                .collect(Collectors.toMap(array -> array[0], array -> array[1]));

        Driver driver;
        if (params.containsKey(FILE)) {
            driver = TrustRelayService.parseFile(params.get(FILE));
        } else if (params.containsKey(CONN) && params.containsKey(DRIVER)) {
            driver = TrustRelayService.constructDriver(params.get(CONN), params.get(DRIVER));
        } else {
            System.out.println("Please provide arguments -file or -conn and -driver");
            return;
        }

        driver.extractCredentials();
        driver.setDefault();
        boolean isConnected = driver.testConnection();
        TrustRelayService.produceOutput(driver, isConnected);
    }

}