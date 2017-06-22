/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller.storage;

import hvv_poller.HVV_Poller;
import static java.lang.Thread.sleep;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;
import org.jfree.data.time.Millisecond;

/**
 *
 * @author yaroslav
 */
public class HVV_StorageThread implements Runnable {
    static Logger logger = Logger.getLogger( HVV_StorageThread.class);
    
    private ConcurrentLinkedQueue m_lstDataQueue;
    private HVV_Storage_FileWriter m_FWriter;
            
    Thread m_thread;
    private boolean m_bContinue;
    
    HVV_Poller theApp;
    
    int m_nLastDay;
    
    public void StopThread() {
        m_bContinue = false;
        try {
            
            if( m_thread != null && m_thread.isAlive())
                m_thread.join();
            
            m_FWriter.StopFileWriter();
        } catch (InterruptedException ex) {
            logger.warn( "InterruptedException caught", ex);
        }
        
    }
    
    public HVV_StorageThread( HVV_Poller app) throws Exception {
        theApp = app;
        
        m_FWriter = new HVV_Storage_FileWriter( app);
                
        m_bContinue = false;
        m_thread = null;
        
        m_lstDataQueue = new ConcurrentLinkedQueue();
    }
    
    public void StartThread() {
        GregorianCalendar clndr = new GregorianCalendar();
        clndr.setTime( theApp.GetLocalDate());
        m_nLastDay = clndr.get( Calendar.DAY_OF_MONTH);
        
        m_thread = new Thread ( this);
        m_thread.start();
    }
    
    public void AddDataPointToQueue( HVV_Storage_DataUnit unit) {
        if( unit == null) {
            logger.error( "NULL parameter!");
        }
        m_lstDataQueue.add( unit);
    }

    @Override
    public void run() {
        m_bContinue = true;
        do {
            
            GregorianCalendar clndrNow = new GregorianCalendar();
            clndrNow.setTime( theApp.GetLocalDate());
            if( clndrNow.get( Calendar.DAY_OF_MONTH) != m_nLastDay) {
                logger.info( "Смена дня!");
                m_nLastDay = clndrNow.get( Calendar.DAY_OF_MONTH);
                m_FWriter.DayChange();
            }
        
            if( m_lstDataQueue.size() > 0) {
                do {
                    HVV_Storage_DataUnit unit = ( HVV_Storage_DataUnit) m_lstDataQueue.poll();
                    if( unit != null) {
                        m_FWriter.saveDataUnit( unit);
                        //logger.debug( "SAVED " + unit.GetDescriptor());
                        
                        if( theApp.m_pMainWnd != null) {
                            String strDescriptor = unit.GetDescriptor();
                        
                            String strDescriptorGraph1 = theApp.m_pMainWnd.strGraph1Device + "." + theApp.m_pMainWnd.strGraph1DeviceParam;
                            if( strDescriptor.equals( strDescriptorGraph1)) {
                                theApp.m_serie1.addOrUpdate( new Millisecond(), unit.GetValue());
                            }
                            
                            String strDescriptorGraph2 = theApp.m_pMainWnd.strGraph2Device + "." + theApp.m_pMainWnd.strGraph2DeviceParam;
                            if( strDescriptor.equals( strDescriptorGraph2)) {
                                theApp.m_serie2.addOrUpdate( new Millisecond(), unit.GetValue());
                            }
                            
                            String strDescriptorGraph3 = theApp.m_pMainWnd.strGraph3Device + "." + theApp.m_pMainWnd.strGraph3DeviceParam;
                            if( strDescriptor.equals( strDescriptorGraph3)) {
                                theApp.m_serie3.addOrUpdate( new Millisecond(), unit.GetValue());
                            }
                            
                            String strDescriptorGraph4 = theApp.m_pMainWnd.strGraph4Device + "." + theApp.m_pMainWnd.strGraph4DeviceParam;
                            if( strDescriptor.equals( strDescriptorGraph4)) {
                                theApp.m_serie4.addOrUpdate( new Millisecond(), unit.GetValue());
                            }
                        }
                    }
                    else {
                        logger.warn( "NULL element from m_lstDataQueue. Size=" + m_lstDataQueue.size());
                    }
                } while( m_lstDataQueue.size() > 0);
            }
            else {
                //logger.debug( ".");
            }
            
            try {
                sleep( 100);
            } catch (InterruptedException ex) {
                logger.warn( "Interrupted Exception caught", ex);
            }
        } while( m_bContinue);
    }
}
