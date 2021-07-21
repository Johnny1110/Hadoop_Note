# 鏡像：ubuntu-ssh

<br>

---

<br>

要想在容器內運行 hadoop，首先要解決 ssh 免密碼登入的問題。

我們建立第一個基底鏡像，他負責調適好 hadoop 的基本所需環境。

<br>

首先，建立一個目錄，就叫他 ubuntu-ssh：

<br>

```bash
cd ~
mkdir ubuntu-ssh
```

<br>

建立一個 Dockerfile 文件：

<br>

```bash
touch Dockerfile
```

<br>

編輯內容如下：

<br>

```Dockerfile
# 1
FROM ubuntu:20.04
MAINTAINER Johnny

# 2
RUN apt-get -yqq update && apt-get install -yqq openssh-server sudo

# 3
RUN addgroup hadoop_group
RUN useradd -g hadoop_group -ms /bin/bash hadoop
RUN sudo adduser hadoop sudo
RUN echo "hadoop:hadoop" | chpasswd

# 4
USER hadoop

RUN mkdir /home/hadoop/.ssh
RUN ssh-keygen -t rsa -P "" -f "/home/hadoop/.ssh/id_rsa"
RUN cat /home/hadoop/.ssh/id_rsa.pub >> /home/hadoop/.ssh/authorized_keys

# 5
USER root
RUN mkdir /run/sshd
EXPOSE 22
CMD ["/usr/sbin/sshd", "-D"]
```

<br>

* `#1` 我們使用 ubuntu-20.04 作為基底鏡像。

* `#2` 安裝 `openssh-server` `sudo` 兩個服務。

* `#3` 建立 hadoop 帳戶與 hadoop_group 群組，之後關於 hadoop 的所有服務都將由 hadoop 帳戶負責。

* `#4` 切換成 hadoop 帳戶設定 ssh 免密碼登入。

* `#5` 最後我們容器要運行的換需要執行 sshd 來組塞當前執行緒，這像工作需要 root 身份執行。當然 hadoop 帳號也可以但是需要設定 sudo 免密碼，我為了省事就不去設定了。

<br>

到目前為止，基底鏡像 ubuntu-ssh 已經完成。