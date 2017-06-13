/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller.hv;

import hvv_poller.vac.*;
import hvv_poller.storage.HVV_Storage_DataUnit;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import org.apache.log4j.Logger;

/**
 * Thread-class for COM-port listener
 */
public class SocketReaderHv implements Runnable 
{
    static Logger logger = Logger.getLogger(SocketReaderHv.class);
    
    private final InputStream m_is;
    private final ObjectInputStream m_ois;
    
    TwoWaySocketServerCommHv pParent;
    
    private boolean m_bContinue;

    public void StopThread() {
        m_bContinue = false;
    }
    
    public SocketReaderHv( InputStream is, ObjectInputStream ois, TwoWaySocketServerCommHv parent)
    {
        m_is = is;
        m_ois = ois;
        pParent = parent;
    }
        
    public void run ()
    {
        m_bContinue = true;
        byte[] buffer = new byte[ CircleBuffer.BUFFER_LEN];
        int len = -1;
        
        logger.debug("In");        
        try {
            
            logger.debug("before while");
            
            while( m_bContinue) {
                
                try {
                    HVV_Storage_DataUnit unit;
                        
                    if( pParent != null &&
                                pParent.GetCmdInAction() != null &&
                                pParent.GetCmdInAction().equals( "QUIT")) {
                        
                        m_bContinue = false;
                        break;
                    }
                        
                    if( pParent.GetCmdInAction() != null) {
                        if( hvv_timeouts.HVV_TimeoutsManager.getInstance().CheckTimeout(pParent.m_lTimeOutId) == true) {
                            logger.info( "TimeOut happens for id=" + pParent.m_lTimeOutId + "  'REQ." + pParent.GetCmdInAction() + "' command!");
                            hvv_timeouts.HVV_TimeoutsManager.getInstance().RemoveId(pParent.m_lTimeOutId);
                            pParent.m_lTimeOutId = 0;
                            pParent.SetCmdInAction( null);
                            pParent.m_nTimeoutCounter++;
                        }
                        else {
                            if( m_is.available() > 0) {
                                String strMarker = ( String) m_ois.readObject();
                                logger.trace( "<-- " + strMarker + ";");
                                if( "RESP".equals( strMarker)) {

                                    if( pParent.theApp.m_pMainWnd != null)
                                        if( pParent.theApp.m_pMainWnd.lblLastActionHv != null)
                                            pParent.theApp.m_pMainWnd.lblLastActionHv.setText(
                                                    pParent.theApp.NiceFormatDateTime( pParent.theApp.GetLocalDate()));
                                    
                                    //pParent.GetTimeoutThread().interrupt();
                                    hvv_timeouts.HVV_TimeoutsManager.getInstance().RemoveId( pParent.m_lTimeOutId);
                                    pParent.m_lTimeOutId = 0;
                                    
                                    String strObject = ( String) m_ois.readObject();
                                    double dblValue = Double.NaN;
                                    
                                    logger.trace( "<-- " + strMarker + ";" + strObject + ";");
                                    
                                    switch( strObject) {
                                        case "MSW.01": 
                                        case "REG.01":
                                        case "PRE.01":
                                        case "VIB.01":
                                        {
                                            int nValue = ( Integer) m_ois.readObject(); dblValue = ( double) nValue;
                                        } break;

                                        case "L1A.01":
                                        case "L1A.02":
                                        case "L1A.03":
                                        case "L1T.01":
                                        case "L1T.02":
                                        case "L1T.03":
                                        case "L2A.01":
                                        case "L2A.02":
                                        case "L2A.03":
                                        case "L2T.01":
                                        case "L2T.02":
                                        case "L2T.03":
                                        case "L3A.01":
                                        case "L3A.02":
                                        case "L3A.03":
                                        case "L3T.01":
                                        case "L3T.02":
                                        case "L3T.03":
                                        case "L4A.01":
                                        case "L4A.02":
                                        case "L4A.03":
                                        case "L4T.01":
                                        case "L4T.02":
                                        case "L4T.03":
                                        case "L5A.01":
                                        case "L5A.02":
                                        case "L5A.03":
                                        case "L5T.01":
                                        case "L5T.02":
                                        case "L5T.03":
                                        case "L6A.01":
                                        case "L6A.02":
                                        case "L6A.03":
                                        case "L6T.01":
                                        case "L6T.02":
                                        case "L6T.03":
                                        case "L7A.01":
                                        case "L7A.02":
                                        case "L7A.03":
                                        case "L7T.01":
                                        case "L7T.02":
                                        case "L7T.03":
                                        case "L8A.01":
                                        case "L8A.02":
                                        case "L8A.03":
                                        case "L8T.01":
                                        case "L8T.02":
                                        case "L8T.03":{
                                            int nValue = ( Integer) m_ois.readObject(); dblValue = ( double) nValue;
                                        } break;

                                        case "LAA.01":
                                        case "LAT.01":{
                                            //logger.info(".");
                                            int nValue = ( Integer) m_ois.readObject(); dblValue = ( double) nValue;
                                        } break;

                                        case "DAC1.1":
                                        case "DAC1.2":
                                        case "DAC1.3":
                                        case "DAC1.4":
                                        case "DAC2.1":
                                        case "DAC2.2":
                                        case "DAC2.3":
                                        case "DAC2.4":
                                        case "DAC3.1":
                                        case "DAC3.2":
                                        case "DAC3.3":
                                        case "DAC3.4":
                                        case "DAC4.1":
                                        case "DAC4.2":
                                        case "DAC4.3":
                                        case "DAC4.4":
                                        {
                                            //logger.info(".");
                                            dblValue = ( double) m_ois.readObject();
                                        } break;

                                        default:
                                            logger.warn( "Unknown object '" + strObject + "'");
                                            //Dropping value
                                            Object dropObj = m_ois.readObject();
                                        break;
                                    }

                                    pParent.m_nTimeoutCounter = 0;

                                    try {
                                        unit = new HVV_Storage_DataUnit( pParent.theApp.GetLocalDate(), HVV_Storage_DataUnit.UNIT_HV,
                                            strObject, dblValue);

                                        if( unit == null)
                                            logger.error( "NULL Data-Unit");
                                        
                                        if( pParent.theApp.GetStorageThread() != null)
                                            pParent.theApp.GetStorageThread().AddDataPointToQueue( unit);
                                        //else
                                        //    logger.warn( "Storage Thread is NULL! Wow!");
                                    }
                                    catch( Exception ex) {
                                        logger.error( "Exception при создании DataUnit:",ex);
                                    }
                                    
                                    pParent.SetCmdInAction( null);

                                }
                                else {
                                    logger.warn( "Маркер-ответ не совпадает с 'RESP': '" + strMarker + "'");
                                }
                            }
                            else {
                                logger.trace( "Команда отправлена. Ждём ответ. Available bytes = 0");
                            }
                        }
                    }
                    else {
                        logger.trace( "Нет отправленной команды");
                    }
                    
                
                    Thread.sleep( 1);
                }
                catch ( IOException ex) {
                    logger.error( "IOException caught!", ex);
                    m_bContinue = false;
                }
            } //closing while
            
            //something new
            //something new2
            logger.debug("after while");
            
        }
        
        catch ( InterruptedException ex) {
            logger.error( "InterruptedException caught!", ex);
        } catch ( ClassNotFoundException ex) {
            logger.error( "ClassNotFoundException caught!", ex);
        }
        
        logger.debug("Out");
    }
}