package garagedoor.MqttHttpsBridge;

import org.eclipse.paho.client.mqttv3.MqttException;

import org.slf4j.Logger;

public class BridgeUtils {

    public static String generateClientId(String clientId) {
        return clientId + System.nanoTime();
    }

    public static void printMqttException(MqttException e, String msg, Logger logger) {
        logger.error("Mqtt Exception: " + msg + "MqttException Details: "
                + e.getMessage() + " Reason: " + e.getReasonCode() + " loc " + e.getLocalizedMessage()
                + " cause " + e.getCause());
        e.printStackTrace();
    }
}
