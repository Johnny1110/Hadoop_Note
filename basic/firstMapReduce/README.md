# 第二章 MapReduce 遇到的問題 (p22)

<br>

---

<br>

## 資料

<br>

這一個章節提到要使用的氣象站資料在這邊[下載](./data/1901)。

<br>

## Code

<br>

書中使用 Hadoop 的 lib 但也沒有特別說明來源於哪個 Jar 檔，所以我就去 maven 倉庫找了一些 Hadoop 的 lib 來試試，順便一提，我是使用 maven 來寫的這個 demo。pom 依賴如下：

<br>

```xml
<dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>3.3.0</version>
        </dependency>

        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-client</artifactId>
            <version>3.3.0</version>
        </dependency>


        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-mapreduce-client-core</artifactId>
            <version>3.3.0</version>
        </dependency>

        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-hdfs</artifactId>
            <version>3.3.0</version>
        </dependency>
```

<br>

我安裝使用的是 Hadoop 3.3.0 版本，所以這邊也都是用 3.3.0 的套件依賴。

<br>

由於最後我們還要將專案打包成 Jar 檔來讓 Hadoop 執行，所以要設定一下編譯設定：

<br>

```xml
<plugin>
    <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
            <archive>
                <manifest>
                    <mainClass>com.frizo.lib.hadoop.MaxTemperature</mainClass>
                </manifest>
            </archive>
            <descriptorRefs>
                 <descriptorRef>jar-with-dependencies</descriptorRef>
            </descriptorRefs>
        </configuration>
</plugin>
```

<br>

有了上面的設定，我們就可以使用指令 `mvn clean compile assembly:single` 來編譯專案了。這邊我有事先編譯好一個能用的 [Jar 範例](./code/hadoop/target/hadoop-example.jar)可以供直接取用