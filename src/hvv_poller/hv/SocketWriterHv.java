/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller.hv;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class SocketWriterHv implements Runnable 
{
    static Logger logger = Logger.getLogger( SocketWriterHv.class);
    ObjectOutputStream out;

    
    
    private boolean m_bContinue;
    public void StopThread() {
        m_bContinue = false;
    }

    TwoWaySocketServerCommHv pParent;
    
    
    public SocketWriterHv ( ObjectOutputStream out, TwoWaySocketServerCommHv parent)
    {
        this.out = out;
        this.pParent = parent;
    }
        
    @Override
    public void run ()
    {
        m_bContinue = true;
        
        String strItem;
        try {
            
            logger.debug("before while");
            
            while( m_bContinue) {

                try {
                    
                    if( pParent.GetCmdInAction() == null) {                 //если мы в этот момент ничего не обрабатываем
                        if( pParent.cmdQueue.isEmpty() == false) {            //в очереди команд на обработку что-то есть
                            
                            strItem = ( String) pParent.cmdQueue.poll();
                            
                            if( strItem != null) {
                                if( strItem.equals( "QUIT")) {
                                    this.out.writeObject( "QUIT");
                                    this.out.writeObject( "");
                                    this.out.flush();
                                    m_bContinue = false;

                                    logger.trace( "--> QUIT;;");
                                    
                                    pParent.SetCmdInAction( "");
                                }
                                else {
                                    //logger.debug( "Item from queue: '" + strItem + "'!");
                                    //logger.debug( "Queue length: " + cmdQueue.size());

                                    String strCommand = "REQ";
                                    
                                    this.out.writeObject( "REQ");
                                    this.out.writeObject( strItem);
                                    this.out.flush();

                                    logger.trace( "--> REQ;" + strItem + ";");

                                    
                                }

                                //START TIMEOUT INSTANCE
                                //pParent.CreateNewTimeoutThread();
                                //pParent.GetTimeoutThread().start();
                                pParent.m_lTimeOutId = hvv_timeouts.HVV_TimeoutsManager.getInstance().StartTimeout( 1500);
                                
                                pParent.SetCmdInAction( strItem);
                            }
                            else {
                                logger.debug( "Item from queue: 'NULL'! Wow!");
                                logger.debug( "Item from queue is 'null'! Queue length: " + pParent.cmdQueue.size());
                            }

                        }
                        else {
                            logger.trace( "2. Очередь команд пустая!");
                        }
                    }
                    else {
                        //Есть команда в обработке (отправлена, и окончания обработки ответа ещё не было 
                        logger.trace( "1. Команда испущена, а сигнала об окончнии её обработки или таймаута не было!");
                        
                        /*
                        boolean bCheckTimeout = hvv_timeouts.HVV_TimeoutsManager.getInstance().CheckTimeout( pParent.m_lTimeOut);
                        
                        //( pParent.GetTimeoutThread().GetInProgress() == false)) {         //таймаут поток погашен 1
                        if( bCheckTimeout == true) {
                            logger.debug( "Есть команда в обработке! Таймаут кончился. Сбрасываем отправленную команду.");
                            pParent.strCommandInAction = null;
                        }
                        else {
                            logger.debug( "Есть команда в обработке! Таймаут не кончился.");
                        }
                        */
                    }
                    
                    Thread.sleep( 1);
                }
                catch ( IOException ex) {
                    logger.error( "IOException caught!", ex);
                    m_bContinue = false;
                }
                
            } // closing while
        }
        catch ( InterruptedException ex) {
            logger.error( "InterruptedException caught!", ex);
        }
        
        logger.debug("Out");
    }
}