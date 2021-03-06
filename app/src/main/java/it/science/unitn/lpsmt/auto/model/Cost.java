package it.science.unitn.lpsmt.auto.model;

import it.science.unitn.lpsmt.auto.model.util.Const;

/**
 * TODO add description
 */
public abstract class Cost {
    private Long id;
    private Vehicle vehicle;
    private Float amount;
    private String notes;

    public Cost(Long id, Vehicle vehicle, Float amount, String notes){
        this.setId(id);
        this.setVehicle(vehicle);
        this.setAmount(amount);
        this.setNotes(notes);
    }

    public Cost(Long id, Vehicle vehicle, Float amount){
        this(id, vehicle, amount, null);
    }

//==================================================================================================
// SETTER
//==================================================================================================
    public void setId(Long id) throws IllegalArgumentException{
        if( id == null )
            this.id = Const.NO_DB_ID_SET;
        else if( id < Const.NO_DB_ID_SET )
            throw new IllegalArgumentException("ID of Cost can not be less then "+Const.NO_DB_ID_SET);
        else
            this.id = id;
    }

    public void setVehicle( Vehicle vehicle ) throws IllegalArgumentException{
        if( vehicle == null )
            throw new IllegalArgumentException("Vehicle associated can not be null.");
        if( vehicle.isDefaultVehicle() )
            throw new IllegalArgumentException("Vehicle associated can not be the Default Vehicle.");
        this.vehicle = vehicle;
        this.vehicle.addCost(this);
    }

    public void setAmount(Float amount) throws IllegalArgumentException{
        if( amount == null )
            throw new IllegalArgumentException("Amount of Cost can not be null.");
        if( amount < 1 )
            throw new IllegalArgumentException("Amount of Cost can not be equal or less then zero.");
        this.amount = amount;
    }

    public void setNotes(String notes) {
        if( notes == null )
            this.notes = "";
        else
            this.notes = notes;
    }

//==================================================================================================
// GETTER
//==================================================================================================
    public Long getId(){ return this.id; }

    public Vehicle getVehicle(){ return this.vehicle; }

    public Float getAmount() { return amount; }

    public String getNotes() { return notes; }

//==================================================================================================
//  OVERRIDE
//==================================================================================================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cost cost = (Cost) o;

        if (!id.equals(cost.id)) return false;
        if (!vehicle.equals(cost.vehicle)) return false;
        return amount.equals(cost.amount);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + vehicle.hashCode();
        result = 31 * result + amount.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Cost{" +
                "id=" + id +
                ", vehicle=" + vehicle.getId() +
                ", amount=" + amount +
                ", notes='" + notes + '\'' +
                '}';
    }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    /**
     * This is the representation of the Database Table.
     * The fields name are in the same order as there are in the sql create statement.
     * =============================== DO NOT MOVE UP OR DOWN! =====================================
     */
    public static final class SQLData{
        public static final String ID         = "id" ;
        public static final String VEHICLE_ID = "vehicle_id";
        public static final String AMOUNT     = "amount";
        public static final String NOTES      = "notes";
        public static final String CLASS      = "class"; // subclass of Cost
        public static final String PLACE_ID   = "place_id";

        // if class == refuel these fields are set
        public static final String PRICE_PER_LITER = "price_per_liter";
        public static final String DATE            = "date";
        public static final String AT_KM           = "at_km";

        // if class == maintenance these fields are set
        public static final String NAME        = "name";
        public static final String TYPE        = "type";
        public static final String CALENDAR_ID = "calendar_id";

        public static final String TABLE_NAME = Cost.class.getSimpleName().toLowerCase();
        public static final String[] ALL_COLUMNS = {ID, VEHICLE_ID, AMOUNT, NOTES, CLASS, PLACE_ID,
                PRICE_PER_LITER, DATE, AT_KM, NAME, TYPE, CALENDAR_ID};
        public static final String[] ALL_COLUMNS_WITHOUT_VEHICLE_FK = {ID, AMOUNT, NOTES, CLASS, PLACE_ID,
                PRICE_PER_LITER, DATE, AT_KM, NAME, TYPE, CALENDAR_ID};
        public static final String[] COLUMNS_REFUEL = {ID, VEHICLE_ID, AMOUNT, NOTES, PLACE_ID,
                PRICE_PER_LITER, DATE, AT_KM};
        public static final String[] COLUMNS_MAINTENANCE = {ID, VEHICLE_ID, AMOUNT, NOTES, PLACE_ID,
                NAME, TYPE, CALENDAR_ID};

        public static final String SQL_CREATE =
                "create table "+TABLE_NAME+" ( "+
                    ID+" integer primary key autoincrement, "+
                    VEHICLE_ID+" integer, "+
                    AMOUNT+" real not null, "+
                    NOTES+" text, "+
                    CLASS+" text not null, "+
                    PLACE_ID+" integer, "+
                    PRICE_PER_LITER+" real, "+
                    DATE+" datetime, "+
                    AT_KM+" integer, "+
                    NAME+" text, "+
                    TYPE+" text, "+
                    CALENDAR_ID+" integer, "+
                    "foreign key ("+PLACE_ID+") references " +
                        Place.SQLData.TABLE_NAME+" ("+Place.SQLData.ID+"), " +
                    "foreign key ("+VEHICLE_ID+") references "+
                        Vehicle.SQLData.TABLE_NAME+" ("+Vehicle.SQLData.ID+") "+
                ");";
    }
}
