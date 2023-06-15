# export, import network, subnet
---

### export network, subnet
---

```
[root@os-migrate os-migrate-works]# cat os-migrate-vars.yml
# queens
os_migrate_src_auth:
  auth_url: http://192.168.123.14/identity
  password: devstack
  project_domain_name: Default
  project_name: demo
  user_domain_name: Default
  username: admin
  #username: demo
# train
os_migrate_src_region_name: RegionOne
os_migrate_dst_auth:
  auth_url: http://192.168.123.20/identity
  password: devstack
  project_domain_name: Default
  project_name: demo
  user_domain_name: Default
  username: admin
  #username: demo
os_migrate_dst_region_name: RegionOne

os_migrate_data_dir: /root/os-migrate-data

os_migrate_src_conversion_net_mtu: 1400
os_migrate_dst_conversion_net_mtu: 1400

os_migrate_conversion_host_ssh_user: centos
os_migrate_conversion_flavor_name: cent8-512
os_migrate_conversion_image_name: centos8

os_migrate_src_conversion_external_network_name: vol-migrate-600
os_migrate_dst_conversion_external_network_name: vol-migrate-601
[root@os-migrate os-migrate-works]#

[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/export_networks.yml

[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/export_subnets.yml
```

### import network, subnet
---

```
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/import_networks.yml
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/import_subnets.yml
```

```
[root@os-migrate os-migrate-works]# source train-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack network list
+--------------------------------------+------------+--------------------------------------+
| ID                                   | Name       | Subnets                              |
+--------------------------------------+------------+--------------------------------------+
| 1e7e36d4-0b1b-4190-82aa-2c5a731fca0c | public-500 | fee1c9a6-332a-450e-9d15-d50fac941a41 |
| 5b5baf42-7db9-4953-be9c-04bd00660ee1 | demo-net01 | 4ed70ab3-fbd9-44cc-9e21-1a0086790f1b |
+--------------------------------------+------------+--------------------------------------+
[root@os-migrate os-migrate-works]# openstack subnet list
+--------------------------------------+---------------+--------------------------------------+----------------+
| ID                                   | Name          | Network                              | Subnet         |
+--------------------------------------+---------------+--------------------------------------+----------------+
| 4ed70ab3-fbd9-44cc-9e21-1a0086790f1b | demo-subnet01 | 5b5baf42-7db9-4953-be9c-04bd00660ee1 | 192.168.0.0/24 |
| fee1c9a6-332a-450e-9d15-d50fac941a41 | public-500    | 1e7e36d4-0b1b-4190-82aa-2c5a731fca0c | 172.16.10.0/24 |
+--------------------------------------+---------------+--------------------------------------+----------------+
[root@os-migrate os-migrate-works]#
```

```
[root@os-migrate os-migrate-works]# openstack project list
+----------------------------------+------+
| ID                               | Name |
+----------------------------------+------+
| 8b724954d39e462b9c4c085a0084da09 | demo |
+----------------------------------+------+
[root@os-migrate os-migrate-works]#
[root@os-migrate os-migrate-works]# openstack network show demo-net01|grep project
| location                  | cloud='', project.domain_id='default', project.domain_name=, project.id='8b724954d39e462b9c4c085a0084da09', project.name='demo', region_name='RegionOne', zone= |
| project_id                | 8b724954d39e462b9c4c085a0084da09                                                                                                                                |
[root@os-migrate os-migrate-works]# openstack subnet show demo-subnet01|grep project
| location          | cloud='', project.domain_id='default', project.domain_name=, project.id='8b724954d39e462b9c4c085a0084da09', project.name='demo', region_name='RegionOne', zone= |
| project_id        | 8b724954d39e462b9c4c085a0084da09                                                                                                                                |
[root@os-migrate os-migrate-works]#
```