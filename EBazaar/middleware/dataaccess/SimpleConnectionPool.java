package middleware.dataaccess;

import java.util.LinkedList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Queue;
import java.util.logging.Logger;
import static business.util.StringParse.*;

import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.externalinterfaces.Cleanup;
import middleware.externalinterfaces.DbConfigKey;

//remove this import statement for jdk 1.4
//import java.util.Queue;

/**
 * Here is the typical way to use this class; the query variable stores the
 * necessary SQL statement: SimpleConnectionPool pool =
 * SimpleConnectionPool.getInstance(DataAccessSubsystemFacade.MAX_CONNECTIONS);
 * Connection con = pool.getConnection(dbUrl); ResultSet rs =
 * SimpleConnectionPool.doQuery(con,query); pool.returnToPool(con,dbUrl);
 */
class SimpleConnectionPool {
	static DbConfigProperties props = new DbConfigProperties();
	// maps URI -> free connections queue
	private HashMap<String, Queue<Connection>> freeConnectionsMap = new HashMap<String, Queue<Connection>>();

	// maps URI -> numclients (Integer)
	private static HashMap<String, Integer> numClientsMap = new HashMap<String, Integer>();
	private static Logger log = Logger.getLogger(SimpleConnectionPool.class
			.getPackage().getName());
	private final String DEFAULT_DRIVER = props.getProperty(DbConfigKey.DRIVER
			.getVal());
	private final String DEFAULT_USER = props.getProperty(DbConfigKey.DB_USER
			.getVal());
	private final String DEFAULT_MAX_CONN = props
			.getProperty(DbConfigKey.MAX_CONNECTIONS.getVal());

	private static SimpleConnectionPool instance;

	private String dbuser;

	private String dbpass;

	private String drivername;

	private int maxconn;

	private Queue<Connection> freeConnections(String URI) {
		return freeConnectionsMap.get(URI);

	}

	private int numFreeConnections(String URI) {
		Queue<Connection> cons = freeConnections(URI);
		if (cons == null)
			return 0;
		return cons.size();

	}

	private boolean noClientsSoFar(String URI) {
		Integer numCl = numClientsMap.get(URI);
		return (numCl == null || numCl.intValue() == 0);
	}

	private static void incrementNumClients(String URI) {
		Integer num = numClientsMap.get(URI);
		if (num == null) {
			numClientsMap.put(URI, new Integer(1));

		} else {
			Integer next = new Integer(num.intValue() + 1);
			numClientsMap.put(URI, next);
		}

	}

	/**
	 * This version is for use by Cleanup only -- instances should be gotten by
	 * passing in correct URI
	 * 
	 * @return SimpleConnectionPool
	 */
	static synchronized SimpleConnectionPool getInstance(Cleanup c) {
		if (instance == null) {
			log.info("No instance of SimpleConnectionPool has been created..." +
				"returning null");
			return null;
		} else {
			return instance;
		}
	}

	/*
	 * the default behavior for getInstance: no username or pwd, and use the
	 * default driver
	 */
	static synchronized SimpleConnectionPool getInstance(int maxconn)
			throws DatabaseException {
		return getInstance("", "", "", maxconn);
	}

	static synchronized SimpleConnectionPool getInstance(String dbuser,
			String dbpass, String drivername, int maxconn)
			throws DatabaseException {
		if (instance == null) {
			instance = new SimpleConnectionPool(dbuser, dbpass, drivername,
					maxconn);
		}

		return instance;
	}

	private void initializePool(String URI) throws DatabaseException {
		for (int i = 0; i < maxconn; ++i) {
			createConnection(URI);

		}
	}

	private SimpleConnectionPool(String dbuser, String dbpass,
			String drivername, int maxconn) throws DatabaseException {

		this.dbuser = isEmptyString(dbuser) ? DEFAULT_USER : dbuser;
		this.dbpass = dbpass;
		this.drivername = isEmptyString(drivername) ? DEFAULT_DRIVER
				: drivername;
		this.maxconn = isEmptyString(maxconn) ? Integer
				.parseInt(DEFAULT_MAX_CONN) : maxconn;
		loadJDBCDriver();
	}

	private void loadJDBCDriver() throws DatabaseException {
		try {
			Class.forName(drivername);
		} catch (java.lang.ClassNotFoundException e) {
			log.warning("ClassNotFoundException: " + e.getMessage());
			throw new DatabaseException(e);
		}

	}

	synchronized Connection getConnection(String URI) throws DatabaseException {
		if (noClientsSoFar(URI)) {
			initializePool(URI);
		}
		Connection con = null;
		Queue<Connection> freeConnections = freeConnections(URI);
		if (freeConnections != null && !freeConnections.isEmpty()) {
			log.info("Returning a live connection from the pool");
			con = freeConnections.poll();
			try {
				if (con.isClosed()) {
					log.info("Removed closed connection!");
					// Try again recursively
					con = getConnection(URI);
				}
			} catch (SQLException e) {
				log.info("Removed closed connection!");
				// Try again recursively
				con = getConnection(URI);
			} catch (Exception e) {
				log.info("Removed closed connection!");
				// Try again recursively
				con = getConnection(URI);
			}
		} else {
			con = createConnection(URI);
		}
		incrementNumClients(URI);
		return con;
	}

	private void addConnection(String URI, Connection con) {
		Queue<Connection> freeForURI = freeConnectionsMap.get(URI);
		if (freeForURI == null) {
			freeForURI = new LinkedList<Connection>();
		}

		freeForURI.add(con);
		freeConnectionsMap.put(URI, freeForURI);
	}

	private Connection createConnection(String URI) throws DatabaseException {
		Connection con = null;
		try {
			if (this.dbuser == null || dbuser.equals("")) {
				con = DriverManager.getConnection(URI);
			} else {
				con = DriverManager
						.getConnection(URI, this.dbuser, this.dbpass);
			}
			// add connection to pool
			log.info("Adding new connection to pool");
			addConnection(URI, con);

		} catch (SQLException e) {
			log.warning("Unable to create a connection to database with dburl "
					+ URI);
			throw new DatabaseException("Database is unavailable.");
		}
		return con;
	}

	static ResultSet doQuery(Connection con, String query)
			throws DatabaseException {
		if (con == null) {
			log.warning("Connection is null -- cannot perform query");
			return null;
		}
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

		} catch (SQLException ex) {
			log.warning("SQLQueryException: " + ex.getMessage());
			throw new DatabaseException(ex);
		}
		return rs;
	}

	/**
	 * @param con - the connection object
	 * @param query - the SQL query
	 * @return - number of rows affected if update or delete; generated key if
	 * insert with auto-increment key field.
	 * @throws DatabaseException
	 */
	static Integer doUpdate(Connection con, String query)
			throws DatabaseException {
		if (con == null) {
			log.warning("Connection is null -- cannot perform update");
		}

		Statement stmt = null;
		Integer generatedKey = null;
		try {
			stmt = con.createStatement();
			log.info("query: " + query);
			int numRows = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				generatedKey = rs.getInt(1);
				log.info("Generated key for auto_increment id column after insert: "
						+ generatedKey);
			} else {
				log.info("No generated key for " + query);
			}
			return (generatedKey == null) ? numRows : generatedKey;
		} catch (SQLException ex) {
			log.warning("SQLQueryException: " + ex.getMessage());
			throw new DatabaseException(ex);
		}

	}

	synchronized void returnToPool(Connection con, String URI) {
		try {
			if ((con != null) && (!con.isClosed())
					&& (numFreeConnections(URI) < this.maxconn)) {
				log.info("Returning a connection to pool");
				con.setAutoCommit(true);
				addConnection(URI, con);
			}
		} catch (Exception e) {
			log.warning("Unable to return connection to pool. Proceeding..."
					+ e.getMessage());
			// do nothing -- just don't return con to the pool
		}
	}

	synchronized void closeConnections() {
		log.info("Closing all connections");
		Collection<Queue<Connection>> conLists = freeConnectionsMap.values();
		for (Queue<Connection> list : conLists) {
			releaseConnectionsInQueue(list);
		}
	}

	private synchronized void releaseConnectionsInQueue(
			Queue<Connection> freeConnections) {
		if (freeConnections == null)
			return;
		for (Connection con : freeConnections) {
			try {
				con.close();
			} catch (SQLException e) {
				log.warning("Cannot close connection! (Probably already closed?)");
			}
		}
		freeConnections.clear();
	}

	private static void testError() {
		log.info("Please provide a db url and a test query for the testConnection method " +
			"and a value for maxconn > 0");
	}

	static void testConnection(String url, String query, int maxconn) {
		if (url == null || url.length() == 0 || query == null
				|| query.length() == 0 || maxconn <= 0) {
			testError();
		}
		SimpleConnectionPool pool = null;
		Connection con = null;
		try {
			pool = SimpleConnectionPool.getInstance(maxconn);
			log.info("creating initial connections...");
			con = pool.getConnection(url);
			log.info("got a connection");

			ResultSet rs = SimpleConnectionPool.doQuery(con, query);
			log.info("statement created...");
			log.info("executing query...");

			int count = 0;

			while (rs.next()) {
				++count;
				log.info("row " + count + ": " + rs.getString("productname"));
			}
		} catch (Exception e) {
			log.warning("Error occurred trying to read table: "
					+ e.getClass().getName() + " Message: " + e.getMessage());
		}
		finally {
			pool.returnToPool(con, url);
		}
		pool.closeConnections();
	}

	public static void main(String[] args) {
		// insert your own db url and test query
		String testQuery = "SELECT * FROM product";// "select productname from product";
		testConnection("jdbc:odbc:EbazProducts", testQuery, 8);
	}

}
