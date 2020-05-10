package ru.otus;

import ru.otus.core.dao.EntityDao;
import ru.otus.core.model.Account;
import ru.otus.core.model.User;
import ru.otus.core.service.DBService;
import ru.otus.core.service.DbServiceImpl;
import ru.otus.core.sqlmapper.SqlMapper;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.dao.EntityDaoJdbc;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;
import ru.otus.jdbc.sqlmapper.SqlMapperJdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class DbServiceDemo {

    public static void main(String[] args) throws Exception {
        DataSource dataSource = new DataSourceH2();
        DbServiceDemo demo = new DbServiceDemo();

        demo.createTables(dataSource);

        SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
        DbExecutor<User> dbExecutor = new DbExecutor<>();
        SqlMapper<User> sqlMapper = new SqlMapperJdbc(dbExecutor);
        EntityDao entityDao = new EntityDaoJdbc(sessionManager, sqlMapper);

        DBService<User> dbService = new DbServiceImpl(entityDao);
        long id = dbService.saveEntityToDb(new User(0, "Onizuka Ekichi", 22));
        Optional<User> optionalUser = dbService.getEntityById(User.class, id);
        User user = optionalUser.orElseThrow(() -> new AssertionError("User was not created"));
        System.out.println("Selected user: " + user);
        assertThat(user.getId()).as("User id should be filled").isEqualTo(id);
        assertThat(user.getAge()).as("User Age").isEqualTo(22);
        assertThat(user.getName()).as("User name").isEqualTo("Onizuka Ekichi");


        SqlMapper<Account> sqlAccountMapper = new SqlMapperJdbc(dbExecutor);
        EntityDao accountDao = new EntityDaoJdbc(sessionManager, sqlAccountMapper);

        DBService<Account> dbServiceAccount = new DbServiceImpl(accountDao);
        long no = dbServiceAccount.saveEntityToDb(new Account(null, "Totoro", 100));
        Optional<Account> optionalAccount = dbServiceAccount.getEntityById(Account.class, no);
        Account account = optionalAccount.orElseThrow(() -> new AssertionError("User was not created"));
        System.out.println("Selected Account: " + account);
        assertThat(account.getNo()).as("Account no should be filled").isEqualTo(no);
        assertThat(account.getType()).as("Account type").isEqualTo("Totoro");
        assertThat(account.getRest()).as("Account rest").isEqualTo(100);

    }

    private void createTables(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement("create table User(id long auto_increment, name varchar(50), age int(3))")) {
            pst.executeUpdate();
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement accountTable = connection.prepareStatement("create table Account(no bigint(20) NOT NULL auto_increment, type varchar(255), rest number)")) {
            accountTable.executeUpdate();
        }
    }
}
