# bill2db

bill2db 是一个简单易用的账单数据管理工具，可以将支付宝和微信的账单CSV文件导入到数据库中进行统一管理。

## 功能特点

- 支持导入支付宝和微信的账单CSV文件
- 支持批量上传多个账单文件
- 支持为账单添加自定义标签，方便分类查询
- 支持导出数据库中的账单为SQL文件
- 支持SQLite和MySQL两种数据库
- 简洁的Web界面，操作便捷
- Docker支持，易于部署
- 数据默认存储本地无需联网

## 快速开始

### 方式一：直接运行

1. 确保已安装Java环境、Maven环境
2. 在项目根目录下 mvn clean package
3. 运行应用：
   ```bash
   java -jar ./target/bill2db.jar
   ```
4. 访问 http://localhost:8080 即可使用
   - 支持批量上传账单CSV文件
   - 支持添加自定义标签
   - 支持清空数据库
   - 支持导出账单数据为SQL文件

### 方式二：使用Docker

1. 拉取镜像：
   ```bash
   docker pull jazzylol/bill2db:latest
   ```

2. 运行容器：
   ```bash
   docker run -d -p 8080:8080 jazzylol/bill2db:latest
   ```

3. 访问 http://localhost:8080 即可使用
   - 数据默认存储在容器内的SQLite数据库中
   - 可以通过环境变量配置使用外部MySQL数据库

### 方式三：使用Docker Compose

1. 创建docker-compose.yml文件：
   ```yaml
   version: '3'
   services:
     bill2db:
       image: jazzylol/bill2db:latest
       ports:
         - "8080:8080"
       restart: unless-stopped
   ```

2. 在docker-compose.yml所在目录执行：
   ```bash
   docker-compose up -d
   ```

3. 访问 http://localhost:8080 即可使用

## 数据库配置

### SQLite (默认)
无需配置，默认文件路径/app/bill.db

### MySQL
通过环境变量配置:
- SPRING_DATASOURCE_URL=jdbc:mysql://host:port/bill
- SPRING_DATASOURCE_USERNAME=your_username
- SPRING_DATASOURCE_PASSWORD=your_password

## 注意事项

1. 支持的账单格式:
   - 支付宝: 账单明细CSV文件
   - 微信: 账单明细CSV文件

2. 文件编码支持:
   - 自动识别UTF-8和GBK编码
   - 建议使用原始编码，不要手动转换

3. 数据导出:
   - 导出的SQL文件包含建表语句和数据
   - 可用于备份或迁移数据

## License

[MIT License](LICENSE)