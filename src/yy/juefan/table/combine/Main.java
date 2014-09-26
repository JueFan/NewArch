package yy.juefan.table.combine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {
	public static void main(String[] args) {

		Map<Integer, Table> tableMap = new HashMap<Integer, Table>();
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
			
			//Hive表加载
			ResultSet rs = stmt.executeQuery("select * from table_info" );
			while(rs.next()){
				Table table = new Table();
				table.setTableID(rs.getInt("id"));
				table.setTableName(rs.getString("table_name"));
				tableMap.put(rs.getInt("id"), table);
			}
			rs.close();
			
			//Hive表字段信息加载
			rs = stmt.executeQuery("select * from table_field_info" );
			while(rs.next()){
				Field field = new Field(rs);
				if(!field.field_name.equals("uid"))
				tableMap.get(field.table_id).fieldMap.put(tableMap.get(field.table_id).fieldID++, field);
			}
			rs.close();
		}catch(Exception exception){
			
		}
System.out.println(tableMap.get(1).getSelect());
for(Field field: Table.structFields){
	System.out.println(field.toString());
}
	}

}
