package com.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddressDto {


    @NotBlank(message = "Adresse 1 est requise")
    @NotNull(message = "Send address field")
    private String adresse1;

    private String adresse2; // Adresse 2 est optionnelle

    @NotBlank(message = "Ville est requise")
    @NotNull(message = "Envoyer la ville")
    private String ville;

    @NotBlank(message = "Code postal est requis")
    @NotNull(message = "Send postal code")
    private String codePostale;

    @NotBlank(message = "Pays est requis")
    @NotNull(message = "Send your country")
    private String pays;

    public String getAdresse1() {
        return adresse1;
    }

    public void setAdresse1(String adresse1) {
        this.adresse1 = adresse1;
    }

    public String getAdresse2() {
        return adresse2;
    }

    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostale() {
        return codePostale;
    }

    public void setCodePostale(String codePostale) {
        this.codePostale = codePostale;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }
}
