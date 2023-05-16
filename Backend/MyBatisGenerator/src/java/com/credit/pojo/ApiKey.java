package com.credit.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author weiyanhu
 * @since 2023-05-16
 */
@TableName("api_key")
@ApiModel(value = "ApiKey对象", description = "")
public class ApiKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String prcId;

    private String apiKey;

    private String companyName;

    private String companyDesc;

    private LocalDate lastUpdate;

    public String getPrcId() {
        return prcId;
    }

    public void setPrcId(String prcId) {
        this.prcId = prcId;
    }
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getCompanyDesc() {
        return companyDesc;
    }

    public void setCompanyDesc(String companyDesc) {
        this.companyDesc = companyDesc;
    }
    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "ApiKey{" +
            "prcId=" + prcId +
            ", apiKey=" + apiKey +
            ", companyName=" + companyName +
            ", companyDesc=" + companyDesc +
            ", lastUpdate=" + lastUpdate +
        "}";
    }
}
