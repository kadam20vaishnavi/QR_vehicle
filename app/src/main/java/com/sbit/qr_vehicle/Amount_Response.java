package com.sbit.qr_vehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Amount_Response {
    String id,cardcount,ratepercard,response,cid,notpaid,paid;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardcount() {
        return cardcount;
    }

    public void setCardcount(String cardcount) {
        this.cardcount = cardcount;
    }

    public String getRatepercard() {
        return ratepercard;
    }

    public void setRatepercard(String ratepercard) {
        this.ratepercard = ratepercard;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getNotpaid() {
        return notpaid;
    }

    public void setNotpaid(String notpaid) {
        this.notpaid = notpaid;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }
}
