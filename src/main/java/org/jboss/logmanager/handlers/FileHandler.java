/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.logmanager.handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import java.util.logging.Formatter;

/**
 * A simple file handler.
 */
public class FileHandler extends OutputStreamHandler {

    private File file;

    /**
     * Construct a new instance with no formatter and no output file.
     */
    public FileHandler() {
    }

    /**
     * Construct a new instance with the given formatter and no output file.
     *
     * @param formatter the formatter
     */
    public FileHandler(final Formatter formatter) {
        super(formatter);
    }

    /**
     * Construct a new instance with the given formatter and output file.
     *
     * @param formatter the formatter
     * @param file the file
     * @throws FileNotFoundException if the file could not be found on open
     */
    public FileHandler(final Formatter formatter, final File file) throws FileNotFoundException {
        super(formatter);
        setFile(file);
    }

    /**
     * Set the output file.
     *
     * @param file the file
     * @throws FileNotFoundException if an error occurs opening the file
     */
    public void setFile(File file) throws FileNotFoundException {
        synchronized (outputLock) {
            if (file == null) {
                setOutputStream(null);
            }
            final File parentFile = file.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            boolean ok = false;
            final FileOutputStream fos = new FileOutputStream(file, false);
            try {
                setOutputStream(fos);
                this.file = file;
            } finally {
                if (! ok) {
                    safeClose(fos);
                }
            }
        }
    }

    /**
     * Get the current output file.
     *
     * @return the file
     */
    public File getFile() {
        synchronized (outputLock) {
            return file;
        }
    }

    /**
     * Set the output file by name.
     *
     * @param fileName the file name
     * @throws FileNotFoundException if an error occurs opening the file
     */
    public void setFileName(String fileName) throws FileNotFoundException {
        setFile(new File(fileName));
    }
}
