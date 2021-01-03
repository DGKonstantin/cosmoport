package com.space.service;

import com.space.NotValidException;
import com.space.ShipNotFound;
import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.sql.SQLException;
import java.util.List;

public interface ShipService {
    ShipRepository repository = null;

    Page<Ship> getAllShips(Specification<Ship> specification, Pageable sortedByName);

    List<Ship> getAllShips(Specification<Ship> specification);
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

    Specification<Ship> filterByName(String name);
    Specification<Ship>filterByPlanet(String planet);
    Specification<Ship>filterByShipType(ShipType shipType);
    Specification<Ship>filterByDate(Long after, Long before);
    Specification<Ship>filterByUsage(Boolean isUsed);
    Specification<Ship>filterBySpeed(Double minSpeed, Double maxSpeed);
    Specification<Ship>filterByCrewSize(Integer minCrewSize, Integer maxCrewSize);
    Specification<Ship>filterByRating(Double minRating, Double maxRating);

}
