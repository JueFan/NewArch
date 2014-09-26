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
			
			//Hive�����
			ResultSet rs = stmt.executeQuery("select * from table_info" );
			while(rs.next()){
				Table table = new Table();
				table.setTableID(rs.getInt("id"));
				table.setTableName(rs.getString("table_name"));
				tableMap.put(rs.getInt("id"), table);
			}
			rs.close();
			
			//Hive���ֶ���Ϣ����
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
