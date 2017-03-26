package zhang.billy.com.helloworld;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhangvae on 2017/3/23.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "fee.db";
    private static final int DATABASE_VERSION = 3;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS person_fee" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, ele1 REAL, fee1 REAL, ele2 REAL, fee2 REAL, ele3 REAL, fee3 REAL, commEle REAL)");
    }

    //如果DATABASE_VERSION值增加,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE person_fee ADD COLUMN commFee REAL");
    }
}
