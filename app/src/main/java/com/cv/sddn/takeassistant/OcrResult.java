package com.cv.sddn.takeassistant;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by sddn on 2018/11/28.
 */

public class OcrResult extends LitePalSupport implements Serializable{

    private int id;
    private String phone_num;
    private Date call_time;

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id= id;
    }
    public String getPhone_num(){
        return  phone_num;
    }
    public void setPhone_num(String phone_num){
        this.phone_num = phone_num;
    }
    public Date getCall_time(){
        return  call_time;
    }
    public void setCall_time(Date call_time){
        this.call_time = call_time;
    }


}
