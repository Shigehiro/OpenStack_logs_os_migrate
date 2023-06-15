# set up OpenStack test env for os-migrate, export, import flavors

### export objects with os migrate
---

use admin user, admin project.

```
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
[root@os-migrate os-migrate-works]# source queens-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack flavor list
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
[root@os-migrate os-migrate-works]#
[root@os-migrate os-migrate-works]# source train-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack flavor list

[root@os-migrate os-migrate-works]#
```

```
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/export_flavors.yml
```

### import
---

error
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

[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/import_flavors.yml

TASK [os_migrate.os_migrate.import_flavors : import flavors] *******************************************************************************************************************************
An exception occurred during task execution. To see the full traceback, use -vvv. The error was: openstack.exceptions.BadRequestException: BadRequestException: 400: Client Error for url: http://192.168.123.20/compute/v2.1/flavors, Invalid input for field/attribute flavor. Value: {u'name': u'm1.tiny', u'links': [{u'href': u'http://192.168.123.14/compute/v2.1/flavors/1', u'rel': u'self'}, {u'href': u'http://192.168.123.14/compute/flavors/1', u'rel': u'bookmark'}], u'ram': 512, u'OS-FLV-DISABLED:disabled': False, u'vcpus': 1, u'swap': u'', u'os-flavor-access:is_public': True, u'rxtx_factor': 1.0, u'OS-FLV-EXT-DATA:ephemeral': 0, u'disk': 1}. Additional properties are not allowed (u'OS-FLV-DISABLED:disabled', u'links' were unexpected)
failed: [localhost] (item={'_info': {'id': '1'}, '_migration_params': {}, 'params': {'description': None, 'disk': 1, 'ephemeral': 0, 'extra_specs': None, 'is_disabled': False, 'is_public': True, 'links': [{'href': 'http://192.168.123.14/compute/v2.1/flavors/1', 'rel': 'self'}, {'href': 'http://192.168.123.14/compute/flavors/1', 'rel': 'bookmark'}], 'name': 'm1.tiny', 'ram': 512, 'rxtx_factor': 1.0, 'swap': '', 'vcpus': 1}, 'type': 'openstack.compute.Flavor'}) => {"ansible_loop_var": "item", "changed": false, "item": {"_info": {"id": "1"}, "_migration_params": {}, "params": {"description": null, "disk": 1, "ephemeral": 0, "extra_specs": null, "is_disabled": false, "is_public": true, "links": [{"href": "http://192.168.123.14/compute/v2.1/flavors/1", "rel": "self"}, {"href": "http://192.168.123.14/compute/flavors/1", "rel": "bookmark"}], "name": "m1.tiny", "ram": 512, "rxtx_factor": 1.0, "swap": "", "vcpus": 1}, "type": "openstack.compute.Flavor"}, "module_stderr": "Traceback (most recent call last):\n  File \"/root/.ansible/tmp/ansible-tmp-1616863000.2964094-35037-149389681406103/AnsiballZ_import_flavor.py\", line 102, in <module>\n    _ansiballz_main()\n  File \"/root/.ansible/tmp/ansible-tmp-1616863000.2964094-35037-149389681406103/AnsiballZ_import_flavor.py\", line 94, in _ansiballz_main\n    i
```

```
[root@os-migrate os-migrate-works]# head -25 /root/os-migrate-data/flavors.yml
os_migrate_version: 0.7.0
resources:
- _info:
    id: '1'
  _migration_params: {}
  params:
    description: null
    disk: 1
    ephemeral: 0
    extra_specs: null
    is_disabled: false
    is_public: true
    links:
    - href: http://192.168.123.14/compute/v2.1/flavors/1
      rel: self
    - href: http://192.168.123.14/compute/flavors/1
      rel: bookmark
    name: m1.tiny
    ram: 512
    rxtx_factor: 1.0
    swap: ''
    vcpus: 1
  type: openstack.compute.Flavor
- _info:
    id: '2'
[root@os-migrate os-migrate-works]#
```

delete links
```
[root@os-migrate os-migrate-works]# grep -v -e links -e href -e rel /root/os-migrate-data/flavors.yml > ./tmp-file
[root@os-migrate os-migrate-works]# mv tmp-file /root/os-migrate-data/flavors.yml
```

another error.
```
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/import_flavors.yml

TASK [os_migrate.os_migrate.import_flavors : import flavors] *******************************************************************************************************************************
An exception occurred during task execution. To see the full traceback, use -vvv. The error was: openstack.exceptions.BadRequestException: BadRequestException: 400: Client Error for url: http://192.168.123.20/compute/v2.1/flavors, Invalid input for field/attribute flavor. Value: {u'name': u'm1.tiny', u'ram': 512, u'OS-FLV-DISABLED:disabled': False, u'vcpus': 1, u'swap': u'', u'os-flavor-access:is_public': True, u'rxtx_factor': 1.0, u'OS-FLV-EXT-DATA:ephemeral': 0, u'disk': 1}. Additional properties are not allowed (u'OS-FLV-DISABLED:disabled' was unexpected)
failed: [localhost] (item={'_info': {'id': '1'}, '_migration_params': {}, 'params': {'description': None, 'disk': 1, 'ephemeral': 0, 'extra_specs': None, 'is_disabled': False, 'is_public': True, 'name': 'm1.tiny', 'ram': 512, 'rxtx_factor': 1.0, 'swap': '', 'vcpus': 1}, 'type': 'openstack.compute.Flavor'}) => {"ansible_loop_var": "item", "changed": false, "item": {"_info": {"id": "1"}, "_migration_params": {}, "params": {"description": null, "disk": 1, "ephemeral": 0, "extra_specs": null, "is_disabled": false, "is_public": true, "name": "m1.tiny", "ram": 512, "rxtx_factor": 1.0, "swap": "", "vcpus": 1}, "type": "openstack.compute.Flavor"}, "module_stderr": "Traceback (most recent call last):\n  File \"/root/.ansible/tmp/ansible-tmp-1616863222.2548535-35376-231193795972745/AnsiballZ_import_flavor.py\", line 102, in <module>\n    _ansiballz_main()\n  File \"/root/.ansible/tmp/ansible-tmp-1616863222.2548535-35376-231193795972745/AnsiballZ_import_flavor.py\", line 94, in _ansiballz_main\n    invoke_module(zipped_mod, temp_path, ANSIBALLZ_PARAMS)\n  File \"/root/.ansible/tmp/ansible-tmp-1616863222.2548535-35376-231193795972745/AnsiballZ_import_flavor.py\", line 40, in invoke_module\n    runpy.run_module(mod_name='ansible_collections.os_migrate.os_migrate.plugins.modules.import_flavor', init_globals=None, run_name='__main__', alter_sys=True)\n  File \"/usr/lib64/python3.6/runpy.py\", line 205, in run_module\n    return _run_module_code(code, init_globals, run_name, mod_spec)\n  File \"/usr/lib64/python3.6/runpy.py\", line 96, in _run_module_code\n    mod_name, mod_spec, pkg_name, script_name)\n  File \"/usr/lib64/python3.6/runpy.py\", line 85, in _run_code\n
```


```
[root@os-migrate os-migrate-works]# head -25 /root/os-migrate-data/flavors.yml
os_migrate_version: 0.7.0
resources:
- _info:
    id: '1'
  _migration_params: {}
  params:
    description: null
    disk: 1
    ephemeral: 0
    extra_specs: null
    is_disabled: false
    is_public: true
    name: m1.tiny
    ram: 512
    rxtx_factor: 1.0
    swap: ''
    vcpus: 1
  type: openstack.compute.Flavor
- _info:
    id: '2'
  _migration_params: {}
  params:
    description: null
    disk: 20
    ephemeral: 0
[root@os-migrate os-migrate-works]#
```

```
[root@os-migrate os-migrate-works]# sed s#"swap: ''"#"swap: 0"# /root/os-migrate-data/flavors.yml -i
```

delete is_disabled param.
```
 1141  grep -v is_disabled /root/os-migrate-data/flavors.yml
 1142  grep -v is_disabled /root/os-migrate-data/flavors.yml > tmp
```

```
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/import_flavors.yml
```

```
[root@os-migrate os-migrate-works]# source train-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]#
[root@os-migrate os-migrate-works]# openstack flavor list
+--------------------------------------+-----------+-------+------+-----------+-------+-----------+
| ID                                   | Name      |   RAM | Disk | Ephemeral | VCPUs | Is Public |
+--------------------------------------+-----------+-------+------+-----------+-------+-----------+
| 35eb4394-8ec2-4e70-88bc-be4e4918fcae | m1.nano   |   256 |    0 |         0 |     1 | True      |
| 39a25676-b0d5-4e02-a44a-98c16d7d6ec9 | m1.small  |  2048 |   20 |         0 |     1 | True      |
| 3eaa360d-45fc-4f00-9374-d54f09b3fdc7 | m1.large  |  8192 |   80 |         0 |     4 | True      |
| 44175728-f9d7-4254-bbd3-1ab5cc46cf68 | ds512M    |   512 |    5 |         0 |     1 | True      |
| 71a041a9-447e-47df-900a-ef8ea611d022 | ds2G      |  2048 |   10 |         0 |     2 | True      |
| 7901cba4-1f22-4741-8995-97d336716f1c | cirros256 |   256 |    0 |         0 |     1 | True      |
| 8043611f-4764-4de6-b8dd-58eb928b26a2 | ds4G      |  4096 |   20 |         0 |     4 | True      |
| 8d271cff-dc1a-40d5-8518-699347902fef | m1.tiny   |   512 |    1 |         0 |     1 | True      |
| d63d1641-a18f-4b27-a1a2-100d1d89ca34 | ds1G      |  1024 |   10 |         0 |     1 | True      |
| fc9db83d-7e06-4ae0-8d24-4464d2b77b63 | m1.xlarge | 16384 |  160 |         0 |     8 | True      |
| fe4eb68b-5607-4262-a9de-98d13858cce2 | cent8-512 |   512 |    0 |         0 |     1 | True      |
| ff8a49ea-5c98-4e7d-a93c-a92c7d805eba | m1.medium |  4096 |   40 |         0 |     2 | True      |
+--------------------------------------+-----------+-------+------+-----------+-------+-----------+
[root@os-migrate os-migrate-works]#
```