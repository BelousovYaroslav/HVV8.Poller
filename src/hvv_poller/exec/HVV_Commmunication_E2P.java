/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller.exec;

import hvv_poller.HVV_Poller;
import java.util.LinkedList;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class HVV_Commmunication_E2P extends HVV_Communication.server.HVV_Comm_Server {
    final HVV_Poller theApp;
    static Logger logger = Logger.getLogger(HVV_Commmunication_E2P.class);
    
    public HVV_Commmunication_E2P( int nServerPort, HVV_Poller app) {
        super( "E2P_SRV: ", nServerPort);
        theApp = app;
        
    }
    
    @Override
    public void processIncomingCommand( String strReqId, LinkedList lstIncomingParcel) throws Exception {
        String strCmd;
        
        int nRetCode = 0;
        
        double dblRespondValue = 0.;
        boolean bGet = false;
        
        strCmd = ( String) lstIncomingParcel.get( 0);
        if( strCmd != null) {
            switch( strCmd) {
                case "GET":
                    String strObject = ( String) lstIncomingParcel.get(1);
                    if( strObject != null) {

                        logger.debug( "E2P_SRV: << [" + strReqId + ";GET;" + strObject + "]");

                        if( !theApp.m_mapHotValues.containsKey( strObject)) {
                            logger.warn( "Requested param '" + strObject + "' is not in hot-list! RetCode 6.");
                            nRetCode = 6;
                        }
                        else {
                            dblRespondValue = ( double) theApp.m_mapHotValues.get( strObject);
                            bGet = true;
                            nRetCode = 0;
                            theApp.m_pMainWnd.lblLastActionExecutor.setText( theApp.NiceFormatDateTime( theApp.GetLocalDate()));
                        }
                    }
                    else {
                        logger.error( "" + strReqId + ": GET: Object is NULL. RetCode 4");
                        nRetCode = 4;
                    }
                break;
                    
                case "PING":
                    logger.debug( "E2P_SRV << : [" + strReqId + ";PING;" + "]");
                    nRetCode = 0;
                    theApp.m_pMainWnd.lblLastActionExecutor.setText( theApp.NiceFormatDateTime( theApp.GetLocalDate()));
                    
                    if( m_nStopRequested == 1) {
                        nRetCode = 100;
                        m_nStopRequested = 2;
                    }
                break;

                case "QUIT":
                    logger.info( "'QUIT' processing");
                    theApp.m_pMainWnd.lblLastActionExecutor.setText( theApp.NiceFormatDateTime( theApp.GetLocalDate()));
                    SetState( STATE_DISCONNECTED);
                    return;

                default:
                    logger.error( "" + strReqId + ": Unknown command '" + strCmd + "'. RetCode 3");
                    nRetCode = 3;
                break;
            }
        }
        else {
            logger.error( "" + strReqId + ": Command is null. RetCode 2");
            nRetCode = 2;
        }


        if( bGet) {
            //RESPOND
            logger.debug( "E2P_SRV: >> [ " + strReqId + ";" + nRetCode + ";" + String.format("%.3e", dblRespondValue) + "]");

            GetObjectOutputStream().writeObject( strReqId);
            GetObjectOutputStream().writeInt( 2);
            GetObjectOutputStream().writeObject( nRetCode);
            GetObjectOutputStream().writeObject( dblRespondValue);
            GetObjectOutputStream().flush();
        }
        else {
            //RESPOND
            logger.debug( "E2P_SRV: >> [ " + strReqId + ";" + nRetCode + "]");

            GetObjectOutputStream().writeObject( strReqId);
            GetObjectOutputStream().writeInt( 1);
            GetObjectOutputStream().writeObject( nRetCode);
            GetObjectOutputStream().flush();
        }
    }
    
}
