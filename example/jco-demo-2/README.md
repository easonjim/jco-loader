# JCO依赖安装
# 传统方式
## 配置依赖库
```shell
# mac，注意初始化用户为www-data，要指定当前用户名，通过这个参数--jco-dir-user
curl -fsSL https://raw.githubusercontent.com/easonjim/jco-sdk/master/set-mac.sh | bash -s -- --jco-dir-user="jim" 2>&1 | tee jco-sdk-set-mac.log
```
## 安装JAR包到本地仓库
```shell
mvn install:install-file -DgroupId=org.hibersap -DartifactId=com.sap.conn.jco.sapjco3 -Dversion=3.0.11 -Dpackaging=jar -Dfile=/data/service/jco-sdk/3.0.11-720.612/sapjco3.jar
```
## 使用依赖
```xml
<dependency>
    <groupId>org.hibersap</groupId>
    <artifactId>com.sap.conn.jco.sapjco3</artifactId>
    <version>3.0.11</version>
</dependency>
```
## 这种方式可以打包，但需要指定一下插件进行打包，并在目标机器上通过shell命令配置环境变量
POM配置插件如下：
```xml
<build>
	<plugins>
        <!-- main方法配置 -->
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-jar-plugin</artifactId>
			<version>2.6</version>
			<configuration>
				<archive>
					<manifest>
						<addClasspath>true</addClasspath>
						<classpathPrefix>lib/</classpathPrefix>
						<mainClass>com.github.easonjim.demo.ConnTest</mainClass>
					</manifest>
				</archive>
			</configuration>
		</plugin>
		<!-- class path指定，并复制lib文件夹，这个插件只能生成非system类型的包，如果scope使用了system之后会造成失效 -->
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-dependency-plugin</artifactId>
			<version>2.10</version>
			<executions>
				<execution>
					<id>copy-dependencies</id>
					<phase>package</phase>
					<goals>
						<goal>copy-dependencies</goal>
					</goals>
					<configuration>
						<outputDirectory>${project.build.directory}/lib</outputDirectory>
					</configuration>
				</execution>
			</executions>
		</plugin>
 
	</plugins>
</build>
```
* 部署时，拷贝jar包，以及lib文件夹，配置环境变量指定动态链接库路径
* 这种方式适合Spring Boot，当Spring Boot项目时，可以去掉插件，直接引入jar包即可，但前提是别忘记设置环境变量！


# 快速方式（推荐）
这种方式免除配置环境变量，并且不用安装本地依赖，打包运行都非常方便。  
原理：sapjco3.jar在没有配置环境变量时，会自动找当前目录下的动态链接库；以这条线索，只要把动态链接库拷贝到jar包的当前目录即可识别。
## 配置依赖
先把依赖clone下来本地（git clone [https://github.com/easonjim/jco-sdk](https://github.com/easonjim/jco-sdk)），然后手动复制要用的lib即可。
```shell
# 拷贝sapjco3.jar、sapjco3.dll、sapjco3.pdb、libsapjco3.jnilib、libsapjco3.so到lib文件夹
```
## 配置Maven依赖
```xml
<dependency>
    <groupId>org.hibersap</groupId>
    <artifactId>com.sap.conn.jco.sapjco3</artifactId>
    <version>3.0.11</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/lib/sapjco3.jar</systemPath>
</dependency>
```
## 打包插件配置
```xml
<!-- 解决class path和main class -->
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-jar-plugin</artifactId>
	<version>2.6</version>
	<configuration>
	    <archive>
		<addMavenDescriptor>false</addMavenDescriptor>
		<manifestEntries>
		    <Class-Path>lib/sapjco3.jar</Class-Path>
		</manifestEntries>
		<manifest>
		    <addClasspath>true</addClasspath>
		    <classpathPrefix>lib/</classpathPrefix>
		    <mainClass>com.github.easonjim.demo.ConnTest</mainClass>
		</manifest>
	    </archive>
	</configuration>
</plugin>
<!-- 复制lib文件 -->
<plugin>
	<artifactId>maven-resources-plugin</artifactId>
	<version>3.0.2</version>
	<executions>
	    <execution>
		<id>copy-resources</id>
		<phase>validate</phase>
		<goals>
		    <goal>copy-resources</goal>
		</goals>
		<configuration>
		    <outputDirectory>${project.build.directory}/lib</outputDirectory>
		    <resources>
			<resource>
			    <directory>lib</directory>
			    <filtering>false</filtering>
			</resource>
		    </resources>
		</configuration>
	    </execution>
	</executions>
</plugin>
```

## 这种配置方式可以打包成直接运行的jar包，会同时附带lib文件夹
* 这种方式的关键配置点在POM，增加了两个插件maven-jar-plugin用于输出MF文件的Class Path，maven-resources-plugin用于复制出lib文件夹
* 使用时，直接拷贝jar包以及lib文件夹到指定计算机运行即可
* 但这种方式适合Main方法项目，但不适合Spring Boot项目，总之，做测试绝对够用了。


# 通用方式（非常推荐）（适合Application/Web/Spring Boot）
* 通过Maven中央仓库插件，引入jco-loader替代本地lib文件夹引入jco-jar的方式，然后使用第1种的插件方式，自动打包出lib文件夹
* Spring Boot项目无需配置plugin，本身已经集成打包插件，引入pom后无需做任何配置。  
## POM依赖：
```xml
<dependency>
    <groupId>com.github.easonjim</groupId>
    <artifactId>com.sap.conn.jco.sapjco3</artifactId>
    <version>3.0.11</version>
</dependency>
<dependency><!-- jco-loader -->
    <groupId>com.github.easonjim</groupId>
    <artifactId>jco-loader</artifactId>
    <version>3.0.11</version>
</dependency>
```
