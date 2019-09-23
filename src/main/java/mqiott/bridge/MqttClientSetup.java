package mqiott.bridge;

public interface MqttClientSetup {

    void loadProperties();

    void connectToBroker();

}
