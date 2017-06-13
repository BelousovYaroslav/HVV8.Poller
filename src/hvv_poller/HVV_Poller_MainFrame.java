/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller;

import org.apache.log4j.Logger;
import hvv_graph.HVV_Graph;
import hvv_poller.storage.HVV_LogsRepacker;
import hvv_poller.storage.HVV_StorageRepacker;
import static java.lang.Thread.sleep;
import org.apache.log4j.Level;

/**
 *
 * @author yaroslav
 */
public class HVV_Poller_MainFrame extends javax.swing.JFrame {

    static Logger logger = Logger.getLogger( HVV_Poller_MainFrame.class);
    
    private final HVV_Poller theApp;
    private final LedsControlThread m_LedsThread;
    public final HVV_Graph m_Graph;
    
    class GraphRefreshThread implements Runnable {
        public boolean m_bContinue;
        
        @Override
        public void run() {
            m_bContinue = true;
            do {
                if( m_Graph != null) {
                    m_Graph.repaint();
                }
                else
                    m_bContinue = false;
                
                try {
                    sleep( 1000);
                } catch (InterruptedException ex) {
                    logger.error( "Caught interrupted exception:", ex);
                }
            } while( m_bContinue);
        }
        
        public void start() {
            new Thread( this).start();
        }
    }
    
    private GraphRefreshThread m_graphRefreshThread;
    
    private final HVV_StorageRepacker m_StorageRepacker;
    
    private final HVV_LogsRepacker m_LogsStorageRepacker;
    
    /**
     * Creates new form HVV_Poller_MainFrame
     */
    public HVV_Poller_MainFrame( HVV_Poller app) {
        initComponents();
        setTitle( "Модуль опроса и накопления данных, v.1.0.0.0, (2016.09.09 13:50)  (C) ФЛАВТ 2016.");
        theApp = app;
        
        m_Graph = new HVV_Graph();
        pnlGraph.add( m_Graph);
        m_Graph.setBounds( 0, 0, 580, 260);
        //m_Graph.setVisible( true);
        
        m_graphRefreshThread = new GraphRefreshThread();
        m_graphRefreshThread.start();
        
        m_LedsThread = new LedsControlThread( app);
        m_LedsThread.start();
        
        m_StorageRepacker = new HVV_StorageRepacker( theApp);
        m_StorageRepacker.StorageRepackStart();
        
        m_LogsStorageRepacker = new HVV_LogsRepacker( theApp);
        m_LogsStorageRepacker.LogsRepackStart();
        
        if(      Logger.getRootLogger().getLevel() == org.apache.log4j.Level.TRACE) btnTogTrace.setSelected( true);
        else if( Logger.getRootLogger().getLevel() == org.apache.log4j.Level.DEBUG) btnTogDebug.setSelected( true);
        else if( Logger.getRootLogger().getLevel() == org.apache.log4j.Level.INFO)  btnTogInfo.setSelected( true);
        else if( Logger.getRootLogger().getLevel() == org.apache.log4j.Level.WARN)  btnTogWarn.setSelected( true);
        else if( Logger.getRootLogger().getLevel() == org.apache.log4j.Level.ERROR) btnTogError.setSelected( true);
        else btnTogFatal.setSelected( true);
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
        pnlGraph = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        btnTogTrace = new javax.swing.JToggleButton();
        btnTogDebug = new javax.swing.JToggleButton();
        btnTogInfo = new javax.swing.JToggleButton();
        btnTogWarn = new javax.swing.JToggleButton();
        btnTogFatal = new javax.swing.JToggleButton();
        btnTogError = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(800, 500));
        setMinimumSize(new java.awt.Dimension(800, 500));
        setPreferredSize(new java.awt.Dimension(800, 500));
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

        pnlGraph.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlGraph.setMaximumSize(new java.awt.Dimension(580, 260));
        pnlGraph.setMinimumSize(new java.awt.Dimension(580, 260));
        pnlGraph.setPreferredSize(new java.awt.Dimension(580, 260));

        javax.swing.GroupLayout pnlGraphLayout = new javax.swing.GroupLayout(pnlGraph);
        pnlGraph.setLayout(pnlGraphLayout);
        pnlGraphLayout.setHorizontalGroup(
            pnlGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 576, Short.MAX_VALUE)
        );
        pnlGraphLayout.setVerticalGroup(
            pnlGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
        );

        getContentPane().add(pnlGraph);
        pnlGraph.setBounds(10, 170, 580, 260);

        btnExit.setText("Выход");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        getContentPane().add(btnExit);
        btnExit.setBounds(610, 390, 180, 40);

        btnGroupLogLevel.add(btnTogTrace);
        btnTogTrace.setText("T");
        btnTogTrace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogTraceActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogTrace);
        btnTogTrace.setBounds(610, 170, 40, 28);

        btnGroupLogLevel.add(btnTogDebug);
        btnTogDebug.setText("D");
        btnTogDebug.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogDebugActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogDebug);
        btnTogDebug.setBounds(610, 200, 40, 28);

        btnGroupLogLevel.add(btnTogInfo);
        btnTogInfo.setText("I");
        btnTogInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogInfoActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogInfo);
        btnTogInfo.setBounds(610, 230, 40, 28);

        btnGroupLogLevel.add(btnTogWarn);
        btnTogWarn.setText("W");
        btnTogWarn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogWarnActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogWarn);
        btnTogWarn.setBounds(610, 260, 40, 28);

        btnGroupLogLevel.add(btnTogFatal);
        btnTogFatal.setText("F");
        btnTogFatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogFatalActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogFatal);
        btnTogFatal.setBounds(610, 320, 40, 28);

        btnGroupLogLevel.add(btnTogError);
        btnTogError.setText("E");
        btnTogError.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTogErrorActionPerformed(evt);
            }
        });
        getContentPane().add(btnTogError);
        btnTogError.setBounds(610, 290, 40, 28);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        m_graphRefreshThread.m_bContinue = false;
        
        btnExit.setVisible(false);
        theApp.GetPollerVacThread().stop();
        theApp.GetPollerHvThread().stop();
        theApp.GetStorageThread().StopThread();
        theApp.GetCommE2P().stop();
        theApp.GetCommA2P().stop();
        m_LedsThread.stop();
        
        m_StorageRepacker.StorageRepackStop();
        m_LogsStorageRepacker.LogsRepackStop();
        
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
    private javax.swing.JToggleButton btnTogDebug;
    private javax.swing.JToggleButton btnTogError;
    private javax.swing.JToggleButton btnTogFatal;
    private javax.swing.JToggleButton btnTogInfo;
    private javax.swing.JToggleButton btnTogTrace;
    private javax.swing.JToggleButton btnTogWarn;
    private javax.swing.JButton btnVacuumRun;
    private javax.swing.JButton btnVacuumStop;
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
    private javax.swing.JPanel pnlGraph;
    // End of variables declaration//GEN-END:variables
}