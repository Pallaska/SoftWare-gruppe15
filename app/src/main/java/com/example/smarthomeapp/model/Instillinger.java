package com.example.smarthomeapp.model;

public class Instillinger {
    String style;
    String language;
    String varsler;

    public Instillinger(String style, String language, String varsler) {
        this.style = style;
        this.language = language;
        this.varsler = varsler;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVarsler() {
        return varsler;
    }

    public void setVarsler(String varsler) {
        this.varsler = varsler;
    }

    @Override
    public String toString() {
        return "instillinger{" + "style=" + style + ", language='" + language + '\'' + ", varsler=" + varsler + '}';
    }
}


