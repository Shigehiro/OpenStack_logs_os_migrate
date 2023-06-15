# deploy conversion host on both queens and train cloud
---

### on queens
---

prepare an external network for conversion host
```
[root@os-migrate os-migrate-works]# source queens-admin-admin-openrc.sh
[root@os-migrate os-migrate-works]# openstack network create --external --share --provider-physical-network public --provider-network-type vlan --provider-segment 600 vol-migrate-600
[root@os-migrate os-migrate-works]# openstack subnet create --network vol-migrate-600 --dns-nameserver 8.8.8.8 --allocation-pool start=172.16.30.10,end=172.16.30.50 --gateway 172.16.30.254 --subnet-range 172.16.30.0/24 vol-migrate-600

[root@os-migrate os-migrate-works]# openstack network list
+--------------------------------------+-----------------+--------------------------------------+
| ID                                   | Name            | Subnets                              |
+--------------------------------------+-----------------+--------------------------------------+
| 1db02755-324d-4f83-ae23-4727860c0150 | vol-migrate-600 | 391e54ec-20f0-49df-9a0c-c4680cecaaaa |
| 7f2cb496-a3f9-49ec-9743-8dd4c09f568d | public-500      | 86d3d4ae-bc57-4aea-a6d6-162af09cd384 |
| af498457-6b4d-4060-ac3a-ef684e053367 | demo-net01      | 26ecb024-3346-4145-9439-2e157c4fc55e |
+--------------------------------------+-----------------+--------------------------------------+
[root@os-migrate os-migrate-works]# openstack image list |grep cent
| 34ce6ce3-8c38-4a67-9cde-9e7518958c89 | centos8                  | active |
[root@os-migrate os-migrate-works]# openstack flavor list |grep cent
| 6aba3875-bf1d-4bdc-9401-4889c1d0603b | cent8-512 |   512 |    0 |         0 |     1 | True      |
[root@os-migrate os-migrate-works]#
```

### on train
---

```
[root@os-migrate os-migrate-works]# source train-admin-admin-openrc.sh
[root@os-migrate os-migrate-works]# openstack network create --external --share --provider-physical-network public --provider-network-type vlan --provider-segment 601 vol-migrate-601

[root@os-migrate os-migrate-works]# openstack subnet create --network vol-migrate-601 --dns-nameserver 8.8.8.8 --allocation-pool start=172.16.31.10,end=172.16.31.50 --gateway 172.16.31.254 --subnet-range 172.16.31.0/24 vol-migrate-601

[root@os-migrate os-migrate-works]# openstack flavor list |grep cent
| fe4eb68b-5607-4262-a9de-98d13858cce2 | cent8-512 |   512 |    0 |         0 |     1 | True      |
[root@os-migrate os-migrate-works]# openstack image list |grep cent
| c102e37a-7881-48bf-a125-861f3d112576 | centos8                  | active |
[root@os-migrate os-migrate-works]#
```

### deploy conversion host in demo project on both queens and train cloud
---

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
```

ssh keys of conversion host will be saved under {{ os_migrate_data_dir }}/conversion deirectory.

error.
failed to attach floating ip on train cloud.
```
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/deploy_conversion_hosts.yml

TASK [os_migrate.os_migrate.conversion_host : create os_migrate conversion keypair] *********************************************************************************************************************************************************
changed: [localhost] => {"changed": true, "id": "os_migrate_conv", "key": {"created_at": null, "fingerprint": "d5:53:42:c2:81:32:1d:fb:cb:f6:8d:2a:04:64:18:3d", "id": "os_migrate_conv", "is_deleted": null, "location": {"cloud": "defaults", "project": {"domain_id": null, "domain_name": "VALUE_SPECIFIED_IN_NO_LOG_PARAMETER", "id": "8b724954d39e462b9c4c085a0084da09", "name": "VALUE_SPECIFIED_IN_NO_LOG_PARAMETER"}, "region_name": "RegionOne", "zone": null}, "name": "os_migrate_conv", "private_key": null, "public_key": "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAACAQDE4XeVdgpZsmOGAeGO+ydzOSrDhHLj7QzcLs2XXFffvfd5bIPPCl95+B08mEiLmfAb5+tU2wbeU+dfhtwmXI0pDtg86OsK1tXGI6M2LwSgAO+rR2RB1Ohxp7Wt6szb8wZfTnFCtZ3K3zpU7CARP9muJAC3Hx6Xgmm/E/njV0L72pbPPU11gBMZ7sj+y/N0CBHlN3wmir0YOqR9fAFixcS14P6uVgvuGA9ZgoALRWvq7p7qNtUThawUAErAJDDhX+nIGMP/KunkeorEFnKke/2NW4DiXzHeQRJ9ti6qzxZttMUJDrPEkEyJvnhzLwfX+Gz7PSnhmHgbT3zxBknlaL7HViYzN6Z0Qa/FgaAep4LtbtoEqlPoR+KqT9Yt/NMUhQG5FOHroJ85jSktWqm3SmoY2zOxsAYPhvxIhZ3ht9ftHgkdeXkroODMfvYWb9kg5yAN5JK6/e6+uvDqDUgBaUopN4M/woglmdxwjEU7+3Y5nRei7kRxa/VgypLPhJuatUC3p/7nnOV+OVlcDjJa1eB/ZWq3xDtFx2CMKBa/t8fslPsjlMwPRjt275fctQLaMGToXAWnKdYQgle2rLJldh4ByAxj3MH91jFjXMxCZBFBlzo6A/aUNjHuLqeh3bcxAbVbWd1uFKQvvyLlhfCp1FQeiROtR2jf026EdsZ1Xr4GKw==", "type": "ssh", "user_id": "56a47133be614cc587ab5c2f75ead7be"}}

TASK [os_migrate.os_migrate.conversion_host : create os_migrate conversion host] ************************************************************************************************************************************************************
fatal: [localhost]: FAILED! => {"changed": false, "extra_data": {"data": null, "details": "None", "response": "None"}, "msg": "Timeout waiting for the floating IP to be attached."}

NO MORE HOSTS LEFT **************************************************************************************************************************************************************************************************************************

PLAY RECAP **********************************************************************************************************************************************************************************************************************************
localhost                  : ok=22   changed=15   unreachable=0    failed=1    skipped=0    rescued=0    ignored=0
```

```
[root@os-migrate os-migrate-works]# source queens-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack server list
+--------------------------------------+-----------------------+--------+---------------------------------------------+--------------------------+-----------+
| ID                                   | Name                  | Status | Networks                                    | Image                    | Flavor    |
+--------------------------------------+-----------------------+--------+---------------------------------------------+--------------------------+-----------+
| b5b9faaf-ea67-41af-aa16-5b67d3118bcd | os_migrate_conv       | ACTIVE | os_migrate_conv=192.168.10.70, 172.16.30.42 | centos8                  | cent8-512 |
| fb75bbb8-e6a4-482f-ac2d-ef0423a5664d | cirros-01-cinder-boot | ACTIVE | demo-net01=192.168.0.32, 172.16.10.27       |                          | cirros256 |
| 188a1abc-3f23-4934-b97f-b3c96dc0fc1a | cirros-01             | ACTIVE | demo-net01=192.168.0.20, 172.16.10.19       | cirros-0.3.5-x86_64-disk | cirros256 |
+--------------------------------------+-----------------------+--------+---------------------------------------------+--------------------------+-----------+
[root@os-migrate os-migrate-works]#
[root@os-migrate os-migrate-works]# source train-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack server list
+--------------------------------------+-----------------+--------+-------------------------------+---------+-----------+
| ID                                   | Name            | Status | Networks                      | Image   | Flavor    |
+--------------------------------------+-----------------+--------+-------------------------------+---------+-----------+
| f1c1b818-29d6-453f-8e7a-6448baac66a1 | os_migrate_conv | ACTIVE | os_migrate_conv=192.168.10.57 | centos8 | cent8-512 |
+--------------------------------------+-----------------+--------+-------------------------------+---------+-----------+
[root@os-migrate os-migrate-works]#
```

```
[root@os-migrate os-migrate-works]# source train-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack server list
+--------------------------------------+-----------------+--------+-------------------------------+---------+-----------+
| ID                                   | Name            | Status | Networks                      | Image   | Flavor    |
+--------------------------------------+-----------------+--------+-------------------------------+---------+-----------+
| f1c1b818-29d6-453f-8e7a-6448baac66a1 | os_migrate_conv | ACTIVE | os_migrate_conv=192.168.10.57 | centos8 | cent8-512 |
+--------------------------------------+-----------------+--------+-------------------------------+---------+-----------+

[root@os-migrate os-migrate-works]# openstack floating ip create vol-migrate-601
```

manually attach a floating ip.
```
[root@os-migrate os-migrate-works]# source train-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack server list
+--------------------------------------+-----------------+--------+-------------------------------+---------+-----------+
| ID                                   | Name            | Status | Networks                      | Image   | Flavor    |
+--------------------------------------+-----------------+--------+-------------------------------+---------+-----------+
| f1c1b818-29d6-453f-8e7a-6448baac66a1 | os_migrate_conv | ACTIVE | os_migrate_conv=192.168.10.57 | centos8 | cent8-512 |
+--------------------------------------+-----------------+--------+-------------------------------+---------+-----------+
[root@os-migrate os-migrate-works]# openstack floating ip list
+--------------------------------------+---------------------+------------------+------+--------------------------------------+----------------------------------+
| ID                                   | Floating IP Address | Fixed IP Address | Port | Floating Network                     | Project                          |
+--------------------------------------+---------------------+------------------+------+--------------------------------------+----------------------------------+
| 7628aac3-4cde-49b5-8292-147230f367dc | 172.16.10.47        | None             | None | 1e7e36d4-0b1b-4190-82aa-2c5a731fca0c | 8b724954d39e462b9c4c085a0084da09 |
| f6762b7f-dc63-40eb-91e9-71ebce91af80 | 172.16.31.28        | None             | None | 796ed6e5-1a40-470a-932b-27d2231e11ea | 8b724954d39e462b9c4c085a0084da09 |
+--------------------------------------+---------------------+------------------+------+--------------------------------------+----------------------------------+
[root@os-migrate os-migrate-works]# openstack server add floating ip os_migrate_conv 172.16.31.28
[root@os-migrate os-migrate-works]#
```

while deploying conversion host, some of RPMs will be installed, which means that conversion host needs to access to the internet.
```
[root@os-migrate os-migrate-works]# $OSM_CMD $OSM_DIR/playbooks/deploy_conversion_hosts.yml


TASK [os_migrate.os_migrate.conversion_host_content : install content] **********************************************************************************************************************************************************************
changed: [os_migrate_conv_src] => {"changed": true, "msg": "", "rc": 0, "results": ["Installed: perl-IO-Socket-IP-0.39-5.el8.noarch", "Installed: libdatrie-0.2.9-7.el8.x86_64", "Installed: perl-IO-Socket-SSL-2.066-4.module_el8.3.0+410+ff426aa3.noarch", "Installed: gnutls-dane-3.6.14-7.el8_3.x86_64", "Installed: gnutls-utils-3.6.14-7.el8_3.x86_64", "Installed: libdrm-2.4.101-1.el8.x86_64", "Installed: nmap-ncat-2:7.70-5.el8.x86_64", "Installed: radvd-2.17-15.el8.x86_64", "Installed: edk2-ovmf-20200602gitca407c7246bf-3.el8.noarch", "Installed: libepoxy-1.5.3-1.el8.x86_64", "Installed: libthai-0.1.27-2.el8.x86_64", "Installed: libtheora-1:1.1.1-21.el8.x86_64", "Installed: yajl-2.1.0-10.el8.x86_64", "Installed: nspr-4.25.0-2.el8_2.x86_64", "Installed: nss-3.53.1-17.el8_3.x86_64", "Installed: nss-softokn-3.53.1-17.el8_3.x86_64", "Installed: libusal-1.1.11-39.el8.x86_64", "Installed: nss-softokn-freebl-3.53.1-17.el8_3.x86_64", "Installed: nss-sysinit-3.53.1-17.el8_3.x86_64", "Installed: perl-Mozilla-CA-20160104-7.module_el8.3.0+416+dee7bcef.noarch", "Installed: nss-util-3.53.1-17.el8_3.x86_64", "Installed: glusterfs-6.0-37.2.el8.x86_64", "Installed: libvirt-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: glusterfs-client-xlators-6.0-37.2.el8.x86_64", "Installed: libvirt-bash-completion-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libvirt-client-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libvirt-daemon-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libvirt-daemon-config-network-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libvirt-daemon-config-nwfilter-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libvirt-daemon-driver-interface-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libvirt-daemon-driver-network-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libvirt-daemon-driver-nodedev-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libvirt-daemon-driver-nwfilter-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libvirt-daemon-driver-qemu-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libglvnd-1:1.2.0-6.el8.x86_64", "Installed: libvirt-daemon-driver-storage-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libvirt-daemon-driver-secret-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libvirt-daemon-driver-storage-core-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libvirt-daemon-driver-storage-gluster-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libvirt-daemon-driver-storage-disk-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libglvnd-egl-1:1.2.0-6.el8.x86_64", "Installed: libvirt-daemon-driver-storage-iscsi-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libvirt-daemon-driver-storage-mpath-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libglvnd-gles-1:1.2.0-6.el8.x86_64", "Installed: libvirt-daemon-driver-storage-scsi-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libglvnd-glx-1:1.2.0-6.el8.x86_64", "Installed: libvirt-daemon-driver-storage-iscsi-direct-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libvirt-daemon-driver-storage-logical-6.0.0-28.module_el8.3.0+555+a55c8938.x86_64", "Installed: libvirt-daemon-driver-storage-rbd-6.0.0-28.module_el8.3.0+555+


TASK [os_migrate.os_migrate.conversion_host_content : start libvirtd] ***********************************************************************************************************************************************************************
changed: [os_migrate_conv_src] => {"changed": true, "enabled": true, "name": "libvirtd", "state": "started", "status": {"ActiveEnterTimestampMonotonic": "0", "ActiveExitTimestampMonotonic": "0", "ActiveState": "inactive", "After": "systemd-machined.service basic.target local-fs.target virtlockd.socket network.target apparmor.service libvirtd-ro.socket libvirtd.socket system.slice virtlockd.service iscsid.service xencommons.service sysinit.target systemd-logind.service systemd-journald.socket libvirtd-admin.socket virtlockd-admin.socket remote-fs.target virtlogd.service virtlogd.socket dbus.service virtlogd-admin.socket", "AllowIsolate": "no", "AllowedCPUs": "", "AllowedMemoryNodes": "", "AmbientCapabilities": "", "AssertResult": "no", "AssertTimestampMonotonic": "0", "Before": "shutdown.target libvirt-guests.service multi-user.target", "BlockIOAccounting": "no", "BlockIOWeight": "[not set]", "CPUAccounting": "no", "CPUAffinity": "", "CPUQuotaPerSecUSec": "infinity", "CPUQuotaPeriodUSec": "infinity", "CPUSchedulingPolicy": "0", "CPUSchedulingPriority": "0", "CPUSchedulingResetOnFork": "no", "CPUShares": "[not set]", "CPUUsageNSec": "[not set]", "CPUWeight": "[not set]", "CacheDirectoryMode": "0755", "CanFreeze": "yes", "CanIsolate": "no", "CanReload": "yes", "CanStart": "yes", "CanStop": "yes", "CapabilityBoundingSet": "cap_chown cap_dac_override cap_dac_read_search cap_fowner cap_fsetid cap_kill cap_setgid cap_setuid cap_setpcap cap_linux_immutable cap_net_bind_service cap_net_broadcast cap_net_admin cap_net_raw cap_ipc_lock cap_ipc_owner cap_sys_module cap_sys_rawio cap_sys_chroot cap_sys_ptrace cap_sys_pacct cap_sys_admin cap_sys_boot cap_sys_nice cap_sys_resource cap_sys_time cap_sys_tty_config cap_mknod cap_lease cap_audit_write cap_audit_control cap_setfcap cap_mac_override cap_mac_admin cap_syslog cap_wake_alarm cap_block_suspend", "CollectMode": "inactive", "ConditionResult": "no", "ConditionTimestampMonotonic": "0", "ConfigurationDirectoryMode": "0755", "Conflicts": "xendomains.service shutdown.target", "ControlPID": "0", "DefaultDependencies": "yes", "DefaultMemoryLow": "0", "DefaultMemoryMin": "0", "Delegate": "no", "Description": "Virtualization daemon", "DevicePolicy": "auto", "Documentation": "man:libvirtd(8) https://libvirt.org", "DynamicUser": "no", "EffectiveCPUs": "", "EffectiveMemoryNodes": "", "EnvironmentFiles": "/etc/sysconfig/libvirtd (ignore_errors=yes)", "ExecMainCode": "0", "ExecMainExitTimestampMonotonic": "0", "ExecMainPID": "0", "ExecMainStartTimestampMonotonic": "0", "ExecMainStatus": "0", "ExecReload": "{ path=/bin/kill ; argv[]=/bin/kill -HUP $MAINPID ; ignore_errors=no ; start_time=[n/a] ; stop_time=[n/a] ; pid=0 ; code=(null) ; status=0/0 }", "ExecStart": "{ path=/usr/sbin/libvirtd ; argv[]=/usr/sbin/libvirtd $LIBVIRTD_ARGS ; ignore_errors=no ; start_time=[n/a] ; stop_time=[n/a] ; pid=0 ; code=(null) ; status=0/0 }", "FailureAction": "none", "FileDescriptorStoreMax": "0", "FragmentPath": "/usr/lib/systemd/system/libvirtd.service", "FreezerState": "running", "GID": "[not set]", "GuessMainPID": "yes", "IOAccounting": "no", "IOSchedulingClass": "0", "IOSchedulingPriority": "0", "IOWeight": "[not set]", "IPAccounting": "no", "IPEgressBytes": "18446744073709551615", "IPEgressPackets": "18446744073709551615", "IPIngressBytes": "18446744073709551615", "IPIngressPackets": "18446744073709551615", "Id": "libvirtd.service", "IgnoreOnIsolate": "no", "IgnoreSIGPIPE": "yes", "InactiveEnterTimestampMonotonic": "0", "InactiveExitTimestampMonotonic": "0", "JobRunningTimeoutUSec": "infinity", "JobTimeoutAction": "none", "JobTimeoutUSec": "infinity", "KeyringMode": "private", "KillMode": "process", "KillSignal": "15", "LimitAS": "infinity", "LimitASSoft": "infinity", "LimitCORE": "infinity", "LimitCORESoft": "infinity", "LimitCPU": "infinity", "LimitCPUSoft": "infinity", "LimitDATA": "infinity", "LimitDATASoft": "infinity", "LimitFSIZE": "infinity", "LimitFSIZESoft": "infinity", "LimitLOCKS": "infinity", "LimitLOCKSSoft": "infinity", "LimitMEMLOCK": "65536", "LimitMEMLOCKSoft": "65536", "LimitMSGQUEUE": "819200", "LimitMSGQUEUESoft": "819200", "LimitNICE": "0", "LimitNICESoft": "0", "LimitNOFILE": "8192", "LimitNOFILESoft": "8192", "LimitNPROC": "1605", "LimitNPROCSoft": "1605", "LimitRSS": "infinity", "LimitRSSSoft": "infinity", "LimitRTPRIO": "0", "LimitRTPRIOSoft": "0", "LimitRTTIME": "infinity", "LimitRTTIMESoft": "infinity", "LimitSIGPENDING": "1605", "LimitSIGPENDINGSoft": "1605", "LimitSTACK": "infinity", "LimitSTACKSoft": "8388608", "LoadState": "loaded", "LockPersonality": "no", "LogLevelMax": "-1", "LogRateLimitBurst": "0", "LogRateLimitIntervalUSec": "0", "LogsDirectoryMode": "0755", "MainPID": "0", "MemoryAccounting": "yes", "MemoryCurrent": "[not set]", "MemoryDenyWriteExecute": "no", "MemoryHigh": "infinity", "MemoryLimit": "infinity", "MemoryLow": "0", "MemoryMax": "infinity", "MemoryMin": "0", "MemorySwapMax": "infinity", "MountAPIVFS": "no", "MountFlags": "", "NFileDescriptorStore": "0", "NRestarts": "0", "NUMAMask": "", "NUMAPolicy": "n/a", "Names": "libvirtd.service", "NeedDaemonReload": "no", "Nice": "0", "NoNewPrivileges": "no", "NonBlocking": "no", "NotifyAccess": "main", "OOMScoreAdjust": "0", "OnFailureJobMode": "replace", "PermissionsStartOnly": "no", "Perpetual": "no", "PrivateDevices": "no", "PrivateMounts": "no", "PrivateNetwork": "no", "PrivateTmp": "no", "PrivateUsers": "no", "ProtectControlGroups": "no", "ProtectHome": "no", "ProtectKernelModules": "no", "ProtectKernelTunables": "no", "ProtectSystem": "no", "RefuseManualStart": "no", "RefuseManualStop": "no", "RemainAfterExit": "no", "RemoveIPC": "no", "Requires": "virtlogd.socket system.slice virtlockd.socket sysinit.target", "Restart": "on-failure", "RestartUSec": "100ms", "RestrictNamespaces": "no", "RestrictRealtime": "no", "RestrictSUIDSGID": "no", "Result": "success", "RootDirectoryStartOnly": "no", "RuntimeDirectoryMode": "0755", "RuntimeDirectoryPreserve": "no", "RuntimeMaxUSec": "infinity", "SameProcessGroup": "no", "SecureBits": "0", "SendSIGHUP": "no", "SendSIGKILL": "yes", "Slice": "system.slice", "StandardError": "inherit", "StandardInput": "null", "StandardInputData": "", "StandardOutput": "journal", "StartLimitAction": "none", "StartLimitBurst": "5", "StartLimitIntervalUSec": "10s", "StartupBlockIOWeight": "[not set]", "StartupCPUShares": "[not set]", "StartupCPUWeight": "[not set]", "StartupIOWeight": "[not set]", "StateChangeTimestampMonotonic": "0", "StateDirectoryMode": "0755", "StatusErrno": "0", "StopWhenUnneeded": "no", "SubState": "dead", "SuccessAction": "none", "SyslogFacility": "3", "SyslogLevel": "6", "SyslogLevelPrefix": "yes", "SyslogPriority": "30", "SystemCallErrorNumber": "0", "TTYReset": "no", "TTYVHangup": "no", "TTYVTDisallocate": "no", "TasksAccounting": "yes", "TasksCurrent": "[not set]", "TasksMax": "32768", "TimeoutStartUSec": "1min 30s", "TimeoutStopUSec": "1min 30s", "TimerSlackNSec": "50000", "Transient": "no", "TriggeredBy": "libvirtd-ro.socket libvirtd.socket libvirtd-admin.socket", "Type": "notify", "UID": "[not set]", "UMask": "0022", "UnitFilePreset": "enabled", "UnitFileState": "enabled", "UtmpMode": "init", "WantedBy": "multi-user.target libvirt-guests.service", "Wants": "libvirtd.socket libvirtd-admin.socket systemd-machined.service libvirtd-ro.socket", "WatchdogTimestampMonotonic": "0", "WatchdogUSec": "0"}}


TASK [os_migrate.os_migrate.conversion_host_content : Include RHEL tasks] *******************************************************************************************************************************************************************
skipping: [os_migrate_conv_src] => {"changed": false, "skip_reason": "Conditional result was False"}
skipping: [os_migrate_conv_dst] => {"changed": false, "skip_reason": "Conditional result was False"}

PLAY RECAP **********************************************************************************************************************************************************************************************************************************
localhost                  : ok=33   changed=2    unreachable=0    failed=0    skipped=2    rescued=0    ignored=0
os_migrate_conv_dst        : ok=10   changed=4    unreachable=0    failed=0    skipped=1    rescued=0    ignored=0
os_migrate_conv_src        : ok=10   changed=4    unreachable=0    failed=0    skipped=1    rescued=0    ignored=0

[root@os-migrate os-migrate-works]#
```

```
[root@os-migrate os-migrate-works]# source queens-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack server list
+--------------------------------------+-----------------------+--------+---------------------------------------------+--------------------------+-----------+
| ID                                   | Name                  | Status | Networks                                    | Image                    | Flavor    |
+--------------------------------------+-----------------------+--------+---------------------------------------------+--------------------------+-----------+
| b5b9faaf-ea67-41af-aa16-5b67d3118bcd | os_migrate_conv       | ACTIVE | os_migrate_conv=192.168.10.70, 172.16.30.42 | centos8                  | cent8-512 |
| fb75bbb8-e6a4-482f-ac2d-ef0423a5664d | cirros-01-cinder-boot | ACTIVE | demo-net01=192.168.0.32, 172.16.10.27       |                          | cirros256 |
| 188a1abc-3f23-4934-b97f-b3c96dc0fc1a | cirros-01             | ACTIVE | demo-net01=192.168.0.20, 172.16.10.19       | cirros-0.3.5-x86_64-disk | cirros256 |
+--------------------------------------+-----------------------+--------+---------------------------------------------+--------------------------+-----------+
[root@os-migrate os-migrate-works]#
[root@os-migrate os-migrate-works]# openstack network list
+--------------------------------------+-----------------+--------------------------------------+
| ID                                   | Name            | Subnets                              |
+--------------------------------------+-----------------+--------------------------------------+
| 1004d078-b690-4467-93ac-c2372f457d81 | os_migrate_conv | 987f321b-885b-4681-aa02-693da2a275b0 |
| 1db02755-324d-4f83-ae23-4727860c0150 | vol-migrate-600 | 391e54ec-20f0-49df-9a0c-c4680cecaaaa |
| 7f2cb496-a3f9-49ec-9743-8dd4c09f568d | public-500      | 86d3d4ae-bc57-4aea-a6d6-162af09cd384 |
| af498457-6b4d-4060-ac3a-ef684e053367 | demo-net01      | 26ecb024-3346-4145-9439-2e157c4fc55e |
+--------------------------------------+-----------------+--------------------------------------+
[root@os-migrate os-migrate-works]#
[root@os-migrate os-migrate-works]# openstack subnet list
+--------------------------------------+-----------------+--------------------------------------+-----------------+
| ID                                   | Name            | Network                              | Subnet          |
+--------------------------------------+-----------------+--------------------------------------+-----------------+
| 26ecb024-3346-4145-9439-2e157c4fc55e | demo-subnet01   | af498457-6b4d-4060-ac3a-ef684e053367 | 192.168.0.0/24  |
| 391e54ec-20f0-49df-9a0c-c4680cecaaaa | vol-migrate-600 | 1db02755-324d-4f83-ae23-4727860c0150 | 172.16.30.0/24  |
| 86d3d4ae-bc57-4aea-a6d6-162af09cd384 | public-500      | 7f2cb496-a3f9-49ec-9743-8dd4c09f568d | 172.16.10.0/24  |
| 987f321b-885b-4681-aa02-693da2a275b0 | os_migrate_conv | 1004d078-b690-4467-93ac-c2372f457d81 | 192.168.10.0/24 |
+--------------------------------------+-----------------+--------------------------------------+-----------------+
[root@os-migrate os-migrate-works]#
[root@os-migrate os-migrate-works]# openstack security group list
+--------------------------------------+-----------------+------------------------+----------------------------------+------+
| ID                                   | Name            | Description            | Project                          | Tags |
+--------------------------------------+-----------------+------------------------+----------------------------------+------+
| 292f39b4-3094-4681-afeb-a626fd689a75 | demo-sec01      | demo-sec01             | 4fcea3f39f8843dd817a78261588437d | []   |
| 363bb60f-ad24-4c2f-be83-491e575db37d | default         | Default security group | 4fcea3f39f8843dd817a78261588437d | []   |
| 903db396-cfee-479e-8b99-bcc8643354b1 | os_migrate_conv |                        | 4fcea3f39f8843dd817a78261588437d | []   |
+--------------------------------------+-----------------+------------------------+----------------------------------+------+
[root@os-migrate os-migrate-works]#
[root@os-migrate os-migrate-works]# openstack keypair list
+-----------------+-------------------------------------------------+
| Name            | Fingerprint                                     |
+-----------------+-------------------------------------------------+
| key01           | c5:2f:eb:6d:c0:2c:3c:94:45:4f:07:18:84:9c:80:94 |
| os_migrate_conv | d5:53:42:c2:81:32:1d:fb:cb:f6:8d:2a:04:64:18:3d |
+-----------------+-------------------------------------------------+
[root@os-migrate os-migrate-works]# openstack router list
+--------------------------------------+-----------------+--------+-------+----------------------------------+
| ID                                   | Name            | Status | State | Project                          |
+--------------------------------------+-----------------+--------+-------+----------------------------------+
| 25e43ea6-7aa1-4bfd-8c1d-914c0774f2c9 | demo-router1    | ACTIVE | UP    | 4fcea3f39f8843dd817a78261588437d |
| a750b782-2d31-4465-affe-b7eecf44c381 | os_migrate_conv | ACTIVE | UP    | 4fcea3f39f8843dd817a78261588437d |
+--------------------------------------+-----------------+--------+-------+----------------------------------+
[root@os-migrate os-migrate-works]#
```

```
[root@os-migrate os-migrate-works]# source train-demo-demo-openrc.sh
[root@os-migrate os-migrate-works]# openstack server list
+--------------------------------------+-----------------+--------+---------------------------------------------+---------+-----------+
| ID                                   | Name            | Status | Networks                                    | Image   | Flavor    |
+--------------------------------------+-----------------+--------+---------------------------------------------+---------+-----------+
| f1c1b818-29d6-453f-8e7a-6448baac66a1 | os_migrate_conv | ACTIVE | os_migrate_conv=192.168.10.57, 172.16.31.28 | centos8 | cent8-512 |
+--------------------------------------+-----------------+--------+---------------------------------------------+---------+-----------+
[root@os-migrate os-migrate-works]# openstack network list
+--------------------------------------+-----------------+--------------------------------------+
| ID                                   | Name            | Subnets                              |
+--------------------------------------+-----------------+--------------------------------------+
| 1e7e36d4-0b1b-4190-82aa-2c5a731fca0c | public-500      | fee1c9a6-332a-450e-9d15-d50fac941a41 |
| 1fc812b6-a2a6-4fc0-98f0-a14535a4e440 | os_migrate_conv | a8464c74-e861-422e-b763-058d0b0c50e2 |
| 5b5baf42-7db9-4953-be9c-04bd00660ee1 | demo-net01      | 4ed70ab3-fbd9-44cc-9e21-1a0086790f1b |
| 796ed6e5-1a40-470a-932b-27d2231e11ea | vol-migrate-601 | acf24c03-cbdc-4915-9b84-07cc8badc4a9 |
+--------------------------------------+-----------------+--------------------------------------+
[root@os-migrate os-migrate-works]# openstack subnet list
+--------------------------------------+-----------------+--------------------------------------+-----------------+
| ID                                   | Name            | Network                              | Subnet          |
+--------------------------------------+-----------------+--------------------------------------+-----------------+
| 4ed70ab3-fbd9-44cc-9e21-1a0086790f1b | demo-subnet01   | 5b5baf42-7db9-4953-be9c-04bd00660ee1 | 192.168.0.0/24  |
| a8464c74-e861-422e-b763-058d0b0c50e2 | os_migrate_conv | 1fc812b6-a2a6-4fc0-98f0-a14535a4e440 | 192.168.10.0/24 |
| acf24c03-cbdc-4915-9b84-07cc8badc4a9 | vol-migrate-601 | 796ed6e5-1a40-470a-932b-27d2231e11ea | 172.16.31.0/24  |
| fee1c9a6-332a-450e-9d15-d50fac941a41 | public-500      | 1e7e36d4-0b1b-4190-82aa-2c5a731fca0c | 172.16.10.0/24  |
+--------------------------------------+-----------------+--------------------------------------+-----------------+
[root@os-migrate os-migrate-works]# openstack router list
+--------------------------------------+-----------------+--------+-------+----------------------------------+
| ID                                   | Name            | Status | State | Project                          |
+--------------------------------------+-----------------+--------+-------+----------------------------------+
| b5464ec6-be8a-4723-8b64-2bb7dfe72e26 | os_migrate_conv | ACTIVE | UP    | 8b724954d39e462b9c4c085a0084da09 |
| ed2e97d0-984c-4f98-b5c5-e60e50c51874 | demo-router1    | ACTIVE | UP    | 8b724954d39e462b9c4c085a0084da09 |
+--------------------------------------+-----------------+--------+-------+----------------------------------+
[root@os-migrate os-migrate-works]#
[root@os-migrate os-migrate-works]# openstack security group list
+--------------------------------------+-----------------+------------------------+----------------------------------+------+
| ID                                   | Name            | Description            | Project                          | Tags |
+--------------------------------------+-----------------+------------------------+----------------------------------+------+
| 3236d47f-2481-4975-9c42-0ef3eaf1d77f | os_migrate_conv |                        | 8b724954d39e462b9c4c085a0084da09 | []   |
| 5e98bda1-ac69-4432-96a9-542ea59f7d0c | default         | Default security group | 8b724954d39e462b9c4c085a0084da09 | []   |
| acd855b6-9d90-4b8a-8be7-9e7c84b44d73 | demo-sec01      | demo-sec01             | 8b724954d39e462b9c4c085a0084da09 | []   |
+--------------------------------------+-----------------+------------------------+----------------------------------+------+
[root@os-migrate os-migrate-works]# openstack keypair list
+-----------------+-------------------------------------------------+
| Name            | Fingerprint                                     |
+-----------------+-------------------------------------------------+
| key01           | c5:2f:eb:6d:c0:2c:3c:94:45:4f:07:18:84:9c:80:94 |
| os_migrate_conv | d5:53:42:c2:81:32:1d:fb:cb:f6:8d:2a:04:64:18:3d |
+-----------------+-------------------------------------------------+
[root@os-migrate os-migrate-works]#
```