package it.science.unitn.lpsmt.auto.controller;

import java.util.List;

import it.science.unitn.lpsmt.auto.model.Cost;
import it.science.unitn.lpsmt.auto.model.Maintenance;
import it.science.unitn.lpsmt.auto.model.Refuel;
import it.science.unitn.lpsmt.auto.model.Vehicle;

/**
 * TODO add doc
 */
public interface CostDAO {
    /**
     * TODO add doc
     */
    void close();

    /**
     * TODO add doc
     * @param c
     * @return
     */
    Long save( Cost c );

    /**
     * TODO add doc
     * @param id
     * @return
     */
    Cost get( Long id );

    /**
     * TODO add doc
     * @param id
     * @return
     */
    boolean exists( Long id );

    /**
     * TODO add doc
     * @param c
     */
    void delete( Cost c );

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
     * @param id
     */
    void deleteAllWhereVehicleID( Long id );

    /**
     * TODO add doc
     * @return
     */
    List<Cost> getAll();

    /**
     * TODO add doc
     * @return
     */
    List<Refuel> getAllRefuel();

    /**
     * TODO add doc
     * @param v
     * @return
     */
    List<Refuel> getAllRefuelWhereVehicleIs(Vehicle v);

    /**
     * TODO add doc
     * @return
     */
    List<Maintenance> getAllMaintenance();

    /**
     * TODO add doc
     * @return
     */
    List<Maintenance> getAllMaintenanceWhereTypeIs(Maintenance.Type type);

    /**
     * TODO add doc
     * @param v
     * @return
     */
    List<Cost> getAllWhereVehicleIs(Vehicle v);

    /**
     * TODO add doc
     * @return
     */
    int countObject();
}
