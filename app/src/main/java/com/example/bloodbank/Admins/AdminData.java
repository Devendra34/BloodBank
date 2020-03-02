package com.example.bloodbank.Admins;

public class AdminData {
    public String instituteName;
    public String instituteEmailId;
    public String country;
    public String state;
    public String city;
    public String id;
    public int o_plus = 0;
    public int o_minus = 0;
    public int a_plus = 0;
    public int a_minus = 0;
    public int b_plus = 0;
    public int b_minus = 0;
    public int ab_plus = 0;
    public int ab_minus = 0;

    public AdminData() {
        // Required for firebase
    }

    public AdminData(String instituteName, String instituteEmailId, String country, String state, String city, String id) {
        this.instituteName = instituteName;
        this.instituteEmailId = instituteEmailId;
        this.country = country;
        this.state = state;
        this.city = city;
        this.id = id;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getInstituteEmailId() {
        return instituteEmailId;
    }

    public void setInstituteEmailId(String instituteEmailId) {
        this.instituteEmailId = instituteEmailId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getO_plus() {
        return o_plus;
    }

    public void setO_plus(int o_plus) {
        this.o_plus = o_plus;
    }

    public int getO_minus() {
        return o_minus;
    }

    public void setO_minus(int o_minus) {
        this.o_minus = o_minus;
    }

    public int getA_plus() {
        return a_plus;
    }

    public void setA_plus(int a_plus) {
        this.a_plus = a_plus;
    }

    public int getA_minus() {
        return a_minus;
    }

    public void setA_minus(int a_minus) {
        this.a_minus = a_minus;
    }

    public int getB_plus() {
        return b_plus;
    }

    public void setB_plus(int b_plus) {
        this.b_plus = b_plus;
    }

    public int getB_minus() {
        return b_minus;
    }

    public void setB_minus(int b_minus) {
        this.b_minus = b_minus;
    }

    public int getAb_plus() {
        return ab_plus;
    }

    public void setAb_plus(int ab_plus) {
        this.ab_plus = ab_plus;
    }

    public int getAb_minus() {
        return ab_minus;
    }

    public void setAb_minus(int ab_minus) {
        this.ab_minus = ab_minus;
    }
}
