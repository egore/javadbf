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

import com.linuxense.javadbf.DBFUtils;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class IndexReader {


    public MDXFile readMDX(InputStream inputStream) throws IOException {
        DataInputStream dataInput = new DataInputStream(inputStream);

        MDXFile mdxFile = new MDXFile();
        mdxFile.signature = dataInput.readByte(); /* 0 */
        mdxFile.yearCreated = dataInput.readByte();      /* 1 */
        mdxFile.monthCreated = dataInput.readByte();     /* 2 */
        mdxFile.dayCreated = dataInput.readByte();       /* 3 */
        mdxFile.datafilename = readString(dataInput, 16); /* 4-19 */
        mdxFile.tagSizeMultiplier = DBFUtils.readLittleEndianShort(dataInput); /* 20-21 */
        mdxFile.tagSize = DBFUtils.readLittleEndianShort(dataInput); /* 22-23 */
        mdxFile.production = dataInput.readByte(); /* 24 */
        mdxFile.entriesPerTag = dataInput.readByte(); /* 25 */
        mdxFile.tagLength = dataInput.readByte(); /* 26 */
        mdxFile.reserved1 = dataInput.readByte(); /* 27 */
        mdxFile.tagCount = DBFUtils.readLittleEndianShort(dataInput); /* 28-29 */
        mdxFile.reserved2 = DBFUtils.readLittleEndianShort(dataInput); /* 30-31 */
        mdxFile.numberOfPages = DBFUtils.readLittleEndianInt(dataInput); /* 32-35 */
        mdxFile.firstFreePage = DBFUtils.readLittleEndianInt(dataInput); /* 36-39 */
        mdxFile.blocksAvailable = DBFUtils.readLittleEndianInt(dataInput); /* 40-43 */
        mdxFile.yearUpdated = dataInput.readByte(); /* 44 */
        mdxFile.monthUpdated = dataInput.readByte(); /* 45 */
        mdxFile.dayUpdated = dataInput.readByte(); /* 46 */
        mdxFile.reserved3 = DBFUtils.readLittleEndianShort(dataInput); /* 47 */
        skip(dataInput, 495);

        for (int i = 0; i < mdxFile.tagCount; i++) {
            MDXTag tag = new MDXTag(mdxFile.tagLength);
            tag.pageNumber = DBFUtils.readLittleEndianInt(dataInput); /* 0-3 */
            tag.tagName = readString(dataInput, 11); /* 4-14 */
            tag.keyFormat = dataInput.readByte(); /* 15 */
            tag.forwardTagLeft = dataInput.readByte(); /* 16 */
            tag.forwardTagRight = dataInput.readByte(); /* 17 */
            tag.backwardTag = dataInput.readByte(); /* 18 */
            tag.reserved1 = dataInput.readByte(); /* 19 */
            tag.keyType = dataInput.readByte(); /* 20 */
            dataInput.readFully(tag.reserved2); /* 21-31 */
            dataInput.readFully(tag.data); /* 32-N */
            mdxFile.tags.add(tag);
        }

        skip(dataInput, (mdxFile.tagSizeMultiplier * mdxFile.tagSize) - 544 - ((mdxFile.tagLength +32) * mdxFile.tagCount));

        for (int i = 0; i < mdxFile.tagCount; i++) {
            MDXTag.Header header = new MDXTag.Header(mdxFile.tagSize);
            header.rootPage = DBFUtils.readLittleEndianInt(dataInput); /* 0-3 */
            header.fileSizeInPages = DBFUtils.readLittleEndianInt(dataInput); /* 4-7 */
            header.keyFormat = dataInput.readByte(); /* 8 */
            header.keyType = dataInput.readByte(); /* 9 */
            header.reserved1 = DBFUtils.readLittleEndianShort(dataInput); /* 10-11 */ /* sql byte, reserved byte */
            header.indexKeyLength = DBFUtils.readLittleEndianShort(dataInput); /* 12-13 */
            header.maxsKeysPerPage = DBFUtils.readLittleEndianShort(dataInput); /* 14-15 */
            header.keyTypeSecondary = DBFUtils.readLittleEndianShort(dataInput); /* 16-17 */
            header.keyIndexItemLength = DBFUtils.readLittleEndianShort(dataInput); /* 18-19 */
            dataInput.readFully(header.reserved2); /* 20-22 */ /* changes short, reserved byte */
            header.unique = dataInput.readByte(); /* 23 */
            header.keyDefinition = readString(dataInput, 101);
            header.forExpression = readString(dataInput, 101);
            dataInput.readFully(header.data);
            mdxFile.tags.get(i).header = header;
        }

        return mdxFile;
    }

    private String readString(DataInput dataInput, int length) throws IOException {
        byte[] filename = new byte[length];
        dataInput.readFully(filename);
        int nullByte = filename.length;
        for (int i = 0; i < filename.length; i++) {
            byte b = filename[i];
            if (b == 0x00) {
                nullByte = i;
                break;
            }
        }
        return new String(filename, 0, nullByte);
    }

    private void skip(DataInputStream dataInput, int bytesToSkip) throws IOException {
        int skipped = (int) dataInput.skip(bytesToSkip);
        for (int i = skipped; i < bytesToSkip; i++) {
            dataInput.readByte();
        }
    }
}