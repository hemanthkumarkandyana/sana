package com.mss;

import java.util.List;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbDFDL;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMRM;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;
import com.ibm.security.util.calendar.BaseCalendar.Date;
import com.ibm.xtq.xml.types.HexBinaryType;

public class CSV_TO_CSV_JavaCompute extends MbJavaComputeNode {

	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		MbOutputTerminal alt = getOutputTerminal("alternate");
		
		// Declaring the varaibles
		String name;
		int id;
		Date dob;
		byte[] bob;
		
		MbMessage inMessage = inAssembly.getMessage();
		MbMessage outMessage = new MbMessage(inMessage);
		MbMessageAssembly outAssembly = null;
		outAssembly = new MbMessageAssembly(inAssembly, outMessage);

		try {
			// create new message as a copy of the input
			outMessage.getRootElement().getFirstElementByPath("./Properties/MessageSet").setValue("M872E8C002001");
			outMessage.getRootElement().getFirstElementByPath("./Properties/MessageType").setValue("{}:EMPLOYEE1");
			outMessage.getRootElement().getFirstElementByPath("./Properties/MessageFormat").setValue("Text_CSV");
			
			
			// Add user code below
			MbElement root = inAssembly.getMessage().getRootElement();
			MbElement MRM=root.getLastChild();
			 
			
			List InputRecords= (List) MRM.evaluateXPath("csv"); 
			
			int count=InputRecords.size();
			
			MbElement outroot = outMessage.getRootElement();
			
			 MbElement mrm1=outroot.createElementAsLastChild(MbMRM.PARSER_NAME);
			
			for (int i = 0; i <InputRecords.size(); i++) {
			//MbElement emp=MRM.getFirstChild();
		     /*name=MRM.getFirstElementByPath("./ENAME").getValueAsString();;
		     id=Integer.parseInt(MRM.getFirstElementByPath("./EID").getValueAsString());
		    String dob1 = MRM.getFirstElementByPath("./EDATE").getValueAsString();
		     bob = ((String) MRM.getFirstElementByPath("./EBOB").getValueAsString()).getBytes();*/
		     
				
		    
		     name=((MbElement) InputRecords.get(i)).getFirstElementByPath("./ENAME").getValueAsString();;
		     id=Integer.parseInt(((MbElement) InputRecords.get(i)).getFirstElementByPath("./EID").getValueAsString());
		    String dob1 = ((MbElement) InputRecords.get(i)).getFirstElementByPath("./EDATE").getValueAsString();
		     bob = ((String) ((MbElement) InputRecords.get(i)).getFirstElementByPath("./EBOB").getValueAsString()).getBytes();
		     
		    
		 	MbElement empDetail=mrm1.createElementAsLastChild(MbElement.TYPE_NAME_VALUE,"EMP",null);
			MbElement ename1=empDetail.createElementAsLastChild(MbElement.TYPE_NAME_VALUE,"ENAME",name);
			MbElement eid1=empDetail.createElementAsLastChild(MbElement.TYPE_NAME_VALUE,"EID",id);
			empDetail.createElementAsLastChild(MbElement.TYPE_NAME_VALUE,"EDATE",dob1);
			empDetail.createElementAsLastChild(MbElement.TYPE_NAME_VALUE,"EBOB",bob);
			}
			
			outroot.getLastChild().getPreviousSibling().detach();
			//outroot.getLastChild().getLastChild().detach();
			// End of user code
			// ----------------------------------------------------------
		} catch (MbException e) {
			// Re-throw to allow Broker handling of MbException
			throw e;
		} catch (RuntimeException e) {
			// Re-throw to allow Broker handling of RuntimeException
			throw e;
		} catch (Exception e) {
			// Consider replacing Exception with type(s) thrown by user code
			// Example handling ensures all exceptions are re-thrown to be handled in the flow
			throw new MbUserException(this, "evaluate()", "", "", e.toString(),
					null);
		}
		// The following should only be changed
		// if not propagating message to the 'out' terminal
		out.propagate(outAssembly);

	}

}
