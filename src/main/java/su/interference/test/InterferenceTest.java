/**
The MIT License (MIT)

Copyright (c) 2010-2020 interference

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */
package su.interference.test;

import su.interference.core.Instance;
import su.interference.persistent.Session;
import su.interference.proxy.GenericResult;
import su.interference.sql.ResultSet;
import su.interference.sql.StreamQueue;
import su.interference.test.entity.Dept;
import su.interference.test.entity.Emp;
import su.interference.test.entity.StreamTable;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Yuriy Glotanov
 * @since 1.0
 */

public class InterferenceTest implements InterferenceTestMBean {

    private static Instance instance;
    private static Session session;
    private static Session session2;
    private static ExecutorService exec = Executors.newCachedThreadPool();
    private Dept dept;
    private ResultSet streamRS;

    public InterferenceTest() throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("su.interference:type=interference-test");
        mbs.registerMBean(this, name);
        System.in.read();
    }

    /*
        use JConsole for access to JMX bean
    */
    public static void main(String[] args) throws Exception {
        InterferenceTest test = new InterferenceTest();
    }

    /*
        startup instance
    */
    public void startup() {
        try {
            instance = Instance.getInstance();
            final Session s = Session.getSession();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    instance.shutdownInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("stopped");
            }));

            s.setUserId(Session.ROOT_USER_ID);

            instance.startupInstance(s);

            // session for insert/update
            session = Session.getSession();

            // session for execute queries
            session2 = Session.getSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() throws Exception {
        instance.shutdownInstance();
    }

    /*
        load 100000 records of random test data to both Dept and Emp tables
    */
    public void loadData() {
        try {
            for (int i = 1; i <= 100000; i++) {
                Dept d = (Dept) session.newEntity(Dept.class, new Object[]{});
                Emp e = (Emp) session.newEntity(Emp.class, new Object[]{});
                d.setDeptId(i, session);
                d.setDeptName("Department "+i, session);
                d.setDescript("abcdefghijklmn "+i, session);
                e.setEmpId(i, session);
                e.setDeptId(i, session);
                e.setEmpName("John Doe "+i, session);
                e.setDescript("abcdefghijklmn "+i, session);
                session.persist(d);
                session.persist(e);
            }
            session.commit();
            System.out.println("200000 records inserted in both tables");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        execute query in insert/update session
    */
    public void executeQuery() throws Exception {
        ResultSet rs = session.execute("select d.deptName, e.empName, e.descript " +
                "from su.interference.test.entity.Dept d, su.interference.test.entity.Emp e " +
                "where d.deptId = e.deptId");
        Object o  = rs.poll(session);
        while (o != null) {
            final GenericResult r = (GenericResult) o;
            System.out.println(r.getValueByName("ddeptName") + ":" + r.getValueByName("eempName") + ":" + r.getValueByName("edescript"));
            o = rs.poll(session);
        }
    }

    /*
        execute query in another session
    */
    public void executeQuery2() throws Exception {
        ResultSet rs = session2.execute("select d.deptName, e.empName, e.descript " +
                "from su.interference.test.entity.Dept d, su.interference.test.entity.Emp e " +
                "where d.deptId = e.deptId");
        Object o  = rs.poll(session2);
        while (o != null) {
            final GenericResult r = (GenericResult) o;
            System.out.println(r.getValueByName("ddeptName") + ":" + r.getValueByName("eempName") + ":" + r.getValueByName("edescript"));
            o = rs.poll(session2);
        }
    }

    public void executeQuery3() throws Exception {
        ResultSet rs = session2.execute("select d.deptName " +
                "from su.interference.test.entity.Dept d ");
        Object o  = rs.poll(session2);
        while (o != null) {
            final GenericResult r = (GenericResult) o;
            System.out.println(r.getValueByName("ddeptName"));
            o = rs.poll(session2);
        }
    }

    /*
        simply update of all records in Dept
    */
    public void updateDept() throws Exception {
        if (session != null) {
            for (int id = 1; id <= 100000; id++) {
                Dept dept = (Dept) session.find(Dept.class, id);
                dept.setDeptName("Outdoor staff", session);
                session.persist(dept);
            }
        }
    }

    /*
        find() method use indices for fast data access
    */
    public void findDepts() throws Exception {
        for (int id = 1; id <= 100000; id++) {
            Dept dept = (Dept) session.find(Dept.class, id);
            System.out.println(dept.getDeptName(session));
        }
    }

    /*
        find() method use indices for fast data access
    */
    public void findDeptsInAnotherSession() throws Exception {
        for (int id = 1; id <= 100000; id++) {
            Dept dept = (Dept) session2.find(Dept.class, id);
            System.out.println(dept.getDeptName(session2));
        }
    }

    /*
        start stream with simple condition
    */
    public void executeStream() throws Exception {
        exec.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Session session2 = Session.getSession();
                    streamRS = session2.execute("select stream s.id, s.name, s.descript " +
                            "from su.interference.test.entity.StreamTable s where s.descript ='99bbb'");

                    while (((StreamQueue)streamRS).isRunning()) {
                        Object o = streamRS.poll(session2);
                        if (o != null) {
                            System.out.println(o);
                        } else {
                            Thread.sleep(100);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
        start stream with tumbling window groups
    */
    public void executeStream2() throws Exception {
        exec.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Session session2 = Session.getSession();
                    streamRS = session2.execute("select stream count(s.id) cnt, s.name " +
                            "from su.interference.test.entity.StreamTable s group by s.name");

                    while (((StreamQueue)streamRS).isRunning()) {
                        Object o = streamRS.poll(session2);
                        if (o != null) {
                            System.out.println(o);
                        } else {
                            Thread.sleep(100);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
        start stream with sliding window groups
    */
    public void executeStream3() throws Exception {
        exec.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Session session2 = Session.getSession();
                    streamRS = session2.execute("select stream last(s.id) last, count(s.id) cnt, sum(s.id) " +
                            "from su.interference.test.entity.StreamTable s window by s.id interval = 100");

                    while (((StreamQueue)streamRS).isRunning()) {
                        Object o = streamRS.poll(session2);
                        if (o != null) {
                            System.out.println(o);
                        } else {
                            Thread.sleep(100);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
        insert 10000 records to stream
    */
    public void insertStream() {
        for (int i=0; i<100; i++) {
            for (int j=0; j<100; j++) {
                Object[] params = new Object[]{0, i+"aaaaaaaaaaaaaaaaaaaaaaa", j+"bbb"};
                try {
                    StreamTable st = (StreamTable) session.newEntity(StreamTable.class, params);
                    session.persist(st);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //need for push to stream immediatelly
        session.commit();
        System.out.println("10000 record(s) inserts to stream");
    }

    /*
        close last started stream
    */
    public void closeStream() {
        if (streamRS != null) {
            ((StreamQueue)streamRS).stop();
        }
    }

    /*
        commit transaction
    */
    public void commit() throws Exception {
        if (session != null) {
            session.commit();
        }
    }

    /*
        rollback transaction
    */
    public void rollback() throws Exception {
        if (session != null) {
            session.rollback();
        }
    }

}