/**
 The MIT License (MIT)

 Copyright (c) 2010-2020 head systems, ltd

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

package su.interference.test.entity;

import su.interference.persistent.Session;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Yuriy Glotanov
 * @since 1.0
 */

@Entity
@Table(name="Emp", indexes={@Index(name="EmpPk", columnList="empId", unique=true),@Index(name="EmpDeptKey", columnList="deptId", unique=false)})
public class Emp {
    @Column
    @Id
    private int empId;
    @Column
    private String empName;
    @Column
    private int deptId;
    @Column
    private String descript;
    @Column
    private Date createDate;

    public Emp () {

    }

    public int getEmpId(Session s) {
        return empId;
    }

    public void setEmpId(int empId, Session s) {
        this.empId = empId;
    }

    public String getEmpName(Session s) {
        return empName;
    }

    public void setEmpName(String empName, Session s) {
        this.empName = empName;
    }

    public int getDeptId(Session s) {
        return deptId;
    }

    public void setDeptId(int deptId, Session s) {
        this.deptId = deptId;
    }

    public String getDescript(Session s) {
        return descript;
    }

    public void setDescript(String descript, Session s) {
        this.descript = descript;
    }

    public Date getCreateDate(Session s) {
        return createDate;
    }

    public void setCreateDate(Date createDate, Session s) {
        this.createDate = createDate;
    }

}
