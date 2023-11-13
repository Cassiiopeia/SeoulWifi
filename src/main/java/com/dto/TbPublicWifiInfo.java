package com.dto;

import java.util.List;

public class TbPublicWifiInfo {
    public int list_total_count;
    private Result RESULT;
    private List<WifiData> row;

    public int getList_total_count() {
        return list_total_count;
    }

    public void setList_total_count(int list_total_count) {
        this.list_total_count = list_total_count;
    }

    public Result getRESULT() {
        return RESULT;
    }

    public void setRESULT(Result RESULT) {
        this.RESULT = RESULT;
    }

    public List<WifiData> getRow() {
        return row;
    }

    public void setRow(List<WifiData> row) {
        this.row = row;
    }
    
   
    
}