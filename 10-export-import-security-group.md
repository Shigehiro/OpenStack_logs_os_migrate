# export, import security group
---

### export
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

os_migrate_src_conversion_external_network_name: public-500
os_migrate_dst_conversion_external_network_name: public-501
[root@os-migrate os-migrate-works]#

[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/export_security_groups.yml

[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/export_security_group_rules.yml
```

### import
---

```
[root@os-migrate os-migrate-works]# openstack security group list
+--------------------------------------+---------+------------------------+----------------------------------+------+
| ID                                   | Name    | Description            | Project                          | Tags |
+--------------------------------------+---------+------------------------+----------------------------------+------+
| 5e98bda1-ac69-4432-96a9-542ea59f7d0c | default | Default security group | 8b724954d39e462b9c4c085a0084da09 | []   |
+--------------------------------------+---------+------------------------+----------------------------------+------+
[root@os-migrate os-migrate-works]#

[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/import_security_groups.yml

[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/import_security_group_rules.yml
```

```
[root@os-migrate os-migrate-works]# source queens-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack security group list
+--------------------------------------+------------+------------------------+----------------------------------+------+
| ID                                   | Name       | Description            | Project                          | Tags |
+--------------------------------------+------------+------------------------+----------------------------------+------+
| 292f39b4-3094-4681-afeb-a626fd689a75 | demo-sec01 | demo-sec01             | 4fcea3f39f8843dd817a78261588437d | []   |
| 363bb60f-ad24-4c2f-be83-491e575db37d | default    | Default security group | 4fcea3f39f8843dd817a78261588437d | []   |
+--------------------------------------+------------+------------------------+----------------------------------+------+
[root@os-migrate os-migrate-works]# openstack security group rule list
+--------------------------------------+-------------+-----------+-----------+------------+--------------------------------------+--------------------------------------+
| ID                                   | IP Protocol | Ethertype | IP Range  | Port Range | Remote Security Group                | Security Group                       |
+--------------------------------------+-------------+-----------+-----------+------------+--------------------------------------+--------------------------------------+
| 032092e7-a5ff-4c41-9a5d-edd0e9e39f03 | None        | IPv4      | 0.0.0.0/0 |            | None                                 | 363bb60f-ad24-4c2f-be83-491e575db37d |
| 1bc426ee-9b91-4945-b923-83e912065977 | None        | IPv6      | ::/0      |            | 363bb60f-ad24-4c2f-be83-491e575db37d | 363bb60f-ad24-4c2f-be83-491e575db37d |
| 27d4ff69-8dd6-4cfd-a0c2-3b3f683e2376 | icmp        | IPv4      | 0.0.0.0/0 |            | None                                 | 292f39b4-3094-4681-afeb-a626fd689a75 |
| 621f37a6-df07-44f7-8b63-5b5339f74e22 | None        | IPv6      | ::/0      |            | None                                 | 292f39b4-3094-4681-afeb-a626fd689a75 |
| 6a82416d-2d1f-437d-86e2-da76142fc8d0 | tcp         | IPv4      | 0.0.0.0/0 | 22:1023    | None                                 | 292f39b4-3094-4681-afeb-a626fd689a75 |
| 7e6f8ccb-5244-4005-9b2e-1206704726b4 | None        | IPv6      | ::/0      |            | None                                 | 363bb60f-ad24-4c2f-be83-491e575db37d |
| 854c39a2-4885-45a5-8e83-77acb6b8bd80 | None        | IPv4      | 0.0.0.0/0 |            | 363bb60f-ad24-4c2f-be83-491e575db37d | 363bb60f-ad24-4c2f-be83-491e575db37d |
| b209ee5b-044f-4be6-8721-33932cd4bcb4 | None        | IPv4      | 0.0.0.0/0 |            | None                                 | 292f39b4-3094-4681-afeb-a626fd689a75 |
+--------------------------------------+-------------+-----------+-----------+------------+--------------------------------------+--------------------------------------+
[root@os-migrate os-migrate-works]#
[root@os-migrate os-migrate-works]# source train-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack security group list
+--------------------------------------+------------+------------------------+----------------------------------+------+
| ID                                   | Name       | Description            | Project                          | Tags |
+--------------------------------------+------------+------------------------+----------------------------------+------+
| 5e98bda1-ac69-4432-96a9-542ea59f7d0c | default    | Default security group | 8b724954d39e462b9c4c085a0084da09 | []   |
| acd855b6-9d90-4b8a-8be7-9e7c84b44d73 | demo-sec01 | demo-sec01             | 8b724954d39e462b9c4c085a0084da09 | []   |
+--------------------------------------+------------+------------------------+----------------------------------+------+
[root@os-migrate os-migrate-works]# openstack security group rule list
+--------------------------------------+-------------+-----------+-----------+------------+--------------------------------------+--------------------------------------+
| ID                                   | IP Protocol | Ethertype | IP Range  | Port Range | Remote Security Group                | Security Group                       |
+--------------------------------------+-------------+-----------+-----------+------------+--------------------------------------+--------------------------------------+
| 0ec7ed3e-10d8-46a5-8305-eef8b1ec85f9 | None        | IPv6      | ::/0      |            | None                                 | 5e98bda1-ac69-4432-96a9-542ea59f7d0c |
| 3177b4af-bf53-411c-b663-9984e44443c3 | None        | IPv4      | 0.0.0.0/0 |            | None                                 | 5e98bda1-ac69-4432-96a9-542ea59f7d0c |
| 87a50bf0-f311-4ebb-b213-e24e67959112 | icmp        | IPv4      | 0.0.0.0/0 |            | None                                 | acd855b6-9d90-4b8a-8be7-9e7c84b44d73 |
| 8b4abe82-5219-43d5-87a5-ef64a0a9b982 | None        | IPv6      | ::/0      |            | 5e98bda1-ac69-4432-96a9-542ea59f7d0c | 5e98bda1-ac69-4432-96a9-542ea59f7d0c |
| 9c21647e-cd48-4c7a-ba14-cfe3d5bc7051 | tcp         | IPv4      | 0.0.0.0/0 | 22:1023    | None                                 | acd855b6-9d90-4b8a-8be7-9e7c84b44d73 |
| ba6ed09b-dc44-46f4-bb32-5ceef5b9d7c8 | None        | IPv4      | 0.0.0.0/0 |            | None                                 | acd855b6-9d90-4b8a-8be7-9e7c84b44d73 |
| ccd30613-963b-459d-ba15-143d9721dfc9 | None        | IPv6      | ::/0      |            | None                                 | acd855b6-9d90-4b8a-8be7-9e7c84b44d73 |
| e3dc053f-efe9-45ba-b498-641e4667a0ec | None        | IPv4      | 0.0.0.0/0 |            | 5e98bda1-ac69-4432-96a9-542ea59f7d0c | 5e98bda1-ac69-4432-96a9-542ea59f7d0c |
+--------------------------------------+-------------+-----------+-----------+------------+--------------------------------------+--------------------------------------+
[root@os-migrate os-migrate-works]#
```