package com.nima.aisuite.device;
import android.content.*;import android.os.*;import android.app.*;import android.os.StatFs;import java.io.*;
public final class DeviceScanner {
 public static DeviceProfile scan(Context c){DeviceProfile p=new DeviceProfile();p.maker=Build.MANUFACTURER;p.brand=Build.BRAND;p.model=Build.MODEL;p.device=Build.DEVICE;p.release=Build.VERSION.RELEASE;p.api=Build.VERSION.SDK_INT;p.abi=Build.CPU_ABI;p.cores=Runtime.getRuntime().availableProcessors();
 try{ActivityManager a=(ActivityManager)c.getSystemService(Context.ACTIVITY_SERVICE);ActivityManager.MemoryInfo m=new ActivityManager.MemoryInfo();a.getMemoryInfo(m);p.availableMb=m.availMem/1048576L;}catch(Exception e){}
 p.totalMb=memTotal();try{StatFs s=new StatFs(Environment.getDataDirectory().getPath());p.storageMb=((long)s.getAvailableBlocks()*s.getBlockSize())/1048576L;}catch(Exception e){}
 p.root=new File("/system/bin/su").exists()||new File("/system/xbin/su").exists();return p;}
 static long memTotal(){BufferedReader r=null;try{r=new BufferedReader(new FileReader("/proc/meminfo"));String s;while((s=r.readLine())!=null)if(s.startsWith("MemTotal:"))return Long.parseLong(s.replaceAll("[^0-9]",""))/1024;}catch(Exception e){}finally{try{if(r!=null)r.close();}catch(Exception e){}}return 0;}
}
