package mqiott.configurations;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Properties {
    @Value("${mqiott.bridge.mqtttopics:empty}")
    public String[] mqttTopics;

    @Value("${mqiott.bridge.deviceids:empty}")
    public String[] deviceIds;

    @Value("${mqiott.bridge.numdevices:empty}")
    public String numDevices;

    //MQTT Broker Arguments
    @Value("${mqiott.bridge.mqttbrokeraddress:empty}")
    public String mqttBrokerAddress; // "ssl://ipaddres:port or "ssl://url:port "

    @Value("${mqiott.bridge.mqttclientid:empty}")
    public String mqttClientId;

    @Value("${mqiott.bridge.mqttusername:empty}")
    public String mqttBrokerUsername;

    @Value("${mqiott.bridge.mqttpassword:empty}")
    public String mqttBrokerPassword;
}
