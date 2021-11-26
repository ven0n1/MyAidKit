package com.example.myaidkit.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Medicine {
    @PrimaryKey
    @NonNull
    String link;
    String name;
    String form;
    String composition;
    String influence;
    String kinetics;
    String indication;
    String dosage;
    String side_effects;
    String contra;
    String special;
    String date;

    @Ignore
    public Medicine(String name, String link, String form) {
        this.name = name;
        this.link = link;
        this.form = form;
    }

    public Medicine(String name, String link, String form, String composition, String influence, String kinetics, String indication, String dosage, String side_effects, String contra, String special, String date) {
        this.name = name;
        this.link = link;
        this.form = form;
        this.composition = composition;
        this.influence = influence;
        this.kinetics = kinetics;
        this.indication = indication;
        this.dosage = dosage;
        this.side_effects = side_effects;
        this.contra = contra;
        this.special = special;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getInfluence() {
        return influence;
    }

    public void setInfluence(String influence) {
        this.influence = influence;
    }

    public String getKinetics() {
        return kinetics;
    }

    public void setKinetics(String kinetics) {
        this.kinetics = kinetics;
    }

    public String getIndication() {
        return indication;
    }

    public void setIndication(String indication) {
        this.indication = indication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getSide_effects() {
        return side_effects;
    }

    public void setSide_effects(String side_effects) {
        this.side_effects = side_effects;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
