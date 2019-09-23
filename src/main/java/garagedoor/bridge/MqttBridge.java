package garagedoor.bridge;

import garagedoor.configurations.Properties;
import garagedoor.device.Device;
import garagedoor.device.DeviceManager;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class MqttBridge extends AbstractBridge<String> implements MqttCallback, Bridge, MqttClientSetup {



    @Autowired
    public MqttBridge(Properties properties, DeviceManager deviceManager, MqttBroker mqttBroker) {
        super(properties, deviceManager, mqttBroker);
    }

    @Override
    public void subscribe() {
        logger.info("MqttClient is subscribing...");

        if (!mqttClient.isConnected()) {
            connectToBroker();
        }
        if (!isSubscribed) {
            subscribeToAllTopics();
        }

        logger.info("client has subscribed to all topics.");
    }

    @Override
    public void connectToBroker() {
        logger.info("MqttClient is setting up...");

        try {
            MqttConnectOptions connectionOptions = new MqttConnectOptions();
            connectionOptions.setCleanSession(true);
            connectionOptions.setUserName(mqttBroker.mqttUsername);
            connectionOptions.setPassword(mqttBroker.mqttPassword.toCharArray());
            mqttClient.setCallback(this);
            mqttClient.connect(connectionOptions);

            if (!mqttClient.isConnected()) {
                logger.warn("mqttClient failed to connect to broker.");
            }
        } catch (MqttException e) {
            BridgeUtils.printMqttException(e, "Mqtt Client Setup Exeception.", logger);
        }
    }

    private void subscribeToAllTopics() {
        try {
            for (String topic : topics) {
                mqttClient.subscribe(topic);
            }
            isSubscribed = true;
        } catch (MqttException e) {
            BridgeUtils.printMqttException(e, "Mqtt Client Subcscribe Exeception.", logger);
        }
    }

    public JSONObject publish(String deviceId, String content) {
        logger.info("MqttClient is publishing");
        JSONObject result = new JSONObject();
        if (!mqttClient.isConnected())
            connectToBroker();

        try {
            MqttMessage mqttMsg = new MqttMessage(content.getBytes());
            mqttMsg.setQos(mqttBroker.qos);
            String topic = deviceManager.getDevice(deviceId).getTopic();

            mqttClient.publish(topic, mqttMsg);
            logger.info(" to device id : " + deviceId + " with the message: " + mqttMsg);

            result.put("result", "success");
            return result;
        } catch (MqttException e) {
            BridgeUtils.printMqttException(e, "Mqtt Client Public Exception.", logger);

            result.put("result", "success");
            result.put("error", e.getMessage());
            result.put("errorcode", e.getReasonCode());
            return result;
        }
    }

    @Override
    public void connectionLost(Throwable thrwbl) {
        logger.info("MqttClient connection lost...");

        isSubscribed = false;
        connectToBroker();

        while (!mqttClient.isConnected()) {
            logger.warn("Failed to reconnect...waiting 5 seconds to try again...");
            BridgeUtils.sleepThread(5000, logger);
            connectToBroker();
        }
        subscribe();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        logger.info("Message arrived! Topic: " + topic + " with message: " + message.toString());

        //If message arrived is for a sensor and is not a read request, then update device status
        if (topic.contains(SENSOR_MQTT_TOPIC) && !message.toString().equals(READ_COMMAND)) {
            logger.info("Updating devices that subscribed to: " + topic);

            String id = deviceManager.getDeviceFromTopic(topic).getId();
            deviceManager.updateDeviceData(id, message.toString());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        logger.debug("Message received by the broker!");
    }


    public void start() {
        boolean onStartup = true;
        logger.info("MqttClient is starting!");

        loadProperties();
        connectToBroker();
        subscribe();

        mqttClientLoop();
    }

    private void mqttClientLoop(){
        boolean onStartup = true;
        while (true) {
            //Check if client is not setup
            while (!mqttClient.isConnected()) {
                logger.warn("MqttClient is not setup!");
                connectToBroker();
                BridgeUtils.sleepThread(5000, logger);
            }

            if (!isSubscribed) {
                subscribe();
            }

            if (isSubscribed && onStartup) {
                requestStatusFromAllDevices();
                onStartup = false;
            }
        }
    }

    private void requestStatusFromAllDevices() {
        Set<Device<String>> deviceSet = (Set) deviceManager.devices();
        for (Device<String> device : deviceSet) {
            String id = device.getId();
            publish(id, READ_COMMAND);
        }
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }

}