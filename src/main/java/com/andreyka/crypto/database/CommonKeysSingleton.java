package com.andreyka.crypto.database;

import com.andreyka.crypto.models.keyexchange.CommonKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.dbutils.ResultSetHandler;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Map;

public enum CommonKeysSingleton {
    INSTANCE;

    public static final CommonKeysRepository repository = new CommonKeysRepository();

    public static void main(String[] args) throws SQLException {
        CommonKey commonKey = new CommonKey();
        commonKey.setCommonKey(new BigInteger("1231223123ababaca", 16));
        commonKey.setUserId(1);

        repository.save(commonKey);
        System.out.println(repository.findByUserId(commonKey.getUserId()));
    }

    public static class CommonKeysRepository extends Repository<CommonKey> {

        public CommonKey findByUserId(final long userId) throws SQLException {
            String sqlStatement = "SELECT * FROM common_keys WHERE userId = ?";
            return query(sqlStatement, userId);
        }

        public void save(CommonKey commonKey) throws SQLException {
            String sqlStatement = "INSERT INTO common_keys VALUES (?, ?)";
            execute(sqlStatement, commonKey.getUserId(), commonKey.getCommonKey().toString(10));
        }

        public void update(CommonKey commonKey) {
            String sqlStatement = "UPDATE common_keys SET commonKey WHERE userId = ?";
            execute(sqlStatement, commonKey.getCommonKey().toString(10), commonKey.getUserId());
        }

        protected ResultSetHandler<CommonKey> getHandler() {
            return rs -> {
                if (!rs.next()) {
                    return null;
                }

                Map<String, Object> result = new LinkedMap<>();
                Field[] fields = CommonKey.class.getDeclaredFields();

                for (Field field : fields) {
                    result.put(field.getName(), rs.getObject(field.getName()));
                }

                return new ObjectMapper().convertValue(result, CommonKey.class);
            };
        }
    }

}