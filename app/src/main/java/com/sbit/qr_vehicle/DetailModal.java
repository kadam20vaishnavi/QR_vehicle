package com.sbit.qr_vehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailModal {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("vehicle_number")
    @Expose
    private String vehicleNumber;
    @SerializedName("driving_licence_exp_date")
    @Expose
    private String drivingLicenceExpDate;
    @SerializedName("insurance_exp_date")
    @Expose
    private String insuranceExpDate;
    @SerializedName("insurance_copy")
    @Expose
    private String insurancecopy;
    @SerializedName("puc_exp_date")
    @Expose
    private String pucExpDate;
    @SerializedName("puc_copy")
    @Expose
    private String puccopy;
    @SerializedName("vehical_reg_date")
    @Expose
    private String vehicalRegDate;
    @SerializedName("qrcode")
    @Expose
    private String qrcode;
    @SerializedName("vehicle_copy")
    @Expose
    private String vehicalCopy;
    @SerializedName("driving_licence_copy")
    @Expose
    private String drivingLicenceCopy;
    @SerializedName("vehical_reg_copy")
    @Expose
    private String Vehicleregcopy;
    @SerializedName("response")
    @Expose
    private String response;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getInsurancecopy() {
        return insurancecopy;
    }

    public void setInsurancecopy(String insurancecopy) {
        this.insurancecopy = insurancecopy;
    }

    public String getPuccopy() {
        return puccopy;
    }

    public void setPuccopy(String puccopy) {
        this.puccopy = puccopy;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getDrivingLicenceExpDate() {
        return drivingLicenceExpDate;
    }

    public void setDrivingLicenceExpDate(String drivingLicenceExpDate) {
        this.drivingLicenceExpDate = drivingLicenceExpDate;
    }

    public String getInsuranceExpDate() {
        return insuranceExpDate;
    }

    public void setInsuranceExpDate(String insuranceExpDate) {
        this.insuranceExpDate = insuranceExpDate;
    }

    public String getPucExpDate() {
        return pucExpDate;
    }

    public void setPucExpDate(String pucExpDate) {
        this.pucExpDate = pucExpDate;
    }

    public String getVehicalRegDate() {
        return vehicalRegDate;
    }

    public void setVehicalRegDate(String vehicalRegDate) {
        this.vehicalRegDate = vehicalRegDate;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getVehicalCopy() {
        return vehicalCopy;
    }

    public void setVehicalCopy(String vehicalCopy) {
        this.vehicalCopy = vehicalCopy;
    }

    public String getDrivingLicenceCopy() {
        return drivingLicenceCopy;
    }

    public void setDrivingLicenceCopy(String drivingLicenceCopy) {
        this.drivingLicenceCopy = drivingLicenceCopy;
    }

    public String getVehicleregcopy() {
        return Vehicleregcopy;
    }

    public void setVehicleregcopy(String vehicleregcopy) {
        this.Vehicleregcopy = vehicleregcopy;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
