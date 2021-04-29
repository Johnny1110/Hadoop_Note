# 寫入資料

<br>

---

<br>

`FileSystem` 可以使用 `Path` 物件來建立檔案，利用它回傳的輸出資料流來寫入資料。

<br>

```java
public FSDataOutputStream create(Path f) throws IOException
```

如果寫入檔案路徑的上一層目錄不存在，create 方法會建立完整目錄。

<br>

`FileSystem` 的 `create` 方法還可以傳入一個 `Progressable` 介面，這個介面可以讓我們獲得資料寫入 datanode 的進度。

<br>

```java
public interface Progressable{
        public void progress();
}
```

<br>

下面做一個簡單的示範：

<br>

```java
public class FIleCopyWithProgress {

        public static void main(String[] args) throws IOException {
                String localSrc = "/home/johnny/lab/test.txt";
                String dest = "hdfs://localhost:9000/user/johnny/test.copy.txt";

                Configuration conf = new Configuration();
                FileSystem fs = FileSystem.get(URI.create(dest), conf);

                InputStream in = new BufferedInputStream(new FileInputStream(localSrc));
                OutputStream out = fs.create(new Path(dest), new Progressable() {
                    @Override
                    public void progress() {
                        System.out.println(">");
                    }
                });
                IOUtils.copyBytes(in, out, 4096, true);
        }

}
```

<br>

這裡呼叫 `create()` 方法時就傳入了 `Progressable` 介面。在執行寫入的過程會不斷的在 console 介面輸出 `>`。

<br>
<br>
<br>
<br>

使用 `append()` 方法可以將資料加入到現有檔案中。

<br>

```java
public FSDataOutputStream append(Path f) throws IOException;
```

<br>

以下示範一個 `append()` 用法。

<br>

```java
public class FIleAppend {

        public static void main(String[] args) throws IOException {
            
                String filePath = "hdfs://localhost:9000/user/johnny/test.txt";
                String localFilePath = "/home/johnny/lab/append.txt";

                Configuration conf = new Configuration();
                FileSystem fs = FileSystem.get(URI.create(filePath), conf);

                OutputStream out = fs.append(new Path(filePath));
                InputStream in = new BufferedInputStream(new FileInputStream(localFilePath));

                IOUtils.copyBytes(in, out, 4096, true);

        }
}
```

在這個範例中，local 端的 append.txt 文件內容會被接到 HDFS 中的 test.txt 文件中。