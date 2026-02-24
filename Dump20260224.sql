CREATE DATABASE  IF NOT EXISTS `cosmetics_shop` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `cosmetics_shop`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: cosmetics_shop
-- ------------------------------------------------------
-- Server version	8.0.44

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
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_id` varchar(50) NOT NULL,
  `category_name` varchar(100) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `category_id` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'CAT01','Skincare','Products for skin health and hydration'),(2,'CAT02','Base','Foundation and primer for a smooth canvas'),(3,'CAT03','Eye','Makeup for eyes including liners and shadows'),(4,'CAT04','Lip','Products for lip color and care'),(5,'CAT05','Face','Blush and highlighters for face contouring');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `number` varchar(20) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `address` text,
  `level` int DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `customer_id` (`customer_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'C001','Dragon Lord','0911000111','dragon888','pwd123','Volcano Cave No.1',5),(2,'C002','Ice Queen','0922000222','icequeen','frosty','Glacier Palace',4),(3,'C003','Shadow Thief','0933000333','phantom','darkness','Unknown Forest',2),(4,'C004','Golden Golem','0944000444','gold_rich','shiny','Treasure Room 5F',3),(5,'C005','Slime King','0955000555','jelly','bounce','Green Meadow',1),(6,'C006','Pai Pai','0911222333','paipai123','paipaisocute','cosmed 262',5);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_detail`
--

DROP TABLE IF EXISTS `order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` varchar(50) DEFAULT NULL,
  `product_id` varchar(50) DEFAULT NULL,
  `product_name` varchar(100) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `unit_price` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `order_detail_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `order_detail_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_detail`
--

LOCK TABLES `order_detail` WRITE;
/*!40000 ALTER TABLE `order_detail` DISABLE KEYS */;
INSERT INTO `order_detail` VALUES (6,'ORD-1771829319106','P003','Primer',1,30),(7,'ORD-1771829319106','P005','Eyeliner',3,15),(8,'ORD-1771840498489','P003','Primer',1,30),(9,'ORD-1771840498489','P008','Lip Balm',1,10),(12,'ORD-1771846747468','P002','Moisturizer',1,45),(13,'ORD-1771846747468','P005','Eyeliner',1,15),(14,'ORD-1771840526638','P007','Lipstick',1,22),(15,'ORD-1771900200376','P007','Lipstick',1,22),(16,'ORD-1771900200376','P009','Blusher',1,18),(17,'ORD-1708400000001','P001','Radiance Serum',2,150),(18,'ORD-1708400000001','P004','Hydrating Cleanser',1,45),(19,'ORD-1708500000002','P002','Night Repair Cream',1,120),(20,'ORD-1708500000002','P005','Anti-Aging Essence',1,195),(21,'ORD-1708600000003','P003','Gold Eye Mask',3,85),(22,'ORD-1708700000004','P001','Radiance Serum',1,150),(23,'ORD-1708700000004','P005','Anti-Aging Essence',2,195),(24,'ORD-1771903752516','P002','Moisturizer',1,45),(25,'ORD-1771903752516','P008','Lip Balm',1,10),(26,'ORD-1771903752516','P011','BB Cream',3,30),(27,'ORD-1771923462336','P002','Moisturizer',1,45),(28,'ORD-1771923462336','P008','Lip Balm',1,10),(29,'ORD-1771923462336','P010','Highlighter',1,28),(30,'ORD-1771923462336','P011','BB Cream',1,30);
/*!40000 ALTER TABLE `order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` varchar(50) NOT NULL,
  `customer_id` varchar(50) DEFAULT NULL,
  `sales_id` varchar(50) DEFAULT NULL,
  `total_amount` double DEFAULT NULL,
  `order_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id` (`order_id`),
  KEY `customer_id` (`customer_id`),
  KEY `sales_id` (`sales_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`sales_id`) REFERENCES `sales` (`sales_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'ORD-1771829319106','C001','S001',75,'2026-02-23 14:48:39'),(3,'ORD-1771840498489','C001','S001',40,'2026-02-23 17:54:58'),(4,'ORD-1771840526638','C001','S001',22,'2026-02-23 17:55:26'),(5,'ORD-1771846747468','C001','S001',60,'2026-02-23 19:39:07'),(6,'ORD-1771900200376','C004','S001',40,'2026-02-24 10:30:00'),(7,'ORD-1708400000001','C001','S002',345,'2026-02-20 14:30:00'),(8,'ORD-1708500000002','C002','S003',315,'2026-02-21 16:15:00'),(9,'ORD-1708600000003','C003','S002',255,'2026-02-23 11:45:00'),(10,'ORD-1708700000004','C001','S004',540,'2026-02-24 09:20:00'),(11,'ORD-1771903752516','C006','S005',145,'2026-02-24 11:29:12'),(12,'ORD-1771923462336','C006','S005',113,'2026-02-24 16:57:42');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` varchar(50) NOT NULL,
  `product_name` varchar(100) NOT NULL,
  `category_id` varchar(50) DEFAULT NULL,
  `category_name` varchar(100) DEFAULT NULL,
  `price` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_id` (`product_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'P001','Toner','CAT01','Skincare',25),(2,'P002','Moisturizer','CAT01','Skincare',45),(3,'P003','Primer','CAT02','Base',30),(4,'P004','Foundation','CAT02','Base',55),(5,'P005','Eyeliner','CAT03','Eye',15),(6,'P006','Eyeshadow','CAT03','Eye',20),(7,'P007','Lipstick','CAT04','Lip',22),(8,'P008','Lip Balm','CAT04','Lip',10),(9,'P009','Blusher','CAT05','Face',18),(10,'P010','Highlighter','CAT05','Face',28),(11,'P011','BB Cream','CAT02','Base',30);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sales_id` varchar(50) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `address` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sales_id` (`sales_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (1,'S001','Expert Salesman','admin','admin123','Company HQ, Taipei'),(2,'S002','Aria Montgomery','aria_vip','aria123','Taipei 101 Boutique (台北101精品店)'),(3,'S003','Liam Chen','liam_pro','liam123','Bellavita Flagship Store (寶麗廣塲旗艦店)'),(4,'S004','Sophia Lin','sophia_elite','sophia123','SOGO BR4 Counter (復興SOGO專櫃)'),(5,'S005','Bobo','bobo123','bobosocute','forever green 262');
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-24 17:05:08
