package com.example.animaldispersal.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang.ObjectUtils;

/**
 * Created by user on 21/5/2016.
 */
public class Animal implements Parcelable {

    private String animalId;
    private String supervisor;
    private String animalType;
    private String gender;
    private String dateOfBirth;
    private String country;
    private String datePurchased;
    private String purchasePrice;
    private String dateDistributed;
    private String dateSold;
    private String salePrice;
    private String recordType;
    private String lastUpdateUser;
    private String lastUpdateTimestamp;
    private String createUser;
    private String createTimestamp;
    private String caretakerUid;
    private String sync;
    private String sync_message;
    private String nfcScanEntryTimestamp;
    private String nfcScanSaveTimestamp;


    //private List<Event> events;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(animalId);
        out.writeString(supervisor);
        out.writeString(animalType);
        out.writeString(gender);
        out.writeString(dateOfBirth);
        out.writeString(country);
        out.writeString(datePurchased);
        out.writeString(purchasePrice);
        out.writeString(dateDistributed);
        out.writeString(dateSold);
        out.writeString(salePrice);
        out.writeString(caretakerUid);
        out.writeString(recordType);
        out.writeString(lastUpdateUser);
        out.writeString(lastUpdateTimestamp);
        out.writeString(createUser);
        out.writeString(createTimestamp);
        out.writeString(sync);
        out.writeString(sync_message);
        out.writeString(nfcScanEntryTimestamp);
        out.writeString(nfcScanSaveTimestamp);


    }

    public static final Parcelable.Creator<Animal> CREATOR
            = new Parcelable.Creator<Animal>() {
        public Animal createFromParcel(Parcel in) {
            return new Animal(in);
        }

        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };

    private Animal(Parcel in) {
        animalId = in.readString();
        supervisor = in.readString();
        animalType = in.readString();
        gender = in.readString();
        dateOfBirth = in.readString();
        country = in.readString();
        datePurchased = in.readString();
        purchasePrice = in.readString();
        dateDistributed = in.readString();
        dateSold = in.readString();
        salePrice  = in.readString();
        caretakerUid = in.readString();
        recordType = in.readString();
        lastUpdateUser = in.readString();
        lastUpdateTimestamp = in.readString();
        createUser = in.readString();
        createTimestamp = in.readString();
        sync = in.readString();
        sync_message  = in.readString();
        nfcScanEntryTimestamp  = in.readString();
        nfcScanSaveTimestamp  = in.readString();
    }

    public Animal(){

    }

    public Animal(String mAnimalId,
                  String mSupervisor,
                  String mAnimalType,
                  String mGender,
                  String mDateOfBirth,
                  String mCountry,
                  String mDatePurchased,
                  String mPurchasePrice,
                  String mDateDistributed,
                  String mDateSold,
                  String mSalePrice,
                  String mCaretakerUid,
                  String mRecordType,
                  String mNfcScanEntryTimestamp,
                  String mNfcScanSaveTimestamp){
        animalId = mAnimalId;
        supervisor = mSupervisor;
        animalType = mAnimalType;
        gender = mGender;
        dateOfBirth = mDateOfBirth;
        country = mCountry;
        datePurchased = mDatePurchased;
        purchasePrice = mPurchasePrice;
        dateDistributed = mDateDistributed;
        dateSold = mDateSold;
        salePrice  = mSalePrice;
        caretakerUid = mCaretakerUid;
        recordType = mRecordType;
        nfcScanEntryTimestamp = mNfcScanEntryTimestamp;
        nfcScanSaveTimestamp = mNfcScanSaveTimestamp;

    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(String datePurchased) {
        this.datePurchased = datePurchased;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getDateDistributed() {
        return dateDistributed;
    }

    public void setDateDistributed(String dateDistributed) {
        this.dateDistributed = dateDistributed;
    }

    public String getDateSold() {
        return dateSold;
    }

    public void setDateSold(String dateSold) {
        this.dateSold = dateSold;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
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

    public void setLastUpdateTimestamp(String lastUpdateTimestamp) { this.lastUpdateTimestamp = lastUpdateTimestamp;}

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(String createTimestamp) { this.createTimestamp = createTimestamp;}

    public String getCaretakerUid() {
        return caretakerUid;
    }

    public void setCaretakerUid(String caretakerUid) { this.caretakerUid = caretakerUid;}

    public String getSync() {return sync;    }

    public void setSync(String sync) {this.sync = sync;    }

    public String getSync_message() {return sync_message;}

    public void setSync_message(String sync_message) {this.sync_message = sync_message;}

    public String getNfcScanEntryTimestamp() {return nfcScanEntryTimestamp;}

    public void setNfcScanEntryTimestamp(String nfcScanEntryTimestamp) {this.nfcScanEntryTimestamp = nfcScanEntryTimestamp;}

    public String getNfcScanSaveTimestamp() {return nfcScanSaveTimestamp; }

    public void setNfcScanSaveTimestamp(String nfcScanSaveTimestamp) {this.nfcScanSaveTimestamp = nfcScanSaveTimestamp;}

    @Override
    public String toString() {
        return "Animal{" +
                "animalId='" + animalId + '\'' +
                ", supervisor='" + supervisor + '\'' +
                ", animalType='" + animalType + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", country='" + country + '\'' +
                ", datePurchased='" + datePurchased + '\'' +
                ", purchasePrice='" + purchasePrice + '\'' +
                ", dateDistributed='" + dateDistributed + '\'' +
                ", dateSold='" + dateSold + '\'' +
                ", salePrice='" + salePrice + '\'' +
                ", recordType='" + recordType + '\'' +
                ", lastUpdateUser='" + lastUpdateUser + '\'' +
                ", lastUpdateTimestamp='" + lastUpdateTimestamp + '\'' +
                ", createUser='" + createUser + '\'' +
                ", createTimestamp='" + createTimestamp + '\'' +
                ", caretakerId='" + caretakerUid + '\'' +
                ", sync='" + sync + '\'' +
                ", sync_message='" + sync_message + '\'' +
                ", nfcScanEntryTimestamp='" + nfcScanEntryTimestamp + '\'' +
                ", nfcScanSaveTimestamp='" + nfcScanSaveTimestamp + '\'' +
                '}';
    }

    public boolean equals(Animal animal){
        if (animal == null) return false;
        if (!ObjectUtils.equals(getAnimalId(),animal.getAnimalId()))
            return false;
        if (!ObjectUtils.equals(getSupervisor(),animal.getSupervisor()))
            return false;
        if (!ObjectUtils.equals(getAnimalType(),animal.getAnimalType()))
            return false;
        if (!ObjectUtils.equals(getGender(),animal.getGender()))
            return false;
        if (!ObjectUtils.equals(getDateOfBirth(),animal.getDateOfBirth()))
            return false;
        if (!ObjectUtils.equals(getCountry(),animal.getCountry()))
            return false;
        if (!ObjectUtils.equals(getDatePurchased(),animal.getDatePurchased()))
            return false;
        if (!ObjectUtils.equals(getPurchasePrice(),animal.getPurchasePrice()))
            return false;
        if (!ObjectUtils.equals(getDateDistributed(),animal.getDateDistributed()))
            return false;
        if (!ObjectUtils.equals(getDateSold(),animal.getDateSold()))
            return false;
        if (!ObjectUtils.equals(getSalePrice(),animal.getSalePrice()))
            return false;
        if (!ObjectUtils.equals(getCaretakerUid(),animal.getCaretakerUid()))
            return false;
        return true;
    }
}


