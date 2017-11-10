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
import java.util.*;
import java.util.regex.*;

import android.view.View.OnClickListener;
import java.lang.Process;

public class Main extends Activity {
	
	String dataPath = "/data/data/";
	String ver = "com.tencent.mobileqq";
	String number;
	String rmFile = "rm ";
	boolean DEBUG = false;
	ArrayList<File> fileList;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		
		final Button rootbtn = (Button) findViewById(R.id.rootbtn);
		rootbtn.setText("软件没有获取权限");
		rootbtn.setTextColor(0xFFF44336);
		rootbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (check_root()) {
					rootbtn.setText("已经获取权限");
					rootbtn.setTextColor(0xFF4CAF50);
					setbtn();
				}
			}
		});
		
		
		Button openqq = (Button) findViewById(R.id.openqq);
		openqq.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (DEBUG) Toast.makeText(Main.this, ver + ".activity.SplashActivity",Toast.LENGTH_SHORT).show();
				return true;
			}
		});
		openqq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					startActivity(new Intent().setClassName(ver, ver + ".activity.SplashActivity").setFlags(1));
					finish();
				} catch (ActivityNotFoundException e) {
					if (DEBUG) {
						e.printStackTrace();
						Toast.makeText(Main.this, e.toString(), 1).show();
					} else {
						Toast.makeText(Main.this, "无法启动QQ", 1).show();
					}
				}
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
					run_cmd("touch " + dataPath + ver + "/files/user/u_" + number + "_t");
					Toast.makeText(Main.this, "完成！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		Button deletefilebtn= (Button) findViewById(R.id.deletefile);
		deletefilebtn.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					if (DEBUG) rmFile = "rm -f ";
					fileDeleteDialog();
					if (DEBUG) Toast.makeText(Main.this, "强制删除模式", Toast.LENGTH_SHORT).show();
					return true;
				}
			});
		deletefilebtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					fileDeleteDialog();
				}
			});
	}
	
	private void fileDeleteDialog() {
		fileList = new ArrayList<File>();
		final String path = dataPath + ver + "/files/user/";
		run_cmd("chmod 777 " + path);
		getAllFiles(new File(path));
		run_cmd("chmod 700 " + path);
		List<String> data = new ArrayList<String>();
		for(int i=0;i<fileList.size();i++) {
			String name = fileList.get(i).toString().substring(fileList.get(i).toString().lastIndexOf("/")+1,fileList.get(i).toString().length());
			Pattern p = Pattern.compile("[^0-9]");
			Matcher m = p.matcher(name);
			data.add(m.replaceAll("").trim());
		}
		final String[] strings = new String[data.size()];
		data.toArray(strings);
		AlertDialog.Builder fileDialog = new AlertDialog.Builder(Main.this);
		fileDialog.setTitle("选择账号");
		fileDialog.setItems(strings, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String deletePath = path + "u_" + strings[which] + "_t";
					run_cmd(rmFile + deletePath);
					Toast.makeText(Main.this, "已删除！", Toast.LENGTH_SHORT).show();
					if (DEBUG) Toast.makeText(Main.this, deletePath, Toast.LENGTH_SHORT).show();
				}
			});
		fileDialog.show();
	}
	
	public boolean run_cmd(String cmd) {
        Process process = null;
        DataOutputStream os = null;
		int re = 1;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
			re = process.waitFor();
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
        return re == 0;
    }
	
	public boolean check_root() {
		try {
			return run_cmd("system/bin/mount -o rw,remount -t rootfs /data");
		} catch (Exception e) {
			if (DEBUG) Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return false;
	}
	
	private void getAllFiles(File root) {
		File files[] = root.listFiles();
		if(files != null)
			for(File f:files) {
				if(f.isDirectory()) {
					getAllFiles(f);
				}
				else{
					this.fileList.add(f);
				}
			}
	}
	
}

