package yy.juefan.data.extraction;

import java.util.ArrayList;
import java.util.List;

public class DataInput {
	
	
	public ArrayList<Integer> idList = new ArrayList<Integer>();
	public ArrayList<Table> tables = new ArrayList<Table>();
	public DataInput(List<String> idList2){
		for(String s: idList2)
			this.idList.add(Integer.parseInt(s));
		for(int id: this.idList)
			tables.add(new Table(id));
	}

}