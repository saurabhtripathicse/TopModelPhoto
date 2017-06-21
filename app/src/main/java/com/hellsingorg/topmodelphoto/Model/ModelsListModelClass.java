package com.hellsingorg.topmodelphoto.Model;

/**
 * Created by Saurabh on 20-03-2017.
 */

public class ModelsListModelClass {

    private String id;
    private String modelImageURL;
    private String dob;
    private String modelName;
    private String country_image;

    public String getCountry_image() {
        return country_image;
    }

    public void setCountry_image(String country_image) {
        this.country_image = country_image;
    }

    public String getModelCountry() {
        return modelCountry;
    }

    public void setModelCountry(String modelCountry) {
        this.modelCountry = modelCountry;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getModelImageURL() {
        return modelImageURL;
    }

    public void setModelImageURL(String modelImageURL) {
        this.modelImageURL = modelImageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String modelCountry;

}
