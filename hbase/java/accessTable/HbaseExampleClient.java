package com.frizo.lib.hadoop;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.*;


public class HbaseExampleClient {

    public static void main(String[] args) throws IOException {
        Connection conn = getConnection();
        TableName tableName = TableName.valueOf("test");

        //TO-DO
    }

    public static Connection getConnection(){
        try {
            Configuration conf = HBaseConfiguration.create();
            conf.set("hbase.zookeeper.quorum", "127.0.0.1");
            conf.set("hbase.zookeeper.property.clientPort", "2181");
            HBaseAdmin.available(conf); // 檢查 conf 是否可用
            return ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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


    public static void put(Connection conn, TableName tableName, String rowKey, String columnFamily, String column, String data) throws IOException {

        try(Table table = conn.getTable(tableName)){
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(data));
            table.put(put);
        }
    }


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


    public static void dropTable(Connection conn, TableName tableName) throws IOException {
        try(Admin admin = conn.getAdmin()){
            if (admin.tableExists(tableName)){
                admin.disableTable(tableName);
                admin.deleteTable(tableName);
                System.out.println("Table: <" + tableName + "> has been droped.");
            }
        }
    }

}
