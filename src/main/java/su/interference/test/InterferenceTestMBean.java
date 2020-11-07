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

/**
 * @author Yuriy Glotanov
 * @since 1.0
 */

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
    void findDepts() throws Exception;
    void findDeptsInAnotherSession() throws Exception;

}
