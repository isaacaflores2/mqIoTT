package garagedoor.bridge;

public interface MqttClientSetup {

    void loadProperties();

    void connectToBroker();

}
