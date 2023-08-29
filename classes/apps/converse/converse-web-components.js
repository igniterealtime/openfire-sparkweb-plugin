class ConverseAbstract extends HTMLElement{
	constructor(name){
		super();
		this.loadTemplate(name);
	} 
 
	async loadTemplate(name) {
		const response = await fetch("./templates/" + name + ".html");
		const html = await response.text();
		console.debug("loadTemplate", name);
		this.template = document.createElement('template');
		this.template.innerHTML = html;	
		this.appendChild(this.template.content.cloneNode(true));		
	}
}

class ConverseRoot extends ConverseAbstract{
	constructor(){  
		super('converse-root');			
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

class ConverseControlBox extends ConverseAbstract{
	constructor(){  
		super('converse-controlbox');		
	} 
}


window.customElements.define('converse-fontawesome', 		ConverseFontAwesome);
window.customElements.define('converse-muc-sidebar', 		ConverseMucSidebar);
window.customElements.define('converse-muc-bottom-panel', 	ConverseMucBottomPanel);
window.customElements.define('converse-muc-heading', 		ConverseMucHeading);
window.customElements.define('converse-chat-content', 		ConverseChatContent);
window.customElements.define('converse-muc-chatarea', 		ConverseMucChatArea);
window.customElements.define('converse-controlbox', 		ConverseControlBox);
window.customElements.define('converse-muc', 				ConverseMuc);
window.customElements.define('converse-root', 				ConverseRoot);