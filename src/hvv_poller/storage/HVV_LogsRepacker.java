/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller.storage;

import hvv_poller.HVV_Poller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class HVV_LogsRepacker {
    static final Logger logger = Logger.getLogger(HVV_LogsRepacker.class);
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final HVV_Poller theApp;

    ScheduledFuture <?> storageHandle;
    
    public HVV_LogsRepacker( HVV_Poller app) {
        theApp = app;
    }
    
    
    
    
    public void LogsRepackStart() {
     
        final Runnable repacker = new Runnable() {
            @Override
            public void run() {
                
                logger.info( "Точка входа в перепаковщик файлов логов");
                
                /*GregorianCalendar clndrNow = new GregorianCalendar();
                clndrNow.setTime( theApp.GetLocalDate());
                if( clndrNow.get( Calendar.HOUR) == 0) {
                    logger.info( "В час перехода дня не запускаемся!");
                    return;
                }*/
                
                TreeMap mapFiles = new TreeMap();
                
                File folder = new File( theApp.GetAMSRoot() + "/logs");
                for( final File fileEntry : folder.listFiles()) {
                    if( !fileEntry.isDirectory()) {
                        String strFileName = fileEntry.getName();
                        
                        String [] strFileNamePartsDot = strFileName.split( "\\.");
                        String [] strFileNamePartsMinus = strFileName.split( "-");
                        if( strFileNamePartsDot.length == 3 && strFileNamePartsMinus.length == 2) {
                            
                            int nIndexMinus = strFileName.indexOf( "-");
                            int nIndexDot = strFileName.indexOf( ".", nIndexMinus);
                            String strDatePart = strFileName.substring( nIndexMinus + 1, nIndexDot);
                            
                            if( strDatePart.length() == 8) {
                                String strYear = strDatePart.substring( 0 , 4);
                                String strMonth = strDatePart.substring( 4, 6);
                                String strDay = strDatePart.substring( 6, 8);

                                int nYear  = Integer.parseInt( strYear);
                                int nMonth = Integer.parseInt( strMonth);
                                int nDay   = Integer.parseInt( strDay);

                                GregorianCalendar dt1 = new GregorianCalendar( nYear, nMonth-1, nDay);
                                //String strDt1 = "" + nYear + "." + nMonth + "." + nDay;
                                String strDt1 = String.format( "%02d.%02d.%02d", nYear, nMonth, nDay);

                                long ldt1 = dt1.getTimeInMillis();
                                long ldtn = theApp.GetLocalDate().getTime();
                                double lLifeLong = (( double) ( ldtn - ldt1)) / 1000. / 3600. / 24.;
                                if( lLifeLong >= 2.) {

                                    logger.info( "Имя файла '" + strFileName + "' содержит 2 точки и один минус, и дата, описанная в нём, была более двух дней назад (файл достаточно стар). Отправляем в упаковщик.");

                                    if( mapFiles.containsKey( strDt1)) {
                                        String strFiles = ( String) mapFiles.get( strDt1);
                                        strFiles += " " + strFileName;
                                        mapFiles.put( strDt1, strFiles);
                                    }
                                    else {
                                        mapFiles.put( strDt1, strFileName);
                                    }
                                }
                                else {
                                    logger.info( "Имя файла '" + strFileName + "' содержит 2 точки и один минус, но дата, описанная в нём, была менее двух дней назад (файл свеж).");
                                }
                            }
                            else {
                                logger.info( "Выделенная часть ответственная за дату = '" + strDatePart + "', и длина этой части не равна 8");
                            }
                        }
                        else {
                            logger.info( "Имя файла '" + strFileName + "' не содержит 2 точки и один минус.");
                        }
                        
                    }
                    else {
                        logger.info( "Файл '" + fileEntry + "' это директория.");
                    }
                }
    
                
                
                Set set = mapFiles.entrySet();
                Iterator it = set.iterator();
        
                while( it.hasNext()) {
                    Map.Entry entry = ( Map.Entry) it.next();
                    String strKey = ( String) entry.getKey();
                    String strFiles = ( String) entry.getValue();
                    
                    logger.debug( "key='" + strKey + "'. Values='" + strFiles + "'");
                    
                    FileOutputStream fos;
                    try {
                        fos = new FileOutputStream( theApp.GetAMSRoot() + "/logs/" + strKey + ".zip");
                        ZipOutputStream zos = new ZipOutputStream(fos);
                        
                        String [] arrFiles = strFiles.split( " ");
                        for( final String strFileNameToPack : arrFiles) {
                            
                            File file = new File( theApp.GetAMSRoot() + "/logs/" + strFileNameToPack);
                            FileInputStream fis = new FileInputStream( file);
                            ZipEntry zipEntry = new ZipEntry( strFileNameToPack);
                            zos.putNextEntry( zipEntry);

                            byte[] bytes = new byte[1024];
                            int length;
                            while ((length = fis.read(bytes)) >= 0) {
                                zos.write( bytes, 0, length);
                            }

                            zos.closeEntry();
                            fis.close();
                        }
                        
                        zos.close();
                        fos.close();
                        
                    } catch (FileNotFoundException ex) {
                        logger.error( "FileNotFoundException caught!", ex);
                    } catch (IOException ex) {
                        logger.error( "IOException caught!", ex);
                    }
                    
                }
                
                
                it = set.iterator();
                while( it.hasNext()) {
                    Map.Entry entry = ( Map.Entry) it.next();
                    String strKey = ( String) entry.getKey();
                    String strFiles = ( String) entry.getValue();
                    
                    logger.debug( "key='" + strKey + "'. Values='" + strFiles + "'");
                    
                    FileOutputStream fos;
                    
                    String [] arrFiles = strFiles.split( " ");
                    for( final String strFileNameToPack : arrFiles) {
                        File f = new File(theApp.GetAMSRoot() + "/logs/" + strFileNameToPack);
                        f.delete();
                    }
                    
                }
                
            }
        };
     
        storageHandle = scheduler.scheduleAtFixedRate( repacker, 0, 4, java.util.concurrent.TimeUnit.HOURS);
        
        /*
        scheduler.schedule( new Runnable() {
            public void run() { beeperHandle.cancel(true); }
        }, 60 * 60, SECONDS);
        */
    }
    
    public void LogsRepackStop() {
        storageHandle.cancel( true);
        scheduler.shutdownNow();
    }
    
}