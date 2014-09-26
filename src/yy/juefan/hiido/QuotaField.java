package yy.juefan.hiido;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class QuotaField {
	
	
	public static String convert(String utfString){
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;
		
		while((i=utfString.indexOf("\\u", pos)) != -1){
			sb.append(utfString.substring(pos, i));
			if(i+5 < utfString.length()){
				pos = i+6;
				sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));
			}
		}	
		return sb.toString();
	}
	
	public static void main(String args[]) {
		try {
			Class.forName("com.mysql.jdbc.Driver"); // 加载MYSQL JDBC驱动程序
		} catch (Exception e) {
			System.out.print("Error loading Mysql Driver!");
			e.printStackTrace();
		}
		try {
			Connection connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/hiido", "root", "baivfhpiaqg");
			// 连接URL为 jdbc:mysql//服务器地址/数据库名 ，后面的2个参数分别是登陆用户名和密码

			Statement stmt = connect.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT configuration from h_chart_info" );
			Map<String, Integer> map = new HashMap<String, Integer>();
			while(rs.next()){
				//System.out.println(convert(rs.getString(1)));
				String[] strings = convert(rs.getString(1)).split(",");
				//System.out.println(strings[0]);
				for(String s: strings){
					if(s.contains(":") && !s.contains("{")){
						String field = s.split(":")[1].replace("[", "");
						if(map.containsKey(field))
							map.put(field, map.get(field) + 1);
						else {
							map.put(field, 1);
						}
					}
				}
			}
			for(String field: map.keySet())
				System.out.println(field + "\t" + map.get(field));
		}catch(Exception e){
			
		}
	}

}
