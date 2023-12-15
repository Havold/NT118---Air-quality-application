package com.example.natural.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "WeatherAsset";
    public static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "WEATHERASSET";
    public static final String COLUMN_TEMPERATURE = "temperature";

    public static final String COLUMN_RAINFALL = "rainfall";
    public static final String COLUMN_HUMIDITY = "humidity";
    public static final String COLUMN_WIND = "wind";
    public static final String COLUMN_TIME = "time";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_TEMPERATURE + " DOUBLE, " +
                COLUMN_RAINFALL + " DOUBLE, " +
                COLUMN_HUMIDITY + " DOUBLE, " +
                COLUMN_WIND + " DOUBLE, " +
                COLUMN_TIME + " INTEGER)";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertWeatherData(double temperature, double humidity, double wind, double rainfall, long time) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Kiểm tra xem dữ liệu có tồn tại không
        if (!isDataExist(db, temperature,rainfall,humidity,wind,time)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TEMPERATURE, temperature);
            values.put(COLUMN_RAINFALL, rainfall);
            values.put(COLUMN_HUMIDITY, humidity);
            values.put(COLUMN_WIND, wind);
            values.put(COLUMN_TIME, time);

            long newRowId = db.insert(TABLE_NAME, null, values);

            db.close();
            return newRowId;
        } else {
            // Dữ liệu đã tồn tại, không thêm vào
            db.close();
            return -1; // hoặc giá trị khác mà bạn chọn để chỉ ra rằng dữ liệu đã tồn tại
        }
    }

    public Cursor getAllWeatherData() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_TEMPERATURE,
                COLUMN_RAINFALL,
                COLUMN_HUMIDITY,
                COLUMN_WIND,
                COLUMN_TIME
        };

        return db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
    }

    private boolean isDataExist(SQLiteDatabase db, double temperature, double rainfall, double humidity, double wind, long time) {
        String[] projection = {COLUMN_TEMPERATURE, COLUMN_RAINFALL, COLUMN_HUMIDITY, COLUMN_WIND, COLUMN_TIME};
        String selection = COLUMN_TEMPERATURE + " = ? AND " +
                COLUMN_RAINFALL + " = ? AND " +
                COLUMN_HUMIDITY + " = ? AND " +
                COLUMN_WIND + " = ? AND " +
                COLUMN_TIME + " = ?";
        String[] selectionArgs = {
                String.valueOf(temperature),
                String.valueOf(rainfall),
                String.valueOf(humidity),
                String.valueOf(wind),
                String.valueOf(time)
        };

        // Thực hiện truy vấn để lấy các dòng có các giá trị cột tương ứng
        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Kiểm tra xem có dòng nào trả về không
        boolean dataExist = cursor.getCount() > 0;

        // Đóng Cursor sau khi sử dụng
        cursor.close();

        // Trả về kết quả kiểm tra
        return dataExist;
    }

    // Hàm xóa toàn bộ dữ liệu
    public void deleteAllWeatherData() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Thực hiện lệnh xóa toàn bộ dữ liệu trong bảng
        db.delete(TABLE_NAME, null, null);

        db.close();
    }

    // Thêm hàm để xóa toàn bộ dữ liệu trong bảng
    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);  // Tạo lại bảng sau khi xóa
        db.close();
    }
}