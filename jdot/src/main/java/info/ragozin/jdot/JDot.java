/**
 * Copyright 2017 Alexey Ragozin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.ragozin.jdot;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JDot {

	private ScriptEngine eng;
	private int memSize = 32 << 20;
	
	public JDot() {

		try {
			InputStream is = this.getClass().getResourceAsStream("/viz-lite.js");
			if (is == null) {
				throw new RuntimeException("Cannot load viz.js - resource not found");
			}
			String vizjs = toString(is);
			
			ScriptEngineManager man = new ScriptEngineManager();
			eng = man.getEngineByName("JavaScript");
			eng.eval(vizjs);
		} catch (IOException e) {
			throw new RuntimeException("Cannot load viz.js", e);
		} catch (ScriptException e) {
			throw new RuntimeException("Cannot load viz.js -  JavaScript errpr", e);
		}
	}

	public void setMemSize(int size) {
		if (size < 1 << 20) {
			throw new IllegalArgumentException("Size is too small: " + size);
		}
		this.memSize = size;
	}
	
	public String dot2svg(String dot) {
        try {
			eng.getBindings(ScriptContext.ENGINE_SCOPE).put("dot", dot);
			eng.getBindings(ScriptContext.ENGINE_SCOPE).put("memSize", memSize);
			String result = (String) eng.eval("Viz(dot, { totalMemory: memSize })");
			return result;
		} catch (ScriptException e) {
			throw new RuntimeException(e);
		}
	}
	
    static String toString(InputStream is) throws IOException {
        try {
            StringBuilder buf = new StringBuilder();
            Reader reader = new InputStreamReader(is);
            char[] swap = new char[1024];
            while(true) {
                int n = reader.read(swap);
                if (n < 0) {
                    break;
                }
                else {
                    buf.append(swap, 0, n);
                }
            }
            return buf.toString();
        }
        finally {
            try {
                is.close();
            }
            catch(Exception e) {
                // ignore
            }
        }
    }
}
