-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 01, 2023 at 03:49 PM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sample`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `id` int(11) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `rfid` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`id`, `first_name`, `last_name`, `middle_name`, `password`, `role`, `sex`, `username`, `rfid`) VALUES
(1, 'Dan', 'Rosete', NULL, '1234', 'Instructor', 'Male', 'citrus-win', 1),
(5, 'asda', 'asdasd', 'asdasdasdasd', '12345678', 'Admin', 'Male', 'citrus-bun', 7);

-- --------------------------------------------------------

--
-- Table structure for table `account_seq`
--

CREATE TABLE `account_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `account_seq`
--

INSERT INTO `account_seq` (`next_val`) VALUES
(6);

-- --------------------------------------------------------

--
-- Table structure for table `attendance`
--

CREATE TABLE `attendance` (
  `id` int(11) NOT NULL,
  `batch` varchar(255) DEFAULT NULL,
  `current_class` varchar(255) DEFAULT NULL,
  `current_instructor` varchar(255) DEFAULT NULL,
  `date_time` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `schedule` int(11) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `rfid` varchar(255) DEFAULT NULL,
  `student_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `attendance`
--

INSERT INTO `attendance` (`id`, `batch`, `current_class`, `current_instructor`, `date_time`, `first_name`, `last_name`, `middle_name`, `schedule`, `status`, `rfid`, `student_id`) VALUES
(2, 'BSIT-1A', 'ENGLISH', 'Rosete, Dan', '2023-04-28 22:51', 'Dan', 'Rosete', '', 2, 'On time', '0009024545', 1),
(4, 'BSIT-1A', 'subject code ', 'Rosete, Rosete', '2023-04-30 08:52', 'Dan', 'Rosete', '', 6, 'Late', '0009024545', 1),
(5, 'BSIT-2A', 'subject code ', 'Rosete, Dan', '2023-04-30 09:17', 'Raven', 'Sanao', '', 6, 'Late', '0007552339', 3);

-- --------------------------------------------------------

--
-- Table structure for table `batch`
--

CREATE TABLE `batch` (
  `id` int(11) NOT NULL,
  `course` varchar(255) DEFAULT NULL,
  `section` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `batch`
--

INSERT INTO `batch` (`id`, `course`, `section`) VALUES
(1, 'BSIT', '1A'),
(2, 'BSIT', '2A'),
(3, 'BSIT', '3A');

-- --------------------------------------------------------

--
-- Table structure for table `batch_seq`
--

CREATE TABLE `batch_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `batch_seq`
--

INSERT INTO `batch_seq` (`next_val`) VALUES
(4);

-- --------------------------------------------------------

--
-- Table structure for table `log`
--

CREATE TABLE `log` (
  `id` int(11) NOT NULL,
  `current_class` varchar(255) DEFAULT NULL,
  `current_instructor` varchar(255) DEFAULT NULL,
  `datetime` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `middlename` varchar(255) DEFAULT NULL,
  `rfid` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `log`
--

INSERT INTO `log` (`id`, `current_class`, `current_instructor`, `datetime`, `firstname`, `lastname`, `middlename`, `rfid`, `role`, `status`, `type`) VALUES
(1, 'ENGLISH', 'Rosete, Dan', '2023-04-28 22:38', 'Hennryx', 'Samson', '', '0008916880', 'Student', 'In', 'Unscheduled'),
(2, 'ENGLISH', 'Rosete, Dan', '2023-04-28 22:42', 'Hennryx', 'Samson', '', '0008916880', 'Student', 'Out', 'Unscheduled'),
(3, 'ENGLISH', 'Rosete, Dan', '2023-04-28 22:43', 'Hennryx', 'Samson', '', '0008916880', 'Student', 'In', 'Unscheduled'),
(4, 'ENGLISH', 'Rosete, Dan', '2023-04-28 22:44', 'Hennryx', 'Samson', '', '0008916880', 'Student', 'Out', 'Unscheduled'),
(5, 'ENGLISH', 'Rosete, Dan', '2023-04-28 22:44', 'Hennryx', 'Samson', '', '0008916880', 'Student', 'In', 'Unscheduled'),
(6, 'ENGLISH', 'Rosete, Dan', '2023-04-28 22:48', 'Dan', 'Rosete', '', '0009024545', 'Student', 'In', 'Scheduled'),
(7, 'ENGLISH', 'Rosete, Dan', '2023-04-28 22:50', 'Dan', 'Rosete', '', '0009024545', 'Student', 'Out', 'Scheduled'),
(8, 'ENGLISH', 'Rosete, Dan', '2023-04-28 22:51', 'Dan', 'Rosete', '', '0009024545', 'Student', 'In', 'Scheduled'),
(9, 'ENGLISH', 'Rosete, Dan', '2023-04-28 22:51', 'Dan', 'Rosete', '', '0009024545', 'Student', 'Out', 'Scheduled'),
(10, 'None', 'None', '2023-04-29 20:12', 'Hennryx', 'Samson', '', '0008916880', 'Student', 'Out', 'Unscheduled'),
(11, 'None', 'None', '2023-04-29 20:14', 'Hennryx', 'Samson', '', '0008916880', 'Student', 'In', 'Unscheduled'),
(12, 'None', 'None', '2023-04-29 20:14', 'Dan', 'Rosete', '', '0009024545', 'Student', 'In', 'Unscheduled'),
(13, 'subject code ', 'Rosete, Rosete', '2023-04-29 20:16', 'Hennryx', 'Samson', '', '0008916880', 'Student', 'Out', 'Scheduled'),
(14, 'subject code ', 'Rosete, Rosete', '2023-04-30 08:52', 'Dan', 'Rosete', '', '0009024545', 'Student', 'Out', 'Scheduled'),
(15, 'subject code ', 'Rosete, Rosete', '2023-04-30 08:53', 'Hennryx', 'Samson', '', '0008916880', 'Student', 'In', 'Scheduled'),
(16, 'subject code ', 'Rosete, Rosete', '2023-04-30 08:56', 'Dan', 'Rosete', '', '0009024545', 'Student', 'In', 'Scheduled'),
(17, 'subject code ', 'Rosete, Rosete', '2023-04-30 08:56', 'Hennryx', 'Samson', '', '0008916880', 'Student', 'Out', 'Scheduled'),
(18, 'subject code ', 'Rosete, Dan', '2023-04-30 09:16', 'Raven', 'Sanao', '', '0007552339', 'Student', 'In', 'Unscheduled'),
(19, 'subject code ', 'Rosete, Dan', '2023-04-30 09:17', 'Raven', 'Sanao', '', '0007552339', 'Student', 'Out', 'Scheduled');

-- --------------------------------------------------------

--
-- Table structure for table `rfid`
--

CREATE TABLE `rfid` (
  `id` int(11) NOT NULL,
  `used_by` varchar(255) DEFAULT NULL,
  `rfid` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `rfid`
--

INSERT INTO `rfid` (`id`, `used_by`, `rfid`) VALUES
(1, 'Instructor', '0007675520'),
(2, 'Student', '0009024545'),
(3, 'Student', '0008916880'),
(7, 'Instructor', '0007863499'),
(8, 'Student', '0007552339');

-- --------------------------------------------------------

--
-- Table structure for table `schedule`
--

CREATE TABLE `schedule` (
  `id` int(11) NOT NULL,
  `end_at` varchar(255) DEFAULT NULL,
  `start_at` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `subject_code` varchar(255) DEFAULT NULL,
  `subject_description` varchar(255) DEFAULT NULL,
  `subject_name` varchar(255) DEFAULT NULL,
  `week_slot` varchar(255) DEFAULT NULL,
  `instructor_id` int(11) DEFAULT NULL,
  `batch_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `schedule`
--

INSERT INTO `schedule` (`id`, `end_at`, `start_at`, `status`, `subject_code`, `subject_description`, `subject_name`, `week_slot`, `instructor_id`, `batch_id`) VALUES
(6, '12:00', '08:50', 1, 'subject code ', 'subject description', 'subject name', 'Sunday', 1, 1),
(9, '13:30', '08:00', 1, 'OOP-002', 'Programming subject', 'Object-Oriented Programing 02', 'Friday', 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

CREATE TABLE `student` (
  `id` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `student_id` varchar(255) DEFAULT NULL,
  `batch_id` int(11) DEFAULT NULL,
  `rfid_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`id`, `address`, `first_name`, `last_name`, `middle_name`, `student_id`, `batch_id`, `rfid_id`) VALUES
(1, 'Concepcion', 'Dan', 'Rosete', '', 'PPY2021-00002', 1, 2),
(2, 'Concepcion', 'Hennryx', 'Samson', '', 'PPY2021-00001', 1, 3),
(3, 'Concepcion', 'Raven', 'Sanao', '', 'PPY2021-00003', 2, 8);

-- --------------------------------------------------------

--
-- Table structure for table `student_seq`
--

CREATE TABLE `student_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `student_seq`
--

INSERT INTO `student_seq` (`next_val`) VALUES
(4);

-- --------------------------------------------------------

--
-- Table structure for table `sub_student`
--

CREATE TABLE `sub_student` (
  `id` int(11) NOT NULL,
  `schedule` int(11) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `student_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_username` (`username`),
  ADD KEY `FKcni7s181v69mwrgtvknhvc0og` (`rfid`);

--
-- Indexes for table `attendance`
--
ALTER TABLE `attendance`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `batch`
--
ALTER TABLE `batch`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_section` (`section`);

--
-- Indexes for table `log`
--
ALTER TABLE `log`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rfid`
--
ALTER TABLE `rfid`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `schedule`
--
ALTER TABLE `schedule`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKakiosqyldjsaib5kaa5c5v7vn` (`instructor_id`),
  ADD KEY `FKm7785qpepcgakln69nk4m7dtg` (`batch_id`);

--
-- Indexes for table `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK17mfv6a26cwnmli2b6vm00dn7` (`batch_id`),
  ADD KEY `FK1cssvqwu0rn1qjvfssfyjewrq` (`rfid_id`);

--
-- Indexes for table `sub_student`
--
ALTER TABLE `sub_student`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `attendance`
--
ALTER TABLE `attendance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `log`
--
ALTER TABLE `log`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `rfid`
--
ALTER TABLE `rfid`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `schedule`
--
ALTER TABLE `schedule`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `sub_student`
--
ALTER TABLE `sub_student`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `FKcni7s181v69mwrgtvknhvc0og` FOREIGN KEY (`rfid`) REFERENCES `rfid` (`id`);

--
-- Constraints for table `schedule`
--
ALTER TABLE `schedule`
  ADD CONSTRAINT `FKakiosqyldjsaib5kaa5c5v7vn` FOREIGN KEY (`instructor_id`) REFERENCES `account` (`id`),
  ADD CONSTRAINT `FKm7785qpepcgakln69nk4m7dtg` FOREIGN KEY (`batch_id`) REFERENCES `batch` (`id`);

--
-- Constraints for table `student`
--
ALTER TABLE `student`
  ADD CONSTRAINT `FK17mfv6a26cwnmli2b6vm00dn7` FOREIGN KEY (`batch_id`) REFERENCES `batch` (`id`),
  ADD CONSTRAINT `FK1cssvqwu0rn1qjvfssfyjewrq` FOREIGN KEY (`rfid_id`) REFERENCES `rfid` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
