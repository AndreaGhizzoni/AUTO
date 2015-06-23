package it.science.unitn.lpsmt.auto.controller;

import java.util.List;

import it.science.unitn.lpsmt.auto.model.Vehicle;

/**
 * TODO add doc
 */
public interface VehicleDAO {
    /**
     * TODO add doc
     */
    void close();

    /**
     * TODO add doc
     * @param v
     * @return
     */
    Long save( Vehicle v );

    /**
     * TODO add doc
     * @param v
     */
    void update( Vehicle old, Vehicle newest );

    /**
     * TODO add doc
     * @param id
     * @return
     */
    Vehicle get( Long id );

    /**
     * TODO add doc
     * @param id
     * @return
     */
    boolean exists( Long id );

    /**
     * TODO add doc
     * @param v
     */
    void delete( Vehicle v );

    /**
     * TODO add doc
     * @param id
     */
    void delete( Long id );

    /**
     * TODO add doc
     */
    void deleteAll();

    /**
     * TODO add doc
     * @return
     */
    List<Vehicle> getAll();

    /**
     * TODO add doc
     * @return
     */
    int countObject();
}
