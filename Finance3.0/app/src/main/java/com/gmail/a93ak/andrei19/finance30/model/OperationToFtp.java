package com.gmail.a93ak.andrei19.finance30.model;


public class OperationToFtp {

    public static final int ADD = 0;
    public static final int UPDATE = 1;
    public static final int DELETE = 2;

    private long id;
    private long cost_id;
    private int operation;

    public OperationToFtp() {
    }

    public OperationToFtp(long cost_id, int operation) {
        this.cost_id = cost_id;
        this.operation = operation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCost_id() {
        return cost_id;
    }

    public void setCost_id(long cost_id) {
        this.cost_id = cost_id;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }
}
