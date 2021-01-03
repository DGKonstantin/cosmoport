package com.space.model;

import javax.persistence.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

@Entity
@Table(name = "ship")
public class Ship {

    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "planet")
    private String planet;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipType")
    private ShipType shipType;

    @Column(name = "isUsed")
    private Boolean isUsed;

    @Column(name = "prodDate")
    private Date prodDate;

    @Column(name = "speed")
    private Double speed;

    @Column(name = "crewSize")
    private Integer crewSize;

    @Column(name = "rating")
    private Double rating;

    public Ship(Long id, String name, String planet, ShipType shipType, Boolean isUsed, Date prodDate, Double speed, Integer crewSize) {
        this.id = id;
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.isUsed = isUsed;
        this.prodDate = prodDate;
        this.speed = speed;
        this.crewSize = crewSize;
        this.rating = calculateRating();
    }

    public Ship(Long id, String name, String planet, ShipType shipType, Boolean isUsed, Date prodDate, Double speed, Integer crewSize, Double rating) {
        this.id = id;
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.isUsed = isUsed;
        this.prodDate = prodDate;
        this.speed = speed;
        this.crewSize = crewSize;
        this.rating = rating;
    }

    public Ship() {

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

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        if (used == null) isUsed = false;
        isUsed = used;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return this.rating;
    }

    public String getprodDateFormatter(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(this.prodDate);
    }

    public void setRating(Double rating){
        this.rating = rating;
    }

    public Double calculateRating(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(prodDate.getTime());
        Double k = isUsed ? 0.5 : 1;
        Double rating = (80 * speed * k) / (3019 - calendar.get(Calendar.YEAR) + 1);
        return Math.round(rating * 100.0) / 100.0;
    }

    @Override
    public String toString(){
        return "id = " + id + ", " +
                "name = " + name + ", " +
                "planet = " + planet + ", " +
                "shipType = " + shipType.name() + ", " +
                "isUsed = " + isUsed + ", " +
                "prodDate = " + prodDate + ", " +
                "speed = " + speed + ", " +
                "crewSize = " + crewSize + ", " +
                "rating = " + rating;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, planet, shipType, prodDate, isUsed, speed, crewSize, rating);
    }

}
