# export, import router
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

os_migrate_src_conversion_external_network_name: vol-migrate-600
os_migrate_dst_conversion_external_network_name: vol-migrate-601
[root@os-migrate os-migrate-works]#


[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/export_routers.yml
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/export_router_interfaces.yml
```

### import
---

```
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/import_routers.yml
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/export_router_interfaces.yml
```

```
[root@os-migrate os-migrate-works]# source train-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack router list
+--------------------------------------+--------------+--------+-------+----------------------------------+
| ID                                   | Name         | Status | State | Project                          |
+--------------------------------------+--------------+--------+-------+----------------------------------+
| ed2e97d0-984c-4f98-b5c5-e60e50c51874 | demo-router1 | ACTIVE | UP    | 8b724954d39e462b9c4c085a0084da09 |
+--------------------------------------+--------------+--------+-------+----------------------------------+
[root@os-migrate os-migrate-works]# openstack router show demo-router1
+-------------------------+------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Field                   | Value                                                                                                                                                                                    |
+-------------------------+------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| admin_state_up          | UP                                                                                                                                                                                       |
| availability_zone_hints |                                                                                                                                                                                          |
| availability_zones      | nova                                                                                                                                                                                     |
| created_at              | 2021-03-27T16:12:06Z                                                                                                                                                                     |
| description             |                                                                                                                                                                                          |
| external_gateway_info   | {"network_id": "1e7e36d4-0b1b-4190-82aa-2c5a731fca0c", "enable_snat": true, "external_fixed_ips": [{"subnet_id": "fee1c9a6-332a-450e-9d15-d50fac941a41", "ip_address": "172.16.10.18"}]} |
| flavor_id               | None                                                                                                                                                                                     |
| id                      | ed2e97d0-984c-4f98-b5c5-e60e50c51874                                                                                                                                                     |
| interfaces_info         | [{"port_id": "4183a61c-58dd-4b58-9aff-3a6573e012e5", "ip_address": "192.168.0.254", "subnet_id": "4ed70ab3-fbd9-44cc-9e21-1a0086790f1b"}]                                                |
| location                | cloud='', project.domain_id='default', project.domain_name=, project.id='8b724954d39e462b9c4c085a0084da09', project.name='demo', region_name='RegionOne', zone=                          |
| name                    | demo-router1                                                                                                                                                                             |
| project_id              | 8b724954d39e462b9c4c085a0084da09                                                                                                                                                         |
| revision_number         | 4                                                                                                                                                                                        |
| routes                  |                                                                                                                                                                                          |
| status                  | ACTIVE                                                                                                                                                                                   |
| tags                    |                                                                                                                                                                                          |
| updated_at              | 2021-03-27T16:12:30Z                                                                                                                                                                     |
+-------------------------+------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
```