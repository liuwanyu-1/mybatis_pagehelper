# 环境配置教程 —— 从零把 mybatis_pagehelper 跑起来（超详细版）

> 一步一步照着做，每一步都有"这一步在干什么"和"做完应该看到什么"。
> 出问题直接翻 **第 7 节报错速查表**，你遇到的坑大概率我们全踩过。

---

## 0. 这个项目是什么

一个 **Maven Web 项目**：MySQL 存班级表 → MyBatis 查数据 → PageHelper 分页插件自动拼 limit → Servlet 接收请求 → JSP 渲染表格 + 上一页/下一页。

技术栈与版本（**版本对应关系不能乱**，后面所有报错一半是版本不配）：

| 组件 | 版本 | 说明 |
|---|---|---|
| JDK | 17 | 编译运行都用它 |
| Tomcat | **10.1.x** | 必须是 Tomcat 10+，原因见下面大坑 |
| Maven | IDEA 自带 3.x | 不用单独装 |
| MySQL | 8.x（5.7 也行，见第 8 节） | 库名 `design`，表名 `class` |
| MyBatis | 3.5.19 | |
| PageHelper | 6.1.1 | 分页插件 |
| servlet-api | jakarta.servlet 6.0.0 | 跟 Tomcat 10 配套 |

### ⚠️ 全文最大的坑：Tomcat 9 和 Tomcat 10 不兼容

Tomcat 10 开始，Servlet 包名从 `javax.servlet.*` 改成了 **`jakarta.servlet.*`**。

- 你用 **Tomcat 10** → 什么都不用改，项目直接跑
- 你只有 **Tomcat 9 / 8** → 项目跑不起来，会报 `ClassNotFoundException: jakarta.servlet...`，改法见 **第 8 节变体篇**

先确认你机器上的是哪个：看解压目录名（`apache-tomcat-10.1.59` = 10），或进 `RELEASE-NOTES` 看。

---

## 1. 环境准备清单

### 1.1 JDK 17

1. 装好 JDK 17（IDEA 里也可以直接下载：File → Project Structure → SDK → 加号 → Download JDK）
2. 验证：Win+R 输入 `cmd`，敲 `java -version`，看到 `17.x.x` 即可
3. 建议顺手配环境变量 `JAVA_HOME` = JDK 安装目录（Tomcat 命令行启动时要用，IDEA 内启动可以不配）

### 1.2 IntelliJ IDEA

⚠️ **必须是旗舰版（Ultimate）**。社区版（Community）**没有 Tomcat 集成**，跑 Web 项目要么装 Smart Tomcat 插件，要么手动把 war 扔进 Tomcat 的 webapps（见第 8 节）。学生可以用学校邮箱免费申请旗舰版。

### 1.3 Tomcat 10.1.x

1. 官网 https://tomcat.apache.org → 左侧 Download → Tomcat 10 → **Core → zip** 下载
2. **解压到纯英文、不带空格的路径**（这是血的教训：路径带空格/中文会让各种脚本莫名崩）
   - 推荐：`C:\tomcat\apache-tomcat-10.1.59`
   - 我自己放的是 `C:\Users\<用户名>\Documents\ideal\apache-tomcat-10.1.59`
3. 不需要安装、不需要配 CATALINA_HOME（IDEA 里跑不用配；想命令行跑才要）

### 1.4 MySQL 8.x

两种装法选一种：

- **phpStudy 小皮面板**（推荐，课堂同款）：下载 phpStudy → 软件管理装 MySQL 8.0.12 → 首页启动 MySQL，绿灯即启动成功
- **MySQL 官方安装器**：装完是 Windows 服务，`services.msc` 里能看到 MySQL80，设为自动启动

验证：cmd 里 `netstat -ano | findstr 3306`，有 `LISTENING` 就说明 MySQL 活着。

### 1.5 Maven 阿里云镜像（强烈建议，不然下载依赖能等到天荒地老）

IDEA 自带 Maven，只要配一个 `settings.xml`：

1. 找到（没有就新建）`C:\Users\<用户名>\.m2\settings.xml`
2. 内容如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0">
  <mirrors>
    <mirror>
      <id>aliyun</id>
      <mirrorOf>central</mirrorOf>
      <name>阿里云公共仓库</name>
      <url>https://maven.aliyun.com/repository/public</url>
    </mirror>
  </mirrors>
</settings>
```

3. IDEA 里确认生效：File → Settings → 搜索 `maven` → User settings file 勾上 Override 指向这个文件

---

## 2. 导入项目

1. 拿到项目（git clone 或直接下 zip 解压），记住放在**纯英文路径**
2. IDEA → **File → Open** → 选中项目里的 **`pom.xml`** → Open as Project
3. 弹窗 **Trust Project** 点信任
4. 右下角会开始 Maven 同步（下载依赖），等进度条走完，左边的 External Libraries 里能看到 mybatis、pagehelper 这些包
5. 检查 JDK：File → Project Structure → Project → SDK 选 17，Language level 选 17

> 做完应该看到：项目结构里 src/main/java、src/main/resources、src/main/webapp 都正常显示，没有红色报错。

---

## 3. 数据库准备

### 3.1 建库建表

启动 MySQL 后，用 Navicat / phpStudy 自带的数据库工具 / cmd 客户端，执行：

```sql
CREATE DATABASE IF NOT EXISTS design DEFAULT CHARSET utf8mb4;
USE design;

CREATE TABLE class (
    cid   INT PRIMARY KEY,
    cno   VARCHAR(20),
    cname VARCHAR(20),
    tid   INT
);

-- 数据自己随意插，10 条左右分页效果比较明显
INSERT INTO class VALUES
(1,'01','一班',1),(2,'02','二班',2),(3,'03','三班',3),(4,'04','四班',4),(5,'05','五班',5),
(6,'06','六班',6),(7,'07','七班',7),(8,'08','八班',8),(9,'09','九班',9),(10,'10','十班',10);
```

### 3.2 改成你自己的数据库账号密码

打开 `src/main/resources/mybatis-config.xml`，改这三行：

```xml
<property name="url" value="jdbc:mysql://localhost:3306/design?serverTimezone=Asia/Shanghai"/>
<property name="username" value="root"/>
<property name="password" value="123456"/>
```

- 数据库不叫 `design` → 改 url 里的库名
- 密码不是 123456 → 改 password（**不改必报 Access denied**）

### 3.3 确认 MySQL 处于启动状态

phpStudy 面板看绿灯，或 `netstat -ano | findstr 3306`。

---

## 4. IDEA 配置 Tomcat（重点，跟着点）

1. 顶部菜单 **Run → Edit Configurations…**
2. 左上角 **＋** → 选 **Tomcat Server → Local**（⚠️ 不是 TomEE，也别选成 Application）
3. **Application server** 一行点 **Configure…**，选中你解压的 Tomcat 根目录（里面要有 bin、conf、webapps 的那个）
4. Name 随便起，比如 `Tomcat 10.1`
5. **HTTP port** 保持 `8080`（被占用就换 8081，见速查表 #3）
6. 切到 **Deployment** 标签页：
   - 点 **＋ → Artifact…**
   - 选 **`mybatis_pagehelper:war exploded`**（⚠️ 有 war 和 war exploded 两个，别漏选 exploded）
   - 下面的 **Application context** 填 `/mybatis_pagehelper`
7. 回 **Server** 标签页：
   - **On 'Update' action** → 选 `Update classes and resources`（改代码后能热更新）
   - **After launch**：默认会勾"启动后打开浏览器"，把 URL 改成
     `http://localhost:8080/mybatis_pagehelper/queryClazzes`（启动直接看到分页页面）
8. **Before launch** 区域确认有 `Build`，点 OK

> **war 和 war exploded 有什么区别？**
> - `war`：部署压缩包，每次改动都要重新打包，慢
> - `war exploded`：部署解压目录，改 JSP 刷新即生效，改 Java 秒级热更新
> - **日常开发选 exploded**，交作业要交 war 包时再用 `mvn package` 打

---

## 5. 启动与验证

1. 确认 MySQL 已启动（第 3.3 节）
2. 点右上角绿色 ▶ 启动
3. 等控制台出现 `Server startup in [xxx] milliseconds`
4. 浏览器自动打开（或手动访问）：

```
http://localhost:8080/mybatis_pagehelper/queryClazzes
```

5. **应该看到**：班级表格、`共 10 条记录，共 5 页，当前第 1 页，每页 2 条`、上一页是灰的、下一页能点
6. 可选 URL 参数：`?pageNumber=3&pageSize=4` = 第 3 页每页 4 条；传个 `pageNumber=999` 会自动修正到最后一页（reasonable 分页合理化在起作用）

> 访问根路径 `http://localhost:8080/mybatis_pagehelper/` 会自动跳到上面这个页面（index.jsp 里写了跳转）。

---

## 6. 项目代码结构速览（老师答辩用）

```
请求链路：浏览器 → Servlet → Service → DAO(MyBatis) → MySQL → 原路返回 → JSP 渲染
```

| 文件 | 作用 |
|---|---|
| `dao/ClazzMapper` | `selectAll()` 查全部、`selectByPage()` 手写 limit、`selectCount()` 统计总条数 |
| `service/IClazzService` + `impl/ClazzServiceImpl` | `PageHelper.startPage(pageNum,pageSize)` 写在查询**上一行**，再调 `selectAll()`，插件自动改 SQL 拼 limit |
| `servlet/QueryClazzesByPage` | 收 pageNumber/pageSize（空则默认 1 和 2）→ 调 Service → 算总页数 `(total+pageSize-1)/pageSize` → 存 **request 域** → **请求转发** JSP |
| `webapp/clazzList.jsp` | JSTL `c:forEach` 渲染表格、`${pageContext.request.contextPath}` 拼翻页链接、首页/末页按钮置灰 |
| `mybatis-config.xml` | `<plugins>` 里配 PageInterceptor + `reasonable=true` |

**手写 limit vs PageHelper（一句话）**：手写要自己算 `pageStart=(pageNumber-1)*pageSize` 再写进 SQL；PageHelper 底层还是 limit，只是用拦截器帮你自动算自动拼。考试考手写原理，工作用插件。

---

## 7. 报错速查表（先对号入座再问人）

| # | 症状 | 原因 | 解决 |
|---|---|---|---|
| 1 | 浏览器显示 **Hello World!** | 骨架自带的 index.jsp，不是出错 | 访问 `/queryClazzes`；或本项目 index.jsp 已改成自动跳转 |
| 2 | 404 Not Found | Application context 不对 / Deployment 没加 artifact | 检查第 4 节第 6 步；URL 的路径要和 Application context 一致 |
| 3 | 启动报 **Port 8080 already in use** | 8080 被别的 Tomcat/程序占了 | 改第 4 节第 5 步的 HTTP port 为 8081，URL 端口同步改；或 cmd `netstat -ano | findstr 8080` 找到 PID 用 `taskkill /pid xxx /f` 杀掉 |
| 4 | **Communications link failure** | MySQL 没启动 | phpStudy 点启动 / 检查 MySQL80 服务 |
| 5 | **Access denied for user 'root'@'localhost'** | mybatis-config.xml 密码和数据库不一致 | 改第 3.2 节的 password |
| 6 | **Unknown database 'design'** | 没建库 | 执行第 3.1 节 SQL |
| 7 | **Table 'design.class' doesn't exist** | 没建表或表名大小写不对 | 表名就是小写 `class` |
| 8 | **ClassNotFoundException: jakarta.servlet...** | 用了 Tomcat 9/8 | 换 Tomcat 10，或看第 8 节变体篇 |
| 9 | 页面把 **${clazzes} 原样显示** | web.xml 太老（2.3 DTD 不求值 EL） | 换成 `web-app_6_0.xsd` 头（本项目已换好） |
| 10 | JSP 报 **The absolute uri [jakarta.tags.core] cannot be resolved** | Tomcat 10 没自带 JSTL | pom 里的 jstl 两个依赖别删（jakarta 版 3.0.x）；Tomcat 9 则要换成旧 uri `http://java.sun.com/jsp/jstl/core` + 旧 jstl 包 |
| 11 | 控制台/页面**中文乱码** | 编码不一致 | IDEA 帮助→编辑自定义 VM 选项加 `-Dfile.encoding=UTF-8`；运行配置 VM options 同样加 |
| 12 | 启动报 **must match "(properties?,settings?,typeAliases?…** | mybatis-config 里 `<plugins>` 插的位置不对 | plugins 必须在 `</typeAliases>` 之后、`<environments>` 之前（DTD 顺序） |
| 13 | Maven 下载依赖极慢/超时 | 没配国内镜像 | 第 1.5 节阿里云镜像 |
| 14 | **PageHelper.startPage 之后必须紧跟查询** | 中间穿插了别的数据库操作 | startPage 和查询之间不能有其他 SQL，这是插件的规矩 |
| 15 | 改了代码/配置**没生效** | IDEA 缓冲区没刷新 或 没重新构建 | File → Reload All from Disk；然后点运行窗口的小锤子（Build）触发 Update |

---

## 8. 环境不一样的同学（变体篇）

### 8.1 你只有 Tomcat 9 / 8

Tomcat 9 及以下用 `javax.servlet`，本项目用 `jakarta.servlet`，**不能混**。要么换 Tomcat 10（推荐，只下个 zip 的事），要么全套降级：

| 改动点 | Tomcat 10（本项目） | Tomcat 9 |
|---|---|---|
| Java 里的 import | `jakarta.servlet.*` | `javax.servlet.*`（Servlet 和所有 import 都要改） |
| pom 的 servlet-api | `jakarta.servlet-api:6.0.0` | `javax.servlet-api:4.0.1` |
| JSTL 依赖 | `jakarta.servlet.jsp.jstl-api:3.0.0` | `jstl:1.2` |
| JSP 的 taglib uri | `jakarta.tags.core` | `http://java.sun.com/jsp/jstl/core` |
| web.xml 头 | web-app_6_0.xsd | web-app_4_0.xsd |
| PageHelper | 6.1.1 | 5.3.0 |

### 8.2 你的 MySQL 是 5.7

- 连接驱动 `mysql-connector-j:8.4.0` 兼容 5.7，不用换
- url 里保留 `serverTimezone=Asia/Shanghai`，缺了 5.7 会报时区错
- 建库语句把 `utf8mb4` 保留

### 8.3 你用的是 IDEA 社区版（Community）

社区版没有 Tomcat Server 运行配置，两条路：

- **方案 A（推荐）**：装 Smart Tomcat 插件（Settings → Plugins → 搜 Smart Tomcat），配置里指 Tomcat 目录和项目，凑合能用
- **方案 B（保底）**：`mvn package` 打出 war → 复制到 Tomcat 的 `webapps/` → 双击 `bin\startup.bat` 启动 → 浏览器访问。改一次代码要重新打包再复制一次

### 8.4 3306 被占了 / 想换端口

改 MySQL 的 my.ini 里 `port=`，同时把 `mybatis-config.xml` 的 url 改成 `jdbc:mysql://localhost:新端口/design?...`。

---

## 9. 交作业前检查清单

- [ ] MySQL 能启动、库里 class 表有数据
- [ ] IDEA 里 Tomcat 配好，启动无红字
- [ ] `/queryClazzes` 页面出表格，上一页/下一页能翻，首页末页按钮会置灰
- [ ] `?pageNumber=999` 自动修正到最后一页（reasonable）
- [ ] 能口头说清：pageStart 公式、总页数公式、request 和 session 的区别、转发和重定向的区别、PageHelper 底层还是 limit
- [ ] 截图里有自己的数据，别直接用别人的运行结果

---

*有新坑随时补充进来。配置遇到问题按速查表对号，对不上的把报错完整截图发群里。*
