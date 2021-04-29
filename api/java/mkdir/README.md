# 建立目錄

<br>

---

<br>

`FileSystem` 提供了一個方法建立目錄

<br>

```java
public boolean mkdir(Path f) throws IOException
```

<br>

如果目錄不存在這個方法就會建立其上目錄，就像 unix 指令 `mkdir -p` 一樣。目錄建立成功就回傳 true。