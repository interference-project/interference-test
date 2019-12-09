package su.interference.test;

public interface InterferenceTestMBean {

    void startup() throws Exception;
    void shutdown() throws Exception;
    void loadData() throws Exception;
    void executeQuery() throws Exception;
    void executeQuery2() throws Exception;
    void commit() throws Exception;
    void rollback() throws Exception;
    void updateDept() throws Exception;
    void findDept() throws  Exception;
    void printDeptName() throws  Exception;

}
