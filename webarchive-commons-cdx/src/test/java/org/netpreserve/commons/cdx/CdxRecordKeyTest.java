/*
 * Copyright 2016 IIPC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.netpreserve.commons.cdx;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 *
 */
public class CdxRecordKeyTest {

    /**
     * Test of getUriKey and getTimeStamp methods, of class CdxRecordKey.
     */
    @Test
    public void testGetUriKeyAndGetTimeStamp() {
        String uriKey = "abc";
        String timeStamp = "123";
        String recordType = "response";

        CdxRecordKey instance = new CdxRecordKey(uriKey, timeStamp, recordType);
        assertThat(instance.getUriKey()).hasToString(uriKey);
        assertThat(instance.getTimeStamp()).hasToString(timeStamp);
        assertThat(instance.getRecordType()).hasToString(recordType);

        instance = new CdxRecordKey(uriKey, timeStamp, recordType);
        assertThat(instance.getUriKey()).hasToString(uriKey);
        assertThat(instance.getTimeStamp()).hasToString(timeStamp);
        assertThat(instance.getRecordType()).hasToString(recordType);
    }

    /**
     * Test of compareTo method, of class CdxRecordKey.
     */
    @Test
    public void testCompareTo() {
        String uriKey1 = "abc";
        String timeStamp1 = "20160214";
        String recordType = "response";

        // Check that two records created from same source are equal
        CdxRecordKey instance1 = new CdxRecordKey(uriKey1, timeStamp1, recordType);
        CdxRecordKey instance2 = new CdxRecordKey(uriKey1, timeStamp1, recordType);
        assertThat(instance1).isEqualByComparingTo(instance2);

        instance1 = new CdxRecordKey(uriKey1, timeStamp1, recordType);
        instance2 = new CdxRecordKey(uriKey1, timeStamp1, recordType);
        assertThat(instance1).isEqualByComparingTo(instance2);

        // Check less and greater than
        instance1 = new CdxRecordKey("abc", "20160213", recordType);
        instance2 = new CdxRecordKey("abcd", "20160212", recordType);
        CdxRecordKey instance3 = new CdxRecordKey("abc", "20160214", recordType);

        assertThat(instance1).isLessThan(instance2);
        assertThat(instance1).isLessThan(instance3);
        assertThat(instance3).isLessThan(instance2);

        assertThat(instance2).isGreaterThan(instance1);
        assertThat(instance3).isGreaterThan(instance1);
        assertThat(instance2).isGreaterThan(instance3);
    }

}