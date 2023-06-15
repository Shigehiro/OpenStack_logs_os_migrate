# export, import images
---

### export
---

focal : project demo
cirros, centos8 : project admin 
```
[root@os-migrate os-migrate-works]# source queens-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack image list
+--------------------------------------+--------------------------+--------+
| ID                                   | Name                     | Status |
+--------------------------------------+--------------------------+--------+
| 34ce6ce3-8c38-4a67-9cde-9e7518958c89 | centos8                  | active |
| 2929ced2-af86-40d0-a8ea-fe1ea071fb4c | cirros-0.3.5-x86_64-disk | active |
| d3b4c0c0-9ca5-4e4f-b678-184616cbda05 | focal                    | active |
+--------------------------------------+--------------------------+--------+
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

[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/export_images.yml
```

```
[root@os-migrate os-migrate-works]# grep name /root/os-migrate-data/images.yml
    name: focal
```

### import
---

```
[root@os-migrate os-migrate-works]# source train-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack image list

[root@os-migrate os-migrate-works]#
```

```
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/import_images.yml
```

```
[root@os-migrate os-migrate-works]# source train-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack image list
+--------------------------------------+-------+--------+
| ID                                   | Name  | Status |
+--------------------------------------+-------+--------+
| 7dab494a-af2a-4ba0-8a46-417bc5dfeff1 | focal | active |
+--------------------------------------+-------+--------+
[root@os-migrate os-migrate-works]# openstack project list |grep demo
| 8b724954d39e462b9c4c085a0084da09 | demo |
[root@os-migrate os-migrate-works]# openstack image show  focal|grep owner
| owner            | 8b724954d39e462b9c4c085a0084da09                                                                                                                                                                                                                                                                                                                                                                                   |
| properties       | os_hash_algo='sha512', os_hash_value='909affea143aee8874bcbef645e7b681977ef58985baae30e6bd498373546d83710ff70c14d9949610e534cd3fd6ed6e5ae9e042dcb28a4b5257c410ed2feae0', os_hidden='False', owner_specified.openstack.md5='90cef53961434b8eda11d3dab0f03774', owner_specified.openstack.object='images/focal', owner_specified.openstack.sha256='0126e2c34395025b676f6cce9f3f26e82012f43650c7b784b33a39ac578a8c51' |
[root@os-migrate os-migrate-works]#
```

### export, import imaegs owned by admin project
---

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
```

```
[root@os-migrate os-migrate-works]# ansible-playbook -i /root/.ansible/collections/ansible_collections/os_migrate/os_migrate/localhost_inventory.yml -e @admin-project-os-migrate-vars.yml /root/.ansible/collections/ansible_collections/os_migrate/os_migrate/playbooks/export_images.yml

[root@os-migrate os-migrate-works]# ansible-playbook -i /root/.ansible/collections/ansible_collections/os_migrate/os_migrate/localhost_inventory.yml -e @admin-project-os-migrate-vars.yml /root/.ansible/collections/ansible_collections/os_migrate/os_migrate/playbooks/import_images.yml

TASK [os_migrate.os_migrate.import_images : import images] *********************************************************************************************************************************
changed: [localhost] => (item={'_info': {'checksum': 'f8ab98ff5e73ebab884d80c9dc9c7290', 'created_at': '2021-03-24T18:21:59Z', 'direct_url': None, 'file': '/v2/images/2929ced2-af86-40d0-a8ea-fe1ea071fb4c/file', 'id': '2929ced2-af86-40d0-a8ea-fe1ea071fb4c', 'instance_uuid': None, 'kernel_id': None, 'locations': None, 'metadata': None, 'owner_id': 'f89e7437221945da9b929cc5d9896d77', 'ramdisk_id': None, 'schema': '/v2/schemas/image', 'size': 13267968, 'status': 'active', 'updated_at': '2021-03-24T18:22:00Z', 'url': None, 'virtual_size': None}, '_migration_params': {}, 'params': {'architecture': None, 'container_format': 'bare', 'disk_format': 'qcow2', 'has_auto_disk_config': None, 'hash_algo': None, 'hash_value': None, 'hw_cpu_cores': None, 'hw_cpu_policy': None, 'hw_cpu_sockets': None, 'hw_cpu_thread_policy': None, 'hw_cpu_threads': None, 'hw_disk_bus': None, 'hw_machine_type': None, 'hw_qemu_guest_agent': None, 'hw_rng_model': None, 'hw_scsi_model': None, 'hw_serial_port_count': None, 'hw_video_model': None, 'hw_video_ram': None, 'hw_vif_model': None, 'hw_watchdog_action': None, 'hypervisor_type': None, 'instance_type_rxtx_factor': None, 'is_hidden': None, 'is_hw_boot_menu_enabled': None, 'is_hw_vif_multiqueue_enabled': None, 'is_protected': False, 'kernel_ref': None, 'min_disk': 0, 'min_ram': 0, 'name': 'cirros-0.3.5-x86_64-disk', 'needs_config_drive': None, 'needs_secure_boot': None, 'os_admin_user': None, 'os_command_line': None, 'os_distro': None, 'os_require_quiesce': None, 'os_shutdown_timeout': None, 'os_type': None, 'os_version': None, 'properties': {}, 'ramdisk_ref': None, 'store': None, 'visibility': 'public', 'vm_mode': None, 'vmware_adaptertype': None, 'vmware_ostype': None}, 'type': 'openstack.image.Image'})
An exception occurred during task execution. To see the full traceback, use -vvv. The error was: openstack.exceptions.HttpException: HttpException: 500: Server Error for url: http://192.168.123.20/image/v2/images/37c2349d-7573-42ff-b326-877eae80a05b/file, Internal Server Error
failed: [localhost] (item={'_info': {'checksum': 'bf653cc2b5becb29c6cf7c6f7ecaf70f', 'created_at': '2021-03-27T17:02:26Z', 'direct_url': None, 'file': '/v2/images/34ce6ce3-8c38-4a67-9cde-9e7518958c89/file', 'id': '34ce6ce3-8c38-4a67-9cde-9e7518958c89', 'instance_uuid': None, 'kernel_id': None, 'locations': None, 'metadata': None, 'owner_id': 'f89e7437221945da9b929cc5d9896d77', 'ramdisk_id': None, 'schema': '/v2/schemas/image', 'size': 1275714048, 'status': 'active', 'updated_at': '2021-03-27T17:02:33Z', 'url': None, 'virtual_size': None}, '_migration_params': {}, 'params': {'architecture': None, 'container_format': 'bare', 'disk_format': 'qcow2', 'has_auto_disk_config': None, 'hash_algo': None, 'hash_value': None, 'hw_cpu_cores': None, 'hw_cpu_policy': None, 'hw_cpu_sockets': None, 'hw_cpu_thread_policy': None, 'hw_cpu_threads': None, 'hw_disk_bus': None, 'hw_machine_type': None, 'hw_qemu_guest_agent': None, 'hw_rng_model': None, 'hw_scsi_model': None, 'hw_serial_port_count': None, 'hw_video_model': None, 'hw_video_ram': None, 'hw_vif_model': None, 'hw_watchdog_action': None, 'hypervisor_type': None, 'instance_type_rxtx_factor': None, 'is_hidden': None, 'is_hw_boot_menu_enabled': None, 'is_hw_vif_multiqueue_enabled': None, 'is_protected': False, 'kernel_ref': None, 'min_disk': 0, 'min_ram': 0, 'name': 'centos8', 'needs_config_drive': None, 'needs_secure_boot': None, 'os_admin_user': None, 'os_command_line': None, 'os_distro': None, 'os_require_quiesce': None, 'os_shutdown_timeout': None, 'os_type': None, 'os_version': None, 'properties': {}, 'ramdisk_ref': None, 'store': None, 'visibility': 'public', 'vm_mode': None, 'vmware_adaptertype': None, 'vmware_ostype': No
```

failed to import cent8 image.
```
[root@os-migrate os-migrate-works]# source train-admin-admin-openrc.sh
[root@os-migrate os-migrate-works]# openstack image list
+--------------------------------------+--------------------------+--------+
| ID                                   | Name                     | Status |
+--------------------------------------+--------------------------+--------+
| 909038a6-5c65-449b-9ce3-c1f06680dc2b | cirros-0.3.5-x86_64-disk | active |
| 7dab494a-af2a-4ba0-8a46-417bc5dfeff1 | focal                    | active |
+--------------------------------------+--------------------------+--------+
[root@os-migrate os-migrate-works]#
```

add -vvv
internal server error..
```

            "store": null,
            "visibility": "public",
            "vm_mode": null,
            "vmware_adaptertype": null,
            "vmware_ostype": null
        },
        "type": "openstack.image.Image"
    },
    "module_stderr": "Traceback (most recent call last):\n  File \"/root/.ansible/tmp/ansible-tmp-1616865499.3385403-38265-59980382513865/AnsiballZ_import_image.py\", line 102, in <module>\n    _ansiballz_main()\n  File \"/root/.ansible/tmp/ansible-tmp-1616865499.3385403-38265-59980382513865/AnsiballZ_import_image.py\", line 94, in _ansiballz_main\n    invoke_module(zipped_mod, temp_path, ANSIBALLZ_PARAMS)\n  File \"/root/.ansible/tmp/ansible-tmp-1616865499.3385403-38265-59980382513865/AnsiballZ_import_image.py\", line 40, in invoke_module\n    runpy.run_module(mod_name='ansible_collections.os_migrate.os_migrate.plugins.modules.import_image', init_globals=None, run_name='__main__', alter_sys=True)\n  File \"/usr/lib64/python3.6/runpy.py\", line 205, in run_module\n    return _run_module_code(code, init_globals, run_name, mod_spec)\n  File \"/usr/lib64/python3.6/runpy.py\", line 96, in _run_module_code\n    mod_name, mod_spec, pkg_name, script_name)\n  File \"/usr/lib64/python3.6/runpy.py\", line 85, in _run_code\n    exec(code, run_globals)\n  File \"/tmp/ansible_os_migrate.os_migrate.import_image_payload_cmxy1rb6/ansible_os_migrate.os_migrate.import_image_payload.zip/ansible_collections/os_migrate/os_migrate/plugins/modules/import_image.py\", line 127, in <module>\n  File \"/tmp/ansible_os_migrate.os_migrate.import_image_payload_cmxy1rb6/ansible_os_migrate.os_migrate.import_image_payload.zip/ansible_collections/os_migrate/os_migrate/plugins/modules/import_image.py\", line 123, in main\n  File \"/tmp/ansible_os_migrate.os_migrate.import_image_payload_cmxy1rb6/ansible_os_migrate.os_migrate.import_image_payload.zip/ansible_collections/os_migrate/os_migrate/plugins/modules/import_image.py\", line 117, in run_module\n  File \"/tmp/ansible_os_migrate.os_migrate.import_image_payload_cmxy1rb6/ansible_os_migrate.os_migrate.import_image_payload.zip/ansible_collections/os_migrate/os_migrate/plugins/module_utils/image.py\", line 113, in create_or_update\n  File \"/tmp/ansible_os_migrate.os_migrate.import_image_payload_cmxy1rb6/ansible_os_migrate.os_migrate.import_image_payload.zip/ansible_collections/os_migrate/os_migrate/plugins/module_utils/image.py\", line 130, in _create_sdk_res\n  File \"/usr/lib/python3.6/site-packages/openstack/image/_base_proxy.py\", line 155, in create_image\n    **image_kwargs)\n  File \"/usr/lib/python3.6/site-packages/openstack/image/v2/_proxy.py\", line 175, in _upload_image\n    **kwargs)\n  File \"/usr/lib/python3.6/site-packages/openstack/image/v2/_proxy.py\", line 213, in _upload_image_put\n    exceptions.raise_from_response(response)\n  File \"/usr/lib/python3.6/site-packages/openstack/exceptions.py\", line 229, in raise_from_response\n    http_status=http_status, request_id=request_id\nopenstack.exceptions.HttpException: HttpException: 500: Server Error for url: http://192.168.123.20/image/v2/images/c1b30778-c916-4914-8df5-6038c073850a/file, Internal Server Error\n",
    "module_stdout": "",
    "msg": "MODULE FAILURE\nSee stdout/stderr for the exact error",
    "rc": 1
}
```

log onto the tarin cloud, import cent8 image.
I was able to import the image.
resource issue? ( glance backend is swift)
```
stack@t-devstack:~$ source devstack/openrc admin admin

stack@t-devstack:~$ openstack image create --file ./cent8-openstack.qcow2 --container-format bare --disk-format qcow2 centos8 --public

stack@t-devstack:~$ openstack image list
+--------------------------------------+--------------------------+--------+
| ID                                   | Name                     | Status |
+--------------------------------------+--------------------------+--------+
| c102e37a-7881-48bf-a125-861f3d112576 | centos8                  | active |
| 909038a6-5c65-449b-9ce3-c1f06680dc2b | cirros-0.3.5-x86_64-disk | active |
| 7dab494a-af2a-4ba0-8a46-417bc5dfeff1 | focal                    | active |
+--------------------------------------+--------------------------+--------+
stack@t-devstack:~$
```

```
[root@os-migrate os-migrate-works]# source train-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack image list
+--------------------------------------+--------------------------+--------+
| ID                                   | Name                     | Status |
+--------------------------------------+--------------------------+--------+
| c102e37a-7881-48bf-a125-861f3d112576 | centos8                  | active |
| 909038a6-5c65-449b-9ce3-c1f06680dc2b | cirros-0.3.5-x86_64-disk | active |
| 7dab494a-af2a-4ba0-8a46-417bc5dfeff1 | focal                    | active |
+--------------------------------------+--------------------------+--------+
[root@os-migrate os-migrate-works]#
```
