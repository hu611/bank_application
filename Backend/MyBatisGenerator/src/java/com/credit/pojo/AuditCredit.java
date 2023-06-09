package com.credit.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author weiyanhu
 * @since 2023-04-25
 */
@TableName("audit_credit")
@ApiModel(value = "AuditCredit对象", description = "")
public class AuditCredit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "audit_credit_id", type = IdType.AUTO)
    private Integer auditCreditId;

    private String prcId;

    private String picFolderLoc;

    public Integer getAuditCreditId() {
        return auditCreditId;
    }

    public void setAuditCreditId(Integer auditCreditId) {
        this.auditCreditId = auditCreditId;
    }
    public String getPrcId() {
        return prcId;
    }

    public void setPrcId(String prcId) {
        this.prcId = prcId;
    }
    public String getPicFolderLoc() {
        return picFolderLoc;
    }

    public void setPicFolderLoc(String picFolderLoc) {
        this.picFolderLoc = picFolderLoc;
    }

    @Override
    public String toString() {
        return "AuditCredit{" +
            "auditCreditId=" + auditCreditId +
            ", prcId=" + prcId +
            ", picFolderLoc=" + picFolderLoc +
        "}";
    }
}
