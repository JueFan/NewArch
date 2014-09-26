package yy.juefan.cube;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class NodeHiveUid {
	
	public static String chooseUser(Set<String> set){
		if(set.contains("uid"))
			return "uid";
		if(set.contains("passport"))
			return "passport";
		if(set.contains("ui"))
			return "ui";
		if(set.contains("imei"))
			return "imei";
		if(set.contains("mac"))
			return "mac";
		if(set.contains("wyy"))
			return "wyy";
		else {
			return "��";
		}
	}
	
	public static String getUser(String field){
		Set<String> user = new HashSet<String>();
		String[] strings = field.split(",");
		for(String s: strings){
			user.add(s.split(":")[0]);
		}
		return chooseUser(user);	
	}
	
	
	public static void main(String args[]) {
		try {
			Class.forName("com.mysql.jdbc.Driver"); // ����MYSQL JDBC��������
		} catch (Exception e) {
			System.out.print("Error loading Mysql Driver!");
			e.printStackTrace();
		}
		try {
			Connection connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/cube", "root", "baivfhpiaqg");
			// ����URLΪ jdbc:mysql//��������ַ/���ݿ��� �������2�������ֱ��ǵ�½�û���������

			Statement stmt = connect.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT b.table, b.field FROM `node_hive` a " +
					"JOIN `node_info_combine` b ON a.`table` = b.`table` WHERE b.table != 'yy_webevent_original'" );
			while(rs.next()){
				System.out.println("update node_hive set user_label = '" + getUser(rs.getString("field")) + "' where `table` = '" + rs.getString("table")  + "';" );				
			}
		}catch(Exception e){
			
		}
	}
}
