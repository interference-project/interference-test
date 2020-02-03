/**
The MIT License (MIT)

Copyright (c) 2010-2016 interference

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
import su.interference.persistent.Table;
import su.interference.proxy.GenericResult;
import su.interference.sql.ResultSet;
import su.interference.sql.StreamQueue;
import su.interference.test.entity.Dept;
import su.interference.test.entity.Emp;
import su.interference.test.entity.StreamTable;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.List;
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

    public static void main(String[] args) throws Exception {
        InterferenceTest test = new InterferenceTest();
    }

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

    public void loadData() throws Exception {
        try {
            for (int i = 1; i <= 10000; i++) {
                Dept d = (Dept) session.newEntity(Dept.class, new Object[]{0, "Department N"+i, "abcdefghijklmn "+i});
                Emp e = (Emp) session.newEntity(Emp.class, new Object[]{0, "John Doe "+i, d.getDeptId(session), "Sales manager "+i, 43286 + i, new Date()});
                session.persist(d);
                session.persist(e);
            }
            System.out.println("10000 records updated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // execute query in insert/update session
    public void executeQuery() throws Exception {
        ResultSet rs = session.execute("select d.deptName, e.empName, e.descript " +
                "from su.interference.test.entity.Dept d, " +
                "su.interference.test.entity.Emp e " +
                "where d.deptId = e.deptId");
        Object o  = rs.poll(session);
        while (o != null) {
            final GenericResult r = (GenericResult) o;
            System.out.println((String)r.getValueByName("ddeptName") + ":" + (String)r.getValueByName("eempName") + ":" + (String)r.getValueByName("edescript"));
            o = rs.poll(session);
        }
    }

    // execute query in another session
    public void executeQuery2() throws Exception {
        ResultSet rs = session2.execute("select d.deptName, e.empName, e.descript " +
                "from su.interference.test.entity.Dept d, " +
                "su.interference.test.entity.Emp e " +
                "where d.deptId = e.deptId");
        Object o  = rs.poll(session2);
        while (o != null) {
            final GenericResult r = (GenericResult) o;
            System.out.println((String)r.getValueByName("ddeptName") + ":" + (String)r.getValueByName("eempName") + ":" + (String)r.getValueByName("edescript"));
            o = rs.poll(session2);
        }
    }

    public void executeQuery3() throws Exception {
        ResultSet rs = session2.execute("select d.deptName " +
                "from su.interference.test.entity.Dept d ");
        Object o  = rs.poll(session2);
        while (o != null) {
            final GenericResult r = (GenericResult) o;
            System.out.println((String)r.getValueByName("ddeptName"));
            o = rs.poll(session2);
        }
    }

    public void updateDept() throws Exception {
        if (session != null) {
            Table t = Instance.getInstance().getTableByName(Dept.class.getName());
            session.startStatement();
            Dept d = (Dept) t.poll(session);
            int i = 0;
            while (d != null) {
                i++;
                d.setDeptName("Outdoor staff", session);
                session.persist(d);
                d = (Dept) t.poll(session);
            }
            System.out.println(i+" records updated");
        }
    }

    public void findDeptById(int id) throws Exception {
        if (session2 == null) {
            session2 = Session.getSession();
        }
        dept = (Dept)session2.find(Dept.class, id);
    }

    public void printDeptName() throws  Exception {
        System.out.println("deptName = " + dept.getDeptName(session2));
    }

    // start stream with simple condition
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

    // start stream with tumbling window groups
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

    // start stream with sliding window groups
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

    // insert 10000 records to stream
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

    // close last started stream
    public void closeStream() {
        if (streamRS != null) {
            ((StreamQueue)streamRS).stop();
        }
    }

    // commit transaction
    public void commit() throws Exception {
        if (session != null) {
            session.commit();
        }
    }

    // rollback transaction
    public void rollback() throws Exception {
        if (session != null) {
            session.rollback();
        }
    }

}