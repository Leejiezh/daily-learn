# 文件断点分片上传系统

这是一个基于Spring Boot和Resumable.js实现的文件断点分片上传系统，支持大文件上传、断点续传、并发上传等功能。

## 功能特点

### 🚀 核心功能
- **断点续传**：支持网络中断后继续上传，无需重新开始
- **分片上传**：大文件自动分片，每片1MB，提高上传成功率
- **并发上传**：支持同时上传3个分片，提升上传速度
- **进度显示**：实时显示上传进度和分片状态
- **文件管理**：支持文件下载和删除操作

### 🔧 技术特性
- **前端**：HTML5 + Bootstrap + Resumable.js + Axios
- **后端**：Spring Boot + Hutool工具库
- **存储**：本地文件系统存储
- **跨域支持**：支持跨域请求

## 系统架构

```
前端 (Resumable.js)
    ↓ HTTP请求
后端 (Spring Boot)
    ↓ 文件操作
本地文件系统
```

### 文件结构
```
src/main/java/com/daily-learn/fileUpload/
├── controller/
│   ├── FileUploadController.java    # 文件上传控制器
│   └── FileDownloadController.java  # 文件下载控制器
├── service/
│   └── FileUploadService.java       # 文件上传服务
├── dto/
│   └── FileChunkDTO.java           # 文件分片数据传输对象
├── resp/
│   └── FileUploadResponse.java     # 响应对象
└── interceptor/                    # 拦截器配置
```

## 快速开始

### 1. 环境要求
- JDK 17+
- Maven 3.6+
- 现代浏览器（支持HTML5 File API）

### 2. 配置
在 `application.yml` 中配置上传目录：
```yaml
file:
  upload:
    dir: D:/tmp  # 修改为你的上传目录
```

### 3. 启动应用
```bash
mvn spring-boot:run
```

### 4. 访问系统
打开浏览器访问：`http://localhost:8081`

## API接口

### 文件上传相关

#### 1. 检查文件块是否存在
```
GET /api/upload/check
参数：
- chunkNumber: 分片编号
- chunkSize: 分片大小
- currentChunkSize: 当前分片大小
- totalSize: 文件总大小
- identifier: 文件标识
- filename: 文件名
- totalChunks: 总分片数
```

#### 2. 上传文件块
```
POST /api/upload/chunk
参数：
- chunkNumber: 分片编号
- chunkSize: 分片大小
- currentChunkSize: 当前分片大小
- totalSize: 文件总大小
- identifier: 文件标识
- filename: 文件名
- totalChunks: 总分片数
- file: 文件数据
```

#### 3. 合并文件块
```
POST /api/upload/merge
参数：
- identifier: 文件标识
- filename: 文件名
- totalChunks: 总分片数
```

### 文件下载相关

#### 1. 获取文件列表
```
GET /api/download/files
返回：文件名列表
```

#### 2. 下载文件
```
GET /api/download/file/{filename}
返回：文件流
```

#### 3. 删除文件
```
DELETE /api/download/file/{filename}
返回：删除结果
```

## 工作原理

### 1. 文件分片
- 前端使用Resumable.js库将大文件分割成小块（默认1MB）
- 每个分片都有唯一的标识和序号

### 2. 断点续传
- 上传前检查分片是否已存在
- 如果分片存在且大小正确，则跳过该分片
- 只上传缺失或损坏的分片

### 3. 并发上传
- 同时上传多个分片（默认3个）
- 提高上传效率，减少总上传时间

### 4. 文件合并
- 所有分片上传完成后，后端按顺序合并分片
- 使用RandomAccessFile进行高效合并
- 合并完成后清理临时分片文件

## 配置说明

### 前端配置
```javascript
const resumable = new Resumable({
    chunkSize: 1 * 1024 * 1024,        // 分片大小：1MB
    simultaneousUploads: 3,             // 并发上传数：3
    testChunks: true,                   // 启用分片检查
    // ... 其他配置
});
```

### 后端配置
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 100MB              # 最大文件大小
      max-request-size: 100MB           # 最大请求大小
      file-size-threshold: 2KB          # 文件大小阈值

file:
  upload:
    dir: D:/tmp                         # 上传目录
```

## 使用示例

### 1. 上传文件
1. 点击"选择文件"按钮选择要上传的文件
2. 系统会显示文件信息（大小、类型、分片数）
3. 点击"上传文件"开始上传
4. 观察进度条和状态信息
5. 上传完成后文件会出现在文件列表中

### 2. 断点续传测试
1. 开始上传一个大文件
2. 在网络传输过程中断开网络连接
3. 重新连接网络
4. 系统会自动检测已上传的分片并继续上传剩余部分

### 3. 文件管理
- **下载文件**：点击文件列表中的"下载"按钮
- **删除文件**：点击文件列表中的"删除"按钮

## 注意事项

1. **存储空间**：确保上传目录有足够的磁盘空间
2. **文件权限**：确保应用有读写上传目录的权限
3. **网络环境**：建议在稳定的网络环境下进行大文件上传
4. **浏览器兼容性**：需要支持HTML5 File API的现代浏览器

## 扩展功能

### 可能的改进方向
1. **云存储支持**：集成阿里云OSS、腾讯云COS等
2. **数据库存储**：将文件信息存储到数据库
3. **用户认证**：添加用户登录和权限控制
4. **文件预览**：支持图片、文档等文件的在线预览
5. **上传限制**：添加文件类型、大小等限制
6. **进度持久化**：将上传进度保存到本地存储

## 故障排除

### 常见问题

1. **上传失败**
   - 检查网络连接
   - 确认上传目录权限
   - 查看服务器日志

2. **文件合并失败**
   - 检查磁盘空间
   - 确认所有分片都已上传
   - 查看错误日志

3. **跨域问题**
   - 确认后端已配置@CrossOrigin注解
   - 检查浏览器控制台错误信息

## 许可证

本项目采用MIT许可证，详见LICENSE文件。 