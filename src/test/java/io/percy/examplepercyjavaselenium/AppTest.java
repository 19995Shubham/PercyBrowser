package io.percy.examplepercyjavaselenium;



import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.percy.selenium.Percy;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * Unit test for example App.
 */
public class AppTest {


    @Test(priority = 0)
    public void loadsHomePage() {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1440, 900));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("http://127.0.0.1:8080");

        System.out.println("ABC");
        System.out.println(driver.getTitle());

        WebElement element = driver.findElement(By.className("todoapp"));
        assertNotNull(element);

        Percy percy = new Percy(driver);
        // Take a Percy snapshot.
        percy.snapshot("Home Page");
    }

    @Test(priority = 1)
    public void acceptsANewTodo() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("http://127.0.0.1:8080");
        // We start with zero todos.
        List<WebElement> todoEls = driver.findElements(By.cssSelector(".todo-list li"));
        assertEquals(0, todoEls.size());

        // Add a todo in the browser.
        WebElement newTodoEl = driver.findElement(By.className("new-todo"));
        newTodoEl.sendKeys("A new fancy todo!");
        newTodoEl.sendKeys(Keys.RETURN);

        // Now we should have 1 todo.
        todoEls = driver.findElements(By.cssSelector(".todo-list li"));
        assertEquals(1, todoEls.size());

        // Take a Percy snapshot specifying browser widths.
        Percy percy = new Percy(driver);
        percy.snapshot("One todo", Arrays.asList(768, 992, 1200));
    }

    @Test
    public void letsYouCheckOffATodo() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("http://127.0.0.1:8080");

        WebElement newTodoEl = driver.findElement(By.className("new-todo"));
        newTodoEl.sendKeys("A new todo to check off");
        newTodoEl.sendKeys(Keys.RETURN);

        WebElement todoCountEl = driver.findElement(By.className("todo-count"));
        assertEquals("1 item left", todoCountEl.getText());

        driver.findElement(By.cssSelector("input.toggle")).click();

        todoCountEl = driver.findElement(By.className("todo-count"));
        assertEquals("0 items left", todoCountEl.getText());

        // Take a Percy snapshot specifying a minimum height.
        Percy percy = new Percy(driver);
        percy.snapshot("Checked off todo", null, 2000);
    }
}
