package com.dto;

import java.util.List;

public class WifiDataResponse {
    private TbPublicWifiInfo TbPublicWifiInfo;

    public TbPublicWifiInfo getTbPublicWifiInfo() {
        return TbPublicWifiInfo;
    }

    public void setTbPublicWifiInfo(TbPublicWifiInfo TbPublicWifiInfo) {
        this.TbPublicWifiInfo = TbPublicWifiInfo;
    }
    
    public List<WifiData> getWifiDataList() {
        if (TbPublicWifiInfo != null) {
            return TbPublicWifiInfo.getRow(); 
        } else {
            return null; 
        }
    }
}
