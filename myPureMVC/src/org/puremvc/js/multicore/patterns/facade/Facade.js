function Facade(key)
{
	if(Facade.instanceMap[key] != null)
	{
		throw new Error(Facade.MULTITON_MSG);
	}
	
	this.initializeNotifier(key);
	Facade.instanceMap[key] = this;
	this.initializeFacade();
};


Facade.prototype.initializeFacade = function()
{
	this.initializeModel();
	this.initializeController();
	this.initializeView();
};


Facade.getInstance = function(key)
{
	if (null == key)
		return null;
	if(Facade.instanceMap[key] == null)
	{
		Facade.instanceMap[key] = new Facade(key);
	}
	
	return Facade.instanceMap[key];
};


Facade.prototype.initializeModel = function(){};


Facade.prototype.initializeController = function(){};


Facade.prototype.initializeView = function(){};

Facade.prototype.initializeNotifier = function (key){};




Facade.prototype.controller = null;

Facade.prototype.model = null;

Facade.prototype.view = null;

Facade.prototype.multitonKey = null;

Facade.instanceMap = [];

Facade.MULTITON_MSG = "Facade instance for this Multiton key already constructed!";