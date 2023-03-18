/*
 *  ====================================================================
 *    Licensed to the Apache Software Foundation (ASF) under one or more
 *    contributor license agreements.  See the NOTICE file distributed with
 *    this work for additional information regarding copyright ownership.
 *    The ASF licenses this file to You under the Apache License, Version 2.0
 *    (the "License"); you may not use this file except in compliance with
 *    the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * ====================================================================
 */

package org.apache.poi.xssf.streaming;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.PrimitiveIterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.CodepointsUtil;
import org.apache.poi.util.Removal;
import org.apache.poi.util.TempFile;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellType;

public class SheetDataWriterImproved extends SheetDataWriter {

    public SheetDataWriterImproved() throws IOException
    {
    }

    public SheetDataWriterImproved(Writer writer) throws IOException
    {
        super(writer);
    }

    public SheetDataWriterImproved(SharedStringsTable sharedStringsTable) throws IOException
    {
        super(sharedStringsTable);
    }

    protected void outputEscapedString(String s) throws IOException {
        if (s == null || s.length() == 0) {
            return;
        }

        int codepoint;
        for (PrimitiveIterator.OfInt iter = CodepointsUtil.primitiveIterator(s); iter.hasNext(); ) {
            codepoint = iter.nextInt();
            switch (codepoint) {
                case '<':
                    _out.write("&lt;");
                    break;
                case '>':
                    _out.write("&gt;");
                    break;
                case '&':
                    _out.write("&amp;");
                    break;
                case '\"':
                    _out.write("&quot;");
                    break;
                // Special characters
                case '\n':
                    _out.write("&#xa;");
                    break;
                case '\r':
                    _out.write("&#xd;");
                    break;
                case '\t':
                    _out.write("&#x9;");
                    break;
                case '\u00A0': // NO-BREAK SPACE
                    _out.write("&#xa0;");
                    break;
                default:
                    final char[] chars = Character.toChars(codepoint);
                    if (chars.length == 1) {
                        char c = chars[0];
                        // YK: XmlBeans silently replaces all ISO control characters ( < 32) with question marks.
                        // the same rule applies to "not a character" symbols.
                        if (replaceWithQuestionMark(c)) {
                            _out.write('?');
                        } else {
                            _out.write(c);
                        }
                    } else {
                        _out.write(chars);
                    }
                    break;
            }
        }
    }
}
