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
			// ��ԭ�ڵ�����·��
			ResultSet rs = stmt.executeQuery("select * from node");
			while (rs.next()) {
				BuildNode.nodeMap.put(Integer.parseInt(rs.getString("id")),
						new Node(rs));
			}
			for (Node node : BuildNode.nodeMap.values()) {
				BuildNode.setTreeMap(node);
			}
			rs.close();

			// ��ȡ��ǰ���ڵ�IDֵ
			InsertNode.ID = BuildNode.ID;
			// �洢ҵ����Ϣ
			rs = stmt.executeQuery("select * from node_info_combine");
			while (rs.next()) {
				InsertNode.infoMap.put(rs.getString("label"), new NodeInfo(rs));
			}
			rs.close();

			// �洢�����ĵ�act��������uid
			Map<String, Integer> act2uidMap = new HashMap<String, Integer>();
			rs = stmt.executeQuery("select * from docs_pack");
			while (rs.next()) {
				act2uidMap.put(rs.getString("act"), rs.getInt("uid"));
			}
			rs.close();

			// �������õĽڵ���ǳ����
			InsertNode.treeMap = BuildNode.treeMap;
			// System.out.println(InsertNode.treeMap.size());
			rs = stmt.executeQuery("select * from node_info_combine");
			while (rs.next()) {
				String[] tmpStrings;
				String tmString = rs.getString("title");
				String bu = rs.getString("bu");
				String subbu = rs.getString("subbu");
				tmpStrings = tmString.replace(" ", "").split("--|-|�C|����|��");
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
					System.out.println("��ȡ��������");
				}
			}

			/**
			 * ������ṹ�������
			 */
			StringBuilder treeBuilder = new StringBuilder();
			TreeMap<Integer, Node> sortMap = new TreeMap<Integer, Node>(); 		//���ڵ���밴id��������
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
			 * ����ڵ�ҵ�����ݲ������
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
			 * ����ڵ�������Ϣ�������
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
