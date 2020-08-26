package no.bysykkel.stasjon.webserver;

import com.google.gson.*;
import no.bysykkel.stasjon.model.Station;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import static spark.Spark.*;

import spark.Filter;
import spark.Spark;

public class WebServer {
  private static final Logger logger = Logger.getLogger(WebServer.class.getName());

  /**
   * Spin up the webserver and listen for the bysykkel.webappDir. API exposed via the
   * /api/* URL.
   *
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    configureLogger();
    readProperties();

    staticFiles.location("/public"); // Static files
    staticFiles.externalLocation(System.getProperty("bysykkel.webappDir"));

    get("/api/stations", BysykkelHandler::getBysykkelStasjoner);
  }

  /**
   * This method will read the config.properties file located alongside the JAR executable.
   * Any properties will be loaded into System.properties.
   * @throws IOException
   */
  private static void readProperties() throws IOException {

    Properties properties = new Properties();
    File configFile = new File("config.properties");
    if (!configFile.exists()) {
      logger.info("config.properties not found at : " + configFile.getAbsolutePath() + " trying one level up.");
      configFile = new File("../config.properties");
    }
    if (!configFile.exists()) {
      logger.info("config.properties not found at : " + configFile.getAbsolutePath() + " trying one level up.");
      configFile = new File("../../config.properties");
    }
    if (configFile.exists()) {
      FileInputStream configStream = new FileInputStream(configFile);
      properties.load(configStream);
      configStream.close();
      logger.info("Server properties loaded from " + configFile.getAbsolutePath());
      for (Enumeration<Object> e = properties.keys(); e.hasMoreElements(); ) {
        Object property = (String) e.nextElement();
        logger.info("\t\t* " + property + "=" + properties.get(property));
      }
    } else {
      String message = "Could not find " + configFile.getAbsolutePath() + ". Unable to start.";
      System.err.println(message);
      throw new RuntimeException(message);
    }

    setProperties(properties);

    //port = Integer.parseInt(System.getProperty("no.haagensoftware.contentice.port", "8080"));
  }

  private static void setProperties(Properties properties) {
    Enumeration<Object> propEnum = properties.keys();
    while (propEnum.hasMoreElements()) {
      String property = (String) propEnum.nextElement();
      System.setProperty(property, properties.getProperty(property));
    }
  }

  /**
   * Configure simple logging...
   *
   */
  private static void configureLogger() {
    Logger root = Logger.getRootLogger();
    if (!root.getAllAppenders().hasMoreElements()) {
      //Log4J is not configured. Set it up correctly!
      root.setLevel(Level.INFO);
      root.addAppender(new ConsoleAppender(new PatternLayout("%d %-5p [%t] %C{1}: %m%n")));
    }
  }
}
