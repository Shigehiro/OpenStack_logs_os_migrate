interfaces {
    ethernet eth0 {
        address dhcp
        hw-id 52:54:00:83:0d:ca
    }
    ethernet eth1 {
        address 172.16.0.254/24
        hw-id 52:54:00:23:ff:71
        vif 500 {
            address 172.16.10.254/24
        }
        vif 501 {
            address 172.16.11.254/24
        }
        vif 600 {
            address 172.16.30.254/24
        }
        vif 601 {
            address 172.16.31.254/24
        }
    }
    loopback lo {
    }
}
nat {
    source {
        rule 100 {
            outbound-interface eth0
            source {
                address 172.16.10.0/24
            }
            translation {
                address masquerade
            }
        }
        rule 101 {
            outbound-interface eth0
            source {
                address 172.16.11.0/24
            }
            translation {
                address masquerade
            }
        }
        rule 102 {
            outbound-interface eth0
            source {
                address 172.16.30.0/24
            }
            translation {
                address masquerade
            }
        }
        rule 103 {
            outbound-interface eth0
            source {
                address 172.16.31.0/24
            }
            translation {
                address masquerade
            }
        }
    }
}
system {
    config-management {
        commit-revisions 100
    }
    console {
        device ttyS0 {
            speed 115200
        }
    }
    host-name vyos
    login {
        user vyos {
            authentication {
                encrypted-password $6$kfQZLmptKI$MVrD/LYub/Ei/Xa0Wsw4W7faWSSTDrC/S7WuTc/3dMjyjDWtWx3iU9T88LiK.QyLBgShO.LJy6RHGU8DDLU2E.
                plaintext-password ""
            }
        }
    }
    ntp {
        server 0.pool.ntp.org {
        }
        server 1.pool.ntp.org {
        }
        server 2.pool.ntp.org {
        }
    }
    syslog {
        global {
            facility all {
                level info
            }
            facility protocols {
                level debug
            }
        }
    }
}


/* Warning: Do not remove the following line. */
/* === vyatta-config-version: "broadcast-relay@1:cluster@1:config-management@1:conntrack@1:conntrack-sync@1:dhcp-relay@2:dhcp-server@5:dns-forwarding@2:firewall@5:interfaces@5:ipsec@5:l2tp@2:lldp@1:mdns@1:nat@4:ntp@1:pptp@1:qos@1:quagga@5:snmp@1:ssh@1:system@16:vrrp@2:vyos-accel-ppp@2:wanloadbalance@3:webgui@1:webproxy@2:zone-policy@1" === */
/* Release version: 1.3-rolling-202003130217 */
