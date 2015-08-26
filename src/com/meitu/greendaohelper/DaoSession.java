package com.meitu.greendaohelper;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig gaoXiaoBiaoQingEntityDaoConfig;
    private final DaoConfig zheDangEntityDaoConfig;
    private final DaoConfig bianKuangEntityDaoConfig;

    private final GaoXiaoBiaoQingEntityDao gaoXiaoBiaoQingEntityDao;
    private final ZheDangEntityDao zheDangEntityDao;
    private final BianKuangEntityDao bianKuangEntityDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        gaoXiaoBiaoQingEntityDaoConfig = daoConfigMap.get(GaoXiaoBiaoQingEntityDao.class).clone();
        gaoXiaoBiaoQingEntityDaoConfig.initIdentityScope(type);

        zheDangEntityDaoConfig = daoConfigMap.get(ZheDangEntityDao.class).clone();
        zheDangEntityDaoConfig.initIdentityScope(type);

        bianKuangEntityDaoConfig = daoConfigMap.get(BianKuangEntityDao.class).clone();
        bianKuangEntityDaoConfig.initIdentityScope(type);

        gaoXiaoBiaoQingEntityDao = new GaoXiaoBiaoQingEntityDao(gaoXiaoBiaoQingEntityDaoConfig, this);
        zheDangEntityDao = new ZheDangEntityDao(zheDangEntityDaoConfig, this);
        bianKuangEntityDao = new BianKuangEntityDao(bianKuangEntityDaoConfig, this);

        registerDao(GaoXiaoBiaoQingEntity.class, gaoXiaoBiaoQingEntityDao);
        registerDao(ZheDangEntity.class, zheDangEntityDao);
        registerDao(BianKuangEntity.class, bianKuangEntityDao);
    }
    
    public void clear() {
        gaoXiaoBiaoQingEntityDaoConfig.getIdentityScope().clear();
        zheDangEntityDaoConfig.getIdentityScope().clear();
        bianKuangEntityDaoConfig.getIdentityScope().clear();
    }

    public GaoXiaoBiaoQingEntityDao getGaoXiaoBiaoQingEntityDao() {
        return gaoXiaoBiaoQingEntityDao;
    }

    public ZheDangEntityDao getZheDangEntityDao() {
        return zheDangEntityDao;
    }

    public BianKuangEntityDao getBianKuangEntityDao() {
        return bianKuangEntityDao;
    }

}