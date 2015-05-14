package it.science.unitn.lpsmt.auto.controller;

import java.util.List;

import it.science.unitn.lpsmt.auto.model.Cost;

/**
 * TODO add doc
 */
public interface CostDAO {
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
     * @return
     */
    List<Cost> getAll();
}
