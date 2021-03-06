/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.tomee.util;

import org.apache.openejb.loader.Files;
import org.apache.openejb.loader.IO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InstallationEnrichers {
    private InstallationEnrichers() {
        // no-op
    }

    public static String addOneLineFormatter(final File home) {
        final String name = SimpleTomEEFormatter.class.getPackage().getName().replace('.', '/') + "/" + SimpleTomEEFormatter.class.getSimpleName() + ".class";
        final InputStream is = InstallationEnrichers.class.getResourceAsStream("/" + name);
        if (is != null) {
            final File parent = Files.path(home, "bin", "classes");
            final File destination = new File(parent, name);
            if (destination.exists()) {
                return parent.getAbsolutePath();
            }

            if (!destination.getParentFile().mkdirs()) {
                Logger.getLogger(InstallationEnrichers.class.getName()).warning("Can't create " + destination.getPath());
            } else {
                try {
                    IO.copy(is, destination);
                    return parent.getAbsolutePath(); // to add to the classpath, don't return destination
                } catch (IOException e) {
                    Logger.getLogger(InstallationEnrichers.class.getName()).log(Level.WARNING, "Can't add SingleLineFormatter", e);
                } finally {
                    IO.close(is);
                }
            }
        }
        return null;
    }
}
