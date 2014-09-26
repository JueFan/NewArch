package yy.juefan.cube;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import yy.juefan.basic.FileIO;

public class NewNode {

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
			// 还原节点完整路径
			ResultSet rs = stmt.executeQuery("select * from node");
			while (rs.next()) {
				BuildNode.nodeMap.put(Integer.parseInt(rs.getString("id")),
						new Node(rs));
			}
			for (Node node : BuildNode.nodeMap.values()) {
				BuildNode.setTreeMap(node);
			}
			rs.close();

			// 获取当前最大节点ID值
			InsertNode.ID = BuildNode.ID;
			// 存储业务信息
			rs = stmt.executeQuery("select * from node_info_combine");
			while (rs.next()) {
				InsertNode.infoMap.put(rs.getString("label"), new NodeInfo(rs));
			}
			rs.close();

			// 存储发包文档act与申请人uid
			Map<String, Integer> act2uidMap = new HashMap<String, Integer>();
			rs = stmt.executeQuery("select * from docs_pack");
			while (rs.next()) {
				act2uidMap.put(rs.getString("act"), rs.getInt("uid"));
			}
			rs.close();

			// 将构建好的节点树浅复制
			InsertNode.treeMap = BuildNode.treeMap;
			// System.out.println(InsertNode.treeMap.size());
			rs = stmt.executeQuery("select * from node_info_combine");
			while (rs.next()) {
				String[] tmpStrings;
				String tmString = rs.getString("title");
				String bu = rs.getString("bu");
				String subbu = rs.getString("subbu");
				tmpStrings = tmString.replace(" ", "").split("--|-|–|——|—");
				if (!InsertNode.treeMap.containsKey(bu)) {
					Node buNode = new Node();
					buNode.nodeString = bu;
					buNode.name = bu;
					buNode.id = InsertNode.ID += 2;
					buNode.pid = 1;
					InsertNode.treeMap.put(bu, buNode);
				}

				InsertNode.setTreeMap(bu + subbu, bu, subbu);
				subbu = bu + subbu;

				String tmp = new String();
				for (int i = 0; i < tmpStrings.length - 1; i++) {
					tmp = tmpStrings[i];
					InsertNode.setTreeMap(subbu + tmp, subbu, tmp);
					subbu = subbu + tmp;
				}
				InsertNode.setNodeMap(
						subbu + tmpStrings[tmpStrings.length - 1], subbu,
						tmpStrings[tmpStrings.length - 1],
						rs.getString("label"));
				try {
					InsertNode.infoMap.get(rs.getString("label")).setId(
							InsertNode.ID);
				} catch (Exception e) {
					System.out.println("获取不到数据");
				}
			}

			/**
			 * 输出树结构插入语句
			 */
			StringBuilder treeBuilder = new StringBuilder();
			TreeMap<Integer, Node> sortMap = new TreeMap<Integer, Node>(); 		//树节点插入按id进行排序
			for(Node node: InsertNode.treeMap.values()){
				if (node.id > BuildNode.ID)
					sortMap.put(node.id, node);
			}
			for (Node node : sortMap.values()) {
					if (act2uidMap.containsKey(node.label)) {
						treeBuilder
								.append("INSERT INTO node (id,pid, NAME, uid) VALUE("
										+ node.id + "," + node.pid + ",'"
										+ node.name + "',"
										+ act2uidMap.get(node.label) + ");\n");
					} else {
						treeBuilder
								.append("INSERT INTO node (id,pid, NAME, uid) VALUE("
										+ node.id + "," + node.pid + ",'"
										+ node.name + "'," + "27217" + ");\n");
					}
			}
			FileIO.FileWrite(".\\file\\node.txt", treeBuilder.toString(), false);

			/**
			 * 输出节点业务数据插入语句
			 */
			treeBuilder.delete(0, treeBuilder.length());
			for (NodeInfo node : InsertNode.infoMap.values()) {
				if (node.id > BuildNode.ID)
					treeBuilder
							.append("INSERT INTO node_info (id, pc, web, mobile, `from`, label,description, uid) VALUE("
									+ node.id
									+ ","
									+ node.pc
									+ ","
									+ node.web
									+ ","
									+ node.mobile
									+ ",'"
									+ node.from
									+ "','"
									+ node.label
									+ "','"
									+ node.description
									+ "',"
									+ node.uid
									+ ");\n");
			}
			FileIO.FileWrite(".\\file\\node_info.txt", treeBuilder.toString(),
					false);

			/**
			 * 输出节点数据信息插入语句
			 */
			treeBuilder.delete(0, treeBuilder.length());
			for (NodeInfo node : InsertNode.infoMap.values()) {
				if (node.id > BuildNode.ID) {
					if (node.from.equals("stanag")) {
						treeBuilder
								.append("INSERT INTO node_hive (id, db, `table`, `field`, `value`, uid) VALUE("
										+ node.id
										+ ",'default','"
										+ node.table
										+ "','eventid',"
										+ node.label
										+ ","
										+ node.uid + ");\n");
					} else {
						treeBuilder
								.append("INSERT INTO node_hive (id, db, `table`, uid) VALUE("
										+ node.id
										+ ",'default','"
										+ node.table
										+ "'," + node.uid + ");\n");
					}
				}
			}
			FileIO.FileWrite(".\\file\\node_hive.txt", treeBuilder.toString(),
					false);

		} catch (Exception e) {
			System.out.print("get data error!");
			e.printStackTrace();
		}
	}
}
