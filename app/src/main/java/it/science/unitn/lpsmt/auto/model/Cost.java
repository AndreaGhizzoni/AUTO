package it.science.unitn.lpsmt.auto.model;

/**
 * TODO add description
 */
public abstract class Cost {
    private Long id;
    private Float amount;
    private String notes;

    public Cost(Float amount, String notes, Long id){
        this.setAmount(amount);
        this.setNotes(notes);
        this.setId(id);
    }

//==================================================================================================
// SETTER
//==================================================================================================
    public void setAmount(Float amount) {
        if( amount != null && amount > 0 )
            this.amount = amount;
    }

    public void setNotes(String notes) {
        if( notes != null )
            this.notes = notes;
    }

    public void setId( Long id ){
        if( id != null && !id.equals(this.id) )
            this.id = id;
    }

//==================================================================================================
// SETTER
//==================================================================================================
    public Float getAmount() { return amount; }

    public String getNotes() { return notes; }

    public Long getId(){ return this.id; }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    /**
     * TODO add doc
     */
    public static final class SQLData{
        public static final String TABLE_NAME = Cost.class.getSimpleName().toLowerCase();
        public static final String ID         = "id" ;
        public static final String AMOUNT     = "amount";
        public static final String NOTES      = "notes";
        public static final String PLACE      = "place_id";
        public static final String CLASS      = "class"; // subclass of Cost

        // if class == refuel these fields are set
        public static final String PRICE_PER_LITER = "price_per_liter";
        public static final String DATE            = "date";
        public static final String AT_KM           = "at_km";

        // if class == maintenance these fields are set
        public static final String NAME        = "name";
        public static final String TYPE        = "type";
        public static final String REMINDER_ID = "reminder_id";
    }
}
