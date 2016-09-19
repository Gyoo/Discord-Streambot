-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu1
-- http://www.phpmyadmin.net
--
-- Client :  localhost
-- Généré le :  Mer 24 Août 2016 à 09:10
-- Version du serveur :  5.7.13-0ubuntu0.16.04.2
-- Version de PHP :  7.0.8-0ubuntu0.16.04.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `streambot`
--
CREATE DATABASE IF NOT EXISTS `streambot` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `streambot`;

-- --------------------------------------------------------

--
-- Structure de la table `channel`
--

DROP TABLE IF EXISTS `channel`;
CREATE TABLE IF NOT EXISTS `channel` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ServerID` bigint(20) NOT NULL,
  `Name` mediumtext,
  `PlatformID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `ServerID` (`ServerID`),
  KEY `PlatformID` (`PlatformID`)
) ENGINE=MyISAM AUTO_INCREMENT=10691 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `command`
--

DROP TABLE IF EXISTS `command`;
CREATE TABLE IF NOT EXISTS `command` (
  `CommandID` bigint(20) NOT NULL AUTO_INCREMENT,
  `Name` mediumtext NOT NULL,
  PRIMARY KEY (`CommandID`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Vider la table avant d'insérer `command`
--

TRUNCATE TABLE `command`;
--
-- Contenu de la table `command`
--

INSERT INTO `command` (`CommandID`, `Name`) VALUES
(1, 'Add'),
(2, 'Remove');

-- --------------------------------------------------------

--
-- Structure de la table `donators`
--

DROP TABLE IF EXISTS `donators`;
CREATE TABLE IF NOT EXISTS `donators` (
  `UserId` bigint(20) NOT NULL,
  `Amount` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `game`
--

DROP TABLE IF EXISTS `game`;
CREATE TABLE IF NOT EXISTS `game` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ServerID` bigint(20) NOT NULL,
  `Name` mediumtext NOT NULL,
  `PlatformID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `ServerID` (`ServerID`),
  KEY `PlatformID` (`PlatformID`)
) ENGINE=MyISAM AUTO_INCREMENT=1339 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `guild`
--

DROP TABLE IF EXISTS `guild`;
CREATE TABLE IF NOT EXISTS `guild` (
  `ServerID` bigint(20) NOT NULL,
  `ChannelID` bigint(20) NOT NULL,
  `isCompact` tinyint(1) NOT NULL DEFAULT '1',
  `isActive` tinyint(1) DEFAULT NULL,
  `Cleanup` tinyint(4) NOT NULL,
  PRIMARY KEY (`ServerID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `manager`
--

DROP TABLE IF EXISTS `manager`;
CREATE TABLE IF NOT EXISTS `manager` (
  `ServerID` bigint(20) NOT NULL,
  `UserID` bigint(20) NOT NULL,
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ID`),
  KEY `ServerID` (`ServerID`)
) ENGINE=MyISAM AUTO_INCREMENT=11017 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `notification`
--

DROP TABLE IF EXISTS `notification`;
CREATE TABLE IF NOT EXISTS `notification` (
  `ServerID` bigint(20) NOT NULL,
  `UserID` bigint(20) NOT NULL,
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ID`),
  KEY `ServerID` (`ServerID`)
) ENGINE=MyISAM AUTO_INCREMENT=862 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `permission`
--

DROP TABLE IF EXISTS `permission`;
CREATE TABLE IF NOT EXISTS `permission` (
  `PermissionID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ServerID` bigint(20) NOT NULL,
  `RoleID` bigint(20) NOT NULL,
  `CommandID` bigint(20) NOT NULL,
  `Level` int(11) NOT NULL,
  PRIMARY KEY (`PermissionID`),
  KEY `ServerID` (`ServerID`),
  KEY `CommandID` (`CommandID`)
) ENGINE=MyISAM AUTO_INCREMENT=998 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `platform`
--

DROP TABLE IF EXISTS `platform`;
CREATE TABLE IF NOT EXISTS `platform` (
  `PlatformID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` mediumtext,
  PRIMARY KEY (`PlatformID`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Vider la table avant d'insérer `platform`
--

TRUNCATE TABLE `platform`;
--
-- Contenu de la table `platform`
--

INSERT INTO `platform` (`PlatformID`, `Name`) VALUES
(1, 'Twitch');

-- --------------------------------------------------------

--
-- Structure de la table `queueitem`
--

DROP TABLE IF EXISTS `queueitem`;
CREATE TABLE IF NOT EXISTS `queueitem` (
  `ServerID` bigint(20) NOT NULL,
  `UserID` bigint(20) NOT NULL,
  `Command` mediumtext,
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ID`),
  KEY `ServerID` (`ServerID`)
) ENGINE=MyISAM AUTO_INCREMENT=99 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `stream`
--

DROP TABLE IF EXISTS `stream`;
CREATE TABLE IF NOT EXISTS `stream` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ServerID` bigint(20) NOT NULL,
  `PlatformID` int(11) NOT NULL,
  `Channel_name` mediumtext NOT NULL,
  `Stream_title` mediumtext CHARACTER SET utf8mb4,
  `Game_name` mediumtext,
  `MessageId` longtext,
  PRIMARY KEY (`ID`),
  KEY `ServerID` (`ServerID`),
  KEY `PlatformID` (`PlatformID`)
) ENGINE=MyISAM AUTO_INCREMENT=330619 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `tag`
--

DROP TABLE IF EXISTS `tag`;
CREATE TABLE IF NOT EXISTS `tag` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ServerID` bigint(20) NOT NULL,
  `Name` mediumtext,
  PRIMARY KEY (`ID`),
  KEY `ServerID` (`ServerID`)
) ENGINE=MyISAM AUTO_INCREMENT=508 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `team`
--

DROP TABLE IF EXISTS `team`;
CREATE TABLE IF NOT EXISTS `team` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ServerID` bigint(20) NOT NULL,
  `Name` mediumtext,
  `PlatformID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `ServerID` (`ServerID`),
  KEY `PlatformID` (`PlatformID`)
) ENGINE=MyISAM AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
