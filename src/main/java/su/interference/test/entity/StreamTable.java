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

/**
 * @author Yuriy Glotanov
 * @since 1.0
 */

@Entity
@Table(name="StreamTable", indexes={@Index(name="Pk", columnList="id", unique=true)})
public class StreamTable {
    @Column
    @Id
    @GeneratedValue
    @MgmtColumn(width=10, show=true, form=false, edit=false)
    private int id;
    @Column
    @MgmtColumn(width=40, show=true, form=true, edit=true)
    private String name;
    @Column
    @MgmtColumn(width=50, show=true, form=false, edit=true)
    private String descript;

    public StreamTable() {

    }

    public StreamTable(Integer id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.descript = desc;
    }

    public int getId(Session s) {
        return id;
    }

    public void setId(int id, Session s) throws CannotAccessToLockedRecord {
        this.id = id;
    }

    public String getName(Session s) {
        return name;
    }

    public void setName(String name, Session s) throws CannotAccessToLockedRecord {
        this.name = name;
    }

    public String getDescript(Session s) {
        return descript;
    }

    public void setDescript(String descript, Session s) throws CannotAccessToLockedRecord {
        this.descript = descript;
    }

}
