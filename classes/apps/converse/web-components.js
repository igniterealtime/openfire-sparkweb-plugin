class ConverseAbstract extends HTMLElement{
	constructor(name){
		super();
		this.setTemplate(name)		
	} 	
 
	setTemplate(name) {
		console.debug("setTemplate", name);
		const template = document.getElementById(name);
		
		if (template) {
			this.appendChild(template.content.cloneNode(true));
		}			
	}
}

class ConverseRoot extends ConverseAbstract{
	constructor(){  
		super("converse-root");	
	} 
	
}

class ConverseFontAwesome extends ConverseAbstract{
	constructor(){  
		super('converse-fontawesome');			
	} 
}

class ConverseMucSidebar extends ConverseAbstract{
	constructor(){  
		super('converse-muc-sidebar');		
	} 
}

class ConverseMucBottomPanel extends ConverseAbstract{
	constructor(){  
	super('converse-muc-bottom-panel');		
	} 
}

class ConverseChatContent extends ConverseAbstract{
	constructor(){  
		super('converse-chat-content');		
	} 
}

class ConverseMucChatArea extends ConverseAbstract{
	constructor(){  
		super('converse-muc-chatarea');		
	} 
}

class ConverseMucHeading extends ConverseAbstract{
	constructor(){  
		super('converse-muc-heading');		
	} 
}

class ConverseMuc extends ConverseAbstract{
	constructor(){  
		super('converse-muc');		
	} 
}

class ConverseChat extends ConverseAbstract{
	constructor(){  
		super('converse-chat');		
	} 
}

class ConverseControlBox extends ConverseAbstract{
	constructor(){  
		super('converse-controlbox');		
	} 
}

class ConverseDragResize extends ConverseAbstract{
	constructor(){  
		super('converse-dragresize');		
	} 
}

class ConverseChatHeading extends ConverseAbstract{
	constructor(){  
		super('converse-chat-heading');		
	} 
}

class ConverseChatBottomPanel extends ConverseAbstract{
	constructor(){  
		super('converse-chat-bottom-panel');		
	} 
}

class ConverseChats extends ConverseAbstract{
	constructor(){  
		super('converse-chats');		
	} 
}

async function setupWebComponents() {
	const response = await fetch("./web-components.html");
	const html = await response.text();
	const template = document.createElement('div');
	template.innerHTML = html;	
	document.body.appendChild(template);	

	window.customElements.define('converse-controlbox', 		ConverseControlBox);
	window.customElements.define('converse-dragresize', 		ConverseDragResize);
	
	window.customElements.define('converse-chat', 				ConverseChat);
	window.customElements.define('converse-chat-bottom-panel', 	ConverseChatBottomPanel);	
	window.customElements.define('converse-chat-heading', 		ConverseChatHeading);
	window.customElements.define('converse-chat-content', 		ConverseChatContent);	
	
	window.customElements.define('converse-muc-sidebar', 		ConverseMucSidebar);	
	window.customElements.define('converse-muc-bottom-panel', 	ConverseMucBottomPanel);
	window.customElements.define('converse-muc-heading', 		ConverseMucHeading);
	window.customElements.define('converse-muc-chatarea', 		ConverseMucChatArea);
	window.customElements.define('converse-muc', 				ConverseMuc);

	window.customElements.define('converse-chats', 				ConverseChats);	
	window.customElements.define('converse-fontawesome', 		ConverseFontAwesome);
	window.customElements.define('converse-root', 				ConverseRoot);
}

setupWebComponents();	