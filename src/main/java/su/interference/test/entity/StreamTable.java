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
    private int id;
    @Column
    private String name;
    @Column
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

    public void setId(int id, Session s) {
        this.id = id;
    }

    public String getName(Session s) {
        return name;
    }

    public void setName(String name, Session s) {
        this.name = name;
    }

    public String getDescript(Session s) {
        return descript;
    }

    public void setDescript(String descript, Session s) {
        this.descript = descript;
    }

}
