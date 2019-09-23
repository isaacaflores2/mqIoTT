package mqiott.bridge;
import org.json.JSONObject;


public interface Bridge<T> {

    void subscribe();
    JSONObject publish(String deviceId, String content);
    boolean isSubscribed();

}
