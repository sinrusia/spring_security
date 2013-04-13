function Notifier () {};


Notifier.prototype.sendNotification = function(notificationName, body, type)
{
	var facade = this.getFacade();
	if(facade)
	{
		facade.sendNotification(notificationName, body, type);
	}
};


Notifier.prototype.initializeNotifier = function(key)
{
	this.multitonKey = String(key);
	this.facade = this.getFacade();
};


Notifier.prototype.getFacade = function()
{
	if(this.multitonKey == null)
	{
		throw new Error(Notifier.MULTITON_MSG);
	}
	
	return Facade.getInstance(this.multitonKey);
};


Notifier.prototype.facade;


Notifier.prototype.multitonKey = null;


Notifier.MULTITON_MSG = "multitonKey for this Notifier not yet initialized!";