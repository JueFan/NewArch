package yy.juefan.basic;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileIO {
	private String fileName;
	private List<String> fileList;
	public static List<String> fileNameList = new ArrayList<String>(); 
	public FileIO(){
		fileName = new String();
		fileList = new ArrayList<String>();
	}

	/**����Ҫ��ȡ���ļ����ļ�·��*/
	public void SetfileName(String fileString){
		this.fileName = fileString;
	}

	/**��fileList���鸴�Ƴ���
	 * ����ԭ�е�fileList���*/
	public List<String> cloneList(){
		List<String> tmpList = new ArrayList<String>();
		tmpList.addAll(fileList);
		fileList.clear();
		return tmpList;
	}

	/**
	 * ɨ��ʵ�������fileName�ļ�
	 * ��fileName�ļ��ڵ����ݰ��д洢��fileList������
	 */
	public void FileRead(){
		System.out.println("�����ļ���ȡ����......");
		try{
			Scanner fileScanner = new Scanner(new File(fileName));
			if(new File( fileName).exists()){
				System.out.println("���ڶ�ȡ�ļ�......");
				while(fileScanner.hasNextLine()){
					fileList.add(fileScanner.nextLine());
				}
				System.out.println("��ȡ�ļ��ɹ�!");
			}
			System.out.println("�ļ��� " + fileList.size() + " ��");
		}catch (Exception e) {
			System.out.println("�ļ�������!");
		}
	}
	/** 
	 * @param path �ļ�·�� 
	 * @param suffix ��׺�� 
	 * @param isdepth �Ƿ������Ŀ¼ 
	 * @return ����Ҫ����ļ�·�����б�
	 */ 
	public static List<String> getListFiles(String path, String suffix, boolean isdepth) { 
		File file = new File(path); 
		return FileIO.listFile(file ,suffix, isdepth); 
	} 
	
	public static List<String> getListFiles(String path, String suffix) { 
		File file = new File(path); 
		return FileIO.listFile(file ,suffix); 
	} 
	
	public static List<String> listFile(File f, String suffix) { 
		return FileIO.listFile( f,  suffix, true);
	}
	
	/** 
	 * ��ȡĿ¼����Ŀ¼��ָ���ļ�����·�� ���ŵ�һ���������淵�ر��� 
	 * @author juefan_c 
	 */ 
	public static List<String> listFile(File f, String suffix, boolean isdepth) { 
		//��Ŀ¼��ͬʱ��Ҫ������Ŀ¼ 
		if (f.isDirectory() && isdepth == true) { 
			//listFiles()�Ƿ���Ŀ¼�µ��ļ�·������
			File[] t = f.listFiles(); 
			for (int i = 0; i < t.length; i++) {
				listFile(t[i], suffix, isdepth); 
			} 
		} 
		else { 
			String filePath = f.getAbsolutePath(); 
			if(suffix =="" || suffix == null) { 
				fileNameList.add(filePath); 
			} 
			else { 
				//���һ��.(����׺��ǰ���.)������
				int begIndex = filePath.lastIndexOf("."); 
				//System.out.println("����Ϊ :"+begIndex);
				String tempsuffix = ""; 
				//��ֹ���ļ���ȴû�к�׺���������ļ�
				if(begIndex != -1){  
					//tempsuffixȡ�ļ��ĺ�׺
					tempsuffix = filePath.substring(begIndex + 1, filePath.length()); 
				} 
				if(tempsuffix.equals(suffix)) { 
					fileNameList.add(filePath); 
				} 
			} 
		} 
		return fileNameList; 
	} 
	/**
	 * ��ָ������д��ָ���ļ���
	 * @param fileWriter  �ļ�·��
	 * @param context  �洢����
	 * @param bool �Ƿ�׷��д��
	 */
	public static void FileWrite(String fileName, String context, boolean bool){
		try{
			@SuppressWarnings("resource")
			FileWriter fileWriter = new FileWriter(fileName, bool);
			fileWriter.write(context);
			fileWriter.flush();
		}catch (Exception e) {
		}
	}
	
	/**
	 * ��ָ������д��ָ���ļ���
	 * ��׷�ӵķ�ʽд��
	 * @param fileWriter  �ļ�·��
	 * @param context  �洢����
	 */
	public static void FileWrite(String fileName, String context){
		try{
			@SuppressWarnings("resource")
			FileWriter fileWriter = new FileWriter(fileName + ".txt", true);
			fileWriter.write(context);
			fileWriter.flush();
		}catch (Exception e) {
		}
	}
	
	public static void main(String[] args) {
		FileIO readFile = new FileIO();
		readFile.SetfileName(args[0]);
		List<String> list = new ArrayList<String>();
		list = FileIO.getListFiles(args[0], args[1]);
		for(String s:list)
			System.out.println(s);
	}

}
