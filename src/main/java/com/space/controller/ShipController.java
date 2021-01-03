package com.space.controller;

import com.space.NotValidException;
import com.space.ShipNotFound;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import com.space.service.ShipServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;


@Controller
@ResponseBody
@RequestMapping
public class ShipController {

    private ShipService shipServiceImpl;

    @Autowired
    public void setShipService(ShipService shipService) {
        this.shipServiceImpl = shipService;
    }

    @GetMapping("/rest/ships")
    public List<Ship> getList(@RequestParam(required = false) String name,
                              @RequestParam(required = false) String planet,
                              @RequestParam(required = false) ShipType shipType,
                              @RequestParam(required = false) Long after,
                              @RequestParam(required = false) Long before,
                              @RequestParam(required = false) Boolean isUsed,
                              @RequestParam(required = false) Double minSpeed,
                              @RequestParam(required = false) Double maxSpeed,
                              @RequestParam(required = false) Integer minCrewSize,
                              @RequestParam(required = false) Integer maxCrewSize,
                              @RequestParam(required = false) Double minRating,
                              @RequestParam(required = false) Double maxRating,
                              @RequestParam(required = false) ShipOrder order,
                              @RequestParam(required = false) Integer pageNumber,
                              @RequestParam(required = false) Integer pageSize){

        List<Ship> list = shipServiceImpl.getFilteredShips(order, pageSize, pageNumber,
                                                            name, planet,
                                                            shipType,
                                                            after, before,
                                                            isUsed,
                                                            minSpeed, maxSpeed,
                                                            minCrewSize, maxCrewSize,
                                                            minRating, maxRating);
        return list;
    }

    @GetMapping("/rest/ships/count")
    public Integer getCount(@RequestParam(required = false) String name,
                              @RequestParam(required = false) String planet,
                              @RequestParam(required = false) ShipType shipType,
                              @RequestParam(required = false) Long after,
                              @RequestParam(required = false) Long before,
                              @RequestParam(required = false) Boolean isUsed,
                              @RequestParam(required = false) Double minSpeed,
                              @RequestParam(required = false) Double maxSpeed,
                              @RequestParam(required = false) Integer minCrewSize,
                              @RequestParam(required = false) Integer maxCrewSize,
                              @RequestParam(required = false) Double minRating,
                              @RequestParam(required = false) Double maxRating){
        return shipServiceImpl.getCount(name, planet,
                                        shipType,
                                        after, before,
                                        isUsed,
                                        minSpeed, maxSpeed,
                                        minCrewSize, maxCrewSize,
                                        minRating, maxRating);
    }

    @PostMapping("/rest/ships")
    public Ship createShip(@RequestBody Ship ship) throws NotValidException {
        return shipServiceImpl.createShip(ship);
    }


    @DeleteMapping("/rest/ships/{sid}")
    void deleteShip(@PathVariable(value = "sid") String sid) throws NotValidException, SQLException, ShipNotFound {
        shipServiceImpl.deleteShip(sid);
    }

    @GetMapping("/rest/ships/{sid}")
    Ship getShipById(@PathVariable(value = "sid") String sid) throws NotValidException, SQLException, ShipNotFound {
        return shipServiceImpl.getShipById(sid);
    }

    @PostMapping(value = "/rest/ships/{sid}")
    public Ship editShip(@PathVariable(value = "sid") String sid, @RequestBody Ship ship) throws NotValidException, SQLException, ShipNotFound {
        if (ship.getId() != null)  new NotValidException("ID is not found");
        ship = shipServiceImpl.editShip(ship, sid);
        return ship;
    }

}
