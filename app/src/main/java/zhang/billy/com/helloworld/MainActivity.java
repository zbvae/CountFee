package zhang.billy.com.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private int countTime = 0;
    private HashMap<Integer, PersonFee> map = new HashMap<>(); // 记录每次计算的当前点击次数和对应的记录

    private EditText et1; // 1号本次用电
    private EditText et2; // 2号本次用电
    private EditText et3; // 3号本次用电
    private EditText et4; // 公共本次用电
    private EditText et5; // 本次总电费

    private TextView tv1; // 1号上次用电
    private TextView tv2; // 2号上次用电
    private TextView tv3; // 3号上次用电
    private TextView tv4; // 公共上次用电
    private TextView tv5; // 上次总电费


    private Button btnCount; // 计算
    private Button btnSave; // 保存
    private Button btnHistory; // 历史记录
    private Button btnClear; // 清除所有记录

    private ListView feeList; // 底部列表
    private DBManager manager; // 数据库管理工具
    private PersonFee person; // 本次计算对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate 方法调用！");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 去掉标题栏
        setContentView(R.layout.activity_main);
        manager = new DBManager(this);

        registerView();

        getLastHistory(); // 展示上一次用电量

        btnCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countFees();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFee();
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHistory();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalDialog("确认清除之前保存的所有记录?", "clear");
            }
        });
    }

    public void registerView() {
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
        et4 = (EditText) findViewById(R.id.et4);
        et5 = (EditText) findViewById(R.id.et5);

        tv1 = (TextView) findViewById(R.id.history1);
        tv2 = (TextView) findViewById(R.id.history2);
        tv3 = (TextView) findViewById(R.id.history3);
        tv4 = (TextView) findViewById(R.id.history4);
        tv5 = (TextView) findViewById(R.id.history5);

        btnCount = (Button) findViewById(R.id.btnCount);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnHistory = (Button) findViewById(R.id.btnHistory);
        btnClear = (Button) findViewById(R.id.btnClear);
        feeList = (ListView) findViewById(R.id.feeList);
        Log.i(TAG, "已成功注册所有组件！");
    }

    private void getLastHistory() {
        List<PersonFee> history = manager.query(true);
        if (history != null && history.size() > 0) {
            PersonFee person = history.get(0);
            tv1.setText(String.valueOf(person.getEle1()));
            tv2.setText(String.valueOf(person.getEle2()));
            tv3.setText(String.valueOf(person.getEle3()));
            tv4.setText(String.valueOf(person.getCommEle()));
            tv5.setText(String.valueOf(person.getCommFee()));
            Log.i(TAG, "成功取得上次用电量和电费记录");
        }
    }

    public void countFees() {
        try {
            countTime++;
            double ele1 = Double.parseDouble(et1.getText().toString());
            double ele2 = Double.parseDouble(et2.getText().toString());
            double ele3 = Double.parseDouble(et3.getText().toString());
            double ele4 = Double.parseDouble(et4.getText().toString());
            double eleFee = Double.parseDouble(et5.getText().toString());

            double lastEle1 = Double.parseDouble(tv1.getText().toString());
            double lastEle2 = Double.parseDouble(tv2.getText().toString());
            double lastEle3 = Double.parseDouble(tv3.getText().toString());
            double lastEle4 = Double.parseDouble(tv4.getText().toString());
            double lastEleFee = Double.parseDouble(tv5.getText().toString());

            double minus1 = ele1 - lastEle1;
            double minus2 = ele2 - lastEle2;
            double minus3 = ele3 - lastEle3;
            double minus4 = ele4 - lastEle4;

            if(minus1 <= 0 || minus2 <= 0 || minus3 <= 0 || minus4 <= 0) {
                showPrompt("信息填写有误！");
                Log.i(TAG, "信息填写有误，将直接返回");
                return;
            }

            if(map.get(countTime-1) != null) { // 上一次点击计算的记录(只需要与上一次点击相比)
                PersonFee lastRec = map.get(countTime-1);
                if(ele1 == lastRec.getEle1() && ele2 == lastRec.getEle2() && ele3 == lastRec.getEle3()) {
                    showPrompt("本次填写的信息与上一次计算时填写的信息完全一样！");
                    Log.i(TAG, "信息填写有误，将直接返回");
                    return;
                }
            }

            double totalMinus = minus1 + minus2 + minus3 + minus4;
            double comFee = eleFee * (minus4 / totalMinus) / 3;
            double fee1 = Double.parseDouble(String.format("%.2f", eleFee * (minus1 / totalMinus) + comFee));
            double fee2 = Double.parseDouble(String.format("%.2f", eleFee * (minus2 / totalMinus) + comFee));
            double fee3 = Double.parseDouble(String.format("%.2f", eleFee * (minus3 / totalMinus) + comFee));

            person = new PersonFee(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()), ele1, fee1, ele2, fee2, ele3, fee3, ele4, eleFee);
            map.put(countTime, person);

            String item1 = "1号房间本次需交" + fee1;
            String item2 = "2号房间本次需交" + fee2;
            String item3 = "3号房间本次需交" + fee3;
            String item4 = "";
            if (eleFee > lastEleFee) {
                item4 = new StringBuilder().append("本次电费比上次超出").append(eleFee - lastEleFee).append("元, 注意解决用电哦！").toString();
            } else if(eleFee == lastEleFee){
                item4 = "本次电费与上次一模一样呢，也太巧合了吧！";
            } else {
                item4 = new StringBuilder().append("本次电费比上次低了").append(lastEleFee - eleFee).append("元, 很好呢，继续保持哦！").toString();
            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, new String[]{item1, item2, item3, item4});
            feeList.setAdapter(adapter);
            feeList.invalidate();
            Log.i(TAG, "已成功计算出本次各房间应交电费！");
        } catch (Exception e) {
            showPrompt("信息未填写完整！");
            Log.i(TAG, "信息未填写完整！");
        }
    }

    private void showNormalDialog(String msg, final String action) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("");
        dialog.setMessage(msg);
        dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if("save".equals(action))
                    saveCurrentFee();
                else if("clear".equals(action))
                    clearAllData();
            }
        });
        dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void saveFee() {
        if (person != null) {
            showNormalDialog("确认要保存本次计算的费用记录吗？", "save");
        } else {
            showPrompt("没有数据!");
        }
    }

    private void saveCurrentFee() {
        if (manager.add(person)) {
            showPrompt("保存成功,你可以进历史记录中查看!");
            Log.i(TAG, "保存成功!");
            et1.setText("");
            et2.setText("");
            et3.setText("");
            et4.setText("");
            et5.setText("");
            getLastHistory();
            et1.requestFocus();
            person = null;
        } else {
            showPrompt("信息有误，保存失败!");
            Log.i(TAG, "信息有误，保存失败!");
        }
    }

    private void showHistory() {
        List<PersonFee> history = manager.query(false);
        if(history != null && history.size()>0) {
            FeeAdapter adapter = new FeeAdapter(this, history); // 自定义adapter
            feeList.setAdapter(adapter);
            feeList.invalidate(); // 更新视图
            showPrompt("查询历史记录成功!");
            Log.i(TAG, "查询历史记录成功!");
        } else {
            showPrompt("查询历史记录失败或没有历史记录!");
            Log.i(TAG, "查询历史记录失败或没有历史记录!");
        }
    }

    private void clearAllData() {
        manager.clearData();
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");

        tv1.setText("0");
        tv2.setText("0");
        tv3.setText("0");
        tv4.setText("0");
        tv5.setText("0");

        feeList.setAdapter(null);
        feeList.invalidate();
        et1.requestFocus();

        showPrompt("已清除所有历史数据!");
        Log.i(TAG, "已清除所有历史数据!");
    }

    private void showPrompt(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }
}
