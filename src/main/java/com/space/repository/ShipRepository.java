package com.space.repository;

import com.space.NotValidException;
import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public interface ShipRepository extends JpaRepository<Ship, Long>, JpaSpecificationExecutor<Ship> {


//    @Qualifier("dataSource") private DataSource ds;
//    private Connection conn;
//    private Statement st;
//    private ResultSet rs;
//    private PreparedStatement ps;
//
//    public ShipRepository()  {
//        try {
//            conn = ds.getConnection();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//    }
//
//    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//
//
//    public Boolean checkID(String sid) throws SQLException, NotValidException {
//        Long aLong = null;
//        try {
//            aLong = Long.parseLong(sid);
//        }catch (Exception e){
//            return false;
//        }
//        if (aLong == 0) return false;
//        else return true;
//    }
//
//    @Override
//    public List<Ship> findAll() {
//        List<Ship> list = new ArrayList<>();
//        try {
//            st = conn.createStatement();
//            rs = st.executeQuery("select * from cosmoport.ship");
//            while (rs.next()){
//                Ship ship = getShipFromTable(rs);
//                list.add(ship);
//            }
//            rs.close();
//            st.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return list;
//    }
//
//    private Ship getShipFromTable(ResultSet rs){
//        Ship ship = null;
//        try {
//            Long id = rs.getLong(rs.findColumn("id"));
//            String name = rs.getString(rs.findColumn("name"));
//            String planet = rs.getString(rs.findColumn("planet"));
//            String shipType = rs.getString(rs.findColumn("shipType"));
//            Date prodDate = rs.getDate(rs.findColumn("prodDate"));
//            Double speed = rs.getDouble(rs.findColumn("speed"));
//            Integer crewSize = rs.getInt(rs.findColumn("crewSize"));
//            Boolean isUsed = rs.getBoolean(rs.findColumn("isUsed"));
//            Double rating = rs.getDouble(rs.findColumn("rating"));
//            prodDate.setTime(prodDate.getTime() + ((long) prodDate.getTimezoneOffset() * 60000));
//            ship = new Ship(id, name, planet, ShipType.valueOf(shipType), isUsed, prodDate, speed, crewSize, rating);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return ship;
//    }
//
//    public Ship createShip(Ship ship){
//        try {
//            st = conn.createStatement();
//            rs = st.executeQuery("select id from cosmoport.ship where id = (select max(id) from ship)");
//            Long max = 0L;
//
//            while (rs.next()){
//                Long id = rs.getLong(rs.findColumn("id"));
//                if (id > max) max = id;
//            }
//            ps = conn.prepareStatement("insert into cosmoport.ship " +
//                    "(id, name, planet, shipType, prodDate, isUsed, speed, crewSize, rating)" +
//                    "values (?,?,?,?,?,?,?,?,?)");
//            ship.setId(max+1L);
//            if (ship.getRating() == null) ship.setRating(ship.calculateRating());
//            ps.setLong(1, ship.getId());
//            ps.setString(2, ship.getName());
//            ps.setString(3, ship.getPlanet());
//            ps.setString(4, ship.getShipType().name());
//            ps.setDate(5, ship.getProdDate());
//            ps.setBoolean(6, ship.getUsed());
//            ps.setDouble(7, ship.getSpeed());
//            ps.setInt(8, ship.getCrewSize());
//            ps.setDouble(9, ship.getRating());
//            ps.executeUpdate();
//
//
//
//            ps.close();
//            rs.close();
//            st.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return ship;
//    }
//
//    public Ship editShip(Ship ship){
//        String query = "";
//        ArrayList<String> queryCollect = new ArrayList<>();
//        if (ship.getName() != null) queryCollect.add("name = '" + ship.getName() + "'");
//        if (ship.getPlanet() != null) queryCollect.add("planet = '" + ship.getPlanet() + "'");
//        if (ship.getCrewSize() != null) queryCollect.add("crewSize = " + ship.getCrewSize());
//        if (ship.getUsed() != null) queryCollect.add("isUsed = " + ship.getUsed());
//        if (ship.getProdDate() != null) queryCollect.add("prodDate = '" + ship.getprodDateFormatter() + "'");
//        if (ship.getShipType() != null) queryCollect.add("shipType = '" + ship.getShipType() + "'");
//        if (ship.getSpeed() != null) queryCollect.add("speed = " + ship.getSpeed());
//        if (queryCollect.size() != 0){
//            query = "update ship set ";
//            for (int i = 0; i < queryCollect.size(); i++) {
//                query += i != queryCollect.size() - 1 ?
//                        String.format(" %s, ",queryCollect.get(i)) :
//                        String.format(" %s ",queryCollect.get(i));
//            }
//            query += " where id = " + ship.getId();
//        }
//        try {
//            st = conn.createStatement();
//            System.out.println(query);
//            if (!query.equals("")) {
//                st.execute(query);
//            }
//            ship = getOne(ship.getId());
//
//            st.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return ship;
//    }
//
//    public Integer getCount(String query){
//        Integer count = null;
//        try {
//            st = conn.createStatement();
//            rs = st.executeQuery(query);
//            rs.last();
//            count = rs.getRow();
//            rs.beforeFirst();
//
//            rs.close();
//            st.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//            return 0;
//        }
//        return count;
//    }
//
//    @Override
//    public List<Ship> findAll(Sort sort) {
//        return null;
//    }
//
//    @Override
//    public Page<Ship> findAll(Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public List<Ship> findAllById(Iterable<Long> iterable) {
//        return null;
//    }
//
//
//    @Override
//    public long count() {
//        return 0;
//    }
//
//    @Override
//    public void deleteById(Long aLong) {
//        try {
//            st = conn.createStatement();
//            st.execute("delete from cosmoport.ship where id = " + aLong);
//
//            st.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//    }
//
//    @Override
//    public void delete(Ship ship) {
//
//    }
//
//    @Override
//    public void deleteAll(Iterable<? extends Ship> iterable) {
//
//    }
//
//    @Override
//    public void deleteAll() {
//
//    }
//
//    @Override
//    public <S extends Ship> S save(S s) {
//        return null;
//    }
//
//    @Override
//    public <S extends Ship> List<S> saveAll(Iterable<S> iterable) {
//        return null;
//    }
//
//    @Override
//    public Optional<Ship> findById(Long aLong) {
//        Ship ship = null;
//        try {
//            st = conn.createStatement();
//            rs = st.executeQuery("select * from cosmoport.ship where id = " + aLong);
//            while (rs.next()){
//                Long id = rs.getLong(rs.findColumn("id"));
//                String name = rs.getString(rs.findColumn("name"));
//                String planet = rs.getString(rs.findColumn("planet"));
//                String shipType = rs.getString(rs.findColumn("shipType"));
//                Boolean isUsed = rs.getBoolean(rs.findColumn("isUsed"));
//                Date prodDate = rs.getDate(rs.findColumn("prodDate"));
//                Double speed = rs.getDouble(rs.findColumn("speed"));
//                Integer crewSize = rs.getInt(rs.findColumn("crewSize"));
//                Double rating = rs.getDouble(rs.findColumn("rating"));
//                ship = new Ship(id, name, planet, ShipType.valueOf(shipType), isUsed, prodDate, speed, crewSize);
//            }
//            rs.close();
//            st.close();
//        } catch (SQLException  throwables) {
//            throwables.printStackTrace();
//        }
//        return Optional.of(ship);
//    }
//
//    @Override
//    public boolean existsById(Long aLong) {
//        try {
//            st = conn.createStatement();
//            rs = st.executeQuery("select id from ship where id = " + aLong);
//            return rs.next();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//            return false;
//        }
//    }
//
//    @Override
//    public void flush() {
//
//    }
//
//    @Override
//    public <S extends Ship> S saveAndFlush(S s) {
//        return null;
//    }
//
//    @Override
//    public void deleteInBatch(Iterable<Ship> iterable) {
//
//    }
//
//    @Override
//    public void deleteAllInBatch() {
//
//    }
//
//    @Override
//    public Ship getOne(Long aLong) {
//        Ship ship = null;
//        try {
//            st = conn.createStatement();
//            rs = st.executeQuery("select * from ship where id = " + aLong);
//            while (rs.next()){
//                ship = getShipFromTable(rs);
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return ship;
//    }
//
//
//
//    @Override
//    public <S extends Ship> Optional<S> findOne(Example<S> example) {
//        return Optional.empty();
//    }
//
//    @Override
//    public <S extends Ship> List<S> findAll(Example<S> example) {
//        return null;
//    }
//
//    @Override
//    public <S extends Ship> List<S> findAll(Example<S> example, Sort sort) {
//        return null;
//    }
//
//    @Override
//    public <S extends Ship> Page<S> findAll(Example<S> example, Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public <S extends Ship> long count(Example<S> example) {
//        return 0;
//    }
//
//    @Override
//    public <S extends Ship> boolean exists(Example<S> example) {
//        return false;
//    }
//
//    @Override
//    public Optional<Ship> findOne(Specification<Ship> specification) {
//        return Optional.empty();
//    }
//
//    @Override
//    public List<Ship> findAll(Specification<Ship> specification) {
//        return null;
//    }
//
//    @Override
//    public Page<Ship> findAll(Specification<Ship> specification, Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public List<Ship> findAll(Specification<Ship> specification, Sort sort) {
//        return null;
//    }
//
//    @Override
//    public long count(Specification<Ship> specification) {
//        return 0;
//    }
}
