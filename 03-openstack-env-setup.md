# set up OpenStack test env for os-migrate, export, import flavors

### confirm you can access to both OpenStack clouds from os-migrate node
---

```
[root@os-migrate os-migrate-works]# source queens-demo-openrc.sh
Please enter your OpenStack Password for project demo as user demo:
[root@os-migrate os-migrate-works]# openstack catalog list | head -5
+-------------+----------------+-------------------------------------------------------------------------------+
| Name        | Type           | Endpoints                                                                     |
+-------------+----------------+-------------------------------------------------------------------------------+
| nova        | compute        | RegionOne                                                                     |
|             |                |   public: http://192.168.123.14/compute/v2.1                                  |
[root@os-migrate os-migrate-works]#
```

```
[root@os-migrate os-migrate-works]# source train-demo-openrc.sh
Please enter your OpenStack Password for project demo as user demo:
[root@os-migrate os-migrate-works]# openstack catalog list | head -5
+-------------+----------------+-------------------------------------------------------------------------------+
| Name        | Type           | Endpoints                                                                     |
+-------------+----------------+-------------------------------------------------------------------------------+
| neutron     | network        | RegionOne                                                                     |
|             |                |   public: http://192.168.123.20:9696/                                         |
[root@os-migrate os-migrate-works]#
```

### remove network, subnet, router created by devstack
---

When installing queens/train by devstack, devstack will create some objects, such networks, subnets, router etc.
Remove those objects.

### create test env on queens
---

create network, subnet, router, keypair etc on src cloud(queens)
see devstack-setup-conf/queens-demo-env-setup.sh.

after creating objects.

queens
```
[root@os-migrate os-migrate-works]# source queens-demo-openrc.sh

[root@os-migrate os-migrate-works]# for i in network subnet router flavor keypair image ; do openstack $i list ;done
+--------------------------------------+------------+--------------------------------------+
| ID                                   | Name       | Subnets                              |
+--------------------------------------+------------+--------------------------------------+
| 7f2cb496-a3f9-49ec-9743-8dd4c09f568d | public-500 | 86d3d4ae-bc57-4aea-a6d6-162af09cd384 |
| af498457-6b4d-4060-ac3a-ef684e053367 | demo-net01 | 26ecb024-3346-4145-9439-2e157c4fc55e |
+--------------------------------------+------------+--------------------------------------+
+--------------------------------------+---------------+--------------------------------------+----------------+
| ID                                   | Name          | Network                              | Subnet         |
+--------------------------------------+---------------+--------------------------------------+----------------+
| 26ecb024-3346-4145-9439-2e157c4fc55e | demo-subnet01 | af498457-6b4d-4060-ac3a-ef684e053367 | 192.168.0.0/24 |
| 86d3d4ae-bc57-4aea-a6d6-162af09cd384 | public-500    | 7f2cb496-a3f9-49ec-9743-8dd4c09f568d | 172.16.10.0/24 |
+--------------------------------------+---------------+--------------------------------------+----------------+
+--------------------------------------+--------------+--------+-------+----------------------------------+
| ID                                   | Name         | Status | State | Project                          |
+--------------------------------------+--------------+--------+-------+----------------------------------+
| 25e43ea6-7aa1-4bfd-8c1d-914c0774f2c9 | demo-router1 | ACTIVE | UP    | 4fcea3f39f8843dd817a78261588437d |
+--------------------------------------+--------------+--------+-------+----------------------------------+
+--------------------------------------+-----------+-------+------+-----------+-------+-----------+
| ID                                   | Name      |   RAM | Disk | Ephemeral | VCPUs | Is Public |
+--------------------------------------+-----------+-------+------+-----------+-------+-----------+
| 1                                    | m1.tiny   |   512 |    1 |         0 |     1 | True      |
| 2                                    | m1.small  |  2048 |   20 |         0 |     1 | True      |
| 3                                    | m1.medium |  4096 |   40 |         0 |     2 | True      |
| 4                                    | m1.large  |  8192 |   80 |         0 |     4 | True      |
| 5                                    | m1.xlarge | 16384 |  160 |         0 |     8 | True      |
| 6aba3875-bf1d-4bdc-9401-4889c1d0603b | cent8-512 |   512 |    0 |         0 |     1 | True      |
| c1                                   | cirros256 |   256 |    0 |         0 |     1 | True      |
| d1                                   | ds512M    |   512 |    5 |         0 |     1 | True      |
| d2                                   | ds1G      |  1024 |   10 |         0 |     1 | True      |
| d3                                   | ds2G      |  2048 |   10 |         0 |     2 | True      |
| d4                                   | ds4G      |  4096 |   20 |         0 |     4 | True      |
| e8e6848d-a58e-4a89-aeb9-224664f37957 | m1.nano   |   256 |    0 |         0 |     1 | True      |
+--------------------------------------+-----------+-------+------+-----------+-------+-----------+
+------------+-------------------------------------------------+
| Name       | Fingerprint                                     |
+------------+-------------------------------------------------+
| demo-key01 | 0e:cd:a8:47:c8:3c:23:2b:2e:9b:fa:10:07:38:a2:3e |
+------------+-------------------------------------------------+
+--------------------------------------+--------------------------+--------+
| ID                                   | Name                     | Status |
+--------------------------------------+--------------------------+--------+
| 2929ced2-af86-40d0-a8ea-fe1ea071fb4c | cirros-0.3.5-x86_64-disk | active |
+--------------------------------------+--------------------------+--------+
[root@os-migrate os-migrate-works]#
```

train
```
[root@os-migrate os-migrate-works]# source train-admin-admin-openrc.sh
[root@os-migrate os-migrate-works]# openstack project list
+----------------------------------+---------+
| ID                               | Name    |
+----------------------------------+---------+
| 980c5469d4a54ed79f39f75d0db4329e | service |
| a1db767b127b40c1afa3cebd75cbf993 | admin   |
+----------------------------------+---------+
[root@os-migrate os-migrate-works]#

[root@os-migrate os-migrate-works]# openstack user list
+----------------------------------+--------------+
| ID                               | Name         |
+----------------------------------+--------------+
| 91e9c4d843c74c8cbe8415f03c1b17bb | admin        |
| 15f52799c385443691eb47dc58f2ac9e | nova         |
| 0ccf90a0f0ff4c34af6e09c0bb7a056d | glance       |
| 76bc9dd628eb47379cc8f47de2c95473 | glance-swift |
| a2dd3bc011364cec8c8e7127ea8a264b | cinder       |
| a78771d46565488a9dcb04d6ebc83378 | neutron      |
| 014d8d65d7274f80a746ab05a6109dd5 | swift        |
| 1f811dc9d851419dadbd857da7e40c97 | placement    |
+----------------------------------+--------------+

[root@os-migrate os-migrate-works]# openstack security group list
o+--------------------------------------+---------+------------------------+----------------------------------+------+
| ID                                   | Name    | Description            | Project                          | Tags |
+--------------------------------------+---------+------------------------+----------------------------------+------+
| 0cc3d4b3-32d8-4a30-9385-91850431903d | default | Default security group | a1db767b127b40c1afa3cebd75cbf993 | []   |
+--------------------------------------+---------+------------------------+----------------------------------+------+
[root@os-migrate os-migrate-works]# openstack network list

[root@os-migrate os-migrate-works]# openstack subnet list

[root@os-migrate os-migrate-works]#
[root@os-migrate os-migrate-works]# openstack router list

[root@os-migrate os-migrate-works]# openstack security group list
+--------------------------------------+---------+------------------------+----------------------------------+------+
| ID                                   | Name    | Description            | Project                          | Tags |
+--------------------------------------+---------+------------------------+----------------------------------+------+
| 0cc3d4b3-32d8-4a30-9385-91850431903d | default | Default security group | a1db767b127b40c1afa3cebd75cbf993 | []   |
+--------------------------------------+---------+------------------------+----------------------------------+------+
[root@os-migrate os-migrate-works]#
[root@os-migrate os-migrate-works]# openstack flavor list

[root@os-migrate os-migrate-works]#

[root@os-migrate os-migrate-works]# openstack keypair list

[root@os-migrate os-migrate-works]#
```
