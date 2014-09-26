package yy.juefan.mysql;

import java.util.HashSet;
import java.util.Set;

public class Test {
	

	Set<String> uidSet = new HashSet<String>();
	Set<String> imeiSet = new HashSet<String>();
	int count = 0;

	public void getDau(int i, String uid, String imei) {
		if (i == 1) {
			if (uidSet.add(uid) && imeiSet.add(imei)) //俩个集合都插入成功
				count++;
		} else {
			if (imeiSet.add(imei)) //ImeiSet插入成功
				count++;
		}
	}
	
	public static void main(String[] args) {

	}

}
