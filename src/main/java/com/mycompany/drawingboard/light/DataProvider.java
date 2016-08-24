package com.mycompany.drawingboard.light;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseBroadcaster;

/**
 * Simple in-memory data storage for the application.
 */
class DataProvider {
    /** ID of the last created drawing. */
    private static int lastId = 0;
    
    /** Map that stores drawings by ID. */
    private static final HashMap<Integer, Drawing> drawings
            = new HashMap<>();
    

    /**
     * Map that stores web socket sessions corresponding to a given drawing ID.
     */
    private static final List<Session> webSockets = new ArrayList<>();
   
    static synchronized void removeWebSocket(Session session) {
        webSockets.remove(session);
    }
    
    static synchronized void addWebSocket(Session session) {
        webSockets.add(session);
    }
    
    private static void wsBroadcast() {
            for (Session session : webSockets) {
                try {
                    session.getBasicRemote().sendText("refresh");
                } catch (IOException ex) {
                    Logger.getLogger(DataProvider.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }

    /**
     * Retrieves a drawing by ID.
     * @param drawingId ID of the drawing to be retrieved.
     * @return Drawing with the corresponding ID.
     */
    static synchronized Drawing getDrawing(int drawingId) {
        return drawings.get(drawingId);
    }

    /**
     * Retrieves all existing drawings.
     * @return List of all drawings.
     */
    static synchronized List<Drawing> getAllDrawings() {
        return new ArrayList(drawings.values());
    }
    
    /**
     * Creates a new drawing based on the supplied drawing object.
     * @param drawing Drawing object containing property values for the new drawing.
     * @return ID of the newly created drawing.
     */
    static synchronized int createDrawing(Drawing drawing) {
        Drawing result = new Drawing();
        result.id = ++lastId;
        result.name = drawing.name;
        drawings.put(result.id, result);
        wsBroadcast();
        return result.id;
    }

    /**
     * Delete a drawing with a given ID.
     * @param drawingId ID of the drawing to be deleted.
     * @return {@code true} if the drawing was deleted, {@code false} if there
     *         was no such drawing.
     */
    static synchronized boolean deleteDrawing(int drawingId) {
        wsBroadcast();
        return drawings.remove(drawingId) != null;
    }

}
