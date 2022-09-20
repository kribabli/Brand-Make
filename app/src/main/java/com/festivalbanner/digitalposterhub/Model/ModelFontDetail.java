package com.festivalbanner.digitalposterhub.Model;

/**
 * Created by Nick Bapu on 22-08-2018.
 */

public class ModelFontDetail {
    /* String fontName;
     String fontPath;

     public ModelFontDetail(String fontName, String fontPath) {
         this.fontName = fontName;
         this.fontPath = fontPath;
     }

     public String getFontName() {
         return fontName;
     }

     public void setFontName(String fontName) {
         this.fontName = fontName;
     }

     public String getFontPath() {
         return fontPath;
     }

     public void setFontPath(String fontPath) {
         this.fontPath = fontPath;
     }*/
    String text;
    String FontName;

    public ModelFontDetail(String text, String fontName) {
        this.text = text;
        FontName = fontName;
    }
    public void setText(String text) {
        this.text = text;
    }

    public void setFontName(String fontName) {
        FontName = fontName;
    }

    public String getFontName() {
        return FontName;
    }

    public String getText() {
        return text;
    }
}
