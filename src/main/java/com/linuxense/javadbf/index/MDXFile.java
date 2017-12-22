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

import java.util.ArrayList;
import java.util.List;

/**
 * Multiple index file (*.mdx). These files contain a file header (which is byte 0-47) plus a padding (byte 48-543).
 * Afterwards they contain so called tags which are split into two parts:
 * <ul>
 * <li>the navigational structure (how to jump from one index to another) for all tags</li>
 * <li>the tag content again split into header information about the tag and the tags content</li>
 * </ul>
 * <p>
 * Loosely based on http://web.tiscali.it/SilvioPitti/
 */
public class MDXFile {

    public byte signature;              /* 0 */
    public byte yearCreated;            /* 1 */
    public byte monthCreated;           /* 2 */
    public byte dayCreated;             /* 3 */
    public String datafilename;         /* 4-19 */
    public short tagSizeMultiplier;     /* 20-21 */
    public short tagSize;               /* 22-23 */
    public byte production;             /* 24 */
    public byte entriesPerTag;          /* 25 */
    public byte tagLength;              /* 26 */
    public byte reserved1;              /* 27 */
    public short tagCount;              /* 28-29 */
    public short reserved2;             /* 30-31 */
    public int numberOfPages;           /* 32-35 */
    public int firstFreePage;           /* 36-39 */
    public int blocksAvailable;         /* 40-43 */
    public byte yearUpdated;            /* 44 */
    public byte monthUpdated;           /* 45 */
    public byte dayUpdated;             /* 46 */
    public short reserved3;             /* 47 */

    public List<MDXTag> tags = new ArrayList<>();
}
