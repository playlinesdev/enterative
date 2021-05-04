//package br.com.chart.enterative.web.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.flywaydb.core.Flyway;
//import org.flywaydb.core.api.callback.Callback;
//import org.flywaydb.core.api.callback.Context;
//import org.flywaydb.core.api.callback.Event;
//import org.flywaydb.core.api.configuration.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.core.annotation.Order;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.jdbc.datasource.init.ScriptUtils;
//import org.springframework.jdbc.support.JdbcUtils;
//import org.springframework.jdbc.support.MetaDataAccessException;
//import org.springframework.stereotype.Component;
//
//import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
//
//@Component
//@Order(HIGHEST_PRECEDENCE)
//@Slf4j
//public class FlywayUpgrader implements Callback {
//    private final Flyway flyway;
//
//    public FlywayUpgrader(@Lazy Flyway flyway) {
//        this.flyway = flyway;
//    }
//
//    private boolean checkColumnExists(Configuration flywayConfiguration) throws MetaDataAccessException {
//        return (boolean) JdbcUtils.extractDatabaseMetaData(flywayConfiguration.getDataSource(),
//                callback -> callback
//                        .getColumns(null, null, flywayConfiguration.getTable(), "version_rank")
//                        .next());
//    }
//
//    @Override
//    public boolean supports(Event event, Context context) {
//        return event == Event.BEFORE_VALIDATE;
//    }
//
//    @Override
//    public boolean canHandleInTransaction(Event event, Context context) {
//        return false;
//    }
//
//    @Override
//    public void handle(Event event, Context context) {
//        boolean versionRankColumnExists = false;
//        try {
//            versionRankColumnExists = checkColumnExists(context.getConfiguration());
//        } catch (MetaDataAccessException e) {
//            log.error("Cannot obtain flyway metadata");
//            return;
//        }
//        if (versionRankColumnExists) {
//            log.info("Upgrading metadata table the Flyway 4.0 format ...");
//            Resource resource = new ClassPathResource("db/migration/flyway_upgradev3Tov4.sql",
//                    Thread.currentThread().getContextClassLoader());
//            ScriptUtils.executeSqlScript(context.getConnection(), resource);
//            log.info("Flyway metadata table updated successfully.");
//            // recalculate checksums
//            flyway.repair();
//        }
//    }
//}