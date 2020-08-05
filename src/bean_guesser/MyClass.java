package bean_guesser;

import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class MyClass {

	public static int findClosest(Vector<Integer> entries, double target){
		
		double closest = Math.abs(entries.get(0) - target);
		int index = 0;
		for(int i=1; i<entries.size(); i++) {
			if(Math.abs(entries.get(i) - target) < closest) {
				closest = Math.abs(entries.get(i) - target);
				index = i;
			}
		}
		
		return entries.get(index);
	}
	
	public static void main(String[] args) {
		//variables
		System.setProperty("webdriver.chrome.driver","chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		String baseUrl = "https://www.facebook.com/uowunibar/photos/a.189984857710922/4289452027764164";
		
		//launch driver
		driver.get(baseUrl);
		
		//get the comments open manually because im lazy
		System.out.println("Press Enter to continue");
		try{
			System.in.read();
		}
		catch(Exception e){}
		
		//get comments
		System.out.println("getting comments");
		WebElement commentSection = driver.findElement(By.className("_7a9a"));
		List<WebElement> commentItems = commentSection.findElements(By.xpath(
				".//*/div[1]/div/div[2]/div/div[1]/div[1]/div/div/div/div/div/span/span/span"));
		
		//pattern for extraction
		Pattern p = Pattern.compile("\\d+");
		
		Vector<Integer> entries = new Vector();
		
		for(int i=0; i<commentItems.size(); i++) {
			//clean string
			String comment = commentItems.get(i).getText().replaceAll("\\s+","");
			comment = comment.replaceAll(",","");
			Matcher m = p.matcher(comment);
			while(m.find()) {
	            System.out.println(Integer.parseInt(m.group()));
	            
	            //if entry out of bounds then remove
	            if(Integer.parseInt(m.group()) > 10000) {
	            	entries.add(Integer.parseInt(m.group()));
	            }

	        }
		}
		
		System.out.println(entries.size()+" entries collected");
		
		//calculate average
		double average = 0;
		
		for(int i = 0; i<entries.size(); i++) {
			average += entries.get(i);
		}
		
		average = average/entries.size();

		int closest = findClosest(entries, average);
		
		System.out.println("Min is: "+Collections.min(entries));
		System.out.println("Max is: "+Collections.max(entries));
		System.out.println("Average is: "+average);
		System.out.println("Closest is: "+closest);
		
		if(closest < average) {
			System.out.println("Best guess is: "+(closest+1));
			System.out.println("Second best guess is: "+(closest-1));
		} else {
			System.out.println("Best guess is: "+(closest-1));
			System.out.println("Second best guess is: "+(closest+1));
		}
		
		//close driver
		driver.close();
	}
	
}
