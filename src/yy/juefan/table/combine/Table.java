package yy.juefan.table.combine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
	public String tableName;
	public int tableID;
	public boolean plat = true;
	public int fieldID = 0;
	public String condition;	//表的查询条件
	public static final String SJ = "sj";
	public static final String WEB = "web";
	public static final String PC = "pc";
	
	public List<String> fieldList;
	public List<String> structList;
	public Map<Integer, Field> fieldMap;
	public List<Field> createField;
	public Map<String, String> udfMap;

	public static List<Field> structFields = new ArrayList<Field>();
	
	public Table(){
		tableName = new String();
		fieldList = new ArrayList<String>();
		structList = new ArrayList<String>();
		fieldMap = new HashMap<Integer, Field>();
		createField = new ArrayList<Field>();
		udfMap = new HashMap<String, String>();
	}
	
	public static void addStructField(){
		
	}
	
	public void setCondition(String con){
		condition = con;
	}
	
	public void setTableID(int id){
		tableID = id;
	}
	
	public void setTableName(String name){
		tableName = name;
	}
	
	/**
	 * 输入udf名称返回组合好的字符串
	 * @param udf
	 * @param field
	 * @return
	 */
	public String udfField(String udf, Field field){
		String tmp = new String();
		tmp = ", " + udf + "(" + field.field_name + ")" + " AS t" + Integer.toString(tableID) + "_" + field.field_name;
		String ttString = new String();
		ttString = "t" + Integer.toString(tableID) + "_" + field.field_name;
		Field tmpField = new Field();
		tmpField.field_name = ttString;
		tmpField.struct = field.struct;
			createField.add(tmpField);
		return tmp;
	}

	public String combine(Field field){
		String tmp = new String();
		tmp = ", " + field.field_name +  " AS t" + Integer.toString(tableID) + "_" + field.field_name;
		String ttString = new String();
		ttString = "t" + Integer.toString(tableID) + "_" + field.field_name;	
		Field tmpField = new Field();
		tmpField.id = field.id;
		tmpField.field_name = ttString;
		tmpField.struct = field.struct;
			createField.add(tmpField);
		return tmp;
	}
	
	public String getSelect(){
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT uid ");
		if(plat){
			for(int i = 0; i < fieldMap.size() - 1; i++){
				String tmpString = new String();
				tmpString = fieldMap.get(i).field_name;
				if(udfMap.containsKey(tmpString)){
					builder.append(udfField(udfMap.get(tmpString),  fieldMap.get(i)));
				}else {
					builder.append(combine( fieldMap.get(i)));
				}
			}
		}
		
		builder.append(" FROM ");
		builder.append(tableName);
		for(Field field: createField){
			structFields.add(field);
		}
		return builder.toString();
	}
	
	public static void main(String[] args) {
		Table table = new Table();
		//System.out.println(table.udfField("udfChange", "name"));
	}
}



class Field{
	int table_id;
	int id;
	String field_name = new String();
	String struct = new String();
	public Field(){
	}
	
	public Field(ResultSet set){
		try {
			table_id = set.getInt("table_id");
			id = set.getInt("id");
			field_name = set.getString("field_name");
			struct = set.getString("struct");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String toString(){
		return "{" + id + ":" + field_name + ":" + struct + "}\t";
	}
}