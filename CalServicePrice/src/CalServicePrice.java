import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class CalServicePrice {

	private static final String PATH = ".\\dist\\promotion1.log";
	private static final String DEST_PATH = ".\\dest\\list_price.json";
	
	private static ArrayList<Object> getStringBufferReader() throws ParseException {
		// set file in PATH
		ArrayList<Object> list = new ArrayList<Object>();
		File file = new File(PATH);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			// read line by line
			while ((line = br.readLine()) != null) {
				// Split String Column
				String[] arryTmpLine = splitColumn(line);
				String promotion = arryTmpLine[4];
				// check Promotion = 1
				if (promotion.equalsIgnoreCase("p1") && arryTmpLine != null) {
					// Convert String To Minute
					int minute = convertDateToMinute(arryTmpLine[1],arryTmpLine[2]);
					int price = -1;
					if(minute > -1){
						price = calculatePrice(minute);
						Map<String, Object> mapObject = new HashMap<String, Object>();
						mapObject.put("price", price);
						mapObject.put("mobile_no", arryTmpLine[3]);
						if(price > -1)
							list.add(mapObject);
					}
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	private static int calculatePrice(int price) {
		price = price == 0 ? 0 : (price == 1 ? 3 : (price - 1) + 3); 
		return price;
	}

	private static String[] splitColumn(String line) {
		String[] arryTmpLine = line.split("\\|");
		if (arryTmpLine.length == 5) {
			return arryTmpLine;
		}
		return null;
	}

	private static int convertDateToMinute(String startTime, String endTime) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		
		try {
			Date date1 = format.parse(startTime);
			Date date2 = format.parse(endTime);
			
			return Integer
					.valueOf(String.format("%d",
							TimeUnit.MILLISECONDS.toMinutes(Math.abs(date2.getTime() - date1.getTime()))));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return -1;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			ArrayList<Object> listPromo1 = getStringBufferReader();
			
			ObjectMapper objectMapper = new ObjectMapper();

			objectMapper.writeValue(new File(DEST_PATH), listPromo1);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
