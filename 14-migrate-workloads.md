# migrate workloads
---

### on queens
---

launch two instances for the testing.
```
[root@os-migrate os-migrate-works]# openstack server create --network demo-net01 --image cirros-0.3.5-x86_64-disk --security-group demo-sec01 --flavor cirros256 --key-name key01 cirros-01


[root@os-migrate os-migrate-works]# openstack volume create --size 1 --image cirros-0.3.5-x86_64-disk boot-vol-cirros

[root@os-migrate os-migrate-works]# openstack server create --network demo-net01 --volume boot-vol-cirros --security-group demo-sec01 --flavor cirros256 --key-name key01 cirros-01-cinder-boot

[root@os-migrate os-migrate-works]# openstack server add floating ip cirros-01 172.16.10.19
[root@os-migrate os-migrate-works]# openstack server add floating ip cirros-01-cinder-boot 172.16.10.27

[root@os-migrate os-migrate-works]# openstack server add floating ip cirros-01-cinder-boot 172.16.10.27
[root@os-migrate os-migrate-works]# openstack server list
+--------------------------------------+-----------------------+--------+---------------------------------------+--------------------------+-----------+
| ID                                   | Name                  | Status | Networks                              | Image                    | Flavor    |
+--------------------------------------+-----------------------+--------+---------------------------------------+--------------------------+-----------+
| fb75bbb8-e6a4-482f-ac2d-ef0423a5664d | cirros-01-cinder-boot | ACTIVE | demo-net01=192.168.0.32, 172.16.10.27 |                          | cirros256 |
| 188a1abc-3f23-4934-b97f-b3c96dc0fc1a | cirros-01             | ACTIVE | demo-net01=192.168.0.20, 172.16.10.19 | cirros-0.3.5-x86_64-disk | cirros256 |
+--------------------------------------+-----------------------+--------+---------------------------------------+--------------------------+-----------+
[root@os-migrate os-migrate-works]#


[root@os-migrate os-migrate-works]# ssh -i ./key01-queens.txt cirros@172.16.10.27
$ touch a.txt
$ exit

[root@os-migrate os-migrate-works]# ssh -i ./key01-queens.txt cirros@172.16.10.19
$ touch a.txt
$ exit

[root@os-migrate os-migrate-works]# openstack server stop cirros-01 cirros-01-cinder-boot
```


### export
---

```
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/export_workloads.yml
```

### import ( an instance with ephemeral disk boot instance )
---

edit file(import only one instance, named cirros-01)


```
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/import_workloads.yml

TASK [os_migrate.os_migrate.import_workloads : create destination instance] *****************************************************************************************************************************************************************
An exception occurred during task execution. To see the full traceback, use -vvv. The error was: openstack.exceptions.BadRequestException: BadRequestException: 400: Client Error for url: http://192.168.123.20/compute/v2.1/servers, Invalid input for field/attribute server. Value: {u'name': u'cirros-01', u'imageRef': u'909038a6-5c65-449b-9ce3-c1f06680dc2b', u'tags': [], u'key_name': u'key01', u'flavorRef': u'7901cba4-1f22-4741-8995-97d336716f1c', u'availability_zone': u'nova', u'OS-DCF:diskConfig': u'MANUAL', u'block_device_mapping_v2': [{u'source_type': u'image', u'uuid': u'909038a6-5c65-449b-9ce3-c1f06680dc2b', u'boot_index': 0, u'delete_on_termination': True, u'destination_type': u'local'}], u'networks': [{u'fixed_ip': u'192.168.0.20', u'uuid': u'5b5baf42-7db9-4953-be9c-04bd00660ee1'}], u'security_groups': [{u'name': u'demo-sec01'}], u'metadata': {}}. Additional properties are not allowed (u'tags' was unexpected)
fatal: [localhost]: FAILED! => {"changed": false, "module_stderr": "Traceback (most recent call last):\n  File \"/root/.ansible/tmp/ansible-tmp-1617011685.2101316-49023-35221293700620/AnsiballZ_import_workload_create_instance.py\", line 102, in <module>\n    _ansiballz_main()\n  File \"/root/.ansible/tmp/ansible-tmp-1617011685.2101316-49023-35221293700620/AnsiballZ_import_workload_create_instance.py\", line 94, in _ansiballz_main\n    invoke_module(zipped_mod, temp_path, ANSIBALLZ_PARAMS)\n  File \"/root/.ansible/tmp/ansible-tmp-1617011685.2101316-49023-35221293700620/AnsiballZ_import_workload_create_instance.py\", line 40, in invoke_module\n    runpy.run_module(mod_name='ansible_collections.os_migrate.os_migrate.plugins.modules.import_workload_create_instance', init_globals=None, run_name='__main__', alter_sys=True)\n  File \"/usr/lib64/python3.6/runpy.py\", line 205, in run_module\n    return _run_module_code(code, init_globals, run_name, mod_spec)\n  File \"/usr/lib64/python3.6/runpy.py\", line 96, in _run_module_code\n    mod_name, mod_spec, pkg_name, script_name)\n  File \"/usr/lib64/python3.6/runpy.py\", line 85, in _run_code\n    exec(code, run_globals)\n  File \"/tmp/ansible_os_migrate.os_migrate.import_workload_create_instance_payload_neg_39ll/ansible_os_migrate.os_migrate.import_workload_create_instance_payload.zip/ansible_collections/os_migrate/os_migrate/plugins/modules/import_workload_create_instance.py\", line 255, in <module>\n  File \"/tmp/ansible_os_migrate.os_migrate.import_workload_create_instance_payload_neg_39ll/ansible_os_migrate.os_migrate.import_workload_create_instance_payload.zip/ansible_collections/os_migrate/os_migrate/plugins/modules/import_workload_create_instance.py\", line 251, in main\n  File \"/tmp/ansible_os_migrate.os_migrate.import_workload_create_instance_payload_neg_39ll/ansible_os_migrate.os_migrate.import_workload_create_instance_payload.zip/ansible_collections/os_migrate/os_migrate/plugins/modules/import_workload_create_instance.py\", line 241, in run_module\n  File \"/tmp/ansible_os_migrate.os_migrate.import_workload_create_instance_payload_neg_39ll/ansible_os_migrate.os_migrate.import_workload_create_instance_payload.zip/ansible_collections/os_migrate/os_migrate/plugins/module_utils/server.py\", line 87, in create\n  File \"/usr/lib/python3.6/site-packages/openstack/compute/v2/_proxy.py\", line 435, in create_server\n    return self._create(_server.Server, **attrs)\n  File \"/usr/lib/python3.6/site-packages/openstack/proxy.py\", line 417, in _create\n    return res.create(self, base_path=base_path)\n  File \"/usr/lib/python3.6/site-packages/openstack/resource.py\", line 1280, in create\n    self._translate_response(response, has_body=has_body)\n  File \"/usr/lib/python3.6/site-packages/openstack/resource.py\", line 1107, in _translate_response\n    exceptions.raise_from_response(response, error_message=error_message)\n  File \"/usr/lib/python3.6/site-packages/openstack/exceptions.py\", line 229, in raise_from_response\n    http_status=http_status, request_id=request_id\nopenstack.exceptions.BadRequestException: BadRequestException: 400: Client Error for url: http://192.168.123.20/compute/v2.1/servers, Invalid input for field/attribute server. Value: {u'name': u'cirros-01', u'imageRef': u'909038a6-5c65-449b-9ce3-c1f06680dc2b', u'tags': [], u'key_name': u'key01', u'flavorRef': u'7901cba4-1f22-4741-8995-97d336716f1c', u'availability_zone': u'nova', u'OS-DCF:diskConfig': u'MANUAL', u'block_device_mapping_v2': [{u'source_type': u'image', u'uuid': u'909038a6-5c65-449b-9ce3-c1f06680dc2b', u'boot_index': 0, u'delete_on_termination': True, u'destination_type': u'local'}], u'networks': [{u'fixed_ip': u'192.168.0.20', u'uuid': u'5b5baf42-7db9-4953-be9c-04bd00660ee1'}], u'security_groups': [{u'name': u'demo-sec01'}], u'metadata': {}}. Additional properties are not allowed (u'tags' was unexpected)\n", "module_stdout": "", "msg": "MODULE FAILURE\nSee stdout/stderr for the exact error", "rc": 1}

TASK [os_migrate.os_migrate.import_workloads : clean up in the source cloud after migration failure] ****************************************************************************************************************************************
ok: [localhost] => {"changed": false}

TASK [os_migrate.os_migrate.import_workloads : clean up in the destination cloud after migration failure] ***********************************************************************************************************************************
ok: [localhost] => {"changed": false}

TASK [os_migrate.os_migrate.import_workloads : ansible.builtin.fail] ************************************************************************************************************************************************************************
fatal: [localhost]: FAILED! => {"changed": false, "msg": "Failed to import cirros-01."}

PLAY RECAP **********************************************************************************************************************************************************************************************************************************
localhost                  : ok=22   changed=2    unreachable=0    failed=1    skipped=3    rescued=1    ignored=0

[root@os-migrate os-migrate-works]#
```

```
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/import_workloads.yml

localhost                  : ok=20   changed=2    unreachable=0    failed=0    skipped=3    rescued=0    ignored=0
```

delete metadta, tag, change flavor
```
[root@os-migrate os-migrate-works]# cat /root/os-migrate-data/workloads.yml
os_migrate_version: 0.7.0
resources:
- _info:
    created_at: '2021-03-29T03:57:30Z'
    flavor_id: c1
    id: 188a1abc-3f23-4934-b97f-b3c96dc0fc1a
    launched_at: '2021-03-29T03:57:36.000000'
    security_group_ids:
    - 292f39b4-3094-4681-afeb-a626fd689a75
    status: SHUTOFF
    updated_at: '2021-03-29T09:42:02Z'
    user_id: 60b0caf2cf114e0db0df55f395ea2049
  _migration_params:
    boot_disk_copy: false
    floating_ip_mode: auto
  params:
    availability_zone: nova
    config_drive: null
    description: null
    disk_config: MANUAL
    flavor_ref:
      domain_name: null
      name: cirros256-02
      project_name: null
    floating_ips:
    - _info:
        created_at: '2021-03-29T03:55:51Z'
        floating_network_id: 7f2cb496-a3f9-49ec-9743-8dd4c09f568d
        id: e637cc45-37fd-43e1-bead-e079f78ea995
        port_id: a18f5b95-1411-4bec-91fc-20855434b2fb
        qos_policy_id: null
        router_id: 25e43ea6-7aa1-4bfd-8c1d-914c0774f2c9
        #tags: []
        updated_at: '2021-03-29T04:00:47Z'
      _migration_params: {}
      params:
        description: ''
        dns_domain: null
        dns_name: null
        fixed_ip_address: 192.168.0.20
        floating_ip_address: 172.16.10.19
        floating_network_ref:
          domain_name: null
          name: public-500
          project_name: null
        qos_policy_ref: null
      type: openstack.network.ServerFloatingIP
    image_ref:
      domain_name: null
      name: cirros-0.3.5-x86_64-disk
      project_name: null
    key_name: key01
    #metadata: {}
    name: cirros-01
    ports:
    - _info:
        device_id: 188a1abc-3f23-4934-b97f-b3c96dc0fc1a
        device_owner: compute:nova
        id: a18f5b95-1411-4bec-91fc-20855434b2fb
      _migration_params: {}
      params:
        fixed_ips_refs:
        - ip_address: 192.168.0.20
          subnet_ref:
            domain_name: '%auth%'
            name: demo-subnet01
            project_name: '%auth%'
        network_ref:
          domain_name: '%auth%'
          name: demo-net01
          project_name: '%auth%'
      type: openstack.network.ServerPort
    scheduler_hints: null
    security_group_refs:
    - domain_name: '%auth%'
      name: demo-sec01
      project_name: '%auth%'
    #tags: []
    user_data: null
  type: openstack.compute.Server
[root@os-migrate os-migrate-works]#
```

after completing the migration.
different floating IP will be assigned.
```
[root@os-migrate os-migrate-works]# source queens-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack server list
+--------------------------------------+-----------------------+---------+---------------------------------------------+--------------------------+-----------+
| ID                                   | Name                  | Status  | Networks                                    | Image                    | Flavor    |
+--------------------------------------+-----------------------+---------+---------------------------------------------+--------------------------+-----------+
| b5b9faaf-ea67-41af-aa16-5b67d3118bcd | os_migrate_conv       | ACTIVE  | os_migrate_conv=192.168.10.70, 172.16.30.42 | centos8                  | cent8-512 |
| fb75bbb8-e6a4-482f-ac2d-ef0423a5664d | cirros-01-cinder-boot | SHUTOFF | demo-net01=192.168.0.32, 172.16.10.27       |                          | cirros256 |
| 188a1abc-3f23-4934-b97f-b3c96dc0fc1a | cirros-01             | SHUTOFF | demo-net01=192.168.0.20, 172.16.10.19       | cirros-0.3.5-x86_64-disk | cirros256 |
+--------------------------------------+-----------------------+---------+---------------------------------------------+--------------------------+-----------+
[root@os-migrate os-migrate-works]#
[root@os-migrate os-migrate-works]# source train-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack server list
+--------------------------------------+-----------------+--------+---------------------------------------------+--------------------------+--------------+
| ID                                   | Name            | Status | Networks                                    | Image                    | Flavor       |
+--------------------------------------+-----------------+--------+---------------------------------------------+--------------------------+--------------+
| 0902c945-d276-4212-9170-b627d878d5c6 | cirros-01       | ACTIVE | demo-net01=192.168.0.20, 172.16.10.40       | cirros-0.3.5-x86_64-disk | cirros256-02 |
| f1c1b818-29d6-453f-8e7a-6448baac66a1 | os_migrate_conv | ACTIVE | os_migrate_conv=192.168.10.57, 172.16.31.28 | centos8                  | cent8-512    |
+--------------------------------------+-----------------+--------+---------------------------------------------+--------------------------+--------------+
[root@os-migrate os-migrate-works]#
```

text file(a.txt) is not migrated.
```
[root@os-migrate os-migrate-works]# ssh -i ./key01-queens.txt cirros@172.16.10.40
$ ls -la
total 6
drwxr-xr-x    3 cirros   cirros        1024 Mar 29 12:59 .
drwxrwxr-x    4 root     root          1024 Feb 10  2017 ..
-rw-------    1 cirros   cirros          36 Mar 29 12:59 .ash_history
-rwxr-xr-x    1 cirros   cirros          43 Feb 10  2017 .profile
-rwxr-xr-x    1 cirros   cirros          66 Feb 10  2017 .shrc
drwxr-xr-x    2 cirros   cirros        1024 Mar 29 12:55 .ssh
$
```

migration logs will be stored:
```
[root@os-migrate ~]# ls /root/os-migrate-data/workload_logs/
cirros-01-cinder-boot.log  cirros-01-cinder-boot.state  cirros-01.log
[root@os-migrate ~]#
```

### import cinder-boot instance
---

```
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/import_workloads.yml

localhost                  : ok=20   changed=2    unreachable=0    failed=0    skipped=3    rescued=0    ignored=0
```

migration logs while migrating data.
I saw an error `qemu-kvm: cannot set up guest memory 'pc.ram': Cannot allocate memory` while running migrating workloads.
conversion host may not have enough memory...
```
[root@os-migrate workload_logs]# tail -n0 -f cirros-01-cinder-boot.log
virt-sparsify: error: libguestfs error: could not create appliance through
libvirt.

Try running qemu directly without libvirt using this environment variable:
export LIBGUESTFS_BACKEND=direct

Original error from libvirt: internal error: process exited while
connecting to monitor: 2021-03-29T12:07:19.067659Z qemu-kvm: cannot set up
guest memory 'pc.ram': Cannot allocate memory [code=1 int1=-1]

If reporting bugs, run virt-sparsify with debugging enabled and include the
complete output:

  virt-sparsify -v -x [...]
2021-03-29 12:07:20,391:INFO: Sparsify return code: 1 (import_workload_transfer_volumes:505)
2021-03-29 12:07:20,391:INFO: Sparsify failed, converting whole device... (import_workload_transfer_volumes:511)
2021-03-29 12:07:23,112:INFO: Copying volume data... (import_workload_transfer_volumes:444)
2021-03-29 12:07:26,117:INFO: Transfer progress for /dev/vda: 0.0% (workload_common:125)
2021-03-29 12:07:27,119:INFO: Transfer progress for /dev/vda: 1.17% (workload_common:125)
2021-03-29 12:07:30,122:INFO: Transfer progress for /dev/vda: 2.34% (workload_common:125)

2021-03-29 12:10:31,313:INFO: Transfer progress for /dev/vda: 100.0% (workload_common:125)
2021-03-29 12:10:33,315:INFO: Transfer progress for /dev/vda: 100.0% (workload_common:125)
2021-03-29 12:10:34,317:INFO: Conversion return code: 0 (import_workload_transfer_volumes:476)
2021-03-29 12:10:34,317:INFO: Transfer progress for /dev/vda: 100.0% (workload_common:125)
2021-03-29 12:10:34,317:INFO: Waiting for lock /var/lock/v2v-destination-volume-lock... (workload_common:36)
2021-03-29 12:10:38,637:INFO: Detaching volumes from destination wrapper. (import_workload_transfer_volumes:518)
2021-03-29 12:10:47,568:DEBUG: Released lock:  (workload_common:60)
2021-03-29 12:10:47,568:INFO: Stopping export forwarding on source conversion host... (import_workload_transfer_volumes:378)
2021-03-29 12:10:47,568:DEBUG: (PID was 52208) (import_workload_transfer_volumes:379)
2021-03-29 12:10:47,569:INFO: Stopping forwarding from source conversion host... (import_workload_transfer_volumes:384)
2021-03-29 12:10:52,955:DEBUG: Stopped forwarding PID (61847).  (import_workload_transfer_volumes:392)
2021-03-29 12:10:52,955:INFO: Waiting for lock /var/lock/v2v-migration-lock... (workload_common:36)
2021-03-29 12:11:10,110:INFO: Currently used ports: [49152] (workload_common:239)
2021-03-29 12:11:10,111:INFO: Cleaning used ports: set() (workload_common:303)
2021-03-29 12:11:21,870:DEBUG: Released lock:  (workload_common:60)
2021-03-29 12:11:36,289:INFO: Stopping exports from source conversion host... (import_workload_src_cleanup:314)
2021-03-29 12:11:38,043:DEBUG: Stopping NBD export PIDs (['62224']) (import_workload_src_cleanup:319)
2021-03-29 12:11:40,781:INFO: Waiting for lock /var/lock/v2v-migration-lock... (workload_common:36)
2021-03-29 12:11:55,753:INFO: Currently used ports: [49152] (workload_common:239)
2021-03-29 12:11:55,753:INFO: Cleaning used ports: set() (workload_common:303)
2021-03-29 12:12:05,107:DEBUG: Released lock:  (workload_common:60)
2021-03-29 12:12:05,107:INFO: Waiting for lock /var/lock/v2v-source-volume-lock... (workload_common:36)
2021-03-29 12:12:09,915:INFO: Detaching volumes from the source conversion host. (import_workload_src_cleanup:339)
2021-03-29 12:12:10,077:INFO: Inspecting volume bcf689ad-a53d-4e60-b990-cab638924414 (import_workload_src_cleanup:343)
2021-03-29 12:12:18,123:DEBUG: Released lock:  (workload_common:60)
2021-03-29 12:12:18,123:INFO: Re-attaching volumes to source VM... (import_workload_src_cleanup:362)
2021-03-29 12:12:18,123:INFO: Removing copy of root volume (import_workload_src_cleanup:366)
2021-03-29 12:12:20,430:INFO: Deleting temporary root device snapshot (import_workload_src_cleanup:372)
```

after completing the migration.
different floating IP will be assigned.
```
[root@os-migrate os-migrate-works]# source train-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack server list
+--------------------------------------+-----------------------+--------+---------------------------------------------+--------------------------+--------------+
| ID                                   | Name                  | Status | Networks                                    | Image                    | Flavor       |
+--------------------------------------+-----------------------+--------+---------------------------------------------+--------------------------+--------------+
| 72e9769f-c06e-4b20-ace7-a2cb9a3cdf34 | cirros-01-cinder-boot | ACTIVE | demo-net01=192.168.0.32, 172.16.10.36       |                          | cirros256    |
| 0902c945-d276-4212-9170-b627d878d5c6 | cirros-01             | ACTIVE | demo-net01=192.168.0.20, 172.16.10.40       | cirros-0.3.5-x86_64-disk | cirros256-02 |
| f1c1b818-29d6-453f-8e7a-6448baac66a1 | os_migrate_conv       | ACTIVE | os_migrate_conv=192.168.10.57, 172.16.31.28 | centos8                  | cent8-512    |
+--------------------------------------+-----------------------+--------+---------------------------------------------+--------------------------+--------------+
[root@os-migrate os-migrate-works]#

$ hostname
cirros-01-cinder-boot
$ ls
a.txt
$


[root@os-migrate os-migrate-works]# source queens-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack server list
+--------------------------------------+-----------------------+---------+---------------------------------------------+--------------------------+-----------+
| ID                                   | Name                  | Status  | Networks                                    | Image                    | Flavor    |
+--------------------------------------+-----------------------+---------+---------------------------------------------+--------------------------+-----------+
| b5b9faaf-ea67-41af-aa16-5b67d3118bcd | os_migrate_conv       | ACTIVE  | os_migrate_conv=192.168.10.70, 172.16.30.42 | centos8                  | cent8-512 |
| fb75bbb8-e6a4-482f-ac2d-ef0423a5664d | cirros-01-cinder-boot | SHUTOFF | demo-net01=192.168.0.32, 172.16.10.27       |                          | cirros256 |
| 188a1abc-3f23-4934-b97f-b3c96dc0fc1a | cirros-01             | SHUTOFF | demo-net01=192.168.0.20, 172.16.10.19       | cirros-0.3.5-x86_64-disk | cirros256 |
+--------------------------------------+-----------------------+---------+---------------------------------------------+--------------------------+-----------+
[root@os-migrate os-migrate-works]#

```

```
[root@os-migrate os-migrate-works]# cat /root/os-migrate-data/workloads.yml
os_migrate_version: 0.7.0
resources:
- _info:
    created_at: '2021-03-29T04:00:21Z'
    flavor_id: c1
    id: fb75bbb8-e6a4-482f-ac2d-ef0423a5664d
    launched_at: '2021-03-29T04:00:30.000000'
    security_group_ids:
    - 292f39b4-3094-4681-afeb-a626fd689a75
    status: SHUTOFF
    updated_at: '2021-03-29T09:42:02Z'
    user_id: 60b0caf2cf114e0db0df55f395ea2049
  _migration_params:
    boot_disk_copy: true
    floating_ip_mode: auto
  params:
    availability_zone: nova
    config_drive: null
    description: null
    disk_config: MANUAL
    flavor_ref:
      domain_name: null
      name: cirros256
      project_name: null
    floating_ips:
    - _info:
        created_at: '2021-03-25T05:20:02Z'
        floating_network_id: 7f2cb496-a3f9-49ec-9743-8dd4c09f568d
        id: 44316c94-bb95-4069-a6f4-43739ca5caaa
        port_id: c02c8f22-d945-4a40-b5e1-c9a7b9bcdccd
        qos_policy_id: null
        router_id: 25e43ea6-7aa1-4bfd-8c1d-914c0774f2c9
        #tags: []
        updated_at: '2021-03-29T04:01:06Z'
      _migration_params: {}
      params:
        description: ''
        dns_domain: null
        dns_name: null
        fixed_ip_address: 192.168.0.32
        floating_ip_address: 172.16.10.27
        floating_network_ref:
          domain_name: null
          name: public-500
          project_name: null
        qos_policy_ref: null
      type: openstack.network.ServerFloatingIP
    image_ref: null
    key_name: key01
    #metadata: {}
    name: cirros-01-cinder-boot
    ports:
    - _info:
        device_id: fb75bbb8-e6a4-482f-ac2d-ef0423a5664d
        device_owner: compute:nova
        id: c02c8f22-d945-4a40-b5e1-c9a7b9bcdccd
      _migration_params: {}
      params:
        fixed_ips_refs:
        - ip_address: 192.168.0.32
          subnet_ref:
            domain_name: '%auth%'
            name: demo-subnet01
            project_name: '%auth%'
        network_ref:
          domain_name: '%auth%'
          name: demo-net01
          project_name: '%auth%'
      type: openstack.network.ServerPort
    scheduler_hints: null
    security_group_refs:
    - domain_name: '%auth%'
      name: demo-sec01
      project_name: '%auth%'
    #tags: []
    user_data: null
  type: openstack.compute.Server
[root@os-migrate os-migrate-works]#
```
