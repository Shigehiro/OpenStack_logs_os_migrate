# OpenStack walkthrough logs of os-migrate

## Description
---

Here are walkthrough logs of [os-migrage](!https://github.com/os-migrate/os-migrate)

- [devstack setup](./01-devstack-setup.md)
- [os-migrate-setup](./02-os-migrate-setup.md)
- [openstack environment setup](./03-openstack-env-setup.md)
- [export/import projects](./04-export-import-projects.md)
- [export/import users](./05-export-import-users.md)
- [export/import provier networks](./06-export-import-provider-network-subnet.md)
- [export/import tenant networks](./07-export-import-tenant-network-subnet.md)
- [export/import routers](./08-export-import-router.md)
- [export/import flavors](./09-export-import-flavors.md)
- [export/import security groups](./10-export-import-security-group.md)
- [export/import images](./11-export-import-images.md)
- [export/import keypairs](./12-export-import-keypairs.md)
- [deploy a conversion host](./13-deploy-conversion-host.md)
- [migrate workloads](./14-migrate-workloads.md)

## Migration steps
---

- export, import projects
- export, import users 
- export, import provider networks
- export, import images
- export, import teneant's objects, such as teneant network/subnet, security group, keypair etc
- deploy conversion host
- export, import workloads

## Notes about each step
---

### Projects
---

- admin user required.

### Flavor
---

- admin user required.

### Users
---

- admin user required.
- role, credentials are not exported to the dst cloud.
- after exporting users, set role, credentials to the users.

### Image
---

- admin user required.

### Provider networks
---

- admin user required.

### Conversion host
---

- should I deploy conversion host in tenant's project? or admin project?

### User groups
---

- can not export, import

### project quota, network qos policy, image extra info, flavor extra info
---

- not tested
