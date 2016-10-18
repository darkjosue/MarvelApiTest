package com.example.android.marvelapitest;

/**
 * Created by Usuario on 17/10/2016.
 */
public class Comic {

    private String mTitulo;
    private String mHeroe;

    public Comic(String titulo, String heroe){
        mTitulo = titulo;
        mHeroe = heroe;
    }

    public String getTitulo(){return mTitulo;}
    public String getHeroe() {return mHeroe;}

}
