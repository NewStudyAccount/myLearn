package com.example;

public class AttrInfoVo {

    private String attrCode;
    private String attrName;
    private String targetAttrCode;

    public AttrInfoVo() {
    }

    public AttrInfoVo(String attrCode, String attrName, String targetAttrCode) {
        this.attrCode = attrCode;
        this.attrName = attrName;
        this.targetAttrCode = targetAttrCode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String attrCode;
        private String attrName;
        private String targetAttrCode;

        public Builder attrCode(String attrCode) {
            this.attrCode = attrCode;
            return this;
        }

        public Builder attrName(String attrName) {
            this.attrName = attrName;
            return this;
        }

        public Builder targetAttrCode(String targetAttrCode) {
            this.targetAttrCode = targetAttrCode;
            return this;
        }

        public AttrInfoVo build() {
            return new AttrInfoVo(attrCode, attrName, targetAttrCode);
        }
    }

    public String getAttrCode() {
        return attrCode;
    }

    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getTargetAttrCode() {
        return targetAttrCode;
    }

    public void setTargetAttrCode(String targetAttrCode) {
        this.targetAttrCode = targetAttrCode;
    }
}
