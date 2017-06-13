/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller.storage;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author yaroslav
 */
public class HVV_Storage_DataUnit {
    public static final int UNIT_VACUUM = 1;
    public static final int UNIT_HV = 2;
            
    private int m_nHvOrVac;
    public int GetHvOrVacPart() { return m_nHvOrVac;}
            
    private String m_strDescriptor;
    public String GetDescriptor() { return m_strDescriptor; }
    
    private Date m_dt;
    public Date GetDate() { return m_dt;}
    
    private Double m_DblValue;
    public Double GetValue() { return m_DblValue;}
    
    public HVV_Storage_DataUnit( Date dt, int nHvOrVacPart, String strDescription, Double DblValue) throws Exception {
        if( nHvOrVacPart != UNIT_VACUUM && nHvOrVacPart != UNIT_HV) {
            throw new Exception( "Неверно задан тип единицы данных (!HV && !VAC)");
        }
        
        m_dt = dt;
        m_nHvOrVac = nHvOrVacPart;
        m_strDescriptor = strDescription;
        m_DblValue = DblValue;
    }
}
