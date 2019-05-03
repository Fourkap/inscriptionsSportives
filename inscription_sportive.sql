-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Feb 21, 2019 at 10:17 PM
-- Server version: 5.7.19
-- PHP Version: 5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `inscription sportive`
--

-- --------------------------------------------------------

--
-- Table structure for table `candidat`
--

DROP TABLE IF EXISTS `candidat`;
CREATE TABLE IF NOT EXISTS `candidat` (
  `id_ca` int(11) NOT NULL AUTO_INCREMENT,
  `nom_ca` varchar(50) DEFAULT NULL,
  `prenom_ca` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_ca`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `competition`
--

DROP TABLE IF EXISTS `competition`;
CREATE TABLE IF NOT EXISTS `competition` (
  `id_co` int(11) NOT NULL AUTO_INCREMENT,
  `nom_com` varchar(50) DEFAULT NULL,
  `date_fin` datetime DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `id_ca` int(11) NOT NULL,
  PRIMARY KEY (`id_co`),
  KEY `id_ca` (`id_ca`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `equipe`
--

DROP TABLE IF EXISTS `equipe`;
CREATE TABLE IF NOT EXISTS `equipe` (
  `id_e` int(11) NOT NULL AUTO_INCREMENT,
  `nom_e` varchar(50) DEFAULT NULL,
  `id_ca` int(11) NOT NULL,
  PRIMARY KEY (`id_e`),
  KEY `id_ca` (`id_ca`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `personne`
--

DROP TABLE IF EXISTS `personne`;
CREATE TABLE IF NOT EXISTS `personne` (
  `id_p` int(11) NOT NULL AUTO_INCREMENT,
  `prenom_p` varchar(50) DEFAULT NULL,
  `nom_p` varchar(50) DEFAULT NULL,
  `mail_p` varchar(100) DEFAULT NULL,
  `adresse` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id_p`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
