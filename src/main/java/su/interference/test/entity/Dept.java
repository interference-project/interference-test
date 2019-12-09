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

import su.interference.exception.CannotAccessToLockedRecord;
import su.interference.mgmt.MgmtColumn;
import su.interference.persistent.Session;

import javax.persistence.*;
import javax.persistence.Table;

/**
 * @author Yuriy Glotanov
 * @since 1.0
 */

@Entity
@Table(name="Dept", indexes={@Index(name="DeptPk", columnList="deptId", unique=true)})
public class Dept {
    @Column
    @Id
    //@GeneratedValue
    @MgmtColumn(width=10, show=true, form=false, edit=false)
    private int deptId;
    @Column
    @MgmtColumn(width=40, show=true, form=true, edit=true)
    private String deptName;
    @Column
    @MgmtColumn(width=50, show=true, form=false, edit=true)
    private String descript;

    public Dept() {

    }

    public Dept(java.lang.Integer id, String name, String desc) {
        this.deptId   = id;
        this.deptName = name;
        this.descript = desc;
    }

    public int getDeptId(Session s) {
        return deptId;
    }

    public void setDeptId(int deptId, Session s) throws CannotAccessToLockedRecord {
        this.deptId = deptId;
    }

    public String getDeptName(Session s) {
        return deptName;
    }

    public void setDeptName(String deptName, Session s) throws CannotAccessToLockedRecord {
        this.deptName = deptName;
    }

    public String getDescript(Session s) {
        return descript;
    }

    public void setDescript(String descript, Session s) throws CannotAccessToLockedRecord {
        this.descript = descript;
    }

}
