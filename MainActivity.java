package com.nima.aisuite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.File;
import java.io.InputStream;

public class MainActivity extends Activity {
    private static final int PICK_GGUF = 41;
    private LinearLayout root;
    private TextView status;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        buildScreen();
        showHome();
    }

    private void buildScreen() {
        ScrollView scroll = new ScrollView(this);
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(14, 14, 14, 14);
        root.setBackgroundColor(0xff10151d);
        scroll.addView(root);
        setContentView(scroll);
    }

    private TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.RIGHT);
        t.setPadding(8, 8, 8, 8);
        return t;
    }

    private void addButton(String label, View.OnClickListener listener) {
        Button b = new Button(this);
        b.setText(label);
        b.setOnClickListener(listener);
        root.addView(b, new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    private void heading(String value) {
        root.addView(text(value, 22, 0xff72d6c1));
    }

    private void showHome() {
        root.removeAllViews();
        heading("NIMA AI SUITE");
        root.addView(text("ابزار آفلاین حافظه و مدل‌های هوش مصنوعی", 15, 0xffffffff));
        root.addView(text("نسخه آزمایشی 0.1.0 | سازگار با Android 2.3+", 12, 0xffaab6c5));
        addButton("۱. RAM Booster / بررسی حافظه", new View.OnClickListener() {
            public void onClick(View v) { showRam(); }
        });
        addButton("۲. GGUF Compiler / بررسی فایل مدل", new View.OnClickListener() {
            public void onClick(View v) { pickGguf(); }
        });
        addButton("۳. GGUF Runner / وضعیت موتور اجرا", new View.OnClickListener() {
            public void onClick(View v) { showRunner(); }
        });
        addButton("درباره پروژه", new View.OnClickListener() {
            public void onClick(View v) { showAbout(); }
        });
        status = text("این نسخه برای بررسی اولیه است و هنوز Swap را فعال نمی‌کند.", 13, 0xffffd27d);
        root.addView(status);
    }

    private String readSmallFile(String path) {
        StringBuilder out = new StringBuilder();
        try {
            java.io.BufferedReader r = new java.io.BufferedReader(
                new java.io.FileReader(path));
            String line;
            int count = 0;
            while ((line = r.readLine()) != null && count < 80) {
                out.append(line).append('\n');
                count++;
            }
            r.close();
        } catch (Exception e) {
            return "خواندن ممکن نیست: " + e.getMessage();
        }
        return out.toString();
    }

    private void showRam() {
        root.removeAllViews();
        heading("بررسی حافظه");
        root.addView(text("اطلاعات /proc/meminfo:", 15, 0xffffffff));
        root.addView(text(readSmallFile("/proc/meminfo"), 13, 0xffd7e0ea));
        root.addView(text("وضعیت Swap:", 15, 0xffffffff));
        root.addView(text(readSmallFile("/proc/swaps"), 13, 0xffd7e0ea));
        root.addView(text("وضعیت دسترسی روت:", 15, 0xffffffff));
        root.addView(text(checkRoot(), 13, 0xffd7e0ea));
        root.addView(text("هشدار: این نسخه هیچ پارتیشن یا فایلی را تغییر نمی‌دهد و Swap را روشن نمی‌کند.", 13, 0xffffd27d));
        addButton("بازگشت", new View.OnClickListener() {
            public void onClick(View v) { showHome(); }
        });
    }

    private String checkRoot() {
        try {
            Process p = Runtime.getRuntime().exec(new String[] {"su", "-c", "id"});
            java.io.BufferedReader r = new java.io.BufferedReader(
                new java.io.InputStreamReader(p.getInputStream()));
            String line = r.readLine();
            p.waitFor();
            return line == null ? "روت تأیید نشد." : line;
        } catch (Exception e) {
            return "دستور su در دسترس نیست یا دسترسی رد شد.";
        }
    }

    private void pickGguf() {
        try {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("*/*");
            startActivityForResult(i, PICK_GGUF);
        } catch (Exception e) {
            showMessage("انتخاب‌گر فایل در این گوشی در دسترس نیست.");
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_GGUF && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            inspectUri(uri);
        }
    }

    private void inspectUri(Uri uri) {
        if (uri == null) {
            showMessage("فایلی انتخاب نشد.");
            return;
        }
        InputStream in = null;
        try {
            in = getContentResolver().openInputStream(uri);
            byte[] h = new byte[24];
            int got = 0;
            while (got < h.length) {
                int n = in.read(h, got, h.length - got);
                if (n < 0) break;
                got += n;
            }
            if (got < 24) {
                showMessage("فایل خیلی کوچک است یا سرآیند GGUF کامل نیست.");
                return;
            }
            String magic = new String(h, 0, 4, "US-ASCII");
            if (!"GGUF".equals(magic)) {
                showMessage("این فایل سرآیند GGUF معتبر ندارد. شناسه: " + magic);
                return;
            }
            long version = u32(h, 4);
            long tensors = u64(h, 8);
            long metadata = u64(h, 16);
            showMessage("سرآیند GGUF شناسایی شد.\nنسخه: " + version +
                "\nتعداد Tensor: " + tensors +
                "\nتعداد Metadata: " + metadata +
                "\n\nاین بررسی اولیه است؛ هنوز تبدیل یا اجرای مدل انجام نمی‌شود.");
        } catch (Exception e) {
            showMessage("خواندن فایل ناموفق بود: " + e.getMessage());
        } finally {
            try { if (in != null) in.close(); } catch (Exception ignored) {}
        }
    }

    private long u32(byte[] b, int p) {
        return ((long)b[p] & 255L) |
            (((long)b[p+1] & 255L) << 8) |
            (((long)b[p+2] & 255L) << 16) |
            (((long)b[p+3] & 255L) << 24);
    }

    private long u64(byte[] b, int p) {
        long low = u32(b, p);
        long high = u32(b, p + 4);
        return low | (high << 32);
    }

    private void showRunner() {
        root.removeAllViews();
        heading("GGUF Runner");
        root.addView(text("موتور اجرای مدل هنوز به این نسخه متصل نشده است.", 16, 0xffffffff));
        root.addView(text("برای اجرای واقعی باید هسته inference سازگار با ARMv6 ساخته و روی دستگاه آزمایش شود. وجود فایل GGUF به‌تنهایی به معنی امکان اجرای آن نیست.", 14, 0xffd7e0ea));
        root.addView(text("هیچ پردازش ابری یا اتصال اینترنتی در این نسخه انجام نمی‌شود.", 13, 0xff72d6c1));
        addButton("بازگشت", new View.OnClickListener() {
            public void onClick(View v) { showHome(); }
        });
    }

    private void showAbout() {
        showMessage("Nima AI Suite\nنسخه 0.1.0\n\nنسخه آغازین آفلاین برای Android قدیمی.\nRAM: فقط تشخیص\nGGUF: بررسی سرآیند\nRunner: موتور هنوز پیاده‌سازی نشده است.");
    }

    private void showMessage(String message) {
        new AlertDialog.Builder(this)
            .setTitle("Nima AI Suite")
            .setMessage(message)
            .setPositiveButton("باشه", null)
            .show();
    }
}
