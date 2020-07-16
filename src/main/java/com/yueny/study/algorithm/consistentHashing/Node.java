package com.yueny.study.algorithm.consistentHashing;


import java.io.Serializable;

public class Node implements Serializable {


    private static final long serialVersionUID = 6402176917125146119L;


    public Node(String path, RoleType role) {
        this.path = path;
        this.role = role;
    }

    private String path;

    private String ip_port;

    private RoleType role;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Node) {
            return this.toString().equals(obj.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return "path="+path+",role="+role+",ip_port="+ip_port;
    }

    public String getIp_port() {
        return ip_port;
    }

    public void setIp_port(String ip_port) {
        this.ip_port = ip_port;
    }
}
