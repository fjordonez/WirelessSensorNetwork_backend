# WirelessSensorNetwork_backend

This repository includes the backend implementation of a monitoring framework which combines a Wireless Sensor Network (WSN) and mobile networks, employing a
publish/subscribe mediation platform.

The WNS consists of distributed autonomous sensing devices. This network was based on commercial RFM DM 1810 wireless network nodes to which simple off-the-shelf sensors can be installed. 
The nodes network is configured in a star topology,  where the access point is a base station which is attached to a computer using a USB port (see /docs for more details about the network nodes).

The MQTT publish/subscribe protocol is the middleware communication model employed to connect the wireless sensors with the mobiles devices (Android smartphones). The sensors are the publishers 
of the systems; the produce the information and do not program the messages to be sent directly to specific subscribers, instead they publish their events -when triggered- in one or more available data channels.

This system was tested as a prototype in a real-life life home environment. The purpose was to investigate the application of this technology to monitor the movements and activities of elderly residents living independently, in order to foster their autonomy. The prototype required the use of several event based sensors: infrared IR motion sensors, passing sensors and operation detectors detect were added to the testing environment. Details about such prototype can be found in the paper ´Remote Mobile Monitoring of Wireless Sensor Networks for Ambient Assisted Living´(available in the /docs folder)

The frontend of the system is implemented as an Android app (available at https://github.com/fjordonez/WirelessSensorNetwork_frontend).
