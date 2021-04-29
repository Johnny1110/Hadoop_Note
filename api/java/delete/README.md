# 刪除資料

<br>

---

<br>

使用 `FileSystem` 類別的 `delete()` 方法可以移除檔案或目錄。

<br>

```java
public boolean delete(Path f, boolean recursive) throws IOException
```

<br>

```java
public class DeleteFileDemo {

        public static void main(String[] args) throws IOException {

            String dest = "hdfs://localhost:9000/user/johnny/test.copy.txt";

            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(URI.create(dest), conf);

            fs.delete(new Path(dest), false);

        }

}
```