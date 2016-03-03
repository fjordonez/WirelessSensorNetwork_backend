#!/bin/bash

#./stop
echo "Starting components..."
echo "  Starting core module"
java -cp ./lib/log4j-1.2.9.jar:./lib/mysql-connector-java-5.1.13-bin.jar:./lib/wmqtt.jar:./wifisensornetwork.jar wfsnrnet.WifiSensorNetwork ./config/SensorList.xml ./config/EventHandler_MQTT.xml ./config/EventListener_MQTT.xml ./config/TopicList.xml &
echo "  Starting sensor network interface"
java -cp ./lib/log4j-1.2.9.jar:./lib/mysql-connector-java-5.1.13-bin.jar:./lib/wmqtt.jar:./wifisensornetwork.jar wfsnrnet.SensorNetworkInterface ./config/SensorNetworkModule_MQTT.xml ./config/CommandListener_MQTT.xml ./config/TopicList.xml /dev/ttyUSB0 &
#echo "  Starting Last values handler"
#java -cp ./lib/log4j-1.2.9.jar:./lib/mysql-connector-java-5.1.13-bin.jar:./lib/wmqtt.jar:./wifisensornetwork.jar wfsnrnet.LastValuesPublisher ./config/SensorList.xml ./config/LastValuesPublisher_MQTT.xml ./config/LastValuesPublisherConfig.xml ./config/TopicList.xml &
