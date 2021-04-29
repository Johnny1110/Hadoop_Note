# 用 FIleSystem API 讀取資料

<br>

---

<br>

先直接看 Code，後面會解釋一些重要物件。

<br>

```java
public class FileSystemCat {

    public static void main(String[] args) throws IOException {

        String url = "hdfs://localhost:9000/user/johnny/test.txt";

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(url), conf);
        InputStream in = null;
        try{
            in = fs.open(new Path(url));
            IOUtils.copyBytes(in, System.out, 4096, false);
        }finally {
            IOUtils.closeStream(in);
        }
    }
}
```

<br>

HDFS 中的檔案是由 Path 物件表示而不是用 File 物件，理解上可以把 Path 當作是 HDFS 的URL。

`Configuration` 物件封裝了 client 或 server 端的設定，這些設定參數是根據我們先前在各種 xml 設定檔中的參數而得來的，例如 `/etc/hadoop/core-site.xml`。

有了 `FileSystem` 物件就可以呼叫 `open()` 來打開檔案的輸入流。

`IOUtils` 類別的 `copyBytes` 方法前兩個參數分別是輸入流與輸出流，輸出流使用 `System.out` 會直接把結果輸出到 console 中。第三個參數是複製時使用的緩衝區大小，最後一個參數是控制當複製完成時是否關閉串流。這裡設定為 `false` 所以後面我們自己 `closeStream(in)` 關閉串流。