#!/bin/bash

echo "Stopping components"

#kill -9 `ps aux | grep "wfsnrnet.LastValuesPublisher" | grep -v grep | awk '{ print $2 }'`
kill -9 `ps aux | grep "wfsnrnet.SensorNetworkInterface" | grep -v grep | awk '{ print $2 }'`
kill -9 `ps aux | grep "wfsnrnet.WifiSensorNetwork" | grep -v grep | awk '{ print $2 }'`
#sudo rm /var/lock/LCK..ttyUSB0
