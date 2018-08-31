

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class PredictionValidation {
	public int time;
	public Map<String,Double> details = new TreeMap<String,Double>();
	
	


	public static List<PredictionValidation> FileReadCalc(String file_name) throws NumberFormatException, IOException
	{
		int timeCheck = 0;
		List<PredictionValidation> _tempList = new ArrayList<PredictionValidation>();
		PredictionValidation temp = new PredictionValidation();
		File file = new File(file_name);

		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		while((st = br.readLine()) != null)
		{
			String[] spliter = st.split(Pattern.quote("|"));
			int index = Integer.parseInt(spliter[0]);			
			temp.time = timeCheck;
			if(timeCheck == index)
			{
				temp.details.put(spliter[1],Double.parseDouble(spliter[2]));	
			}
			else
			{
				if(!temp.details.isEmpty())
					_tempList.add(temp);
				temp = new PredictionValidation();
				timeCheck++;
			}
			
		}
		return _tempList;
		
	}
	
	public static void main(String[] argv) throws IOException
	{
	

		
		DecimalFormat df2 = new DecimalFormat(".##");
		List<PredictionValidation> ListActual = PredictionValidation.FileReadCalc(argv[0]);
		List<PredictionValidation> ListPredict = PredictionValidation.FileReadCalc(argv[1]);
		Object[] actual = ListActual.toArray();
		Object[] predict = ListPredict.toArray();
		
		File file = new File(argv[2]);
		BufferedReader br = new BufferedReader(new FileReader(file));
		int window = Integer.parseInt(br.readLine());
		int min = 0;
		int max = ListActual.size();
		double error = 0.0;
		int count = 0;
	
		
		for(int i = min ;i < (i + window)  ; i++)
		{
			
			PredictionValidation pvActual = (PredictionValidation) actual[i];
			PredictionValidation pvPredict = (PredictionValidation) predict[i];
			Iterator iter = pvActual.details.entrySet().iterator();
			while(iter.hasNext())
			{
				Map.Entry pair = (Map.Entry)iter.next();
				if(pvPredict.details.containsKey(pair.getKey()));
				{
					
				Double actualValue = pvActual.details.get(pair.getKey());
				Double predictValue = pvPredict.details.get(pair.getKey());
				if(predictValue != null)
					{
					  count++;
					  double subtract = Double.valueOf(df2.format(actualValue - predictValue));
					  error += subtract;
					
					};
				}
			}
			System.out.println(i+"|"+(i+window)+"|"+Double.valueOf(df2.format(error/count)));
			if((i+window) == max)
				break;
			if(i == i+window-1)
			{
				File output = new File("../output/comparison.txt");
				if(!output.exists()){
			    	   file.createNewFile();
	    		FileWriter fw = new FileWriter(file,true);
	        	//BufferedWriter writer give better performance
	        	BufferedWriter bw = new BufferedWriter(fw);
				error = 0.0;
				count = 0;
	        	bw.write(i+"|"+(min+window)+"|"+Double.valueOf(df2.format(error/count)));
	        	//Closing BufferedWriter Stream
	        	bw.close();
				 min++;
				 i = min;
			}			
		}
		
		
			
	}
	}
}
	

