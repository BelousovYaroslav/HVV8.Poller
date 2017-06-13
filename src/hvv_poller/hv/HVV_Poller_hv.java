/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller.hv;

import hvv_devices.HVV_HvDevice;
import hvv_devices.HVV_HvDevices;
import hvv_poller.vac.*;
import hvv_poller.HVV_Poller;
import static java.lang.Thread.sleep;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class HVV_Poller_hv implements Runnable {
    HVV_Poller theApp;
    TwoWaySocketServerCommHv m_rxtx;
    static Logger logger = Logger.getLogger( HVV_Poller_hv.class);
    
    private int m_nState;
    public int GetState() { return m_nState; }
    
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTED_OK = 1;
    public static final int STATE_CONNECTED_PROBLEMS = 2;
    public static final int STATE_CONNECTED_IDLE = 3;
    
    public Thread m_Thread;
    boolean m_bContinue;
    
    private final HVV_HvDevices m_hvDevices;
    
    public HVV_Poller_hv( HVV_Poller app) {
        theApp = app;
        m_rxtx = new TwoWaySocketServerCommHv( app);
        m_Thread = null;
        m_nState = STATE_DISCONNECTED;
        m_hvDevices = hvv_devices.HVV_HvDevices.getInstance();
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
                m_rxtx.AddCommandToQueue( "QUIT");
                
                m_bContinue = false;
                
                if( m_rxtx.thrInput != null) m_rxtx.thrInput.join();
                if( m_rxtx.thrOutput != null) m_rxtx.thrOutput.join();
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
        
        Set setDevices = m_hvDevices.m_devices.entrySet();
        Iterator itDevices = setDevices.iterator();
        
        if( itDevices.hasNext() == false) {
            logger.fatal( "IOPT!");
            return;
        }
        
        Map.Entry entryDevices = ( Map.Entry) itDevices.next();
        HVV_HvDevice hvDevice = ( HVV_HvDevice) entryDevices.getValue();
        Set setParams = hvDevice.m_mapParameters.entrySet();
        Iterator itParams = setParams.iterator();
        
        do {
            //logger.info( "HVV_Poller_hv thread cycle in...");
            if( m_rxtx.IsConnected()) {
                //мы подсоединены - всё ок
                if( m_rxtx.m_nTimeoutCounter > 10) {
                    m_nState = STATE_CONNECTED_IDLE;
                    
                    logger.info( "Получено " + m_rxtx.m_nTimeoutCounter + " таймаутов. После двадцатого порвём связь, и будем устанавливать её заново.");
                    if( m_rxtx.m_nTimeoutCounter >= 20) {
                        logger.warn( "Достигли максимальное число таймаутов. Рвём связь, и будем заново создавать её.");
                        m_nState = STATE_DISCONNECTED;
                        try {
                            m_rxtx.disconnect();
                        } catch( Exception ex) {
                            logger.error( "Exception happens on disconnecting m_rxtx.", ex);
                        }
                        m_rxtx = null;
                    }
                }
                else
                    m_nState = STATE_CONNECTED_OK;
                
                if( m_rxtx != null &&
                            m_rxtx.thrInput != null && m_rxtx.thrOutput != null &&
                            m_rxtx.thrInput.isAlive() && m_rxtx.thrOutput.isAlive()) {
                    //logger.info( "HVV_Poller_hv thread is alive, connected, and both reader and writer are running!");
                    
                    
                    if( m_rxtx.cmdQueue.size() < 5) {
                        
                        String strObjectToPoll;
                        if( itParams.hasNext()) {
                            Map.Entry entry = ( Map.Entry) itParams.next();
                            strObjectToPoll = hvDevice.m_strID + "." + ( String) entry.getKey();
                        }
                        else {
                            if( itDevices.hasNext()) {
                                //переходим к параметрам следующего device
                                entryDevices = ( Map.Entry) itDevices.next();
                                hvDevice = ( HVV_HvDevice) entryDevices.getValue();
                                setParams = hvDevice.m_mapParameters.entrySet();
                                itParams = setParams.iterator();

                                Map.Entry entry = ( Map.Entry) itParams.next();
                                strObjectToPoll = hvDevice.m_strID + "." + ( String) entry.getKey();
                            }
                            else {
                                //переходим к первому device
                                itDevices = setDevices.iterator();
                                entryDevices = ( Map.Entry) itDevices.next();
                                hvDevice = ( HVV_HvDevice) entryDevices.getValue();
                                setParams = hvDevice.m_mapParameters.entrySet();
                                itParams = setParams.iterator();

                                Map.Entry entry = ( Map.Entry) itParams.next();
                                strObjectToPoll = hvDevice.m_strID + "." + ( String) entry.getKey();
                            }

                        }


                        m_rxtx.AddCommandToQueue( strObjectToPoll);
                    }
                
                    try {
                        sleep( 1);
                    } catch (InterruptedException ex) {
                        logger.warn( "InterruptedException caught!", ex);
                    }
                            
                }
                else {
                    logger.info( "Connection broken! Disconnecting!");
                    logger.info( "m_rxtx=" + m_rxtx);
                    if( m_rxtx != null) {
                        logger.info( "m_rxtx.thrInput=" + m_rxtx.thrInput);
                        if( m_rxtx.thrInput != null)
                            logger.info( "m_rxtx.thrInput.isAlive()=" + m_rxtx.thrInput.isAlive());
                        
                        logger.info( "m_rxtx.thrOutput=" + m_rxtx.thrOutput);
                        if( m_rxtx.thrOutput != null)
                            logger.info( "m_rxtx.thrOutput.isAlive()=" + m_rxtx.thrOutput.isAlive());
                    }

                    try {
                        if( m_rxtx != null)
                            m_rxtx.disconnect();
                        
                    } catch( Exception ex) {
                        logger.warn( "Exception caught!", ex);
                    }
                    m_nState = STATE_DISCONNECTED;
                }
            }
            else {
                //мы не подсоединены... подсоединяемся
                m_nState = STATE_DISCONNECTED;
                try {
                    m_rxtx.connect();
                } catch( Exception ex) {
                    logger.warn( "Exception caught!", ex);
                }
            
                if( m_rxtx.IsConnected() == false) {
                    logger.warn( "Попытка соединиться неуспешна.");
                    try {
                        sleep( 5000);
                    } catch (InterruptedException ex) {
                        logger.warn( "InterruptedException caught!", ex);
                    }
                }
                else {
                    logger.info( "Связь с HV модулем установлена!");
                    m_rxtx.SetCmdInAction(null);
                }
            }
            
        } while( m_bContinue);
        
        /*
        try {
            m_rxtx.disconnect();
        } catch( Exception ex) {
            logger.warn( "Exception caught!", ex);
        }
        */
    }
}
