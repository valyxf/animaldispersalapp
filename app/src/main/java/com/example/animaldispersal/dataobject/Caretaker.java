package com.example.animaldispersal.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang.ObjectUtils;

/**
 * Created by user on 30/8/2016.
 */
public class Caretaker implements Parcelable {

    private String animalId;
    private String caretakerId;
    private String caretakerUid;
    private String caretakerName;
    private String caretakerTel;
    private String caretakerAddr1;
    private String caretakerAddr2;
    private String caretakerAddr3;
    private String recordType;
    private String lastUpdateUser;
    private String lastUpdateTimestamp;
    private String createUser;
    private String createTimestamp;
    private String sync;
    private String sync_message;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(animalId);
        out.writeString(caretakerId);
        out.writeString(caretakerUid);
        out.writeString(caretakerName);
        out.writeString(caretakerTel);
        out.writeString(caretakerAddr1);
        out.writeString(caretakerAddr2);
        out.writeString(caretakerAddr3);
        out.writeString(recordType);
        out.writeString(lastUpdateUser);
        out.writeString(lastUpdateTimestamp);
        out.writeString(createUser);
        out.writeString(createTimestamp);
        out.writeString(sync);
        out.writeString(sync_message);
    }

    public static final Parcelable.Creator<Caretaker> CREATOR
            = new Parcelable.Creator<Caretaker>() {
        public Caretaker createFromParcel(Parcel in) {
            return new Caretaker(in);
        }

        public Caretaker[] newArray(int size) {
            return new Caretaker[size];
        }
    };

    private Caretaker(Parcel in) {
        animalId = in.readString();
        caretakerId = in.readString();
        caretakerUid = in.readString();
        caretakerName = in.readString();
        caretakerTel = in.readString();
        caretakerAddr1 = in.readString();
        caretakerAddr2 = in.readString();
        caretakerAddr3 = in.readString();
        recordType = in.readString();
        lastUpdateUser = in.readString();
        lastUpdateTimestamp = in.readString();
        createUser = in.readString();
        createTimestamp = in.readString();
        sync = in.readString();
        sync_message = in.readString();
    }

    public Caretaker(){}

    public Caretaker( String mAnimalId,
                      String mCaretakerId,
                      String mCaretakerUid,
                      String mCaretakerName,
                      String mCaretakerTel,
                      String mCaretakerAddr1,
                      String mCaretakerAddr2,
                      String mCaretakerAddr3,
                      String mRecordType){
        animalId = mAnimalId;
        caretakerId = mCaretakerId;
        caretakerUid = mCaretakerUid;
        caretakerName = mCaretakerName;
        caretakerTel = mCaretakerTel;
        caretakerAddr1 = mCaretakerAddr1;
        caretakerAddr2 = mCaretakerAddr2;
        caretakerAddr3 = mCaretakerAddr3;
        recordType = mRecordType;
    }

    public String getCaretakerUid() {        return caretakerUid;    }

    public void setCaretakerUid(String caretakerUid) {        this.caretakerUid = caretakerUid;}

    public String getCaretakerId() {
        return caretakerId;
    }

    public void setCaretakerId(String caretakerId) {
        this.caretakerId = caretakerId;
    }

    public String getCaretakerName() {
        return caretakerName;
    }

    public void setCaretakerName(String caretakerName) {
        this.caretakerName = caretakerName;
    }

    public String getCaretakerTel() {
        return caretakerTel;
    }

    public void setCaretakerTel(String caretakerTel) {
        this.caretakerTel = caretakerTel;
    }

    public String getCaretakerAddr1() {
        return caretakerAddr1;
    }

    public void setCaretakerAddr1(String caretakerAddr1) {
        this.caretakerAddr1 = caretakerAddr1;
    }

    public String getCaretakerAddr2() {
        return caretakerAddr2;
    }

    public void setCaretakerAddr2(String caretakerAddr2) {
        this.caretakerAddr2 = caretakerAddr2;
    }

    public String getCaretakerAddr3() {
        return caretakerAddr3;
    }

    public void setCaretakerAddr3(String caretakerAddr3) {
        this.caretakerAddr3 = caretakerAddr3;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public String getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(String lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getSync() {return sync;}

    public void setSync(String sync) {
        this.sync = sync;
    }

    public String getSync_message() {
        return sync_message;
    }

    public void setSync_message(String sync_message) {
        this.sync_message = sync_message;
    }

    public String getAnimalId() {return animalId;}

    public void setAnimalId(String animalId) {this.animalId = animalId;}

    @Override
    public String toString() {
        return "Caretaker{" +
                "animalId='" + animalId + '\'' +
                ", caretakerId='" + caretakerId + '\'' +
                ", caretakerName='" + caretakerName + '\'' +
                ", caretakerTel='" + caretakerTel + '\'' +
                ", caretakerAddr1='" + caretakerAddr1 + '\'' +
                ", caretakerAddr2='" + caretakerAddr2 + '\'' +
                ", caretakerAddr3='" + caretakerAddr3 + '\'' +
                ", recordType='" + recordType + '\'' +
                ", sync='" + sync + '\'' +
                ", sync_message='" + sync_message + '\'' +
                '}';
    }

    public boolean equals(Caretaker caretaker){
        if(getCaretakerId()==null && caretaker==null)
            return true;
        if(caretaker==null)
            return false;
        if (!ObjectUtils.equals(getAnimalId(),caretaker.getAnimalId()))
            return false;
        if (!ObjectUtils.equals(getCaretakerId(),caretaker.getCaretakerId()))
            return false;
        if (!ObjectUtils.equals(getCaretakerName(),caretaker.getCaretakerName()))
            return false;
        if (!ObjectUtils.equals(getCaretakerTel(),caretaker.getCaretakerTel()))
            return false;
        if (!ObjectUtils.equals(getCaretakerAddr1(),caretaker.getCaretakerAddr1()))
            return false;
        if (!ObjectUtils.equals(getCaretakerAddr2(),caretaker.getCaretakerAddr2()))
            return false;
        if (!ObjectUtils.equals(getCaretakerAddr3(),caretaker.getCaretakerAddr3()))
            return false;
        return true;
    }
}
