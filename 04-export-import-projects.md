# export, import project
---

### export project
---

chnage privilege user(username admin, project admin) to export/import other projects.
```
[root@os-migrate os-migrate-works]# cat os-migrate-vars.yml
# queens
os_migrate_src_auth:
  auth_url: http://192.168.123.14/identity
  password: devstack
  project_domain_name: Default
  #project_name: demo
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
  #project_name: demo
  project_name: admin
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
```

```
[root@os-migrate os-migrate-works]# unset OS_AUTH_URL
[root@os-migrate os-migrate-works]# unset OS_PROJECT_ID
[root@os-migrate os-migrate-works]# unset OS_PROJECT_NAME
[root@os-migrate os-migrate-works]# unset OS_USER_DOMAIN_NAME
[root@os-migrate os-migrate-works]# unset OS_PROJECT_DOMAIN_ID
[root@os-migrate os-migrate-works]# unset OS_USERNAME
[root@os-migrate os-migrate-works]# unset OS_PASSWORD
[root@os-migrate os-migrate-works]# unset OS_REGION_NAME
[root@os-migrate os-migrate-works]# unset OS_INTERFACE
[root@os-migrate os-migrate-works]# unset OS_IDENTITY_API_VERSION

# export OSM_DIR=/root/.ansible/collections/ansible_collections/os_migrate/os_migrate
# export OSM_CMD="ansible-playbook -v -i $OSM_DIR/localhost_inventory.yml -e @os-migrate-vars.yml"

[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/export_projects.yml
```

after editing file.
```
[root@os-migrate os-migrate-works]# cat /root/os-migrate-data/projects.yml
os_migrate_version: 0.7.0
resources:
- _info:
    domain_id: default
    parent_id: default
  _migration_params: {}
  params:
    description: ''
    is_domain: false
    is_enabled: true
    name: demo
  type: openstack.identity.Project
[root@os-migrate os-migrate-works]#
```

### improt project
---

```
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/import_projects.yml
```

```
[root@os-migrate os-migrate-works]# source queens-admin-admin-openrc.sh
[root@os-migrate os-migrate-works]# openstack project list
+----------------------------------+---------+
| ID                               | Name    |
+----------------------------------+---------+
| 402c59bd541a4953b88ec05e8bc6de08 | service |
| 4fcea3f39f8843dd817a78261588437d | demo    |
| f89e7437221945da9b929cc5d9896d77 | admin   |
+----------------------------------+---------+
[root@os-migrate os-migrate-works]#

[root@os-migrate os-migrate-works]# openstack project list
+----------------------------------+---------+
| ID                               | Name    |
+----------------------------------+---------+
| 8b724954d39e462b9c4c085a0084da09 | demo    |
| 980c5469d4a54ed79f39f75d0db4329e | service |
| a1db767b127b40c1afa3cebd75cbf993 | admin   |
+----------------------------------+---------+
[root@os-migrate os-migrate-works]#
```