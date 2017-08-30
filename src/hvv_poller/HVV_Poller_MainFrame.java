/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller;

import hvv_devices.HVV_HvDevice;
import hvv_devices.HVV_HvDevices;
import hvv_devices.HVV_VacuumDevice;
import hvv_devices.HVV_VacuumDevices;
import org.apache.log4j.Logger;
import hvv_poller.storage.HVV_LogsRepacker;
import hvv_poller.storage.HVV_StorageRepacker;
import javax.swing.DefaultComboBoxModel;
import org.apache.log4j.Level;

/**
 *
 * @author yaroslav
 */
public class HVV_Poller_MainFrame extends javax.swing.JFrame {

    static Logger logger = Logger.getLogger( HVV_Poller_MainFrame.class);
    
    private final HVV_Poller theApp;
    private final LedsControlThread m_LedsThread;

    PanelGraph m_panelGraph1;
    PanelGraph m_panelGraph2;
    PanelGraph m_panelGraph3;
    PanelGraph m_panelGraph4;
    
    //private final HVV_StorageRepacker m_StorageRepacker;
    
    //private final HVV_LogsRepacker m_LogsStorageRepacker;
    
    final private DefaultComboBoxModel cmbObjectModelG1;
    final private DefaultComboBoxModel cmbObjectModelG2;
    final private DefaultComboBoxModel cmbObjectModelG3;
    final private DefaultComboBoxModel cmbObjectModelG4;
    
    public String strGraph1Device, strGraph1DeviceParam;
    public String strGraph2Device, strGraph2DeviceParam;
    public String strGraph3Device, strGraph3DeviceParam;
    public String strGraph4Device, strGraph4DeviceParam;
    
    
    /**
     * Creates new form HVV_Poller_MainFrame
     */
    public HVV_Poller_MainFrame( HVV_Poller app) {
        initComponents();
        
        setTitle( "Модуль опроса и накопления данных, v.1.0.0.0, (2017.07.06 16:00)  (C) ФЛАВТ 2017.");
        theApp = app;
        
        btnLayout1x1.setText( ""); btnLayout1x1.setIcon( theApp.GetResources().getIconLayout1x1());
        btnLayout1x2.setText( ""); btnLayout1x2.setIcon( theApp.GetResources().getIconLayout1x2());
        btnLayout2x1.setText( ""); btnLayout2x1.setIcon( theApp.GetResources().getIconLayout2x1());
        btnLayout2x2.setText( ""); btnLayout2x2.setIcon( theApp.GetResources().getIconLayout2x2());
        
        cmbObjectModelG1 = new DefaultComboBoxModel();
        HVV_VacuumDevices.getInstance().fillComboGraph( cmbObjectModelG1, true);
        HVV_HvDevices.getInstance().fillComboGraph( cmbObjectModelG1, false);
        cmbGraph1.setModel( cmbObjectModelG1);
        
        cmbObjectModelG2 = new DefaultComboBoxModel();
        HVV_VacuumDevices.getInstance().fillComboGraph( cmbObjectModelG2, true);
        HVV_HvDevices.getInstance().fillComboGraph( cmbObjectModelG2, false);
        cmbGraph2.setModel( cmbObjectModelG2);
        
        cmbObjectModelG3 = new DefaultComboBoxModel();
        HVV_VacuumDevices.getInstance().fillComboGraph( cmbObjectModelG3, true);
        HVV_HvDevices.getInstance().fillComboGraph( cmbObjectModelG3, false);
        cmbGraph3.setModel( cmbObjectModelG3);
        
        cmbObjectModelG4 = new DefaultComboBoxModel();
        HVV_VacuumDevices.getInstance().fillComboGraph( cmbObjectModelG4, true);
        HVV_HvDevices.getInstance().fillComboGraph( cmbObjectModelG4, false);
        cmbGraph4.setModel( cmbObjectModelG4);
                
        m_LedsThread = new LedsControlThread( app);
        m_LedsThread.start();
        
        //m_StorageRepacker = new HVV_StorageRepacker( theApp);
        //m_StorageRepacker.StorageRepackStart();
        
        //m_LogsStorageRepacker = new HVV_LogsRepacker( theApp);
        //m_LogsStorageRepacker.LogsRepackStart();
        
        if(      Logger.getRootLogger().getLevel() == org.apache.log4j.Level.TRACE) btnTogTrace.setSelected( true);
        else if( Logger.getRootLogger().getLevel() == org.apache.log4j.Level.DEBUG) btnTogDebug.setSelected( true);
        else if( Logger.getRootLogger().getLevel() == org.apache.log4j.Level.INFO)  btnTogInfo.setSelected( true);
        else if( Logger.getRootLogger().getLevel() == org.apache.log4j.Level.WARN)  btnTogWarn.setSelected( true);
        else if( Logger.getRootLogger().getLevel() == org.apache.log4j.Level.ERROR) btnTogError.setSelected( true);
        else btnTogFatal.setSelected( true);
        
        m_panelGraph1 = new PanelGraph( app, app.m_serie1);
        pnlGraph1.add( m_panelGraph1);
        m_panelGraph1.setVisible( true);
        m_panelGraph1.setBoundsO( 0, 0, 530, 250);
        
        m_panelGraph2 = new PanelGraph( app, app.m_serie2);
        pnlGraph2.add( m_panelGraph2);
        m_panelGraph2.setVisible( true);
        m_panelGraph2.setBoundsO( 0, 0, 530, 250);
        
        m_panelGraph3 = new PanelGraph( app, app.m_serie3);
        pnlGraph3.add( m_panelGraph3);
        m_panelGraph3.setVisible( true);
        m_panelGraph3.setBoundsO( 0, 0, 530, 250);
        
        m_panelGraph4 = new PanelGraph( app, app.m_serie4);
        pnlGraph4.add( m_panelGraph4);
        m_panelGraph4.setVisible( true);
        m_panelGraph4.setBoundsO( 0, 0, 530, 250);
        
        cmbGraph1ActionPerformed( null);
        cmbGraph2ActionPerformed( null);
        cmbGraph3ActionPerformed( null);
        cmbGraph4ActionPerformed( null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupLogLevel = new javax.swing.ButtonGroup();
        lblLedVac = new javax.swing.JLabel();
        lblTitleVac = new javax.swing.JLabel();
        lblLastActionVac = new javax.swing.JLabel();
        btnVacuumStop = new javax.swing.JButton();
        btnVacuumRun = new javax.swing.JButton();
        lblLedHv = new javax.swing.JLabel();
        lblTitleHv = new javax.swing.JLabel();
        lblLastActionHv = new javax.swing.JLabel();
        btnHvStop = new javax.swing.JButton();
        btnHvRun = new javax.swing.JButton();
        lblLedExecutor = new javax.swing.JLabel();
        lblTitleExecutor = new javax.swing.JLabel();
        lblLastActionExecutor = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        lblLedAdmin = new javax.swing.JLabel();
        lblTitleAdmin = new javax.swing.JLabel();
        lblLastActionAdmin = new javax.swing.JLabel();
        cmbGraph1 = new javax.swing.JComboBox();
        pnlGraph1 = new javax.swing.JPanel();
        cmbGraph2 = new javax.swing.JComboBox();
        pnlGraph2 = new javax.swing.JPanel();
        cmbGraph3 = new javax.swing.JComboBox();
        pnlGraph3 = new javax.swing.JPanel();
        cmbGraph4 = new javax.swing.JComboBox();
        pnlGraph4 = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        btnTogTrace = new javax.swing.JToggleButton();
        btnTogDebug = new javax.swing.JToggleButton();
        btnTogInfo = new javax.swing.JToggleButton();
        btnTogWarn = new javax.swing.JToggleButton();
        btnTogFatal = new javax.swing.JToggleButton();
        btnTogError = new javax.swing.JToggleButton();
        btnLayout1x1 = new javax.swing.JButton();
        btnLayout1x2 = new javax.swing.JButton();
        btnLayout2x1 = new javax.swing.JButton();
        btnLayout2x2 = new javax.swing.JButton();
        btnLayout0 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1100, 800));
        setMinimumSize(new java.awt.Dimension(1100, 200));
        setPreferredSize(new java.awt.Dimension(1100, 800));
        setResizable(false);
        getContentPane().setLayout(null);

        lblLedVac.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLedVac.setText("LED");
        lblLedVac.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lblLedVac);
        lblLedVac.setBounds(10, 10, 50, 30);

        lblTitleVac.setText("Связь с модулем управления вакуумной частью");
        getContentPane().add(lblTitleVac);
        lblTitleVac.setBounds(70, 10, 370, 30);

        lblLastActionVac.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLastActionVac.setText("--.--.-- --:--:--");
        getContentPane().add(lblLastActionVac);
        lblLastActionVac.setBounds(450, 10, 160, 30);

        btnVacuumStop.setText("Стоп");
        btnVacuumStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVacuumStopActionPerformed(evt);
            }
        });
        getContentPane().add(btnVacuumStop);
        btnVacuumStop.setBounds(610, 10, 90, 40);

        btnVacuumRun.setText("Запуск");
        btnVacuumRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVacuumRunActionPerformed(evt);
            }
        });
        getContentPane().add(btnVacuumRun);
        btnVacuumRun.setBounds(700, 10, 90, 40);

        lblLedHv.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLedHv.setText("LED");
        lblLedHv.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lblLedHv);
        lblLedHv.setBounds(10, 50, 50, 30);

        lblTitleHv.setText("Связь с модулем управления высоковольтной частью");
        getContentPane().add(lblTitleHv);
        lblTitleHv.setBounds(70, 50, 370, 30);

        lblLastActionHv.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLastActionHv.setText("--.--.-- --:--:--");
        getContentPane().add(lblLastActionHv);
        lblLastActionHv.setBounds(450, 50, 160, 30);

        btnHvStop.setText("Стоп");
        btnHvStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHvStopActionPerformed(evt);
            }
        });
        getContentPane().add(btnHvStop);
        btnHvStop.setBounds(610, 50, 90, 40);

        btnHvRun.setText("Запуск");
        btnHvRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHvRunActionPerformed(evt);
            }
        });
        getContentPane().add(btnHvRun);
        btnHvRun.setBounds(700, 50, 90, 40);

        lblLedExecutor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLedExecutor.setText("LED");
        lblLedExecutor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lblLedExecutor);
        lblLedExecutor.setBounds(10, 90, 50, 30);

        lblTitleExecutor.setText("Связь с модулем исполнения программ автоматизации");
        getContentPane().add(lblTitleExecutor);
        lblTitleExecutor.setBounds(70, 90, 370, 30);

        lblLastActionExecutor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLastActionExecutor.setText("--.--.-- --:--:--");
        getContentPane().add(lblLastActionExecutor);
        lblLastActionExecutor.setBounds(450, 90, 160, 30);

        jButton1.setText("Выход");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(610, 90, 180, 40);

        lblLedAdmin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLedAdmin.setText("LED");
        lblLedAdmin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lblLedAdmin);
        lblLedAdmin.setBounds(10, 130, 50, 30);

        lblTitleAdmin.setText("Связь с административным модулем");
        getContentPane().add(lblTitleAdmin);
        lblTitleAdmin.setBounds(70, 130, 370, 30);

        lblLastActionAdmin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLastActionAdmin.setText("--.--.-- --:--:--");
        getContentPane().add(lblLastActionAdmin);
        lblLastActionAdmin.setBounds(450, 130, 160, 30);

        cmbGraph1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbGraph1.setMaximumSize(new java.awt.Dimension(530, 25));
        cmbGraph1.setMinimumSize(new java.awt.Dimension(530, 25));
        cmbGraph1.setPreferredSize(new java.awt.Dimension(530, 25));
        cmbGraph1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGraph1ActionPerformed(evt);
            }
        });
        getContentPane().add(cmbGraph1);
        cmbGraph1.setBounds(10, 200, 530, 25);

        pnlGraph1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlGraph1.setMaximumSize(new java.awt.Dimension(530, 380));
        pnlGraph1.setMinimumSize(new java.awt.Dimension(530, 380));

        javax.swing.GroupLayout pnlGraph1Layout = new javax.swing.GroupLayout(pnlGraph1);
        pnlGraph1.setLayout(pnlGraph1Layout);
        pnlGraph1Layout.setHorizontalGroup(
            pnlGraph1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 526, Short.MAX_VALUE)
        );
        pnlGraph1Layout.setVerticalGroup(
            pnlGraph1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 376, Short.MAX_VALUE)
        );

        getContentPane().add(pnlGraph1);
        pnlGraph1.setBounds(10, 225, 530, 250);

        cmbGraph2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbGraph2.setMaximumSize(new java.awt.Dimension(530, 25));
        cmbGraph2.setMinimumSize(new java.awt.Dimension(530, 25));
        cmbGraph2.setPreferredSize(new java.awt.Dimension(530, 25));
        cmbGraph2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGraph2ActionPerformed(evt);
            }
        });
        getContentPane().add(cmbGraph2);
        cmbGraph2.setBounds(560, 200, 530, 25);

        pnlGraph2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlGraph2.setMaximumSize(new java.awt.Dimension(530, 260));
        pnlGraph2.setMinimumSize(new java.awt.Dimension(530, 260));
        pnlGraph2.setPreferredSize(new java.awt.Dimension(530, 260));

        javax.swing.GroupLayout pnlGraph2Layout = new javax.swing.GroupLayout(pnlGraph2);
        pnlGraph2.setLayout(pnlGraph2Layout);
        pnlGraph2Layout.setHorizontalGroup(
            pnlGraph2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 526, Short.MAX_VALUE)
        );
        pnlGraph2Layout.setVerticalGroup(
            pnlGraph2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
        );

        getContentPane().add(pnlGraph2);
        pnlGraph2.setBounds(560, 225, 530, 250);

        cmbGraph3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbGraph3.setMaximumSize(new java.awt.Dimension(530, 25));
        cmbGraph3.setMinimumSize(new java.awt.Dimension(530, 25));
        cmbGraph3.setPreferredSize(new java.awt.Dimension(530, 25));
        cmbGraph3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGraph3ActionPerformed(evt);
            }
        });
        getContentPane().add(cmbGraph3);
        cmbGraph3.setBounds(10, 490, 530, 25);

        pnlGraph3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlGraph3.setMaximumSize(new java.awt.Dimension(530, 380));
        pnlGraph3.setMinimumSize(new java.awt.Dimension(530, 380));

        javax.swing.GroupLayout pnlGraph3Layout = new javax.swing.GroupLayout(pnlGraph3);
        pnlGraph3.setLayout(pnlGraph3Layout);
        pnlGraph3Layout.setHorizontalGroup(
            pnlGraph3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 526, Short.MAX_VALUE)
        );
        pnlGraph3Layout.setVerticalGroup(
            pnlGraph3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 376, Short.MAX_VALUE)
        );

        getContentPane().add(pnlGraph3);
        pnlGraph3.setBounds(10, 515, 530, 250);

        cmbGraph4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbGraph4.setMaximumSize(new java.awt.Dimension(530, 25));
        cmbGraph4.setMinimumSize(new java.awt.Dimension(530, 25));
        cmbGraph4.setPreferredSize(new java.awt.Dimension(530, 25));
        cmbGraph4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGraph4ActionPerformed(evt);
            }
        });
        getContentPane().add(cmbGraph4);
        cmbGraph4.setBounds(560, 490, 530, 25);

        pnlGraph4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlGraph4.setMaximumSize(new java.awt.Dimension(530, 260));
        pnlGraph4.setMinimumSize(new java.awt.Dimension(530, 260));

        javax.swing.GroupLayout pnlGraph4Layout = new javax.swing.GroupLayout(pnlGraph4);
        pnlGraph4.setLayout(pnlGraph4Layout);
        pnlGraph4Layout.setHorizontalGroup(
            pnlGraph4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 526, Short.MAX_VALUE)
        );
        pnlGraph4Layout.setVerticalGroup(
            pnlGraph4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
        );

        getContentPane().add(pnlGraph4);
        pnlGraph4.setBounds(560, 515, 530, 250);

        btnExit.setText("Выход");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        getContentPane().add(btnExit);
        btnExit.setBounds(840, 10, 240, 40);

        btnGroupLogLevel.add(btnTogTrace);
        btnTogTrace.setText("T");
        btnTogTrace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogTraceActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogTrace);
        btnTogTrace.setBounds(840, 70, 40, 25);

        btnGroupLogLevel.add(btnTogDebug);
        btnTogDebug.setText("D");
        btnTogDebug.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogDebugActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogDebug);
        btnTogDebug.setBounds(880, 70, 40, 25);

        btnGroupLogLevel.add(btnTogInfo);
        btnTogInfo.setText("I");
        btnTogInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogInfoActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogInfo);
        btnTogInfo.setBounds(920, 70, 40, 25);

        btnGroupLogLevel.add(btnTogWarn);
        btnTogWarn.setText("W");
        btnTogWarn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogWarnActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogWarn);
        btnTogWarn.setBounds(960, 70, 40, 25);

        btnGroupLogLevel.add(btnTogFatal);
        btnTogFatal.setText("F");
        btnTogFatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogFatalActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogFatal);
        btnTogFatal.setBounds(1040, 70, 40, 25);

        btnGroupLogLevel.add(btnTogError);
        btnTogError.setText("E");
        btnTogError.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogErrorActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogError);
        btnTogError.setBounds(1000, 70, 40, 25);

        btnLayout1x1.setText("1x1");
        btnLayout1x1.setMaximumSize(new java.awt.Dimension(50, 40));
        btnLayout1x1.setMinimumSize(new java.awt.Dimension(50, 40));
        btnLayout1x1.setPreferredSize(new java.awt.Dimension(50, 40));
        btnLayout1x1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLayout1x1ActionPerformed(evt);
            }
        });
        getContentPane().add(btnLayout1x1);
        btnLayout1x1.setBounds(880, 120, 50, 40);

        btnLayout1x2.setText("1x2");
        btnLayout1x2.setMaximumSize(new java.awt.Dimension(50, 40));
        btnLayout1x2.setMinimumSize(new java.awt.Dimension(50, 40));
        btnLayout1x2.setPreferredSize(new java.awt.Dimension(50, 40));
        btnLayout1x2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLayout1x2ActionPerformed(evt);
            }
        });
        getContentPane().add(btnLayout1x2);
        btnLayout1x2.setBounds(930, 120, 50, 40);

        btnLayout2x1.setText("2x1");
        btnLayout2x1.setMaximumSize(new java.awt.Dimension(50, 40));
        btnLayout2x1.setMinimumSize(new java.awt.Dimension(50, 40));
        btnLayout2x1.setPreferredSize(new java.awt.Dimension(50, 40));
        btnLayout2x1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLayout2x1ActionPerformed(evt);
            }
        });
        getContentPane().add(btnLayout2x1);
        btnLayout2x1.setBounds(980, 120, 50, 40);

        btnLayout2x2.setText("2x2");
        btnLayout2x2.setMaximumSize(new java.awt.Dimension(50, 40));
        btnLayout2x2.setMinimumSize(new java.awt.Dimension(50, 40));
        btnLayout2x2.setPreferredSize(new java.awt.Dimension(50, 40));
        btnLayout2x2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLayout2x2ActionPerformed(evt);
            }
        });
        getContentPane().add(btnLayout2x2);
        btnLayout2x2.setBounds(1030, 120, 50, 40);

        btnLayout0.setText("0");
        btnLayout0.setMaximumSize(new java.awt.Dimension(50, 40));
        btnLayout0.setMinimumSize(new java.awt.Dimension(50, 40));
        btnLayout0.setPreferredSize(new java.awt.Dimension(50, 40));
        btnLayout0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLayout0ActionPerformed(evt);
            }
        });
        getContentPane().add(btnLayout0);
        btnLayout0.setBounds(830, 120, 50, 40);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        btnExit.setVisible(false);
        theApp.GetPollerVacThread().stop();
        theApp.GetPollerHvThread().stop();
        theApp.GetStorageThread().StopThread();
        theApp.GetCommE2P().stop();
        theApp.GetCommA2P().stop();
        m_LedsThread.stop();
        
        //m_StorageRepacker.StorageRepackStop();
        //m_LogsStorageRepacker.LogsRepackStop();
        
        dispose();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnVacuumRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVacuumRunActionPerformed
        if( theApp.GetPollerVacThread().m_Thread.isAlive() == false) {
            theApp.GetPollerVacThread().start();
        }
    }//GEN-LAST:event_btnVacuumRunActionPerformed

    private void btnHvRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHvRunActionPerformed
        if( theApp.GetPollerHvThread().m_Thread.isAlive() == false) {
            theApp.GetPollerHvThread().start();
        }
    }//GEN-LAST:event_btnHvRunActionPerformed

    private void btnVacuumStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVacuumStopActionPerformed
        if( theApp.GetPollerVacThread().m_Thread.isAlive() == true) {
            theApp.GetPollerVacThread().stop();
        }
    }//GEN-LAST:event_btnVacuumStopActionPerformed

    private void btnHvStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHvStopActionPerformed
        if( theApp.GetPollerHvThread().m_Thread.isAlive() == true) {
            theApp.GetPollerHvThread().stop();
        }
    }//GEN-LAST:event_btnHvStopActionPerformed

    private void btnTogTraceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTogTraceActionPerformed
        if( Logger.getRootLogger().getLevel() != Level.TRACE) {
            logger.info( "Switching log level to TRACE");
            Logger.getRootLogger().setLevel( Level.TRACE);
        }
    }//GEN-LAST:event_btnTogTraceActionPerformed

    private void btnTogDebugActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTogDebugActionPerformed
        if( Logger.getRootLogger().getLevel() != Level.DEBUG) {
            logger.info( "Switching log level to DEBUG");
            Logger.getRootLogger().setLevel( Level.DEBUG);
        }
    }//GEN-LAST:event_btnTogDebugActionPerformed

    private void btnTogInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTogInfoActionPerformed
        if( Logger.getRootLogger().getLevel() != Level.INFO) {
            logger.info( "Switching log level to INFO");
            Logger.getRootLogger().setLevel( Level.INFO);
        }
    }//GEN-LAST:event_btnTogInfoActionPerformed

    private void btnTogWarnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTogWarnActionPerformed
        if( Logger.getRootLogger().getLevel() != Level.WARN) {
            logger.info( "Switching log level to WARN");
            Logger.getRootLogger().setLevel( Level.WARN);
        }
    }//GEN-LAST:event_btnTogWarnActionPerformed

    private void btnTogFatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTogFatalActionPerformed
        if( Logger.getRootLogger().getLevel() != Level.FATAL) {
            logger.info( "Switching log level to FATAL");
            Logger.getRootLogger().setLevel( Level.FATAL);
        }
    }//GEN-LAST:event_btnTogFatalActionPerformed

    private void btnTogErrorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTogErrorActionPerformed
        if( Logger.getRootLogger().getLevel() != Level.ERROR) {
            logger.info( "Switching log level to ERROR");
            Logger.getRootLogger().setLevel( Level.ERROR);
        }
    }//GEN-LAST:event_btnTogErrorActionPerformed

    private void btnLayout1x1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLayout1x1ActionPerformed
        setSize( 1100, 800);
        
        cmbGraph1.setBounds( 10, 200, 1080, 25);
        pnlGraph1.setBounds( 10, 225, 1080, 540);
        m_panelGraph1.setBoundsO( 0, 0, 1080, 540);

        cmbGraph2.setVisible( false);
        pnlGraph2.setVisible( false);
       
        cmbGraph3.setVisible( false);
        pnlGraph3.setVisible( false);
        
        cmbGraph4.setVisible( false);
        pnlGraph4.setVisible( false);
    }//GEN-LAST:event_btnLayout1x1ActionPerformed

    private void btnLayout1x2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLayout1x2ActionPerformed
        //=
        
        setSize( 1100, 800);
        
        cmbGraph1.setBounds( 10, 200, 1080, 25);
        pnlGraph1.setBounds( 10, 225, 1080, 250);
        m_panelGraph1.setBoundsO( 0, 0, 1080, 250);
        
        cmbGraph2.setVisible( false);
        pnlGraph2.setVisible( false);
        
        cmbGraph3.setVisible( true);
        cmbGraph3.setBounds( 10, 490, 1080, 25);
        pnlGraph3.setVisible( true);
        pnlGraph3.setBounds( 10, 515, 1080, 250);
        m_panelGraph3.setBoundsO( 0, 0, 1080, 250);
        
        cmbGraph4.setVisible( false);
        pnlGraph4.setVisible( false);
    }//GEN-LAST:event_btnLayout1x2ActionPerformed

    private void btnLayout2x1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLayout2x1ActionPerformed
        //||
        
        setSize( 1100, 800);
        
        cmbGraph1.setBounds( 10, 200, 530, 25);
        pnlGraph1.setBounds( 10, 225, 530, 540);
        m_panelGraph1.setBoundsO( 0, 0, 530, 540);
        
        cmbGraph2.setVisible( true);
        cmbGraph2.setBounds( 560, 200, 530, 25);
        pnlGraph2.setVisible( true);
        pnlGraph2.setBounds( 560, 225, 530, 540);
        m_panelGraph2.setBoundsO( 0, 0, 530, 540);
        
        cmbGraph3.setVisible( false);
        pnlGraph3.setVisible( false);
        
        cmbGraph4.setVisible( false);
        pnlGraph4.setVisible( false);
    }//GEN-LAST:event_btnLayout2x1ActionPerformed

    private void btnLayout2x2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLayout2x2ActionPerformed
        setSize( 1100, 800);
        
        cmbGraph1.setBounds( 10, 200, 530, 25);
        pnlGraph1.setBounds( 10, 225, 530, 250);
        m_panelGraph1.setBoundsO( 0, 0, 530, 250);
        
        cmbGraph2.setVisible( true);
        cmbGraph2.setBounds( 560, 200, 530, 25);
        pnlGraph2.setVisible( true);
        pnlGraph2.setBounds( 560, 225, 530, 250);
        m_panelGraph2.setBoundsO( 0, 0, 530, 250);
        
        cmbGraph3.setVisible( true);
        cmbGraph3.setBounds( 10, 490, 530, 25);
        pnlGraph3.setVisible( true);
        pnlGraph3.setBounds( 10, 515, 530, 250);
        m_panelGraph3.setBoundsO( 0, 0, 530, 250);
        
        cmbGraph4.setVisible( true);
        cmbGraph4.setBounds( 560, 490, 530, 25);
        pnlGraph4.setVisible( true);
        pnlGraph4.setBounds( 560, 515, 530, 250);
        m_panelGraph4.setBoundsO( 0, 0, 530, 250);
    }//GEN-LAST:event_btnLayout2x2ActionPerformed

    private void btnLayout0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLayout0ActionPerformed
        
        setSize( 1100, 200);
    }//GEN-LAST:event_btnLayout0ActionPerformed

    private void cmbGraph1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGraph1ActionPerformed
        theApp.m_serie1.clear();
        
        int nIndex = cmbGraph1.getSelectedIndex();
        String strSelection = ( String) cmbGraph1.getModel().getElementAt(nIndex);
            
        String [] parts;
        parts = strSelection.split( "\\.");
        
        strGraph1Device = parts[0];
        
        String strAxisUnit = "";
        if( Character.isDigit( strGraph1Device.charAt(0))) {
            //VAC
            HVV_VacuumDevice dev = ( HVV_VacuumDevice) hvv_devices.HVV_VacuumDevices.getInstance().m_devices.get( strGraph1Device);
            if( dev != null) {
                String strDeviceParamIndex = parts[2];
                if( strSelection.charAt( 0) == '2') {
                    //ОСОБЫЙ СЛУЧАЙ - печки
                    strDeviceParamIndex = parts[3];
                }
                
                strAxisUnit = ( String) dev.m_mapParametersUnits.get( strDeviceParamIndex);
            }
        }
        else {
            //HV
            HVV_HvDevice dev = ( HVV_HvDevice) hvv_devices.HVV_HvDevices.getInstance().m_devices.get( strGraph1Device);
            if( dev != null) {
                String strDeviceParamIndex = parts[3];
                strAxisUnit = ( String) dev.m_mapParametersUnits.get( strDeviceParamIndex);
            }
        }
        m_panelGraph1.m_chart.getXYPlot().getRangeAxis().setLabel( strAxisUnit);
    }//GEN-LAST:event_cmbGraph1ActionPerformed

    private void cmbGraph2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGraph2ActionPerformed
        theApp.m_serie2.clear();
        
        int nIndex = cmbGraph2.getSelectedIndex();
        String strSelection = ( String) cmbGraph2.getModel().getElementAt(nIndex);
            
        String [] parts;
        parts = strSelection.split( "\\.");
        
        strGraph2Device = parts[0];
        
        String strAxisUnit = "";
        if( Character.isDigit( strGraph2Device.charAt(0))) {
            //VAC
            HVV_VacuumDevice dev = ( HVV_VacuumDevice) hvv_devices.HVV_VacuumDevices.getInstance().m_devices.get( strGraph2Device);
            if( dev != null) {
                String strDeviceParamIndex = parts[2];
                if( strSelection.charAt( 0) == '2') {
                    //ОСОБЫЙ СЛУЧАЙ - печки
                    strDeviceParamIndex = parts[3];
                }
                
                strAxisUnit = ( String) dev.m_mapParametersUnits.get( strDeviceParamIndex);
            }
        }
        else {
            //HV
            HVV_HvDevice dev = ( HVV_HvDevice) hvv_devices.HVV_HvDevices.getInstance().m_devices.get( strGraph2Device);
            if( dev != null) {
                String strDeviceParamIndex = parts[3];
                strAxisUnit = ( String) dev.m_mapParametersUnits.get( strDeviceParamIndex);
            }
        }
        m_panelGraph2.m_chart.getXYPlot().getRangeAxis().setLabel( strAxisUnit);
    }//GEN-LAST:event_cmbGraph2ActionPerformed

    private void cmbGraph3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGraph3ActionPerformed
        theApp.m_serie3.clear();
        
        int nIndex = cmbGraph3.getSelectedIndex();
        String strSelection = ( String) cmbGraph3.getModel().getElementAt(nIndex);
            
        String [] parts;
        parts = strSelection.split( "\\.");
        
        strGraph3Device = parts[0];
        
        String strAxisUnit = "";
        if( Character.isDigit( strGraph3Device.charAt(0))) {
            //VAC
            HVV_VacuumDevice dev = ( HVV_VacuumDevice) hvv_devices.HVV_VacuumDevices.getInstance().m_devices.get( strGraph3Device);
            if( dev != null) {
                String strDeviceParamIndex = parts[2];
                if( strSelection.charAt( 0) == '2') {
                    //ОСОБЫЙ СЛУЧАЙ - печки
                    strDeviceParamIndex = parts[3];
                }
                
                strAxisUnit = ( String) dev.m_mapParametersUnits.get( strDeviceParamIndex);
            }
        }
        else {
            //HV
            HVV_HvDevice dev = ( HVV_HvDevice) hvv_devices.HVV_HvDevices.getInstance().m_devices.get( strGraph3Device);
            if( dev != null) {
                String strDeviceParamIndex = parts[3];
                strAxisUnit = ( String) dev.m_mapParametersUnits.get( strDeviceParamIndex);
            }
        }
        m_panelGraph3.m_chart.getXYPlot().getRangeAxis().setLabel( strAxisUnit);
    }//GEN-LAST:event_cmbGraph3ActionPerformed

    private void cmbGraph4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGraph4ActionPerformed
        theApp.m_serie4.clear();
        
        int nIndex = cmbGraph4.getSelectedIndex();
        String strSelection = ( String) cmbGraph4.getModel().getElementAt(nIndex);
            
        String [] parts;
        parts = strSelection.split( "\\.");
        
        strGraph4Device = parts[0];
        
        String strAxisUnit = "";
        if( Character.isDigit( strGraph4Device.charAt(0))) {
            //VAC
            HVV_VacuumDevice dev = ( HVV_VacuumDevice) hvv_devices.HVV_VacuumDevices.getInstance().m_devices.get( strGraph4Device);
            if( dev != null) {
                String strDeviceParamIndex = parts[2];
                if( strSelection.charAt( 0) == '2') {
                    //ОСОБЫЙ СЛУЧАЙ - печки
                    strDeviceParamIndex = parts[3];
                }
                
                strAxisUnit = ( String) dev.m_mapParametersUnits.get( strDeviceParamIndex);
            }
        }
        else {
            //HV
            HVV_HvDevice dev = ( HVV_HvDevice) hvv_devices.HVV_HvDevices.getInstance().m_devices.get( strGraph4Device);
            if( dev != null) {
                String strDeviceParamIndex = parts[3];
                strAxisUnit = ( String) dev.m_mapParametersUnits.get( strDeviceParamIndex);
            }
        }
        m_panelGraph4.m_chart.getXYPlot().getRangeAxis().setLabel( strAxisUnit);
    }//GEN-LAST:event_cmbGraph4ActionPerformed

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(HVV_Poller_MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HVV_Poller_MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HVV_Poller_MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HVV_Poller_MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HVV_Poller_MainFrame( null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.ButtonGroup btnGroupLogLevel;
    private javax.swing.JButton btnHvRun;
    private javax.swing.JButton btnHvStop;
    private javax.swing.JButton btnLayout0;
    private javax.swing.JButton btnLayout1x1;
    private javax.swing.JButton btnLayout1x2;
    private javax.swing.JButton btnLayout2x1;
    private javax.swing.JButton btnLayout2x2;
    private javax.swing.JToggleButton btnTogDebug;
    private javax.swing.JToggleButton btnTogError;
    private javax.swing.JToggleButton btnTogFatal;
    private javax.swing.JToggleButton btnTogInfo;
    private javax.swing.JToggleButton btnTogTrace;
    private javax.swing.JToggleButton btnTogWarn;
    private javax.swing.JButton btnVacuumRun;
    private javax.swing.JButton btnVacuumStop;
    private javax.swing.JComboBox cmbGraph1;
    private javax.swing.JComboBox cmbGraph2;
    private javax.swing.JComboBox cmbGraph3;
    private javax.swing.JComboBox cmbGraph4;
    private javax.swing.JButton jButton1;
    public javax.swing.JLabel lblLastActionAdmin;
    public javax.swing.JLabel lblLastActionExecutor;
    public javax.swing.JLabel lblLastActionHv;
    public javax.swing.JLabel lblLastActionVac;
    public javax.swing.JLabel lblLedAdmin;
    public javax.swing.JLabel lblLedExecutor;
    public javax.swing.JLabel lblLedHv;
    public javax.swing.JLabel lblLedVac;
    private javax.swing.JLabel lblTitleAdmin;
    private javax.swing.JLabel lblTitleExecutor;
    private javax.swing.JLabel lblTitleHv;
    private javax.swing.JLabel lblTitleVac;
    private javax.swing.JPanel pnlGraph1;
    private javax.swing.JPanel pnlGraph2;
    private javax.swing.JPanel pnlGraph3;
    private javax.swing.JPanel pnlGraph4;
    // End of variables declaration//GEN-END:variables
}
