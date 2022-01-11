-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- 主机： 127.0.0.1:3308
-- 生成日期： 2021-05-31 10:00:14
-- 服务器版本： 8.0.18
-- PHP 版本： 7.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `chat`
--

-- --------------------------------------------------------

--
-- 表的结构 `friend`
--

DROP TABLE IF EXISTS `friend`;
CREATE TABLE IF NOT EXISTS `friend` (
  `ID` varchar(8) NOT NULL,
  `Friend` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Remark_Name` varchar(20) DEFAULT NULL,
  `Record` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`ID`,`Friend`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- 转存表中的数据 `friend`
--

INSERT INTO `friend` (`ID`, `Friend`, `Remark_Name`, `Record`) VALUES
('10219873', '66666666', '猫猫', 'zjy_mewo'),
('10219873', '88888888', '黄子昂', 'zjy_hza'),
('88888888', '10219873', '曾嘉怡', 'zjy_hza'),
('66666666', '10219873', '曾嘉怡', 'zjy_mewo');

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `ID` varchar(8) NOT NULL,
  `Name` varchar(20) NOT NULL,
  `Password` varchar(16) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`ID`, `Name`, `Password`) VALUES
('10219873', 'Zjy', '200101'),
('88888888', 'Hza', '200109'),
('66666666', 'Mewo', '200101'),
('6868', 'Nice', '6868');

-- --------------------------------------------------------

--
-- 表的结构 `zjy_hza`
--

DROP TABLE IF EXISTS `zjy_hza`;
CREATE TABLE IF NOT EXISTS `zjy_hza` (
  `time` varchar(20) NOT NULL,
  `id` varchar(8) DEFAULT NULL,
  `message` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`time`),
  KEY `id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- 转存表中的数据 `zjy_hza`
--

INSERT INTO `zjy_hza` (`time`, `id`, `message`) VALUES
('2021-05-31 11:51:22', '88888888', 'Java作业什么时候可以交呢'),
('2021-05-31 11:51:13', '88888888', '早安'),
('2021-05-31 11:50:54', '10219873', '早安'),
('2021-05-31 11:51:32', '10219873', '马上就可以啦');

-- --------------------------------------------------------

--
-- 表的结构 `zjy_mewo`
--

DROP TABLE IF EXISTS `zjy_mewo`;
CREATE TABLE IF NOT EXISTS `zjy_mewo` (
  `time` varchar(20) NOT NULL,
  `id` varchar(8) DEFAULT NULL,
  `message` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`time`),
  KEY `id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
