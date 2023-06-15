# os migrate tool test ( migrate openstack objects from queens to train )

### test environment
---

- use devstack for migration testings
- set up queens all-in-one node and train all-in-one node by using devstack
- set up two devstack nodes as a virtual machine udner KVM host (ubuntu 20.04)
- prepare three nodes. (two for devstack nodes and the remaing one for os-migrate.)

```
         os-migrate 
             |          management segment
   -------- L2 SW ---------
   |                      |
q-devstack           t-devstack
   |br-ex                 |br-ex
   --------- vyos ---------
        data segment ( instances running on queens can access to instances on train through data segment )

```

q-devstack : queens ( ubuntu 16.04 )

t-devstack : train ( ubuntu 18.04 )

### devstack set up
---

launch two instances for queens/train.
```
$ virsh net-define ./isolated01.xml
$ virsh net-start isolated-01
$ virsh net-autostart isolated-01

# devstack for queens
$ uvt-kvm create --cpu 4 --memory 16384 --disk 40 q-devstack release=xenial --password ubuntu --template ./q-devstack-template.xml

# devstack for train
$ uvt-kvm create --cpu 4 --memory 16384 --disk 40 t-devstack release=bionic --password ubuntu --template ./t-devstack-template.xml
```

Here is local.conf of queens/train devstack.
Use the same local.conf.
```
$ cat devstack-local.conf
[[local|localrc]]

HOST_IP=127.0.0.1

ADMIN_PASSWORD=devstack
MYSQL_PASSWORD=devstack
RABBIT_PASSWORD=devstack
SERVICE_PASSWORD=$ADMIN_PASSWORD
SERVICE_TOKEN=devstack

PUBLIC_INTERFACE=enp2s0

#IPV4_ADDRS_SAFE_TO_USE=172.31.1.0/24
#FLOATING_RANGE=192.168.20.0/25
#HOST_IP=192.168.123.20

#Q_USE_SECGROUP=False
#LIBVIRT_FIREWALL_DRIVER=nova.virt.firewall.NoopFirewallDriver

enable_service c-bak
enable_service swift

disable_service tempest

[[post-config|/etc/neutron/dhcp_agent.ini]]
[DEFAULT]
enable_isolated_metadata = True
```

set up devstack.
see https://docs.openstack.org/devstack/latest/

```
# queens
$ git clone https://opendev.org/openstack/devstack -b stable/queens
$ cd devstack/
$ vi local.conf
$ ./stack.sh

# train
$ git clone https://opendev.org/openstack/devstack -b stable/train
$ cd devstack/
$ vi local.conf
$ ./stack.sh
```

after completing devstack setup.

##### queens
```
stack@q-devstack:~/devstack$ source openrc admin
WARNING: setting legacy OS_TENANT_NAME to support cli tools.
stack@q-devstack:~/devstack$ openstack endpoint list
+----------------------------------+-----------+--------------+----------------+---------+-----------+----------------------------------------------+
| ID                               | Region    | Service Name | Service Type   | Enabled | Interface | URL                                          |
+----------------------------------+-----------+--------------+----------------+---------+-----------+----------------------------------------------+
| 08e2d0c797694645912a2c12c0f46fd4 | RegionOne | cinderv2     | volumev2       | True    | public    | http://127.0.0.1/volume/v2/$(project_id)s    |
| 12da8e702874427c8b1b12633fb6d4b3 | RegionOne | nova_legacy  | compute_legacy | True    | public    | http://127.0.0.1/compute/v2/$(project_id)s   |
| 2500f4b82257482a839fb485e8c06473 | RegionOne | cinderv3     | volumev3       | True    | public    | http://127.0.0.1/volume/v3/$(project_id)s    |
| 3009a74fdb11406fa03e6a9b87600457 | RegionOne | swift        | object-store   | True    | public    | http://127.0.0.1:8080/v1/AUTH_$(project_id)s |
| 37caa4e91c214b51b86282d3748dd29a | RegionOne | placement    | placement      | True    | public    | http://127.0.0.1/placement                   |
| 6160e0c34f744ff98bf3e7994071d3e9 | RegionOne | nova         | compute        | True    | public    | http://127.0.0.1/compute/v2.1                |
| 7bffe1146d6444ae92bc9e2f7fa9909c | RegionOne | keystone     | identity       | True    | public    | http://127.0.0.1/identity                    |
| 8bf368faf76b478b9000cc356df538e6 | RegionOne | cinder       | block-storage  | True    | public    | http://127.0.0.1/volume/v3/$(project_id)s    |
| 8f861fd844514846a843e89e7ce1e97a | RegionOne | glance       | image          | True    | public    | http://127.0.0.1/image                       |
| 9c5648d10e0f476b9914a860670aabcb | RegionOne | swift        | object-store   | True    | admin     | http://127.0.0.1:8080                        |
| aa895d6c766f4d178ef299830b38d0d7 | RegionOne | cinder       | volume         | True    | public    | http://127.0.0.1/volume/v1/$(project_id)s    |
| c93a22d4d3ef44058d15c34d19032aa6 | RegionOne | neutron      | network        | True    | public    | http://127.0.0.1:9696/                       |
| cbd6ae969ed648839c457f1d005fd469 | RegionOne | keystone     | identity       | True    | admin     | http://127.0.0.1/identity                    |
+----------------------------------+-----------+--------------+----------------+---------+-----------+----------------------------------------------+
stack@q-devstack:~/devstack$ openstack network list
+--------------------------------------+---------+----------------------------------------------------------------------------+
| ID                                   | Name    | Subnets                                                                    |
+--------------------------------------+---------+----------------------------------------------------------------------------+
| 492aa51e-b6ca-4e20-9f75-546b56e30c73 | public  | 09a56daf-2cf7-4ced-bbff-2e76c9708b5b, 45cccedf-9fe5-4ac8-80a6-688dc0608c20 |
| a5072d26-1d9a-4afd-8016-5a83f84b9642 | private | 771c07da-9c3c-405c-95ed-ef4febc92a36, 83902d1c-37a3-43ff-9860-1a34c4b19c4b |
+--------------------------------------+---------+----------------------------------------------------------------------------+
stack@q-devstack:~/devstack$
```

##### train
```
stack@t-devstack:~/devstack$ source openrc admin
opeWARNING: setting legacy OS_TENANT_NAME to support cli tools.
stack@t-devstack:~/devstack$ openstack endpoint list
+----------------------------------+-----------+--------------+----------------+---------+-----------+----------------------------------------------+
| ID                               | Region    | Service Name | Service Type   | Enabled | Interface | URL                                          |
+----------------------------------+-----------+--------------+----------------+---------+-----------+----------------------------------------------+
| 09f659bf3ed949478ebc4c70b099b7f8 | RegionOne | keystone     | identity       | True    | public    | http://127.0.0.1/identity                    |
| 19e0f22033e44fbcbc03491c5b26e1cc | RegionOne | keystone     | identity       | True    | admin     | http://127.0.0.1/identity                    |
| 1ad51601e09c490b98ce2bc94d4820f0 | RegionOne | swift        | object-store   | True    | public    | http://127.0.0.1:8080/v1/AUTH_$(project_id)s |
| 3553a8a801be4f5cbc83428bb80604be | RegionOne | nova         | compute        | True    | public    | http://127.0.0.1/compute/v2.1                |
| 3bf0eb1492e142f3a5b875899819d59b | RegionOne | swift        | object-store   | True    | admin     | http://127.0.0.1:8080                        |
| 411107599cfc43cba570ecb277367a86 | RegionOne | nova_legacy  | compute_legacy | True    | public    | http://127.0.0.1/compute/v2/$(project_id)s   |
| 4b88ed79fe0741fa975baa3c79fea273 | RegionOne | neutron      | network        | True    | public    | http://127.0.0.1:9696/                       |
| 8eeebe5bedb049208c1797a8f58373c1 | RegionOne | cinderv3     | volumev3       | True    | public    | http://127.0.0.1/volume/v3/$(project_id)s    |
| b1cba28023844b8a968ac5f4aaca3a0d | RegionOne | cinderv2     | volumev2       | True    | public    | http://127.0.0.1/volume/v2/$(project_id)s    |
| b89daf4c2da94d0a9595d2a45c476b11 | RegionOne | cinder       | block-storage  | True    | public    | http://127.0.0.1/volume/v3/$(project_id)s    |
| bced0018e7cb4f818a5fe85243f01e78 | RegionOne | placement    | placement      | True    | public    | http://127.0.0.1/placement                   |
| e1a4c913463a45a7b30577e625ecaef7 | RegionOne | glance       | image          | True    | public    | http://127.0.0.1/image                       |
+----------------------------------+-----------+--------------+----------------+---------+-----------+----------------------------------------------+
stack@t-devstack:~/devstack$

stack@t-devstack:~/devstack$ source openrc demo
WARNING: setting legacy OS_TENANT_NAME to support cli tools.
stack@t-devstack:~/devstack$ openstack network list
+--------------------------------------+---------+----------------------------------------------------------------------------+
| ID                                   | Name    | Subnets                                                                    |
+--------------------------------------+---------+----------------------------------------------------------------------------+
| 13ad29f6-ebeb-43f7-9a8c-60dc142586f2 | private | 959e454c-e2e5-4b20-a3e9-982014cef469, fbbeea50-49d3-4f56-aecf-58207282aaaa |
| a66562f6-fc2e-4963-bcd0-6fc8342eaf0d | public  | 53d535ab-ee23-4ad8-bd31-44a1c2669d57, a322d3f8-3816-454c-8ad7-996c568192c0 |
+--------------------------------------+---------+----------------------------------------------------------------------------+
stack@t-devstack:~/devstack$
```

remove existing network, subnet and router.
```
stack@t-devstack:~$ for i in `openstack subnet list -c Name -f value`;do openstack router remove subnet router1 $i; done
stack@t-devstack:~$ openstack router delete router1
stack@t-devstack:~$ openstack network delete public
stack@t-devstack:~$ openstack network delete private
```

Move onto 02-os-migrate-setup.md

