# export, import keypairs
---

### export
---

```
[root@os-migrate os-migrate-works]# openstack keypair delete key-01
[root@os-migrate os-migrate-works]# source queens-demo-demo-openrc.sh

[root@os-migrate os-migrate-works]# openstack keypair create key01 --private-key key01-queens.txt
+-------------+-------------------------------------------------+
| Field       | Value                                           |
+-------------+-------------------------------------------------+
| fingerprint | c5:2f:eb:6d:c0:2c:3c:94:45:4f:07:18:84:9c:80:94 |
| name        | key01                                           |
| user_id     | 60b0caf2cf114e0db0df55f395ea2049                |
+-------------+-------------------------------------------------+
[root@os-migrate os-migrate-works]#

[root@os-migrate os-migrate-works]# openstack keypair list
+-------+-------------------------------------------------+
| Name  | Fingerprint                                     |
+-------+-------------------------------------------------+
| key01 | c5:2f:eb:6d:c0:2c:3c:94:45:4f:07:18:84:9c:80:94 |
+-------+-------------------------------------------------+
[root@os-migrate os-migrate-works]#

[root@os-migrate os-migrate-works]# source train-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack keypair list

[root@os-migrate os-migrate-works]#

[root@os-migrate os-migrate-works]# cat os-migrate-vars.yml
# queens
os_migrate_src_auth:
  auth_url: http://192.168.123.14/identity
  password: devstack
  project_domain_name: Default
  project_name: demo
  #project_name: admin
  user_domain_name: Default
  #username: admin
  username: demo
# train
os_migrate_src_region_name: RegionOne
os_migrate_dst_auth:
  auth_url: http://192.168.123.20/identity
  password: devstack
  project_domain_name: Default
  project_name: demo
  #project_name: admin
  user_domain_name: Default
  #username: admin
  username: demo
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

[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/export_keypairs.yml
```

### import
---

```
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/import_keypairs.yml

[root@os-migrate os-migrate-works]# source train-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack keypair list
+-------+-------------------------------------------------+
| Name  | Fingerprint                                     |
+-------+-------------------------------------------------+
| key01 | c5:2f:eb:6d:c0:2c:3c:94:45:4f:07:18:84:9c:80:94 |
+-------+-------------------------------------------------+
[root@os-migrate os-migrate-works]#
```

```
[root@os-migrate os-migrate-works]# source queens-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack keypair show key01
+-------------+-------------------------------------------------+
| Field       | Value                                           |
+-------------+-------------------------------------------------+
| created_at  | 2021-03-29T03:48:16.000000                      |
| deleted     | False                                           |
| deleted_at  | None                                            |
| fingerprint | c5:2f:eb:6d:c0:2c:3c:94:45:4f:07:18:84:9c:80:94 |
| id          | 10                                              |
| name        | key01                                           |
| updated_at  | None                                            |
| user_id     | 60b0caf2cf114e0db0df55f395ea2049                |
+-------------+-------------------------------------------------+
[root@os-migrate os-migrate-works]#
[root@os-migrate os-migrate-works]# source train-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack keypair show key01
+-------------+-------------------------------------------------+
| Field       | Value                                           |
+-------------+-------------------------------------------------+
| created_at  | 2021-03-29T03:50:42.000000                      |
| deleted     | False                                           |
| deleted_at  | None                                            |
| fingerprint | c5:2f:eb:6d:c0:2c:3c:94:45:4f:07:18:84:9c:80:94 |
| id          | 3                                               |
| name        | key01                                           |
| updated_at  | None                                            |
| user_id     | 56a47133be614cc587ab5c2f75ead7be                |
+-------------+-------------------------------------------------+
[root@os-migrate os-migrate-works]#
```

