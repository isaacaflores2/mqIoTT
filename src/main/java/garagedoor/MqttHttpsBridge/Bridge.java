package garagedoor.MqttHttpsBridge;
import org.json.JSONObject;


public interface Bridge<T> {

    public void subscribe();
    public JSONObject publish(String deviceId, String content);
    public boolean isSubscribed();

}
