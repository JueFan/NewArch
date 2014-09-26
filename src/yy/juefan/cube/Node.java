package yy.juefan.cube;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Node{
	public String label;
	public String nodeString;
	public String name;
	public int id;
	public int pid;
	
	public Node(ResultSet rs) throws NumberFormatException, SQLException{
		id = Integer.parseInt(rs.getString("id"));
		pid = Integer.parseInt(rs.getString("pid"));
		name = rs.getString("name");
	}

	public Node() {
		// TODO Auto-generated constructor stub
	}
}