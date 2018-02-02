-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.6.37 - MySQL Community Server (GPL)
-- Server OS:                    Win32
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping structure for table testDB.Agent
CREATE TABLE IF NOT EXISTS `Agent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(50) NOT NULL DEFAULT '0',
  `middleName` varchar(50) NOT NULL DEFAULT '0',
  `lastName` varchar(50) NOT NULL DEFAULT '0',
  `organization` varchar(50) NOT NULL DEFAULT '0',
  `position` varchar(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table testDB.Role
CREATE TABLE IF NOT EXISTS `Role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table testDB.User
CREATE TABLE IF NOT EXISTS `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL DEFAULT '0',
  `password` varchar(200) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=174 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table testDB.UserAgent
CREATE TABLE IF NOT EXISTS `UserAgent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT '0',
  `agent_id` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_UserAgent_User` (`user_id`),
  KEY `FK_UserAgent_Agent` (`agent_id`),
  CONSTRAINT `FK_UserAgent_Agent` FOREIGN KEY (`agent_id`) REFERENCES `Agent` (`id`),
  CONSTRAINT `FK_UserAgent_User` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table testDB.UserRole
CREATE TABLE IF NOT EXISTS `UserRole` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `role_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_UserRole_User` (`user_id`),
  KEY `FK_UserRole_Role` (`role_id`),
  CONSTRAINT `FK_UserRole_Role` FOREIGN KEY (`role_id`) REFERENCES `Role` (`id`),
  CONSTRAINT `FK_UserRole_User` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
