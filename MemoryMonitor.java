package com.nima.aisuite.memory;
import android.content.*;import android.app.*;import java.io.*;
public final class MemoryMonitor {
 public static Snapshot read(Context c){Snapshot s=new Snapshot();try{ActivityManager a=(ActivityManager)c.getSystemService(Context.ACTIVITY_SERVICE);ActivityManager.MemoryInfo m=new ActivityManager.MemoryInfo();a.getMemoryInfo(m);s.available=m.availMem/1048576L;s.low=m.lowMemory;}catch(Exception e){}
 BufferedReader r=null;try{r=new BufferedReader(new FileReader("/proc/meminfo"));String x;while((x=r.readLine())!=null){if(x.startsWith("MemTotal:"))s.total=num(x);if(x.startsWith("MemFree:"))s.free=num(x);if(x.startsWith("Cached:"))s.cached=num(x);if(x.startsWith("SwapTotal:"))s.swapTotal=num(x);if(x.startsWith("SwapFree:"))s.swapFree=num(x);}}catch(Exception e){}finally{try{if(r!=null)r.close();}catch(Exception e){}}return s;}
 static long num(String x){try{return Long.parseLong(x.replaceAll("[^0-9]",""))/1024;}catch(Exception e){return 0;}}
 public static class Snapshot{public long total,free,cached,available,swapTotal,swapFree;public boolean low;public String toString(){return "Total RAM: "+total+" MB\nFree RAM: "+free+" MB\nCached: "+cached+" MB\nAvailable (Android): "+available+" MB\nLow memory: "+low+"\nSwap total/free: "+swapTotal+" / "+swapFree+" MB";}}
}
