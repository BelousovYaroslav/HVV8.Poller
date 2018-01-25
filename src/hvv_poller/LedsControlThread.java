/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller;

import hvv_poller.hv.HVV_Poller_hv;
import hvv_poller.vac.HVV_Poller_vac;
import static java.lang.Thread.sleep;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;

/**
 *
 * @author yaroslav
 */
public class LedsControlThread implements Runnable {
    private final HVV_Poller theApp;

    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( LedsControlThread.class);
            
    private boolean m_bContinue = false;
    private Thread m_Thread;
    
    public LedsControlThread( HVV_Poller app) {
        theApp = app;
    }

    
    public void start() {
        m_Thread = new Thread( this);
        m_Thread.start();
    }
    
    public void stop() {
        m_bContinue = false;
        try {
            if( m_Thread != null)
                m_Thread.join();
        } catch (InterruptedException ex) {
            logger.warn( "InterruptedException caught: ", ex);
        }
    }
        
    @Override
    public void run() {
        m_bContinue = true;
        do {
            ImageIcon img = null;
            
            //VACUUM
            switch( theApp.GetPollerVacThread().GetState()) {                
                case HVV_Poller_vac.STATE_CONNECTED_OK:
                    img = theApp.GetResources().getIconBigGreen();
                break;
                
                case HVV_Poller_vac.STATE_DISCONNECTED:
                    img = theApp.GetResources().getIconBigRed();
                break;
                
                case HVV_Poller_vac.STATE_CONNECTED_IDLE:
                    img = theApp.GetResources().getIconBigBlue();
                break;
                        
                default:
                    img = theApp.GetResources().getIconBigBlack();
                break;
            }
            if( theApp.m_pMainWnd != null) {
                if( img != null && theApp.m_pMainWnd.lblLedVac != null) {
                    theApp.m_pMainWnd.lblLedVac.setIcon( img);
                    theApp.m_pMainWnd.lblLedVac.setText( "");
                    theApp.m_pMainWnd.lblLedVac.setBorder( null);
                    theApp.m_pMainWnd.lblReconnectionsVac.setText( ""  + theApp.GetPollerVacReconnections());
                }
                else {
                    theApp.m_pMainWnd.lblLedVac.setText( "LED");
                    theApp.m_pMainWnd.lblLedVac.setBorder( LineBorder.createBlackLineBorder());
                }
            }
            
            
            //HV
            switch( theApp.GetPollerHvThread().GetState()) {                
                case HVV_Poller_hv.STATE_CONNECTED_OK:
                    img = theApp.GetResources().getIconBigGreen();
                break;
                
                case HVV_Poller_hv.STATE_DISCONNECTED:
                    img = theApp.GetResources().getIconBigRed();
                break;
                    
                default:
                    img = theApp.GetResources().getIconBigBlack();
                break;
            }
            if( theApp.m_pMainWnd != null) {
                if( img != null && theApp.m_pMainWnd.lblLedHv != null) {
                    theApp.m_pMainWnd.lblLedHv.setIcon( img);
                    theApp.m_pMainWnd.lblLedHv.setText( "");
                    theApp.m_pMainWnd.lblLedHv.setBorder( null);
                    theApp.m_pMainWnd.lblReconnectionsHv.setText( "" + theApp.GetPollerHvReconnections());                    
                }
                else {
                    theApp.m_pMainWnd.lblLedHv.setText( "LED");
                    theApp.m_pMainWnd.lblLedHv.setBorder( LineBorder.createBlackLineBorder());
                }
            }
            
            //EXECUTOR
            switch( theApp.GetCommE2P().GetState()) {                
                case HVV_Communication.server.HVV_Comm_Server.STATE_CONNECTED_OK:
                    img = theApp.GetResources().getIconBigGreen();
                break;
                
                case HVV_Communication.server.HVV_Comm_Server.STATE_CONNECTED_PROBLEMS:
                    img = theApp.GetResources().getIconBigRed();
                break;
                
                case HVV_Communication.server.HVV_Comm_Server.STATE_CONNECTED_IDLE:
                    img = theApp.GetResources().getIconBigBlue();
                break;
                        
                default:
                    img = theApp.GetResources().getIconBigBlack();
                break;
            }
            if( theApp.m_pMainWnd != null) {
                if( img != null && theApp.m_pMainWnd.lblLedExecutor != null) {
                    theApp.m_pMainWnd.lblLedExecutor.setIcon( img);
                    theApp.m_pMainWnd.lblLedExecutor.setText( "");
                    theApp.m_pMainWnd.lblLedExecutor.setBorder( null);
                    theApp.m_pMainWnd.lblReconnectionsExe.setText( "" + theApp.GetCommE2P().GetReconnections()); 
                }
                else {
                    theApp.m_pMainWnd.lblLedExecutor.setText( "LED");
                    theApp.m_pMainWnd.lblLedExecutor.setBorder( LineBorder.createBlackLineBorder());
                }
            }
            
            //ADMIN
            switch( theApp.GetCommA2P().GetState()) {                
                case HVV_Communication.server.HVV_Comm_Server.STATE_CONNECTED_OK:
                    img = theApp.GetResources().getIconBigGreen();
                break;
                
                case HVV_Communication.server.HVV_Comm_Server.STATE_CONNECTED_PROBLEMS:
                    img = theApp.GetResources().getIconBigRed();
                break;
                
                case HVV_Communication.server.HVV_Comm_Server.STATE_CONNECTED_IDLE:
                    img = theApp.GetResources().getIconBigBlue();
                break;
                        
                default:
                    img = theApp.GetResources().getIconBigBlack();
                break;
            }
            if( theApp.m_pMainWnd != null) {
                if( img != null && theApp.m_pMainWnd.lblLedAdmin != null) {
                    theApp.m_pMainWnd.lblLedAdmin.setIcon( img);
                    theApp.m_pMainWnd.lblLedAdmin.setText( "");
                    theApp.m_pMainWnd.lblLedAdmin.setBorder( null);
                    theApp.m_pMainWnd.lblReconnectionsAdm.setText( "" + theApp.GetCommA2P().GetReconnections());
                }
                else {
                    theApp.m_pMainWnd.lblLedAdmin.setText( "LED");
                    theApp.m_pMainWnd.lblLedAdmin.setBorder( LineBorder.createBlackLineBorder());
                }
            }
            
            try {
                sleep( 1000);
            } catch (InterruptedException ex) {
                logger.warn( "InterruptedException caught: ", ex);
            }
        } while( m_bContinue);
    }
}
