package net.kyberia.dynatrace.opentelemetry.model;

public class Transaction {
    private String id;
    private String name;

    public Transaction(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getID() {
        return this.id; 
    }

    public void setID(String id) {
        this.id = id; 
    }

    public String getName() {
        return this.name; 
    }
    
    public void setName(String name) {
        this.name = name; 
    }
}
