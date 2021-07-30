package pkgEnums;

public enum eModes {
	VIEWALL, VIEWSOME, VIEWONE, INSERT, QUIT, ERROR;
	
	public eModes formatIntoEmode(String s) {
		if(s.equals("show all")) {
			return VIEWALL;
		}
		else if(s.equals("show some")) {
			return VIEWSOME;
		}
		else if(s.equals("show a food")) {
			return VIEWONE;
		}
		else if(s.equals("insert")) {
			return INSERT;
		}
		else if(s.equals("quit")) {
			return QUIT;
		}
		else {
			return ERROR;
		}
	}
}