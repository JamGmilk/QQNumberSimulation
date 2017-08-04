package com.zzz.QQNumberSimulation;

import android.app.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.io.*;

import android.view.View.OnClickListener;
import java.lang.Process;

public class Main extends Activity {
	
	String dataPath = "/data/data/"; // 定义系统数据目录，目录为 /data/data/
	String ver = "com.tencent.mobileqq"; // 定义QQ版本（默认为 "com.tencent.mobileqq"）
	String number; // 定义QQ号码
	boolean DEBUG = false; // 定义调试模式（默认为 “否“ ）
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
	    
	final Button rootbtn = (Button) findViewById(R.id.rootbtn);
	rootbtn.setText("软件没有获取权限"); // 设置按钮上的文字
	rootbtn.setTextColor(0xFFF44336); // 设置按钮上文字的颜色（MD红）
	rootbtn.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			/* 如果软件获取到root权限 */
			if (check_root()) {
				rootbtn.setText("已经获取权限"); // 设置按钮上的文字
				rootbtn.setTextColor(0xFF4CAF50); // 设置按钮上文字的颜色（MD绿）
				setbtn(); // 激活方法
			}
		}
	});
		
	Button openqq = (Button) findViewById(R.id.openqq);
	openqq.setOnLongClickListener(new View.OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			if (DEBUG) Toast.makeText(Main.this, ver + ".activity.SplashActivity",Toast.LENGTH_SHORT).show(); // 如果处于 Debug 模式，长按按钮会弹出Toast，提示启动QQ的活动
			return true;
		}
	});
	    
	openqq.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			startActivity(new Intent().setClassName(ver, ver + ".activity.SplashActivity")); // 启动QQ
		}
	});
		
	Spinner sp = (Spinner) findViewById(R.id.sp);
	String[] mItems = getResources().getStringArray(R.array.qqver);
	ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	sp.setAdapter(adapter);
	sp.setOnItemSelectedListener(new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			if (pos == 0) ver="com.tencent.mobileqq";
			if (pos == 1) ver = "com.tencent.minihd.qq";
			if (pos == 2) ver = "com.tencent.qqlite";
			if (pos == 3) ver = "com.tencent.mobileqqi";
			if (pos == 4) ver = "com.tencent.qq.kddi";
			if (pos == 5) ver = "com.tencent.tim";
			if (pos == 6) ver = "com.tencent.eim";
				
			if (DEBUG) Toast.makeText(Main.this, ver,Toast.LENGTH_SHORT).show();
				
			}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Another interface callback
		}
	});
		
		
	final TextView tw = (TextView) findViewById(R.id.tw);
	tw.setOnLongClickListener(new View.OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			rootbtn.setText("Debug Mode");
			rootbtn.setTextColor(0xFFFFEB3B);
			DEBUG = true;
			setbtn();
			return true;
		}
	});
		
		
	if (check_root()) {
		rootbtn.setText("已经获取权限");
		rootbtn.setTextColor(0xFF4CAF50);
		setbtn();
	}
		
		
    }
	
	public void setbtn() {
		Button btn = (Button) findViewById(R.id.btn);
		final EditText et = (EditText) findViewById(R.id.et);
		btn.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (DEBUG) if (!TextUtils.isEmpty(et.getText())) Toast.makeText(Main.this, dataPath + ver + "/files/user/u_" + et.getText().toString() + "_t", Toast.LENGTH_SHORT).show();	
				return true;
			}
		});
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(et.getText())) {
					Toast.makeText(Main.this, "请输入QQ号！", Toast.LENGTH_SHORT).show();
				} else {
					number = et.getText().toString();
					create();
					Toast.makeText(Main.this, "完成！", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	public boolean run_cmd(String command) {
        Process process = null;
        DataOutputStream os = null;
        int rt = 1;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            rt = process.waitFor();
        } catch (Exception e) {
			if (DEBUG) Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
					if (DEBUG) Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
        return rt == 0;
    }

    public boolean create() {
		try {
			return run_cmd("touch " + dataPath + ver + "/files/user/u_" + number + "_t");
		} catch (Exception e) {
			if (DEBUG) Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}     
		return false;
	}
	
	public boolean check_root() {
		try {
			return run_cmd("system/bin/mount -o rw,remount -t rootfs /data");
		} catch (Exception e) {
			if (DEBUG) Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return false;
	}
	
}

