class ConverseRoot extends HTMLElement{
  constructor(id){
    super();
	if (!id) id = "converse-root";
	this.loadTemplate(id);
 } 
 
 async loadTemplate(id) {
	const response = await fetch("./templates/" + id + ".html");
	const html = await response.text();
	console.debug("loadTemplate", id);
	this.template = document.createElement('template');
	this.template.innerHTML = html;	
	document.querySelector(id).appendChild(this.template.content.cloneNode(true));		
 }
}

class ConverseFontAwesome extends ConverseRoot{
  constructor(){  
    super('converse-fontawesome');			
 } 
}

class ConverseMucSidebar extends ConverseRoot{
  constructor(){  
    super('converse-muc-sidebar');		
 } 
}

class ConverseMucBottomPanel extends ConverseRoot{
  constructor(){  
    super('converse-muc-bottom-panel');		
 } 
}

class ConverseChatContent extends ConverseRoot{
  constructor(){  
    super('converse-chat-content');		
 } 
}

class ConverseMucChatArea extends ConverseRoot{
  constructor(){  
    super('converse-muc-chatarea');		
 } 
}

class ConverseMucHeading extends ConverseRoot{
  constructor(){  
    super('converse-muc-heading');		
 } 
}

class ConverseMuc extends ConverseRoot{
  constructor(){  
    super('converse-muc');		
 } 
}

class ConverseControlBox extends ConverseRoot{
  constructor(){  
    super('converse-controlbox');		
 } 
}


window.customElements.define('converse-fontawesome', ConverseFontAwesome);
window.customElements.define('converse-muc-sidebar', ConverseMucSidebar);
window.customElements.define('converse-muc-bottom-panel', ConverseMucBottomPanel);
window.customElements.define('converse-muc-heading', ConverseMucHeading);
window.customElements.define('converse-chat-content', ConverseChatContent);
window.customElements.define('converse-muc-chatarea', ConverseMucChatArea);
window.customElements.define('converse-controlbox', ConverseControlBox);
window.customElements.define('converse-muc', ConverseMuc);
window.customElements.define('converse-root', ConverseRoot);