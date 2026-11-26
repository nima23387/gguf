package com.nima.aisuite.compiler;
import java.util.*;
public final class NimaVM {
 public static String run(NimaCompiler.Program p)throws Exception{ArrayList<Long> st=new ArrayList<Long>();Map<String,Long> vars=new HashMap<String,Long>();StringBuilder out=new StringBuilder();
 for(NimaCompiler.Instruction i:p.code){if(i.op.equals("PUSH"))st.add(i.value);else if(i.op.equals("LOAD")){Long v=vars.get(i.text);if(v==null)throw new Exception("Undefined variable "+i.text);st.add(v);}else if(i.op.equals("STORE"))vars.put(i.text,pop(st));else if(i.op.equals("PRINT"))out.append(pop(st)).append('\n');else if(i.op.equals("NEG"))st.add(-pop(st));else{long b=pop(st),a=pop(st),v;if(i.op.equals("ADD"))v=a+b;else if(i.op.equals("SUB"))v=a-b;else if(i.op.equals("MUL"))v=a*b;else if(i.op.equals("DIV")){if(b==0)throw new Exception("Division by zero");v=a/b;}else throw new Exception("Unknown instruction "+i.op);st.add(v);}if(st.size()>1024)throw new Exception("Stack limit exceeded");}
 return out.length()==0?"Completed":out.toString();}
 static long pop(ArrayList<Long>s)throws Exception{if(s.size()==0)throw new Exception("Stack underflow");return s.remove(s.size()-1);}
}
