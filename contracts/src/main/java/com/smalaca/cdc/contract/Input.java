package com.smalaca.cdc.contract;

public class Input {
    private String from;
    private String to;
    private long amount;
    private String date;

    public Input() {}

    public Input(String from, String to, long amount, String date) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public long getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Input input = (Input) o;

        if (amount != input.amount) return false;
        if (from != null ? !from.equals(input.from) : input.from != null) return false;
        if (to != null ? !to.equals(input.to) : input.to != null) return false;
        return date != null ? date.equals(input.date) : input.date == null;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (int) (amount ^ (amount >>> 32));
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Input{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                '}';
    }
}
