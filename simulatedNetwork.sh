
java -cp ./lib/log4j-1.2.9.jar:./lib/mysql-connector-java-5.1.13-bin.jar:./lib/wmqtt.jar:./wifisensornetwork.jar wfsnrnet.WifiSensorNetwork ./config/SensorList.xml ./config/EventHandler_MQTT.xml ./config/EventListener_MQTT.xml ./config/TopicList.xml &
java -cp ./lib/log4j-1.2.9.jar:./lib/mysql-connector-java-5.1.13-bin.jar:./lib/wmqtt.jar:./wifisensornetwork.jar wfsnrnet.SimulatedNetwork &

