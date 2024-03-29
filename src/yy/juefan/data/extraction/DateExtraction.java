package yy.juefan.data.extraction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yy.juefan.basic.FileIO;

public class DateExtraction {
	
	public  StringBuilder log = new StringBuilder();
	public  StringBuilder hql = new StringBuilder();
	public  ArrayList<String> hqlList = new ArrayList<String>();
	public  ArrayList<Integer> eventid = new ArrayList<Integer>();
	public  ArrayList<Integer> key = new ArrayList<Integer>();	
	public static Date dt=new Date();
	SimpleDateFormat matter=new SimpleDateFormat("yyyyMMdd");
	static{
		dt.setTime(dt.getTime() - 24*60*60*1000);
	}
	
	public  void build(Table table){
		if(!table.state){
			log.append(table.toString() + "\n");
		}else {
			if(!table.user_label.equals("uid")){
				log.append(table.id + "\t" + "用户标识为:" + table.user_label + "\n");
			}else {
				String string = new String();
				if(table.from.equals("docs")){
					string = "SELECT uid FROM " + table.db + "." + table.table + " WHERE dt = "
							+ matter.format(dt) ;
					if(table.plat == 2){
						string = string + " AND isWebYYLogin(uid) = 1 GROUP BY uid ";
					}else if (table.plat == 3 ) {
						string = string + " AND isSJYYLogin(uid) = 1 GROUP BY uid ";
					}else {
						string = string + " GROUP BY uid ";
					}
					hqlList.add(string);
				}else {
					if(table.field.equals("eventid"))
					eventid.add(table.value);
					else {
						key.add(table.value);
					}
				}
			}
		}
	}
	
	public void combine(){
		hql.append("CREATE TEMPORARY FUNCTION isWebYYLogin AS 'com.hiido.hive.udf.GenericUDFJudgeWebYYUid';\n");
		hql.append("CREATE TEMPORARY FUNCTION isSJYYLogin AS 'com.hiido.hive.udf.GenericUDFJudgeSJYYUid';\n");
		hql.append("SELECT uid FROM(");
		for(int i = 0; i < hqlList.size() - 1; i++){
			hql.append(hqlList.get(i)).append(" UNION ALL ");
		}
		hql.append(hqlList.get(hqlList.size() - 1));
		if(eventid.size() > 0){
			hql.append(" UNION ALL SELECT uid FROM default.yy_webevent_original WHERE dt = ")
			.append(matter.format(dt)).append(" AND eventid IN (");
			for(int j = 0; j < eventid.size() - 1 ; j++)
				hql.append(eventid.get(j)).append(",");
			hql.append(eventid.get(eventid.size() - 1)).append(")  AND isWebYYLogin(uid) = 1  GROUP BY uid ");
		}
		if(key.size() > 0){
			hql.append(" UNION ALL SELECT uid FROM default.yy_tlv_day_main1_ver WHERE dt = ")
			.append(matter.format(dt)).append(" AND key IN (");
			for(int j = 0; j < key.size() - 1 ; j++)
				hql.append(key.get(j)).append(",");
			hql.append(key.get(key.size() - 1)).append(") GROUP BY uid ");
		}
		hql.append(")a ");
		hql.append("GROUP BY uid;");
	}
	

	
	public static void main(String[] args) {
		List<String> idList = new ArrayList<String>();
		FileIO fileIO = new FileIO();
		fileIO.SetfileName("./file/extraction.txt");
		fileIO.FileRead();
		idList = fileIO.cloneList();
		DataInput dataInput = new DataInput(idList);
		DateExtraction extraction = new DateExtraction();
		for(Table table: dataInput.tables)
			extraction.build(table);
		extraction.combine();
		System.out.println(extraction.hql.toString());
	}
}
