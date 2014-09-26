package yy.juefan.cube;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuildNode {
	
	/*树结构*/
	static Map<String, Node> treeMap = new HashMap<String, Node>();

	static Map<Integer, Node> nodeMap = new HashMap<Integer, Node>();
	
	static int ID = 0;
	
	/**还原节点的完整路径*/
	public static void setTreeMap(Node node){
		if(node.id > ID)
			ID = node.id;
		int pid = node.pid;
		int id = node.id;
		StringBuilder builder = new StringBuilder();
		ArrayList<String> nameList = new ArrayList<String>();
		while(id != 1){
			nameList.add(nodeMap.get(id).name);
			pid = nodeMap.get(id).pid;
			id = nodeMap.get(pid).id;
		}
		//nameList.add(nodeMap.get(id).name);
		for(int i = nameList.size() - 1; i >= 0; i--){
			builder.append(nameList.get(i));
		}
		treeMap.put(builder.toString(), node);
		//System.out.println("节点ID:" + node.id + "\t节点路径:" + builder.toString());
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
					"jdbc:mysql://localhost:3306/cube", "root", "baivfhpiaqg");
			// 连接URL为 jdbc:mysql//服务器地址/数据库名 ，后面的2个参数分别是登陆用户名和密码

			Statement stmt = connect.createStatement();
			
			ResultSet rs = stmt.executeQuery("select * from node" );
			while(rs.next()){
				nodeMap.put(Integer.parseInt(rs.getString("id")), new Node(rs));
			}
			for(Node node: nodeMap.values()){
				setTreeMap(node);
			}
			for(String s: treeMap.keySet())
				System.out.println(s);
		}catch(Exception e){
			
		}
	}

}
