function Observer (notifyMethod, notifyContext)
{
	this.setNotifyMethod(notifyMethod);
	this.setNotifyContext(notifyContext);

};


Observer.prototype.setNotifyMethod = function(notifyMethod)
{
	this.notify = notifyMethod;

};

Observer.prototype.getNotifyMethod = function()
{
	return this.notify;
};

Observer.prototype.setNotifyContext = function(nytifyContext)
{
	this.context = notifyContext;
};

Observer.prototype.getNotifyContext = function()
{
	return this.context;
}


Observer.prototype.notify = null;


Observer.prototype.context = null;