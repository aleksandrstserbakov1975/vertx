package core;

import model.Mail;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.platform.Verticle;

import service.MailService;

public class Server extends Verticle {
	
	private Logger logger;
	private MailService mailService = createMailService();
	
	private final static String CHANNEL_IN = "epms.email.in";
	private final static String CHANNEL_OUT = "epms.email.out";
	
    public void start() {
    	
    	logger = container.logger();
    	HttpServer server = vertx.createHttpServer();
        
        server.requestHandler(new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
            	String file = req.path().equals("/") ? "index.html" : req.path();
				req.response().sendFile("src/main/resources/webroot/" + file);            	
        	}
        });
    	
 		JsonArray permitted = new JsonArray();
 		permitted.add(new JsonObject());

 		SockJSServer sockJSServer = vertx.createSockJSServer(server);
 		sockJSServer = sockJSServer.bridge(new JsonObject().putString("prefix", "/eventbus"),
     				permitted, permitted);

		vertx.eventBus().registerHandler(CHANNEL_IN, new Handler<Message<JsonObject>>() {
 			@Override
 			public void handle(Message<JsonObject> message) {
 				logger.info("received: " + message.body().toString());
 				Mail mail = new Mail(message.body());
 				model.Message mailMessage = mailService.send(mail);
 				vertx.eventBus().send(CHANNEL_OUT, new JsonObject(mailMessage.asMap()));
 			}
 		});
 		 		
 		server.listen(8080);
    }
    
    public MailService createMailService(){
    	return new MailService();
    }
}