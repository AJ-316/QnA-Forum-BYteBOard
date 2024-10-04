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
  `answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answers`
--

LOCK TABLES `answers` WRITE;
/*!40000 ALTER TABLE `answers` DISABLE KEYS */;
INSERT INTO `answers` (`answer_id`, `answer`, `answer_user_id`, `answer_question_id`, `answer_upvotes`, `answer_downvotes`) VALUES (1,'There are two overarching types of variables in Java: \\\n\n* Primitives: variables that contain data. If you want to manipulate the data in a primitive variable you can manipulate that variable directly. By convention primitive types start with a lowercase letter. For example variables of type int or char are primitives. \\\n\n* References: variables that contain the memory address of an Object i.e. variables that refer to an Object. If you want to manipulate the Object that a reference variable refers to you must dereference it. Dereferencing usually entails using . to access a method or field, or using [ to index an array. By convention reference types are usually denoted with a type that starts in uppercase. For example variables of type Object are references. \\\n\nConsider the following code where you declare a variable of primitive type int and don\'t initialize it:\n\'\'\'\nint x;\nint y = x + x;\n\'\'\'\nThese two lines will crash the program because no value is specified for x and we are trying to use x\'s value to specify y. All primitives have to be initialized to a usable value before they are manipulated.\n\nNow here is where things get interesting. Reference variables can be set to null which means \"I am referencing nothing\". You can get a null value in a reference variable if you explicitly set it that way, or a reference variable is uninitialized and the compiler does not catch it (Java will automatically set the variable to null). \\\n\nIf a reference variable is set to null either explicitly by you or through Java automatically, and you attempt to dereference it you get a NullPointerException. \\\n\nThe NullPointerException (NPE) typically occurs when you declare a variable but did not create an object and assign it to the variable before trying to use the contents of the variable. So you have a reference to something that does not actually exist. \\\n\nTake the following code:\n\'\'\'\nInteger num;\nnum = new Integer(10);\n\'\'\'\nThe first line declares a variable named num, but it does not actually contain a reference value yet. Since you have not yet said what to point to, Java sets it to null. \\\n\nIn the second line, the new keyword is used to instantiate (or create) an object of type Integer, and the reference variable num is assigned to that Integer object. \\\n\nIf you attempt to dereference num before creating the object you get a NullPointerException. In the most trivial cases, the compiler will catch the problem and let you know that \"num may not have been initialized,\" but sometimes you may write code that does not directly create the object. \\\n\nFor instance, you may have a method as follows:\n\'\'\'\npublic void doSomething(SomeObject obj) {\n   // Do something to obj, assumes obj is not null\n   obj.myMethod();\n}\n\'\'\'\n\nIn which case, you are not creating the object obj, but rather assuming that it was created before the doSomething() method was called. Note, it is possible to call the method like this: \n\'\'\'\ndoSomething(null);\nIn which case, obj is null, and the statement obj.myMethod() will throw a NullPointerException.\n\'\'\'\n\nIf the method is intended to do something to the passed-in object as the above method does, it is appropriate to throw the NullPointerException because it\'s a programmer error and the programmer will need that information for debugging purposes. \\\n\nIn addition to NullPointerExceptions thrown as a result of the method\'s logic, you can also check the method arguments for null values and throw NPEs explicitly by adding something like the following near the beginning of a method:\n\'\'\'\n// Throws an NPE with a custom error message if obj is null\nObjects.requireNonNull(obj, \"obj must not be null\");\n\'\'\'\n\nNote that it\'s helpful to say in your error message clearly which object cannot be null. The advantage of validating this is that 1) you can return your own clearer error messages and 2) for the rest of the method you know that unless obj is reassigned, it is not null and can be dereferenced safely. \\\n\nAlternatively, there may be cases where the purpose of the method is not solely to operate on the passed in object, and therefore a null parameter may be acceptable. In this case, you would need to check for a null parameter and behave differently. You should also explain this in the documentation. For example, doSomething() could be written as:\n\'\'\'\n/**\n  * @param obj An optional foo for ____. May be null, in which case\n  *  the result will be ____.\n  */\npublic void doSomething(SomeObject obj) {\n    if(obj == null) {\n       // Do something\n    } else {\n       // Do something else\n    }\n}\n\'\'\'\n\nFinally, How to pinpoint the exception & cause using Stack Trace\n\n> What methods/tools can be used to determine the cause so that you stop the exception from causing the program to terminate prematurely?\n\n\nSonar with find bugs can detect NPE. Can sonar catch null pointer exceptions caused by JVM Dynamically \\\n\nNow Java 14 has added a new language feature to show the root cause of NullPointerException. This language feature has been part of SAP commercial JVM since 2006. \\\n\nIn Java 14, the following is a sample NullPointerException Exception message:\n\n> in thread \"main\" java.lang.NullPointerException: Cannot invoke \"java.util.List.size()\" because \"list\" is null\n\n\\\nList of situations that cause a NullPointerException to occur\nHere are all the situations in which a NullPointerException occurs, that are directly* mentioned by the Java Language Specification: \\\n\n* Accessing (i.e. getting or setting) an instance field of a null reference. (static fields don\'t count!) \\\n* Calling an instance method of a null reference. (static methods don\'t count!) \\\n* throw null; \\\n* Accessing elements of a null array. \\\n* Synchronising on null - synchronized (someNullReference) { ... } \\\n* Any integer/floating point operator can throw a NullPointerException if one of its operands is a boxed null reference \\\n* An unboxing conversion throws a NullPointerException if the boxed value is null. \\\n* Calling super on a null reference throws a NullPointerException. If you are confused, this is talking about qualified superclass constructor invocations:\n\'\'\'\nclass Outer {\n    class Inner {}\n}\nclass ChildOfInner extends Outer.Inner {\n    ChildOfInner(Outer o) { \n        o.super(); // if o is null, NPE gets thrown\n    }\n}\n\'\'\'\n* Using a for (element : iterable) loop to loop through a null collection/array. \\\n\n* switch (foo) { ... } (whether its an expression or statement) can throw a NullPointerException when foo is null. \\\n\n* foo.new SomeInnerClass() throws a NullPointerException when foo is null. \\\n\n* Method references of the form name1::name2 or primaryExpression::name throws a NullPointerException when evaluated when name1 or primaryExpression evaluates to null.\n\nA note from the JLS here says that, someInstance.someStaticMethod() doesn\'t throw an NPE, because someStaticMethod is static, but someInstance::someStaticMethod still throw an NPE!',2,1,0,0),(2,'> → For a more general explanation of asynchronous behaviour with different examples, see Why is my variable unaltered after I modify it inside of a function? - Asynchronous code reference \\\n\\\n→ If you already understand the problem, skip to the possible solutions below.\n\n\n* The problem: \\\nThe A in Ajax stands for asynchronous. That means sending the request (or rather receiving the response) is taken out of the normal execution flow. In your example, $.ajax returns immediately and the next statement, return result;, is executed before the function you passed as success callback was even called. \\\n\nHere is an analogy which hopefully makes the difference between synchronous and asynchronous flow clearer: \\\n\n- Synchronous: \\\nImagine you make a phone call to a friend and ask him to look something up for you. Although it might take a while, you wait on the phone and stare into space, until your friend gives you the answer that you needed. \\\n\nThe same is happening when you make a function call containing \"normal\" code:\n\'\'\'\nfunction findItem() {\n    var item;\n    while(item_not_found) {\n        // search\n    }\n    return item;\n}\n\nvar item = findItem();\n\n// Do something with item\ndoSomethingElse();\n\'\'\'\n\n\nEven though findItem might take a long time to execute, any code coming after var item = findItem(); has to wait until the function returns the result.\n\n- Asynchronous: \\\nYou call your friend again for the same reason. But this time you tell him that you are in a hurry and he should call you back on your mobile phone. You hang up, leave the house, and do whatever you planned to do. Once your friend calls you back, you are dealing with the information he gave to you. \\\n\nThat\'s exactly what\'s happening when you do an Ajax request.\n\'\'\'\nfindItem(function(item) {\n    // Do something with the item\n});\ndoSomethingElse();\n\'\'\'\n\nInstead of waiting for the response, the execution continues immediately and the statement after the Ajax call is executed. To get the response eventually, you provide a function to be called once the response was received, a callback (notice something? call back ?). Any statement coming after that call is executed before the callback is called. \\\n\n* Solution(s) \\\nEmbrace the asynchronous nature of JavaScript! While certain asynchronous operations provide synchronous counterparts (so does \"Ajax\"), it\'s generally discouraged to use them, especially in a browser context. \\\n\nWhy is it bad do you ask? \\\n\nJavaScript runs in the UI thread of the browser and any long-running process will lock the UI, making it unresponsive. Additionally, there is an upper limit on the execution time for JavaScript and the browser will ask the user whether to continue the execution or not. \\\n\nAll of this results in a really bad user experience. The user won\'t be able to tell whether everything is working fine or not. Furthermore, the effect will be worse for users with a slow connection. \\\n\nIn the following we will look at three different solutions that are all building on top of each other: \\\n\n+ Promises with async/await (ES2017+, available in older browsers if you use a transpiler or regenerator) \\\n\n+ Callbacks (popular in node) \\\n\n+ Promises with then() (ES2015+, available in older browsers if you use one of the many promise libraries) \\\n\nAll three are available in current browsers, and node 7+.',3,2,2,0),(3,'The correct way to avoid SQL injection attacks, no matter which database you use, is to separate the data from SQL, so that data stays data and will never be interpreted as commands by the SQL parser. It is possible to create an SQL statement with correctly formatted data parts, but if you don\'t fully understand the details, you should always use prepared statements and parameterized queries. These are SQL statements that are sent to and parsed by the database server separately from any parameters. This way it is impossible for an attacker to inject malicious SQL. \\\n\nYou basically have two options to achieve this: \\\n\n1) Using PDO (for any supported database driver):\n\'\'\'\n$stmt = $pdo->prepare(\'SELECT * FROM employees WHERE name = :name\'); \\\n$stmt->execute([ \'name\' => $name ]);\n\nforeach ($stmt as $row) {\n    // Do something with $row\n}\n\'\'\'\n2) Using MySQLi (for MySQL):\nSince PHP 8.2+ we can make use of execute_query() which prepares, binds parameters, and executes SQL statement in one method:\n\'\'\'\n$result = $db->execute_query(\'SELECT * FROM employees WHERE name = ?\', [$name]);\n while ($row = $result->fetch_assoc()) {\n     // Do something with $row\n }\n\'\'\'\nUp to PHP8.1:\n\'\'\'\n $stmt = $db->prepare(\'SELECT * FROM employees WHERE name = ?\');\n $stmt->bind_param(\'s\', $name); // \'s\' specifies the variable type => \'string\'\n $stmt->execute();\n $result = $stmt->get_result();\n while ($row = $result->fetch_assoc()) {\n     // Do something with $row\n }\n\'\'\'\nIf you\'re connecting to a database other than MySQL, there is a driver-specific second option that you can refer to (for example, pg_prepare() and pg_execute() for PostgreSQL). PDO is the universal option. \\\n\\\n* Correctly setting up the connection\n- PDO: \\\nNote that when using PDO to access a MySQL database real prepared statements are not used by default. To fix this you have to disable the emulation of prepared statements. An example of creating a connection using PDO is: \\\n\'\'\'\n$dbConnection = new PDO(\'mysql:dbname=dbtest;host=127.0.0.1;charset=utf8mb4\', \'user\', \'password\');\n\n$dbConnection->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);\n$dbConnection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);\n\'\'\'\nIn the above example, the error mode isn\'t strictly necessary, but it is advised to add it. This way PDO will inform you of all MySQL errors by means of throwing the PDOException. \\\n\nWhat is mandatory, however, is the first setAttribute() line, which tells PDO to disable emulated prepared statements and use real prepared statements. This makes sure the statement and the values aren\'t parsed by PHP before sending it to the MySQL server (giving a possible attacker no chance to inject malicious SQL). \\\n\nAlthough you can set the charset in the options of the constructor, it\'s important to note that \'older\' versions of PHP (before 5.3.6) silently ignored the charset parameter in the DSN. \\\n\n- Mysqli:\nFor mysqli we have to follow the same routine:\n\'\'\'\nmysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT); // error reporting\n$dbConnection = new mysqli(\'127.0.0.1\', \'username\', \'password\', \'test\');\n$dbConnection->set_charset(\'utf8mb4\'); // charset\n\'\'\'\n\n* Explanation: \\\nThe SQL statement you pass to prepare is parsed and compiled by the database server. By specifying parameters (either a ? or a named parameter like :name in the example above) you tell the database engine where you want to filter on. Then when you call execute, the prepared statement is combined with the parameter values you specify. \\\n\nThe important thing here is that the parameter values are combined with the compiled statement, not an SQL string. SQL injection works by tricking the script into including malicious strings when it creates SQL to send to the database. So by sending the actual SQL separately from the parameters, you limit the risk of ending up with something you didn\'t intend. \\\n\nAny parameters you send when using a prepared statement will just be treated as strings (although the database engine may do some optimization so parameters may end up as numbers too, of course). In the example above, if the $name variable contains \'Sarah\'; DELETE FROM employees the result would simply be a search for the string \"\'Sarah\'; DELETE FROM employees\", and you will not end up with an empty table. \\\n\nAnother benefit of using prepared statements is that if you execute the same statement many times in the same session it will only be parsed and compiled once, giving you some speed gains. \\\n\nOh, and since you asked about how to do it for an insert, here\'s an example (using PDO):\n\'\'\'\n$preparedStatement = $db->prepare(\'INSERT INTO table (column) VALUES (:column)\');\n\n$preparedStatement->execute([ \'column\' => $unsafeValue ]);\n\'\'\'\n\n* Can prepared statements be used for dynamic queries?\nWhile you can still use prepared statements for the query parameters, the structure of the dynamic query itself cannot be parametrized and certain query features cannot be parametrized. \\\n\nFor these specific scenarios, the best thing to do is use a whitelist filter that restricts the possible values.\n\'\'\'\n// Value whitelist\n// $dir can only be \'DESC\', otherwise it will be \'ASC\'\nif (empty($dir) || $dir !== \'DESC\') {\n   $dir = \'ASC\';\n}\n\'\'\'',1,3,2,0);
/*!40000 ALTER TABLE `answers` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (1,1,2,NULL,'I don\'t think this is useful. OP made this question and self-answer to document how to get the response from async calls. Suggesting a 3rd party module defeats such purpose, and IMO the paradigm introduced by that module is not good practice.',0),(2,2,NULL,3,'Also, the official documentation of mysql_query only allows to execute one query, so any other query besides ; is ignored. Even if this is already deprecated there are a lot of systems under PHP 5.5.0 and that may use this function.',2);
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qna_comments_feedback`
--

LOCK TABLES `qna_comments_feedback` WRITE;
/*!40000 ALTER TABLE `qna_comments_feedback` DISABLE KEYS */;
INSERT INTO `qna_comments_feedback` VALUES (1,1,2,0),(2,2,3,1),(3,2,1,1);
/*!40000 ALTER TABLE `qna_comments_feedback` ENABLE KEYS */;
UNLOCK TABLES;
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qna_tags`
--

LOCK TABLES `qna_tags` WRITE;
/*!40000 ALTER TABLE `qna_tags` DISABLE KEYS */;
INSERT INTO `qna_tags` VALUES (1,'java'),(2,'nullPointerException'),(3,'exception'),(4,'javascript'),(5,'ajax'),(6,'asynchronous'),(7,'php'),(8,'mysql'),(9,'security'),(10,'sqlInjection');
/*!40000 ALTER TABLE `qna_tags` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qna_users`
--

LOCK TABLES `qna_users` WRITE;
/*!40000 ALTER TABLE `qna_users` DISABLE KEYS */;
INSERT INTO `qna_users` VALUES (1,'Advaitya','$2a$10$ExC3XhxZq6uS3ADLJPka6eyFk8On6vWnClVFf96DhChyJnZuLU1Ra','advaitya@gmail.com',3,3),(2,'Vinayak','$2a$10$FudfzJf0ruBZQGLJmVQDGu8K5oonNpknfBwEpqsYRvYQG7zaI5UV2','vinayak@gmail.com',5,0),(3,'Meghan','$2a$10$QbgZB2ECYtprjiHo8g4wN.aO5SM87E07aeFtOOJL2w/NrG7eyznSK','meghan@gmail.com',2,4);
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
  KEY `user_id_vote_fk_idx` (`vote_user_id`) /*!80000 INVISIBLE */,
  KEY `question_id_vote_fk_idx` (`vote_question_id`) /*!80000 INVISIBLE */,
  KEY `answer_id_vote_fk_idx` (`vote_answer_id`),
  CONSTRAINT `answer_id_vote_fk` FOREIGN KEY (`vote_answer_id`) REFERENCES `answers` (`answer_id`),
  CONSTRAINT `question_id_vote_fk` FOREIGN KEY (`vote_question_id`) REFERENCES `questions` (`question_id`),
  CONSTRAINT `user_id_vote_fk` FOREIGN KEY (`vote_user_id`) REFERENCES `qna_users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qna_votes`
--

LOCK TABLES `qna_votes` WRITE;
/*!40000 ALTER TABLE `qna_votes` DISABLE KEYS */;
INSERT INTO `qna_votes` VALUES (1,2,1,NULL,'up'),(2,1,NULL,2,'up'),(3,1,2,NULL,'down'),(4,2,NULL,2,'up'),(5,3,2,NULL,'up'),(6,1,3,NULL,'up'),(7,2,3,NULL,'up'),(8,2,NULL,3,'up'),(9,3,NULL,3,'up');
/*!40000 ALTER TABLE `qna_votes` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_tags`
--

LOCK TABLES `question_tags` WRITE;
/*!40000 ALTER TABLE `question_tags` DISABLE KEYS */;
INSERT INTO `question_tags` VALUES (1,1,1),(2,2,1),(3,3,1),(4,4,2),(5,5,2),(6,6,2),(7,7,3),(8,8,3),(9,9,3),(10,10,3);
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
  `question_head` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `question_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `question_user_id` int NOT NULL,
  `question_upvotes` int DEFAULT '0',
  `question_downvotes` int DEFAULT '0',
  `question_bytescore` int GENERATED ALWAYS AS ((`question_upvotes` - `question_downvotes`)) VIRTUAL,
  PRIMARY KEY (`question_id`),
  UNIQUE KEY `question_head_UNIQUE` (`question_head`),
  KEY `user_id_questions_fk_idx` (`question_user_id`),
  FULLTEXT KEY `question_head_search_idx` (`question_head`),
  FULLTEXT KEY `question_search_idx` (`question_head`,`question_body`),
  CONSTRAINT `user_id_questions_fk` FOREIGN KEY (`question_user_id`) REFERENCES `qna_users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
INSERT INTO `questions` (`question_id`, `question_head`, `question_body`, `question_user_id`, `question_upvotes`, `question_downvotes`) VALUES (1,'What is a NullPointerException, and how do I fix it?','What are Null Pointer Exceptions\n> java.lang.NullPointerException\n\nand what causes them? \\\n\nWhat methods/tools can be used to determine the cause so that you stop the exception from causing the program to terminate prematurely?',1,1,0),(2,'How do I return the response from an asynchronous call?','How do I return the response/result from a function foo that makes an asynchronous request? \\\n\nI am trying to return the value from the callback, as well as assigning the result to a local variable inside the function and returning that one, but none of those ways actually return the response — they all return undefined or whatever the initial value of the variable result is. \\\n\nExample of an asynchronous function that accepts a callback (using jQuery\'s ajax function):\n\'\'\'\nfunction foo() {\n    var result;\n\n    $.ajax({\n        url: \'...\',\n        success: function(response) {\n            result = response;\n            // return response; // <- I tried that one as well\n        }\n    });\n\n    return result; // It always returns `undefined`\n}\n\'\'\'\n\nExample using Node.js:\n\'\'\'\nfunction foo() {\n    var result;\n\n    fs.readFile(\"path/to/file\", function(err, data) {\n        result = data;\n        // return data; // <- I tried that one as well\n    });\n\n    return result; // It always returns `undefined`\n}\n\'\'\'\n\nExample using the \'then\' block of a promise:\n\'\'\'\nfunction foo() {\n    var result;\n\n    fetch(url).then(function(response) {\n        result = response;\n        // return response; // <- I tried that one as well\n    });\n\n    return result; // It always returns `undefined`\n}\n\'\'\'',2,1,1),(3,'How can I prevent SQL injection in PHP?','If user input is inserted without modification into an SQL query, then the application becomes vulnerable to SQL injection, like in the following example:\n\'\'\'\n$unsafe_variable = $_POST[\'user_input\']; \n\nmysql_query(\"INSERT INTO `table` (`column`) VALUES (\'$unsafe_variable\')\");\n\'\'\'\n\nThat\'s because the user can input something like \n\'\'\'\nvalue\'); DROP TABLE table;--\n\'\'\'\nand the query becomes:\n\'\'\'\nINSERT INTO `table` (`column`) VALUES(\'value\'); DROP TABLE table;--\')\n\'\'\'\n\nWhat can be done to prevent this from happening?',3,2,0);
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'byteboarddb_qnaforum'
--

--
-- Dumping routines for database 'byteboarddb_qnaforum'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-04 11:52:04
