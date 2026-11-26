package com.nima.aisuite;
import android.app.*; import android.os.*; import android.content.*; import android.view.*; import android.widget.*; import java.io.*;
import com.nima.aisuite.device.*; import com.nima.aisuite.memory.*; import com.nima.aisuite.gguf.*; import com.nima.aisuite.advisor.*; import com.nima.aisuite.compiler.*;
public class MainActivity extends Activity {
 LinearLayout box; EditText code; TextView result; static final int PICK=42;
 public void onCreate(Bundle b){super.onCreate(b); home();}
 TextView label(String s,int z){TextView t=new TextView(this);t.setText(s);t.setTextSize(z);t.setTextColor(0xffeeeeee);t.setPadding(10,8,10,8);return t;}
 Button btn(String s,View.OnClickListener l){Button b=new Button(this);b.setText(s);b.setOnClickListener(l);return b;}
 void page(String title){ScrollView sc=new ScrollView(this);box=new LinearLayout(this);box.setOrientation(1);box.setPadding(12,12,12,20);box.setBackgroundColor(0xff101820);sc.addView(box);setContentView(sc);box.addView(label("NIMA AI SUITE  /  "+title,21));box.addView(label("OFFLINE PLATFORM • 1.0.0",11));}
 void home(){page("DASHBOARD");DeviceProfile p=DeviceScanner.scan(this);box.addView(label(p.model+"  |  "+p.abi+"  |  API "+p.api,15));box.addView(label("RAM "+p.availableMb+" MB available / "+p.totalMb+" MB total",14));
 box.addView(btn("DEVICE ANALYZER",v->device()));box.addView(btn("AI MODEL ADVISOR",v->advisor()));box.addView(btn("OPEN GGUF FILE",v->pick()));box.addView(btn("NIMA SCRIPT COMPILER",v->compiler()));box.addView(btn("RAM MANAGER",v->memory()));}
 void device(){page("DEVICE ANALYZER");box.addView(label(DeviceScanner.scan(this).toString(),14));box.addView(btn("BACK",v->home()));}
 void advisor(){page("MODEL ADVISOR");for(String s:ModelAdvisor.rows(DeviceScanner.scan(this)))box.addView(label(s,14));box.addView(btn("BACK",v->home()));}
 void pick(){try{Intent i=new Intent(Intent.ACTION_GET_CONTENT);i.setType("*/*");startActivityForResult(i,PICK);}catch(Exception e){msg(e.toString());}}
 protected void onActivityResult(int r,int c,Intent d){super.onActivityResult(r,c,d);if(r==PICK&&c==RESULT_OK&&d!=null)try{InputStream in=getContentResolver().openInputStream(d.getData());GgufInspector.Report g=GgufInspector.inspect(in);page("GGUF REPORT");box.addView(label(g.toString(),14));ModelAdvisor.Assessment a=ModelAdvisor.assess(DeviceScanner.scan(this),g.fileBytes);box.addView(label(a.state+"\n"+a.reason,14));box.addView(label("Inspection is not inference. A native runtime must be installed and pass its self-test.",12));box.addView(btn("BACK",v->home()));}catch(Exception e){msg("GGUF error: "+e.getMessage());}}
 void compiler(){page("NIMA SCRIPT COMPILER");box.addView(label("Offline mini-language compiler + virtual machine",12));code=new EditText(this);code.setTextColor(-1);code.setMinLines(7);code.setText("let a = 7;\nlet b = 5;\nprint a * b;\n");box.addView(code);result=label("Compiler output",14);box.addView(result);box.addView(btn("COMPILE + RUN",v->{try{NimaCompiler.Program p=NimaCompiler.compile(code.getText().toString());result.setText("COMPILE SUCCESS\n"+NimaVM.run(p));}catch(Exception e){result.setText("ERROR: "+e.getMessage());}}));box.addView(btn("BACK",v->home()));}
 void memory(){page("RAM MANAGER");box.addView(label(MemoryMonitor.read(this).toString(),15));box.addView(label("This reports memory and releases only this app's temporary resources. It cannot increase physical RAM.",12));box.addView(btn("REFRESH",v->memory()));box.addView(btn("BACK",v->home()));}
 void msg(String s){new AlertDialog.Builder(this).setMessage(s).setPositiveButton("OK",null).show();}
}
