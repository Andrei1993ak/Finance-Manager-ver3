package com.gmail.a93ak.andrei19.finance30.control.base;

public class RequestHolder<Pojo> {

    private static final int KEY_ADD = 1;
    private static final int KEY_EDIT = 2;
    private static final int KEY_DELETE = 3;
    private static final int KEY_GET = 4;
    private static final int KEY_GET_ALL = 5;
    private static final int KEY_DELETE_ALL = 6;
    private static final int KEY_GET_ALL_TO_LIST = 7;

    private Request addRequest = null;
    private Request editRequest = null;
    private Request deleteRequest = null;
    private Request getRequest = null;
    private Request getAllRequest = null;
    private Request deleteAllRequest = null;
    private Request getAllToListRequest = null;

    public Request getGetRequest() {
        return getRequest;
    }

    public Request getGetAllRequest() {
        return getAllRequest;
    }

    public Request getDeleteAllRequest() {
        return deleteAllRequest;
    }

    public Request getGetAllToListRequest() {
        return getAllToListRequest;
    }

    public Request getAddRequest() {
        return addRequest;
    }

    public Request getEditRequest() {
        return editRequest;
    }

    public Request getDeleteRequest() {
        return deleteRequest;
    }

    public void setAddRequest(Pojo pojo) {
        this.addRequest = new Request(KEY_ADD, pojo);
    }

    public void setEditRequest(Pojo pojo) {
        this.editRequest = new Request(KEY_EDIT, pojo);
    }

    public void setDeleteRequest(Long id) {
        this.deleteRequest = new Request(KEY_DELETE, id);
    }

    public void setGetRequest(Long id) {
        this.getRequest = new Request(KEY_GET, id);
    }

    public void setGetAllRequest() {
        this.getAllRequest = new Request(KEY_GET_ALL, null);
    }

    public void setDeleteAllRequest() {
        this.deleteAllRequest = new Request(KEY_DELETE_ALL, null);
    }

    public void setGetAllToListRequest(Integer selection) {
        this.getAllToListRequest = new Request(KEY_GET_ALL_TO_LIST, selection);
    }
}
