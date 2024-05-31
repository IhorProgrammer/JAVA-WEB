package step.learning.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import step.learning.services.db.DBService;
import step.learning.services.db.MySQLDBService;
import step.learning.services.form.FormParseService;
import step.learning.services.form.HybridFormParser;
import step.learning.services.hash.HashService;
import step.learning.services.hash.MD5HashService;
import step.learning.services.kdf.HashKdfService;
import step.learning.services.kdf.KdfService;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        super.configure();
        bind(HashService.class).to(MD5HashService.class);
        bind(DBService.class).to(MySQLDBService.class);
        bind(FormParseService.class).to(HybridFormParser.class);
        bind(KdfService.class).to(HashKdfService.class);
    }
}
