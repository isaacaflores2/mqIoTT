package mqiott.bridge;

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

    public static void sleepThread(int mSec, Logger logger) {
        try {
            Thread.sleep(mSec);
        } catch (InterruptedException ex) {
            logger.error("MqttSleep interrup execption.");
            logger.error(ex.getMessage());
        }
    }
}
