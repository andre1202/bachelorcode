package tests;

import at.fhv.ale8340.ba2.owndb.model.DataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TODELETE {
    private final Logger logger = (Logger) LoggerFactory.getLogger(TODELETE.class);


	public static void tf(String[] args){

//		try {
//			File file = new File("./Database/2022/05/02.csv");
//			file.getParentFile().mkdirs();
//			file.createNewFile();
//			System.out.println(file.exists());
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//
//		List<String> list = new ArrayList<>();
//		list.add("TEST t"); list.add("test B"); list.add("TEST c");
//
//		System.out.println(list);
//
//		String s = "\"2021-02-12 00:00:08.0\tAna1 - 1: T.Au?en\t5.8\"";
//		String[] sa = s.split("\"");
//		System.out.println("start ... start");
//		System.out.println(sa[1]);
//		System.out.println("\"2021-02-12 00:00:08.0\tAna1 - 1: T.Au?en\t5.8\"");
//
//
//		List<String> list = new ArrayList<>();
//		List<String> toSave = null;
//
//		list.add("ASD"); list.add("BSD"); list.add("CSD");
//
//		toSave = list;
//		list = new ArrayList<>();
//
//		System.out.println("LIST: " + list);
//		System.out.println("toSave: " + toSave);
//		System.out.println("");
//		System.out.println("");
//
//
//		System.out.println(checkValue(new DataPoint(null, "", 19.9f), 15.0, 25.0));
//
// 		System.out.println(15.0f < 15.0);
// 		System.out.println();
// 		System.out.println();
// 		System.out.println();

//		System.out.println(LocalDateTime.now().toString());



	}

	private static boolean checkValue(DataPoint dataPoint, Double max_value, Double min_value) {
		if(min_value != null){
			if(dataPoint.getValue() > min_value){
				return false;
			}
		}
		if(max_value != null){
			if(dataPoint.getValue() < max_value){
				return false;
			}
		}
		return true;
	}
}
