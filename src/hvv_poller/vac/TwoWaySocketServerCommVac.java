package hvv_poller.vac;


import hvv_poller.HVV_Poller;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;

import org.apache.log4j.Logger;

public class TwoWaySocketServerCommVac
{
    public final HVV_Poller theApp;
    static Logger logger = Logger.getLogger(TwoWaySocketServerCommVac.class);
    
    public Thread thrInput;
    public Thread thrOutput;
            
    Socket pSocket;
    
    private CmdRespondTimeoutThreadVac pTimeoutThread;
    public CmdRespondTimeoutThreadVac GetTimeoutThread() { return pTimeoutThread;}
    public void CreateNewTimeoutThread() {
        if( pTimeoutThread == null) {
            logger.warn( "CreateNewTimeoutThread(): Previous thread object was null! Strange situation.");
        }
        else if( pTimeoutThread.getState() != Thread.State.TERMINATED) {
            logger.warn( "CreateNewTimeoutThread(): Previous thread object state = " + pTimeoutThread.getState());
        }
        pTimeoutThread = new CmdRespondTimeoutThreadVac( this);
    }
            
    public String strCommandInAction;

    public CircleBuffer crclBuffer;
    
    SocketReaderVac sr;
    SocketWriterVac sw;
    
    /* ********************************************************** */
    /* *************** COMMAND QUEUE **************************** */
    //private Stack cmdQueue;
    private LinkedList cmdQueue;
    public synchronized void AddCommandToQueueEmergent( String strCmd) {
        cmdQueue.addFirst( new String( strCmd));
        logger.debug( "AddCommandToQueueEmergent(" + strCmd + ",...): queue length: " + cmdQueue.size());
    }
    
    public synchronized void AddCommandToQueue( String strCmd) {
        cmdQueue.addLast( new String( strCmd));
        logger.debug( "AddCommandToQueue(" + strCmd + ",...): queue length: " + cmdQueue.size());
    }
    
    public synchronized int GetCommandQueueLen() { return cmdQueue.size(); }
    /* ********************************************************** */
    
    
    public TwoWaySocketServerCommVac( HVV_Poller app)
    {
        cmdQueue = new LinkedList();
        crclBuffer = new CircleBuffer();
        pTimeoutThread = new CmdRespondTimeoutThreadVac( this);
        pSocket = null;
        theApp = app;
    }
    
    public boolean connect( /*COMPortSettings pSettings*/) throws Exception
    {
        if( pSocket != null && !pSocket.isClosed()) {
            logger.error( "Socket is already open!");
            return false;
        }
        
        int nVacPort = theApp.GetSettings().GetVacuumPartPort();
        String strVacHost = theApp.GetSettings().GetVacuumPartHost();
        
        try {
            pSocket = new Socket();
            /*pSocket.setSoTimeout( 2000);
            pSocket.setSoLinger(true, 2000);
            pSocket.connect( new InetSocketAddress( InetAddress.getByName( strVacHost), nVacPort));
            */
            pSocket.connect( new InetSocketAddress( InetAddress.getByName( strVacHost), nVacPort), 2000);
        }
        catch( SocketException ex) {
            //logger.info( "SocketException caught", ex);
            logger.info( "SocketException: " + ex.getLocalizedMessage());
            pSocket = null;
            return false;
        }
        catch( SocketTimeoutException ex) {
            logger.info( "SocketTimeoutException: " + ex.getLocalizedMessage());
            if( !pSocket.isClosed())
                pSocket.close();
            pSocket = null;
            return false;
        }
        
        InputStream in = pSocket.getInputStream();
        OutputStream out = pSocket.getOutputStream();
                
        sr = new SocketReaderVac( in, this);
        thrInput = new Thread( sr);
        thrInput.start();
        
        sw = new SocketWriterVac( out, cmdQueue, this);
        thrOutput = new Thread( sw);
        thrOutput.start();
        
        crclBuffer = new CircleBuffer();
        
        return true;
    }
    
    public boolean IsConnected() {
        if( pSocket == null) return false;
        //logger.debug( "Socket isConnected:" + pSocket.isConnected());
        return pSocket.isConnected();
    }
    
    public void disconnect( /*COMPortSettings pSettings*/) throws Exception {
        if( pTimeoutThread != null)
            pTimeoutThread.interrupt();
        
        if( sr != null) {
            sr.StopThread();
            thrInput.join();
            thrInput = null;
            sr = null;
        }
        
        if( sw != null) {
            sw.StopThread();
            thrOutput.join();
            thrOutput = null;
            sw = null;
        }
        
        if( pSocket != null) {
            pSocket.close();
            pSocket = null;
        }
        
        crclBuffer = null;
    }
}