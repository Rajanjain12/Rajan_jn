package com.rsnt.util.common;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Manager;
import org.jboss.seam.log.Log;

@Name("convManager")
@Scope(ScopeType.EVENT)
public class ConvManager {
	
	  @Logger
      private Log log;

	public void endAndStartConversation() {
		log.info("Running CID:::::::::::"
				+ Manager.instance().getCurrentConversationId());
		Manager.instance().leaveConversation();
		Manager.instance().beginConversation();
		log.info("New CID:::::::::::" + Manager.instance().getCurrentConversationId());
	}
	

}
