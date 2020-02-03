/**
 The MIT License (MIT)

 Copyright (c) 2010-2019 head systems, ltd

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

import su.interference.mgmt.MgmtColumn;
import su.interference.exception.CannotAccessToLockedRecord;
import su.interference.persistent.Session;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Yuriy Glotanov
 * @since 1.0
 */

@Entity
@Table(name="Emp", indexes={@Index(name="EmpDeptKey", columnList="deptId", unique=false)})
public class Emp {
    @Column
    @Id
    @GeneratedValue
    @MgmtColumn(width=10, show=true, form=false, edit=false)
    private int empId;
    @Column
    @MgmtColumn(width=40, show=true, form=true, edit=true)
    private String empName;
    @Column
    @MgmtColumn(width=50, show=true, form=false, edit=true)
    private int deptId;
    @Column
    @MgmtColumn(width=10, show=true, form=false, edit=true)
    private int empCode;
    @Column
    @MgmtColumn(width=50, show=true, form=false, edit=true)
    private String descript;
    @Column
    @MgmtColumn(width=50, show=true, form=false, edit=true)
    private Date createDate;

    public Emp () {

    }

    public Emp(java.lang.Integer empId, String empName, java.lang.Integer deptId, String descript, java.lang.Integer empCode, Date createDate) {
        this.empId = empId;
        this.empName = empName;
        this.deptId = deptId;
        this.empCode = empCode;
        this.descript = descript;
        this.createDate = createDate;
    }

    public int getEmpId(Session s) {
        return empId;
    }

    public void setEmpId(int empId, Session s) throws CannotAccessToLockedRecord {
        this.empId = empId;
    }

    public String getEmpName(Session s) {
        return empName;
    }

    public void setEmpName(String empName, Session s) throws CannotAccessToLockedRecord {
        this.empName = empName;
    }

    public int getDeptId(Session s) {
        return deptId;
    }

    public void setDeptId(int deptId, Session s) throws CannotAccessToLockedRecord {
        this.deptId = deptId;
    }

    public int getEmpCode(Session s) {
        return empCode;
    }

    public void setEmpCode(int empCode, Session s) throws CannotAccessToLockedRecord {
        this.empCode = empCode;
    }

    public String getDescript(Session s) {
        return descript;
    }

    public void setDescript(String descript, Session s) throws CannotAccessToLockedRecord {
        this.descript = descript;
    }

    public Date getCreateDate(Session s) {
        return createDate;
    }

    public void setCreateDate(Date createDate, Session s) throws CannotAccessToLockedRecord {
        this.createDate = createDate;
    }

}
