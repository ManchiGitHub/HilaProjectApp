package com.example.parkinson.model.general_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.parkinson.features.metrics.add_new_medicine.single_metric.SingleMetric;

import java.util.List;

public class Medicine implements Parcelable {
    private String id;
//    private String categoryId;
    private String name;
    private String value;
    List<SingleMetric> valueList;

    public Medicine() {
    }

    public Medicine(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Medicine(String id, String name, List<SingleMetric> valueList) {
        this.id = id;
        this.name = name;
        this.valueList = valueList;
    }

    protected Medicine(Parcel in) {
        id = in.readString();
        name = in.readString();
        value = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }




    public List<SingleMetric> getValueList() {
        return valueList;
    }

    public void setValueList(List<SingleMetric> valueList) {
        this.valueList = valueList;
    }
}
