# 從 Hadoop URL 讀取檔案

<br>

---

<br>

可以使用 `java.net.URL` 物件開啟 Stream 來讀取  HDFS 中的資料。但是要多做一些前置作業讓 java 可以辨識 `hdfs://` 的 url 格式。具體作法如下：

<br>

```java
public class UrlCat {

    static{
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory()); // 設定 hdfs URL 格式
    }

    public static void main(String[] args) throws IOException {

        try(InputStream in = new URL("hdfs://localhost:9000/user/johnny/test.txt").openStream()){
            IOUtils.copyBytes(in, System.out, 4096, false);
        }
    }
}
```

<br>

注意這裡在靜態區塊呼叫 `setURLStreamHandlerFactory()`，因為這個方法在一個 JVM 中只能被呼叫一次，如果在其他地方設定了一個 `URLStreamHandlerFactory` 的話就不能用這個方法讀取 HDFS 資料。