package template;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.hamcrest.Matcher;

public class Calculator {


	public String calcSum(String path) throws IOException {
		LineCallback<String> sumCallback = new LineCallback<String>() {
			
			@Override
			public String doSomethingWithLine(String line, String value) {
				return value + line;
			}
		};
		
		return lineReadTemplate(path, sumCallback, "");
	}
	
	/*
	public int calcMultiply(String numFilepath) throws IOException {
		LineCallback multiplyCallback = new LineCallback() {
			
			@Override
			public Integer doSomethingWithLine(String line, Integer value) {
				return Integer.valueOf(line) * value;
			}
		};
		return lineReadTemplate(numFilepath, multiplyCallback, 1);
	}
	*/
	
	public <T> T lineReadTemplate(String filePath, LineCallback<T> callback, T intVal) throws IOException{
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(filePath));
			T res = intVal;
			String line = null;
			while((line = br.readLine()) != null){
				res = callback.doSomethingWithLine(line, res);
			}
			return res;
		}catch(IOException e){
			System.out.println(e.getMessage());
			throw e;
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e2) {
					System.out.println(e2.getMessage());
				}
			}
		}

	}
	
	public Integer fileReadTemplate(String filepath, BufferedReaderCALLBACK callback) throws  IOException{
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(filepath));
			int sum = callback.doSomethingWithReader(br);
			return sum;
		}catch(IOException e){
			System.out.println(e.getMessage());
			throw e;
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e2) {
					System.out.println(e2.getMessage());
				}
			}
		}
	}

}
