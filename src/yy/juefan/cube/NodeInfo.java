package yy.juefan.cube;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NodeInfo {
	public int id;
	public int pc = 0;
	public int web = 0;
	public int mobile = 0;
	public String pm;
	public String coder;
	public String operator;
	public String from;
	public String label;
	public String table;
	public String url;
	public String pic;
	public String description;
	public int uid = 27217;
	public String state;
	public String create_time;
	public String update_time;
	
	
	public void setId(int id){
		this.id = id;
	}
	
	public NodeInfo(ResultSet rs) throws SQLException{
		switch (rs.getString("platform")){
		case "pc":
			pc = 1;
			break;
		case "web":
			web = 1;
			break;
		case "mobile":
			mobile = 1;
			break;
		}
		
		if(rs.getString("from").equals("stanag"))
			from = "stanag";
		else {
			from = "docs";
		}
		 
		label = rs.getString("label");
		table = rs.getString("table");
		description = rs.getString("trigger");
		uid = rs.getInt("uid");
	}
}
