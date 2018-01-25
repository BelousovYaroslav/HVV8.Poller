/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller.vac;

import hvv_poller.HVV_Poller;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class HVV_Poller_vac implements Runnable {
    HVV_Poller theApp;
    TwoWaySocketServerCommVac m_rxtx;
    static Logger logger = Logger.getLogger( HVV_Poller_vac.class);
    
    private int m_nState;
    public int GetState() { return m_nState; }
    
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTED_OK = 1;
    public static final int STATE_CONNECTED_PROBLEMS = 2;
    public static final int STATE_CONNECTED_IDLE = 3;
    
    public int m_nRespondTimeOuts;
    
    public Thread m_Thread;
    boolean m_bContinue;
    
    public HVV_Poller_vac( HVV_Poller app) {
        theApp = app;
        
        m_rxtx = new TwoWaySocketServerCommVac( app);
        
        m_Thread = null;
        m_nRespondTimeOuts = 0;
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
        
        do {
            //logger.info( "HVV_Poller_vac thread cycle in...");
            if( m_rxtx.IsConnected()) {
                
                if( m_rxtx != null &&
                            m_rxtx.thrInput != null && m_rxtx.thrOutput != null &&
                            m_rxtx.thrInput.isAlive() && m_rxtx.thrOutput.isAlive()) {
                    //logger.info( "HVV_Poller_vac thread is alive, connected, and both reader and writer are running!");
                    
                    //m_rxtxVac.AddCommandToQueue( "REQUEST");
                
                    try {
                        sleep( 1000);
                    } catch (InterruptedException ex) {
                        logger.warn( "InterruptedException caught!", ex);
                    }
                    
                    m_nRespondTimeOuts++;
                    if( m_nRespondTimeOuts >= 20) {
                        logger.warn( m_nRespondTimeOuts + " сек бездействия со стороны вакуумной части. Disconnecting, then Pause 10 sec.");
                        try {
                            m_nState = STATE_DISCONNECTED;
                            m_rxtx.disconnect();
                        } catch (Exception ex) {
                            logger.warn( "Exception caught!", ex);
                        }
                        
                        logger.warn( m_nRespondTimeOuts + " сек бездействия со стороны вакуумной части. Disconnected. Starting pause.");
                        try {
                            sleep( 10000);
                        } catch (InterruptedException ex) {
                            logger.warn( "Pause interrupted!", ex);
                        }
                        logger.warn( m_nRespondTimeOuts + " сек бездействия со стороны вакуумной части. Pause passed");
                        
                    } else if( m_nRespondTimeOuts > 10) {
                        logger.warn( m_nRespondTimeOuts + " сек бездействия со стороны вакуумной части. Idle.");
                        logger.warn( "m_rxtx=" + m_rxtx);
                        logger.warn( "m_rxtx.thrInput=" + m_rxtx.thrInput + "  m_rxtx.thrOutput=" + m_rxtx.thrOutput);
                        logger.warn( "m_rxtx.thrInput.isa=" + m_rxtx.thrInput.isAlive() + "  m_rxtx.thrOutput.isa=" + m_rxtx.thrOutput.isAlive());
                        m_nState = STATE_CONNECTED_IDLE;                                
                    }
                    else {
                        //мы подсоединены - всё ок
                        m_nState = STATE_CONNECTED_OK;
                    }
                    /*
                    if( m_rxtxVac.crclBuffer.isAnswerReady() > 0) {
                        
                    }*/
                            
                }
                else {
                    logger.info( "Connection broken! Disconnecting!");
                    logger.info( "m_rxtx = " + m_rxtx);
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
                
                theApp.IncPollerVacReconnections();
                
                try {
                    m_rxtx.connect();
                    m_nRespondTimeOuts = 0;
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
            }
            
        } while( m_bContinue);
        
        try {
            m_rxtx.disconnect();
        } catch( Exception ex) {
            logger.warn( "Exception caught!", ex);
        }
    }
}
