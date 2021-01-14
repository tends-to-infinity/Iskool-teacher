package com.iskool.iskool_t.Models;

public class AssignmentModel {

    ModelClass self;
    ModelClass topicReq;
    String link;

    public ModelClass getSelf() {
        return self;
    }

    public void setSelf(ModelClass self) {
        this.self = self;
    }

    public ModelClass getTopicReq() {
        return topicReq;
    }

    public void setTopicReq(ModelClass topicReq) {
        this.topicReq = topicReq;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public AssignmentModel() {
    }

    public AssignmentModel(ModelClass self, ModelClass topicReq, String link) {
        this.self = self;
        this.topicReq = topicReq;
        this.link = link;
    }
}
