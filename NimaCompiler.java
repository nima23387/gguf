package com.nima.aisuite.compiler;
import java.util.*;
/** NimaScript v1: let name = expression; print expression; */
public final class NimaCompiler {
 public static Program compile(String s)throws Exception{return new Parser(s==null?"":s).parse();}
 public static class Instruction{public String op,text;public long value;Instruction(String o,String t,long v){op=o;text=t;value=v;}}
 public static class Program{public List<Instruction> code=new ArrayList<Instruction>();}
 static class Token{String s;int type;Token(String x,int t){s=x;type=t;}}
 static class Parser{
 String s;int p;Token t;Program out=new Program();Parser(String x)throws Exception{s=x;next();}
 Program parse()throws Exception{while(t.type!=0)stmt();return out;}
 void stmt()throws Exception{if(word("let")){next();if(t.type!=1)err("Variable name expected");String n=t.s;next();expect("=");expr();emit("STORE",n,0);expect(";");}else if(word("print")){next();expr();emit("PRINT","",0);expect(";");}else err("Expected let or print");}
 void expr()throws Exception{term();while(is("+")||is("-")){String x=t.s;next();term();emit(x.equals("+")?"ADD":"SUB","",0);}}
 void term()throws Exception{factor();while(is("*")||is("/")){String x=t.s;next();factor();emit(x.equals("*")?"MUL":"DIV","",0);}}
 void factor()throws Exception{if(is("-")){next();factor();emit("NEG","",0);}else if(is("(")){next();expr();expect(")");}else if(t.type==2){emit("PUSH","",Long.parseLong(t.s));next();}else if(t.type==1){emit("LOAD",t.s,0);next();}else err("Number, variable or '(' expected");}
 void emit(String o,String x,long v){out.code.add(new Instruction(o,x,v));}boolean word(String x){return t.type==1&&t.s.equals(x);}boolean is(String x){return t.s.equals(x);}
 void expect(String x)throws Exception{if(!is(x))err("Expected "+x);next();}
 void err(String x)throws Exception{throw new Exception(x+" at character "+p);}
 void next()throws Exception{while(p<s.length()&&Character.isWhitespace(s.charAt(p)))p++;if(p>=s.length()){t=new Token("",0);return;}char c=s.charAt(p);if(Character.isLetter(c)||c=='_'){int a=p++;while(p<s.length()&&(Character.isLetterOrDigit(s.charAt(p))||s.charAt(p)=='_'))p++;t=new Token(s.substring(a,p),1);}else if(Character.isDigit(c)){int a=p++;while(p<s.length()&&Character.isDigit(s.charAt(p)))p++;t=new Token(s.substring(a,p),2);}else if("+-*/=();".indexOf(c)>=0){p++;t=new Token(""+c,3);}else err("Unexpected character "+c);}
 }
}
