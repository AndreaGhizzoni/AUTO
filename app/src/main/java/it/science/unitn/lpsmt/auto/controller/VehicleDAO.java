package it.science.unitn.lpsmt.auto.controller;

import java.util.List;

import it.science.unitn.lpsmt.auto.model.Vehicle;

/**
 * TODO add doc
 */
public interface VehicleDAO {
    /**
     * TODO add doc
     * @param v
     * @return
     */
    Long save( Vehicle v );

    /**
     * TODO add doc
     * @param id
     * @return
     */
    Vehicle get( Long id );

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
     * @return
     */
    List<Vehicle> getAll();
}
