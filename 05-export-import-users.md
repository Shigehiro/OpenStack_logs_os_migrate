# export, import users
---

### note
---

after exporting users into dst cloud(train), conifgure credentials, role to those users.
https://docs.openstack.org/keystone/train/admin/cli-manage-projects-users-and-roles.html

### export uesrs
---

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

[root@os-migrate os-migrate-works]# openstack user list --project demo
+----------------------------------+--------+
| ID                               | Name   |
+----------------------------------+--------+
| 6a23115f9f4642258867b8b3e220b5f9 | demo01 |
| 60b0caf2cf114e0db0df55f395ea2049 | demo   |
+----------------------------------+--------+

[root@os-migrate os-migrate-works]# source train-admin-admin-openrc.sh
[root@os-migrate os-migrate-works]# openstack user list --project demo

[root@os-migrate os-migrate-works]#
```

I already imported a project demo into train, but no users exist in the demo project.
add admins group into the demo project to so that priviledge user(in this case, admin user) can modify this project.

train
```
[root@os-migrate os-migrate-works]# source train-admin-admin-openrc.sh

[root@os-migrate os-migrate-works]# openstack group list
+----------------------------------+--------+
| ID                               | Name   |
+----------------------------------+--------+
| 1a2e4c17604c440d99ea3de97464dc9b | admins |
+----------------------------------+--------+
[root@os-migrate os-migrate-works]#

[root@os-migrate os-migrate-works]# openstack group contains user admins admin
admin in group admins
[root@os-migrate os-migrate-works]#

[root@os-migrate os-migrate-works]# openstack role list
+----------------------------------+---------------+
| ID                               | Name          |
+----------------------------------+---------------+
| 19320ba3e8d747e6b27848eddd98acfc | anotherrole   |
| 4c3f10042e4e414ea2964d156b33e595 | admin         |
| 4cec9e7dd3714194bf1402cca74c6394 | reader        |
| 9bb01ab58eff485ba1ae747c685662f7 | service       |
| b70a7f4107154506905ee17830b036f3 | member        |
| ecb161abc8e944ac905cee46dfedb08f | ResellerAdmin |
+----------------------------------+---------------+
[root@os-migrate os-migrate-works]#

[root@os-migrate os-migrate-works]# openstack role add admin --group admins --project demo
```

queens
```
[root@os-migrate os-migrate-works]# source queens-admin-admin-openrc.sh
[root@os-migrate os-migrate-works]# openstack group list
+----------------------------------+--------+
| ID                               | Name   |
+----------------------------------+--------+
| 7211d5230db04cc4888cc07b7a4c2528 | demo   |
| a1eaedaea44342048b1815b86b98dd56 | admins |
+----------------------------------+--------+
[root@os-migrate os-migrate-works]# openstack group contains user admins admin
admin in group admins
[root@os-migrate os-migrate-works]# openstack role add admin --group admins --project demo
[root@os-migrate os-migrate-works]#
```

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
```

```
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/export_users.yml
localhost                  : ok=7    changed=1    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```

delet unnecessary users, such as nova, glance etc.
after edit.

```
[root@os-migrate os-migrate-works]# cat /root/os-migrate-data/users.yml
os_migrate_version: 0.7.0
resources:
- _info:
    default_project_id: null
    domain_id: default
    id: 60b0caf2cf114e0db0df55f395ea2049
  _migration_params: {}
  params:
    default_project_ref: null
    description: null
    email: demo@example.com
    is_enabled: true
    name: demo
  type: openstack.user.User
- _info:
    default_project_id: 4fcea3f39f8843dd817a78261588437d
    domain_id: default
    id: 6a23115f9f4642258867b8b3e220b5f9
  _migration_params: {}
  params:
    default_project_ref:
      domain_name: null
      name: demo
      project_name: null
    description: null
    email: demo01@a.com
    is_enabled: true
    name: demo01
  type: openstack.user.User
[root@os-migrate os-migrate-works]#
```

### import users
---

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
```

delete users you do not import.
confirm if project id(demo) matches train's demo project ID.
```
[root@os-migrate os-migrate-works]# source queens-admin-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack project list |grep demo
| 4fcea3f39f8843dd817a78261588437d | demo    |
[root@os-migrate os-migrate-works]#

[root@os-migrate os-migrate-works]# source train-admin-admin-openrc.sh
[root@os-migrate os-migrate-works]# openstack project list |grep demo
| 8b724954d39e462b9c4c085a0084da09 | demo    |
[root@os-migrate os-migrate-works]#

[root@os-migrate os-migrate-works]# sed s/4fcea3f39f8843dd817a78261588437d/8b724954d39e462b9c4c085a0084da09/ -i /root/os-migrate-data/users.yml
[root@os-migrate os-migrate-works]#
```

```
[root@os-migrate os-migrate-works]# cat /root/os-migrate-data/users.yml
os_migrate_version: 0.7.0
resources:
- _info:
    default_project_id: 8b724954d39e462b9c4c085a0084da09
    domain_id: default
    id: 60b0caf2cf114e0db0df55f395ea2049
  _migration_params: {}
  params:
    default_project_ref:
      domain_name: null
      name: demo
      project_name: null
    description: null
    email: demo@example.com
    is_enabled: true
    name: demo
  type: openstack.user.User
- _info:
    default_project_id: 8b724954d39e462b9c4c085a0084da09
    domain_id: default
    id: 6a23115f9f4642258867b8b3e220b5f9
  _migration_params: {}
  params:
    default_project_ref:
      domain_name: null
      name: demo
      project_name: null
    description: null
    email: demo01@a.com
    is_enabled: true
    name: demo01
  type: openstack.user.User
[root@os-migrate os-migrate-works]#
```

```
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/import_users.yml
```

```
[root@os-migrate os-migrate-works]# source train-admin-admin-openrc.sh
[root@os-migrate os-migrate-works]# openstack project list |grep demo
| 8b724954d39e462b9c4c085a0084da09 | demo    |
[root@os-migrate os-migrate-works]#
[root@os-migrate os-migrate-works]# openstack user show demo
+---------------------+----------------------------------+
| Field               | Value                            |
+---------------------+----------------------------------+
| default_project_id  | 8b724954d39e462b9c4c085a0084da09 |
| domain_id           | default                          |
| email               | demo@example.com                 |
| enabled             | True                             |
| id                  | 56a47133be614cc587ab5c2f75ead7be |
| name                | demo                             |
| options             | {}                               |
| password_expires_at | None                             |
+---------------------+----------------------------------+
[root@os-migrate os-migrate-works]#
[root@os-migrate os-migrate-works]# openstack user show demo01
+---------------------+----------------------------------+
| Field               | Value                            |
+---------------------+----------------------------------+
| default_project_id  | 8b724954d39e462b9c4c085a0084da09 |
| domain_id           | default                          |
| email               | demo01@a.com                     |
| enabled             | True                             |
| id                  | 2ed6871a208041b5ab13eee02be20cd5 |
| name                | demo01                           |
| options             | {}                               |
| password_expires_at | None                             |
+---------------------+----------------------------------+
[root@os-migrate os-migrate-works]#
```

set credentials
```
[root@os-migrate os-migrate-works]# source train-admin-admin-openrc.sh

[root@os-migrate os-migrate-works]# openstack user set --project demo demo01 --password-prompt
User Password:
Repeat User Password:
[root@os-migrate os-migrate-works]#

[root@os-migrate os-migrate-works]# openstack user set --project demo demo --password-prompt
User Password:
Repeat User Password:
[root@os-migrate os-migrate-works]#
```

```
[root@os-migrate os-migrate-works]# source train-admin-admin-openrc.sh

[root@os-migrate os-migrate-works]# openstack role list
+----------------------------------+---------------+
| ID                               | Name          |
+----------------------------------+---------------+
| 19320ba3e8d747e6b27848eddd98acfc | anotherrole   |
| 4c3f10042e4e414ea2964d156b33e595 | admin         |
| 4cec9e7dd3714194bf1402cca74c6394 | reader        |
| 9bb01ab58eff485ba1ae747c685662f7 | service       |
| b70a7f4107154506905ee17830b036f3 | member        |
| ecb161abc8e944ac905cee46dfedb08f | ResellerAdmin |
+----------------------------------+---------------+
[root@os-migrate os-migrate-works]#

[root@os-migrate os-migrate-works]# openstack role add --user demo --project demo member
[root@os-migrate os-migrate-works]# openstack role add --user demo01 --project demo member

[root@os-migrate os-migrate-works]# openstack user list --project demo
+----------------------------------+--------+
| ID                               | Name   |
+----------------------------------+--------+
| 56a47133be614cc587ab5c2f75ead7be | demo   |
| 2ed6871a208041b5ab13eee02be20cd5 | demo01 |
+----------------------------------+--------+
[root@os-migrate os-migrate-works]#
```