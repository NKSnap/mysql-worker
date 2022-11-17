package repository.interfacies;

public interface TableWorker {
    int readAllRecords();
    int deleteRecordByID(int id);
    int insertNewRecord();
    int updateNewRecord(int id);
}