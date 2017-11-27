package unal.edu.co.reto10;

import java.io.Serializable;

/**
 * Created by FABIAN on 13/11/2017.
 */

public class Contact implements Serializable {
    private String edad_a_os;
    private String pa_s;

    public Contact(String edad_a_os, String pa_s) {
        this.edad_a_os = edad_a_os;
        this.pa_s = pa_s;
    }

    public String getEdad_a_os() {
        return edad_a_os;
    }

    public String getPa_s() {
        return pa_s;
    }
}
