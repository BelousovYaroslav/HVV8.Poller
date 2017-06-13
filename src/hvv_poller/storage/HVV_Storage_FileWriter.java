/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller.storage;

import hvv_devices.HVV_HvDevice;
import hvv_devices.HVV_HvDevices;
import hvv_devices.HVV_VacuumDevice;
import hvv_poller.HVV_Poller;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import hvv_devices.HVV_VacuumDevices;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author yaroslav
 */
public class HVV_Storage_FileWriter {
    static Logger logger = Logger.getLogger( HVV_Storage_FileWriter.class);
    private final HVV_Poller theApp;
    
    TreeMap m_mapFileWriters;
    
    public HVV_Storage_FileWriter( HVV_Poller app) throws Exception {
        theApp = app;
        
        m_mapFileWriters = new TreeMap();
        //'data' folder
        String strData = theApp.GetAMSRoot() + "/data";
        File file = new File( strData);
        if( file.exists() == false) {
            logger.warn( "Не существует папки хранилища данных! Создаётся новая!");
            boolean bSuccess = ( new File( strData)).mkdirs();
            if (!bSuccess) {
                throw new Exception( "Не смог создать папку 'data' хранилища данных!");
            }
        }
        
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy.MM.dd");        
        
        HVV_VacuumDevices dvcs = HVV_VacuumDevices.getInstance();
        
        Set set = dvcs.m_devices.entrySet();
        Iterator it = set.iterator();
        while( it.hasNext()) {
            Map.Entry entry = ( Map.Entry) it.next();
            HVV_VacuumDevice dev = ( HVV_VacuumDevice) entry.getValue();
            
            Set set2 = dev.m_mapParameters.entrySet();
            Iterator it2 = set2.iterator();
            while( it2.hasNext()) {
                Map.Entry entry2 = ( Map.Entry) it2.next();
                
                String strParameterKey = ( String) entry2.getKey();
                String strParameterValue = ( String) entry2.getValue();
                logger.debug( "Создаём поток в файл данных для вакуумного объекта [" + dev.toString() + "].["
                                + strParameterKey + "." + strParameterValue + "]");
                
                Date dt = theApp.GetLocalDate();                        
                String strFileName = strData + "/" + formatter.format( dt) + ".VAC." + dev.m_strID + "." + strParameterKey + ".csv";
                FileOutputStream streamOutput = new FileOutputStream( strFileName, true);
                
                m_mapFileWriters.put( dev.m_strID + "." + strParameterKey, streamOutput);
            }
        }
        
        
        HVV_HvDevices dvcs_hv = HVV_HvDevices.getInstance();
        
        Set set_hv = dvcs_hv.m_devices.entrySet();
        Iterator it_hv = set_hv.iterator();
        while( it_hv.hasNext()) {
            Map.Entry entry = ( Map.Entry) it_hv.next();
            HVV_HvDevice dev = ( HVV_HvDevice) entry.getValue();
            
            Set set2 = dev.m_mapParameters.entrySet();
            Iterator it2 = set2.iterator();
            while( it2.hasNext()) {
                Map.Entry entry2 = ( Map.Entry) it2.next();
                
                String strParameterKey = ( String) entry2.getKey();
                String strParameterValue = ( String) entry2.getValue();
                logger.debug( "Создаём поток в файл данных для высоковольтного объекта [" + dev.toString() + "].["
                                + strParameterKey + "." + strParameterValue + "]");
                
                Date dt = theApp.GetLocalDate();
                String strFileName = strData + "/" + formatter.format( dt) + ".HV." + dev.m_strID + "." + strParameterKey + ".csv";
                FileOutputStream streamOutput = new FileOutputStream( strFileName, true);
                
                m_mapFileWriters.put( dev.m_strID + "." + strParameterKey, streamOutput);
            }
        }
    }
    
    public void DayChange() {
        
        //сперва позакрываем все открытые файл-потоки
        if( m_mapFileWriters != null) {
            if( m_mapFileWriters.size() > 0) {
                Set set = m_mapFileWriters.entrySet();
                Iterator it = set.iterator();
                while( it.hasNext()) {
                    Map.Entry entry = ( Map.Entry) it.next();
                    
                    String strKey = ( String) entry.getKey();
                    logger.debug( "Закрываем файл-поток для параметра '" + strKey + "'");
                    FileOutputStream stream = ( FileOutputStream) entry.getValue();
                    try {
                        stream.close();
                    }
                    catch( IOException ex) {
                        logger.error( "При закрытии файл-потока произошла IOException", ex);
                    }
                }
            }
            else {
                logger.warn( "m_mapFileWriters != NULL но он пустой!");
            }
        }
        else {
            logger.warn( "m_mapFileWriters == NULL!");
        }
        
        
        //у нас все текущие файл-потоки для параметров прикрыты
        //создаём связки новых файл-потоков к параметрам
        if( m_mapFileWriters != null)
            m_mapFileWriters.clear();
        else
            m_mapFileWriters = new TreeMap();
        
        //'data' folder
        String strData = theApp.GetAMSRoot() + "/data";
        File file = new File( strData);
        if( file.exists() == false) {
            logger.warn( "Не существует папки хранилища данных! Создаётся новая!");
            boolean bSuccess = ( new File( strData)).mkdirs();
            if ( !bSuccess) {
                logger.fatal( "Не смог создать папку 'data' хранилища данных!");
                return;
            }
        }
        
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy.MM.dd");        
        
        HVV_VacuumDevices dvcs = HVV_VacuumDevices.getInstance();
        
        Set set = dvcs.m_devices.entrySet();
        Iterator it = set.iterator();
        while( it.hasNext()) {
            Map.Entry entry = ( Map.Entry) it.next();
            HVV_VacuumDevice dev = ( HVV_VacuumDevice) entry.getValue();
            
            Set set2 = dev.m_mapParameters.entrySet();
            Iterator it2 = set2.iterator();
            while( it2.hasNext()) {
                Map.Entry entry2 = ( Map.Entry) it2.next();
                
                String strParameterKey = ( String) entry2.getKey();
                String strParameterValue = ( String) entry2.getValue();
                logger.debug( "Создаём поток в файл данных для вакуумного объекта [" + dev.toString() + "].["
                                + strParameterKey + "." + strParameterValue + "]");
                
                Date dt = theApp.GetLocalDate();                        
                String strFileName = strData + "/" + formatter.format( dt) + ".VAC." + dev.m_strID + "." + strParameterKey + ".csv";
                try {
                    FileOutputStream streamOutput = new FileOutputStream( strFileName, true);
                    m_mapFileWriters.put( dev.m_strID + "." + strParameterKey, streamOutput);
                }
                catch( FileNotFoundException ex) {
                    logger.error( "При создании файл-потока для '" + dev.m_strID +
                            "." + strParameterKey + "' произошла FileNotFoundException", ex);
                }                
            }
        }
        
        
        HVV_HvDevices dvcs_hv = HVV_HvDevices.getInstance();
        
        Set set_hv = dvcs_hv.m_devices.entrySet();
        Iterator it_hv = set_hv.iterator();
        while( it_hv.hasNext()) {
            Map.Entry entry = ( Map.Entry) it_hv.next();
            HVV_HvDevice dev = ( HVV_HvDevice) entry.getValue();
            
            Set set2 = dev.m_mapParameters.entrySet();
            Iterator it2 = set2.iterator();
            while( it2.hasNext()) {
                Map.Entry entry2 = ( Map.Entry) it2.next();
                
                String strParameterKey = ( String) entry2.getKey();
                String strParameterValue = ( String) entry2.getValue();
                logger.debug( "Создаём поток в файл данных для высоковольтного объекта [" + dev.toString() + "].["
                                + strParameterKey + "." + strParameterValue + "]");
                
                Date dt = theApp.GetLocalDate();
                String strFileName = strData + "/" + formatter.format( dt) + ".HV." + dev.m_strID + "." + strParameterKey + ".csv";
                try {
                    FileOutputStream streamOutput = new FileOutputStream( strFileName, true);
                    m_mapFileWriters.put( dev.m_strID + "." + strParameterKey, streamOutput);
                }
                catch( FileNotFoundException ex) {
                    logger.error( "При создании файл-потока для '" + dev.m_strID +
                            "." + strParameterKey + "' произошла FileNotFoundException", ex);
                }
            }
        }
        
    }
    
    public void StopFileWriter() {
        Set set = m_mapFileWriters.entrySet();
        Iterator it = set.iterator();
        while( it.hasNext()) {
            Map.Entry entry = ( Map.Entry) it.next();
            FileOutputStream stream = ( FileOutputStream) entry.getValue();
            try {
                stream.close();
            } catch (IOException ex) {
                logger.error( "При закрытии потоков записи в файлы данных произошёл IOException", ex);
            }
            stream = null;
        }
    }
    
    public void saveDataUnit( HVV_Storage_DataUnit unit) {
        if( unit == null) {
            logger.fatal( "Incoming DataUnit is null!");
            return;
        }
        if( unit.GetHvOrVacPart() == HVV_Storage_DataUnit.UNIT_VACUUM) {
            FileOutputStream stream = ( FileOutputStream) m_mapFileWriters.get( unit.GetDescriptor());
            if( stream != null) {
                String strToWrite;
                
                GregorianCalendar clndr = new GregorianCalendar();
                clndr.setTime( unit.GetDate());
                
                strToWrite = String.format( "%02d.%02d.%02d %02d:%02d:%02d.%03d;%s;\n",
                        clndr.get(Calendar.DAY_OF_MONTH),
                        clndr.get(Calendar.MONTH) + 1,
                        clndr.get(Calendar.YEAR),
                        clndr.get(Calendar.HOUR_OF_DAY),
                        clndr.get(Calendar.MINUTE),
                        clndr.get(Calendar.SECOND),
                        clndr.get(Calendar.MILLISECOND),
                        unit.GetValue().toString());
                try {
                    stream.write( strToWrite.getBytes());
                } catch (IOException ex) {
                    logger.error("При сохранении точки данных в файл произошёл IOException", ex);
                }
            }
            else
                logger.warn( "Null file stream for  descriptor: '" + unit.GetDescriptor() + "'");
        }
        
        if( unit.GetHvOrVacPart() == HVV_Storage_DataUnit.UNIT_HV) {
            FileOutputStream stream = ( FileOutputStream) m_mapFileWriters.get( unit.GetDescriptor());
            if( stream != null) {
                String strToWrite;
                
                GregorianCalendar clndr = new GregorianCalendar();
                clndr.setTime( unit.GetDate());
                
                strToWrite = String.format( "%02d.%02d.%02d %02d:%02d:%02d.%03d;%s;\n",
                        clndr.get(Calendar.DAY_OF_MONTH),
                        clndr.get(Calendar.MONTH) + 1,
                        clndr.get(Calendar.YEAR),
                        clndr.get(Calendar.HOUR_OF_DAY),
                        clndr.get(Calendar.MINUTE),
                        clndr.get(Calendar.SECOND),
                        clndr.get(Calendar.MILLISECOND),
                        unit.GetValue().toString());
                try {
                    stream.write( strToWrite.getBytes());
                } catch (IOException ex) {
                    logger.error("При сохранении точки данных в файл произошёл IOException", ex);
                }
            }
            else
                logger.warn( "Null file stream for  descriptor: '" + unit.GetDescriptor() + "'");
        }
    }
}
