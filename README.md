# DailyJournal 个人日记与生活管理系统 📔

这是一个基于 Java Spring Boot 开发的个人日记与生活记录平台。项目集成了日记记录、账单管理、特殊日子提醒等功能，旨在帮助用户更好地记录生活点滴。

## ✨ 核心功能
*   **日记记录**：随时随地记录生活感悟。
*   **消费管理 (Expense)**：记录日常开销，管理个人财务。
*   **特殊日子 (SpecialDay)**：记录纪念日、生日等重要日期。
*   **定时提醒 (Task)**：内置定时任务，提供每日提醒功能。

## 🛠️ 技术栈
*   **后端**：Java 21, Spring Boot 3.1.5, Spring Data JPA, Hibernate
*   **数据库**：MySQL 8.0
*   **前端**：HTML5, CSS3, JavaScript (原生)
*   **构建工具**：Maven

## 🚀 如何启动项目

### 1. 环境准备
请确保你的电脑上安装了以下软件：
*   **JDK 21** 或更高版本
*   **MySQL 8.0+**
*   **Maven** (通常 IDEA 自带，无需单独安装)

### 2. 初始化数据库
1. 打开你的 MySQL 客户端（如 Navicat 或命令行）。
2. 创建一个新的数据库，例如命名为 `dailyjournal`：
   ```sql
   CREATE DATABASE dailyjournal DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
