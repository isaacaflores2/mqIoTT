package mqiott.device;

public interface Device<T> {

    String getId();

    String getTopic();

    String getStatus();

    T getData();

    void setId(String id);

    void setTopic(String topic);

    void setStatus(String status);

    void setData(T data);
}
