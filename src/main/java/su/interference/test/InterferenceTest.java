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
import su.interference.proxy.GenericResult;
import su.interference.sql.ResultSet;
import su.interference.test.entity.Dept;
import su.interference.test.entity.Emp;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.List;

/**
 * @author Yuriy Glotanov
 * @since 1.0
 */

public class InterferenceTest implements InterferenceTestMBean {

    private static Instance instance;
    private static Session session;
    private static Session session2;
    private Dept dept;

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
                Dept d = (Dept) session.newEntity(Dept.class, new Object[]{i, "Sales Department "+i, "abcdefghijklmn "+i});
                Emp e = (Emp) session.newEntity(Emp.class, new Object[]{i, "John Doe "+i, i, "Sales manager "+i, 43286 + i, new Date()});
                session.persist(d);
                session.persist(e);
            }
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
        List<Object> list = rs.getAll(session, 0);
        for (Object o : list) {
            GenericResult r = (GenericResult) o;
            System.out.println((String)r.getValueByName("ddeptName") + ":" + (String)r.getValueByName("eempName") + ":" + (String)r.getValueByName("edescript"));
        }
    }

    // execute query in another session
    public void executeQuery2() throws Exception {
        ResultSet rs = session2.execute("select d.deptName, e.empName, e.descript " +
                "from su.interference.test.entity.Dept d, " +
                "su.interference.test.entity.Emp e " +
                "where d.deptId = e.deptId");
        List<Object> list = rs.getAll(session2, 0);
        for (Object o : list) {
            GenericResult r = (GenericResult) o;
            System.out.println((String)r.getValueByName("ddeptName") + ":" + (String)r.getValueByName("eempName") + ":" + (String)r.getValueByName("edescript"));
        }
    }

    public void updateDept() throws Exception {
        if (session != null) {
            for (int i = 1; i <= 10000; i++) {
                Dept d = (Dept) session.find(Dept.class, i);
                d.setDeptName("Outdoor staff "+i, session);
                session.persist(d);
            }
        }
    }

    public void findDept() throws  Exception {
        if (session2 == null) {
            session2 = Session.getSession();
        }
        dept = (Dept)session2.find(Dept.class, 1);
    }

    public void printDeptName() throws  Exception {
        System.out.println("deptName = " + dept.getDeptName(session2));
    }

    public void commit() throws Exception {
        if (session != null) {
            session.commit();
        }
    }

    public void rollback() throws Exception {
        if (session != null) {
            session.rollback();
        }
    }

}