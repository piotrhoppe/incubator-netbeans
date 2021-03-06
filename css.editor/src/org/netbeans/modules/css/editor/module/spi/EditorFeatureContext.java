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
package org.netbeans.modules.css.editor.module.spi;

import javax.swing.text.Document;
import org.netbeans.modules.css.lib.api.CssParserResult;

/**
 * Context for editor based features
 *
 * @author mfukala@netbeans.org
 */
public class EditorFeatureContext extends FeatureContext {

    private int caretOffset;

    public EditorFeatureContext(CssParserResult result, int caretOffset) {
        super(result);
        this.caretOffset = caretOffset;
    }
    
    /**
     * @return The editor's caret offset relative to the edited document.
     */
    public int getCaretOffset() {
        return caretOffset;
    }
    
    /**
     * @return instance of the edited document
     */
    public Document getDocument() {
        return getSnapshot().getSource().getDocument(false);
    }
}
