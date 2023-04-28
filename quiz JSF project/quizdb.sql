
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `quizdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `answer`
--

CREATE TABLE `answer` (
  `id` bigint(20) NOT NULL,
  `statement` varchar(255) NOT NULL,
  `correct` varchar(10) NOT NULL,
  `question_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `answer`
--

INSERT INTO `answer` (`id`, `statement`, `correct`, `question_id`) VALUES
(11, 'Option A', 'correct', 25),
(12, 'Option B', 'incorrect', 25),
(13, 'Option C', 'incorrect', 25),
(14, 'Option D', 'incorrect', 25),
(15, 'Option E', 'incorrect', 25),
(16, 'all might', 'correct', 35),
(17, 'uzoku medoria', 'incorrect', 35),
(18, 'monkey D. luffy', 'incorrect', 35),
(19, 'nezku chan', 'incorrect', 35),
(20, 'answer 2', 'incorrect', 36);

-- --------------------------------------------------------

--
-- Table structure for table `question`
--

CREATE TABLE `question` (
  `id` bigint(20) NOT NULL,
  `statement` varchar(255) NOT NULL,
  `grade` int(11) NOT NULL,
  `quiz_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `question`
--

INSERT INTO `question` (`id`, `statement`, `grade`, `quiz_id`) VALUES
(25, 'Question 1', 10, 26),
(26, 'Question 2', 10, 26),
(27, 'Question 3', 10, 26),
(28, 'Question 4', 10, 26),
(29, 'Question 5', 10, 26),
(30, 'Question 6', 10, 26),
(31, 'Question 7', 10, 26),
(32, 'Question 8', 10, 26),
(33, 'Question 9', 10, 26),
(34, 'Question 10', 10, 26),
(35, 'who is the first user of one for all ?', 2, 26),
(36, 'when did konot kill ashilad', 0, 26);

-- --------------------------------------------------------

--
-- Table structure for table `quiz`
--

CREATE TABLE `quiz` (
  `id` bigint(20) NOT NULL,
  `label` varchar(255) NOT NULL,
  `theme` varchar(255) NOT NULL,
  `score` int(11) NOT NULL,
  `created` timestamp NOT NULL DEFAULT current_timestamp(),
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `quiz`
--

INSERT INTO `quiz` (`id`, `label`, `theme`, `score`, `created`, `user_id`) VALUES
(26, 'oth', 'oth theme', 20, '2023-04-18 23:22:04', 1),
(27, 'tt', 'dddd', 10, '2023-04-19 17:44:20', 1);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `login` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `role` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `login`, `password`, `first_name`, `last_name`, `role`) VALUES
(1, 'imad-creator', '20022023', 'Imad', 'Belattar', 1),
(2, 'othmane', 'o', 'Othmane', 'Belattar', 2),
(3, 'user1', 'password1', 'John', 'Doe', 1),
(4, 'user2', 'password2', 'Jane', 'Doe', 1),
(5, 'user3', 'password3', 'Bob', 'Smith', 1),
(6, 'user4', 'password4', 'Samantha', 'Johnson', 1),
(7, 'user5', 'password5', 'Michael', 'Lee', 1),
(8, 'user6', 'password6', 'Emily', 'Nguyen', 1),
(9, 'user7', 'password7', 'David', 'Brown', 1),
(10, 'user8', 'password8', 'Avery', 'Taylor', 1),
(11, 'user9', 'password9', 'Ethan', 'Garcia', 1),
(12, 'user10', 'password10', 'Madison', 'Kim', 1),
(13, 'user11', 'password11', 'William', 'Wilson', 1),
(14, 'user12', 'password12', 'Isabella', 'Jones', 1),
(15, 'user13', 'password13', 'Elijah', 'Davis', 1),
(16, 'user14', 'password14', 'Olivia', 'Martinez', 1),
(17, 'user15', 'password15', 'Lucas', 'Anderson', 1),
(18, 'user16', 'password16', 'Sophia', 'Thomas', 1),
(19, 'user17', 'password17', 'Alexander', 'Jackson', 1),
(20, 'user18', 'password18', 'Mia', 'White', 1),
(21, 'user19', 'password19', 'Benjamin', 'Harris', 1),
(22, 'user20', 'password20', 'Chloe', 'Martin', 1);

-- --------------------------------------------------------

--
-- Table structure for table `userquizscore`
--

CREATE TABLE `userquizscore` (
  `user_id` bigint(20) NOT NULL,
  `quiz_id` bigint(20) NOT NULL,
  `obtained_score` float NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `userquizscore`
--

INSERT INTO `userquizscore` (`user_id`, `quiz_id`, `obtained_score`) VALUES
(2, 26, 12),
(2, 27, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `answer`
--
ALTER TABLE `answer`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_question_answer` (`question_id`);

--
-- Indexes for table `question`
--
ALTER TABLE `question`
  ADD PRIMARY KEY (`id`),
  ADD KEY `question_ibfk_1` (`quiz_id`);

--
-- Indexes for table `quiz`
--
ALTER TABLE `quiz`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_quiz_user` (`user_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_login` (`login`);

--
-- Indexes for table `userquizscore`
--
ALTER TABLE `userquizscore`
  ADD PRIMARY KEY (`user_id`,`quiz_id`),
  ADD KEY `FK_user_quiz_score_user` (`user_id`),
  ADD KEY `FK_user_quiz_score_quiz` (`quiz_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `answer`
--
ALTER TABLE `answer`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `question`
--
ALTER TABLE `question`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `quiz`
--
ALTER TABLE `quiz`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `answer`
--
ALTER TABLE `answer`
  ADD CONSTRAINT `FK_question_answer` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `question`
--
ALTER TABLE `question`
  ADD CONSTRAINT `question_ibfk_1` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `quiz`
--
ALTER TABLE `quiz`
  ADD CONSTRAINT `FK_quiz_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `userquizscore`
--
ALTER TABLE `userquizscore`
  ADD CONSTRAINT `FK_user_quiz_score_quiz` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_user_quiz_score_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
