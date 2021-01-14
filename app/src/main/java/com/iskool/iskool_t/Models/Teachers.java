package com.iskool.iskool_t.Models;

import java.util.ArrayList;

public class Teachers {


    ModelClass self;
    ArrayList<ModelClass> courses;

    public Teachers() {
    }

    public Teachers(ModelClass self, ArrayList<ModelClass> courses) {
        this.self = self;
        this.courses = courses;
    }

    public ModelClass getSelf() {
        return self;
    }

    public void setSelf(ModelClass self) {
        this.self = self;
    }

    public ArrayList<ModelClass> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<ModelClass> courses) {
        this.courses = courses;
    }
}
