package com.space.service;

import com.space.NotValidException;
import com.space.ShipNotFound;
import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import java.sql.SQLException;
import java.util.*;

@Service
public class ShipServiceImpl implements ShipService{

    private ShipRepository repository;

    @Autowired
    public void setShipRepository(ShipRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<Ship> getAllShips() {
        return repository.findAll();
    }

    private List<Ship> getSortedShips(List<Ship> list, ShipOrder order, Integer pageSize, Integer pageNumber){
        if (order != null) {
            switch (order){
                case ID:
                    Collections.sort(list, new Comparator<Ship>() {
                        @Override
                        public int compare(Ship o1, Ship o2) {
                            return o1.getId().compareTo(o2.getId());
                        }
                    });
                    break;
                case DATE:
                    Collections.sort(list, new Comparator<Ship>() {
                        @Override
                        public int compare(Ship o1, Ship o2) {
                            int res = o1.getProdDate().compareTo(o2.getProdDate());
                            if (res == 0) return o1.getId().compareTo(o2.getId());
                            return res;
                        }
                    });
                    break;
                case SPEED:
                    Collections.sort(list, new Comparator<Ship>() {
                        @Override
                        public int compare(Ship o1, Ship o2) {
                            int res = o1.getSpeed().compareTo(o2.getSpeed());
                            if (res == 0) return o1.getId().compareTo(o2.getId());
                            return res;
                        }
                    });
                    break;
                case RATING:
                    Collections.sort(list, new Comparator<Ship>() {
                        @Override
                        public int compare(Ship o1, Ship o2) {
                            int res = o1.getRating().compareTo(o2.getRating());
                            if (res == 0) return o1.getId().compareTo(o2.getId());
                            return res;
                        }
                    });
                    break;
            }
        }
        List<Ship> cuttedList = new ArrayList<>();
        int start = getBountary(pageSize, pageNumber, list.size())[0];
        int finish = getBountary(pageSize, pageNumber, list.size())[1];
        for (int i = start; i < finish; i++) {
            cuttedList.add(list.get(i));
        }
        return cuttedList;
    }
    private int[] getBountary(Integer pageSize, Integer pageNumber, int listSize){
        int[] ints = new int[2];
        if (pageNumber == null) pageNumber = 0;
        if (pageSize == null) pageSize = 3;
        if (pageNumber != 0){
            ints[0] = pageNumber * pageSize;
            ints[1] = Math.min(pageNumber * pageSize + pageSize, listSize);
        }else {
            ints[1] = listSize < pageSize ? listSize : pageSize == 0 ? 3 : pageSize;
        }
        return ints;
    }

    public List<Ship> getFilteredShips(ShipOrder order, Integer pageSize, Integer pageNumber,
                                       String name, String planet,
                                       ShipType shipType,
                                       Long after, Long before,
                                       Boolean isUsed,
                                       Double minSpeed, Double maxSpeed,
                                       Integer minCrewSize, Integer maxCrewSize,
                                       Double minRating, Double maxRating){
        List<Ship> list = getAllShips();
        if (name != null){
            for (int i = 0; i < list.size(); i++) {
                if(!list.get(i).getName().contains(name)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (planet != null){
            for (int i = 0; i < list.size(); i++) {
                if(!list.get(i).getPlanet().contains(planet)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (shipType != null){
            for (int i = 0; i < list.size(); i++) {
                if(!list.get(i).getShipType().equals(shipType)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (after != null){
            Calendar calendarThis = Calendar.getInstance();
            Calendar calendarThat = Calendar.getInstance();
            for (int i = 0; i < list.size(); i++) {
                calendarThis.setTimeInMillis(list.get(i).getProdDate().getTime());
                //int prodYearThis = calendarThis.get(Calendar.YEAR);
                int prodYearThis = Integer.parseInt(list.get(i).getProdDate().toString().substring(0, 4));
                calendarThat.setTimeInMillis(after);
                int prodYearThat = calendarThat.get(Calendar.YEAR);

                if(!(prodYearThis >= prodYearThat)) {
                    list.remove(i);
                    --i;
                    if (list.size() == 0) break;
                }
            }
        }
        if (before != null){
            Calendar calendarThis = Calendar.getInstance();
            Calendar calendarThat = Calendar.getInstance();
            for (int i = 0; i < list.size(); i++) {
                calendarThis.setTimeInMillis(list.get(i).getProdDate().getTime());
                //int prodYearThis = calendarThis.get(Calendar.YEAR);
                int prodYearThis = Integer.parseInt(list.get(i).getProdDate().toString().substring(0, 4));
                calendarThat.setTimeInMillis(before);
                int prodYearThat = calendarThat.get(Calendar.YEAR);
                if(!(prodYearThis <= prodYearThat)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (isUsed != null){
            for (int i = 0; i < list.size(); i++) {
                if(!(list.get(i).getUsed() == isUsed)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (minSpeed != null){
            for (int i = 0; i < list.size(); i++) {
                if(!(list.get(i).getSpeed() >= minSpeed)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (maxSpeed != null){
            for (int i = 0; i < list.size(); i++) {
                if(!(list.get(i).getSpeed() <= maxSpeed)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (minCrewSize != null){
            for (int i = 0; i < list.size(); i++) {
                if(!(list.get(i).getCrewSize() >= minCrewSize)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (maxCrewSize != null){
            for (int i = 0; i < list.size(); i++) {
                if(!(list.get(i).getCrewSize() <= maxCrewSize)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (minRating != null){
            for (int i = 0; i < list.size(); i++) {
                //System.out.println("name: " + list.get(i).getName() + " rating: " +  list.get(i).getRating() );
                if(!(list.get(i).getRating() >= minRating)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (maxRating != null){
            for (int i = 0; i < list.size(); i++) {
                if(!(list.get(i).getRating() <= maxRating)) {
                    list.remove(i);
                    --i;
                }
            }
        }

        List<Ship> result = getSortedShips(list, order, pageSize, pageNumber);
        return result;
    }

    public void deleteShip(String sid) throws NotValidException, SQLException, ShipNotFound {
        if(!checkID(sid)) throw new NotValidException(String.format("Not Valid ID = ", sid));
        Long id = Long.parseLong(sid);
        if(!repository.existsById(id)) throw new ShipNotFound(String.format("Ship with %d doesn't exist", id));
        repository.deleteById(id);
    }

    @Override
    public Ship createShip(Ship ship) throws NotValidException {
        ship = checkShip(ship, false);
        ship.setRating(ship.calculateRating());
        return repository.saveAndFlush(ship);
    }

    public Ship editShip(Ship newship, String sid) throws NotValidException, SQLException, ShipNotFound {
        if(!checkID(sid)) throw new NotValidException(String.format("Ship with %s doesn't exist", sid));
        Long id = Long.parseLong(sid);
        Optional<Ship> optionalShip = repository.findById(id);
        if (!optionalShip.isPresent()) throw new ShipNotFound(String.format("Ship with %s doesn't exist", sid));
        Ship oldship = optionalShip.get();
        newship = checkShip(newship, true);
        oldship = merge(newship, oldship);
        oldship.setRating(oldship.calculateRating());
        return repository.save(oldship);
    }

    public Integer getCount(String name, String planet,
                            ShipType shipType,
                            Long after, Long before,
                            Boolean isUsed,
                            Double minSpeed, Double maxSpeed,
                            Integer minCrewSize, Integer maxCrewSize,
                            Double minRating, Double maxRating){
        List<Ship> list = getAllShips();
        if (name != null){
            for (int i = 0; i < list.size(); i++) {
                if(!list.get(i).getName().contains(name)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (planet != null){
            for (int i = 0; i < list.size(); i++) {
                if(!list.get(i).getPlanet().contains(planet)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (shipType != null){
            for (int i = 0; i < list.size(); i++) {
                if(!list.get(i).getShipType().equals(shipType)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (after != null){
            Calendar prodDate = Calendar.getInstance();
            Calendar afterDate = Calendar.getInstance();
            for (int i = 0; i < list.size(); i++) {
                prodDate.setTimeInMillis(list.get(i).getProdDate().getTime());
                afterDate.setTimeInMillis(after);
                if(!(prodDate.get(Calendar.YEAR) >= afterDate.get(Calendar.YEAR))) {
                    list.remove(i);
                    --i;
                    if (list.size() == 0) break;
                }
            }
        }
        if (before != null){
            Calendar prodDate = Calendar.getInstance();
            Calendar beforeDate = Calendar.getInstance();
            for (int i = 0; i < list.size(); i++) {
                prodDate.setTimeInMillis(list.get(i).getProdDate().getTime());
                beforeDate.setTimeInMillis(before);
                if(!(prodDate.get(Calendar.YEAR) <= beforeDate.get(Calendar.YEAR))) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (isUsed != null){
            for (int i = 0; i < list.size(); i++) {
                if(!(list.get(i).getUsed() == isUsed)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (minSpeed != null){
            for (int i = 0; i < list.size(); i++) {
                if(!(list.get(i).getSpeed() >= minSpeed)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (maxSpeed != null){
            for (int i = 0; i < list.size(); i++) {
                if(!(list.get(i).getSpeed() <= maxSpeed)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (minCrewSize != null){
            for (int i = 0; i < list.size(); i++) {
                if(!(list.get(i).getCrewSize() >= minCrewSize)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (maxCrewSize != null){
            for (int i = 0; i < list.size(); i++) {
                if(!(list.get(i).getCrewSize() <= maxCrewSize)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (minRating != null){
            for (int i = 0; i < list.size(); i++) {
                //System.out.println("name: " + list.get(i).getName() + " rating: " +  list.get(i).getRating() );
                if(!(list.get(i).getRating() >= minRating)) {
                    list.remove(i);
                    --i;
                }
            }
        }
        if (maxRating != null){
            for (int i = 0; i < list.size(); i++) {
                if(!(list.get(i).getRating() <= maxRating)) {
                    list.remove(i);
                    --i;
                }
            }
        }

        return list.size();
    }

    public Ship getShipById(String sid) throws NotValidException, SQLException, ShipNotFound {
        if(!checkID(sid)) throw new NotValidException(String.format("Ship with %s doesn't exist", sid));
        Long id = Long.parseLong(sid);
        List<Ship> ships = getAllShips();
        for(Ship ship : ships)
            if (ship.getId().equals(id)) return ship;
        throw new ShipNotFound(String.format("Ship with %d doesn't exist", id));
    }

    private Ship checkShip(Ship ship, Boolean isUpdate) throws NotValidException {
        if (!isUpdate){
            if (ship.getName() == null || ship.getPlanet() == null ||
                    ship.getProdDate() == null || ship.getSpeed() == null ||
                    ship.getCrewSize() == null) throw new NotValidException("Some params is empty");
        }

//        if (ship.getName() == null && ship.getPlanet() == null &&
//                ship.getProdDate() == null && ship.getSpeed() == null &&
//                ship.getCrewSize() == null && ship.getUsed() == null &&
//                ship.getShipType() == null && ship.getRating() == null) throw new NotValidException("All params is empty");

        if (ship.getName() != null && ship.getName().length() > 50 ||
                ship.getName() != null && ship.getName().equals(""))
            throw new NotValidException("Name is not valid");

        if (ship.getPlanet() != null && ship.getPlanet().length() > 50 ||
                ship.getPlanet() != null && ship.getPlanet().equals(""))
            throw new NotValidException("Planet is not valid");

        if (!isUpdate){
            if (ship.getUsed() == null) ship.setUsed(false);
            if (ship.getRating() == null) ship.setRating(ship.calculateRating());
        }

        Calendar calendar = Calendar.getInstance();
        if (ship.getProdDate() != null) calendar.setTimeInMillis(ship.getProdDate().getTime());
        if  (ship.getProdDate() != null && calendar.get(Calendar.YEAR) <= 2800 ||
                ship.getProdDate() != null && calendar.get(Calendar.YEAR) >= 3019 ||
                ship.getProdDate() != null && calendar.get(Calendar.YEAR) < 0)
            throw new NotValidException("Prod date is not valid");

        if (ship.getSpeed() != null && Math.round(ship.getSpeed() * 100.0) / 100.0 <= 0.01 ||
                ship.getSpeed() != null && Math.round(ship.getSpeed() * 100.0) / 100.0 >= 0.99)
            throw new NotValidException("Speed is not valid");

        if (ship.getCrewSize() != null && ship.getCrewSize() <= 1 ||
                ship.getCrewSize() != null && ship.getCrewSize() >= 9999)
            throw new NotValidException("Crew size is not valid");
        return ship;
    }

    private Boolean checkID(String sid) throws SQLException, NotValidException {
        Long aLong = null;
        try {
            aLong = Long.parseLong(sid);
        }catch (Exception e){
            return false;
        }
        if (aLong == 0) return false;
        else return true;
    }

    private Ship merge(Ship newship, Ship oldship){
        if (newship.getName() != null) oldship.setName(newship.getName());
        if (newship.getPlanet() != null) oldship.setPlanet(newship.getPlanet());
        if (newship.getShipType() != null) oldship.setShipType(newship.getShipType());
        if (newship.getSpeed() != null) oldship.setSpeed(newship.getSpeed());
        if (newship.getRating() != null) oldship.setRating(newship.getRating());
        if (newship.getUsed() != null) oldship.setUsed(newship.getUsed());
        if (newship.getCrewSize() != null) oldship.setCrewSize(newship.getCrewSize());
        if (newship.getProdDate() != null) oldship.setProdDate(newship.getProdDate());

        return oldship;
    }
}
