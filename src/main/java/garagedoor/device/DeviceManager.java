package garagedoor.device;

import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
public interface DeviceManager<T> {
    int numDevices();

    Collection <Device<T>> devices();

    void addDevice(Device<T> device);

    void removeDevice(Device<T> device);

    Device<T> getDevice(String id);

    Device<T> getDeviceFromTopic(String topic);

    boolean contains(String id);

    void loadDevices(Object o);

    T getDeviceData(String id);

    String getDeviceStatus(String id);

    void updateDeviceStatus(String id, String status);

    void updateDeviceData(String id, T data);

}
