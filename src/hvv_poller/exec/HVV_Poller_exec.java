/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller.exec;

import hvv_poller.hv.*;
import hvv_devices.HVV_HvDevice;
import hvv_devices.HVV_HvDevices;
import hvv_poller.vac.*;
import hvv_poller.HVV_Poller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class HVV_Poller_exec implements Runnable {
    HVV_Poller theApp;
    static Logger logger = Logger.getLogger(HVV_Poller_exec.class);
    
    private int m_nState;
    public int GetState() { return m_nState; }
    
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTED_OK = 1;
    public static final int STATE_CONNECTED_PROBLEMS = 2;
    public static final int STATE_CONNECTED_IDLE = 3;
    
    public Thread m_Thread;
    volatile boolean m_bContinue;
    
    public HVV_Poller_exec( HVV_Poller app) {
        theApp = app;
        m_Thread = null;
        m_nState = STATE_DISCONNECTED;
    }
    
    public void start() {
        if( m_Thread != null && m_Thread.isAlive() == true)
            return;
        
        m_Thread = new Thread( this);
        m_Thread.start();
    }
    
    public void stop() {
        logger.debug( "STOP requested!");
        if( m_Thread != null) {
            try {
                m_bContinue = false;
                m_Thread.join();
                
                m_nState = STATE_DISCONNECTED;
                
                m_Thread = null;
            } catch( InterruptedException ex) {
                logger.warn( "InterruptedException caught!", ex);
            }
        }
        logger.debug( "Thread stopped!");
    }

    @Override
    public void run() {
        m_bContinue = true;
        
        ServerSocket serverSocket = null;
        Socket socket = null;
        Random rnd = new Random();
        
        ObjectInputStream is = null;
        ObjectOutputStream os = null;
        
        do {
            if( m_nState == STATE_CONNECTED_OK) {
                if( is != null && os != null) {
                        
                    try {
                        int nRetCode = 0;
                        double dblRespondValue = 0;
                        String strReqId = ( String) is.readObject();
                        logger.trace( "Incoming Request ID:" + strReqId);
                        String strCmd = "";
                        String strObject = "";
                        if( strReqId != null) {
                            strCmd = ( String) is.readObject();
                            
                            theApp.m_pMainWnd.lblLastActionExecutor.setText( theApp.NiceFormatDateTime( theApp.GetLocalDate()));
                            
                            if( strCmd != null) {
                                if( strCmd.equals( "PING")) {
                                    logger.trace( strReqId + ": PING: RetCode will be 0.");
                                    nRetCode = 0;
                                }
                                else if( strCmd.equals( "GET")) {
                                    logger.debug( strReqId + ": GET");
                                        
                                    strObject = ( String) is.readObject();
                                    if( strObject != null) {
                                            
                                        logger.debug( strReqId + ": GET: " + strObject);
                                        
                                        if( !theApp.m_mapHotValues.containsKey( strObject)) {
                                            logger.warn( "Requested param '" + strObject + "' is not in hot-list! RetCode 6.");
                                            nRetCode = 6;
                                        }
                                        else {
                                            dblRespondValue = ( double) theApp.m_mapHotValues.get( strObject);
                                            nRetCode = 0;
                                        }
                                    }
                                    else {
                                        logger.error( "" + strReqId + ": GET: Object is NULL. RetCode 4");
                                        nRetCode = 4;
                                    }
                                }
                                else if( strCmd.equals( "QUIT")) {
                                    logger.info( "'QUIT' processing");
                                    
                                    try { is.close(); } catch( IOException ex2) { logger.error( "Exception при аварийном закрытии входящего потока", ex2);}
                                    is = null;
                                    
                                    try { os.close(); } catch( IOException ex2) { logger.error( "Exception при аварийном закрытии исходящего потока", ex2);}
                                    os = null;
                                
                                    if( socket!= null) {
                                        if( !socket.isClosed())
                                            try { socket.close(); } catch( IOException ex2) { logger.error( "Exception при аварийном закрытии сокета", ex2);}
                                        socket = null;
                                    }
                                    
                                    m_nState = STATE_DISCONNECTED;
                                    continue;
                                }
                                else {
                                    logger.error( "" + strReqId + ": Unknown command '" + strCmd + "'. RetCode 3");
                                    nRetCode = 3;
                                }
                            }
                            else {
                                logger.error( "" + strReqId + ": Command is null. RetCode 2");
                                nRetCode = 2;
                            }
                        }
                        else {
                            logger.error( "Request-ID is 0. RetCode 1");
                            nRetCode = 1;
                        }
                            
                        //RESPOND
                        logger.trace( "Responding with code: " + nRetCode);
                        logger.trace( "Responding with value: " + dblRespondValue);                        
                        logger.debug( "ID:" + strReqId +
                                      ". strCmd:" + strCmd +
                                      ( strCmd.equals( "PING") ? ". PING" : ". GET '" + strObject+ "'") +
                                      ". RetCode:" + nRetCode + ". RetValue:" + dblRespondValue);
                        
                        os.writeObject( strReqId);
                        os.writeInt( nRetCode);
                        os.writeDouble( dblRespondValue);
                        os.flush();
                    }
                    catch( IOException ex) {
                        logger.error( "IOException caught", ex);
                        
                        if( is != null) {
                            try { is.close(); } catch( IOException ex2) { logger.error( "Exception при аварийном закрытии входящего потока", ex2);}
                            is = null;
                        }
                        if( os != null) {
                            try { os.close(); } catch( IOException ex2) { logger.error( "Exception при аварийном закрытии исходящего потока", ex2);}
                            os = null;
                        }
                        if( socket!= null) {
                            if( !socket.isClosed())
                                try { socket.close(); } catch( IOException ex2) { logger.error( "Exception при аварийном закрытии сокета", ex2);}
                            socket = null;
                        }
                        m_nState = STATE_DISCONNECTED;
                    }
                    catch( Exception ex) {
                        logger.error("Exception caught!", ex);
                        
                        if( is != null) {
                            try { is.close(); } catch( IOException ex2) { logger.error( "Exception при аварийном закрытии входящего потока", ex2);}
                            is = null;
                        }
                        if( os != null) {
                            try { os.close(); } catch( IOException ex2) { logger.error( "Exception при аварийном закрытии исходящего потока", ex2);}
                            os = null;
                        }
                        if( socket!= null) {
                            if( !socket.isClosed())
                                try { socket.close(); } catch( IOException ex2) { logger.error( "Exception при аварийном закрытии сокета", ex2);}
                            socket = null;
                        }
                        m_nState = STATE_DISCONNECTED;
                    }
                }
                else {
                    logger.error("One or both of streams is (are) null... Reseting connecton!");
                    if( is != null) {
                            try { is.close(); } catch( IOException ex2) { logger.error( "Exception при аварийном закрытии входящего потока", ex2);}
                            is = null;
                        }
                        if( os != null) {
                            try { os.close(); } catch( IOException ex2) { logger.error( "Exception при аварийном закрытии исходящего потока", ex2);}
                            os = null;
                        }
                        if( socket!= null) {
                            if( !socket.isClosed())
                                try { socket.close(); } catch( IOException ex2) { logger.error( "Exception при аварийном закрытии сокета", ex2);}
                            socket = null;
                        }
                        m_nState = STATE_DISCONNECTED;
                }
            }
            else {
                //Clean up sockets and streams
                m_nState = STATE_DISCONNECTED;
                if( is != null) {
                    try { is.close(); } catch( IOException ex2) { logger.error( "Exception при аварийном закрытии входящего потока", ex2);}
                    is = null;
                }
                if( os != null) {
                    try { os.close(); } catch( IOException ex2) { logger.error( "Exception при аварийном закрытии исходящего потока", ex2);}
                    os = null;
                }
                if( socket!= null) {
                    if( !socket.isClosed())
                        try { socket.close(); } catch( IOException ex2) { logger.error( "Exception при аварийном закрытии сокета", ex2);}
                    socket = null;
                }
                if( serverSocket != null) {
                    if( !serverSocket.isClosed())
                        try { serverSocket.close(); } catch( IOException ex2) { logger.error( "Exception при аварийном закрытии серверного сокета", ex2);}
                    serverSocket = null;
                }
                
                try {
                    serverSocket = new ServerSocket( theApp.GetSettings().GetExecutorPartPort());
                    serverSocket.setSoTimeout( 10000);
        
                    logger.info( "Waiting for a connection on " + theApp.GetSettings().GetExecutorPartPort());
                
                    socket = serverSocket.accept();
                    
                    logger.info( "Connection accepted! Creating streams");
                    os = new ObjectOutputStream( socket.getOutputStream());
                    is = new ObjectInputStream( socket.getInputStream());
            
                    socket.setKeepAlive( true);
                    socket.setSoLinger( true, 5);
                        
                    m_nState = STATE_CONNECTED_OK;
                }
                catch( SocketTimeoutException ex) {
                    logger.warn( "Server connection timeout! Restarting!");
                    m_nState = STATE_DISCONNECTED;
                }
                catch( Exception ex) {
                    logger.warn( "Exception caught while waiting for incoming connecton!", ex);
                    m_nState = STATE_DISCONNECTED;
                }
            }
            logger.trace( "m_bContinue:" + m_bContinue);
        } while( m_bContinue);
        
        logger.info( "Closing streams");
        if( is != null) {
            try { is.close(); } catch( IOException ex2) { logger.error( "Exception при финальном закрытии входящего потока", ex2);}
            is = null;
        }
        if( os != null) {
            try { os.close(); } catch( IOException ex2) { logger.error( "Exception при финальном закрытии исходящего потока", ex2);}
            os = null;
        }
        if( socket!= null) {
            if( !socket.isClosed())
                try { socket.close(); } catch( IOException ex2) { logger.error( "Exception при финальном закрытии сокета", ex2);}
            socket = null;
        }
        
        logger.info( "Closing socket");
        try {
            serverSocket.close();
        }
        catch( IOException ex2) {
            logger.error( "Exception при финальном закрытии серверного сокета", ex2);
        }
    }
}
