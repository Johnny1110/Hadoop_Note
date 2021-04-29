# FileSystem 延伸：支援隨機存取

<br>

---

<br>

FileSystem 的 `open()` 方法回傳的是`FSDataInputStream` 這個類別支援隨機存，所以可以讀取資料中的任一位置。

<br>

```java
public class FileSystemDoubleCat {

    public static void main(String[] args) throws IOException {

        String url = "hdfs://localhost:9000/user/johnny/test.txt";

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(url), conf);
        FSDataInputStream in = null;

        try{
            in = fs.open(new Path(url));
            IOUtils.copyBytes(in, System.out, 4096, false);
            in.seek(0); // 索引回到開頭
            IOUtils.copyBytes(in, System.out, 4096, false);
        }finally {
            IOUtils.closeStream(in);
        }
    }
}
```

<br>

`FSDataInputStream` 的 `seek()` 方法可以把資料流指標移動到任意位置。如果移動超過檔案長度時就會丟出 `IOException` 錯誤。

`FSDataInputStream` 還有一個 `getPos()` 方法可以取得目前資料讀取到的位置。

`FSDataInputStream` 也實作了 `PositionedReadable` 介面，可以從一個指定位置讀取檔案中部份資料。

<br>

```java
public interface PositionedReadable{

        public int read(long positon, byte[] buffer, int offset, int length) throws IOException;

        public void readFully(long position, byte[] buffer, int offset, int length) throws IOException;

        public void readFully(long position, byte[] buffer) throws IOException;

}
```