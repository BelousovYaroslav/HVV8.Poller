/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author yaroslav
 */
public class HVV_PollerSettings {
    static Logger logger = Logger.getLogger( HVV_PollerSettings.class);
    
    private String m_strVacuumPartHost;
    public String GetVacuumPartHost() { return m_strVacuumPartHost;}
    
    private int m_nVacuumPartPort;
    public int GetVacuumPartPort() { return m_nVacuumPartPort;}
    
    private String m_strHvPartHost;
    public String GetHvPartHost() { return m_strHvPartHost;}
    
    private int m_nHvPartPort;
    public int GetHvPartPort() { return m_nHvPartPort;}
    
    private int m_nExecutorPartPort;
    public int GetExecutorPartPort() { return m_nExecutorPartPort;}
    
    private int m_nAdminPartPort;
    public int GetAdminPartPort() { return m_nAdminPartPort;}
    
    private int m_nTimeZoneShift;
    public int GetTimeZoneShift() { return m_nTimeZoneShift;}
    
    private int m_nSingleInstanceSocketServerPort;
    public int GetSingleInstanceSocketServerPort() { return m_nSingleInstanceSocketServerPort;}
    
    private int m_nArcViewerSingleInstanceSocketServerPort;
    public int GetArcViewerSingleInstanceSocketServerPort() { return m_nArcViewerSingleInstanceSocketServerPort;}
    
    public HVV_PollerSettings( String strAMSRoot) {
        //EMULATOR
        m_strVacuumPartHost = "localhost";
        m_nVacuumPartPort = 1777;
        
        //REAL
        //m_strVacuumPartHost = "192.168.1.1";
        //m_nVacuumPartPort = 6340;
        
        
        m_strHvPartHost = "localhost";
        m_nHvPartPort = 6341;
        
        m_nExecutorPartPort = 6342;
        
        m_nAdminPartPort = 6343;
        
        m_nTimeZoneShift = 0;
        
        m_nSingleInstanceSocketServerPort = 10000;
        m_nArcViewerSingleInstanceSocketServerPort = 10005;
        
        ReadSettings();
    }
    
    private boolean ReadSettings() {
        boolean bResOk = true;
        try {
            SAXReader reader = new SAXReader();
            
            String strSettingsFilePathName = System.getenv( "AMS_ROOT") + "/etc/settings.poller.xml";
            URL url = ( new java.io.File( strSettingsFilePathName)).toURI().toURL();
            
            Document document = reader.read( url);
            
            Element root = document.getRootElement();

            // iterate through child elements of root
            for ( Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element element = ( Element) i.next();
                String name = element.getName();
                String value = element.getText();
                
                //logger.debug( "Pairs: [" + name + " : " + value + "]");
                
                if( "vacuum.host".equals( name)) m_strVacuumPartHost = value;
                if( "vacuum.port".equals( name)) m_nVacuumPartPort = Integer.parseInt( value);
                
                if( "hv.host".equals( name)) m_strHvPartHost = value;
                if( "hv.port".equals( name)) m_nHvPartPort = Integer.parseInt( value);
                
                if( "executor.port".equals( name)) m_nExecutorPartPort = Integer.parseInt( value);
                
                if( "admin.port".equals( name)) m_nAdminPartPort = Integer.parseInt( value);
                
                if( "timezone".equals( name)) m_nTimeZoneShift = Integer.parseInt( value);
                
                if( "singleInstancePort.ArcViewer".equals( name)) m_nArcViewerSingleInstanceSocketServerPort = Integer.parseInt( value);
                if( "singleInstancePort.Poller".equals( name)) m_nSingleInstanceSocketServerPort = Integer.parseInt( value);
                
            }
            
        } catch( MalformedURLException ex) {
            logger.error( "MalformedURLException caught while loading settings!", ex);
            bResOk = false;
        } catch( DocumentException ex) {
            logger.error( "DocumentException caught while loading settings!", ex);
            bResOk = false;
        }
        
        return bResOk;
    }
}
