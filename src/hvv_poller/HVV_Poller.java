/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller;

import hvv_poller.admin.HVV_Commmunication_A2P;
import hvv_poller.admin.HVV_Poller_admin;
import hvv_poller.exec.HVV_Commmunication_E2P;
import hvv_poller.exec.HVV_Poller_exec;
import hvv_poller.hv.HVV_Poller_hv;
import hvv_poller.storage.HVV_StorageThread;
import hvv_poller.vac.HVV_Poller_vac;
import hvv_resources.HVV_Resources;
import java.io.File;
import java.net.ServerSocket;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;
import javax.swing.JOptionPane;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;

/**
 *
 * @author yaroslav
 */
public class HVV_Poller {

    public final TimeSeries m_serie1;
    public final TimeSeries m_serie2;
    public final TimeSeries m_serie3;
    public final TimeSeries m_serie4;
    
    private boolean m_bStartedSuccessfully;
    static Logger logger = Logger.getLogger( HVV_Poller.class);
    
    private final String m_strAMSrootEnvVar;
    public String GetAMSRoot() { return m_strAMSrootEnvVar; }
    
    public HVV_Poller_MainFrame m_pMainWnd;
    
    private HVV_Poller_vac m_pollerVac;
    public HVV_Poller_vac GetPollerVacThread() { return m_pollerVac; }
    
    private HVV_Poller_hv m_pollerHv;
    public HVV_Poller_hv GetPollerHvThread() { return m_pollerHv; }
    
    /*
    private HVV_Poller_exec m_pollerExec;
    public HVV_Poller_exec GetPollerExecThread() { return m_pollerExec; }
    */
    HVV_Commmunication_E2P m_comm_e2p;
    public HVV_Commmunication_E2P GetCommE2P() { return m_comm_e2p; }
    
    
    /*
    private HVV_Poller_admin m_pollerAdmin;
    public HVV_Poller_admin GetPollerAdminThread() { return m_pollerAdmin; }
    */
    HVV_Commmunication_A2P m_comm_a2p;
    public HVV_Commmunication_A2P GetCommA2P() { return m_comm_a2p; }
    
    
    
    private final HVV_Resources m_Resources;
    public HVV_Resources GetResources() { return m_Resources;}
    
    private final HVV_PollerSettings m_Settings;
    public HVV_PollerSettings GetSettings() { return m_Settings;}
    
    private HVV_StorageThread m_storageThread;
    public HVV_StorageThread GetStorageThread() { return m_storageThread; }
    
    private ServerSocket m_pSingleInstanceSocketServer;
    
    public final TreeMap m_mapHotValues;
    /**
     * @param args the command line arguments
     */

            
    public HVV_Poller() {
        m_bStartedSuccessfully = false;
        m_strAMSrootEnvVar = System.getenv( "AMS_ROOT");
        
        m_serie1 = new TimeSeries( "G1", Millisecond.class);
        m_serie2 = new TimeSeries( "G2", Millisecond.class);
        m_serie3 = new TimeSeries( "G3", Millisecond.class);
        m_serie4 = new TimeSeries( "G4", Millisecond.class);
    
        //SETTINGS
        m_Settings = new HVV_PollerSettings( m_strAMSrootEnvVar);
        
        //ПРОВЕРКА ОДНОВРЕМЕННОГО ЗАПУСКА ТОЛЬКО ОДНОЙ КОПИИ ПРОГРАММЫ
        try {
            m_pSingleInstanceSocketServer = new ServerSocket( GetSettings().GetSingleInstanceSocketServerPort());
        }
        catch( Exception ex) {
            MessageBoxError( "Модуль сбора данных уже запущен.\nПоищите на других \"экранах\".", "Модуль сбора данных");
            logger.error( "Не смогли открыть сокет для проверки запуска только одной копии программы! Программа уже запущена?", ex);
            m_pSingleInstanceSocketServer = null;
            m_Resources = null;
            m_mapHotValues = null;
            return;
        }
        
        //RESOURCES
        m_Resources = HVV_Resources.getInstance();
        
        m_mapHotValues = new TreeMap();
        
        //STORAGE THREAD
        try {
            m_storageThread = new HVV_StorageThread( this);
            m_storageThread.StartThread();
        } catch( Exception ex) {
            logger.fatal( "При создании объекта управления накоплением и хранением данных произошёл Exception!", ex);
            MessageBoxError( "При создании объекта управления накоплением и хранением данных произошёл Exception!", "HVV_Poller");
            m_storageThread = null;
            return;
        }
        
        
        //VACUUM
        try {
            m_pollerVac = new HVV_Poller_vac( this);
            m_pollerVac.start();
        } catch( Exception ex) {
            logger.fatal( "При создании объекта взаимодействия с ваккуумной частью поста произошёл Exception!", ex);
            MessageBoxError( "При создании объекта взаимодействия с ваккуумной частью поста произошёл Exception!", "HVV_Poller");
            m_pollerVac = null;
            return;
        }
        
        //HV
        try {
            m_pollerHv = new HVV_Poller_hv( this);
            m_pollerHv.start();
        } catch( Exception ex) {
            logger.fatal( "При создании объекта взаимодействия с высоковольтной частью поста произошёл Exception!", ex);
            MessageBoxError( "При создании объекта взаимодействия с высоковольтной частью поста произошёл Exception!", "HVV_Poller");
            m_pollerHv = null;
            return;
        }
        

        //COMMUNICATION: EXECUTOR -- (GET, PING, QUIT) --> POLLER
        m_comm_e2p = new HVV_Commmunication_E2P( m_Settings.GetExecutorPartPort(), this);
        m_comm_e2p.start();
        /*
        //EXECUTOR
        try {
            m_pollerExec = new HVV_Poller_exec(this);
            m_pollerExec.start();
        } catch( Exception ex) {
            logger.fatal( "При создании объекта взаимодействия с модулем исполнения программ автоматизаци произошёл Exception!", ex);
            MessageBoxError( "При создании объекта взаимодействия с модулем исполнения программ автоматизаци произошёл Exception!", "HVV_Poller");
            m_pollerExec = null;
            return;
        }
        */
        
        //COMMUNICATION: ADMIN -- (GET, PING, QUIT) --> POLLER
        m_comm_a2p = new HVV_Commmunication_A2P( m_Settings.GetAdminPartPort(), this);
        m_comm_a2p.start();
        /*
        //Admin
        try {
            m_pollerAdmin = new HVV_Poller_admin( this);
            m_pollerAdmin.start();
        } catch( Exception ex) {
            logger.fatal( "При создании объекта взаимодействия с административным модулем произошёл Exception!", ex);
            MessageBoxError( "При создании объекта взаимодействия с административным модулем произошёл Exception!", "HVV_Poller");
            m_pollerAdmin = null;
            return;
        }
        */
        
        m_bStartedSuccessfully = true;
           
    }
    
    public void start() {
        if( m_bStartedSuccessfully) {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger( HVV_Poller_MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger( HVV_Poller_MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger( HVV_Poller_MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger( HVV_Poller_MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
            //</editor-fold>
        
        
        
            //MAINFRAME
            m_pMainWnd = new HVV_Poller_MainFrame( this);
            java.awt.EventQueue.invokeLater( new Runnable() {
                public void run() {
                    m_pMainWnd.setVisible( true);
                }
            });
        }
        else {
            if( m_storageThread != null)
                m_storageThread.StopThread();
            
            if( m_pollerVac != null)
                m_pollerVac.stop();
            
            if( m_pollerHv != null)
                m_pollerHv.stop();
            
            if( m_comm_e2p != null)
                m_comm_e2p.stop();
            
            if( m_comm_a2p != null)
                m_comm_a2p.stop();
            
        }
    }
    
    public static void main(String[] args) {
        //BasicConfigurator.configure();
        //logger.setLevel( Level.DEBUG);
        
        //главная переменная окружения
        String strAMSrootEnvVar = System.getenv( "AMS_ROOT");
        if( strAMSrootEnvVar == null) {
            MessageBoxError( "Не задана переменная окружения AMS_ROOT!", "HVV_Poller");
            return;
        }
        
        //настройка логгера
        String strlog4jPropertiesFile = strAMSrootEnvVar + "/etc/log4j.poller.properties";
        File file = new File( strlog4jPropertiesFile);
        if(!file.exists())
            System.out.println("It is not possible to load the given log4j properties file :" + file.getAbsolutePath());
        else
            PropertyConfigurator.configure( file.getAbsolutePath());
        
        //запуск программы
        HVV_Poller appInstance = new HVV_Poller();
        if( appInstance.m_pSingleInstanceSocketServer != null) {
            logger.info( "HVV_Poller::main(): Start point!");
            appInstance.start();
        }
    }
    
    /**
     * Функция для сообщения пользователю информационного сообщения
     * @param strMessage сообщение
     * @param strTitleBar заголовок
     */
    public static void MessageBoxInfo( String strMessage, String strTitleBar)
    {
        JOptionPane.showMessageDialog( null, strMessage, strTitleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Функция для сообщения пользователю сообщения об ошибке
     * @param strMessage сообщение
     * @param strTitleBar заголовок
     */
    public static void MessageBoxError( String strMessage, String strTitleBar)
    {
        JOptionPane.showMessageDialog( null, strMessage, strTitleBar, JOptionPane.ERROR_MESSAGE);
    }
    
    public Date GetLocalDate() {
        Date dt = new Date( System.currentTimeMillis() - 1000 * 60 * 60 * GetSettings().GetTimeZoneShift());
        return dt;
    }
    
    public String NiceFormatDateTime( Date dt) {
        String strResult;
                
        GregorianCalendar clndr = new GregorianCalendar();
        clndr.setTime( dt);

        strResult = String.format( "%02d.%02d.%02d %02d:%02d:%02d",
                clndr.get(Calendar.DAY_OF_MONTH),
                clndr.get(Calendar.MONTH) + 1,
                clndr.get(Calendar.YEAR),
                clndr.get(Calendar.HOUR_OF_DAY),
                clndr.get(Calendar.MINUTE),
                clndr.get(Calendar.SECOND));
        
        return strResult;
    }
}
