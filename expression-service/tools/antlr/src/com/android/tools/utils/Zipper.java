/*
 * Copyright (C) 2016 The Android Open Source Project
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

package com.android.tools.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@SuppressWarnings("ALL")
public class Zipper {

    // Matches the construction bazel uses to generate jar files.
    private static final long DOS_EPOCH = new GregorianCalendar(
            1980,
            Calendar.JANUARY,
            1,
            0,
            0,
            0
    ).getTimeInMillis();
    private final byte[] mBuffer = new byte[1024 * 1024];

    private static ZipEntry createZipEntry(String entryName) {
        final ZipEntry entry = new ZipEntry(entryName);
        // Bazel adds two seconds (the minimum delta representable in some file systems)
        // to .jars, we use the exact same delta.
        entry.setTime(DOS_EPOCH + 2);
        return entry;
    }

    /**
     * Moves a directory into a .zip. All the files are added relative to the given directory.
     * The directory itself is also deleted.
     *
     * @param directory the directory whose files will be deleted and moved into a zip.
     * @param file      the zip file to create.
     */
    public void directoryToZip(File directory, File file) throws IOException {
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file))) {
            final File[] files = directory.listFiles();
            if (files != null) {
                for (final File child : files) {
                    addFileToZip(child, out, child.getName(), true);
                }
            }
            //noinspection ResultOfMethodCallIgnored
            directory.delete();
        }
    }

    public void addFileToZip(
            File file,
            ZipOutputStream out,
            String name,
            boolean delete
    ) throws IOException {
        final ZipEntry entry = createZipEntry(file.isDirectory() ? name + "/" : name);
        if (file.isDirectory()) {
            out.putNextEntry(entry);
            out.closeEntry();
            final File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (final File child : files) {
                addFileToZip(child, out, name + "/" + child.getName(), delete);
            }
        } else {
            out.putNextEntry(entry);
            try (final FileInputStream in = new FileInputStream(file)) {
                copy(in, out);
            }
            out.closeEntry();
        }
        if (delete) {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }

    private void copy(InputStream in, OutputStream out) throws IOException {
        int k;
        while ((k = in.read(mBuffer)) != -1) {
            out.write(mBuffer, 0, k);
        }
    }
}
