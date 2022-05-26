package sk.slsp.opentelemetry.demo.model;

public class Quote {
    private long input;
    private long value = 0;

    public Quote() {}

    public long getInput() {
        return this.input; 
    }

    public void setID(long input) {
        this.input = input; 
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
