package hcmut.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import hcmut.data.TimeEff;
import hcmut.framework.DatabaseAdapter;
import hcmut.framework.DatabaseHelper;

/**
 * Created by minh on 12/08/2015.
 */
public class FcamDatabase extends DatabaseAdapter {
    public static final String TBL_TIME_EFFICIENCY = "TBL_TIME_EFFICIENCY";
    public static final String COL_TE_ID = "COL_TE_ID";
    public static final String COL_TE_RAM_AVAILABLE_MB = "COL_TE_RAM_AVAILABLE_MB";
    public static final String COL_TE_TIME_CONSUME = "COL_TE_TIME_CONSUME";
    public static final String COL_TE_REPEAT = "COL_TE_REPEAT";
    public static final String COL_TE_ACTIVITY_IN_BACKGROUND = "COL_TE_ACTIVITY_IN_BACKGROUND";

    public FcamDatabase(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        String[] sqls = createTableSql();
        for(String sql : sqls) {
            mDatabase.execSQL(sql);
        }
    }

    @Override
    public Object[] getAllRecord(String tableName) {
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor c = mDatabase.query(tableName, null, null, null, null, null, null);
        c.moveToFirst();
        if(tableName.equals(TBL_TIME_EFFICIENCY)) {
            while(!c.isAfterLast()){
                result.add(new TimeEff(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
                c.moveToNext();
            }
        }
        return result.toArray();
    }

    /*@Override
    public void insertUpdateRecord(Object data) {
        super.insertUpdateRecord(data);
    }*/

    public void insertTimeEff(TimeEff timeEff) {
        Object[] timeEffs = getAllRecord(TBL_TIME_EFFICIENCY);
        boolean isUpdate = false;
        for(Object o : timeEffs) {
            if(o instanceof TimeEff) {
                TimeEff te = (TimeEff) o;
                if(te.getRamAvailable()==timeEff.getRamAvailable() && te.isInBackground()==timeEff.isInBackground()) {
                    updateRecord(TBL_TIME_EFFICIENCY, COL_TE_ID, te.getId(),
                            COL_TE_TIME_CONSUME, String.valueOf(timeEff.getTimeConsume()));
                    isUpdate = true;
                }
            }
        }
        if(!isUpdate) {
            insertRecord(TBL_TIME_EFFICIENCY,
                    new String[] {COL_TE_ID, COL_TE_RAM_AVAILABLE_MB, COL_TE_TIME_CONSUME, COL_TE_REPEAT, COL_TE_ACTIVITY_IN_BACKGROUND},
                    new String[] {timeEff.getId(), timeEff.getmRamAvailableString(), timeEff.getmTimeConsumeString(), timeEff.getmRepeatString(), timeEff.getIsInBackground()});
        }
    }

    /**
     *
     * @param ramAvailableMB the amount of available RAM (MegaByte)
     * @return time in miliseconds
     */
    public int getTimeConsume(int ramAvailableMB, boolean isInBackground) {
        Object[] timeEffs = getAllRecord(TBL_TIME_EFFICIENCY);
        ArrayList<TimeEff> newTimeEffs = new ArrayList<TimeEff>();
        for(Object o : timeEffs) {
            TimeEff te = (TimeEff) o;
            if(te.isInBackground()==isInBackground) {
                newTimeEffs.add(te);
            }
        }
        timeEffs = newTimeEffs.toArray();

        int timeConsume = -1;
        int minDelta = Integer.MAX_VALUE;
        for(Object o : timeEffs) {
            if(minDelta==0) { break; }
            if(o instanceof TimeEff) {
                TimeEff te = (TimeEff) o;
                if(Math.abs(te.getRamAvailable()-ramAvailableMB) < minDelta) {
                    minDelta = Math.abs(te.getRamAvailable()-ramAvailableMB);
                    timeConsume = te.getTimeConsume();
                }
            }
        }
        return timeConsume;
    }

    @Override
    protected String[] createTableSql() {
        String[] tblTimeEfficiencyCols = new String[] {COL_TE_ID, COL_TE_RAM_AVAILABLE_MB, COL_TE_TIME_CONSUME, COL_TE_REPEAT, COL_TE_ACTIVITY_IN_BACKGROUND};
        String createTblTimeEfficiency = DatabaseHelper.generateSQLCreateTable(TBL_TIME_EFFICIENCY,
                tblTimeEfficiencyCols,
                DatabaseHelper.generateColArrayType(tblTimeEfficiencyCols.length, "TEXT"),
                0);
        return new String[] {createTblTimeEfficiency};
    }

    @Override
    protected void upgradeDb(SQLiteDatabase db, int versionFrom, int versionTo) {
        // first version. do nothing...
    }
}
