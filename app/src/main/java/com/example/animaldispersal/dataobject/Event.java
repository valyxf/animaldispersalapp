package com.example.animaldispersal.dataobject;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang.ObjectUtils;

/**
 * Created by user on 21/5/2016.
 */
public class Event implements Parcelable {


    private String animalId;
    private String eventId;
    private String eventType;
    private String eventDateTime;
    private String eventRemarks;
    private String recordType;
    private Boolean eventUpdated=false;
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
        out.writeString(eventId);
        out.writeString(eventType);
        out.writeString(eventDateTime);
        out.writeString(eventRemarks);
        out.writeString(recordType);
        out.writeByte((byte) (eventUpdated ? 1 : 0));
        out.writeString(lastUpdateUser);
        out.writeString(lastUpdateTimestamp);
        out.writeString(createUser);
        out.writeString(createTimestamp);
        out.writeString(sync);
        out.writeString(sync_message);


    }

    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    private Event(Parcel in) {
        animalId = in.readString();
        eventId = in.readString();
        eventType = in.readString();
        eventDateTime = in.readString();
        eventRemarks = in.readString();
        recordType = in.readString();
        eventUpdated = in.readByte() != 0;
        lastUpdateUser = in.readString();
        lastUpdateTimestamp = in.readString();
        createUser = in.readString();
        createTimestamp = in.readString();
        sync = in.readString();
        sync_message = in.readString();
    }

    public Event(
            String mEventId,
            String mEventType,
            String mEventDateTime,
            String mEventRemarks) {

        eventId = mEventId;
        eventType = mEventType;
        eventDateTime = mEventDateTime;
        eventRemarks = mEventRemarks;
        recordType = "N";
    }

    public Event(){

    }

    public String getAnimalId() { return animalId; }

    public void setAnimalId(String animalId) { this.animalId = animalId; }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getEventRemarks() {
        return eventRemarks;
    }

    public void setEventRemarks(String eventRemarks) {
        this.eventRemarks = eventRemarks;
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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getSync() {return sync;}

    public void setSync(String sync) {this.sync = sync;}

    public String getSync_message() {return sync_message;}

    public void setSync_message(String sync_message) {this.sync_message = sync_message;}

    public Boolean getEventUpdated() {return eventUpdated;}

    public void setEventUpdated(Boolean eventUpdated) {this.eventUpdated = eventUpdated;}

    @Override
    public String toString() {
        return eventId+"/"+eventType+"/"+eventDateTime+"/"+eventRemarks+"/"+recordType+"/"+eventUpdated.toString();
    }

    public boolean equals(Event event){
        if (event == null) return false;
        if (!ObjectUtils.equals(getEventId(), event.getEventId())) return false;
        if (!ObjectUtils.equals(getEventType(), event.getEventType())) return false;
        if (!ObjectUtils.equals(getEventDateTime(), event.getEventDateTime())) return false;
        if (!ObjectUtils.equals(getEventRemarks(), event.getEventRemarks())) return false;
        return true;
    }
}
