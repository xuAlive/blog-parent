# Blog Parent

博客系统父项目 - 基于 Spring Boot 3.x 的多模块应用

## 项目结构

```
blog-parent/
├── blog-common/     # 公共模块（JWT工具、权限注解、用户上下文）
├── blog/            # 博客应用（文章管理、用户系统、AI对话）
├── schedule-app/    # 排班系统（员工排班管理）
├── calendar-app/    # 日历系统（日历记事、提醒、周期计划）
└── timesheet-app/   # 工时记录系统（签到记工、人工记工、结算）
```

## 技术栈

| 技术 | 版本 |
|------|------|
| Java | 21 |
| Spring Boot | 3.5.10 |
| MyBatis-Plus | 3.5.12 |
| MySQL | 8.x |
| Druid | 1.2.27 |
| Liquibase | - |
| JWT | 4.5.0 |

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.6+
- MySQL 8.x

### 构建项目

```bash
# 编译所有模块
mvn clean install

# 跳过测试编译
mvn clean install -DskipTests
```

### 运行应用

**博客应用** (端口 6101)
```bash
cd blog
mvn spring-boot:run
```

**排班应用**
```bash
cd schedule-app
mvn spring-boot:run
```

### 配置数据库

复制配置文件并修改数据库连接：

```bash
cp blog/.env.example blog/.env
```

## 模块说明

### blog-common
公共模块，提供：
- `@RequireRole` / `@RequirePermission` - 权限注解
- `JWTUtil` - JWT 令牌工具
- `UserContext` - 用户上下文（ThreadLocal）
- `Response<T>` - 统一响应封装

### blog
博客主应用，功能包括：
- 文章管理（CRUD）
- 用户系统（注册、登录、JWT认证）
- 权限系统（RBAC 角色权限控制）
- DeepSeek AI 对话集成

### schedule-app
排班管理系统，功能包括：
- 班次管理
- 员工排班

### calendar-app
日历系统，功能包括：
- 日历事件
- 提醒与周期计划

### timesheet-app
工时记录系统，功能包括：
- 项目与成员管理
- 员工签到、离班签到、补签审批
- 项目创建者人工维护工数
- 单人结算、批量结算、项目结束结算
- 输入每工薪资后输出本次薪资

## 开发规范

- 所有 API 返回 `Response<T>` 统一格式
- 数据库变更使用 Liquibase 管理
- 权限控制使用注解 `@RequireRole` / `@RequirePermission`

## License

MIT
