package com.planonsoftware.tms.presales.dto;

public class Location {

    private String token;
    private String baseAssetRef;
    private String propertyRef;
    private String spaceRef;
    private int primaryKey;
    private String sysMutationDateTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBaseAssetRef() {
        return baseAssetRef;
    }

    public void setBaseAssetRef(String baseAssetRef) {
        this.baseAssetRef = baseAssetRef;
    }

    public String getPropertyRef() {
        return propertyRef;
    }

    public void setPropertyRef(String propertyRef) {
        this.propertyRef = propertyRef;
    }

    public String getSpaceRef() {
        return spaceRef;
    }

    public void setSpaceRef(String spaceRef) {
        this.spaceRef = spaceRef;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getSysMutationDateTime() {
        return sysMutationDateTime;
    }

    public void setSysMutationDateTime(String sysMutationDateTime) {
        this.sysMutationDateTime = sysMutationDateTime;
    }

}
