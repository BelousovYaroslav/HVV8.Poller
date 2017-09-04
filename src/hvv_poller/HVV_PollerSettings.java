/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

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
    
    //RW SECTION
    private int m_nGraphLayout;
    public int GetGraphLayout() { return m_nGraphLayout;}
    public void SetGraphLayout( int nLayout) { m_nGraphLayout = nLayout;}
    
    private String m_strGraph1Dev;
    public String GetGraph1Dev() { return m_strGraph1Dev;}
    public void SetGraph1Dev( String strDev) { m_strGraph1Dev = strDev;}
    
    private String m_strGraph1DevParam;
    public String GetGraph1DevParam() { return m_strGraph1DevParam;}
    public void SetGraph1DevParam( String strDevParam) { m_strGraph1DevParam = strDevParam;}
    
    
    private String m_strGraph2Dev;
    public String GetGraph2Dev() { return m_strGraph2Dev;}
    public void SetGraph2Dev( String strDev) { m_strGraph2Dev = strDev;}
    
    private String m_strGraph2DevParam;
    public String GetGraph2DevParam() { return m_strGraph2DevParam;}
    public void SetGraph2DevParam( String strDevParam) { m_strGraph2DevParam = strDevParam;}
    
    
    private String m_strGraph3Dev;
    public String GetGraph3Dev() { return m_strGraph3Dev;}
    public void SetGraph3Dev( String strDev) { m_strGraph3Dev = strDev;}
    
    private String m_strGraph3DevParam;
    public String GetGraph3DevParam() { return m_strGraph3DevParam;}
    public void SetGraph3DevParam( String strDevParam) { m_strGraph3DevParam = strDevParam;}
    
    
    private String m_strGraph4Dev;
    public String GetGraph4Dev() { return m_strGraph4Dev;}
    public void SetGraph4Dev( String strDev) { m_strGraph4Dev = strDev;}
    
    private String m_strGraph4DevParam;
    public String GetGraph4DevParam() { return m_strGraph4DevParam;}
    public void SetGraph4DevParam( String strDevParam) { m_strGraph4DevParam = strDevParam;}
    
    
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
        
        m_nGraphLayout = 0;
        
        m_strGraph1Dev = "002"; m_strGraph1Dev = "01";
        m_strGraph2Dev = "002"; m_strGraph2Dev = "01";
        m_strGraph3Dev = "002"; m_strGraph3Dev = "01";
        m_strGraph4Dev = "002"; m_strGraph4Dev = "01";
        
        ReadSettings();
    }
    
    private boolean ReadSettings() {
        boolean bResOk = true;
        try {
            SAXReader reader = new SAXReader();
            
            String strSettingsFilePathName = System.getenv( "AMS_ROOT") + "/etc/settings.poller.r.xml";
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
        
        
        if( bResOk) {
            try {
                SAXReader reader = new SAXReader();

                String strSettingsFilePathName = System.getenv( "AMS_ROOT") + "/etc/settings.poller.rw.xml";
                URL url = ( new java.io.File( strSettingsFilePathName)).toURI().toURL();

                Document document = reader.read( url);

                Element root = document.getRootElement();

                // iterate through child elements of root
                for ( Iterator i = root.elementIterator(); i.hasNext(); ) {
                    Element element = ( Element) i.next();
                    String name = element.getName();
                    String value = element.getText();

                    //logger.debug( "Pairs: [" + name + " : " + value + "]");

                    if( "nGraphLayout".equals( name)) m_nGraphLayout = Integer.parseInt( value);

                    if( "strGraph1Dev".equals( name)) m_strGraph1Dev = value;
                    if( "strGraph1DevParam".equals( name)) m_strGraph1DevParam = value;
                    
                    if( "strGraph2Dev".equals( name)) m_strGraph2Dev = value;
                    if( "strGraph2DevParam".equals( name)) m_strGraph2DevParam = value;
                    
                    if( "strGraph3Dev".equals( name)) m_strGraph3Dev = value;
                    if( "strGraph3DevParam".equals( name)) m_strGraph3DevParam = value;
                    
                    if( "strGraph4Dev".equals( name)) m_strGraph4Dev = value;
                    if( "strGraph4DevParam".equals( name)) m_strGraph4DevParam = value;

                }

            } catch( MalformedURLException ex) {
                logger.error( "MalformedURLException caught while loading settings!", ex);
                bResOk = false;
            } catch( DocumentException ex) {
                logger.error( "DocumentException caught while loading settings!", ex);
                bResOk = false;
            }
        }
        return bResOk;
    }
    
    /**
     * Функция сохранения настроек в .xml файл
     */
    public void SaveSettings() {
        try {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement( "Settings" );
            
            
            root.addElement( "nGraphLayout").addText( "" + m_nGraphLayout);
            
            root.addElement( "strGraph1Dev").addText( m_strGraph1Dev);
            root.addElement( "strGraph1DevParam").addText( m_strGraph1DevParam);
            
            root.addElement( "strGraph2Dev").addText( m_strGraph2Dev);
            root.addElement( "strGraph2DevParam").addText( m_strGraph2DevParam);
            
            root.addElement( "strGraph3Dev").addText( m_strGraph3Dev);
            root.addElement( "strGraph3DevParam").addText( m_strGraph3DevParam);
            
            root.addElement( "strGraph4Dev").addText( m_strGraph4Dev);
            root.addElement( "strGraph4DevParam").addText( m_strGraph4DevParam);
            
            OutputFormat format = OutputFormat.createPrettyPrint();
            
            //TODO
            String strSettingsFilePathName = System.getenv( "AMS_ROOT") + "/etc/settings.poller.rw.xml";
            
            XMLWriter writer = new XMLWriter( new FileWriter( strSettingsFilePathName), format);
            
            writer.write( document );
            writer.close();
        } catch (IOException ex) {
            logger.error( "IOException caught while saving settings!", ex);
        }
    }
}
