package edu.planon.tms.rest.dto;

@SuppressWarnings("PMD")
public class Asset {

    private int Syscode;
    private String Code;
    private Integer ItemGroupRef;
    private Integer PropertyRef;
    private boolean IsSimple;
    private boolean IsArchived;

    public int getSyscode() {
        return Syscode;
    }

    public Integer getItemGroupRef() {
        return ItemGroupRef;
    }

    public void setItemGroupRef(Integer itemGroupRef) {
        ItemGroupRef = itemGroupRef;
    }

    public Integer getPropertyRef() {
        return PropertyRef;
    }

    public void setPropertyRef(Integer propertyRef) {
        PropertyRef = propertyRef;
    }

    public boolean getIsSimple() {
        return IsSimple;
    }

    public void setIsSimple(boolean isSimple) {
        IsSimple = isSimple;
    }

    public boolean getIsArchived() {
        return IsArchived;
    }

    public void setIsArchived(boolean isArchived) {
        IsArchived = isArchived;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
