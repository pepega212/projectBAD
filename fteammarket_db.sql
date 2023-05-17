-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 11, 2023 at 09:23 AM
-- Server version: 10.4.25-MariaDB
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `fteammarket_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `userID` char(5) NOT NULL,
  `itemID` char(5) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `cart`
--

INSERT INTO `cart` (`userID`, `itemID`, `quantity`) VALUES
('US008', 'IT008', 3);

-- --------------------------------------------------------

--
-- Table structure for table `item`
--

CREATE TABLE `item` (
  `itemID` char(5) NOT NULL,
  `itemName` varchar(40) NOT NULL,
  `itemDescription` varchar(200) NOT NULL,
  `price` int(11) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `item`
--

INSERT INTO `item` (`itemID`, `itemName`, `itemDescription`, `price`, `quantity`) VALUES
('IT005', 'Dota 2 : The Magus Cypher', 'Most Magi believed the Puzzle of Perplex to be a mere curiosity.', 350000, 20),
('IT006', 'CSGO : AK-47 | Predator', 'Powerful and reliable the AK-47 is one of the most popular assault rifles in the world', 110000, 0),
('IT007', 'Subnautica : Markiplier Hull Plate', 'Markiplier Hull Plate', 55000, 14),
('IT008', 'COC : Gems', 'used for buy magic items / instant upgrade', 20000, 4),
('IT009', 'CR : Gems', 'Used for Buy Chest & Join tournament', 20000, 14);

-- --------------------------------------------------------

--
-- Table structure for table `transaction`
--

CREATE TABLE `transaction` (
  `transactionID` char(5) NOT NULL,
  `userID` char(5) NOT NULL,
  `transactionDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transaction`
--

INSERT INTO `transaction` (`transactionID`, `userID`, `transactionDate`) VALUES
('TR001', 'US009', '2022-05-30'),
('TR002', 'US009', '2022-05-30'),
('TR003', 'US009', '2022-05-30'),
('TR004', 'US010', '2022-05-30'),
('TR005', 'US009', '2022-05-30'),
('TR006', 'US009', '2022-05-30'),
('TR007', 'US009', '2022-05-30'),
('TR009', 'US011', '2022-12-27'),
('TR012', 'US008', '2022-12-27'),
('TR013', 'US008', '2023-01-01'),
('TR014', 'US008', '2023-01-03'),
('TR015', 'US008', '2023-01-04'),
('TR016', 'US008', '2023-01-07'),
('TR017', 'US011', '2023-01-08'),
('TR018', 'US008', '2023-01-11');

-- --------------------------------------------------------

--
-- Table structure for table `transactiondetail`
--

CREATE TABLE `transactiondetail` (
  `transactionID` char(5) NOT NULL,
  `itemID` char(5) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transactiondetail`
--

INSERT INTO `transactiondetail` (`transactionID`, `itemID`, `quantity`) VALUES
('TR001', 'IT005', 2),
('TR002', 'IT006', 2),
('TR003', 'IT007', 1),
('TR004', 'IT006', 4),
('TR005', 'IT005', 8),
('TR006', 'IT006', 3),
('TR007', 'IT007', 4),
('TR013', 'IT008', 3),
('TR014', 'IT007', 1),
('TR015', 'IT008', 2),
('TR016', 'IT009', 4),
('TR018', 'IT006', 4);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `userID` char(5) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `gender` varchar(6) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phoneNumber` varchar(13) NOT NULL,
  `age` int(11) NOT NULL,
  `role` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`userID`, `username`, `password`, `gender`, `email`, `phoneNumber`, `age`, `role`) VALUES
('US001', 'admin', 'admin', 'male', 'admin@gmail.com', '0812312312', 0, 'admin'),
('US008', 'tester2', 'tester2', 'Male', 'tester@gmail.com', '1234513245', 20, 'user'),
('US009', 'haloo1', 'haloo1', 'Male', 'haloo@gmail.com', '0852112341', 20, 'user'),
('US010', 'tester123', 'tester123', 'Female', 'testerUpdate1@gmail.com', '08131950689', 18, 'user'),
('US011', 'rakpradiva', 'rakpradiva25', 'Male', 'rakpradiva@mail.com', '08123456789', 19, 'user'),
('US012', 'tester21', 'tester21', 'Male', 'tester21@aa.com', '08123456767', 21, 'user');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`userID`,`itemID`),
  ADD KEY `itemID` (`itemID`),
  ADD KEY `userID` (`userID`);

--
-- Indexes for table `item`
--
ALTER TABLE `item`
  ADD PRIMARY KEY (`itemID`);

--
-- Indexes for table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`transactionID`),
  ADD KEY `userID` (`userID`);

--
-- Indexes for table `transactiondetail`
--
ALTER TABLE `transactiondetail`
  ADD PRIMARY KEY (`transactionID`),
  ADD KEY `itemID` (`itemID`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userID`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`itemID`) REFERENCES `item` (`itemID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `transaction`
--
ALTER TABLE `transaction`
  ADD CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `transactiondetail`
--
ALTER TABLE `transactiondetail`
  ADD CONSTRAINT `transactiondetail_ibfk_1` FOREIGN KEY (`transactionID`) REFERENCES `transaction` (`transactionID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `transactiondetail_ibfk_2` FOREIGN KEY (`itemID`) REFERENCES `item` (`itemID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
