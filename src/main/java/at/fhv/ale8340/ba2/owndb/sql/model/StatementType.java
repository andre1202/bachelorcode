package at.fhv.ale8340.ba2.owndb.sql.model;

public enum StatementType {
    INSERT, SELECT, COUNT, ERROR;

    public static StatementType getType(String input){
        switch (input){
            case "INSERT":
                return StatementType.INSERT;
            case "SELECT":
                return StatementType.SELECT;
            case "COUNT":
                return StatementType.COUNT;
            default:
                return StatementType.ERROR;

        }
    }

    @Override
    public String toString() {
        switch (this){
            case INSERT:
                return "INSERT";
            case SELECT:
                return "SELECT";
            case COUNT:
                return "COUNT";
            default:
                return "ERROR";
        }
    }
}
