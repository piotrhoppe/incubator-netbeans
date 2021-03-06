/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.netbeans.lib.editor.util.swing;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.netbeans.junit.NbTestCase;
import org.openide.util.Exceptions;

public class DocumentUtilitiesTest extends NbTestCase {

    public DocumentUtilitiesTest(String testName) {
        super(testName);
    }

    public void testDebugOffset() throws Exception {
        PlainDocument doc = new PlainDocument(); // tabSize is 8
        //                   0123 45 678 90123456 789
        doc.insertString(0, "abc\na\tbc\nabcdefg\thij", null);
        assertEquals("0[1:1]", DocumentUtilities.debugOffset(doc, 0));
        assertEquals("5[2:2]", DocumentUtilities.debugOffset(doc, 5));
        assertEquals("6[2:9]", DocumentUtilities.debugOffset(doc, 6));
        assertEquals("7[2:10]", DocumentUtilities.debugOffset(doc, 7));
        assertEquals("16[3:8]", DocumentUtilities.debugOffset(doc, 16));
        assertEquals("17[3:9]", DocumentUtilities.debugOffset(doc, 17));
        assertEquals("19[3:11]", DocumentUtilities.debugOffset(doc, 19));
    }

    public void testIsReadLocked() throws Exception {
        PlainDocument doc = new PlainDocument();
        assertFalse(DocumentUtilities.isReadLocked(doc));
        doc.readLock();
        try {
            assertTrue(DocumentUtilities.isReadLocked(doc));
        } finally {
            doc.readUnlock();
        }
    }

    public void testGetText() throws Exception {
        PlainDocument doc = new PlainDocument();
        CharSequence text = DocumentUtilities.getText(doc);
        assertEquals(1, text.length());
        assertEquals('\n', text.charAt(0));

        text = DocumentUtilities.getText(doc);
        doc.insertString(0, "a\nb", null);
        for (int i = 0; i < doc.getLength() + 1; i++) {
            assertEquals(doc.getText(i, 1).charAt(0), text.charAt(i));
        }
    }
    
    public void testIsWriteLocked() throws Exception {
        PlainDocument doc = new PlainDocument();
        assertFalse(DocumentUtilities.isWriteLocked(doc));
        doc.addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent evt) {
                assertTrue(DocumentUtilities.isWriteLocked(evt.getDocument()));
            }
            public void removeUpdate(DocumentEvent evt) {
            }
            public void changedUpdate(DocumentEvent evt) {
            }
        });
        doc.insertString(0, "test", null);
    }

    public void testInsertedTextIsPresent() throws Exception {
        final PlainDocument doc = new PlainDocument();
        final CountDownLatch insertDone = new CountDownLatch(1);
        final CountDownLatch removeDone = new CountDownLatch(1);

        doc.addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent evt) {
                insertDone.countDown();
                try {
                    removeDone.await(1, TimeUnit.SECONDS);
                } catch (InterruptedException ex) {
                    throw new IllegalStateException(ex);
                }

                try {
                    String insertedText = evt.getDocument().getText(evt.getOffset(), evt.getLength());
                } catch (BadLocationException ex) {
                    throw new IllegalStateException(
                            "Inserted text not present in document !!! docLen=" + doc.getLength(), ex);
                }
            }
            public void removeUpdate(DocumentEvent evt) {
            }
            public void changedUpdate(DocumentEvent evt) {
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    insertDone.await();
                    doc.remove(0, doc.getLength());
                    removeDone.countDown();
                } catch (Exception ex) {
                    throw new IllegalStateException(ex);
                }
            }
        }).start();

        doc.insertString(0, "Hello", null);
        
    }

}
