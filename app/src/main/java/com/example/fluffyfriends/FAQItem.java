package com.example.fluffyfriends;

public class FAQItem {

    public String pregunta;
    public String respuesta;
    public boolean expanded;

    public FAQItem(String pregunta, String respuesta) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.expanded = false;
    }

    public String getPregunta() {

        return pregunta;
    }

    public void setPregunta(String pregunta) {

        this.pregunta = pregunta;
    }

    public String getRespuesta() {

        return respuesta;
    }

    public void setRespuesta(String respuesta) {

        this.respuesta = respuesta;
    }

    public boolean isExpanded() {

        return expanded;
    }

    public void setExpanded(boolean expanded) {

        this.expanded = expanded;
    }
}
