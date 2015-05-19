package it.science.unitn.lpsmt.auto.controller;

import java.util.List;

import it.science.unitn.lpsmt.auto.model.Reminder;

/**
 * TODO add doc
 */
public interface ReminderDAO {
    /**
     * TODO add doc
     */
    void close();

    /**
     * TODO add doc
     * @param r
     * @return
     */
    Long save( Reminder r );

    /**
     * TODO add doc
     * @param id
     * @return
     */
    Reminder get( Long id );

    /**
     * TODO add doc
     * @param id
     * @return
     */
    boolean exists( Long id );

    /**
     * TODO add doc
     * @param r
     */
    void delete( Reminder r );

    /**
     * TODO add doc
     * @param id
     */
    void delete( Long id );

    /**
     * TODO add doc
     * @return
     */
    List<Reminder> getAll();
}
