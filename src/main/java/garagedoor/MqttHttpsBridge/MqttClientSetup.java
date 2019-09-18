package garagedoor.MqttHttpsBridge;

public interface MqttClientSetup {

    public void loadProperties();

    public void connectToBroker();

}
