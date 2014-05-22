/**
 * RequestRouter.java
 *
 * Utility class used to determine a unique server node based on some key hashcode.
 *
 * Copyright(c) 2014 Equinix, Inc.  All Rights Reserved.
 * This software is the proprietary information of Equinix.
 *
 */
package common.util;

import java.util.List;

import play.Logger;

import common.exception.BaseException;

public class RequestRouter {

    private String[]               clusters;

    private int                    keyHashCode;

    public static final AppConfig CONFIGLOADER = AppConfig.getInstance();

    public RequestRouter(String clusterName, int keyHashCode) throws BaseException {
        Logger.debug("CLUSTER PARAM NAME  : "+clusterName);
        Logger.debug("HASH Code  : "+keyHashCode);
        List<String> servers = CONFIGLOADER.getArray(clusterName);
        if(servers==null) {
            throw new BaseException("No such configuration :"+clusterName, "");
        }
        this.clusters = servers.toArray(new String[servers.size()]);
        this.keyHashCode = keyHashCode;
    }

    /**
     * Determines the end point based on the hashcode and number of servers.
     * 
     * @return
     */
    public String getEndPoint() {
        int hash = Math.abs(keyHashCode);
        int serverIndex = hash % clusters.length;
        return clusters[serverIndex];
    }

    /**
     * @return the clusters
     */
    public String[] getClusters() {
        return clusters;
    }

    /**
     * @param clusters
     *            the clusters to set
     */
    public void setClusters(String[] clusters) {
        this.clusters = clusters;
    }

    /**
     * @return the keyHashCode
     */
    public int getKeyHashCode() {
        return keyHashCode;
    }

    /**
     * @param keyHashCode
     *            the keyHashCode to set
     */
    public void setKeyHashCode(int keyHashCode) {
        this.keyHashCode = keyHashCode;
    }

}
