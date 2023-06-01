package com.main.neuronavigator.models;

import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
public class Patient implements Serializable {


    private ObjectId _id;
    private String name;
    private String lastName;
    private LocalDate birth;
    private Integer age;
    private String address;
    private String phone;
    private String phoneM;
    private String phoneD;
    private String occupation;
    private String momName;
    private String dadName;
    private String sender;
    private String reason;
    private List<LocalDate> evaluations=new ArrayList<LocalDate>();
    private String observations;


    public ObjectId get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneM() {
        return phoneM;
    }

    public void setPhoneM(String phoneM) {
        this.phoneM = phoneM;
    }

    public String getPhoneD() {
        return phoneD;
    }

    public void setPhoneD(String phoneD) {
        this.phoneD = phoneD;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getMomName() {
        return momName;
    }

    public void setMomName(String momName) {
        this.momName = momName;
    }

    public String getDadName() {
        return dadName;
    }

    public void setDadName(String dadName) {
        this.dadName = dadName;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<LocalDate> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<LocalDate> evaluations) {
        this.evaluations = evaluations;
    }

    public void addEvaluation(LocalDate evaluation) {
        this.evaluations.add(evaluation);
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birth=" + birth +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", phoneM='" + phoneM + '\'' +
                ", phoneD='" + phoneD + '\'' +
                ", occupation='" + occupation + '\'' +
                ", momName='" + momName + '\'' +
                ", dadName='" + dadName + '\'' +
                ", sender='" + sender + '\'' +
                ", reason='" + reason + '\'' +
                ", evaluations=" + evaluations +
                ", observations='" + observations + '\'' +
                '}';
    }
}
