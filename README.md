# 苍穹外卖 (Sky Take Out 2.0)

## 项目简介

苍穹外卖2.0是一个基于 Spring Boot 3 的外卖点餐系统，对原先基于SpringBoot2框架进行的升级。版本包含管理端和用户端两大模块。管理端提供菜品管理、订单处理、数据统计等功能；用户端支持在线浏览菜单、下单支付、地址管理等完整的外卖点餐流程，与上一个版本保持一致

## 技术栈

### 后端框架
- **Spring Boot 3.5.7** - 核心框架
- **Java 24** - 开发语言
- **MyBatis-Plus 3.5.5** - ORM框架
- **MySQL 9.4.0** - 关系型数据库
- **Redis** - 缓存中间件

### 安全与认证
- **JWT (jjwt 0.12.3)** - Token认证
- **Spring AOP** - 切面编程

### 接口文档
- **Knife4j 4.4.0** - OpenAPI 3 接口文档

### 工具库
- **Lombok 1.18.42** - 简化代码
- **Apache Commons Lang3 3.20.0** - 工具类
- **PageHelper 2.1.0** - 分页插件
- **Aliyun OSS 3.18.4** - 对象存储
- **Apache POI 5.2.4** - Excel导出

### 其他
- **WebSocket** - 实时消息推送
- **定时任务** - 订单超时处理
- **Maven** - 项目管理

## 项目结构
sky-take-out-2.0/ ├── sky-common/ # 公共模块 │ ├── constant/ # 常量定义 │ ├── context/ # 上下文管理 │ ├── enumeration/ # 枚举类 │ ├── exception/ # 自定义异常 │ ├── properties/ # 配置属性 │ ├── result/ # 统一返回结果 │ └── utils/ # 工具类 │ ├── sky-pojo/ # 实体类模块 │ ├── dto/ # 数据传输对象 │ ├── entity/ # 实体类 │ └── vo/ # 视图对象 │ └── sky-server/ # 服务模块 ├── controller/ # 控制器 │ ├── admin/ # 管理端接口 │ └── user/ # 用户端接口 ├── service/ # 业务逻辑层 ├── mapper/ # 数据访问层 ├── config/ # 配置类 ├── interceptor/ # 拦截器 ├── aspect/ # 切面 ├── task/ # 定时任务 └── webSocket/ # WebSocket配置
## 功能模块

### 管理端功能

#### 1. 员工管理
- 员工登录/登出
- 员工信息增删改查
- 员工账号启用/禁用
- 分页查询员工列表

#### 2. 分类管理
- 菜品分类增删改查
- 套餐分类增删改查
- 分类排序管理
- 分页查询分类列表

#### 3. 菜品管理
- 菜品新增（含口味配置）
- 菜品修改/删除
- 菜品起售/停售
- 分页查询菜品
- 根据分类查询菜品

#### 4. 套餐管理
- 套餐新增（关联菜品）
- 套餐修改/删除
- 套餐起售/停售
- 分页查询套餐

#### 5. 订单管理
- 订单详情查询
- 订单状态管理（接单、拒单、取消、派送、完成）
- 订单搜索与分页
- 订单统计分析

#### 6. 数据统计
- 营业额统计（按日查询）
- 用户统计（新增用户、总用户数）
- 订单统计（订单量、完成率）
- 销量Top10排行

#### 7. 工作台
- 今日数据概览
- 订单管理快捷入口
- 热销菜品统计

#### 8. 其他功能
- 文件上传（阿里云OSS）
- 店铺营业状态管理

### 用户端功能

#### 1. 用户认证
- 微信登录
- JWT Token认证

#### 2. 菜单浏览
- 菜品分类查询
- 套餐分类查询
- 根据分类查看菜品/套餐详情

#### 3. 购物车
- 添加/删除商品
- 修改商品数量
- 清空购物车
- 查看购物车列表

#### 4. 地址管理
- 新增收货地址
- 修改/删除地址
- 设置默认地址
- 查询地址列表

#### 5. 订单功能
- 提交订单
- 订单支付（模拟）
- 查询历史订单
- 订单详情查看
- 再来一单
- 用户催单（WebSocket推送）

## 快速开始

### 环境要求

- JDK 24+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 配置说明

1. **复制配置文件模板**
bash cp sky-server/src/main/resources/application-dev.yml.example sky-server/src/main/resources/application-dev.yml
2. **修改配置信息** (`application-dev.yml`)
yaml sky: datasource: host: localhost port: 3306 database: sky_take_out username: root password: your_password
redis: host: localhost port: 6379 database: 0
alioss: endpoint: oss-cn-hangzhou.aliyuncs.com access-key-id: your_access_key_id access-key-secret: your_access_key_secret bucket-name: your_bucket_name
wechat: appid: your_wechat_appid secret: your_wechat_secret
3. **初始化数据库**
- 创建数据库 `sky_take_out`
- 导入 SQL 脚本（需自行准备）

### 启动项目

bash
编译项目
mvn clean install
启动应用
cd sky-server mvn spring-boot:run
启动成功后访问：
- **应用地址**: http://localhost:8080
- **接口文档**: http://localhost:8080/doc.html

## API 接口

### 管理端接口 ( `/admin/*` )

| 模块 | 路径前缀 | 说明 |
|------|---------|------|
| 员工管理 | `/admin/employee` | 员工CRUD、登录 |
| 分类管理 | `/admin/category` | 分类CRUD |
| 菜品管理 | `/admin/dish` | 菜品CRUD、口味管理 |
| 套餐管理 | `/admin/setmeal` | 套餐CRUD |
| 订单管理 | `/admin/order` | 订单查询、状态管理 |
| 数据统计 | `/admin/report` | 营业额、用户、订单统计 |
| 工作台 | `/admin/workspace` | 工作台数据 |
| 通用接口 | `/admin/common` | 文件上传等 |
| 店铺管理 | `/admin/shop` | 营业状态 |

### 用户端接口 ( `/user/*` )

| 模块 | 路径前缀 | 说明 |
|------|---------|------|
| 用户认证 | `/user/user` | 微信登录 |
| 分类查询 | `/user/category` | 菜品/套餐分类 |
| 菜品查询 | `/user/dish` | 菜品列表 |
| 套餐查询 | `/user/setmeal` | 套餐列表 |
| 购物车 | `/user/shoppingCart` | 购物车操作 |
| 地址管理 | `/user/addressBook` | 地址CRUD |
| 订单管理 | `/user/order` | 下单、查询、催单 |
| 店铺状态 | `/user/shop` | 营业状态查询 |

## 核心技术实现

### 1. JWT 认证
- 管理端和用户端使用不同的 Token
- 通过拦截器验证 Token 有效性
- 使用 ThreadLocal 存储当前用户ID

### 2. 数据缓存
- 使用 Redis 缓存菜品、套餐数据
- 店铺营业状态缓存
- 缓存更新策略：写操作时清除缓存

### 3. 分页查询
- 使用 PageHelper 实现物理分页
- 统一返回 `PageResult` 格式

### 4. WebSocket 实时推送
- 用户催单时向管理端推送消息
- 订单状态变更通知

### 5. 定时任务
- 每分钟检查超时未支付订单
- 自动取消超时订单

### 6. AOP 切面
- 操作日志记录
- 统一异常处理

### 7. 事务管理
- 使用 `@Transactional` 保证数据一致性
- 订单创建、库存扣减等操作的事务控制

## 注意事项

1. **敏感配置**: `application-dev.yml` 和 `application-prod.yml` 已加入 `.gitignore`，请勿提交到版本控制系统
2. **数据库密码**: 建议使用环境变量或配置中心管理敏感信息
3. **OSS配置**: 需要先在阿里云开通OSS服务并获取AccessKey
4. **微信登录**: 需要在微信公众平台配置AppID和Secret

## 常见问题

### 1. 启动失败：数据库连接错误
- 检查 MySQL 是否启动
- 确认 `application-dev.yml` 中的数据库配置正确
- 确保数据库已创建并导入SQL脚本

### 2. Redis 连接失败
- 检查 Redis 服务是否启动
- 确认 Redis 配置（host、port、password）

### 3. 接口文档无法访问
- 确认 Knife4j 依赖已正确引入
- 访问地址：http://localhost:8080/doc.html

### 4. 文件上传失败
- 检查阿里云 OSS 配置是否正确
- 确认 Bucket 权限设置

## 开发规范

- 使用 Lombok 简化实体类代码
- 统一使用 `Result` 封装返回结果
- 异常使用自定义异常类
- Controller 层只做参数接收和响应返回
- 业务逻辑写在 Service 层
- 数据库操作使用 MyBatis-Plus + XML

## 许可证

本项目仅供学习交流使用。

## 联系方式

如有问题，请提 Issue 或联系开发者。


