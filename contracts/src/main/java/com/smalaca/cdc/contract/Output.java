package com.smalaca.cdc.contract;

public class Output {
    private long amount;
    private String error;

    public Output() {}

    public Output(long amount, String error) {
        this.amount = amount;
        this.error = error;
    }

    public long getAmount() {
        return amount;
    }

    public String getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Output output = (Output) o;

        if (amount != output.amount) return false;
        return error != null ? error.equals(output.error) : output.error == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (amount ^ (amount >>> 32));
        result = 31 * result + (error != null ? error.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Output{" +
                "amount=" + amount +
                ", error='" + error + '\'' +
                '}';
    }
}
