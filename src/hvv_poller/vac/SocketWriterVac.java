/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller.vac;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class SocketWriterVac implements Runnable 
{
    static Logger logger = Logger.getLogger(SocketWriterVac.class);
    OutputStream out;
        
    private boolean m_bContinue;
    public void StopThread() {
        m_bContinue = false;
    }
                
    private final LinkedList cmdQueue;
    TwoWaySocketServerCommVac pParent;
    
    
    public SocketWriterVac ( OutputStream out, LinkedList queue, TwoWaySocketServerCommVac parent)
    {
        this.out = out;
        this.cmdQueue = queue;
        this.pParent = parent;
        
    }
        
    @Override
    public void run ()
    {
        String strToSend;
        m_bContinue = true;
        
        String strItem;
        try {
            
            logger.debug("before while");
            
            while( m_bContinue) {

                if( ( pParent.strCommandInAction == null) &&                        //нет команды в обработке
                        ( !cmdQueue.isEmpty()) &&                                       //в очереди что-то есть
                        ( pParent.GetTimeoutThread().GetInProgress() != true)) {        //таймаут поток погашен
                    
                    strItem = ( String) cmdQueue.poll();
                    
                    if( strItem != null) {
                        logger.debug( "Item from queue: '" + strItem + "'!");
                        logger.debug( "Queue length: " + cmdQueue.size());
                    
                        int nLen = strItem.length();
                        this.out.write( ( nLen & 0xFF));
                        this.out.write( ( nLen & 0xFF00) >> 8);
                        
                        
                        byte [] btsToSend = strItem.getBytes();
                        //logger.debug( "btsToSend[]=" + btsToSend);
                        this.out.write( strItem.getBytes());
                        
                        pParent.strCommandInAction = strItem;
                    
                        //logger.debug( "Timeout thread isAlive(): " + pParent.GetTimeoutThread().isAlive());
                        //logger.debug( "Timeout thread getState(): " + pParent.GetTimeoutThread().getState());
                        //logger.debug( "Timeout thread GetInProgress(): " + pParent.GetTimeoutThread().GetInProgress());


                        pParent.CreateNewTimeoutThread();
                        pParent.GetTimeoutThread().start();
                    }
                    else {
                        logger.debug( "Item from queue: 'NULL'! Wow!");
                        logger.debug( "Item from queue is 'null'! Queue length: " + cmdQueue.size());
                    }
                }
                else {
                    if( pParent.strCommandInAction != null) {
                        //logger.trace( "Есть команда в обработке!");
                    }
                    if( cmdQueue.isEmpty()) {
                        //logger.trace( "Очередь команды пустая!");
                    }
                    if( pParent.GetTimeoutThread().GetInProgress() != true) {
                        //logger.trace( "Поток таймаута не закончен!");
                    }
                }
                
                Thread.sleep( 20);
            }
        }
        catch ( IOException ex) {
            logger.error( "IOException caught!", ex);
        }
        catch ( InterruptedException ex) {
            logger.error( "InterruptedException caught!", ex);
        }
        
        logger.debug("Out");
    }
}