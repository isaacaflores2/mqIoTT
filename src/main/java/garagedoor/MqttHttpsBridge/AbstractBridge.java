package garagedoor.MqttHttpsBridge;

import garagedoor.Configurations.Properties;
import garagedoor.iot.device.DeviceManager;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractBridge<T> implements Bridge, MqttClientSetup {

    protected Properties properties;
    protected DeviceManager<T> deviceManager;
    protected MqttBroker mqttBroker;
    protected MqttClient mqttClient;
    protected boolean isClientSetup = false;
    protected boolean isSubscribed = false;
    protected String[] topics;
    protected Logger logger;
    protected String lineSeperator;

    public AbstractBridge() {
        mqttClient = null;
        isClientSetup = false;
        isSubscribed = false;
        mqttBroker = null;
        topics = null;
        deviceManager = null;
        properties = null;
        logger = LoggerFactory.getLogger(MqttBridge.class);
        lineSeperator = System.lineSeparator();
    }

    @Autowired
    public AbstractBridge(Properties properties, DeviceManager deviceManager, MqttBroker mqttBroker) {
        this();
        this.properties = properties;
        this.deviceManager = deviceManager;
        this.mqttBroker = mqttBroker;
    }

    public abstract void start();

    public abstract void subscribe();

    public abstract String publish(String deviceId, String content);

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void loadProperties() {
        logger.info("Loading config files...");

        if (properties == null) {
            logger.error("Config is null! " + lineSeperator
                    + "Check your application properties files for requried MqttBridgeClient configuration parameters:" + lineSeperator
                    + "1) mqttTopics 2) mqttClientId 3) mqttBrokerUsername 4) MqttBrokerPassword 5) mqttBrokerAddress");
            return;
        }

        logger.info("Properties broker value: " + properties.mqttBrokerAddress);
        topics = properties.mqttTopics;

        try {
            mqttClient = new MqttClient(mqttBroker.broker, BridgeUtils.generateClientId(mqttBroker.clientId), mqttBroker.persistence);
        } catch (MqttException e) {
            BridgeUtils.printMqttException(e, "Mqtt Client Setup Exeception! ", logger);
        }
    }

    public abstract void connectToBroker();

}
