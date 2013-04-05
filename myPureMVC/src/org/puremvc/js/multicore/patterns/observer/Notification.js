function Notification(name, body, type)
{
	this.name = name;
	this.body = body;
	this.type = type;
};


Notification.prototype.getName = function()
{
	return this.name;
};

Notification.prototype.setBody = function(body)
{
	this.body = body;
};

Notification.prototype.getBody = function()
{
	return this.body;
};


Notification.prototype.setType = function(type)
{
	this.type = type;
};

Notification.prototype.getType = function()
{
	return this.type;
};


Notification.prototype.toString = function()
{
	var msg = "Notification Name : " + this.getName();
	msg += "\nBody : " + ((this.body == null) ? "null" : this.body.toString());
	msg += "\nType : " + ((this.type == null) ? "null" : this.type.toString());
	return msg;
};


Notification.prototype.name = null;


Notification.prototype.body = null;


Notification.prototype.type = null;