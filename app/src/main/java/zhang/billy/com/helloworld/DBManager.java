package zhang.billy.com.helloworld;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangvae on 2017/3/23.
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public void clearData() {
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("delete from person_fee");
            db.setTransactionSuccessful();
        } catch (Exception e) {

        }finally {
            db.endTransaction();
        }
    }

    public boolean add(PersonFee person) {
        boolean flag = false;
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("INSERT INTO person_fee VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{person.getEle1(), person.getFee1(), person.getEle2(), person.getFee2(),
                            person.getEle3(), person.getFee3(), person.getCommEle(),
                            person.getCreatetime(), person.getCommFee()});
            db.setTransactionSuccessful();  //设置事务成功完成
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return flag;
    }

    public List<PersonFee> query(boolean last) {
        ArrayList<PersonFee> persons = new ArrayList<PersonFee>();
        Cursor c = db.rawQuery("SELECT * FROM person_fee order by _id desc", null);
        while (c.moveToNext()) {
            PersonFee person = new PersonFee();
//            int sequence = c.getInt(c.getColumnIndex("_id"));
            person.setCreatetime(c.getString(c.getColumnIndex("createtime")));
            person.setEle1(c.getDouble(c.getColumnIndex("ele1")));
            person.setFee1(c.getDouble(c.getColumnIndex("fee1")));
            person.setEle2(c.getDouble(c.getColumnIndex("ele2")));
            person.setFee2(c.getDouble(c.getColumnIndex("fee2")));
            person.setEle3(c.getDouble(c.getColumnIndex("ele3")));
            person.setFee3(c.getDouble(c.getColumnIndex("fee3")));
            person.setCommEle(c.getDouble(c.getColumnIndex("commEle")));
            person.setCommFee(c.getDouble(c.getColumnIndex("commFee")));
            persons.add(person);
            if (last)
                break;
        }
        c.close();
        return persons;
    }

}
