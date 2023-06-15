# export, import network, subnet of provider segment
---

### export
---

export, import with admin priviledge.

queens
```
[root@os-migrate os-migrate-works]# source queens-admin-admin-openrc.sh
[root@os-migrate os-migrate-works]# openstack network list
+--------------------------------------+------------+--------------------------------------+
| ID                                   | Name       | Subnets                              |
+--------------------------------------+------------+--------------------------------------+
| 7f2cb496-a3f9-49ec-9743-8dd4c09f568d | public-500 | 86d3d4ae-bc57-4aea-a6d6-162af09cd384 |
| af498457-6b4d-4060-ac3a-ef684e053367 | demo-net01 | 26ecb024-3346-4145-9439-2e157c4fc55e |
+--------------------------------------+------------+--------------------------------------+
[root@os-migrate os-migrate-works]# openstack subnet list
+--------------------------------------+---------------+--------------------------------------+----------------+
| ID                                   | Name          | Network                              | Subnet         |
+--------------------------------------+---------------+--------------------------------------+----------------+
| 26ecb024-3346-4145-9439-2e157c4fc55e | demo-subnet01 | af498457-6b4d-4060-ac3a-ef684e053367 | 192.168.0.0/24 |
| 86d3d4ae-bc57-4aea-a6d6-162af09cd384 | public-500    | 7f2cb496-a3f9-49ec-9743-8dd4c09f568d | 172.16.10.0/24 |
+--------------------------------------+---------------+--------------------------------------+----------------+
[root@os-migrate os-migrate-works]#
```

```
[root@os-migrate os-migrate-works]# cat admin-project-os-migrate-vars.yml
# queens
os_migrate_src_auth:
  auth_url: http://192.168.123.14/identity
  password: devstack
  project_domain_name: Default
  project_name: admin
  user_domain_name: Default
  username: admin
  #username: demo
# train
os_migrate_src_region_name: RegionOne
os_migrate_dst_auth:
  auth_url: http://192.168.123.20/identity
  password: devstack
  project_domain_name: Default
  project_name: admin
  user_domain_name: Default
  username: admin
  #username: demo
os_migrate_dst_region_name: RegionOne

os_migrate_data_dir: /root/os-migrate-data-admin

os_migrate_src_conversion_net_mtu: 1400
os_migrate_dst_conversion_net_mtu: 1400

os_migrate_conversion_host_ssh_user: centos
os_migrate_conversion_flavor_name: cent8-512
os_migrate_conversion_image_name: centos8

os_migrate_src_conversion_external_network_name: vol-migrate-600
os_migrate_dst_conversion_external_network_name: vol-migrate-601

[root@os-migrate os-migrate-works]#
```

```
[root@os-migrate os-migrate-works]# $OSM_CMD -i $OSM_DIR/localhost_inventory.yml $OSM_DIR/playbooks/export_networks.yml -e @./admin-project-os-migrate-vars.yml

[root@os-migrate os-migrate-works]# $OSM_CMD -i $OSM_DIR/localhost_inventory.yml $OSM_DIR/playbooks/export_subnets.yml -e @./admin-project-os-migrate-vars.yml
```

network
```
[root@os-migrate os-migrate-works]# cat /root/os-migrate-data-admin/networks.yml
os_migrate_version: 0.7.0
resources:
- _info:
    availability_zones:
    - nova
    created_at: '2021-03-25T05:12:33Z'
    id: 7f2cb496-a3f9-49ec-9743-8dd4c09f568d
    project_id: f89e7437221945da9b929cc5d9896d77
    qos_policy_id: null
    revision_number: 7
    status: ACTIVE
    subnet_ids:
    - 86d3d4ae-bc57-4aea-a6d6-162af09cd384
    updated_at: '2021-03-25T05:12:40Z'
  _migration_params: {}
  params:
    availability_zone_hints: []
    description: ''
    dns_domain: null
    is_admin_state_up: true
    is_default: false
    is_port_security_enabled: true
    is_router_external: true
    is_shared: true
    is_vlan_transparent: null
    mtu: 1500
    name: public-500
    provider_network_type: vlan
    provider_physical_network: public
    provider_segmentation_id: 500
    qos_policy_ref: null
    segments: null
    tags: []
  type: openstack.network.Network
```

subnet
```
[root@os-migrate os-migrate-works]# cat /root/os-migrate-data-admin/subnets.yml
os_migrate_version: 0.7.0
resources:
- _info:
    created_at: '2021-03-25T05:12:40Z'
    id: 86d3d4ae-bc57-4aea-a6d6-162af09cd384
    network_id: 7f2cb496-a3f9-49ec-9743-8dd4c09f568d
    prefix_length: null
    project_id: f89e7437221945da9b929cc5d9896d77
    revision_number: 0
    segment_id: null
    subnet_pool_id: null
    updated_at: '2021-03-25T05:12:40Z'
  _migration_params: {}
  params:
    allocation_pools:
    - end: 172.16.10.50
      start: 172.16.10.10
    cidr: 172.16.10.0/24
    description: ''
    dns_nameservers:
    - 8.8.8.8
    gateway_ip: 172.16.10.254
    host_routes: []
    ip_version: 4
    ipv6_address_mode: null
    ipv6_ra_mode: null
    is_dhcp_enabled: true
    name: public-500
    network_ref:
      domain_name: '%auth%'
      name: public-500
      project_name: '%auth%'
    segment_ref: null
    service_types: []
    subnet_pool_ref: null
    tags: []
    use_default_subnet_pool: null
  type: openstack.subnet.Subnet
[root@os-migrate os-migrate-works]#
```

### import
---

```
[root@os-migrate os-migrate-works]# $OSM_CMD -i $OSM_DIR/localhost_inventory.yml -e @./admin-project-os-migrate-vars.yml $OSM_DIR/playbooks/import_networks.yml

[root@os-migrate os-migrate-works]# $OSM_CMD -i $OSM_DIR/localhost_inventory.yml -e @./admin-project-os-migrate-vars.yml $OSM_DIR/playbooks/import_subnets.yml
```

train
```
[root@os-migrate os-migrate-works]# source train-admin-admin-openrc.sh
[root@os-migrate os-migrate-works]# openstack network list
+--------------------------------------+------------+--------------------------------------+
| ID                                   | Name       | Subnets                              |
+--------------------------------------+------------+--------------------------------------+
| 1e7e36d4-0b1b-4190-82aa-2c5a731fca0c | public-500 | fee1c9a6-332a-450e-9d15-d50fac941a41 |
+--------------------------------------+------------+--------------------------------------+
[root@os-migrate os-migrate-works]# openstack subnet list
+--------------------------------------+------------+--------------------------------------+----------------+
| ID                                   | Name       | Network                              | Subnet         |
+--------------------------------------+------------+--------------------------------------+----------------+
| fee1c9a6-332a-450e-9d15-d50fac941a41 | public-500 | 1e7e36d4-0b1b-4190-82aa-2c5a731fca0c | 172.16.10.0/24 |
+--------------------------------------+------------+--------------------------------------+----------------+
[root@os-migrate os-migrate-works]#
```