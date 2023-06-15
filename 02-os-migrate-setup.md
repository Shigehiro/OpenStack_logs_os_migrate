# set up os-migrate

prepare an instance for os-migrate
use LXD container for os-migrate.
```
$ lxc image list
+-------+--------------+--------+---------------------------------------------+--------------+-----------+----------+-------------------------------+
| ALIAS | FINGERPRINT  | PUBLIC |                 DESCRIPTION                 | ARCHITECTURE |   TYPE    |   SIZE   |          UPLOAD DATE          |
+-------+--------------+--------+---------------------------------------------+--------------+-----------+----------+-------------------------------+
|       | 8053cb95e852 | no     | ubuntu 20.04 LTS amd64 (release) (20210323) | x86_64       | CONTAINER | 360.87MB | Mar 24, 2021 at 10:34am (UTC) |
+-------+--------------+--------+---------------------------------------------+--------------+-----------+----------+-------------------------------+
|       | f08f65eaf3fc | no     | Centos 8 amd64 (20210324_07:08)             | x86_64       | CONTAINER | 125.58MB | Mar 25, 2021 at 7:00am (UTC)  |
+-------+--------------+--------+---------------------------------------------+--------------+-----------+----------+-------------------------------+

$ lxc profile show os-migrate
config:
  security.nesting: "true"
  security.privileged: "true"
description: Default LXD profile
devices:
  eth0:
    name: eth0
    nictype: bridged
    parent: virbr0
    type: nic
  eth1:
    name: eth1
    nictype: bridged
    parent: iso-br0
    type: nic
  root:
    path: /
    pool: default
    type: disk
name: os-migrate
$

$ lxc launch --profile os-migrate f08f65eaf3fc os-migrate

$ lxc list
+------------+---------+----------------------+------+-----------+-----------+
|    NAME    |  STATE  |         IPV4         | IPV6 |   TYPE    | SNAPSHOTS |
+------------+---------+----------------------+------+-----------+-----------+
| os-migrate | RUNNING | 192.168.123.9 (eth0) |      | CONTAINER | 0         |
|            |         | 172.16.0.11 (eth1)   |      |           |           |
+------------+---------+----------------------+------+-----------+-----------+
$

$ lxc exec os-migrate bash

[root@os-migrate ~]# cat /etc/centos-release
CentOS Linux release 8.3.2011
```

install os-migrate.
```
[root@os-migrate ~]# history |grep 'yum install' | grep -v grep
   34  yum install -y centos-release-openstack-train.noarch
   37  yum install centos-release-ansible-29.noarch
   40  yum install ansible
   41  yum install iputils python3-openstackclient python3-openstacksdk
[root@os-migrate ~]#
```
