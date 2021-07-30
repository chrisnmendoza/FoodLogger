package pkgLogic;

public class ServerInfo {
	private String ip;
	private String port;
	private String database;
	private String username;
	private String password;
	
	public ServerInfo(String ip, String port, String database, String username, String password) {
		this.ip = ip;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public String getDatabase() {
		return database;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getPort() {
		return port;
	}
	
}