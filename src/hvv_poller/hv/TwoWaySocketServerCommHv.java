package hvv_poller.hv;


import hvv_poller.HVV_Poller;
import hvv_poller.vac.CircleBuffer;
import hvv_timeouts.HVV_TimeoutsManager;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

public class TwoWaySocketServerCommHv
{
    public final HVV_Poller theApp;
    static Logger logger = Logger.getLogger(TwoWaySocketServerCommHv.class);
    
    public Thread thrInput;
    public Thread thrOutput;
            
    Socket pSocket;
        
    volatile public long m_lTimeOutId;
    volatile int m_nTimeoutCounter;
    volatile private String strCommandInAction;
    
    synchronized String GetCmdInAction() { return strCommandInAction; }
    synchronized void SetCmdInAction( String strNewAction) { strCommandInAction = strNewAction; }
    
    //public CircleBuffer crclBuffer;
    
    SocketReaderHv sr;
    SocketWriterHv sw;
    
    /* ********************************************************** */
    /* *************** COMMAND QUEUE **************************** */
    public final ConcurrentLinkedQueue cmdQueue;
    
    public synchronized void AddCommandToQueue( String strObj) {
        cmdQueue.add( strObj);
        //logger.debug( "AddCommandToQueue(" + strObj + ",...): queue length: " + cmdQueue.size());
    }
    
    public synchronized int GetCommandQueueLen() { return cmdQueue.size(); }
    /* ********************************************************** */
    
    
    public TwoWaySocketServerCommHv( HVV_Poller app)
    {
        cmdQueue = new ConcurrentLinkedQueue();
        //crclBuffer = new CircleBuffer();
        
        m_lTimeOutId = 0;
        m_nTimeoutCounter = 0;
        
        pSocket = null;
        theApp = app;
    }
    
    public boolean connect( /*COMPortSettings pSettings*/) throws Exception
    {
        if( pSocket != null && !pSocket.isClosed()) {
            logger.error( "Socket is already open!");
            return false;
        }
        
        int nVacPort = theApp.GetSettings().GetHvPartPort();
        String strVacHost = theApp.GetSettings().GetHvPartHost();
        
        try {
            pSocket = new Socket( InetAddress.getByName( strVacHost), nVacPort);
        }
        catch( SocketException ex) {
            //logger.info( "SocketException caught", ex);
            logger.info( "SocketException: " + ex.getLocalizedMessage());
            pSocket = null;
            return false;
        }
        
        InputStream is = pSocket.getInputStream();
        ObjectInputStream in = new ObjectInputStream( is);
        ObjectOutputStream out = new ObjectOutputStream( pSocket.getOutputStream());
                
        sr = new SocketReaderHv( is, in, this);
        thrInput = new Thread( sr);
        thrInput.start();
        
        sw = new SocketWriterHv( out, this);
        thrOutput = new Thread( sw);
        thrOutput.start();
        
        return true;
    }
    
    public boolean IsConnected() {
        if( pSocket == null) return false;
        //logger.debug( "Socket isConnected:" + pSocket.isConnected());
        return pSocket.isConnected();
    }
    
    public void disconnect( /*COMPortSettings pSettings*/) throws Exception {
        //if( pTimeoutThread != null)
        //    pTimeoutThread.interrupt();
        
        if( m_lTimeOutId != 0) {            
            HVV_TimeoutsManager.getInstance().RemoveId(m_lTimeOutId);
            m_lTimeOutId = 0;
        }
        
        if( sr != null) {
            sr.StopThread();
            thrInput.join();
            
            //thrInput.interrupt();
            
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
    }
}