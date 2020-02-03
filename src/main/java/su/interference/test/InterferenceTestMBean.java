package su.interference.test;

public interface InterferenceTestMBean {

    void startup() throws Exception;
    void shutdown() throws Exception;
    void loadData() throws Exception;
    void executeQuery() throws Exception;
    void executeQuery2() throws Exception;
    void executeQuery3() throws Exception;
    void executeStream() throws Exception;
    void executeStream2() throws Exception;
    void executeStream3() throws Exception;
    void insertStream() throws Exception;
    void closeStream() throws Exception;
    void commit() throws Exception;
    void rollback() throws Exception;
    void updateDept() throws Exception;
    void findDeptById(int id) throws Exception;
    void printDeptName() throws  Exception;

}
