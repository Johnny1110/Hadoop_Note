# 使用 java 管理存取 table

<br>

---

<br>

使用 Java 的 Hbase API 來寫 Hbase 的基本操作。

<br>

首先看一下將使用到的 jar 依賴：

<br>

```xml
<dependency>
            <groupId>org.apache.hbase</groupId>
            <artifactId>hbase-client</artifactId>
            <version>2.3.5</version>
        </dependency>


        <dependency>
            <groupId>org.apache.hbase</groupId>
            <artifactId>hbase</artifactId>
            <version>2.3.5</version>
            <type>pom</type>
        </dependency>
```

使用哪一版的 hbase 就用那一版的版號。

<br>
<br>
<br>
<br>

## 建立連線

<br>

```java
public static Connection getConnection(){
        try {
            Configuration conf = HBaseConfiguration.create();
            HBaseAdmin.available(conf); // 檢查 conf 是否可用
            return ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
```

<br>
<br>
<br>
<br>

## 建立 Table

<br>

```java
public static void createTable(Connection conn, TableName tableName, String... columnFamilies) throws IOException {
        try (Admin admin = conn.getAdmin()){
            if (admin.tableExists(tableName)){
                System.err.println("error: table <" + tableName + "> " + "already existed.");
            }else {
                TableDescriptorBuilder builder = TableDescriptorBuilder.newBuilder(tableName);
                for (String columnFamily: columnFamilies){
                    builder.setColumnFamily(ColumnFamilyDescriptorBuilder.of(columnFamily));
                }
                admin.createTable(builder.build());
            }
        }
    }
```

<br>

測試：

<br>

```java
public static void main(String[] args) throws IOException {
        Connection conn = getConnection();
        TableName tableName = TableName.valueOf("test");
        createTable(conn, tableName, "name", "state");
    }
```

建立好後就可以去 hbase shell 確認一下了。

<br>
<br>
<br>
<br>

## 建立資料

<br>

```java
public static void put(Connection conn, TableName tableName, String rowKey, String columnFamily, String column, String data) throws IOException {

        try(Table table = conn.getTable(tableName)){
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(data));
            table.put(put);
        }
    }
```

<br>

測試：

<br>

```java
public static void main(String[] args) throws IOException {
        Connection conn = getConnection();
        TableName tableName = TableName.valueOf("test");
        put(conn, tableName, "row1", "name", "c1", "電視");
        put(conn, tableName, "row1", "state", "c2", "open");
        put(conn, tableName, "row2", "name", "c1", "洗衣機");
        put(conn, tableName, "row2", "state", "c2", "close");
    }
```

<br>
<br>
<br>
<br>

## 讀取資料

<br>

```java
public static String getCell(Connection conn, TableName tableName, String rowKey, String columnFamily, String column) throws IOException {
        try(Table table = conn.getTable(tableName)){
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));

            Result result = table.get(get);

            List<Cell> cells = result.listCells();

            if (CollectionUtils.isEmpty(cells)){
                return null;
            }else{
                return new String(CellUtil.cloneValue(cells.get(0)), "UTF-8");
            }
        }
    }
```

<br>

測試：

<br>

```java
public static void main(String[] args) throws IOException {
        Connection conn = getConnection();
        TableName tableName = TableName.valueOf("test");
        String result1 = getCell(conn, tableName, "row1", "name", "c1");
        String result2 = getCell(conn, tableName, "row2", "state", "c2");
        System.out.println(result1);
        System.out.println(result2);
    }
```

<br>
<br>
<br>
<br>

## 讀取一個 row

<br>

```java
public static Map<String, String> getRow(Connection conn, TableName tableName, String rowKey) throws IOException {
        try(Table table = conn.getTable(tableName)){
            Get get = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(get);
            List<Cell> cells = result.listCells();

            if (CollectionUtils.isEmpty(cells)){
                return Collections.emptyMap();
            }else{
                Map<String, String> map = new HashMap<>();
                for (Cell cell: cells){
                    String key = new String(CellUtil.cloneQualifier(cell));
                    String value = new String(CellUtil.cloneValue(cell), "UTF-8");
                    map.put(key, value);
                }
                return map;
            }
         }
    }
```

<br>

測試：

<br>

```java
public static void main(String[] args) throws IOException {
        Connection conn = getConnection();
        TableName tableName = TableName.valueOf("test");
        Map<String, String> resultMap = getRow(conn, tableName, "row1");
        resultMap.forEach((k, v) -> {
            System.out.println("key: " + k + " value: " + v);
        });
    }
```

<br>
<br>
<br>
<br>

## scan 

<br>

```java
public static List<Map<String, String>> scan(Connection conn, TableName tableName, String rowKeyStart, String rowKeyEnd) throws IOException {
        try(Table table = conn.getTable(tableName)){
            Scan scan = new Scan();
            if (!StringUtils.isEmpty(rowKeyStart)){
                scan.withStartRow(Bytes.toBytes(rowKeyStart));
            }
            if (!StringUtils.isEmpty(rowKeyEnd)){
                scan.withStopRow(Bytes.toBytes(rowKeyEnd));
            }
            try(ResultScanner rs = table.getScanner(scan)){
                List<Map<String, String>> dataList = new ArrayList<>();

                for(Result result : rs){
                    Map<String, String> map = new HashMap<>();
                    for (Cell cell : result.listCells()){
                        String key = new String(CellUtil.cloneQualifier(cell));
                        String value = new String(CellUtil.cloneValue(cell));
                        map.put(key, value);
                    }
                    dataList.add(map);
                }
                return dataList;
            }
        }
    }
```

<br>

測試：

<br>

```java
public static void main(String[] args) throws IOException {
        Connection conn = getConnection();
        TableName tableName = TableName.valueOf("test");

        List<Map<String, String>> mapList = scan(conn, tableName, null, null);
        mapList.forEach(map -> {
            map.forEach((k, v) -> {
                System.out.println("------------------------------------------------");
                System.out.println("key: " + k);
                System.out.println("value: " + v);
                System.out.println("------------------------------------------------");
            });
        });
    }
```

<br>
<br>
<br>
<br>

## Drop Table

<br>

```java
public static void dropTable(Connection conn, TableName tableName) throws IOException {
        try(Admin admin = conn.getAdmin()){
            if (admin.tableExists(tableName)){
                admin.disableTable(tableName);
                admin.deleteTable(tableName);
                System.out.println("Table: <" + tableName + "> has been droped.");
            }
        }
    }
```

<br>

測試：

<br>

```java
public static void main(String[] args) throws IOException {
        Connection conn = getConnection();
        TableName tableName = TableName.valueOf("test");

        dropTable(conn, tableName);
    }
```
