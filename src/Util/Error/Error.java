package Util.Error;

import Util.Position;

abstract public class Error extends RuntimeException {
    private String msg;
    private Position pos;

    public Error(String msg, Position pos) {
        this.msg = msg;
        this.pos = pos;
    }

    public String toString() {
        return msg + ": " + pos.toString();
    }
}