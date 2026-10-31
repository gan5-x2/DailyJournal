markdown
# DailyJournal 个人收支记账系统 💰

这是一个基于 Java Spring Boot 开发的个人收支记录管理平台。项目主要用于记录日常的消费开销与收入，帮助用户清晰地管理个人财务流水。

## ✨ 核心功能
*   **记录开销**：随时添加日常消费记录，支持金额、分类、备注等。
*   **记录收入**：添加收入流水，管理资金来源。
*   **流水展示**：分类查看所有的收支明细（对应前端页面展示）。
*   **用户管理**：提供基础的用户鉴权与登录功能（基于 JWT）。

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


CREATE DATABASE dailyjournal DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

3. 配置数据库连接
在项目目录 src/main/resources/ 下找到 application.properties（或 application.yml），修改为你自己的数据库信息：

properties
spring.datasource.url=jdbc:mysql://localhost:3306/dailyjournal?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=你的数据库密码
4. 启动项目（重要）
本项目必须运行 DailyJournalApplication 这个主类才能完整启动。

⚠️ 注意：请不要直接单独运行 Controller 或 Repository 里的文件，否则会报错。

方式一：使用 IDEA 运行（推荐）

使用 IntelliJ IDEA 打开本项目，等待 Maven 依赖下载完毕。

在项目结构中找到这个文件：src/main/java/org/example/dailyjournal/DailyJournalApplication.java。

点击 DailyJournalApplication 类名旁边的绿色三角形 Run 按钮。

看到控制台输出 Started DailyJournalApplication in x.x seconds 即代表启动成功。

方式二：使用命令行运行
在项目根目录下，打开终端执行：

bash
mvn spring-boot:run
5. 访问应用
项目启动成功后，打开浏览器访问：
👉 http://localhost:8080

📂 项目结构
text
DailyJournal
├── src
│   ├── main
│   │   ├── java/org/example/dailyjournal  # 核心业务代码
│   │   │   ├── controller                 # 控制层
│   │   │   ├── entity                     # 数据库实体类 (Expense, User 等)
│   │   │   ├── repository                 # 数据访问层
│   │   │   └── DailyJournalApplication    # 【项目唯一启动入口】
│   │   └── resources
│   │       ├── static                     # 前端静态资源 (HTML/CSS/JS)
│   │       └── application.properties     # 配置文件
│   └── test                               # 测试代码
└── pom.xml                                # Maven 依赖配置
如果这个项目对你有帮助，欢迎点个 Star ⭐！
