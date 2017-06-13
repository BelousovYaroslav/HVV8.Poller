/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_poller.hv;

/**
 *
 * @author control
 */
public class CommandItem {
    private final String m_strCommand;
    public String GetCommand() { return m_strCommand; }
    
    private final String m_strObject;
    public String GetObject() { return m_strObject; }
    
    public CommandItem( String strCommand, String strObject) {
        m_strCommand = strCommand;
        m_strObject = strObject;
    }
}
