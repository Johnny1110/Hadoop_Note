# 下載與注意事項

<br>

---

<br>

Cloudera 免試版的載點：

<br>

Download Location: https://downloads.cloudera.com/demo_vm/virtualbox/cloudera-quickstart-vm-5.12.0-0-virtualbox.zip
 
Download Location: https://downloads.cloudera.com/demo_vm/vmware/cloudera-quickstart-vm-5.12.0-0-vmware.zip
 
Download Location: https://downloads.cloudera.com/demo_vm/kvm/cloudera-quickstart-vm-5.12.0-0-kvm.zip
 
Download Location: https://downloads.cloudera.com/demo_vm/docker/cloudera-quickstart-vm-5.12.0-0-beta-docker.tar.gz

<br>

以上都是 vmquickstart 版本，OS 是 centos-6.7 目前官方已經停止維護，所以 VM 啟動齁需要手動設定 yum 鏡像站。__另外，cloudera manager 啟動需要 8G 記憶體與雙核心處裡器，設定 VM 的時候要注意一下資源分配。__

<br>

## 修復 yum

<br>

* 移動到 /etc/yum.repos.d/

    ```bash
    cd /etc/yum.repos.d/
    ```

<br>

* 編輯 CentOS-Base.repo

    ```bash
    vi CentOS-Base.repo
    ```

<br>

* 把以下三個區塊內容刪除

    ```bash
    [base]
    [updates]
    [extras]
    ```

* 換上新的鏡像站內容

    ```bash
    [base]
    name=CentOS-$releasever - Base
    # mirrorlist=http://mirrorlist.centos.org/?release=$releasever&arch=$basearch&repo=os&infra=$infra
    # baseurl=http://mirror.centos.org/centos/$releasever/os/$basearch/
    baseurl=https://vault.centos.org/6.10/os/$basearch/
    gpgcheck=1
    gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-6

    # released updates
    [updates]
    name=CentOS-$releasever - Updates
    # mirrorlist=http://mirrorlist.centos.org/?release=$releasever&arch=$basearch&repo=updates&infra=$infra
    # baseurl=http://mirror.centos.org/centos/$releasever/updates/$basearch/
    baseurl=https://vault.centos.org/6.10/updates/$basearch/
    gpgcheck=1
    gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-6

    # additional packages that may be useful
    [extras]
    name=CentOS-$releasever - Extras
    # mirrorlist=http://mirrorlist.centos.org/?release=$releasever&arch=$basearch&repo=extras&infra=$infra
    # baseurl=http://mirror.centos.org/centos/$releasever/extras/$basearch/
    baseurl=https://vault.centos.org/6.10/extras/$basearch/
    gpgcheck=1
    gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-6
    ```

<br>

* 刪除 /etc/yum.repos.d/ 目錄下所有以 cloudera 開頭的 repo 文件：

    ```bash
    rm cloudera*.repo
    ```

<br>

* 清除快許

    ```bash
    yum clean all
    ```

<br>

* 更新 yum

    ```bash
    yum update
    ```