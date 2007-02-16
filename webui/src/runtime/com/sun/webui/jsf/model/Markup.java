/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License).  You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at
 * https://woodstock.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at https://woodstock.dev.java.net/public/CDDLv1.0.html.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 */
 /*
 * $Id: Markup.java,v 1.1 2007-02-16 01:31:12 bob_yennaco Exp $
 */

package com.sun.webui.jsf.model;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;


/**
 * <p>Utility bean that serves as an accumulating buffer for
 * well formed markup fragments typically generated by renderers.
 * The fundamental API is modelled after <code>ResponseWriter</code>
 * in JavaServer Faces.</p>
 */

public class Markup {


    // ------------------------------------------------------- Static Variables


    /*
     * <p>Entities from HTML 4.0, section 24.2.1;
     * character codes 0xA0 to 0xFF</p>
     */
    private static String[] ISO8859_1_Entities = new String[]{
        "nbsp",
        "iexcl",
        "cent",
        "pound",
        "curren",
        "yen",
        "brvbar",
        "sect",
        "uml",
        "copy",
        "ordf",
        "laquo",
        "not",
        "shy",
        "reg",
        "macr",
        "deg",
        "plusmn",
        "sup2",
        "sup3",
        "acute",
        "micro",
        "para",
        "middot",
        "cedil",
        "sup1",
        "ordm",
        "raquo",
        "frac14",
        "frac12",
        "frac34",
        "iquest",
        "Agrave",
        "Aacute",
        "Acirc",
        "Atilde",
        "Auml",
        "Aring",
        "AElig",
        "Ccedil",
        "Egrave",
        "Eacute",
        "Ecirc",
        "Euml",
        "Igrave",
        "Iacute",
        "Icirc",
        "Iuml",
        "ETH",
        "Ntilde",
        "Ograve",
        "Oacute",
        "Ocirc",
        "Otilde",
        "Ouml",
        "times",
        "Oslash",
        "Ugrave",
        "Uacute",
        "Ucirc",
        "Uuml",
        "Yacute",
        "THORN",
        "szlig",
        "agrave",
        "aacute",
        "acirc",
        "atilde",
        "auml",
        "aring",
        "aelig",
        "ccedil",
        "egrave",
        "eacute",
        "ecirc",
        "euml",
        "igrave",
        "iacute",
        "icirc",
        "iuml",
        "eth",
        "ntilde",
        "ograve",
        "oacute",
        "ocirc",
        "otilde",
        "ouml",
        "divide",
        "oslash",
        "ugrave",
        "uacute",
        "ucirc",
        "uuml",
        "yacute",
        "thorn",
        "yuml"
    };


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>Buffer into which we accumulate the created markup.</p>
     */
    private StringBuffer buffer = new StringBuffer();


    /**
     * <p>The character encoding that we assume will be used when
     * the markup contained in this instance is rendered.  The
     * default value ("ISO-8859-1") is an attempt to be conservative.</p>
     */
    private String encoding = "ISO-8859-1";


    /**
     * <p>Flag indicating that an element is currently open.</p>
     */
    private boolean open = false;


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the character encoding assumed to be used when the
     * markup contained in this instance is ultimately rendered.</p>
     */
    public String getEncoding() {

        return this.encoding;

    }


    /**
     * <p>Set the character encoding assumed to be used when the
     * markup contained in this instance is ultimately rendered.</p>
     *
     * @param encoding The new character encoding
     */
    public void setEncoding(String encoding) {

        this.encoding = encoding;

    }


    /**
     * <p>Return the markup that has been accumulated in this element,
     * as a String suitable for direct transcription to the response
     * buffer.</p>
     */
    public String getMarkup() {

        close();
        return buffer.toString();

    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Clear any accumulated markup stored in this object,
     * making it suitable for reuse.</p>
     */
    public void clear() {

        buffer.setLength(0);
        open = false;

    }


    /**
     * <p>Return the markup that has been accumulated in this element.
     * This is an alias for the <code>getMarkup()</code> method.</p>
     */
    public String toString() {

        return getMarkup();

    }


    /**
     * <p>Accumulate the start of a new element, up to and including
     * the element name.  Once this method has been called, clients
     * can call <code>writeAttribute()</code> or
     * <code>writeURIAttriute()</code> to add attributes and their
     * corresponding values.  The starting element will be closed
     * on any subsequent call to <code>startElement()</code>,
     * <code>writeComment()</code>, <code>writeText()</code>,
     * <code>writeRaw()</code>, <code>endElement()</code>, or
     * <code>getMarkup()</code>.</p>
     *
     * @param name Name of the element to be started
     * @param component The <code>UIComponent</code> (if any)
     *  to which this element corresponds
     *
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public void startElement(String name, UIComponent component) {

        if (name == null) {
            throw new NullPointerException();
        }
        close();
        buffer.append('<'); //NOI18N
        buffer.append(name);
        open = true;

    }


    /**
     * <p>Accumulate the end of an element, after closing any open element
     * created by a call to <code>startElement()</code>.  Elements must be
     * closed in the inverse order from which they were opened; it is an
     * error to do otherwise.</p>
     *
     * @param name Name of the element to be ended
     *
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public void endElement(String name) {

        if (name == null) {
            throw new NullPointerException();
        }
        if (open) {
            buffer.append('/'); //NOI18N
            close();
        } else {
            buffer.append("</"); //NOI18N
            buffer.append(name);
            buffer.append('>'); //NOI18N
        }

    }


    /**
     * <p>Accumulate an attribute name and corresponding value.  This
     * method may only be called after a call to <code>startElement()</code>
     * and before the opened element has been closed.</p>
     *
     * @param name Attribute name to be added
     * @param value Attribute value to be added
     * @param property Name of the component property or attribute (if any)
     *  of the <code>UIComponent</code> associated with the containing
     *  element, to which the generated attribute corresponds
     *
     * @exception IllegalStateException if this method is called
     *  when there is no currently open element
     * @exception NullPointerException if <code>name</code>
     *  or <code>value</code> is <code>null</code>
     */
    public void writeAttribute(String name, Object value, String property) {

        if ((name == null) || (value == null)) {
            throw new NullPointerException();
        }
        if (!open) {
            throw new IllegalStateException
                ("No element is currently open"); //I18N - FIXME
        }

        // Handle boolean values specially
        Class clazz = value.getClass();
        if (clazz == Boolean.class) {
            if (Boolean.TRUE.equals(value)) {
                // No attribute minimization for XHTML like markup
                buffer.append(' '); //NOI18N
                buffer.append(name);
                buffer.append("=\""); //NOI18N
                buffer.append(name);
                buffer.append('"'); //NOI18N
            // } else {
                // Write nothing for false boolean attributes
            }
            return;
        }

        // Render the attribute name and beginning of the value
        buffer.append(' '); //NOI18N
        buffer.append(name);
        buffer.append("=\""); //NOI18N

        // Render the value itself
        String text = value.toString();
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char ch = text.charAt(i);
            
            // Tilde or less...
            if (ch < 0xA0) {
                // If "?" or over, no escaping is needed (this covers
                // most of the Latin alphabet)
                if (ch >= 0x3f) {
                    buffer.append(ch);
                } else if (ch >= 0x27) { // If above "'"...
                    // If between "'" and ";", no escaping is needed
                    if (ch < 0x3c) {
                        buffer.append(ch);
                        // Note - "<" isn't escaped in attributes, as per
                        // HTML spec
                    } else if (ch == '>') { //NOI18N
                        buffer.append("&gt;"); //NOI18N
                    } else {
                        buffer.append(ch);
                    }
                } else {
                    if (ch == '&') { //NOI18N
                        // HTML 4.0, section B.7.1: ampersands followed by
                        // an open brace don't get escaped
                        if ((i + 1 < length) && (text.charAt(i + 1) == '{')) //NOI18N
                            buffer.append(ch);
                        else
                            buffer.append("&amp;"); //NOI18N
                    } else if (ch == '"') {
                        buffer.append("&quot;"); //NOI18N
                    } else {
                        buffer.append(ch);
                    }
                }
            } else if (ch <= 0xff) {
                // ISO-8859-1 entities: encode as needed
                buffer.append('&'); //NOI18N
                buffer.append(ISO8859_1_Entities[ch - 0xA0]);
                buffer.append(';'); //NOI18N
            } else {
                // Double-byte characters to encode.
                // PENDING: when outputting to an encoding that
                // supports double-byte characters (UTF-8, for example),
                // we should not be encoding
                numeric(ch);
            }
        }

        // Render the end of the value
        buffer.append('"'); //NOI18N

    }


    /**
     * <p>Accumulate an attribute name and corresponding URI value.  This
     * method may only be called after a call to <code>startElement()</code>
     * and before the opened element has been closed.</p>
     *
     * @param name Attribute name to be added
     * @param value Attribute value to be added
     * @param property Name of the component property or attribute (if any)
     *  of the <code>UIComponent</code> associated with the containing
     *  element, to which the generated attribute corresponds
     *
     * @exception IllegalStateException if this method is called
     *  when there is no currently open element
     * @exception NullPointerException if <code>name</code>
     *  or <code>value</code> is <code>null</code>
     */
    public void writeURIAttribute(String name, Object value, String property) {

        if ((name == null) || (value == null)) {
            throw new NullPointerException();
        }
        if (!open) {
            throw new IllegalStateException
                ("No element is currently open"); //I18N - FIXME
        }

        String text = value.toString();
        if (text.startsWith("javascript:")) {
            writeAttribute(name, value, property);
            return;
        }

        // Render the attribute name and beginning of the value
        buffer.append(' '); //NOI18N
        buffer.append(name);
        buffer.append("=\""); //NOI18N

        // Render the value itself
        int length = text.length();

        for (int i = 0; i < length; i++) {
            char ch = text.charAt(i);

            if ((ch < 33) || (ch > 126)) {
                if (ch == ' ') { //NOI18N
                    buffer.append('+'); //NOI18N
                } else {
                    // ISO-8859-1.  Blindly assume the character will be < 255.
                    // Not much we can do if it isn't.
                    hexadecimals(ch);
                }
            }
            // DO NOT encode '%'.  If you do, then for starters,
            // we'll double-encode anything that's pre-encoded.
            // And, what's worse, there becomes no way to use
            // characters that must be encoded if you
            // don't want them to be interpreted, like '?' or '&'.
            // else if('%' == ch)
            // {
            //   hexadecimals(ch);
            // } 
            else if (ch == '"') {
                buffer.append("%22"); //NOI18N
            }
            // Everything in the query parameters will be decoded
            // as if it were in the request's character set.  So use
            // the real encoding for those!
            else if (ch == '?') { //NOI18N
                buffer.append('?'); //NOI18N
                try {
                    buffer.append
                        (URLEncoder.encode(text.substring(i + 1), encoding));
                } catch (UnsupportedEncodingException e) {
                    throw new FacesException(e);
                }
                break;
            } else {
                buffer.append(ch);
            }
        }

        // Render the end of the value
        buffer.append('"'); //NOI18N

    }


    /**
     * <p>Accumulate a comment containing the specified text, after
     * converting that text to a String (if necessary) and performing
     * any escaping appropriate for the markup language being rendered.</p>
     *
     * <p>If there is an open element that has been created by a call to
     * <code>startElement()</code>, that element will be closed first.</p>
     *
     * @param comment Text content of the comment
     *
     * @exception NullPointerException if <code>comment</code>
     *  is <code>null</code>
     */
    public void writeComment(Object comment) {

        if (comment == null) {
            throw new NullPointerException();
        }
        close();
        buffer.append("<!-- "); //NOI18N
        buffer.append(comment); // FIXME - filtering?
        buffer.append(" -->"); //NOI18N

    }


    /**
     * <p>Accumulate an object, after converting it to a String (if necessary)
     * <strong>WITHOUT</strong> performing escaping appropriate for the
     * markup language being rendered.</p>
     * <p>If there is an open element that has been created by a call to
     * <code>startElement()</code>, that element will be closed first.</p>
     *
     * @param raw Raw content to be written
     * @param property Name of the component property or attribute (if any)
     *  of the <code>UIComponent</code> associated with the containing
     *  element, to which the generated content corresponds
     *
     * @exception NullPointerException if <code>text</code>
     *  is <code>null</code>
     */
    public void writeRaw(Object raw, String property) {

        if (raw == null) {
            throw new NullPointerException();
        }
        close();
        buffer.append(raw.toString());

    }


    /**
     * <p>Accumulate an object, after converting it to a String (if necessary)
     * and after performing any escaping appropriate for the markup
     * language being rendered.</p>
     *
     * <p>If there is an open element that has been created by a call to
     * <code>startElement()</code>, that element will be closed first.</p>
     *
     * @param text Text to be written
     * @param property Name of the component property or attribute (if any)
     *  of the <code>UIComponent</code> associated with the containing
     *  element, to which the generated attribute corresponds
     *
     * @exception NullPointerException if <code>text</code>
     *  is <code>null</code>
     */
    public void writeText(Object text, String property) {

        if (text == null) {
            throw new NullPointerException();
        }
        // Close any open element
        close();

        // Render the filtered version of the specified text
        String stext = text.toString();
        int length = stext.length();

        for (int i = 0; i < length; i++) {
            char ch = stext.charAt(i);
            
            // Tilde or less...
            if (ch < 0xA0) {
                // If "?" or over, no escaping is needed (this covers
                // most of the Latin alphabet)
                if (ch >= 0x3f) {
                    buffer.append(ch);
                } else if (ch >= 0x27) {  // If above "'"...
                    // If between "'" and ";", no escaping is needed
                    if (ch < 0x3c) {
                        buffer.append(ch);
                    } else if (ch == '<') {
                        buffer.append("&lt;"); //NOI18N
                    } else if (ch == '>') {
                        buffer.append("&gt;"); //NOI18N
                    } else {
                        buffer.append(ch);
                    }
                } else {
                    if (ch == '&') {
                        buffer.append("&amp;"); //NOI18N
                    } else {
                        buffer.append(ch);
                    }
                }
            } else if (ch <= 0xff) {
                // ISO-8859-1 entities: encode as needed 
                buffer.append('&'); //NOI18N
                buffer.append(ISO8859_1_Entities[ch - 0xA0]);
                buffer.append(';'); //NOI18N
            } else {
                // Double-byte characters to encode.
                // PENDING: when outputting to an encoding that
                // supports double-byte characters (UTF-8, for example),
                // we should not be encoding
                numeric(ch);
            }
        }

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Close the currently open starting element, if any.</p>
     */
    protected void close() {

        if (open) {
            buffer.append('>');
            open = false;
        }

    }


    /**
     * <p>Append the hexadecimal equivalent of the specified
     * numeric value.</p>
     */
    protected void hexadecimal(int i) {

        if (i < 10) {
            buffer.append((char) ('0' + i));
        } else {
            buffer.append((char) ('A' + (i - 10)));
        }

    }


    /**
     * <p>Append the specified character as an escaped two-hex-digit value.</p>
     *
     * @param ch Character to be escaped
     */
    protected void hexadecimals(char ch) {

        buffer.append('%'); //NOI18N
        hexadecimal( (int) ((ch >> 4) % 0x10) );
        hexadecimal( (int) (ch % 0x10) );

    }


    /**
     * <p>Append a numeric escape for the specified character.</p>
     *
     * @param ch Character to be escaped
     */
    protected void numeric(char ch) {

        if (ch == '\u20ac') { //NOI18N
            buffer.append("&euro;"); //NOI18N
            return;
        }

        // Formerly used String.valueOf().  This version tests out
        // about 40% faster in a microbenchmark (and on systems where GC is
        // going gonzo, it should be even better)
        int i = (int) ch;
        if (i > 10000) {
            buffer.append('0' + (i / 10000));
            i = i % 10000;
            buffer.append('0' + (i / 1000));
            i = i % 1000;
            buffer.append('0' + (i / 100));
            i = i % 100;
            buffer.append('0' + (i / 10));
            i = i % 10;
            buffer.append('0' + i);
        } else if (i > 1000) {
            buffer.append('0' + (i / 1000));
            i = i % 1000;
            buffer.append('0' + (i / 100));
            i = i % 100;
            buffer.append('0' + (i / 10));
            i = i % 10;
            buffer.append('0' + i);
        } else {
            buffer.append('0' + (i / 100));
            i = i % 100;
            buffer.append('0' + (i / 10));
            i = i % 10;
            buffer.append('0' + i);
        }
        buffer.append(';'); //NOI18N

    }


}
