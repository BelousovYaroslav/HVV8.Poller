/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller.vac;

import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class CmdRespondTimeoutThreadVac extends Thread {//implements Runnable {
    static Logger logger = Logger.getLogger(CmdRespondTimeoutThreadVac.class);
    TwoWaySocketServerCommVac m_SocketRxTx;
    
    private boolean m_bInProgress;
    public boolean GetInProgress() { return m_bInProgress;}
    
    private int m_nTimeOut;
    public int GetTimeout() { return m_nTimeOut; }
    public void SetTimeout( int nNewTimeOut) {
        m_nTimeOut = nNewTimeOut;
    }
    
    /*
    private boolean m_bCancel;
    public void CancelTimeout() { m_bCancel = true;}
    */
    
    public CmdRespondTimeoutThreadVac( TwoWaySocketServerCommVac rxtx) {
        m_nTimeOut = 1000;
        m_SocketRxTx = rxtx;
    }
    
    @Override
    public void run() {
        m_bInProgress = true;
        logger.trace( "Timeout thread starts!");
        
        try {
            //m_bCancel = false;
            
            Thread.sleep( m_nTimeOut);
            
            logger.debug( "Timeout happens!");
            
            if( m_SocketRxTx.strCommandInAction != null) {
                logger.warn( "TODO: Обработка TimeOut'а!");
                /*
                m_SocketRxTx.strCommandInAction.ProcessTimeOut();
                */
                
                m_SocketRxTx.strCommandInAction = null;
            }
            else {
                logger.warn( "Произошло события таймаута, однако текущая обрабатываемая команда null!");
            }
            
        }
        catch ( InterruptedException ex) {
            logger.trace( "Timeout thread interrupted!");
        }
        
        m_bInProgress = false;
    }
    
}
