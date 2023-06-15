#!/bin/sh

# devstack for queens
uvt-kvm create --cpu 4 --memory 16384 --disk 40 q-devstack release=xenial --password ubuntu --template ./q-devstack-template.xml

# devstack for train
uvt-kvm create --cpu 4 --memory 16384 --disk 40 t-devstack release=bionic --password ubuntu --template ./t-devstack-template.xml
