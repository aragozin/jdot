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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;

public class Dot2SvgCmd {

	public static void main(String[] args) {
		try {
			String outFile = null;
			String inFile = null;
			int memSize = -1;
			
			Iterator<String> it = Arrays.asList(args).iterator();
			while(it.hasNext()) {
				String arg = it.next();
				if (arg.equals("-m")) {
					try {
						memSize = Integer.parseInt(it.next()) << 20;
					}
					catch(Exception e) {
						System.err.println("Bad -m option format");
						exit();
					}				
				}
				else if (arg.equals("-o")) {
					try {
						if (outFile != null) {
							System.err.println("You can specify only one output file");
							exit();
						}
						else {
							outFile = it.next();
						}
					}
					catch(Exception e) {
						System.err.println("Bad -o option format");
						exit();					
					}
				}
				else if (arg.equals("-h") || arg.equals("--help") || arg.equals("-?")) {
					System.err.println("Arguments: dot2svg [INPUTFILE] [-o OUTPUTFILE] [-m MEMSIZE]");;
					System.err.println(" -o Output file (stdout if not specified)");
					System.err.println(" -m Memory size in megabytes");
					exit();
				}
				else if (arg.startsWith("-")) {
					System.err.println("Unknown option '" + arg + "'");
					exit();
				}
				else {
					if (inFile != null) {
						System.err.println("Only one input file allowed");
						exit();
					}
					else {
						inFile = arg;
					}
				}
			}
			
			InputStream is;
			if (inFile == null) {
				is = System.in;
			}
			else {
				try {
					is = new FileInputStream(inFile);
				} catch (FileNotFoundException e) {
					System.err.println("Failed open file: " +inFile);
					exit();
					return;
				}
			}
			
			String dot = JDot.toString(is);
			
			JDot jdot;
			try {
				jdot = new JDot();
				if (memSize > 0) {
					jdot.setMemSize(memSize);
				}
			}
			catch(RuntimeException e) {
				System.err.println("Failed to load viz.js");
				e.printStackTrace();
				exit();				
				return;
			}
			
			String svg = jdot.dot2svg(dot);
			
			OutputStream out;
			if (outFile == null) {
				out = System.out;
			}
			else {
				File f = new File(outFile);
				if (f.getParentFile() != null) {
					f.getParentFile().mkdirs();
				}
				try {
					out = new FileOutputStream(f);
				} catch (FileNotFoundException e) {
					System.err.println("Failed open output file: " +outFile);
					System.err.println(e.toString());
					exit();
					return;
				}
			}
			
			out.write(svg.getBytes());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			exit();
		}
	}

	private static void exit() {
		System.exit(1);		
	}	
}
