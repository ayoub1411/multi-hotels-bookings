package com.booking.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Entity
public class Hotel {
@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
Long id;

@NotBlank(message = "Le nom ne doit pas etre vide")
String name;

@Max(value = 5,message = "Nombre d'etoiles d'hotel ne doit pas depasser 5")
@Min(value = 1,message = "Nombre d'etoiles d'hotel ne doit pas etre inferieur a 1")
int stars ;

@NotBlank(message = "L'address ne doit pas etre vide")
String address;
@OneToMany(mappedBy = "hotel",cascade = CascadeType.REMOVE)

List<Room> rooms ;

@ManyToOne
City city;

String imageName;//chemain

@Transient
MultipartFile image;


private int numberOfLike;

    public int getNumberOfLike() {
        return numberOfLike;
    }

    public void setNumberOfLike(int numberOfLike) {
        this.numberOfLike = numberOfLike;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getAddress() {
        return address;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}


