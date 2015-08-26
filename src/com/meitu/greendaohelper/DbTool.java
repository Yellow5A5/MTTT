package com.meitu.greendaohelper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.meitu.greendaohelper.DaoMaster.DevOpenHelper;

public class DbTool {

  private Context mContext;
  private SQLiteDatabase db;
  private DaoMaster daoMaster;
  private DaoSession daoSession;
  private BianKuangEntityDao BianKuangDao;
  private GaoXiaoBiaoQingEntityDao gaoXiaoDao;
  private ZheDangEntityDao zheDangDao;
  
  public DbTool(Context context) {
    this.mContext = context;
  }
  
  /**
   * 实例化一个数据库对象/TODO 
   */
  public void NewDbInstance() {
    DevOpenHelper helper = new DevOpenHelper(mContext, "image-db", null);
    db = helper.getWritableDatabase();
    daoMaster = new DaoMaster(db);
    daoSession = daoMaster.newSession();
    BianKuangDao = daoSession.getBianKuangEntityDao();
    gaoXiaoDao = daoSession.getGaoXiaoBiaoQingEntityDao();
    zheDangDao = daoSession.getZheDangEntityDao();
  }
  
  /**
   * 获取表中所有图片的名字倒序插入中返回。
   * @return 所有图片的名字的集合
   */
  public List<BianKuangEntity> loadAllImageName() {  
    List<BianKuangEntity> sessions = new ArrayList<BianKuangEntity>();  
    List<BianKuangEntity> tmpSessions = BianKuangDao.loadAll();  
    int len = tmpSessions.size();  
    for (int i = len-1; i >=0; i--) {  
            sessions.add(tmpSessions.get(i));  
    }  
    return sessions;  
} 
  
  /**
   * 插入边框文件名
   * @param nameThumb 小图标名 
   * @param name 大图标名
   */
  public void addBianKuangEntity(String nameThumb, String name){
    BianKuangEntity entity = new BianKuangEntity(null,nameThumb,name);
    BianKuangDao.insert(entity);
  }
  
  /**
   * 插入搞笑表情文件名
   * @param nameThumb 小图标名 
   * @param name 大图标名
   */
  public void addGaoXiaoEntity(String nameThumb, String name){
    GaoXiaoBiaoQingEntity entity = new GaoXiaoBiaoQingEntity(null,nameThumb,name);
    gaoXiaoDao.insert(entity);
  }
  
  /**
   * 插入遮挡贴纸文件名
   * @param nameThumb 小图标名 
   * @param name 大图标名
   */
  public void addZheDangEntity(String nameThumb, String name){
    ZheDangEntity entity = new ZheDangEntity(null,nameThumb,name);
    zheDangDao.insert(entity);
  }
  
}
