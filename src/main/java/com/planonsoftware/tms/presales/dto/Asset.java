package com.planonsoftware.tms.presales.dto;

@SuppressWarnings("PMD")
public class Asset {

	private int primaryKey;
    private Integer itemGroupRef;
    private Integer properytyRef;
    private boolean isSimple;
    private boolean isArchived;


   
    public Integer getItemGroupRef() {
        return itemGroupRef;
    }

    public void setItemGroupRef(Integer itemGroupRef) {
        this.itemGroupRef = itemGroupRef;
    }

    public Integer getProperytyRef() {
        return properytyRef;
    }

    public void setProperytyRef(Integer properytyRef) {
        this.properytyRef = properytyRef;
    }


    public boolean getIsArchived() {
        return isArchived;
    }
    
    public boolean getIsSimple() {
        return isSimple;
    }

    public void setIsSimple(boolean isSimple) {
        this.isSimple = isSimple;
    }
    
    public void setIsArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }
    
    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

}
