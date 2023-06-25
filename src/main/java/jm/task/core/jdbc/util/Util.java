package jm.task.core.jdbc.util;


import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.Configuration;


import javax.imageio.spi.ServiceRegistry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String hostName = "localhost";
    private static final String dbName = "table";
    private static final String userName = "root";
    private static final String password = "Lijyg741";
    private static SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;
        Connection conn = DriverManager.getConnection(connectionURL, userName, password);
        return conn;
    }
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties setting = new Properties();
                setting.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                setting.put(Environment.URL, "jdbc:mysql://" + hostName + ":3306/" + dbName);
                setting.put(Environment.USER, userName);
                setting.put(Environment.PASS, password);
                setting.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
                setting.put(Environment.SHOW_SQL, "true");
                setting.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "tread");
                setting.put(Environment.HBM2DDL_AUTO, "create-drop");
                configuration.setProperties(setting);
                configuration.addAnnotatedClass(User.class);
                ServiceRegistry serviceRegistry = (ServiceRegistry) new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory((org.hibernate.service.ServiceRegistry) serviceRegistry);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sessionFactory;
    }
    public Session getSession() {
        return session;
    }
    public Transaction getTransaction() {
        return transaction;
    }
    public Session openSession() {
        return Util.getSessionFactory().openSession();
    }
    public Session openTransactionSession() {
        session = openSession();
        transaction = session.beginTransaction();
        return session;
    }
    public void closeSession() {
        session.close();
    }
    public void closeTransactionSession() {
        transaction.commit();
        closeSession();
    }
}
