/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqiott.device;

import java.util.*;

import mqiott.configurations.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MqttDeviceManager<T> implements DeviceManager {
    public final static String DEFAULT_STATUS = "No status recieved.";
    private Properties properties;
    private String[] deviceTopics;
    private String[] deviceIDs;
    private Map<String, Device<T>> deviceMap;
    private Map<String, String> deviceTopicToIdMap;
    private Logger logger;


    public MqttDeviceManager() {
        this.properties = null;
        this.deviceTopics = null;
        this.deviceIDs = null;
        this.deviceMap = new HashMap<>();
        this.deviceTopicToIdMap = new HashMap<>();
        this.logger = LoggerFactory.getLogger(MqttDeviceManager.class);

    }

    @Autowired
    public MqttDeviceManager(Properties properties) {
        this.properties = properties;
        this.deviceTopics = null;
        this.deviceIDs = null;
        this.deviceMap = new HashMap<>();
        this.deviceTopicToIdMap = new HashMap<>();
        this.logger = LoggerFactory.getLogger(MqttDeviceManager.class);
        loadDevices(this.properties);
    }

    @Override
    public int numDevices() {
        return deviceMap.size();
    }

    @Override
    public Collection<Device<T>> devices() {
        return new HashSet<>(deviceMap.values());
    }

    @Override
    public void addDevice(Device device) {
        logger.info("Adding a new mqttDevice...");

        if (device == null) {
            throw new RuntimeException("Provide device is null");
        }
        deviceMap.put(device.getId(), device);
    }

    @Override
    public void removeDevice(Device device) {
        logger.info("Removing device...");

        if (device == null) {
            throw new RuntimeException("Provide mqttDevice is null");
        }
        deviceMap.remove(device.getId());
    }

    @Override
    public Device getDevice(String id) {
        logger.info("Getting device with getId: " + id);
        return deviceMap.getOrDefault(id, null);
    }

    @Override
    public Device getDeviceFromTopic(String topic) {
        String id = deviceTopicToIdMap.get(topic);
        return getDevice(id);
    }

    @Override
    public boolean contains(String id) {
        logger.info("Contains mqttDevice with getId: " + id);
        return deviceMap.containsKey(id);
    }

    @Override
    public void loadDevices(Object o) {
        logger.info("Loading new device...");

        //Load device parameters from Config object
        if (o == null)
            throw new IllegalArgumentException("Object is null. Cannot load devices.");

        if (o instanceof Properties)
            this.properties = (Properties) o;
        else
            throw new IllegalArgumentException("Object is not instance of type Config. Cannot load devices.");

        //Load parameters
        this.deviceTopics = properties.mqttTopics;
        this.deviceIDs = properties.deviceIds;

        if (deviceTopics != null) {
            if (deviceTopics.length > 0) {
                for (int i = 0; i < deviceTopics.length; i++) {
                    Device<T> device = new MqttDevice(deviceIDs[i], deviceTopics[i], DEFAULT_STATUS, null);
                    deviceMap.put(device.getId(), device);
                    deviceTopicToIdMap.put(device.getTopic(), device.getId());
                }
            } else
                logger.error("MqttDevice Topics Length is 0");
        }
    }

    @Override
    public T getDeviceData(String id) {
        logger.info("Getting mqttDevice getData for: " + id);

        Device device = getDevice(id);
        if (device == null)
            return null;
        else{
            return (T) device.getData();
        }
    }

    @Override
    public String getDeviceStatus(String id) {
        logger.info("Getting mqttDevice getStatus for: " + id);

        Device device = getDevice(id);
        if (device == null)
            return null;
        else
            return device.getStatus();
    }

    @Override
    public void updateDeviceStatus(String id, String status) {
        logger.info("Update mqttDevice getStatus for: " + id);

        Device device = getDevice(id);
        if (device != null)
            device.setStatus(status);
        else
            throw new IllegalArgumentException("Device with id: " + id + " does not exist.");
    }

    @Override
    public void updateDeviceData(String id, Object data) {
        logger.info("Update mqttDevice getData for: " + id);

        Device device = getDevice(id);
        if (device != null)
            device.setData(data);
        else
            throw new IllegalArgumentException("Device with id: " + id + " does not exist.");
    }

    public String topic(int i) {
        logger.info("Getting topic...");

        if (i <= deviceTopics.length - 1)
            return deviceTopics[i];

        return null;
    }
}
