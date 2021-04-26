# distcp 平行複製 (p76)

<br>

---

<br>

`distcp` 可以在 Hadoop 平台上用平行處理的方式複製大量檔案。

`distcp` 是一個用來取代 `hadoop fs -cp` 的方案。

<br>

* 複製一個檔案到他處：

    ```bash
    hadoop distcp file1 file2
    ```

<br>
<br>

* 複製目錄：

    ```bash
    hadoop distcp dir1 dir2
    ```

    如果 dir2 不存在，那麼他會被自動建立出來，dir1 內容被複製到 dir2 中。

    如果 dir2 已存在，dir1 內容會被複製到 dir2 目錄下，產生一個 dir2/dir1 的目錄結構。也可以使用 `-overwrite` 選項來保持相同的目錄結構及強迫檔案被覆蓋
    
    <br>
    <br>

* 使用 `-update` 選項只更新修改過得檔案。

    ```bash
    hadoop distcp -update dir1 dir2
    ```

    假如 dir1 的子目錄下修改一個檔案，用以上指令同步 dir2 進行改變。

    <br>
    <br>

* `distcp` 在兩個 HDFS 叢集間傳輸資料，如果叢集使用相同 Hadoop 版本，就很適合用 hdfs 做路徑設定：

    ```bash
    hadoop distcp hdfs://namenode1/foo hdfs://namenode2/bar
    ```

    <br>
    <br>

* 產生一個 /foo/ 叢集目錄的備份到第二個叢集中：

    ```bash
    hadoop distcp -update -delete -p hdfs://namenode1/foo hdfs://namenode2/foo
    ```

    `-delete` 參數會使 `distcp` 指令刪除目標端的任何檔案或目錄。

<br>
<br>

* 如果兩個叢集執行不同版本 HDFS，則可以使用 `webhdfs` 通訊協定執行 `distcp`：

    ```bash
    hadoop distcp webhdfs://namenode1/foo webhdfs://namenode2/foo
    ```

    <br>
    <br>

    