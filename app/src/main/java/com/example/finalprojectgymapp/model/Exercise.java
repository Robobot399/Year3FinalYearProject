package com.example.finalprojectgymapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise_table")
public class Exercise implements Parcelable {

    private String bodyPart;
    private String equipment;
    private String gifUrl;
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String target;

    public Exercise(String bodyPart, String equipment, String gifUrl, @NonNull String id, String name, String target) {
        this.bodyPart = bodyPart;
        this.equipment = equipment;
        this.gifUrl = gifUrl;
        this.id = id;
        this.name = name;
        this.target = target;
    }

    protected Exercise(Parcel in) {
        bodyPart = in.readString();
        equipment = in.readString();
        gifUrl = in.readString();
        id = in.readString();
        name = in.readString();
        target = in.readString();
    }

    public String getBodyPart() {
        return bodyPart;
    }
    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public String getEquipment() {
        return equipment;
    }
    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getGifUrl() {
        return gifUrl;
    }
    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "bodyPart='" + bodyPart + '\'' +
                ", equipment='" + equipment + '\'' +
                ", gifUrl='" + gifUrl + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", target='" + target + '\'' +
                '}';
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(bodyPart);
        dest.writeString(equipment);
        dest.writeString(gifUrl);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(target);
    }
}
