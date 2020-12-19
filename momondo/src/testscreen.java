import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

    public class testscreen {
        public static String CaptureScreenshots(WebDriver driver, String nazwaFolderu, String nazwaPliku) {

            String sciezka = "";
            try {
                DateFormat dateFormat = new SimpleDateFormat("hh-mm-ss");
                Date date = new Date();
                TakesScreenshot ts = (TakesScreenshot)driver;
                File src = ts.getScreenshotAs(OutputType.FILE);
                //File file = new File("./Screenshots/"+"/"+nazwaFolderu+"/"+nazwaPliku+".png");
//			if (new File("./Screenshots/"+"/"+nazwaFolderu+"/"+nazwaPliku+".png").exists()){
//				file = new File("./Screenshots/"+"/"+nazwaFolderu+"/"+nazwaPliku+2+".png");
//			}
                FileUtils.copyFile(src, new File(".\\Screenshots\\"+nazwaFolderu+"\\"+dateFormat.format(date)+"-"+nazwaPliku+".png"));
                if (!nazwaFolderu.equals("")) {
                    nazwaFolderu += "\\";
                }
                sciezka = ".\\Screenshots\\"+nazwaFolderu+dateFormat.format(date)+"-"+nazwaPliku+".png";


            } 	catch (Exception e) {
                System.out.println("Exception while taking screenshot "+e.getMessage());
            }
            return sciezka;
        }
    }
