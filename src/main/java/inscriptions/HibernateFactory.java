package inscriptions;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateFactory {
	private static SessionFactory sessionFactory;
	private static Logger log =  LoggerFactory.getLogger(HibernateFactory.class);

	public static void main(String... args) {
		System.out.println(buildIfNeeded() != null);
	}

	/**
	 * Constructs a new Singleton SessionFactory
	 *
	 * @return
	 * @throws HibernateException
	 */
	public static void buildSessionFactory() throws HibernateException {
		if (sessionFactory != null) {
			closeFactory();
		}
		configureSessionFactory();
	}

	/**
	 * Builds a SessionFactory, if it hasn't been already.
	 */
	public static SessionFactory buildIfNeeded() {
		if (sessionFactory != null) {
			return sessionFactory;
		}
		try {
			return configureSessionFactory();
		} catch (HibernateException e) {
			throw new RuntimeException(e);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Session openSession() throws HibernateException {
		buildIfNeeded();
		return sessionFactory.openSession();
	}

	public static void closeFactory() {
		if (sessionFactory != null) {
			try {
				sessionFactory.close();
			} catch (HibernateException ignored) {
				log.error("Couldn't close SessionFactory", ignored);
			}
		}
	}

	public static void close(Session session) {
		if (session != null) {
			try {
				session.close();
			} catch (HibernateException ignored) {
				log.error("Couldn't close Session", ignored);
			}
		}
	}

	public static void rollback(Transaction tx) {
		try {
			if (tx != null) {
				tx.rollback();
			}
		} catch (HibernateException ignored) {
			log.error("Couldn't rollback Transaction", ignored);
		}
	}

	/**
	 * @return
	 * @throws HibernateException
	 */
	private static SessionFactory configureSessionFactory() throws HibernateException {
		log.info("Starting Configuring SessionFactory .......");

		StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()

				.configure()

				.build();

		sessionFactory = new MetadataSources(standardRegistry).buildMetadata().getSessionFactoryBuilder().build();
		log.info("Configuring SessionFactory with SUCCESS .......");

		return sessionFactory;
	}
}