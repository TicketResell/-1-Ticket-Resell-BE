-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: localhost    Database: ticket_resell_db
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `categories` (
                              `id` int(11) NOT NULL AUTO_INCREMENT,
                              `category_name` varchar(255) DEFAULT NULL,
                              `created_date` datetime(6) DEFAULT NULL,
                              `category_image` varchar(255) DEFAULT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Music','2024-09-30 20:35:29.000000','https://i.ibb.co/dGWvdCs/music.png'),(2,'Sports','2024-09-30 20:35:29.000000','https://i.ibb.co/tpNzpS0/sport.png?utm_source=zalo&utm_medium=zalo&utm_campaign=zalo'),(3,'E-Sports','2024-09-30 20:35:29.000000','https://i.ibb.co/4N7yGW6/esports.png?utm_source=zalo&utm_medium=zalo&utm_campaign=zalo'),(4,'Musiz',NULL,NULL);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_message`
--

DROP TABLE IF EXISTS `chat_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `chat_message` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                `sender_id` bigint(20) NOT NULL,
                                `receiver_id` bigint(20) NOT NULL,
                                `message_content` varchar(1000) NOT NULL,
                                `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
                                `chat_type` enum('text','image','bid') NOT NULL DEFAULT 'text',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_message`
--

LOCK TABLES `chat_message` WRITE;
/*!40000 ALTER TABLE `chat_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `json_storage`
--

DROP TABLE IF EXISTS `json_storage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `json_storage` (
                                `Id` int(10) NOT NULL,
                                `userID` int(10) NOT NULL,
                                `json_data` varchar(4500) DEFAULT 'null',
                                PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `json_storage`
--

LOCK TABLES `json_storage` WRITE;
/*!40000 ALTER TABLE `json_storage` DISABLE KEYS */;
/*!40000 ALTER TABLE `json_storage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `orders` (
                          `id` int(11) NOT NULL AUTO_INCREMENT,
                          `buyer_id` int(11) DEFAULT NULL,
                          `seller_id` int(11) DEFAULT NULL,
                          `ticket_id` int(11) DEFAULT NULL,
                          `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                          `order_method` enum('COD','paypal','vnpay') DEFAULT NULL,
                          `quantity` int(11) NOT NULL,
                          `total_amount` decimal(10,2) DEFAULT NULL,
                          `service_fee` decimal(10,2) DEFAULT NULL,
                          `order_status` enum('pending','completed','cancelled') DEFAULT 'pending',
                          `payment_status` enum('pending','completed','cancelled') DEFAULT 'pending',
                          PRIMARY KEY (`id`),
                          KEY `buyer_id` (`buyer_id`),
                          KEY `seller_id` (`seller_id`),
                          KEY `ticket_id` (`ticket_id`),
                          CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`buyer_id`) REFERENCES `users` (`id`),
                          CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`),
                          CONSTRAINT `orders_ibfk_3` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ratings`
--

DROP TABLE IF EXISTS `ratings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `ratings` (
                           `rate_id` int(11) NOT NULL AUTO_INCREMENT,
                           `user_id` int(11) DEFAULT NULL,
                           `order_id` int(11) DEFAULT NULL,
                           `rating_score` int(11) DEFAULT NULL,
                           `feedback` text,
                           `rating_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                           PRIMARY KEY (`rate_id`),
                           KEY `user_id` (`user_id`),
                           KEY `order_id` (`order_id`),
                           CONSTRAINT `ratings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                           CONSTRAINT `ratings_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ratings`
--

LOCK TABLES `ratings` WRITE;
/*!40000 ALTER TABLE `ratings` DISABLE KEYS */;
/*!40000 ALTER TABLE `ratings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket_image_urls`
--

DROP TABLE IF EXISTS `ticket_image_urls`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `ticket_image_urls` (
                                     `ticket_id` int(11) NOT NULL,
                                     `image_url` varchar(255) NOT NULL,
                                     PRIMARY KEY (`ticket_id`,`image_url`),
                                     CONSTRAINT `ticket_image_urls_ibfk_1` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket_image_urls`
--

LOCK TABLES `ticket_image_urls` WRITE;
/*!40000 ALTER TABLE `ticket_image_urls` DISABLE KEYS */;
INSERT INTO `ticket_image_urls` VALUES (1,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(1,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(1,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(2,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(2,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(2,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(3,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(3,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(3,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(5,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(5,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(5,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(6,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(6,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(6,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(7,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(7,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(7,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(8,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(8,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(8,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(9,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(9,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(9,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(10,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(10,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(10,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(11,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(11,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(11,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(12,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(12,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(12,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(13,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(13,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(13,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(14,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(14,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(14,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(15,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(15,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(15,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(16,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(16,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(16,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(17,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(17,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(17,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(18,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(18,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(18,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(19,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(19,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(19,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg'),(20,'https://i.ibb.co/nPqSQmt/OIP-1.jpg'),(20,'https://i.ibb.co/Rh0FKx0/OIP.jpg'),(20,'https://i.ibb.co/ZgMMVP2/Music-Concert-Event-Ticket-Design-870x490.jpg');
/*!40000 ALTER TABLE `ticket_image_urls` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tickets`
--

DROP TABLE IF EXISTS `tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `tickets` (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `user_id` int(11) DEFAULT NULL,
                           `price` float DEFAULT NULL,
                           `event_title` varchar(255) DEFAULT NULL,
                           `event_date` date DEFAULT NULL,
                           `category_id` int(11) DEFAULT NULL,
                           `location` varchar(255) DEFAULT NULL,
                           `ticket_type` varchar(100) DEFAULT NULL,
                           `sale_price` decimal(10,2) DEFAULT NULL,
                           `ticket_details` varchar(255) DEFAULT NULL,
                           `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                           `quantity` int(11) DEFAULT '1',
                           PRIMARY KEY (`id`),
                           KEY `user_id` (`user_id`),
                           KEY `category_id` (`category_id`),
                           CONSTRAINT `tickets_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                           CONSTRAINT `tickets_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tickets`
--

LOCK TABLES `tickets` WRITE;
/*!40000 ALTER TABLE `tickets` DISABLE KEYS */;
INSERT INTO `tickets` VALUES (1,1,50,'Manchester United VS Manchester City','2024-10-12',2,'Old Trafford, Manchester','Standard',60.00,'Premier League football match','2024-09-20 08:34:00',1),(2,2,30,'Sky Tour','2024-11-05',1,'Ho Chi Minh City','VIP',45.00,'Concert by Sơn Tùng M-TP','2024-09-22 03:00:00',1),(3,3,20,'FPT Flash VS SGP','2024-10-22',3,'FPT Arena, Hanoi','Standard',25.00,'Mobile Legends Championship','2024-09-25 04:12:00',1),(5,2,75,'Taylor Swift - The Eras Tour','2024-11-15',1,'Sophie Stadium, LA','VIP',90.00,'Taylor Swift Live Concert','2024-09-29 02:30:00',1),(6,3,60,'Lakers VS Warriors','2024-11-01',2,'Crypto.com Arena, LA','Standard',65.00,'NBA regular season match','2024-09-30 03:20:00',1),(7,1,40,'VietNam National Day Celebration','2024-09-02',2,'Ba Dinh Square, Hanoi','Free',0.00,'Cultural event celebrating Vietnam Independence','2024-08-10 00:55:00',1),(8,1,80,'Formula 1: Abu Dhabi Grand Prix','2024-12-09',1,'Yas Marina Circuit, Abu Dhabi','Premium',95.00,'F1 season finale','2024-09-20 05:15:00',1),(9,2,45,'Coldplay: Music of the Spheres Tour','2024-11-30',2,'Wembley Stadium, London','VIP',55.00,'Coldplay Live Concert','2024-09-25 07:32:00',1),(10,3,35,'World Cup Qualifier: Brazil VS Argentina','2024-11-18',1,'Maracanã Stadium, Rio de Janeiro','Standard',40.00,'FIFA World Cup 2026 Qualifier','2024-09-21 02:40:00',1),(11,1,25,'Vietnam Idol Finals','2024-10-29',2,'Saigon Convention Center','Standard',30.00,'Final of Vietnam Idol 2024','2024-09-23 09:00:00',1),(12,2,20,'Champions League: Barcelona VS PSG','2024-12-12',1,'Camp Nou, Barcelona','Standard',25.00,'Champions League Group Stage','2024-09-19 10:45:00',1),(13,3,50,'Super Bowl LVIII','2024-02-11',1,'Allegiant Stadium, Las Vegas','VIP',65.00,'NFL Championship game','2024-09-30 11:20:00',1),(14,1,35,'SEA Games Opening Ceremony','2024-11-29',3,'My Dinh National Stadium, Hanoi','Standard',40.00,'SEA Games 2024 Opening','2024-09-21 13:30:00',1),(15,2,70,'Rihanna Live: Anti World Tour','2024-12-10',2,'Madison Square Garden, New York','VIP',85.00,'Rihanna Concert','2024-09-18 15:10:00',1),(16,3,30,'Wimbledon Finals','2024-07-14',1,'All England Club, London','Standard',35.00,'Wimbledon Tennis Final','2024-09-22 04:10:00',1),(17,1,25,'Vietnam VS Thailand','2024-10-15',1,'Thống Nhất Stadium, HCMC','Standard',30.00,'AFF Championship Match','2024-09-27 02:50:00',1),(18,2,55,'Billie Eilish: Happier Than Ever Tour','2024-12-20',2,'The O2, London','VIP',65.00,'Billie Eilish Concert','2024-09-20 09:55:00',1),(19,3,40,'V-League Finals: Hanoi FC VS Hoang Anh Gia Lai','2024-11-20',1,'Hang Day Stadium, Hanoi','Standard',45.00,'V-League Finals','2024-09-28 05:45:00',1),(20,1,15,'French Open: Nadal VS Djokovic','2024-06-09',1,'Roland Garros, Paris','Standard',20.00,'French Open Tennis Match','2024-09-23 07:30:00',1);
/*!40000 ALTER TABLE `tickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `transactions` (
                                `id` int(11) NOT NULL AUTO_INCREMENT,
                                `order_id` int(11) NOT NULL,
                                `buyer_id` int(11) NOT NULL,
                                `seller_id` int(11) NOT NULL,
                                `transaction_amount` decimal(10,2) NOT NULL,
                                `transaction_type` enum('Income','Expense','Refund') NOT NULL,
                                `vnp_response_code` varchar(255) DEFAULT NULL,
                                `vnp_transaction_no` varchar(255) DEFAULT NULL,
                                `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                `updated_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                PRIMARY KEY (`id`),
                                KEY `order_id` (`order_id`),
                                KEY `buyer_id` (`buyer_id`),
                                KEY `seller_id` (`seller_id`),
                                CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
                                CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`buyer_id`) REFERENCES `users` (`id`),
                                CONSTRAINT `transactions_ibfk_3` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `users` (
                         `id` int(11) NOT NULL AUTO_INCREMENT,
                         `fullname` varchar(255) DEFAULT NULL,
                         `username` varchar(255) NOT NULL,
                         `password` varchar(255) NOT NULL,
                         `email` varchar(255) NOT NULL,
                         `phone` varchar(20) DEFAULT NULL,
                         `address` varchar(255) DEFAULT NULL,
                         `role` varchar(20) NOT NULL DEFAULT 'USER',
                         `status` enum('active','inactive','banned') DEFAULT 'active',
                         `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         `verified_email` tinyint(1) DEFAULT '0',
                         `user_image` varchar(255) DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         KEY `role_id` (`role`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'John Doe','john_doe','password123','john@example.com','123456789','123 Main St','user','active','2024-09-30 09:51:51',0,'https://th.bing.com/th/id/OIP.nMIr-nB5v0rbPzWEJzeZcQHaE7?https://th.bing.com/th/id/OIP.U5rs3o2TurXbKw5UAH2rAgAAAA?w=210&h=180&c=7&r=0&o=5&dpr=1.1&pid=1.7'),(2,'Jane','jane_seller','password456','jane@example.com','987654321','456 Elm St','staff','active','2024-09-30 09:51:51',0,'https://th.bing.com/th/id/OIP.nMIr-nB5v0rbPzWEJzeZcQHaE7?w=264&h=180&c=7&r=0&o=5&dpr=1.1&pid=1.7'),(3,'ADMIN','admin_user','admin789','admin@example.com','123123123','789 Oak St','admin','active','2024-09-30 09:51:51',1,'https://th.bing.com/th/id/OIP.nMIr-nB5v0rbPzWEJzeZcQHaE7?w=264&h=180&c=7&r=0&o=5&dpr=1.1&pid=1.7'),(14,'Tri dz','trinmse183033@fpt.edu.vn_google','trinmse183033@fpt.edu.vn_password','trinmse183033@fpt.edu.vn',NULL,NULL,'user','active','2024-10-01 06:09:30',1,NULL),(15,'Tri','minhtrifptu@gmail.com_google','minhtrifptu@gmail.com_password','minhtrifptu@gmail.com',NULL,NULL,'user','active','2024-10-01 08:11:28',1,NULL),(16,'Siuuuuu','tridz','Aa@123456','minhtri10504@gmail.com','0938132218',NULL,'user','active','2024-10-03 15:19:47',1,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-20 21:07:05
