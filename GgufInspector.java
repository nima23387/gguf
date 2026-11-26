package com.nima.aisuite.gguf;
import java.io.*;import java.nio.charset.Charset;
public final class GgufInspector {
 public static Report inspect(InputStream src)throws IOException{CountingInputStream c=new CountingInputStream(new BufferedInputStream(src));DataInputStream d=new DataInputStream(c);byte[] m=new byte[4];d.readFully(m);if(!"GGUF".equals(new String(m,Charset.forName("US-ASCII"))))throw new IOException("Invalid GGUF magic");long v=u32(d);if(v<1||v>3)throw new IOException("Unsupported GGUF version "+v);long t=u64(d),n=u64(d);if(t>1000000||n>1000000)throw new IOException("Implausible header counts");return new Report(v,t,n,c.count);}
 static long u32(DataInputStream d)throws IOException{return (long)d.readUnsignedByte()|((long)d.readUnsignedByte()<<8)|((long)d.readUnsignedByte()<<16)|((long)d.readUnsignedByte()<<24);}
 static long u64(DataInputStream d)throws IOException{long lo=u32(d),hi=u32(d);if(hi>0x7fffffffL)throw new IOException("Header integer overflow");return (hi<<32)|lo;}
 static class CountingInputStream extends FilterInputStream{long count;CountingInputStream(InputStream i){super(i);}public int read()throws IOException{int x=super.read();if(x>=0)count++;return x;}public int read(byte[]b,int o,int l)throws IOException{int n=super.read(b,o,l);if(n>0)count+=n;return n;}}
 public static class Report{public long version,tensors,metadata,headerBytes,fileBytes=-1;Report(long v,long t,long n,long h){version=v;tensors=t;metadata=n;headerBytes=h;}public String toString(){return "GGUF v"+version+"\nTensors: "+tensors+"\nMetadata entries: "+metadata+"\nHeader bytes read: "+headerBytes+"\nFile size: not available from content stream";}}
}
