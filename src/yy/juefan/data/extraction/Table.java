package yy.juefan.data.extraction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Table{
	public int id;
	public String from = new String();
	public String db = new String();
	public String table = new String();
	public String user_label = new String();
	public int value = 0;
	public String field = new String();
	public int plat = 0;
	public boolean state = true;
	
	public String toString(){
		return this.id + "\t" + (state? this.from + "\t" + this.db + "." + this.table + "\t" + this.user_label
				:"不存在");
	}
	public Table(int id){
		this.id = id;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/cube", "root", "baivfhpiaqg");
			Statement stmt = connect.createStatement();
			// 还原节点完整路径
			ResultSet rs = stmt.executeQuery("select `from`  from node_info where id = " + id);
			if(rs.next()){
				this.from = rs.getString(1);
			}else {
				state = state & false;
			}
			rs.close();
			rs = stmt.executeQuery("select db, `table`, user_label, value, `field` from node_hive where id = " + id);
			if(rs.next()){
				db = rs.getString(1);
				table = rs.getString(2);
				user_label = rs.getString(3);
				value = rs.getInt(4);
				field = rs.getString(5);
			}else {
				state = state & false;
			}
			rs.close();
			rs = stmt.executeQuery("select pc, web, mobile from node_info where id = " + id);
			if(rs.next()){
				if(rs.getInt(1) == 1){
					plat = 1;
				}else if (rs.getInt(2) == 1) {
					plat = 2;
				}else {
					plat = 3;
				}				
			}else {
				state = state & false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}