# source ~/devstack/openrc admin admin

openstack image create --file ./cent8-openstack.qcow2 --container-format bare --disk-format qcow2 centos8
openstack flavor create cent8-512 --vcpus 1 --ram 512
openstack flavor create m1.nano --vcpus 1 --ram 256

openstack network create --external --share --provider-physical-network public --provider-network-type vlan --provider-segment 500 public-500
openstack subnet create --network public-500 --dns-nameserver 8.8.8.8 --allocation-pool start=172.16.10.10,end=172.16.10.50 --gateway 172.16.10.254 --subnet-range 172.16.10.0/24 public-500


# source ~/devstack/openrc demo demo

openstack keypair create demo-key01 --private-key ./demo-key01.txt
chmod 0400 ./demo-key01.txt

openstack security group create demo-sec01
neutron security-group-rule-create --protocol icmp  --direction ingress demo-sec01
neutron security-group-rule-create --protocol tcp --port-range-min 22 --port-range-max 1023 --direction ingress demo-sec01

openstack network create demo-net01
openstack subnet create --network demo-net01 demo-subnet01 --allocation-pool start=192.168.0.10,end=192.168.0.50 --subnet-range 192.168.0.0/24 --gateway 192.168.0.254
openstack router create demo-router1
openstack router add subnet demo-router1 demo-subnet01
openstack router set --external-gateway public-500 demo-router1

openstack server create --image cirros-0.3.5-x86_64-disk --network demo-net01 --flavor m1.nano --key-name demo-key01 --security-group demo-sec01 demo-vm01

openstack volume create --size 1 --image cirros-0.3.5-x86_64-disk vol01
openstack server create --volume vol01 --network demo-net01 --flavor m1.nano --key-name demo-key01 --security-group demo-sec01 demo-vm02

for i in `seq 1 3`;do openstack floating ip create public-500;done
