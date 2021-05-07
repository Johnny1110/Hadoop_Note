# hbase 資料結構介紹

<br>

---

<br>

HBase 的 Table 是由欄（column）與列 （row）組成的。

資料表的行鍵（row key）是位元陣列，因此理論上任何東西都可以當作 row key，而且 table 中的資料也是通過 row key