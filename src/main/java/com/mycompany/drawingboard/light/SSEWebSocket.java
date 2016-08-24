package com.mycompany.drawingboard.light;

import javax.websocket.Session;
import javax.websocket.OnClose;
import javax.websocket.server.ServerEndpoint;

import javax.websocket.OnOpen;

/**
 * Class handling web socket connections at "/websockets" path.
 */
@ServerEndpoint(
        value = "/websockets")
public class SSEWebSocket {

    /**
     * Method called by the web socket runtime when a new web socket opens.
     *
     * @param session Session associated with the new web socket connection.
     * @throws Exception 
     */
    @OnOpen
    public void onOpen(Session session) throws Exception {
        DataProvider.addWebSocket(session);
    }

    /**
     * Method called by the web socket runtime when the web socket connection
     * closes.
     *
     * @param session Session associated with the web socket connection being
     * closed.
     */
    @OnClose
    public void onClose(Session session) {
        DataProvider.removeWebSocket(session);
    }

    
}
