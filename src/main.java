import java.sql.Connection;

import database.Utils;

public class main {

	public static void main(String[] args) {
		question8();
	}
	
	public static void question8() {
		Utils.getConnection();
		Utils.closeConnection();
	}

}
