package step.learning.services.db;

import com.google.inject.Singleton;

import java.sql.Connection;

public interface DBService {
    Connection getConnection();
}
