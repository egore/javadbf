/*

(C) Copyright 2017 Christoph Brill <egore911@gmail.com>

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 3.0 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library.  If not, see <http://www.gnu.org/licenses/>.

*/
package com.linuxense.javadbf.index;

public class MDXTag {

    public static byte KEY_FORMAT_CALCULATED = 0x00;
    public static byte KEY_FORMAT_DATAFIELD = 0x10;

    public static byte KEY_TYPE_CHARACTER = 'C';
    public static byte KEY_TYPE_NUMERICAL = 'N';
    public static byte KEY_TYPE_DATE = 'D';

    public int pageNumber;                    /* 0-3 */
    public String tagName;                    /* 4-14 */
    public byte keyFormat;                    /* 15 */
    public byte forwardTagLeft;               /* 16 */
    public byte forwardTagRight;              /* 17 */
    public byte backwardTag;                  /* 18 */
    public short reserved1;                   /* 19 */
    public byte keyType;                      /* 20 */
    public byte[] reserved2 = new byte[11];   /* 21-31 */
    public byte[] data;                       /* 32-X */
    public Header header;

    public MDXTag(int datasize) {
        data = new byte[datasize];
    }
    public static class Header {

        public static byte KEY_FORMAT_RIGHT_LEFT_DTOC = 0x00;
        public static byte KEY_FORMAT_DESCENDING = 0x08;
        public static byte KEY_FORMAT_FIELDS_STRING = 0x10;
        public static byte KEY_FORMAT_UNIQUE = 0x40;

        public int rootPage;                    /* 1-3 */
        public int fileSizeInPages;             /* 4-7 */
        public byte keyFormat;                  /* 8 */
        public byte keyType;                    /* 9 */
        public short reserved1;                 /* 10-11 */
        public short indexKeyLength;            /* 12-13 */
        public short maxsKeysPerPage;           /* 14-15 */
        public short keyTypeSecondary;          /* 16-17 */
        public short keyIndexItemLength;        /* 18-19 */
        public byte[] reserved2 = new byte[3];  /* 20-22 */
        public byte unique;                     /* 23 */
        public String keyDefinition;            /* 24-125 */
        public String forExpression;            /* 126-227 */
        public byte[] data;                /* 228-x */

        public Header(short blockSizeAdderN) {
            data = new byte[blockSizeAdderN-226];
        }
    }
}
