-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: byteboarddb_qnaforum
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `answers`
--

DROP TABLE IF EXISTS `answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answers` (
  `answer_id` int NOT NULL AUTO_INCREMENT,
  `answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `answer_user_id` int NOT NULL,
  `answer_question_id` int NOT NULL,
  `answer_upvotes` int DEFAULT '0',
  `answer_downvotes` int DEFAULT '0',
  `answer_bytescore` int GENERATED ALWAYS AS ((`answer_upvotes` - `answer_downvotes`)) VIRTUAL,
  PRIMARY KEY (`answer_id`),
  KEY `user_id_answers_fk_idx` (`answer_user_id`),
  KEY `question_id_answers_fk_idx` (`answer_question_id`),
  CONSTRAINT `question_id_answers_fk` FOREIGN KEY (`answer_question_id`) REFERENCES `questions` (`question_id`),
  CONSTRAINT `user_id_answers_fk` FOREIGN KEY (`answer_user_id`) REFERENCES `qna_users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `comment_user_id` int NOT NULL,
  `comment_question_id` int DEFAULT NULL,
  `comment_answer_id` int DEFAULT NULL,
  `comment` varchar(255) NOT NULL,
  `comment_feedback_score` int DEFAULT '0',
  PRIMARY KEY (`comment_id`),
  KEY `user_id_comment_fk_idx` (`comment_user_id`) /*!80000 INVISIBLE */,
  KEY `question_id_comment_fk` (`comment_question_id`),
  KEY `answer_id_comment_fk` (`comment_answer_id`),
  CONSTRAINT `answer_id_comment_fk` FOREIGN KEY (`comment_answer_id`) REFERENCES `answers` (`answer_id`),
  CONSTRAINT `question_id_comment_fk` FOREIGN KEY (`comment_question_id`) REFERENCES `questions` (`question_id`),
  CONSTRAINT `user_id_comment_fk` FOREIGN KEY (`comment_user_id`) REFERENCES `qna_users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qna_comments_feedback`
--

DROP TABLE IF EXISTS `qna_comments_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qna_comments_feedback` (
  `feedback_id` int NOT NULL AUTO_INCREMENT,
  `feedback_comment_id` int NOT NULL,
  `feedback_user_id` int NOT NULL,
  `feedback` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`feedback_id`),
  KEY `comment_id_feedback_fk_idx` (`feedback_comment_id`),
  KEY `user_id_feedback_fk_idx` (`feedback_user_id`),
  CONSTRAINT `comment_id_feedback_fk` FOREIGN KEY (`feedback_comment_id`) REFERENCES `comments` (`comment_id`),
  CONSTRAINT `user_id_feedback_fk` FOREIGN KEY (`feedback_user_id`) REFERENCES `qna_users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `update_comment_feedback_score_after_insert` AFTER INSERT ON `qna_comments_feedback` FOR EACH ROW BEGIN
	IF NEW.feedback = 1 THEN
		UPDATE comments
		SET comment_feedback_score = comment_feedback_score + 1
		WHERE comment_id = NEW.feedback_comment_id;
	ELSE
		UPDATE comments
		SET comment_feedback_score = comment_feedback_score - 1
		WHERE comment_id = NEW.feedback_comment_id;
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `update_comment_feedback_score_after_update` AFTER UPDATE ON `qna_comments_feedback` FOR EACH ROW BEGIN
    IF NEW.feedback = 1 AND OLD.feedback = 0 THEN
        UPDATE comments
        SET comment_feedback_score = comment_feedback_score + 1
        WHERE comment_id = NEW.feedback_comment_id;
    ELSEIF NEW.feedback = 0 AND OLD.feedback = 1 THEN
        UPDATE comments
        SET comment_feedback_score = comment_feedback_score - 1
        WHERE comment_id = NEW.feedback_comment_id;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `update_comment_feedback_score_after_delete` AFTER DELETE ON `qna_comments_feedback` FOR EACH ROW BEGIN
    IF OLD.feedback = 1 THEN
        UPDATE comments
        SET comment_feedback_score = comment_feedback_score - 1
        WHERE comment_id = OLD.feedback_comment_id;
    ELSE
        UPDATE comments
        SET comment_feedback_score = comment_feedback_score + 1
        WHERE comment_id = OLD.feedback_comment_id;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `qna_tags`
--

DROP TABLE IF EXISTS `qna_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qna_tags` (
  `tag_id` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(45) NOT NULL,
  PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qna_users`
--

DROP TABLE IF EXISTS `qna_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qna_users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(25) NOT NULL,
  `user_password` varchar(100) NOT NULL,
  `user_email` varchar(50) NOT NULL,
  `user_profile` int NOT NULL DEFAULT '0',
  `user_bytescore` int DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username_UNIQUE` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qna_votes`
--

DROP TABLE IF EXISTS `qna_votes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qna_votes` (
  `vote_id` int NOT NULL AUTO_INCREMENT,
  `vote_user_id` int NOT NULL,
  `vote_question_id` int DEFAULT NULL,
  `vote_answer_id` int DEFAULT NULL,
  `vote_type` enum('up','down','none') NOT NULL DEFAULT 'none',
  PRIMARY KEY (`vote_id`),
  KEY `user_id_vote_fk_idx` (`vote_user_id`) /*!80000 INVISIBLE */,
  KEY `question_id_vote_fk_idx` (`vote_question_id`) /*!80000 INVISIBLE */,
  KEY `answer_id_vote_fk_idx` (`vote_answer_id`),
  CONSTRAINT `answer_id_vote_fk` FOREIGN KEY (`vote_answer_id`) REFERENCES `answers` (`answer_id`),
  CONSTRAINT `question_id_vote_fk` FOREIGN KEY (`vote_question_id`) REFERENCES `questions` (`question_id`),
  CONSTRAINT `user_id_vote_fk` FOREIGN KEY (`vote_user_id`) REFERENCES `qna_users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `question_tags`
--

DROP TABLE IF EXISTS `question_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question_tags` (
  `qt_id` int NOT NULL AUTO_INCREMENT,
  `qt_tag_id` int NOT NULL,
  `qt_question_id` int NOT NULL,
  PRIMARY KEY (`qt_id`),
  UNIQUE KEY `question_tag_id_UNIQUE` (`qt_id`) /*!80000 INVISIBLE */,
  KEY `quesion_id_qt_fk_idx` (`qt_question_id`),
  KEY `tag_id_qt_fk_idx` (`qt_tag_id`),
  CONSTRAINT `quesion_id_qt_fk` FOREIGN KEY (`qt_question_id`) REFERENCES `questions` (`question_id`),
  CONSTRAINT `tag_id_qt_fk` FOREIGN KEY (`qt_tag_id`) REFERENCES `qna_tags` (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questions` (
  `question_id` int NOT NULL AUTO_INCREMENT,
  `question_head` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `question_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `question_user_id` int NOT NULL,
  `question_upvotes` int DEFAULT '0',
  `question_downvotes` int DEFAULT '0',
  `question_bytescore` int GENERATED ALWAYS AS ((`question_upvotes` - `question_downvotes`)) VIRTUAL,
  PRIMARY KEY (`question_id`),
  UNIQUE KEY `question_head_UNIQUE` (`question_head`),
  KEY `user_id_questions_fk_idx` (`question_user_id`),
  FULLTEXT KEY `question_search_idx` (`question_head`,`question_body`),
  FULLTEXT KEY `question_head_search_idx` (`question_head`),
  CONSTRAINT `user_id_questions_fk` FOREIGN KEY (`question_user_id`) REFERENCES `qna_users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-01  1:06:09
