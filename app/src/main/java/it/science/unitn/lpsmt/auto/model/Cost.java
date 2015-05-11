package it.science.unitn.lpsmt.auto.model;

/**
 * TODO add description
 */
public abstract class Cost {
    float amount;
    String notes;

    public Cost(float amount, String notes){
        this.setAmount(amount);
        this.setNotes(notes);
    }

    public Cost(float amount) {
        this(amount, "");
    }

    public Cost(){}

//==================================================================================================
// SETTER
//==================================================================================================
    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setNotes(String notes) {
        if(notes != null)
            this.notes = notes;
    }

//==================================================================================================
// SETTER
//==================================================================================================
    public float getAmount() { return amount; }

    public String getNotes() { return notes; }
}
