package garagedoor.bridge;

import garagedoor.configurations.Properties;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqttBroker {
    public int qos;
    public String broker;
    public String clientId;
    public String mqttUsername;
    public String mqttPassword;
    public MemoryPersistence persistence;

    @Autowired
    public MqttBroker(Properties properties) {
        qos = 2;
        persistence = new MemoryPersistence();
        loadConfigurationParameters(properties);
    }

    private void loadConfigurationParameters(Properties properties) {
        broker = properties.mqttBrokerAddress;
        clientId = properties.mqttClientId;
        mqttUsername = properties.mqttBrokerUsername;
        mqttPassword = properties.mqttBrokerPassword;
    }

}
