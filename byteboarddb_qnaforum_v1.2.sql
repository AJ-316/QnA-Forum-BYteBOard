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
  `answer` longtext NOT NULL,
  `answer_user_id` int NOT NULL,
  `answer_question_id` int NOT NULL,
  `answer_upvotes` int DEFAULT '0',
  `answer_downvotes` int DEFAULT '0',
  `answer_bytescore` int GENERATED ALWAYS AS ((`answer_upvotes` - `answer_downvotes`)) VIRTUAL,
  PRIMARY KEY (`answer_id`),
  KEY `question_id_answers_fk_idx` (`answer_question_id`),
  KEY `user_id_answers_fk_idx` (`answer_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answers`
--

LOCK TABLES `answers` WRITE;
/*!40000 ALTER TABLE `answers` DISABLE KEYS */;
/*!40000 ALTER TABLE `answers` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qna_users`
--

LOCK TABLES `qna_users` WRITE;
/*!40000 ALTER TABLE `qna_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `qna_users` ENABLE KEYS */;
UNLOCK TABLES;

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
  KEY `vote_user_id_idx` (`vote_user_id`),
  KEY `vote_question_id_fk` (`vote_question_id`),
  KEY `vote_answer_id_fk` (`vote_answer_id`),
  CONSTRAINT `vote_answer_id_fk` FOREIGN KEY (`vote_answer_id`) REFERENCES `answers` (`answer_id`),
  CONSTRAINT `vote_question_id_fk` FOREIGN KEY (`vote_question_id`) REFERENCES `questions` (`question_id`),
  CONSTRAINT `vote_user_id_fk` FOREIGN KEY (`vote_user_id`) REFERENCES `qna_users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qna_votes`
--

LOCK TABLES `qna_votes` WRITE;
/*!40000 ALTER TABLE `qna_votes` DISABLE KEYS */;
/*!40000 ALTER TABLE `qna_votes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_tags`
--

DROP TABLE IF EXISTS `question_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question_tags` (
  `tag_id` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(45) NOT NULL,
  `tag_question_id` int DEFAULT NULL,
  PRIMARY KEY (`tag_id`),
  KEY `question_id_tags_fk_idx` (`tag_question_id`) /*!80000 INVISIBLE */,
  CONSTRAINT `question_id_tags_fk` FOREIGN KEY (`tag_question_id`) REFERENCES `questions` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_tags`
--

LOCK TABLES `question_tags` WRITE;
/*!40000 ALTER TABLE `question_tags` DISABLE KEYS */;
/*!40000 ALTER TABLE `question_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questions` (
  `question_id` int NOT NULL AUTO_INCREMENT,
  `question_head` varchar(255) NOT NULL,
  `question_body` longtext NOT NULL,
  `question_user_id` int NOT NULL,
  `question_upvotes` int DEFAULT '0',
  `question_downvotes` int DEFAULT '0',
  `question_bytescore` int GENERATED ALWAYS AS ((`question_upvotes` - `question_downvotes`)) VIRTUAL,
  PRIMARY KEY (`question_id`),
  UNIQUE KEY `question_head_UNIQUE` (`question_head`),
  KEY `user_id_questions_fk_idx` (`question_user_id`),
  CONSTRAINT `user_id_questions_fk` FOREIGN KEY (`question_user_id`) REFERENCES `qna_users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-05  0:32:31
