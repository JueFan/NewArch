package yy.juefan.cube;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import yy.juefan.basic.FileIO;

public class InsertNode {
	
	public static int ID = 1;
	/*树结构*/
	static Map<String, Node> treeMap = new HashMap<String, Node>();
	/*节点业务信息*/
	static Map<String, NodeInfo> infoMap = new HashMap<String, NodeInfo>();
	public static void setTreeMap(String a, String b, String name){
		if(!treeMap.containsKey(a)){
			Node buNode = new Node();
			buNode.name = name;
			buNode.nodeString = a;
			buNode.id = ID +=2;
			buNode.pid = treeMap.get(b).id;
			treeMap.put(a, buNode);
		}
	}

	public static void setNodeMap(String a, String b, String name, String label){
		if(!treeMap.containsKey(a)){
			Node buNode = new Node();
			buNode.label = label;
			buNode.name = name;
			buNode.nodeString = a;
			buNode.id = ID +=2;
			buNode.pid = treeMap.get(b).id;
			treeMap.put(a, buNode);
		}else {
			Node buNode = new Node();
			buNode.label = label;
			buNode.name = name;
			buNode.nodeString = a;
			buNode.id = ID +=2;
			buNode.pid = treeMap.get(b).id;
			treeMap.put(a + Integer.toString((int)(Math.random() * 100)), buNode);
		}
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
			
			ResultSet rs = stmt.executeQuery("select * from node_info_combine" );
			while(rs.next()){
				infoMap.put(rs.getString("label"), new NodeInfo(rs));
			}
			rs.close();
			
			Map<String, Integer> act2uidMap = new HashMap<String, Integer>();
			rs = stmt.executeQuery("select * from docs_pack" );
			while(rs.next()){
				act2uidMap.put(rs.getString("act"), rs.getInt("uid"));
			}
			rs.close();
			
			/**
			 * 更新表中的uid
			 */
			/*for(String act: act2uidMap.keySet()){
				stmt.executeUpdate("update node_info_combine set uid = " + act2uidMap.get(act) + " where label = '" + act + "'" );			
			}*/
			
			rs = stmt.executeQuery("select * from node_info_combine" );
			while (rs.next()) {
				String[] tmpStrings;
				String tmString = rs.getString("title");
				String bu = rs.getString("bu");
				String subbu = rs.getString("subbu");
				tmpStrings = tmString.replace(" ", "").split("--|-|–|——|—");
				if(!treeMap.containsKey(bu)){
					Node buNode = new Node();
					buNode.nodeString = bu;
					buNode.name = bu;
					buNode.id = ID +=2;
					buNode.pid = 1;
					treeMap.put(bu, buNode);
				}
				
				setTreeMap(bu + subbu, bu, subbu);
				subbu = bu + subbu;
				
				String tmp = new String();
				for(int i = 0; i < tmpStrings.length - 1; i++){
					tmp = tmpStrings[i];
					setTreeMap(subbu + tmp, subbu, tmp);
					subbu = subbu + tmp;
				}
				setNodeMap(subbu + tmpStrings[tmpStrings.length - 1], subbu, tmpStrings[tmpStrings.length - 1], rs.getString("label"));
				try {
					infoMap.get(rs.getString("label")).setId(ID);
				} catch (Exception e) {
					System.out.println("获取不到数据");
				}
			}
			
			/**
			 *  输出树结构插入语句
			 */
			StringBuilder treeBuilder = new StringBuilder();
			for(Node node: treeMap.values()){
				if(node.id >= 0 && node.id < 2000)
					if(act2uidMap.containsKey(node.label)){
						treeBuilder.append(node.id + "\tINSERT INTO node (id,pid, NAME, uid) VALUE(" + node.id + "," + node.pid + ",'" + node.name + "',"
						+act2uidMap.get(node.label) + ");\n");
					}else{
						treeBuilder.append(node.id + "\tINSERT INTO node (id,pid, NAME, uid) VALUE(" + node.id + "," + node.pid + ",'" + node.name + "',"
								+"27217" + ");\n");
					}
			}
			FileIO.FileWrite("node.txt", treeBuilder.toString(), false);
			
			/**
			 * 输出节点业务数据插入语句
			 */
			treeBuilder.delete(0, treeBuilder.length());
			for(NodeInfo node: infoMap.values()){
				if(node.id != 0)
					treeBuilder.append("INSERT INTO node_info (id, pc, web, mobile, `from`, label,description, uid) VALUE("
						+ node.id + "," + node.pc + "," + node.web + "," + node.mobile + ",'" + node.from + "','" + node.label + "','" +  node.description + "'," + node.uid + ");\n");
			}
			FileIO.FileWrite("node_info.txt", treeBuilder.toString(), false);
			
			/**
			 * 输出节点数据信息插入语句
			 */
			treeBuilder.delete(0, treeBuilder.length());
			for(NodeInfo node: infoMap.values()){
				if(node.from.equals("stanag")){
					treeBuilder.append("INSERT INTO node_hive (id, db, `table`, `field`, `value`, uid) VALUE("
							+ node.id +  ",'default','" + node.table + "','eventid'," + node.label +  "," + node.uid + ");\n");
				}else{
					treeBuilder.append("INSERT INTO node_hive (id, db, `table`, uid) VALUE("
							+ node.id +  ",'default','" + node.table +  "'," + node.uid + ");\n");
				}			
			}
			FileIO.FileWrite("node_hive.txt", treeBuilder.toString(), false);
			
		} catch (Exception e) {
			System.out.print("get data error!");
			e.printStackTrace();
		}
	}
}
