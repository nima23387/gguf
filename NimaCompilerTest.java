package com.nima.aisuite.compiler;
import org.junit.Test;import static org.junit.Assert.*;
public class NimaCompilerTest {
 @Test public void arithmetic()throws Exception{assertEquals("35\n",NimaVM.run(NimaCompiler.compile("let a=7; let b=5; print a*b;")));}
 @Test public void precedence()throws Exception{assertEquals("14\n",NimaVM.run(NimaCompiler.compile("print 2+3*4;")));}
}
