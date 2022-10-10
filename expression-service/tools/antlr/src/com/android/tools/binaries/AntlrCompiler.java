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

package com.android.tools.binaries;

import com.android.tools.utils.JarOutputCompiler;
import org.antlr.v4.Tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Invokes ANTLR 4.x from prebuilts to create a {@code *.srcjar} with the generated parser code.
 */
@SuppressWarnings("ALL")
public class AntlrCompiler extends JarOutputCompiler {

    private final String outputPackage;

    public AntlrCompiler(String outputPackage) {
        super("antlr");
        this.outputPackage = outputPackage;
    }

    public static void main(String[] args) throws IOException {
        final List<String> argsList = new ArrayList<>(Arrays.asList(args));
        String outputPackage = "";
        if (argsList.size() >= 2 && "-package".equals(argsList.get(0))) {
            argsList.remove(0);
            outputPackage = argsList.remove(0);
        }
        System.exit(new AntlrCompiler(outputPackage).run(argsList));
    }

    @Override
    protected boolean compile(List<String> files, String classPath, File outDir) {
        final List<String> antlrArgs = new ArrayList<>(
                Arrays.asList("-visitor", "-o", outDir.getAbsolutePath()));
        antlrArgs.addAll(files);
        if (!outputPackage.isEmpty()) {
            antlrArgs.add("-package");
            antlrArgs.add(outputPackage);
        }
        Tool antlr = new Tool(antlrArgs.toArray(new String[0]));
        antlr.processGrammarsOnCommandLine();
        return antlr.errMgr.getNumErrors() == 0;
    }
}
