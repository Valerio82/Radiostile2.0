package com.radiostile.arcaik.utility;

/**
 * Created by arcaik on 12/12/2014.
 */
public class MetadataString {
    private String metadataString=null;
    private String nomeArtista=null;
    private String nomeCanzone=null;

    public void setString(String metadataString){
        this.metadataString=metadataString;
    }
    public String getNomeArtista(){
        if(metadataString!=null) {
            int indice = metadataString.indexOf("-");
            nomeArtista = metadataString.substring(0, indice);
            return nomeArtista;
        }else return null;
    }
    public String getNomeCanzone(){
        if(metadataString!=null) {
            int indice = metadataString.indexOf("-");
            nomeCanzone = metadataString.substring(indice + 2, metadataString.length());
            return nomeCanzone;
        }else
            return null;
    }

}
