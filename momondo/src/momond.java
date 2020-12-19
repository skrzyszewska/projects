
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.List;
import java.util.Set;

public class momond {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver driver;

        // PARAMETRY WYSZUKIWANIA
        String wylot_lotnisko = "Warszwa(WAW)";
        String przylot_lotnisko ="Barcelona";
        String dzien_przylotu = "7";
        String dzien_wylotu= "20";
        String miesiac_przylotu = "maj";
        String miesiac_wylotu = "sierpień";

        String []miesiace={"styczeń","luty","marzec","kwiecień","maj","czerwiec","lipiec","sierpień","wrzesień","październik","listopad","grudzień"};

        driver = new ChromeDriver();
        driver.get("https://www.momondo.pl/");
        Thread.sleep(3000);

        /*
        Set<Cookie> cookies = driver.manage().getCookies();
        for (Cookie cook : cookies){
            String name = cook.getName();
            String value = cook.getValue();
            driver.manage().addCookie(new Cookie(name,value));
        }
        Thread.sleep(3000);
        */

        WebElement element = driver.findElement(By.xpath(".//*[@class=\"c-input-element au-target\"]"));
        //element.click();
        //Thread.sleep(100);
        //element = driver.findElement(By.className("c-input-field"));
        element.sendKeys(wylot_lotnisko);
        Thread.sleep(3000);
        WebElement elementdrugi = driver.findElement(By.xpath("html/body/div[1]/layout/router-view/div/router-view/page-header/div/div/flights-searchform/div[1]/div/div/div[1]/div[2]/flights-autocompleter/text-input/div[1]/div[3]/div[2]/input"));
        elementdrugi.click();
        elementdrugi.sendKeys(przylot_lotnisko);
        Thread.sleep(3000);

        WebElement elementdaty = driver.findElement(By.className("c-date_selector-button-label"));
        elementdaty.click();
        Thread.sleep(1000);


        for(int i=0;i<24;i++){
            WebElement elementmiesiaca = driver.findElement(By.xpath("/html/body/div[1]/layout/router-view/div/router-view/page-header/div/div/flights-searchform/div[1]/div/div/div[2]/div[1]/button-group/date-selector[1]/dropdown/div/panel/pane/section/calendar/div[1]/div"));
            String msc=elementmiesiaca.getText();
            if(msc.contains(miesiac_przylotu)){
                break;
            }else
            {
                WebElement dalejmsc = driver.findElement(By.xpath("/html/body/div[1]/layout/router-view/div/router-view/page-header/div/div/flights-searchform/div[1]/div/div/div[2]/div[1]/button-group/date-selector[1]/dropdown/div/panel/pane/section/calendar/div[1]/push-button[2]/div[2]/icon"));
                dalejmsc.click();
            }
        }
        Thread.sleep(1500);

      List<WebElement> op = driver.findElements(By.xpath("html/body/div[1]/layout/router-view/div/router-view/page-header/div/div/flights-searchform/div[1]/div/div/div[2]/div[1]/button-group/date-selector[1]/dropdown/div/panel/pane/section/calendar/div[2]/table/tbody/tr/td"/*/calendar-buttondiv[2]/div[2]"*/));

        for(WebElement we : op){
           // System.out.println(we.getText());
                if(we.getText().equals(dzien_przylotu)){
                    we.click();
                    break;
                }
        }

        Thread.sleep(1500);

        for(int i=0;i<24;i++){
            WebElement elementmiesiaca1 = driver.findElement(By.xpath("/html/body/div[1]/layout/router-view/div/router-view/page-header/div/div/flights-searchform/div[1]/div/div/div[2]/div[1]/button-group/date-selector[2]/dropdown/div/panel/pane/section/calendar/div[1]/div"));
            String msc1=elementmiesiaca1.getText();
            if(msc1.contains(miesiac_wylotu)){
                break;
            }else
            {
                WebElement dalejmsc1 = driver.findElement(By.xpath("/html/body/div[1]/layout/router-view/div/router-view/page-header/div/div/flights-searchform/div[1]/div/div/div[2]/div[1]/button-group/date-selector[2]/dropdown/div/panel/pane/section/calendar/div[1]/push-button[2]/div[2]/icon"));
                dalejmsc1.click();
            }
        }
        Thread.sleep(1500);

        List<WebElement> opl = driver.findElements(By.xpath("html/body/div[1]/layout/router-view/div/router-view/page-header/div/div/flights-searchform/div[1]/div/div/div[2]/div[1]/button-group/date-selector[2]/dropdown/div/panel/pane/section/calendar/div[2]/table/tbody/tr/td/calendar-button/div[2]/div[2]"));

        for(WebElement e : opl){
            //System.out.println(" "+ e.getText());
            if(e.getText().equals(dzien_wylotu)){
                e.click();
                break;
            }
        }


        WebElement wyszukaj = driver.findElement(By.xpath("html/body/div[1]/layout/router-view/div/router-view/page-header/div/div/flights-searchform/div[2]/push-button[1]/div[2]"));
        wyszukaj.click();

        Thread.sleep(30000);

        WebElement kwota = driver.findElement(By.xpath(".//*[@id='flight-tickets-sortbar-cheapest']/div/span[2]/span[1]"));
        System.out.println(kwota.getText());
        String cenatxt = kwota.getText();
        int cena = Integer.parseInt(cenatxt);

        String sciezka1 = testscreen.CaptureScreenshots(driver, "", "test");
        System.out.println(sciezka1);


            sendmail.sendmail("Cena " + cenatxt + " PLN", "skrzyszewska.a.k@gmail.com", sciezka1);


        driver.close();
    }
}
