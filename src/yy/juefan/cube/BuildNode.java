package yy.juefan.cube;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuildNode {
	
	/*���ṹ*/
	static Map<String, Node> treeMap = new HashMap<String, Node>();

	static Map<Integer, Node> nodeMap = new HashMap<Integer, Node>();
	
	static int ID = 0;
	
	/**��ԭ�ڵ������·��*/
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
		//System.out.println("�ڵ�ID:" + node.id + "\t�ڵ�·��:" + builder.toString());
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
