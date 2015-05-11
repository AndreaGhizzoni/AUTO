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

    public Cost(Float amount) {
        this(amount, "", -1L);
    }

    public Cost(){
        this(null);
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
}
