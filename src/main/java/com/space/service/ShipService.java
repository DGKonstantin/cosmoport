package com.space.service;

import com.space.NotValidException;
import com.space.ShipNotFound;
import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;

import java.sql.SQLException;
import java.util.List;

public interface ShipService {
    ShipRepository repository = null;

    List<Ship> getAllShips();

    Ship createShip(Ship ship) throws NotValidException;
    Ship editShip(Ship ship, String sid) throws NotValidException, SQLException, ShipNotFound;


    List<Ship> getFilteredShips(ShipOrder order, Integer pageSize, Integer pageNumber,
                                String name, String planet,
                                ShipType shipType,
                                Long after, Long before,
                                Boolean isUsed,
                                Double minSpeed, Double maxSpeed,
                                Integer minCrewSize, Integer maxCrewSize,
                                Double minRating, Double maxRating);

    void deleteShip(String sid) throws NotValidException, SQLException, ShipNotFound;

    Integer getCount(String name, String planet,
                     ShipType shipType,
                     Long after, Long before,
                     Boolean isUsed,
                     Double minSpeed, Double maxSpeed,
                     Integer minCrewSize, Integer maxCrewSize,
                     Double minRating, Double maxRating);

    Ship getShipById(String sid) throws NotValidException, SQLException, ShipNotFound;

}
