/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package demo.jaxrs.search.server;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.cxf.helpers.IOUtils;

public class Storage {
    private final File folder = new File("files");
    
    public Storage() throws IOException {
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Unable to initialize FS storage:" + folder.getAbsolutePath());
        }
        
        if (!folder.isDirectory() || !folder.canWrite() || !folder.canWrite()) {
            throw new IOException("Unable to access FS storage:" + folder.getAbsolutePath());
        }        
    }
    
    public void addDocument(final String name, final byte[] content) throws IOException {
        InputStream in = null;
        
        try {
            in = new ByteArrayInputStream(content);
            addDocument(name, in);
        } finally {
            if (in != null) {
                try { in.close(); } catch (IOException ex) { /* do nothing */ }
            }
        }
    }
    
    public void addDocument(final String name, final InputStream in) throws IOException { 
        final File f = new File(folder, name);
        
        if (f.exists() && !f.delete()) {
            throw new IOException("Unable to delete FS file:" + f.getAbsolutePath());
        } 
        
        OutputStream out = null;
        try {            
            out = new BufferedOutputStream(new FileOutputStream(f));
            out.write(IOUtils.readBytesFromStream(in));
            
            f.deleteOnExit();
        } finally {
            if (out != null) {
                try { out.close(); } catch (IOException ex) { /* do nothing */ }
            }
        }
    }
}