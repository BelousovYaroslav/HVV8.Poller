/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller.vac;

import hvv_poller.storage.HVV_Storage_DataUnit;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;

/**
 * Thread-class for COM-port listener
 */
public class SocketReaderVac implements Runnable 
{
    static Logger logger = Logger.getLogger(SocketReaderVac.class);
    
    InputStream in;
    TwoWaySocketServerCommVac pParent;
    
    private boolean m_bContinue;

    public void StopThread() { m_bContinue = false;}
    
    public SocketReaderVac( InputStream in, TwoWaySocketServerCommVac parent)
    {
        this.in = in;
        pParent = parent;
    }
    
    public int ParseDouble( int nStart, byte [] answer, String strParamName) {
        int nLen = answer[ nStart];
        if( nLen > 0 && nLen < 15) {
            String strDblValue = "";
            for( int i=0; i < nLen; i++) {
                strDblValue += ( ( char) answer[ nStart + 1 + i]);
            }
                                            
            HVV_Storage_DataUnit unit;
            try {
                strDblValue = strDblValue.replace( ',', '.');
                Double dblValue = Double.valueOf( strDblValue);
                unit = new HVV_Storage_DataUnit(
                            pParent.theApp.GetLocalDate(),
                            HVV_Storage_DataUnit.UNIT_VACUUM,
                            strParamName,
                            dblValue);

                if( unit == null)
                    logger.error( "NULL Double-DataUnit");
                pParent.theApp.GetStorageThread().AddDataPointToQueue( unit);
                pParent.theApp.m_mapHotValues.put( strParamName, dblValue);
                        
            } catch (Exception ex) {
                logger.error( "Exception при создании HVV_Storage_DataUnit", ex);
            }
                                        
        }
        
        return ( nLen + 1);
    }
    
    public int ParseByte( int nStart, byte [] answer, String strParamName) {
        if( answer[ nStart] == '0' || answer[ nStart] == '1') {
            HVV_Storage_DataUnit unit;
            try {
                String strDblValue = "" + ( ( char) answer[ nStart]);
                double dblValue = Double.parseDouble( strDblValue);
                unit = new HVV_Storage_DataUnit(
                    pParent.theApp.GetLocalDate(),
                    HVV_Storage_DataUnit.UNIT_VACUUM,
                    strParamName,
                    dblValue);
                                            
                if( unit == null) {
                    logger.error( "NULL Byte-DataUnit");
                }
                pParent.theApp.GetStorageThread().AddDataPointToQueue( unit);
                pParent.theApp.m_mapHotValues.put( strParamName, dblValue);
            } catch (Exception ex) {
                logger.error( "Exception при создании HVV_Storage_DataUnit", ex);
            }
        }
        
        return 1;
    }
        
    @Override
    public void run ()
    {
        m_bContinue = true;
        byte[] buffer = new byte[ CircleBuffer.BUFFER_LEN];
        int len = -1;
        
        logger.debug("In");
        
        try {
            
            logger.debug("before while");
            
            while( m_bContinue) {
                
                if( this.in.available() > 0) {
                    len = this.in.read(buffer);
                
                    logger.trace("after this.in.read. len=" + len);
                
                    String strResponse1 = new String( buffer, 0, len - 1);
                    //logger.debug( "COM-INTERACTION Response1: " + strResponse1);
                
                    pParent.crclBuffer.AddBytes( buffer, len);
                    
                    
                    String strR = "\"";
                    strR += "" + String.format( "%x", buffer[ 0]);
                    
                    for( int i=1; i<len; i++) {
                        strR += ", " + String.format( "%x", buffer[i]);
                    }
                    
                    strR += "\"";
                    //logger.info( strR);
                    
                    //if( pParent.strCommandInAction != null) {
                        len = pParent.crclBuffer.isAnswerReady();
                        if( len > 0) {
                            byte [] answer = new byte[len];
                            if( pParent.crclBuffer.getAnswer( len, answer) == 0) {
                            
                                boolean bCorrectResponse = true;
                        
                                //check if strResponse is valid
                                //1. 0xff на конце
                                if( answer[len-1] != -1) {
                                    logger.warn( "Пришедший ответ без (0xFF) на конце");
                                    bCorrectResponse = false;
                                }
                            
                                if( bCorrectResponse) {
                                    
                                    if( pParent.theApp.m_pMainWnd != null)
                                        if( pParent.theApp.m_pMainWnd.lblLastActionVac != null)
                                            pParent.theApp.m_pMainWnd.lblLastActionVac.setText(
                                                pParent.theApp.NiceFormatDateTime( pParent.theApp.GetLocalDate()));
                                    
                                    pParent.GetTimeoutThread().interrupt();
                                
                                    //CommandItem item = pParent.currentCommandInAction;
                                    pParent.strCommandInAction = null;
                                
                                    //logger.info( "ОБРАБОТКА ОТВЕТА!" + pParent.theApp.GetPollerVacThread().m_nRespondTimeOuts);
                                    pParent.theApp.GetPollerVacThread().m_nRespondTimeOuts = 0;
                                    int nIndex = 0;
                                    
                                    //valve_14a
                                    nIndex += ParseByte( nIndex, answer, "14A.01");
                                    
                                    //valve_14b
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "14B.01");
                                    
                                    //valve_14c
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "14C.01");
                                    
                                    //valve_14d
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "14D.01");
                                    
                                    //valve_14e
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "14E.01");

                                    //valve_15a
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "15A.01");
                                    
                                    //valve_15b
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "15B.01");
                                    
                                    //valve_15c
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "15C.01");

                                    //valve_23a
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "23A.01");
                                    
                                    //valve_23b
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "23B.01");
                                    
                                    //valve_16a
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "16A.03");
                                    
                                    //valve_16b
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "16B.03");
                                    
                                    //valve_16c
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "16C.03");

                                    //valve_17a
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "17A.03");
                                    
                                    //valve_17b
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "17B.03");

                                    //valve_10a
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "10A.01");
                                    
                                    //valve_10b
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "10B.01");
                                    
                                    //valve_10c
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "10C.01");

                                    //valve_9a
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "09A.01");
                                    
                                    //valve_9b
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "09B.01");
                                    
                                    //valve_9c
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "09C.01");
                                    
                                    //valve_9d
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "09D.01");

                                    //valve_8a
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "08A.01");
                                    
                                    //valve_8b
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "08B.01");
                                    
                                    //valve_8c
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "08C.01");

                                    //valve_13
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "013.01");

                                    //valve_12
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "012.01");

                                    //valve_11A
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "11A.01");

                                    //valve_11a
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "11B.01");

                                    //valve_18
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "018.03");

                                    //relay_20
                                    if( nIndex < len) {
                                        nIndex += ParseByte( nIndex, answer, "020.01");
                                        //logger.debug("Parsing value for relay: 020.01");
                                    }

                                    //Geter
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "021.01");
                                    
                                    //HiCube_80
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "003.01");
                                    
                                    //HiPace_300
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "002.01");
                                    
                                    //nXSD15iR
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "001.01");

                                    //Heater_1
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "24A.01");

                                    //Heater_2
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "24B.01");
                                    
                                    //Heater_3
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "24C.01");

                                    //Heater_4
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "24D.01");
                                    
                                    //Heater_5
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "24E.01");

                                    //Heater_6
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "24F.01");
                                    
                                    //Heater_7
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "24G.01");

                                    //Heater_8
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "24H.01");
                                    
                                    //LG_1
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "24A.03");
                                    
                                    //LG_2
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "24B.03");
                                    
                                    //LG_3
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "24C.03");
                                    
                                    //LG_4
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "24D.03");
                                    
                                    //LG_5
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "24E.03");
                                    
                                    //LG_6
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "24F.03");
                                    
                                    //LG_7
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "24G.03");
                                    
                                    //LG_8
                                    if( nIndex < len)
                                        nIndex += ParseByte( nIndex, answer, "24H.03");
//**********
//double
//**********
                                    //Ctrl_16a
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "16A.01");
                                    else
                                        logger.error( "Fails on 16A.01");
                                    
                                    //Ctrl_16b
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "16B.01");
                                    else
                                        logger.error( "Fails on 16B.01");
                                    
                                    //Ctrl_16c
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "16C.01");
                                    else
                                        logger.error( "Fails on 16C.01");
                                    
                                    //Ctrl_17a
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "17A.01");
                                    else
                                        logger.error( "Fails on 17A.01");
                                    
                                    //Ctrl_17b
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "17B.01");
                                    else
                                        logger.error( "Fails on 17B.01");
                                    
                                    //Ctrl_18
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "018.01");
                                    else
                                        logger.error( "Fails on 018.01");

                                    //Ind_16a
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "16A.02");
                                    else
                                        logger.error( "Fails on 16A.02");
                                    
                                    //Ind_16b
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "16B.02");
                                    else
                                        logger.error( "Fails on 16B.02");
                                    
                                    //Ind_16c
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "16C.02");
                                    else
                                        logger.error( "Fails on 16C.02");

                                    //Ind_17a
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "17A.02");
                                    else
                                        logger.error( "Fails on 17A.02");
                                    
                                    //Ind_17b
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "17B.02");
                                    else
                                        logger.error( "Fails on 17B.02");
                                    
                                    //Ind_18
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "018.02");
                                    else
                                        logger.error( "Fails on 018.02");
        
                                    //baratron_7
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "007.01");
                                    else
                                        logger.error( "Fails on 007.01");
                                    
                                    //baratron_6
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "006.01");
                                    else
                                        logger.error( "Fails on 006.01");

                                    //velocity_HiCube
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "003.02");
                                    else
                                        logger.error( "Fails on 003.02");
                                    
                                    //velocity_HiPace
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "002.02");
                                    else
                                        logger.error( "Fails on 002.02");
                                    
                                    //Thermometer_1
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "24A.02");
                                    else
                                        logger.error( "Fails on 24A.02");
                                    
                                    //Thermometer_2
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "24B.02");
                                    else
                                        logger.error( "Fails on 24B.02");
                                    
                                    //Thermometer_3
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "24C.02");
                                    else
                                        logger.error( "Fails on 24C.02");
                                    
                                    //Thermometer_4
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "24D.02");
                                    else
                                        logger.error( "Fails on 24D.02");
                                    
                                    //Thermometer_5
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "24E.02");
                                    else
                                        logger.error( "Fails on 24E.02");
                                    
                                    //Thermometer_6
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "24F.02");
                                    else
                                        logger.error( "Fails on 24F.02");
                                    
                                    //Thermometer_7
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "24G.02");
                                    else
                                        logger.error( "Fails on 24G.02");
                                    
                                    //Thermometer_8
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "24H.02");
                                    else
                                        logger.error( "Fails on 24H.02");
                                    
                                    //vacuum_4a
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "04A.01");
                                    else
                                        logger.error( "Fails on 04A.01");
                                    
                                    //vacuum_4b
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "04B.01");
                                    else
                                        logger.error( "Fails on 04B.01");
                                    
                                    //vacuum_4c
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "04C.01");
                                    else
                                        logger.error( "Fails on 04C.01");

                                    //vacuum_5
                                    if( nIndex < len)
                                        nIndex += ParseDouble( nIndex, answer, "005.01");
                                    else
                                        logger.error( "Fails on 005.01");
        
                                }
                                
                            }
                            else
                                logger.error( "CircleBuffer.getAnswer returns not 0");
                        }
                        else
                            logger.trace( "CircleBuffer ответил что ответ не готов!");
                    
                    //}
                    //else {
                    //    logger.warn( "Входящие данные при отсутствии запрашивающей команды!");
                    //}
                }
                else {
                    //logger.debug( "No RX data");
                    len = 0;
                }
                
                if( len == 0)
                    Thread.sleep( 50);
            }
            
            //something new
            //something new2
            logger.debug("after while");
            
        }
        catch ( IOException ex) {
            logger.error( "IOException caught!", ex);
        }
        catch ( InterruptedException ex) {
            logger.error( "InterruptedException caught!", ex);
        }
        
        logger.debug("Out");
    }
}