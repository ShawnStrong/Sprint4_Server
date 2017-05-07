import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;




public class HTTPServer{

	public static String sharedResponse = "";
	static Gson g;
	public static LinkedList<ResultRacer> results = new LinkedList<ResultRacer>();
	
	public static void main(String[] args) throws Exception {
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
		server.createContext("/add", new addHandler());
		server.createContext("/results", new resultsHandler());
		
		server.setExecutor(null); // creates a default executor

		// get it going
		System.out.println("Starting Server...");
		server.start();
	}
	
	static class addHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {

			sharedResponse = "";

			// set up a stream to read the body of the request
			java.io.InputStream inputStr = t.getRequestBody();

			// set up a stream to write out the body of the response
			OutputStream outputStream = t.getResponseBody();

			// string to hold the result of reading in the request
			StringBuilder sb = new StringBuilder();

			// read the characters from the request byte by byte and build up
			// the sharedResponse
			int nextChar = inputStr.read();
			while (nextChar > -1) {
				sb = sb.append((char) nextChar);
				nextChar = inputStr.read();
			}

			sb.insert(0, "[");

			sharedResponse = sharedResponse + sb.toString() + "]";

			Gson g = new Gson();
			ArrayList<ResultRacer> incoming = (g.fromJson(sharedResponse, new TypeToken<Collection<ResultRacer>>() {
			}.getType()));
			for (ResultRacer emp : incoming) {
				results.add(emp);
			}

			//System.out.println("Json string: " + sharedResponse);

			String postResponse = "Json received";
			t.sendResponseHeaders(300, postResponse.length());
			// write it and return it
			outputStream.write(postResponse.getBytes());

			outputStream.close();
		}
	}
	
	static class resultsHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {

			sharedResponse = "";

			// set up a stream to read the body of the request
			java.io.InputStream inputStr = t.getRequestBody();

			// set up a stream to write out the body of the response
			OutputStream outputStream = t.getResponseBody();

			// string to hold the result of reading in the request
			StringBuilder sb = new StringBuilder();

			// read the characters from the request byte by byte and build up
			// the sharedResponse
			int nextChar = inputStr.read();
			while (nextChar > -1) {
				sb = sb.append((char) nextChar);
				nextChar = inputStr.read();
			}

			create_print_html(results);

			//System.out.println("Json string: " + sharedResponse);

			String postResponse = "Json received";
			t.sendResponseHeaders(300, postResponse.length());
			// write it and return it
			outputStream.write(postResponse.getBytes());

			outputStream.close();
		}
	}
	
	public static void create_print_html(LinkedList<ResultRacer> out) {
		String cssurl = "styles.css";
		String url = "index.html";
		Gson g = new Gson();

		// ArrayList<Racer> printThis = new ArrayList<>();
		

		String json = g.toJson(out);

		// create text of css file
		String css = "tr:nth-of-type(odd) {background-color:#42f45f; } body {background-color: powderblue;}";

		// create text of html file
		String html = "<html>   <head>   <title>Lab 8</title>   <link rel=\"stylesheet\" href=\"" + cssurl
				+ "\"> <meta charset=\"UTF-8\">       <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js\"></script>       <title>title</title>   </head>   <body onLoad=\"buildHtmlTable('#excelDataTable')\">   <table id=\"excelDataTable\" border=\"1\">  <caption>Racers</caption> </table>  <script> var myList = "
				+ json
				+ ";  function buildHtmlTable(selector) {   var columns = addAllColumnHeaders(myList, selector);    for (var i = 0; i < myList.length; i++) {     var row$ = $('<tr/>');     for (var colIndex = 0; colIndex < columns.length; colIndex++) {       var cellValue = myList[i][columns[colIndex]];       if (cellValue == null) cellValue = \"\";       row$.append($('<td/>').html(cellValue));     }     $(selector).append(row$);   } }  function addAllColumnHeaders(myList, selector) {   var columnSet = [];   var headerTr$ = $('<tr/>');    for (var i = 0; i < myList.length; i++) {     var rowHash = myList[i];     for (var key in rowHash) {       if ($.inArray(key, columnSet) == -1) {         columnSet.push(key);         headerTr$.append($('<th/>').html(key));       }     }   }   $(selector).append(headerTr$);    return columnSet; }       </script> </body>  </html>";

		// create the css file

		File cssfile = new File(cssurl);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(cssfile));
			bw.write(css);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// create the html file

		File f = new File(url);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(html);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// run the html file:

		try {
			Desktop.getDesktop().browse(f.toURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}