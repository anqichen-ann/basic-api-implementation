package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;

public class User {
    @NotNull
    @Size(max = 8)
    private String name;
    @NotNull
    private String gender;
    @NotNull
    @Max(100)
    @Min(18)
    private int age;
    @Email
    private String email;
    @Pattern(regexp = "1\\d{10}")
    private String phone;
    private int voteNum = 10;
    public User(String name, String gender, int age, String email, String phone) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    public User() {
    }
    @JsonProperty("user_name")
    public String getName() {

        return name;
    }
    @JsonProperty("name")
    public void setName(String name) {

        this.name = name;
    }
    @JsonProperty("user_gender")
    public String getGender() {

        return gender;
    }
    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }
     @JsonProperty("user_age")
    public int getAge() {
        return age;
    }
    @JsonProperty("age")
    public void setAge(int age) {
        this.age = age;
    }
     @JsonProperty("user_email")
    public String getEmail() {
        return email;
    }
    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }
     @JsonProperty("user_phone")
    public String getPhone() {
        return phone;
    }
    @JsonProperty("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }


}
