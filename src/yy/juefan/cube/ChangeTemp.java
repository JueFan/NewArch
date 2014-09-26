package yy.juefan.cube;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ChangeTemp {
	
	
	public static void main(String args[]) {
		try {
			Class.forName("com.mysql.jdbc.Driver"); // 加载MYSQL JDBC驱动程序
		} catch (Exception e) {
			System.out.print("Error loading Mysql Driver!");
			e.printStackTrace();
		}
		try {
			Connection connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/cube", "root", "baivfhpiaqg");
			// 连接URL为 jdbc:mysql//服务器地址/数据库名 ，后面的2个参数分别是登陆用户名和密码

			Statement stmt = connect.createStatement();
			
			ResultSet rs = stmt.executeQuery("select id from node_info where pc = 1 and `from` = 'stanag'" );
			while(rs.next()){
				System.out.println("update node_hive set `table` = 'yy_tlv_original' where id = " + rs.getString("id") + ";");
			}
			rs.close();
		}catch(Exception exception){
			System.out.println("出错");
		}
	}
}
