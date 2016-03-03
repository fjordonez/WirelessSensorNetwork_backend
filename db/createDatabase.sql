DROP DATABASE IF EXISTS WifiSensorNetwork;

CREATE DATABASE WifiSensorNetwork;

USE WifiSensorNetwork;

CREATE TABLE `Sensors` (  
  `sensorID` int(10) NOT NULL ,
  `placeID` varchar(255) NOT NULL,
  `sensorAlias` int(10) NOT NULL,
  `hardwareID` varchar(255) NOT NULL,
  `sensorType` enum('KitchenHeat', 'Toilet', 'BathroomDoor', 'Kitchen', 'KitchenStorage', 'Outside', 'SleepDoor', 'Sleep', 'Bathroom', 'Novalue'),
  `sensorPlace` varchar(255),
  `inverted` bool,
  PRIMARY KEY (sensorID)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

CREATE TABLE `SensorEvents` (
  `eventID` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `sensorID` varchar(255) NOT NULL,
  `value` int,
  `timestamp` bigint,
  `date` datetime NOT NULL,
  PRIMARY KEY (eventID)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

CREATE TABLE `SensorData` (
  `dataID` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `sensorID` varchar(255) NOT NULL,
  `sensorType` enum('KitchenHeat', 'Toilet', 'BathroomDoor', 'Kitchen', 'KitchenStorage', 'Outside', 'SleepDoor', 'Sleep', 'Bathroom', 'Novalue'),
  `sensorPlace` varchar(255),
  `value` int,
  `timestamp` bigint,
  `date` datetime NOT NULL,
  PRIMARY KEY (dataID)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
