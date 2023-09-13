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

class ConverseControlBoxNavback extends ConverseAbstract{
	constructor(){  
		super('converse-controlbox-navback');		
	} 
}

class ConverseUserProfile extends ConverseAbstract{
	constructor(){  
		super('converse-user-profile');		
	} 	
}

class ConverseControlBox extends ConverseAbstract{
	constructor(){  
		super('converse-controlbox');		
	} 
	
	
	connectedCallback() {
		this.announceDiv = this.querySelector("#headline");
		this.roomDiv = this.querySelector("#chatrooms");
		this.chatDiv = this.querySelector("#converse-roster");
		this.activeDiv = this.querySelector("#active-conversations");		
		this.changeViewButton = this.querySelector('.pade-active-conversations.controlbox-heading__btn');
		
		this.changeViewButton.addEventListener('click', ev => this.changeView(ev));
	}

	changeView(ev) {
		ev.preventDefault();
		
		if (this.roomDiv.getAttribute("hidden")) {
			this.roomDiv.removeAttribute("hidden");
			this.chatDiv.removeAttribute("hidden");
			this.announceDiv.removeAttribute("hidden");	
			this.activeDiv.setAttribute("hidden", true);			
		} else {
			this.activeDiv.removeAttribute("hidden");				
			this.roomDiv.setAttribute("hidden", true);
			this.chatDiv.setAttribute("hidden", true);
			this.announceDiv.setAttribute("hidden", true);				
		}

	}	
	
	disconnectedCallback() {
		this.changeViewButton.removeEventListener('click', this.changeView);
		super.disconnectedCallback();
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

class ConverseChatToolbar extends ConverseAbstract{
	constructor(){  
		super('converse-chat-toolbar');		
	} 
}

class ConverseChats extends ConverseAbstract{
	constructor(){  
		super('converse-chats');		
	} 
}

class ConverseDropDown extends HTMLElement{
	constructor(){  
		super();		
		console.debug("ConverseDropDown - constructor", this.getAttribute("class"));
	}

	connectedCallback() {
		this.clickOutside = ev => this._clickOutside(ev);
		document.addEventListener('click', this.clickOutside);

		this.menu = this.querySelector('.dropdown-menu');
		this.button = this.querySelector('button');
		this.addEventListener('click', ev => this.toggleMenu(ev));
		this.addEventListener('keyup', ev => this.handleKeyUp(ev));
	}
	
	_clickOutside(ev) {
		if (!this.contains(ev.composedPath()[0])) {
		  this.hideMenu(ev);
		}
	}
	
	hideMenu() {
		this.menu.classList.remove('show');		
		this.button?.setAttribute('aria-expanded', false);
		this.button?.blur();
	}
	
	showMenu() {
		this.menu.classList.add('show');		
		this.button.setAttribute('aria-expanded', true);
	}
	
	toggleMenu(ev) {
		ev.preventDefault();
		
		if (this.menu.classList.contains('show')) {
		  this.hideMenu();
		} else {
		  this.showMenu();
		}
	}
	
	handleKeyUp(ev) {
		if (ev.keyCode === 27) {
		  this.hideMenu();
		}
	}
	
	disconnectedCallback() {
		document.removeEventListener('click', this.clickOutside);
		super.disconnectedCallback();
	}
}

class ConverseRichText extends HTMLElement{
	constructor(){  
		super();
		console.debug("ConverseRichText - constructor", this.getAttribute("text"));		
	}

	connectedCallback() {		
		const urlify = (text) => {
			var urlRegex = /(https?:\/\/[^\s]+)/g;
			return text.replace(urlRegex, '<a target="_blank" href="$1">$1</a>');
		}		
		
		this.innerHTML = urlify(this.getAttribute("text"));
	}	
}
	
async function externalStringLiterals(string, variables) {
	// await externalStringLiterals("${v1} and ${v2}", {v1: 'dele', v2: 'wale'});
	
	new Promise(finish => {
	  let templateString = string;
	  let templates = [...string.matchAll(/\$\{(\w*)}/g)];
	  templates.forEach((str, index) => {
		console.log(`Replacing template item ${str[0]} @ ${str[1]} with ${variables[str[1]]}`, str, str[1], variables);
		templateString = templateString.replace(str[0], variables[str[1]]);
		index >= templates.length - 1 ? finish(templateString) : '';
	  });
	  finish(string);
	})
}

async function setupWebComponents() {
	const response = await fetch("./web-components.html");
	const html = await response.text();
	const template = document.createElement('div');
	template.innerHTML = html;	
	document.body.appendChild(template);	

	window.customElements.define('converse-user-profile', 		ConverseUserProfile);	
	window.customElements.define('converse-controlbox-navback', ConverseControlBoxNavback);
	window.customElements.define('converse-controlbox', 		ConverseControlBox);
	window.customElements.define('converse-dragresize', 		ConverseDragResize);

	window.customElements.define('converse-chat-toolbar', 		ConverseChatToolbar);
	window.customElements.define('converse-chat-bottom-panel', 	ConverseChatBottomPanel);	
	window.customElements.define('converse-chat-heading', 		ConverseChatHeading);
	window.customElements.define('converse-chat-content', 		ConverseChatContent);
	window.customElements.define('converse-chat', 				ConverseChat);		
	
	window.customElements.define('converse-muc-sidebar', 		ConverseMucSidebar);	
	window.customElements.define('converse-muc-bottom-panel', 	ConverseMucBottomPanel);
	window.customElements.define('converse-muc-heading', 		ConverseMucHeading);
	window.customElements.define('converse-muc-chatarea', 		ConverseMucChatArea);
	window.customElements.define('converse-muc', 				ConverseMuc);

	window.customElements.define('converse-rich-text', 			ConverseRichText);
	window.customElements.define('converse-dropdown', 			ConverseDropDown);	
	window.customElements.define('converse-chats', 				ConverseChats);	
	window.customElements.define('converse-fontawesome', 		ConverseFontAwesome);
	window.customElements.define('converse-root', 				ConverseRoot);
}

setupWebComponents();	