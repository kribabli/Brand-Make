package com.festivalbanner.digitalposterhub.Model;

public class ModelHomeChild {

    String childitemtittle;
    int childitemimage;

    public ModelHomeChild(String childitemtittle, int childitemimage) {
        this.childitemtittle = childitemtittle;
        this.childitemimage = childitemimage;
    }

    public String getChilditemtittle() {
        return childitemtittle;
    }

    public void setChilditemtittle(String childitemtittle) {
        this.childitemtittle = childitemtittle;
    }

    public int getChilditemimage() {
        return childitemimage;
    }

    public void setChilditemimage(int childitemimage) {
        this.childitemimage = childitemimage;
    }
}
